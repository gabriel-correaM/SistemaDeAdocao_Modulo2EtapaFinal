package com.etapafinal.atividade_spring_mvc.dto;

import com.etapafinal.atividade_spring_mvc.model.Animal;

public record AnimalResponse(Long id, String nome, String raca, String abrigo,
                             boolean adotado, Long adotanteId) {
    public static AnimalResponse from(Animal animal) {
        return new AnimalResponse(
            animal.getId(),
            animal.getNome(),
            animal.getRaca(),
            animal.getAbrigo(),
            animal.isAdotado(),
            animal.getAdotante() == null ? null : animal.getAdotante().getId()
        );
    }
}
