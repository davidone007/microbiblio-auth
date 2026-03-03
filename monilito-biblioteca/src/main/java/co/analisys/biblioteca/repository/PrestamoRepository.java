package co.analisys.biblioteca.repository;

import co.analisys.biblioteca.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
}
