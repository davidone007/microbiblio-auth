package co.analisys.biblioteca;

import co.analisys.biblioteca.entity.Libro;
import co.analisys.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cargar datos de prueba
        libroRepository.save(Libro.builder().id(1).titulo("El Quijote").disponible(true).build());
        libroRepository.save(Libro.builder().id(2).titulo("Cien años de soledad").disponible(true).build());
        libroRepository.save(Libro.builder().id(3).titulo("El principito").disponible(true).build());

        System.out.println("Datos de prueba cargados exitosamente.");
    }
}
