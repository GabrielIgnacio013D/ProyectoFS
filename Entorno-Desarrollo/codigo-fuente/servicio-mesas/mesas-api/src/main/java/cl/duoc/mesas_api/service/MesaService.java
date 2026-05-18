package cl.duoc.mesas_api.service;

import cl.duoc.mesas_api.client.MenuClient;
import cl.duoc.mesas_api.dto.PlatoDTO;
import cl.duoc.mesas_api.model.Mesa;
import cl.duoc.mesas_api.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private MenuClient menuClient;

    public List<Mesa> obtenerTodas() {
        return mesaRepository.findAll();
    }

    public Optional<Mesa> obtenerMesaPorId(Long id) {
        return mesaRepository.findById(id);
    }

    public Mesa guardar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public void eliminar(Long id) {
        mesaRepository.deleteById(id);
    }

    public String obtenerDetalleMesaConPlato(Long idMesa, Long idPlato) {
        try {
            PlatoDTO plato = menuClient.obtenerPlatoPorId(idPlato);
            if (plato != null) {
                return "Mesa ID: " + idMesa + " tiene asignado el plato: " + plato.getNombre() + " ($" + plato.getPrecio() + ")";
            }
        } catch (Exception e) {
            return "Mesa ID: " + idMesa + " - No se pudo conectar con el servicio de menú.";
        }
        return "Mesa ID: " + idMesa + " - El plato no existe en el menú.";
    }
}
