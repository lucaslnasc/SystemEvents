package com.eventos.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventoTest {
    private Evento evento;
    private Palestrante palestrante;
    private Participante participante;
    private LocalDate dataFutura;

    // Configuração inicial executada antes de cada teste
    @BeforeEach
    void setUp() {
        // Cria data futura para o evento (10 dias à frente)
        dataFutura = LocalDate.now().plusDays(10);
        // Inicializa um novo evento com dados básicos
        evento = new Evento("Concert", "Concert description", dataFutura, "Concert Hall", 100);
        // Inicializa um palestrante para os testes
        palestrante = new Palestrante("John Doe", "PhD em Música", "Música Clássica");
        // Inicializa um participante para os testes
        participante = new Participante("Maria Silva", "maria@email.com", "12345678901");
    }

    // Testa se um evento é criado corretamente com todos os atributos
    @Test
    void testEventoCreationSuccess() {
        assertNotNull(evento);  // Verifica se o evento foi criado
        assertEquals("Concert", evento.getNome());  // Verifica o nome
        assertEquals("Concert description", evento.getDescricao());  // Verifica a descrição
        assertEquals(dataFutura, evento.getData());  // Verifica a data
        assertEquals("Concert Hall", evento.getLocal());  // Verifica o local
        assertEquals(100, evento.getCapacidade());  // Verifica a capacidade
        assertNotNull(evento.getPalestrantes(), "Lista de palestrantes não deve ser nula");  // Verifica se a lista de palestrantes foi inicializada
    }

    // Testa a adição de palestrante ao evento
    @Test
    void testEventoComPalestrante() {
        assertTrue(evento.getPalestrantes().isEmpty());  // Verifica se a lista começa vazia
        evento.getPalestrantes().add(palestrante);  // Adiciona um palestrante
        assertEquals(1, evento.getPalestrantes().size());  // Verifica se foi adicionado
        assertEquals("John Doe", evento.getPalestrantes().get(0).getNome());  // Verifica os dados do palestrante
    }

    // Testa os métodos getters e setters do evento
    @Test
    void testEventoSettersAndGetters() {
        Evento evento = new Evento();
        LocalDate novaData = LocalDate.now().plusMonths(1);
        
        // Testa a configuração de todos os atributos
        evento.setNome("Festival");
        evento.setDescricao("Festival description");
        evento.setData(novaData);
        evento.setLocal("Festival Ground");
        evento.setCapacidade(500);

        // Testa palestrantes
        ArrayList<Palestrante> palestrantes = new ArrayList<>();
        palestrantes.add(palestrante);
        evento.setPalestrantes(palestrantes);

        // Verifica se todos os valores foram configurados corretamente
        assertEquals("Festival", evento.getNome());
        assertEquals("Festival description", evento.getDescricao());
        assertEquals(novaData, evento.getData());
        assertEquals("Festival Ground", evento.getLocal());
        assertEquals(500, evento.getCapacidade());
        assertEquals(1, evento.getPalestrantes().size());
    }

    // Testa o método toString do evento
    @Test
    void testEventoToString() {
        String expectedString = "Concert - Concert description - Concert Hall";
        assertEquals(expectedString, evento.toString());  // Verifica o formato da string
    }

    // Testa se a capacidade do evento está dentro dos limites válidos
    @Test
    void testEventoCapacidadeValida() {
        assertTrue(evento.getCapacidade() > 0, "Capacidade deve ser maior que zero");
        assertTrue(evento.getCapacidade() <= 1000, "Capacidade não deve exceder 1000");
    }

    // Testa a integração entre evento, palestrante e participante
    @Test
    void testEventoComPalestranteEParticipante() {
        evento.getPalestrantes().add(palestrante);  // Adiciona palestrante
        
        // Verifica palestrante
        assertFalse(evento.getPalestrantes().isEmpty(), "Evento deve ter pelo menos um palestrante");
        assertEquals(palestrante.getNome(), evento.getPalestrantes().get(0).getNome());
        assertEquals(palestrante.getAreaAtuacao(), evento.getPalestrantes().get(0).getAreaAtuacao());
        
        // Verifica capacidade para participantes
        assertTrue(evento.getCapacidade() > 0, "Deve haver espaço para participantes");
    }

    // Testa a validação dos dados do participante
    @Test
    void testEventoComParticipante() {
        assertTrue(evento.getCapacidade() > 0);  // Verifica se há espaço
        
        // Verifica se os dados do participante são válidos
        assertNotNull(participante.getNome());
        assertNotNull(participante.getEmail());
        assertNotNull(participante.getCpf());
        
        // Verifica os dados específicos do participante
        assertEquals("Maria Silva", participante.getNome());
        assertEquals("maria@email.com", participante.getEmail());
        assertEquals("12345678901", participante.getCpf());
    }
}