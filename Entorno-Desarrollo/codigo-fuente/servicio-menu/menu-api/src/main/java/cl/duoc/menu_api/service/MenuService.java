package cl.duoc.menu_api.service;

import cl.duoc.menu_api.model.Plato;
import cl.duoc.menu_api.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Plato> obtenerTodos() {
        return menuRepository.findAll();
    }

    public Optional<Plato> obtenerPorId(Long id) {
        return menuRepository.findById(id);
    }

    public Plato guardar(Plato plato) {
        return menuRepository.save(plato);
    }

    public void eliminar(Long id) {
        menuRepository.deleteById(id);
    }
}
