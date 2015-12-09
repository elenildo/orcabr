package br.com.elenildo.entidades;

public class Conta {
	private int _id;
	private String nome;
	private double saldo;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public int deposito(double valor){
		if(valor > 0){
			this.saldo += valor;
			return 1;
		}
		return -1;
	}
	
	public int saque(double valor){
		if(valor > 0){
			if(valor < this.saldo){
				saldo -= valor;
				return 1;
			}
		}
		return -1;
	}
	

}
