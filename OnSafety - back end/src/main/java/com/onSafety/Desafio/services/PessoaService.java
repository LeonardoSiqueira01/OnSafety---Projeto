package com.onSafety.Desafio.services;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onSafety.Desafio.exceptions.InvalidException;
import com.onSafety.Desafio.model.Pessoa;
import com.onSafety.Desafio.repositories.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	PessoaRepository repository;
	

	public Pessoa salvarPessoa(Pessoa p1) {
		if (repository.findByEmail(p1.getEmail()) != null) {
            throw new InvalidException("Email já existe");
        }
		if (repository.findByCpf(p1.getCpf()) != null) {
			throw new InvalidException("CPF já existe");
		}
		return repository.save(p1);
	}
	
	public List<Pessoa> listarTodos() {
		List<Pessoa> pessoas = new ArrayList<>();
		pessoas = repository.findAll();
		return pessoas;
	}

	public Pessoa encontrarPorId(Long id) {
		Optional<Pessoa> pessoa = repository.findById(id);
		return pessoa.get();
	}
	public Pessoa updatePessoa(Long id, Pessoa pessoa) {
		Optional<Pessoa> p1 = repository.findById(id);
		if(repository.findByEmail(pessoa.getEmail())!= null) {
			throw new InvalidException("Este email já existe!");
		}
		if (p1 == null) {
			throw new ObjectNotFoundException("Usuário " + pessoa.getNome() + " não encontrado!", id);
		} else {
			repository.save(pessoa);
			return pessoa;
		}
	}



	public boolean deletarPorId(Long id) throws InvalidObjectException {
		Optional<Pessoa> p1 = repository.findById(id);
		if (!p1.isEmpty()) {
			repository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}


}
