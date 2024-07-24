package com.onSafety.Desafio.exceptions;

public class ErrorResponse {
	private String mensagem;

	public ErrorResponse(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}