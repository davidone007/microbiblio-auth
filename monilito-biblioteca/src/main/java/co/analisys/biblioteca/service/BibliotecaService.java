package co.analisys.biblioteca.service;

import co.analisys.biblioteca.entity.Libro;
import co.analisys.biblioteca.entity.Prestamo;
import co.analisys.biblioteca.entity.Usuario;
import co.analisys.biblioteca.repository.LibroRepository;
import co.analisys.biblioteca.repository.PrestamoRepository;
import co.analisys.biblioteca.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BibliotecaService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public void prestarLibro(int usuarioId, int libroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (libro.isDisponible()) {
            Prestamo prestamo = new Prestamo();
            prestamo.setUsuario(usuario);
            prestamo.setLibro(libro);
            prestamo.setFechaPrestamo(new Date());
            prestamoRepository.save(prestamo);

            libro.setDisponible(false);
            libroRepository.save(libro);

            notificationService.enviarNotificacion(usuario.getEmail(), "Libro prestado: " + libro.getTitulo());
        } else {
            throw new RuntimeException("El libro no está disponible");
        }
    }

    @Transactional
    public void devolverLibro(int prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);
        libroRepository.save(libro);

        notificationService.enviarNotificacion(prestamo.getUsuario().getEmail(),
                "Libro devuelto: " + libro.getTitulo());
    }

    public List<Libro> buscarLibros(String criterio) {
        // Implementar lógica de búsqueda
        return libroRepository.findAll(); // Ejemplo simple
    }
}
