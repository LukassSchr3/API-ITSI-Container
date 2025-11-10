package org.tgm.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.tgm.api.config.TestConfig;
import org.tgm.api.dto.ContainerDTO;
import org.tgm.api.exception.BackendException;
import org.tgm.api.service.ContainerService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContainerController.class)
@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
@DisplayName("ContainerController Tests")
class ContainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContainerService containerService;

    private ContainerDTO testContainer;
    private int testUserId;
    private int testAufgabenId;

    @BeforeEach
    void setUp() {
        testUserId = 1;
        testAufgabenId = 100;

        testContainer = new ContainerDTO();
        testContainer.setContainerId(123);
        testContainer.setContainerName("test-container");
        testContainer.setStatus("running");
        testContainer.setUserId(testUserId);
        testContainer.setAufgabenId(testAufgabenId);
        testContainer.setIpAddress("172.17.0.2");
        testContainer.setPort(8080);
    }

    @Test
    @DisplayName("POST /api/containers/start - Container erfolgreich starten")
    void testStartContainer_Success() throws Exception {
        // Given
        when(containerService.startContainer(testUserId, testAufgabenId))
                .thenReturn(testContainer);

        // When & Then
        mockMvc.perform(post("/api/containers/start")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Container erfolgreich gestartet"))
                .andExpect(jsonPath("$.data.containerId").value(123))
                .andExpect(jsonPath("$.data.containerName").value("test-container"))
                .andExpect(jsonPath("$.data.status").value("running"))
                .andExpect(jsonPath("$.data.userId").value(testUserId))
                .andExpect(jsonPath("$.data.aufgabenId").value(testAufgabenId))
                .andExpect(jsonPath("$.data.ipAddress").value("172.17.0.2"))
                .andExpect(jsonPath("$.data.port").value(8080));

        verify(containerService, times(1)).startContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("POST /api/containers/start - Fehlende Parameter")
    void testStartContainer_MissingParameters() throws Exception {
        // When & Then - Bei fehlenden required Parametern erwartet Spring Boot 400
        mockMvc.perform(post("/api/containers/start")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(containerService, never()).startContainer(anyInt(), anyInt());
    }

    @Test
    @DisplayName("POST /api/containers/start - Backend Fehler")
    void testStartContainer_BackendException() throws Exception {
        // Given
        when(containerService.startContainer(testUserId, testAufgabenId))
                .thenThrow(new BackendException("Backend nicht erreichbar"));

        // When & Then
        mockMvc.perform(post("/api/containers/start")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        verify(containerService, times(1)).startContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("POST /api/containers/stop - Container erfolgreich stoppen")
    void testStopContainer_Success() throws Exception {
        // Given
        doNothing().when(containerService).stopContainer(testUserId, testAufgabenId);

        // When & Then
        mockMvc.perform(post("/api/containers/stop")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Container erfolgreich gestoppt"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(containerService, times(1)).stopContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("GET /api/containers/user/{userId} - Alle Container eines Users abrufen")
    void testGetUserContainers_Success() throws Exception {
        // Given
        ContainerDTO container2 = new ContainerDTO();
        container2.setContainerId(456);
        container2.setContainerName("test-container-2");
        container2.setStatus("stopped");
        container2.setUserId(testUserId);
        container2.setAufgabenId(200);

        List<ContainerDTO> containers = Arrays.asList(testContainer, container2);
        when(containerService.getUserContainers(testUserId)).thenReturn(containers);

        // When & Then
        mockMvc.perform(get("/api/containers/user/{userId}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].containerId").value(123))
                .andExpect(jsonPath("$.data[1].containerId").value(456));

        verify(containerService, times(1)).getUserContainers(testUserId);
    }

    @Test
    @DisplayName("GET /api/containers/user/{userId}/aufgabe/{aufgabenId} - Spezifischen Container abrufen")
    void testGetContainerByUserAndAufgabe_Success() throws Exception {
        // Given
        when(containerService.getContainerByUserAndAufgabe(testUserId, testAufgabenId))
                .thenReturn(testContainer);

        // When & Then
        mockMvc.perform(get("/api/containers/user/{userId}/aufgabe/{aufgabenId}",
                        testUserId, testAufgabenId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.containerId").value(123))
                .andExpect(jsonPath("$.data.userId").value(testUserId))
                .andExpect(jsonPath("$.data.aufgabenId").value(testAufgabenId));

        verify(containerService, times(1))
                .getContainerByUserAndAufgabe(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("GET /api/containers/{containerId} - Container-Details abrufen")
    void testGetContainerDetails_Success() throws Exception {
        // Given
        String containerId = "123";
        when(containerService.getContainerDetails(containerId))
                .thenReturn(testContainer);

        // When & Then
        mockMvc.perform(get("/api/containers/{containerId}", containerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.containerId").value(123))
                .andExpect(jsonPath("$.data.containerName").value("test-container"));

        verify(containerService, times(1)).getContainerDetails(containerId);
    }

    @Test
    @DisplayName("DELETE /api/containers - Container erfolgreich löschen")
    void testDeleteContainer_Success() throws Exception {
        // Given
        doNothing().when(containerService).deleteContainer(testUserId, testAufgabenId);

        // When & Then
        mockMvc.perform(delete("/api/containers")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Container erfolgreich gelöscht"));

        verify(containerService, times(1)).deleteContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("POST /api/containers/restart - Container erfolgreich neu starten")
    void testRestartContainer_Success() throws Exception {
        // Given
        doNothing().when(containerService).restartContainer(testUserId, testAufgabenId);

        // When & Then
        mockMvc.perform(post("/api/containers/restart")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Container erfolgreich neu gestartet"));

        verify(containerService, times(1)).restartContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("POST /api/containers/pause - Container erfolgreich pausieren")
    void testPauseContainer_Success() throws Exception {
        // Given
        doNothing().when(containerService).pauseContainer(testUserId, testAufgabenId);

        // When & Then
        mockMvc.perform(post("/api/containers/pause")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Container erfolgreich pausiert"));

        verify(containerService, times(1)).pauseContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("POST /api/containers/unpause - Container erfolgreich fortsetzen")
    void testUnpauseContainer_Success() throws Exception {
        // Given
        doNothing().when(containerService).unpauseContainer(testUserId, testAufgabenId);

        // When & Then
        mockMvc.perform(post("/api/containers/unpause")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Container erfolgreich fortgesetzt"));

        verify(containerService, times(1)).unpauseContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("POST /api/containers/restart - Mit ungültiger UserID")
    void testRestartContainer_InvalidUserId() throws Exception {
        // When & Then - Bei ungültigen Parametern erwartet Spring Boot 400
        mockMvc.perform(post("/api/containers/restart")
                        .param("userId", "invalid")
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(containerService, never()).restartContainer(anyInt(), anyInt());
    }

    @Test
    @DisplayName("GET /api/containers/user/{userId} - Leere Liste zurückgeben")
    void testGetUserContainers_EmptyList() throws Exception {
        // Given
        when(containerService.getUserContainers(testUserId)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/containers/user/{userId}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(0)));

        verify(containerService, times(1)).getUserContainers(testUserId);
    }

    @Test
    @DisplayName("POST /api/containers/stop - Backend Exception")
    void testStopContainer_BackendException() throws Exception {
        // Given
        doThrow(new BackendException("Verbindung zum Backend fehlgeschlagen"))
                .when(containerService).stopContainer(testUserId, testAufgabenId);

        // When & Then
        mockMvc.perform(post("/api/containers/stop")
                        .param("userId", String.valueOf(testUserId))
                        .param("aufgabenId", String.valueOf(testAufgabenId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        verify(containerService, times(1)).stopContainer(testUserId, testAufgabenId);
    }

    @Test
    @DisplayName("GET /api/containers/user/{userId}/aufgabe/{aufgabenId} - Container nicht gefunden")
    void testGetContainerByUserAndAufgabe_NotFound() throws Exception {
        // Given
        when(containerService.getContainerByUserAndAufgabe(testUserId, testAufgabenId))
                .thenThrow(new BackendException("Container nicht gefunden"));

        // When & Then
        mockMvc.perform(get("/api/containers/user/{userId}/aufgabe/{aufgabenId}",
                        testUserId, testAufgabenId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        verify(containerService, times(1))
                .getContainerByUserAndAufgabe(testUserId, testAufgabenId);
    }
}

