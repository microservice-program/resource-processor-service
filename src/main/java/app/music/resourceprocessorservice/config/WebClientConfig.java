package app.music.resourceprocessorservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    private static final Integer TIME_OUT = 5000;
    private static final Integer READ_TIME_OUT = 10000;
    private static final Integer WRITE_TIME_OUT = 10000;
    private static final Integer RESPONSE_TIME = 50000;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIME_OUT)
                .responseTimeout(Duration.ofMillis(RESPONSE_TIME))
                .doOnConnected(connection ->
                        connection
                                .addHandlerFirst(new ReadTimeoutHandler(READ_TIME_OUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIME_OUT, TimeUnit.MILLISECONDS))
                );
    }


    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
