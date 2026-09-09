-- ============================================================
-- Clínica Vida - Script de criação do banco de dados (MySQL)
-- ============================================================

CREATE DATABASE IF NOT EXISTS clinica_vida CHARACTER SET utf8mb4;
USE clinica_vida;

CREATE TABLE IF NOT EXISTS convenio (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    valor_consulta  DECIMAL(10,2) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS medico (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    crm             VARCHAR(20)  NOT NULL UNIQUE,
    especialidade   VARCHAR(80)  NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS paciente (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    nome                VARCHAR(120) NOT NULL,
    cpf                 VARCHAR(14)  NOT NULL UNIQUE,
    data_nascimento     DATE NOT NULL,
    telefone            VARCHAR(20),
    email               VARCHAR(120),
    convenio_id         INT NULL,
    CONSTRAINT fk_paciente_convenio FOREIGN KEY (convenio_id) REFERENCES convenio(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS consulta (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id     INT NOT NULL,
    medico_id       INT NOT NULL,
    data_hora       DATETIME NOT NULL,
    status          ENUM('AGENDADA', 'REALIZADA', 'CANCELADA') NOT NULL DEFAULT 'AGENDADA',
    valor           DECIMAL(10,2) NOT NULL,
    observacoes     VARCHAR(500),
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    CONSTRAINT fk_consulta_medico   FOREIGN KEY (medico_id)   REFERENCES medico(id),
    CONSTRAINT uq_medico_horario UNIQUE (medico_id, data_hora)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS prontuario (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    consulta_id     INT NOT NULL UNIQUE,
    diagnostico     VARCHAR(1000),
    prescricao      VARCHAR(1000),
    data_registro   DATETIME NOT NULL,
    CONSTRAINT fk_prontuario_consulta FOREIGN KEY (consulta_id) REFERENCES consulta(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pagamento (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    consulta_id     INT NOT NULL UNIQUE,
    valor           DECIMAL(10,2) NOT NULL,
    forma_pagamento ENUM('DINHEIRO', 'CARTAO', 'PIX', 'CONVENIO') NOT NULL,
    data_pagamento  DATETIME NOT NULL,
    CONSTRAINT fk_pagamento_consulta FOREIGN KEY (consulta_id) REFERENCES consulta(id)
) ENGINE=InnoDB;

-- Dados de exemplo (opcional)
INSERT INTO convenio (nome, valor_consulta) VALUES
    ('Particular', 250.00),
    ('UnimedVida', 180.00),
    ('SulSaúde', 160.00);

INSERT INTO medico (nome, crm, especialidade) VALUES
    ('Dra. Ana Souza', 'CRM-SP 123456', 'Clínico Geral'),
    ('Dr. Bruno Lima', 'CRM-SP 654321', 'Cardiologia'),
    ('Dra. Carla Menezes', 'CRM-SP 111222', 'Dermatologia');
