package com.ramiletus.frauddetection.service.location;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class LocationServiceImpl implements LocationService {

    private final WebClient webClient;



    public LocationServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://ip-api.com").build();
    }

    // Método para obtener la ubicación desde la IP
    public LocationData getLocationFromIp(String ip) {
        try {
            // Consumo de la API externa para obtener la ubicación
            return webClient.get()
                    .uri("/json/{ip}?fields=184319", ip)
                    .retrieve()
                    .bodyToMono(LocationData.class)
                    .block(); // Para obtener el resultado sincrónicamente
        } catch (WebClientResponseException e) {
            // Manejo de errores en la llamada HTTP
            System.err.println("Error al obtener la ubicación para la IP: " + ip + " - " + e.getMessage());
            return null; // Manejar errores adecuadamente en un entorno real
        }
    }
}
