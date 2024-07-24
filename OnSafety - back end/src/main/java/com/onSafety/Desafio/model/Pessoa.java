package com.onSafety.Desafio.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
public class Pessoa implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Nome é obrigatório")
    @NotNull(message = "O Nome não pode ficar nulo")
    private String nome;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    @NotNull(message = "O CPF não pode ficar nulo")
    private String cpf;

    @Past(message = "Data de nascimento deve ser no passado")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    
	public Pessoa() {
	}

	public Pessoa(
			@NotBlank(message = "O Nome é obrigatório") @NotNull(message = "O Nome não pode ficar nulo") String nome,
			@CPF(message = "CPF inválido") @NotBlank(message = "CPF é obrigatório") @NotNull(message = "O CPF não pode ficar nulo") String cpf,
			@Past(message = "Data de nascimento deve ser no passado") LocalDate dataNascimento,
			@Email(message = "Email inválido") @NotBlank(message = "Email é obrigatório") String email) {
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    
    
    
}