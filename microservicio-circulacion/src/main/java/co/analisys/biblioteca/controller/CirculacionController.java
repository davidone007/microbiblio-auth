package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.model.Prestamo;
import co.analisys.biblioteca.model.PrestamoId;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.CirculacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/circulacion")
@Tag(name = "Circulación", description = "Gestión de préstamos y devoluciones de libros de la biblioteca")
@SecurityRequirement(name = "bearerAuth")
public class CirculacionController {
    @Autowired
    private CirculacionService circulacionService;

    @Operation(
        summary = "Registrar préstamo de un libro",
        description = "Crea un nuevo préstamo asignando el libro especificado al usuario indicado. " +
                "Verifica disponibilidad contra el catálogo antes de crear el préstamo. Solo accesible por ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Préstamo registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El libro no está disponible para préstamo", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Requiere rol ROLE_LIBRARIAN", content = @Content),
        @ApiResponse(responseCode = "500", description = "Usuario o libro no encontrado, o servicio de catálogo no disponible", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/prestar")
    public void prestarLibro(
            @Parameter(description = "ID del usuario que recibe el préstamo", required = true, example = "1")
            @RequestParam String usuarioId,
            @Parameter(description = "ID del libro a prestar", required = true, example = "1")
            @RequestParam String libroId) {
        circulacionService.prestarLibro(new UsuarioId(usuarioId), new LibroId(libroId));
    }

    @Operation(
        summary = "Registrar devolución de un libro",
        description = "Procesa la devolución de un préstamo activo, marcándolo como devuelto y liberando el libro en el catálogo. Solo accesible por ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Devolución registrada exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Requiere rol ROLE_LIBRARIAN", content = @Content),
        @ApiResponse(responseCode = "500", description = "Préstamo no encontrado o ya devuelto", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/devolver")
    public void devolverLibro(
            @Parameter(description = "ID único del préstamo a devolver (UUID)", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam String prestamoId) {
        circulacionService.devolverLibro(new PrestamoId(prestamoId));
    }

    @Operation(
        summary = "Listar todos los préstamos",
        description = "Retorna la lista completa de préstamos registrados en el sistema (activos, devueltos y vencidos). Accesible por ROLE_USER y ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Prestamo.class)))),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN','ROLE_USER')")
    @GetMapping("/prestamos")
    public List<Prestamo> obtenerTodosPrestamos() {
        return circulacionService.obtenerTodosPrestamos();
    }
}
