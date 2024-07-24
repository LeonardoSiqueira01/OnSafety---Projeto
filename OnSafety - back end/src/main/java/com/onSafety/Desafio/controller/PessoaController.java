package com.onSafety.Desafio.controller;

import java.io.InvalidObjectException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onSafety.Desafio.model.Pessoa;
import com.onSafety.Desafio.services.PessoaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	PessoaService service;

	@GetMapping
	public ResponseEntity<List<Pessoa>> getAllPessoas() {
		List<Pessoa> pessoas = service.listarTodos();
		return new ResponseEntity<>(pessoas, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pessoa> getPessoaById(@PathVariable Long id) {
		try {
			Pessoa p1 = service.encontrarPorId(id);
			return new ResponseEntity<>(p1, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Pessoa> createPessoa(@Valid @RequestBody Pessoa pessoa) {
		try {
			Pessoa p1 = service.salvarPessoa(pessoa);
			return new ResponseEntity<>(p1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> updatePessoa(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
		Pessoa updatedPessoa = service.updatePessoa(id, pessoa);
		if (updatedPessoa != null) {
			return new ResponseEntity<>(updatedPessoa, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
		try {
			boolean deleted = service.deletarPorId(id);
			if (deleted) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (InvalidObjectException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

}
