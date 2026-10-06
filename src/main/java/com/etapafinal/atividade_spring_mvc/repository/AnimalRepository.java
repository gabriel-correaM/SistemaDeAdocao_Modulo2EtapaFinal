package com.etapafinal.atividade_spring_mvc.repository;

import com.etapafinal.atividade_spring_mvc.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
