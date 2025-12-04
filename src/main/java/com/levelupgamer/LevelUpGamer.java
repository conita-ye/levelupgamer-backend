package com.levelupgamer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
    info = @Info(
        title = "Level-Up Gamer Store API",
        version = "1.0.0",
        description = "API REST para la tienda online Level-Up Gamer. Sistema de e-commerce especializado en productos gaming con gamificación, sistema de puntos y gestión de inventario."
    )
)
@SpringBootApplication
@EnableJpaAuditing
public class LevelUpGamer {

	public static void main(String[] args) {
		SpringApplication.run(LevelUpGamer.class, args);
		System.out.println("╔════════════════════════════════════════╗");
		System.out.println("║   Level-Up Gamer Store iniciado       ║");
		System.out.println("║   Puerto: 8081                        ║");
		System.out.println("╚════════════════════════════════════════╝");
	}

}
