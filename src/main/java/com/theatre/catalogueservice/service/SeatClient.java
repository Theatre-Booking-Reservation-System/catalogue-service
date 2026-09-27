package com.theatre.catalogueservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SeatClient {

    private final RestClient restClient;

    public SeatClient(RestClient.Builder restClientBuilder,
                      @Value("${clients.seat.base-url:http://localhost:8083/seat-service}")
                      String seatBaseUrl) {
        this.restClient = restClientBuilder.baseUrl(seatBaseUrl).build();
    }

    public long getTotalSeats(String bearerToken) {
        try {
            SeatCountPayload payload = restClient.get()
                    .uri("/seats/count")
                    .headers(headers -> {
                        if (bearerToken != null && !bearerToken.isBlank()) {
                            headers.set(HttpHeaders.AUTHORIZATION, bearerToken);
                        }
                    })
                    .retrieve()
                    .body(SeatCountPayload.class);

            return payload != null ? payload.total() : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }

    private record SeatCountPayload(long total) {
    }
}
