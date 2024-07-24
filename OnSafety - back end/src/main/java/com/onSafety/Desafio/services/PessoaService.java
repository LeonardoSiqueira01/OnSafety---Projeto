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
		if (p1.getCpf().length() != 14) {
			String novoCPF = p1.getCpf();
			p1.setCpf(arrumarCPF(novoCPF));
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
		if (repository.findByEmail(pessoa.getEmail()) != null) {
			throw new InvalidException("Este email já existe!");
		} else if (repository.findByCpf(pessoa.getCpf()) != null) {
			throw new InvalidException("Este cpf já existe!");
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

	public static String formatCPF(String cpf) {
		if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
			throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos.");
		}
		return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
	}

	public static String arrumarCPF(String cpf) {
		String novoCPF = "";
		if (cpf.length() == 11) {
			novoCPF = formatCPF(cpf);
		}
		return novoCPF;
	}
}
