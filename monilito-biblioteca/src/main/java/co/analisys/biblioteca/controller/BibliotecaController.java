package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.entity.Libro;
import co.analisys.biblioteca.service.BibliotecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {
    @Autowired
    private BibliotecaService bibliotecaService;

    @PostMapping("/prestar")
    public void prestarLibro(@RequestParam int usuarioId, @RequestParam int libroId) {
        bibliotecaService.prestarLibro(usuarioId, libroId);
    }

    @PostMapping("/devolver/{prestamoId}")
    public void devolverLibro(@PathVariable int prestamoId) {
        bibliotecaService.devolverLibro(prestamoId);
    }

    @GetMapping("/buscar")
    public List<Libro> buscarLibros(@RequestParam String criterio) {
        return bibliotecaService.buscarLibros(criterio);
    }
}
