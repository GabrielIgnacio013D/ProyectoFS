package cl.duoc.menu_api.repository;

import cl.duoc.menu_api.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Plato, Long> {
}
