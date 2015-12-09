package br.com.elenildo.entidades;

public class Balanco {
	private int _id;
	private String data;
	private double receitas;
	private double despesas;
	private double resultado;
	

	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public double getReceitas() {
		return receitas;
	}
	public void setReceitas(double receitas) {
		this.receitas = receitas;
	}
	public double getDespesas() {
		return despesas;
	}
	public void setDespesas(double despesas) {
		this.despesas = despesas;
	}
	public double getResultado() {
		return resultado;
	}
	public void setResultado(double resultado) {
		this.resultado = resultado;
	}
	
	

}
