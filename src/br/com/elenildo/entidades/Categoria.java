package br.com.elenildo.entidades;

public class Categoria extends Tipo{
	private long id_tipo;
	
	public Categoria(String descricao) {
		super(descricao);
	}

	public long getId_tipo() {
		return id_tipo;
	}

	public void setId_tipo(long id_tipo) {
		this.id_tipo = id_tipo;
	}

}
