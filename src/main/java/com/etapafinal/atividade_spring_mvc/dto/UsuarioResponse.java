package com.etapafinal.atividade_spring_mvc.dto;

import com.etapafinal.atividade_spring_mvc.model.Usuario;

public record UsuarioResponse(Long id, String nome, String email, String telefone, String senha) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(), usuario.getNome(), usuario.getEmail(),
            usuario.getTelefone(), usuario.getSenha()
        );
    }
}
