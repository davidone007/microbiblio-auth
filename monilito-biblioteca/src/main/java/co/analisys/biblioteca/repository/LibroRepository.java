package co.analisys.biblioteca.repository;

import co.analisys.biblioteca.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
}
