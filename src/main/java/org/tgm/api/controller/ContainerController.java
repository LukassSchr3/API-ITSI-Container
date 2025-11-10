package org.tgm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tgm.api.dto.ApiResponse;
import org.tgm.api.dto.ContainerDTO;
import org.tgm.api.service.ContainerService;

import java.util.List;

@RestController
@RequestMapping("/api/containers")
@RequiredArgsConstructor
@Tag(name = "Container Management", description = "API Endpoints für Container-Verwaltung (Start, Stop, Status)")
public class ContainerController {
    private final ContainerService containerService;

    @Operation(
            summary = "Container starten",
            description = "Startet einen neuen Container für die angegebene UserID und AufgabenID"
    )
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<ContainerDTO>> startContainer(
            @Parameter(description = "ID des Users", required = true) @RequestParam int userId,
            @Parameter(description = "ID der Aufgabe", required = true) @RequestParam int aufgabenId) {
        ContainerDTO container = containerService.startContainer(userId, aufgabenId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Container erfolgreich gestartet", container));
    }

    @Operation(
            summary = "Container stoppen",
            description = "Stoppt den Container für die angegebene UserID und AufgabenID"
    )
    @PostMapping("/stop")
    public ResponseEntity<ApiResponse<Void>> stopContainer(
            @Parameter(description = "ID des Users", required = true) @RequestParam int userId,
            @Parameter(description = "ID der Aufgabe", required = true) @RequestParam int aufgabenId) {
        containerService.stopContainer(userId, aufgabenId);
        return ResponseEntity.ok(ApiResponse.success("Container erfolgreich gestoppt", null));
    }

    @Operation(
            summary = "Alle Container eines Users abrufen",
            description = "Gibt alle Container für einen bestimmten User zurück"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ContainerDTO>>> getUserContainers(
            @Parameter(description = "ID des Users", required = true) @PathVariable int userId) {
        List<ContainerDTO> containers = containerService.getUserContainers(userId);
        return ResponseEntity.ok(ApiResponse.success(containers));
    }

    @Operation(
            summary = "Container für User und Aufgabe abrufen",
            description = "Gibt den spezifischen Container für UserID und AufgabenID zurück"
    )
    @GetMapping("/user/{userId}/aufgabe/{aufgabenId}")
    public ResponseEntity<ApiResponse<ContainerDTO>> getContainerByUserAndAufgabe(
            @Parameter(description = "ID des Users", required = true) @PathVariable int userId,
            @Parameter(description = "ID der Aufgabe", required = true) @PathVariable int aufgabenId) {
        ContainerDTO container = containerService.getContainerByUserAndAufgabe(userId, aufgabenId);
        return ResponseEntity.ok(ApiResponse.success(container));
    }

    @Operation(
            summary = "Container-Details abrufen",
            description = "Gibt Details für einen Container anhand der Container-ID zurück"
    )
    @GetMapping("/{containerId}")
    public ResponseEntity<ApiResponse<ContainerDTO>> getContainerDetails(
            @Parameter(description = "ID des Containers", required = true) @PathVariable String containerId) {
        ContainerDTO container = containerService.getContainerDetails(containerId);
        return ResponseEntity.ok(ApiResponse.success(container));
    }

    @Operation(
            summary = "Container löschen",
            description = "Löscht den Container für die angegebene UserID und AufgabenID"
    )
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteContainer(
            @Parameter(description = "ID des Users", required = true) @RequestParam int userId,
            @Parameter(description = "ID der Aufgabe", required = true) @RequestParam int aufgabenId) {
        containerService.deleteContainer(userId, aufgabenId);
        return ResponseEntity.ok(ApiResponse.success("Container erfolgreich gelöscht", null));
    }

    @Operation(
            summary = "Container neu starten",
            description = "Startet den Container für die angegebene UserID und AufgabenID neu"
    )
    @PostMapping("/restart")
    public ResponseEntity<ApiResponse<Void>> restartContainer(
            @Parameter(description = "ID des Users", required = true) @RequestParam int userId,
            @Parameter(description = "ID der Aufgabe", required = true) @RequestParam int aufgabenId) {
        containerService.restartContainer(userId, aufgabenId);
        return ResponseEntity.ok(ApiResponse.success("Container erfolgreich neu gestartet", null));
    }

    @Operation(
            summary = "Container pausieren",
            description = "Pausiert den Container für die angegebene UserID und AufgabenID"
    )
    @PostMapping("/pause")
    public ResponseEntity<ApiResponse<Void>> pauseContainer(
            @Parameter(description = "ID des Users", required = true) @RequestParam int userId,
            @Parameter(description = "ID der Aufgabe", required = true) @RequestParam int aufgabenId) {
        containerService.pauseContainer(userId, aufgabenId);
        return ResponseEntity.ok(ApiResponse.success("Container erfolgreich pausiert", null));
    }

    @Operation(
            summary = "Container fortsetzen",
            description = "Setzt den pausierten Container für die angegebene UserID und AufgabenID fort"
    )
    @PostMapping("/unpause")
    public ResponseEntity<ApiResponse<Void>> unpauseContainer(
            @Parameter(description = "ID des Users", required = true) @RequestParam int userId,
            @Parameter(description = "ID der Aufgabe", required = true) @RequestParam int aufgabenId) {
        containerService.unpauseContainer(userId, aufgabenId);
        return ResponseEntity.ok(ApiResponse.success("Container erfolgreich fortgesetzt", null));
    }
}

