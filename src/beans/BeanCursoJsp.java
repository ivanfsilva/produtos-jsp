package beans;

public class BeanCursoJsp {

	private String login;
	private String senha;
	
	public boolean validaLoginSenha(String login, String senha) {
		if ("admin".equalsIgnoreCase(login) && "admin".equalsIgnoreCase(senha)) {
			return true;
		}
		
		return false;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
