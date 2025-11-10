package org.tgm.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.tgm.api.dto.BackendResponse;
import org.tgm.api.dto.ContainerDTO;
import org.tgm.api.exception.BackendException;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContainerService {
    private final WebClient webClient;

    /**
     * Container starten - UserID und AufgabenID an Backend senden
     */
    public ContainerDTO startContainer(int userId, int aufgabenId) {
        log.info("Starte Container für User {} und Aufgabe {}", userId, aufgabenId);

        try {
            BackendResponse<ContainerDTO> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/containers/start")
                            .queryParam("userId", userId)
                            .queryParam("aufgabenId", aufgabenId)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<ContainerDTO>>() {})
                    .timeout(Duration.ofSeconds(60))
                    .block();

            if (response != null && response.isSuccess()) {
                return response.getData();
            } else {
                throw new BackendException("Container konnte nicht gestartet werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Starten des Containers", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Container stoppen - UserID und AufgabenID an Backend senden
     */
    public void stopContainer(int userId, int aufgabenId) {
        log.info("Stoppe Container für User {} und Aufgabe {}", userId, aufgabenId);

        try {
            BackendResponse<Void> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/containers/stop")
                            .queryParam("userId", userId)
                            .queryParam("aufgabenId", aufgabenId)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<Void>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null || !response.isSuccess()) {
                throw new BackendException("Container konnte nicht gestoppt werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Stoppen des Containers", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Alle Container eines Users abrufen
     */
    public List<ContainerDTO> getUserContainers(int userId) {
        log.info("Rufe Container für User {} ab", userId);

        try {
            BackendResponse<List<ContainerDTO>> response = webClient.get()
                    .uri("/api/containers/user/{userId}", userId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<List<ContainerDTO>>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response != null && response.isSuccess()) {
                return response.getData();
            } else {
                throw new BackendException("Container konnten nicht abgerufen werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Abrufen der Container", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Container für User und Aufgabe abrufen
     */
    public ContainerDTO getContainerByUserAndAufgabe(int userId, int aufgabenId) {
        log.info("Rufe Container für User {} und Aufgabe {} ab", userId, aufgabenId);

        try {
            BackendResponse<ContainerDTO> response = webClient.get()
                    .uri("/api/containers/user/{userId}/aufgabe/{aufgabenId}", userId, aufgabenId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<ContainerDTO>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response != null && response.isSuccess()) {
                return response.getData();
            } else {
                throw new BackendException("Container konnte nicht abgerufen werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Abrufen des Containers", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Container-Details mit ContainerID abrufen
     */
    public ContainerDTO getContainerDetails(String containerId) {
        log.info("Rufe Details für Container {} ab", containerId);

        try {
            BackendResponse<ContainerDTO> response = webClient.get()
                    .uri("/api/containers/{containerId}", containerId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<ContainerDTO>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response != null && response.isSuccess()) {
                return response.getData();
            } else {
                throw new BackendException("Container-Details konnten nicht abgerufen werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Abrufen der Container-Details", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Container löschen - UserID und AufgabenID an Backend senden
     */
    public void deleteContainer(int userId, int aufgabenId) {
        log.info("Lösche Container für User {} und Aufgabe {}", userId, aufgabenId);

        try {
            BackendResponse<Void> response = webClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/containers")
                            .queryParam("userId", userId)
                            .queryParam("aufgabenId", aufgabenId)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<Void>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null || !response.isSuccess()) {
                throw new BackendException("Container konnte nicht gelöscht werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Löschen des Containers", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Container neu starten - UserID und AufgabenID an Backend senden
     */
    public void restartContainer(int userId, int aufgabenId) {
        log.info("Starte Container für User {} und Aufgabe {} neu", userId, aufgabenId);

        try {
            BackendResponse<Void> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/containers/restart")
                            .queryParam("userId", userId)
                            .queryParam("aufgabenId", aufgabenId)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<Void>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null || !response.isSuccess()) {
                throw new BackendException("Container konnte nicht neu gestartet werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Neustarten des Containers", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Container pausieren - UserID und AufgabenID an Backend senden
     */
    public void pauseContainer(int userId, int aufgabenId) {
        log.info("Pausiere Container für User {} und Aufgabe {}", userId, aufgabenId);

        try {
            BackendResponse<Void> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/containers/pause")
                            .queryParam("userId", userId)
                            .queryParam("aufgabenId", aufgabenId)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<Void>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null || !response.isSuccess()) {
                throw new BackendException("Container konnte nicht pausiert werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Pausieren des Containers", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }

    /**
     * Container fortsetzen - UserID und AufgabenID an Backend senden
     */
    public void unpauseContainer(int userId, int aufgabenId) {
        log.info("Setze Container für User {} und Aufgabe {} fort", userId, aufgabenId);

        try {
            BackendResponse<Void> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/containers/unpause")
                            .queryParam("userId", userId)
                            .queryParam("aufgabenId", aufgabenId)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BackendResponse<Void>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null || !response.isSuccess()) {
                throw new BackendException("Container konnte nicht fortgesetzt werden: " +
                        (response != null ? response.getMessage() : "Keine Antwort"));
            }
        } catch (Exception e) {
            log.error("Fehler beim Fortsetzen des Containers", e);
            throw new BackendException("Fehler bei der Kommunikation mit dem Backend: " + e.getMessage());
        }
    }
}

