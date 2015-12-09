package br.com.elenildo.entidades;

public final class SessionUser {
	private String email;
	private String paswd;
	private int protect;
	
	public int getProtect() {
		return protect;
	}

	public void setProtect(int protect) {
		this.protect = protect;
	}

	private static SessionUser usuario;
	
	public SessionUser(){}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaswd() {
		return paswd;
	}

	public void setPaswd(String paswd) {
		this.paswd = paswd;
	}

	public static SessionUser getUsuario() {
		return usuario;
	}

	public static SessionUser getInstance(){
		if(usuario == null) usuario = new SessionUser();
		return usuario;
	}
	

}
