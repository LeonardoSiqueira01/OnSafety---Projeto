
-- Seleciona o banco de dados
USE onsafety;

-- Cria a tabela pessoa
CREATE TABLE pessoa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    UNIQUE (cpf, email) -- Adiciona a restrição única combinando cpf e email
);
