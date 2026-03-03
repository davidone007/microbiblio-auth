package co.analisys.biblioteca.repository;

import co.analisys.biblioteca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
