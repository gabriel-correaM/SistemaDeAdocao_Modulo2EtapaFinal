package com.etapafinal.atividade_spring_mvc.controller;

import com.etapafinal.atividade_spring_mvc.dto.AnimalRequest;
import com.etapafinal.atividade_spring_mvc.dto.AnimalResponse;
import com.etapafinal.atividade_spring_mvc.model.Animal;
import com.etapafinal.atividade_spring_mvc.service.AnimalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private static final String USUARIO_SESSAO = "usuarioId";
    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @GetMapping
    public List<AnimalResponse> listar() {
        return service.listar().stream().map(AnimalResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse cadastrar(@RequestBody AnimalRequest request, HttpSession session) {
        exigirLogin(session);
        try {
            return AnimalResponse.from(service.cadastrar(
                request.nome(), request.raca(), request.abrigo()
            ));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}/adotar")
    public AnimalResponse adotar(@PathVariable Long id, HttpSession session) {
        Long usuarioId = exigirLogin(session);
        try {
            Animal animal = service.adotar(id, usuarioId);
            return AnimalResponse.from(animal);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, HttpSession session) {
        exigirLogin(session);
        try {
            service.excluir(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private Long exigirLogin(HttpSession session) {
        Long id = (Long) session.getAttribute(USUARIO_SESSAO);
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
        return id;
    }
}
