package tech.springboot.websocketClientApp;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;


import reactor.core.publisher.Mono;

@SpringBootApplication
public class WebsocketClientAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketClientAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner run () {
		return args -> {
			var client = new ReactorNettyWebSocketClient();
			client.execute(
							URI.create("ws://localhost:8080/ws/messages"),
							session -> session.send(
											Mono.just(session.textMessage("server-message")))
									.thenMany(session.receive()
																.map(WebSocketMessage::getPayloadAsText)
																.log())
									.then())
					.block(Duration.ofSeconds(10L));
		};
	}
}
