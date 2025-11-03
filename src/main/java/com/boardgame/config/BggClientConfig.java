package com.boardgame.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration //config pour les beans
@Slf4j
public class BggClientConfig {

    //objet à instancier et stocker dans le contexte d’application :
    //Permet d’injecter ensuite WebClient partout avec @Autowired
    @Bean
    public WebClient bggWebClient(
            //@Value : Injecte une valeur de configuration depuis application.properties ou application.yml
            @Value("${bgg.base-url}") String baseUrl,
            @Value("${bgg.connect-timeout-ms:3000}") int connectTimeoutMs,
            @Value("${bgg.read-timeout-ms:5000}") int readTimeoutMs
    ) {log.info(baseUrl);
        //Reactor Netty permet un contrôle fin sur le bas niveau (timeouts, connexion…)
        HttpClient httpClient = HttpClient.create()
                //Option : Si le serveur ne répond pas lors de l’établissement de connexion → abandonne au bout de X ms
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                //Si la réponse prend trop de temps → on coupe
                .responseTimeout(Duration.ofMillis(readTimeoutMs))
                //Sécurise encore plus la lecture. Coupe si aucun octet n’arrive pendant X ms.
                // Plus bas niveau que responseTimeout
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS)))
                .wiretap(true);

        //baseUrl : toutes les requêtes relatives partent d’ici
        //clientConnector : permet à WebClient d’utiliser ton HttpClient personnalisé
        return WebClient.builder()
                //TODO : 401 error a corriger
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .defaultHeader("Accept", "application/xml,text/xml,*/*")
                .defaultHeader("Accept-Language", "en-US,en;q=0.9")
                .defaultHeader("Accept-Encoding", "gzip, deflate, br")
                .defaultHeader("Connection", "keep-alive")
                .filter((request, next) -> {
                    log.info("==> BGG API Call: {} {}", request.method(), request.url());
                    return next.exchange(request)
                            .doOnNext(response -> log.info("<== BGG API Response: {}", response.statusCode()));
                })
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // augmente le buffer si nécessaire (grosses réponses BGG)
                //Par défaut, WebClient ne peut pas lire de grosses réponses
                //L’API BGG renvoie parfois du XML très volumineux. On étend à ~8Mo
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(8 * 1024 * 1024))
                        .build())
                .build();
    }
}
