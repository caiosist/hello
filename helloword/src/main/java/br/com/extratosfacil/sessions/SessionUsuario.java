package br.com.extratosfacil.sessions;

import java.util.Date;

import br.com.extratosfacil.entities.Empresa;
import br.com.jbc.controller.Controller;

public class SessionUsuario {

	private Controller<Empresa> controller = new Controller<Empresa>();

	// public Empresa efetuarLogin(String login, String senha) {
	// controller = new Controller<Empresa>();
	// Empresa empresa = new Empresa();
	// empresa.setLogin(login);
	// empresa.setSenha(senha);
	// try {
	// // return controller
	// // .getObjectByHQLCondition("From Empresa where login = '"
	// // + login + "' and senha = '" + senha + "'");
	// empresa = this.controller.find(empresa,
	// Controller.SEARCH_EQUALS_STRING);
	// return empresa;
	// } catch (Exception e) {
	// e.printStackTrace();
	// empresa = null;
	// controller = null;
	// return null;
	// }
	// }

	public Empresa efetuarLogin(Empresa empresa) {
		// verifica se o login e senha nao sao vazios
		if ((empresa.getLogin() != null)
				&& (!empresa.getLogin().trim().equals(""))
				&& (empresa.getSenha() != null)
				&& (!empresa.getSenha().trim().equals(""))) {
			// tente buscar o usuario
			try {
				empresa.setSenha(this.crip(empresa.getSenha()));
				empresa = this.controller.find(empresa,
						Controller.SEARCH_EQUALS_STRING);
			} catch (Exception e) {
				empresa = null;
				e.printStackTrace();
			}
		} else {
			empresa = null;
		}

		return empresa;
	}

	public String crip(String senha) {
		char[] crip = new char[senha.length()];
		char c;
		int charVal = 0;
		for (int i = 0; i < senha.length(); i++) {
			c = senha.charAt(i);
			charVal = c + 1;
			c = (char) charVal;
			crip[i] = c;
		}
		return String.valueOf(crip);
	}

	public void validaVencimento(Empresa empresa) {
		Date hoje = new Date();
		if (empresa.getPlano() != null) {
			Date vencimento = empresa.getPlano().getVencimento();
			if (vencimento != null) {
				if (hoje.after(vencimento)) {
					SessionPlano sPlano = new SessionPlano();
					sPlano.bloquear(empresa.getPlano());
					sPlano = null;
				}
			}
		}

	}
}
