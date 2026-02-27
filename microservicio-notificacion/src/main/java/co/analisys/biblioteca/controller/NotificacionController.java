package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.dto.NotificacionDTO;
import co.analisys.biblioteca.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificar")
@Tag(name = "Notificaciones", description = "Envío de notificaciones y alertas a los usuarios de la biblioteca")
@SecurityRequirement(name = "bearerAuth")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @Operation(
        summary = "Enviar notificación a un usuario",
        description = "Envía un mensaje de notificación a un usuario registrado en la biblioteca. " +
                "Útil para alertas de vencimiento de préstamos, confirmaciones y comunicados. " +
                "Solo accesible por ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación enviada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la notificación inválidos o incompletos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Requiere rol ROLE_LIBRARIAN", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping
    public void enviarNotificacion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la notificación: ID del destinatario y texto del mensaje",
                required = true,
                content = @Content(schema = @Schema(implementation = NotificacionDTO.class))
            )
            @RequestBody NotificacionDTO notificacion) {
        notificacionService.enviarNotificacion(notificacion);
    }
}