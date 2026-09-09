# 🏥 Clínica Vida - Sistema de Gestão

Sistema completo para gerenciamento de clínicas e consultórios médicos. O projeto contempla desde o cadastro de entidades básicas até o agendamento de consultas (com validação de conflitos de horário), evolução de prontuário, processamento de pagamentos e geração de relatórios financeiros agregados.

## 🚀 Tecnologias e Arquitetura

O sistema foi desenvolvido aplicando separação de responsabilidades (Padrão DAO e MVC) e boas práticas de Orientação a Objetos:

- **Linguagem:** Java (JDK 17+)
- **Interface Gráfica (UI):** Java Swing
- **Banco de Dados:** MySQL
- **Integração:** JDBC (Connector/J) com `ConexaoFactory` e leitura de credenciais via `config.properties`
- **Arquitetura:** `Model` (Entidades), `DAO` (Acesso a Dados) e `UI` (Telas).

---

## ⚙️ Pré-requisitos

Para rodar este projeto localmente, você precisará de:
1. **JDK (Java Development Kit)** instalado.
2. **MySQL** rodando na sua máquina.
3. Driver **MySQL Connector/J** (arquivo `.jar`).

---

## 🛠️ Como Configurar e Executar

### 1. Preparando o Banco de Dados
Abra o terminal na raiz do projeto e importe o script de criação do banco:

```bash
mysql -u root -p < sql/schema.sql
```

*(Opcional: crie o usuário da aplicação rodando os comandos `CREATE USER 'clinica_app'@'localhost' IDENTIFIED BY 'clinica123'; GRANT ALL PRIVILEGES ON clinica_vida.* TO 'clinica_app'@'localhost';` no console do MySQL).*

### 2. Configurando a Conexão
- Crie uma pasta chamada `lib` na raiz do projeto e coloque o arquivo `mysql-connector-j-X.X.X.jar` dentro dela.
- Verifique o arquivo `src/config.properties`. Se você não criou o usuário `clinica_app`, altere as variáveis `db.user` e `db.password` para o seu usuário (ex: `root`) e senha.

### 3. Compilando e Rodando (Via Terminal)

**No Windows:**

```cmd
cd src
javac -d ../out -cp ".;../lib/mysql-connector-j-9.1.0.jar" model\*.java dao\*.java util\*.java ui\*.java Main.java
copy config.properties ..\out\
cd ..\out
java -cp ".;../lib/mysql-connector-j-9.1.0.jar" Main
```

**No macOS / Linux:**

```bash
cd src
javac -d ../out -cp ".:../lib/mysql-connector-j-9.1.0.jar" $(find . -name "*.java")
cp config.properties ../out/
cd ../out
java -cp ".:../lib/mysql-connector-j-9.1.0.jar" Main
```

*(Atenção: substitua `mysql-connector-j-9.1.0.jar` pelo nome exato da versão que você baixou).*

> **💡 Dica Profissional:** Para evitar a complexidade do terminal, abra a pasta `clinica-vida` em uma IDE como **IntelliJ IDEA** ou **Eclipse**. Basta adicionar o `.jar` do Connector/J nas bibliotecas (Libraries) do projeto e rodar o arquivo `Main.java`.

---

## 🧪 Roteiro de Teste (Casos de Uso)

Com o sistema aberto, navegue pelas abas para testar as regras de negócio:

1. **Pacientes:** Cadastre um paciente vinculando-o a um Convênio (ou deixe como Particular).
2. **Agenda:** Tente agendar uma consulta. O valor será sugerido automaticamente com base no convênio do paciente.
3. **Validação:** Tente agendar *outra* consulta para o mesmo médico no mesmo horário. O sistema deve bloquear a ação.
4. **Atendimento:** Selecione uma consulta agendada e clique em "Marcar como realizada". Preencha o diagnóstico, prescrição e a forma de pagamento.
5. **Relatórios:** Verifique o faturamento total e as consultas agrupadas por médico e convênio.

---

## ⚠️ Solução de Problemas (Troubleshooting)

- **`ClassNotFoundException: com.mysql.cj.jdbc.Driver`**: O arquivo `.jar` do Connector não foi encontrado. Verifique se ele está na pasta `lib` e se o nome no comando `-cp` está correto.
- **`Access denied for user`**: Usuário ou senha do banco incorretos. Ajuste o arquivo `config.properties`.
- **`Communications link failure`**: O serviço do MySQL não está rodando no seu computador. Inicie o serviço pelo sistema operacional.
