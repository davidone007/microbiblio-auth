package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.Libro;
import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.service.CatalogoService;
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
@RequestMapping("/libros")
@Tag(name = "Catálogo", description = "Gestión del catálogo de libros: consulta, búsqueda y control de disponibilidad")
@SecurityRequirement(name = "bearerAuth")
public class CatalogoController {
    private final CatalogoService catalogoService;

    @Autowired
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @Operation(
        summary = "Obtener libro por ID",
        description = "Retorna los detalles completos de un libro dado su identificador único. Requiere rol ROLE_USER o ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes", content = @Content),
        @ApiResponse(responseCode = "500", description = "Libro no encontrado o error interno", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN','ROLE_USER')")
    @GetMapping("/{id}")
    public Libro obtenerLibro(
            @Parameter(description = "ID único del libro (ej: 1, 2)", required = true, example = "1")
            @PathVariable String id) {
        return catalogoService.obtenerLibro(new LibroId(id));
    }

    @Operation(
        summary = "Verificar disponibilidad de un libro",
        description = "Retorna true si el libro está disponible para préstamo, false en caso contrario. Requiere rol ROLE_USER o ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de disponibilidad del libro",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN','ROLE_USER')")
    @GetMapping("/{id}/disponible")
    public boolean isLibroDisponible(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable String id) {
        Libro libro = catalogoService.obtenerLibro(new LibroId(id));
        return libro != null && libro.isDisponible();
    }

    @Operation(
        summary = "Actualizar disponibilidad de un libro",
        description = "Cambia el estado de disponibilidad de un libro (disponible/no disponible). Solo accesible por ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Requiere rol ROLE_LIBRARIAN", content = @Content),
        @ApiResponse(responseCode = "500", description = "Libro no encontrado o error interno", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("/{id}/disponibilidad")
    public void actualizarDisponibilidad(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable String id,
            @Parameter(description = "true para marcar disponible, false para no disponible", required = true)
            @RequestBody boolean disponible) {
        catalogoService.actualizarDisponibilidad(new LibroId(id), disponible);
    }

    @Operation(
        summary = "Buscar libros por criterio",
        description = "Busca libros cuyo título o categoría contengan el criterio especificado (búsqueda parcial, insensible a mayúsculas). Requiere rol ROLE_USER o ROLE_LIBRARIAN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de libros que coinciden con el criterio",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Libro.class)))),
        @ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN','ROLE_USER')")
    @GetMapping("/buscar")
    public List<Libro> buscarLibros(
            @Parameter(description = "Texto a buscar en título o categoría del libro", required = true, example = "soledad")
            @RequestParam String criterio) {
        return catalogoService.buscarLibros(criterio);
    }
}
