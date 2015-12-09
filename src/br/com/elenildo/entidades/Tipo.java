package br.com.elenildo.entidades;

public class Tipo{

	private long _id;
	private String descricao;
	
	public Tipo(String descricao){
		this.descricao=descricao;
	}
	
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
