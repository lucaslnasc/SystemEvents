# Sistema de Gerenciamento de Eventos

Este é um sistema de gerenciamento de eventos desenvolvido em Java utilizando Swing para a interface gráfica e PostgreSQL como banco de dados. O sistema permite gerenciar eventos, palestrantes e participantes, além de realizar inscrições em eventos.

## Funcionalidades

- **Gerenciamento de Eventos**: Criar, editar, excluir e listar eventos.
- **Gerenciamento de Palestrantes**: Criar, editar, excluir e listar palestrantes.
- **Gerenciamento de Participantes**: Criar, editar, excluir e listar participantes.
- **Inscrição em Eventos**: Inscrever palestrantes e participantes em eventos.
- **Capacidade de Eventos**: Controle de capacidade máxima para eventos.

## Estrutura do Projeto

O projeto segue a estrutura padrão de um projeto Maven:

src/ ├── main/ │ ├── java/ │ │ └── com/ │ │ └── eventos/ │ │ ├── dao/ # Classes de acesso ao banco de dados │ │ ├── model/ # Classes de modelo (entidades) │ │ └── view/ # Interface gráfica (Swing) ├── test/ # Testes unitários pom.xml # Arquivo de configuração do Maven

### Principais Classes

#### **Pacote `com.eventos.model`**

- **`Evento`**: Representa um evento com atributos como nome, descrição, data, local, capacidade e palestrantes associados.
- **`Palestrante`**: Representa um palestrante com atributos como nome, currículo e área de atuação.
- **`Participante`**: Representa um participante com atributos como nome, email e CPF.

#### **Pacote `com.eventos.dao`**

- **`DatabaseConnection`**: Gerencia a conexão com o banco de dados PostgreSQL.
- **`EventoDAO`**: Realiza operações de CRUD para eventos.
- **`PalestranteDAO`**: Realiza operações de CRUD para palestrantes.
- **`ParticipanteDAO`**: Realiza operações de CRUD para participantes e gerencia inscrições.

#### **Pacote `com.eventos.view`**

- **`MainFrame`**: Janela principal do sistema com abas para gerenciar eventos, palestrantes e participantes.
- **`EventoPanel`**: Interface para gerenciar eventos.
- **`PalestrantePanel`**: Interface para gerenciar palestrantes.
- **`ParticipantePanel`**: Interface para gerenciar participantes.
- **`EventoDialog`**: Diálogo para criar ou editar eventos.
- **`PalestranteDialog`**: Diálogo para criar ou editar palestrantes.
- **`ParticipanteDialog`**: Diálogo para criar ou editar participantes.
- **`InscricaoDialog`**: Diálogo para inscrever participantes em eventos.
- **`PalestranteInscricaoDialog`**: Diálogo para inscrever palestrantes em eventos.

## Pré-requisitos

- **Java 17** ou superior
- **Maven** para gerenciar dependências
- **PostgreSQL** como banco de dados
- Configuração do banco de dados no arquivo [`DatabaseConnection`](src/main/java/com/eventos/dao/DatabaseConnection.java).

## Configuração do Banco de Dados

1. Crie um banco de dados no PostgreSQL chamado `eventos_db`.
2. Configure as credenciais no arquivo [`DatabaseConnection`](src/main/java/com/eventos/dao/DatabaseConnection.java):
   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/eventos_db";
   private static final String USER = "postgres";
   private static final String PASSWORD = "1501";
   // ou a senha que você utilizar para conectar
   ```
3. Crie as tabelas necessárias executando os seguintes comandos SQL:

   ```sql
   CREATE TABLE participante (
       id SERIAL PRIMARY KEY,
       nome VARCHAR(100),
       email VARCHAR(100),
       cpf VARCHAR(11)
   );

   CREATE TABLE palestrante (
       id SERIAL PRIMARY KEY,
       nome VARCHAR(100),
       curriculo TEXT,
       area_atuacao VARCHAR(100)
   );

   CREATE TABLE evento (
       id SERIAL PRIMARY KEY,
       nome VARCHAR(100),
       descricao TEXT,
       data DATE,
       local VARCHAR(100),
       capacidade INT
   );

   CREATE TABLE inscricao (
       id SERIAL PRIMARY KEY,
       evento_id INT REFERENCES evento(id),
       participante_id INT REFERENCES participante(id),
       UNIQUE (evento_id, participante_id)
   );

   CREATE TABLE evento_palestrante (
       evento_id INT REFERENCES evento(id),
       palestrante_id INT REFERENCES palestrante(id),
       PRIMARY KEY (evento_id, palestrante_id)
   );
   ```

### Como Executar

1. Clone o repositório:
   ```
   git clone https://github.com/lucaslnasc/SystemEvents.git
   cd sistema-eventos
   ```
2. Compile o projeto com Maven:
   ```
   mvn clean install
   ```
3. Execute o sistema:
  ```
  mvn exec:java -Dexec.mainClass="com.eventos.view.MainFrame"
  ```

Autores:

Lucas de Lima

João Leno

Sérgio Paulo
