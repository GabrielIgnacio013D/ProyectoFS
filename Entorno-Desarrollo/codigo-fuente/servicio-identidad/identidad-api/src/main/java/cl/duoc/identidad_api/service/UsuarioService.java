package cl.duoc.identidad_api.service;

import cl.duoc.identidad_api.model.Usuario;
import cl.duoc.identidad_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para el GET del controlador
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Método para el POST (crear) del controlador
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // --- NUEVA LÓGICA DE AUTENTICACIÓN (HITO 2) ---
    // El Service se encarga de buscar al usuario en la BD y validar la clave
    public Optional<Usuario> autenticar(String username, String password) {
    // Traemos todos los usuarios de la base de datos
    List<Usuario> usuarios = usuarioRepository.findAll();

    // Buscamos al usuario de forma segura sin peligro de NullPointerException
    return usuarios.stream()
            .filter(u -> u.getUsername() != null && username.equals(u.getUsername()))
            .findFirst()
            .filter(u -> u.getPassword() != null && u.getPassword().equals(password));
}
}