package com.etapafinal.atividade_spring_mvc.service;

import com.etapafinal.atividade_spring_mvc.model.Animal;
import com.etapafinal.atividade_spring_mvc.model.Usuario;
import com.etapafinal.atividade_spring_mvc.repository.AnimalRepository;
import com.etapafinal.atividade_spring_mvc.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;

    public AnimalService(AnimalRepository animalRepository, UsuarioRepository usuarioRepository) {
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Animal cadastrar(String nome, String raca, String abrigo) {
        if (nome == null || nome.trim().length() < 2 || nome.trim().matches("\\d+")) {
            throw new IllegalArgumentException("Nome do animal inválido.");
        }
        if (raca == null || raca.trim().length() < 2 || raca.trim().matches("\\d+")) {
            throw new IllegalArgumentException("Raça inválida.");
        }
        if (abrigo == null || abrigo.trim().length() < 2 || abrigo.trim().matches("\\d+")) {
            throw new IllegalArgumentException("Abrigo inválido.");
        }

        return animalRepository.save(new Animal(nome.trim(), raca.trim(), abrigo.trim()));
    }

    public List<Animal> listar() {
        return animalRepository.findAll();
    }

    public Animal adotar(Long animalId, Long usuarioId) {
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new IllegalArgumentException("Animal não encontrado."));

        if (animal.isAdotado()) {
            throw new IllegalArgumentException("Animal já foi adotado.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        animal.setAdotado(true);
        animal.setAdotante(usuario);
        return animalRepository.save(animal);
    }

    public void excluir(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new IllegalArgumentException("Animal não encontrado.");
        }
        animalRepository.deleteById(animalId);
    }
}
