package com.onSafety.Desafio.exceptions;

public class InvalidException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidException(String message) {
		super(message);
	}
}
