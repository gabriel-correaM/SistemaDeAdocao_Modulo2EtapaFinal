package com.etapafinal.atividade_spring_mvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String raca;

    @Column(nullable = false, length = 150)
    private String abrigo;

    @Column(nullable = false)
    private boolean adotado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adotante_id")
    private Usuario adotante;

    public Animal() {
    }

    public Animal(String nome, String raca, String abrigo) {
        this.nome = nome;
        this.raca = raca;
        this.abrigo = abrigo;
        this.adotado = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public String getAbrigo() { return abrigo; }
    public void setAbrigo(String abrigo) { this.abrigo = abrigo; }
    public boolean isAdotado() { return adotado; }
    public void setAdotado(boolean adotado) { this.adotado = adotado; }
    public Usuario getAdotante() { return adotante; }
    public void setAdotante(Usuario adotante) { this.adotante = adotante; }
}
