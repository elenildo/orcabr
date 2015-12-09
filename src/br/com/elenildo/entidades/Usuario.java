package br.com.elenildo.entidades;

public class Usuario {
	int _id;
	int protegido;
	String senha;
	String email;
	
	public Usuario(){}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getProtegido() {
		return protegido;
	}

	public void setProtegido(int protegido) {
		this.protegido = protegido;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
