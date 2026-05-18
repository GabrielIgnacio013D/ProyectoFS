package cl.duoc.identidad_api.repository;

import cl.duoc.identidad_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aquí no se escribe nada más.
}