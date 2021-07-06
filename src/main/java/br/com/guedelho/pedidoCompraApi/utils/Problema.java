package br.com.guedelho.pedidoCompraApi.utils;

public class Problema {
	private int status;
	private String error;
	
	
	public Problema() {
		super();
	}

	public Problema(int status, String error) {
		super();
		this.status = status;
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
