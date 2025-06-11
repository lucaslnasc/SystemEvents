package com.eventos.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseConnectionTest {
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            fail("Não foi possível estabelecer conexão com o banco de dados: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseConnection() {
        try {
            assertNotNull(connection, "A conexão não deve ser nula");
            assertFalse(connection.isClosed(), "A conexão não deve estar fechada");
        } catch (SQLException e) {
            fail("Erro ao testar a conexão: " + e.getMessage());
        }
    }

    @Test
    void testMultipleConnections() {
        try {
            Connection connection2 = DatabaseConnection.getConnection();
            assertNotNull(connection2, "A segunda conexão não deve ser nula");
            assertSame(connection, connection2, "Deve retornar a mesma instância de conexão");
        } catch (SQLException e) {
            fail("Erro ao testar múltiplas conexões: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            DatabaseConnection.closeConnection();
            assertTrue(connection.isClosed(), "A conexão deve estar fechada após closeConnection()");
        } catch (SQLException e) {
            fail("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}