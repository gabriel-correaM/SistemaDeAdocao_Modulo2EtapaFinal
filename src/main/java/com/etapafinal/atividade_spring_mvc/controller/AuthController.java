package com.etapafinal.atividade_spring_mvc.controller;

import com.etapafinal.atividade_spring_mvc.dto.CadastroUsuarioRequest;
import com.etapafinal.atividade_spring_mvc.dto.LoginRequest;
import com.etapafinal.atividade_spring_mvc.dto.UsuarioResponse;
import com.etapafinal.atividade_spring_mvc.model.Usuario;
import com.etapafinal.atividade_spring_mvc.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String USUARIO_SESSAO = "usuarioId";
    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody CadastroUsuarioRequest request, HttpSession session) {
        try {
            Usuario usuario = service.cadastrar(
                request.nome(), request.email(), request.telefone(), request.senha()
            );
            session.setAttribute(USUARIO_SESSAO, usuario.getId());
            return UsuarioResponse.from(usuario);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login")
    public UsuarioResponse login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            Usuario usuario = service.autenticar(request.email(), request.senha());
            session.setAttribute(USUARIO_SESSAO, usuario.getId());
            return UsuarioResponse.from(usuario);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/me")
    public UsuarioResponse me(HttpSession session) {
        Long id = (Long) session.getAttribute(USUARIO_SESSAO);
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
        try {
            return UsuarioResponse.from(service.buscar(id));
        } catch (IllegalArgumentException e) {
            session.invalidate();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
