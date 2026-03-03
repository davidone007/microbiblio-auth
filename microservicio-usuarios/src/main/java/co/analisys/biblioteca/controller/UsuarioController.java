package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.Email;
import co.analisys.biblioteca.model.Usuario;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios: consulta de perfil y actualización de datos de contacto")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Retorna la información completa de un usuario registrado en la biblioteca, incluyendo nombre, email y dirección. Accesible por ROLE_USER, ROLE_LIBRARIAN y ROLE_ADMIN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes", content = @Content),
        @ApiResponse(responseCode = "500", description = "Usuario no encontrado o error interno", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN','ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/{id}")
    public Usuario obtenerUsuario(
            @Parameter(description = "ID único del usuario (ej: 1)", required = true, example = "1")
            @PathVariable String id) {
        return usuarioService.obtenerUsuario(new UsuarioId(id));
    }

    @Operation(
        summary = "Cambiar email de un usuario",
        description = "Actualiza la dirección de correo electrónico de un usuario. El email debe tener un formato válido. Solo accesible por ROLE_ADMIN y ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Email actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Formato de email inválido", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Requiere rol ROLE_ADMIN o ROLE_LIBRARIAN", content = @Content),
        @ApiResponse(responseCode = "500", description = "Usuario no encontrado o error interno", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LIBRARIAN')")
    @PutMapping("/{id}/email")
    public void cambiarEmail(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable String id,
            @Parameter(description = "Nueva dirección de email (debe ser un email válido)", required = true, example = "nuevo@email.com")
            @RequestBody String nuevoEmail) {
        usuarioService.cambiarEmailUsuario(new UsuarioId(id), new Email(nuevoEmail));
    }
}
