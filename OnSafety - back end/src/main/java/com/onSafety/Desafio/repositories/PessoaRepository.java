package com.onSafety.Desafio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onSafety.Desafio.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	Pessoa findByEmail(String email);

	Pessoa findByCpf(String cpf);

}
