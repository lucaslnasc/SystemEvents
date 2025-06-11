package com.eventos.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParticipanteTest {
    // Instância de Participante que será usada nos testes
    private Participante participante;

    // Configuração inicial executada antes de cada teste
    @BeforeEach
    void setUp() {
        // Cria um novo participante com dados básicos para teste
        participante = new Participante("Maria Silva", "maria@email.com", "12345678901");
    }

    // Testa se um participante é criado corretamente com todos os atributos
    @Test
    void testParticipanteCreation() {
        assertNotNull(participante);  // Verifica se o participante foi criado
        assertEquals("Maria Silva", participante.getNome());  // Verifica o nome
        assertEquals("maria@email.com", participante.getEmail());  // Verifica o email
        assertEquals("12345678901", participante.getCpf());  // Verifica o CPF
    }

    // Testa os métodos getters e setters do participante
    @Test
    void testParticipanteSettersAndGetters() {
        // Define novos valores para os atributos
        participante.setNome("João Silva");
        participante.setEmail("joao@email.com");
        participante.setCpf("98765432101");

        // Verifica se os valores foram alterados corretamente
        assertEquals("João Silva", participante.getNome());
        assertEquals("joao@email.com", participante.getEmail());
        assertEquals("98765432101", participante.getCpf());
    }

    // Testa a integração entre participante e evento
    @Test
    void testParticipanteEmEvento() {
        // Cria um evento para teste
        Evento evento = new Evento("Concert", "Concert description", 
            LocalDate.now().plusDays(10), "Concert Hall", 100);
        
        // Verifica se o evento tem capacidade para o participante
        assertTrue(evento.getCapacidade() > 0);
        
        // Verifica se o participante tem todos os dados obrigatórios
        assertNotNull(participante.getNome(), "Participante deve ter um nome");
        assertNotNull(participante.getEmail(), "Participante deve ter um email");
        assertNotNull(participante.getCpf(), "Participante deve ter um CPF");
    }

    // Testa o método toString do participante
    @Test
    void testParticipanteToString() {
        // Define a string esperada no formato: nome - email
        String expectedString = "Maria Silva - maria@email.com";
        // Verifica se o toString() retorna a string no formato correto
        assertEquals(expectedString, participante.toString());
    }
}