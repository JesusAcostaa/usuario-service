package com.usuario.service.servicio;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.modelos.Libro;
import com.usuario.service.modelos.Juego;
import com.usuario.service.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return nuevoUsuario;
    }

    public List<Libro> getLibros(int usuarioId) {
        List<Libro> libros = restTemplate.getForObject("http://localhost:8002/libro/usuario/" + usuarioId, List.class);
        return libros;
    }

    public List<Juego> getJuegos(int usuarioId) {
        List<Juego> juegos = restTemplate.getForObject("http://localhost:8003/juego/usuario/" + usuarioId, List.class);
        return juegos;
    }


    public Map<String, Object> getUsuarioAndVehiculos(int usuarioId) {
        Map<String, Object> resultado = new HashMap<>();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null) {
            resultado.put("Mensaje", "El usuario no existe");
            return resultado;
        }

        resultado.put("Usuario", usuario);
        List<Libro> libros = restTemplate.getForObject("http://localhost:8002/libro/usuario/" + usuarioId, List.class);
        if (libros.isEmpty()) {
            resultado.put("Libros", "El usuario no tiene libros");
        } else {
            resultado.put("Libros", libros);
        }

        List<Juego> juegos = restTemplate.getForObject("http://localhost:8003/juego/usuario/" + usuarioId, List.class);
        if (juegos.isEmpty()) {
            resultado.put("Juegos", "El usuario no tiene juegos");
        } else {
            resultado.put("Juegos", juegos);
        }
        return resultado;
    }

}