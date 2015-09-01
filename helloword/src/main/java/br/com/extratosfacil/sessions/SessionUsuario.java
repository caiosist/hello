package br.com.extratosfacil.sessions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.extratosfacil.entities.Usuario;
import br.com.jbc.controller.Controller;

public class SessionUsuario {

	private Controller<Usuario> controller = new Controller<Usuario>();

	static String loginPage = "/helloword/login.jsf";
	static String indexPage = "/helloword/index.jsf";

	public boolean save(Usuario usuario) {
		if (this.valida(usuario)) {
			try {
				this.controller.insert(usuario);
				return true;
			} catch (Exception e) {
				if (e.getClass()
						.equals(org.hibernate.exception.ConstraintViolationException.class)) {
					// Mensagem.msgLoginDuplicado();
					return false;
				}
				return false;
			}
		}
		return false;
	}

	private boolean valida(Usuario usuario) {
		// // Nome Obrigatorio
		// if ((usuario.getNome() == null) || (usuario.getNome().equals(""))) {
		// Mensagens.msgCamposObrigatorios();
		// return false;
		// }
		//
		// // email obrigatorio + validacao
		// if ((usuario.getEmail() == null) || (usuario.getEmail().equals("")))
		// {
		// Mensagens.msgCamposObrigatorios();
		// return false;
		// } else {
		// if (!this.validaEmail(usuario.getEmail())) {
		// Mensagens.msgEmailInvalido();
		// return false;
		// }
		// }
		//
		// // Login obrigatorio
		// if ((usuario.getLogin() == null) || (usuario.getLogin().equals("")))
		// {
		// Mensagens.msgCamposObrigatorios();
		// return false;
		// }
		//
		// // Senha Obrigatoria com no minimo 8 digitos
		// if ((usuario.getSenha() == null) || (usuario.getSenha().equals("")))
		// {
		// Mensagens.msgCamposObrigatorios();
		// return false;
		// } else {
		// if ((usuario.getSenha().length() > 8)
		// || (usuario.getSenha().length() < 4)) {
		// Mensagens.msgSenha();
		// return false;
		// }
		// }
		//
		// if (usuario.getAdmin() == null) {
		// usuario.setAdmin(false);
		// }

		return true;
	}

	private boolean validaEmail(String email) {
		System.out.println("Metodo de validacao de email");
		Pattern p = Pattern
				.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(email);
		if (m.find()) {
			// System.out.println("O email " + email + " e valido");
			return true;
		} else {
			// System.out.println("O E-mail " + email + " é inválido");
			return false;
		}
	}

	public boolean update(Usuario usuario) {
		try {
			this.controller.update(usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean delete(Usuario usuario) {
		try {
			this.controller.delete(usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<Usuario> findList(Usuario usuario) {

		List<Usuario> usuarios = new ArrayList<Usuario>();

		try {
			usuarios = this.controller.findList(usuario);
		} catch (Exception e) {
			// TODO: handle exception
			usuarios = null;
			e.printStackTrace();
		}

		return usuarios;
	}

	public Controller<Usuario> getController() {
		return controller;
	}

	public void setController(Controller<Usuario> controller) {
		this.controller = controller;
	}

	public Usuario efetuarLogin(Usuario usuario) {

		// verifica se o login e senha nao sao vazios
		if ((usuario.getLogin() != null) && (!usuario.getLogin().equals(""))
				&& (usuario.getSenha() != null)
				&& (!usuario.getSenha().equals(""))) {
			// tente buscar o usuario
			try {
				usuario = this.controller.find(usuario,
						Controller.SEARCH_EQUALS_STRING);

			} catch (Exception e) {
				usuario = null;
				e.printStackTrace();
			}
		} else {
			usuario = null;
		}

		return usuario;
	}

	public Usuario find(Usuario filtro) {

		if (validaFiltro(filtro)) {
			try {
				filtro = this.controller.find(filtro,
						Controller.SEARCH_EQUALS_STRING);
				return filtro;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private boolean validaFiltro(Usuario filtro) {
		if ((filtro.getEmail() == null) || (filtro.getEmail().equals(""))) {
			// Mensagens.msgEmailInvalido();
			return false;
		} else {
			if (!this.validaEmail(filtro.getEmail())) {
				// Mensagens.msgEmailInvalido();
				return false;
			}
		}
		return true;
	}

	public Usuario findUsuarioByName(String nome) {

		Usuario usuario = new Usuario();
		usuario.setNome(nome);

		try {
			return this.controller.find(usuario,
					Controller.SEARCH_EQUALS_STRING);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
