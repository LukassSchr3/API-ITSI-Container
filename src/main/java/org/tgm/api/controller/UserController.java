package org.tgm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tgm.api.dto.ApiResponse;
import org.tgm.api.dto.CreateUserRequest;
import org.tgm.api.dto.UpdateUserRequest;
import org.tgm.api.dto.UserDTO;
import org.tgm.api.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API Endpoints für Benutzerverwaltung")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Neuen Benutzer erstellen",
            description = "Erstellt einen neuen Benutzer im System"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody CreateUserRequest request) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Benutzer erfolgreich erstellt", user));
    }

    @Operation(
            summary = "Benutzer nach ID abrufen",
            description = "Gibt die Details eines Benutzers anhand seiner ID zurück"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @Parameter(description = "ID des Benutzers", required = true) @PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(
            summary = "Alle Benutzer abrufen",
            description = "Gibt eine Liste aller Benutzer im System zurück"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @Operation(
            summary = "Benutzer aktualisieren",
            description = "Aktualisiert die Daten eines bestehenden Benutzers"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @Parameter(description = "ID des Benutzers", required = true) @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        UserDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("Benutzer erfolgreich aktualisiert", user));
    }

    @Operation(
            summary = "Benutzer löschen",
            description = "Löscht einen Benutzer aus dem System"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "ID des Benutzers", required = true) @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Benutzer erfolgreich gelöscht", null));
    }
}

