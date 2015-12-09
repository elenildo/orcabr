package br.com.elenildo.entidades;

public class Lancamento {
	private long _id;
	private long id_cat;
	private long id_tipo;
	private String data;
	private Double valor;
	private int registrado;
	private String obs;
	
	
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	public long getId_cat() {
		return id_cat;
	}
	public void setId_cat(long id_cat) {
		this.id_cat = id_cat;
	}
	public long getId_tipo() {
		return id_tipo;
	}
	public void setId_tipo(long id_tipo) {
		this.id_tipo = id_tipo;
	}
	public String getData() {
		return data;
	}
	public void setData(String string) {
		this.data = string;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double editable) {
		this.valor = editable;
	}
	public int isRegistrado() {
		return registrado;
	}
	public void setRegistrado(int registrado) {
		this.registrado = registrado;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String editable) {
		this.obs = editable;
	}
	
	
	
	

}
