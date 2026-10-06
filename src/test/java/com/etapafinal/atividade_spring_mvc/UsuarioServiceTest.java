package com.etapafinal.atividade_spring_mvc;

import com.etapafinal.atividade_spring_mvc.model.Usuario;
import com.etapafinal.atividade_spring_mvc.repository.UsuarioRepository;
import com.etapafinal.atividade_spring_mvc.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void deveCadastrarUsuarioNormalizandoEmail() {
        when(repository.existsByEmail("teste@gmail.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = service.cadastrar(
            "Maria Silva",
            " teste@gmail.com ",
            "(47) 99999-8888",
            "123456"
        );

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());

        assertEquals("teste@gmail.com", resultado.getEmail());
        assertEquals("teste@gmail.com", captor.getValue().getEmail());
        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("123456", resultado.getSenha());
    }
}
