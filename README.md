# 🏥 Clínica Vida — Plataforma de Gestão Médica Integrada

Sistema completo para gerenciamento de clínicas e consultórios médicos. O projeto contempla desde o cadastro de entidades básicas (médicos e pacientes) até o agendamento inteligente de consultas com validação de conflitos de horário e acompanhamento de status.

---

## 💡 Sobre o Projeto e Intuito

O **Clínica Vida** nasceu com o propósito de modernizar a gestão operacional de consultórios e clínicas de saúde de pequeno a médio porte. Em muitos estabelecimentos, a administração ainda é feita de forma manual (planilhas ou papel), o que gera a perda de históricos, o risco de agendamentos duplicados e a lentidão na recepção.

Para solucionar essas dores, o sistema foi projetado para atuar como um **hub centralizador da clínica**. Na prática, o software permite que a recepção e a administração realizem as seguintes operações:
* **Cadastro de Pacientes:** Registro de dados pessoais, documentos e contatos.
* **Gestão do Corpo Clínico:** Cadastro de médicos vinculados às suas especialidades e CRM.
* **Agendamento de Consultas:** Marcação de horários vinculando diretamente o paciente ao médico escolhido.
* **Controle de Fluxo (Status):** Acompanhamento da jornada do paciente alterando o status da consulta (ex: Agendada, Em Andamento, Finalizada).
* **Trava de Segurança:** O motor do sistema impede automaticamente que duas consultas sejam marcadas para o mesmo médico no exato mesmo horário.

---

## 🚀 Tecnologias e Arquitetura

O sistema foi desenvolvido aplicando a separação de responsabilidades (Padrão DAO e MVC) e boas práticas de Orientação a Objetos:
* **Linguagem:** Java (JDK 17+)
* **Interface Gráfica (UI):** Java Swing (`JFrame`, `JTabbedPane`, `JTable`)
* **Banco de Dados:** MySQL
* **Integração:** JDBC (Connector/J) com `PreparedStatement` para segurança contra SQL Injection.
* **Arquitetura:** Model (Entidades), DAO (Acesso a Dados) e UI (Telas).

---

## ⚙️ Pré-requisitos

Para rodar este projeto localmente, você precisará de:
1. **JDK (Java Development Kit)** instalado.
2. **MySQL** rodando na sua máquina.
3. **Driver MySQL Connector/J** (arquivo `.jar`).

---

## 🛠️ Como Configurar e Executar

### 1. Preparando o Banco de Dados
O sistema depende de um banco de dados relacional para funcionar. No final desta página (na seção **Schema**), há um código SQL. Copie aquele código e execute-o no seu **MySQL Workbench** para criar o banco de dados `clinica_vida_db` e as tabelas necessárias.

### 2. Configurando a Conexão
* Crie uma pasta chamada `lib` na raiz do projeto e coloque o arquivo do driver JDBC (`mysql-connector-j-x.x.x.jar`) dentro dela.
* Acesse a classe de conexão no código Java do projeto e ajuste as credenciais (usuário e senha) para bater com as do seu banco de dados local.

### 3. Compilando e Rodando (Via Terminal)

**No Windows:**
```bash
cd src
javac -d ../out -cp ".;../lib/mysql-connector-j.jar" *.java
cd ../out
java -cp ".;../lib/mysql-connector-j.jar" Main
```

**No macOS / Linux:**
```bash
cd src
javac -d ../out -cp ".:../lib/mysql-connector-j.jar" $(find . -name "*.java")
cd ../out
java -cp ".:../lib/mysql-connector-j.jar" Main
```
> 💡 **Dica:** Para evitar a complexidade do terminal, abra a pasta do projeto em uma IDE como IntelliJ IDEA ou Eclipse, adicione o `.jar` do Connector/J nas bibliotecas (Libraries) do projeto e rode direto pelo arquivo `Main.java`.

---

## 🧪 Roteiro de Teste (Casos de Uso)

Com o sistema aberto, navegue pelas abas para testar as regras de negócio:
1. **Pacientes:** Cadastre um paciente preenchendo as informações obrigatórias corretamente.
2. **Médicos:** Cadastre um médico e informe sua especialidade e CRM.
3. **Agenda:** Tente agendar uma consulta escolhendo o paciente, o médico e a data/hora.
4. **Validação:** Tente agendar *outra* consulta para o mesmo médico no exato mesmo horário. O sistema deve bloquear a ação automaticamente.
5. **Atendimento:** Altere o status da consulta para simular o andamento do atendimento.

---

## ⚠️ Solução de Problemas (Troubleshooting)

* **`ClassNotFoundException: com.mysql.cj.jdbc.Driver`**: O arquivo `.jar` do Connector não foi encontrado. Verifique se ele está na pasta `lib` e se o nome no comando `-cp` está correto.
* **`Access denied for user`**: Usuário ou senha do banco incorretos. Ajuste as credenciais no seu código Java.
* **`Communications link failure`**: O serviço do MySQL não está rodando no seu computador. Inicie o serviço pelo sistema operacional.

---

## 🗄️ Estrutura do Banco de Dados (Schema)

*Nota: Este é o script mencionado no Passo 1. Copie e cole este código no MySQL Workbench para criar a estrutura onde o Java vai salvar os dados.*

```sql
CREATE DATABASE IF NOT EXISTS clinica_vida_db;
USE clinica_vida_db;

CREATE TABLE IF NOT EXISTS pacientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    data_nascimento DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS medicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    crm VARCHAR(20) NOT NULL UNIQUE,
    especialidade VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS consultas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    data_hora DATETIME NOT NULL,
    status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_consulta_medico FOREIGN KEY (medico_id) REFERENCES medicos(id)
);
```
