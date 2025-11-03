package com.boardgame.integration.bgg;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

@Component //composant Spring
@RequiredArgsConstructor // Génère automatiquement un constructeur avec tous les champs final en paramètres.
@Slf4j
public class BggClient {

    // Reçu depuis BggClientConfig. Effectue les appels HTTP vers BGG
    // Injection de dépendance par constructeur.
    private final WebClient bggWebClient;
    //Mapper Jackson spécialisé pour lire le XML. Alternative à ObjectMapper classique
    //Recyclé par chaque appel (pas recréé)
    private final XmlMapper xmlMapper = new XmlMapper();
    /**
     * Appelle /thing?id=...&stats=1 et renvoie l'XML brut.
     */
    public Mono<String> fetchThingXml(long bggId) {
        return bggWebClient.get()
            // https://api.geekdo.com/xmlapi2/thing?id=174430&stats=1
            .uri(uriBuilder -> uriBuilder
                    .path("/thing")
                    .queryParam("id", bggId)
                    .queryParam("stats", 1)
                    .build())
            //Ajoute un header HTTP. attend une réponse XML
            .accept(MediaType.APPLICATION_XML)
                //TODO : 401 error a corriger
            //Envoie la requête et prépare la lecture de la réponse. 4xx & 5xx → erreurs gérées par défaut
            .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> {
                            log.error("BGG API error: status code {}", response.statusCode().value());
                            return response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(
                                            new BggApiException("BGG API returned status " + response.statusCode().value() + ": " + body)
                                    ));
                        }
                )
            //Demande à WebClient : lis le corps et renvoie-le sous forme de chaîne (XML brut).(flux réactif à un seul élément)
            .bodyToMono(String.class)
                .doOnNext(xml -> log.debug("Successfully fetched XML for BGG ID: {}", bggId))
                .doOnError(e -> log.error("Error fetching BGG ID {}: {}", bggId, e.getMessage()));
    }

    /**
     * Transforme l'XML BGG en JsonNode (structure générique).
     */
    public Mono<JsonNode> fetchThingAsJson(long bggId) {
        return fetchThingXml(bggId)
            //pour les transformation légère : petit volume
            .map(xml -> {
                try {
                    //Convertit l’arbre XML en un JsonNode Jackson générique
                    return xmlMapper.readTree(xml); // XML -> JsonNode
                } catch (Exception e) {
                    throw new BggXmlParseException("Failed to parse BGG XML", e);
                }
            });
        //// Variante alternative plus idiomatique WebFlux (flatMap + fromCallable).
        //// Gardée pour référence.
        //// Pour des transformations lourdes : parsing volumétrique ou coût CPU important.
        //// Mono : type réactif Project Reactor représentant 0 ou 1 élément asynchrone.
        //return fetchThingXml(bggId)
        //        .flatMap(xml -> Mono.fromCallable(() -> xmlMapper.readTree(xml)))
        //        .onErrorMap(Exception.class, e ->
        //                new BggXmlParseException("Failed to parse BGG XML", e));
        //// Variante pour parsing lourd ou volumétrique.
        //// fromCallable() : encapsule correctement une transformation synchrone pouvant lever une exception.
        //// subscribeOn(boundedElastic) : exécute la tâche sur un pool dédié hors de l'Event Loop Reactor,
        ////                               évitant de bloquer les threads réactifs pour des volumes importants.
        //// Schedulers : permet de choisir sur quel pool de threads s'exécutera le flux.
        //// boundedElastic : pool élastique, dimensionné pour des tâches potentiellement bloquantes.
/*      return fetchThingXml(bggId)
            .flatMap(xml -> Mono.fromCallable(() -> xmlMapper.readTree(xml))
            //ajouter pour traiter les tres gros volume (evite les blocages)
            .subscribeOn(Schedulers.boundedElastic()))
            .onErrorMap(e -> new BggXmlParseException("Failed to parse BGG XML", e));*/
    }
}
