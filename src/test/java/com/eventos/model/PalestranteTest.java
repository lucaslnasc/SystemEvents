package com.eventos.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PalestranteTest {
    // Instância de Palestrante que será usada nos testes
    private Palestrante palestrante;

    // Configuração inicial executada antes de cada teste
    @BeforeEach
    void setUp() {
        // Cria um novo palestrante com dados básicos para teste
        palestrante = new Palestrante("John Doe", "PhD em Música", "Música Clássica");
    }

    // Testa se um palestrante é criado corretamente com todos os atributos
    @Test
    void testPalestranteCreation() {
        assertNotNull(palestrante);  // Verifica se o palestrante foi criado
        assertEquals("John Doe", palestrante.getNome());  // Verifica o nome
        assertEquals("PhD em Música", palestrante.getCurriculo());  // Verifica o currículo
        assertEquals("Música Clássica", palestrante.getAreaAtuacao());  // Verifica a área de atuação
    }

    // Testa os métodos getters e setters do palestrante
    @Test
    void testPalestranteSettersAndGetters() {
        // Define novos valores para os atributos
        palestrante.setNome("Jane Doe");
        palestrante.setCurriculo("Mestrado em Arte");
        palestrante.setAreaAtuacao("Arte Moderna");

        // Verifica se os valores foram alterados corretamente
        assertEquals("Jane Doe", palestrante.getNome());
        assertEquals("Mestrado em Arte", palestrante.getCurriculo());
        assertEquals("Arte Moderna", palestrante.getAreaAtuacao());
    }

    // Testa o método toString do palestrante
    @Test
    void testPalestranteToString() {
        // Define a string esperada no formato: nome - currículo - área de atuação
        String expectedString = "John Doe - PhD em Música - Música Clássica";
        // Verifica se o toString() retorna a string no formato correto
        assertEquals(expectedString, palestrante.toString());
    }
}