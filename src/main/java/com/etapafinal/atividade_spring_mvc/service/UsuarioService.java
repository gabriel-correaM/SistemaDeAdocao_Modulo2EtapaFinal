
package com.etapafinal.atividade_spring_mvc.service;

import com.etapafinal.atividade_spring_mvc.model.Usuario;
import com.etapafinal.atividade_spring_mvc.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario cadastrar(
            String nome,
            String email,
            String telefone,
            String senha
    ) {

        String emailNormalizado = email == null
                ? null
                : email.trim().toLowerCase(Locale.ROOT);

        validar(
                nome,
                emailNormalizado,
                telefone,
                senha
        );

        if (repository.existsByEmail(emailNormalizado)) {
            throw new IllegalArgumentException(
                    "E-mail já cadastrado."
            );
        }

        Usuario usuario = new Usuario(
                nome.trim(),
                emailNormalizado,
                telefone.trim(),
                senha
        );

        return repository.save(usuario);
    }

    public Usuario autenticar(
            String email,
            String senha
    ) {

        String emailNormalizado = email == null
                ? null
                : email.trim().toLowerCase(Locale.ROOT);

        return repository.findByEmail(emailNormalizado)
                .filter(usuario -> usuario.getSenha().equals(senha))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "E-mail ou senha incorretos."
                        )
                );
    }

    public Usuario buscar(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuário não encontrado."
                        )
                );
    }

    private void validar(
            String nome,
            String email,
            String telefone,
            String senha
    ) {

        if (nome == null
                || nome.trim().length() < 5
                || nome.trim().split("\\s+").length < 2) {

            throw new IllegalArgumentException(
                    "Informe nome e sobrenome."
            );
        }

        if (email == null
                || !email.matches(
                        "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$"
                )) {

            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        if (telefone == null
                || telefone.replaceAll("\\D", "").length() < 10) {

            throw new IllegalArgumentException(
                    "Telefone com DDD (10 ou 11 dígitos)."
            );
        }

        if (senha == null || senha.length() < 6) {

            throw new IllegalArgumentException(
                    "A senha deve ter ao menos 6 caracteres."
            );
        }
    }
}

