package br.com.extratosfacil.sessions;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import org.apache.commons.mail.EmailException;
import org.hibernate.exception.ConstraintViolationException;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.entities.Email;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Plano;
import br.com.extratosfacil.entities.location.Cidade;
import br.com.extratosfacil.entities.location.Estado;
import br.com.jbc.controller.Controller;

/**
 * Session que representa as regras de negÃ³cios da entidade Empresa
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Session
 */

public class SessionEmpresa {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private Controller<Empresa> controller = new Controller<Empresa>();

	private Controller<Estado> controllerEstado = new Controller<Estado>();

	private Controller<Cidade> controllerCidade = new Controller<Cidade>();

	private Controller<Plano> controllerPlano = new Controller<Plano>();

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Controller<Empresa> getController() {
		return controller;
	}

	public Controller<Estado> getControllerEstado() {
		return controllerEstado;
	}

	public void setControllerEstado(Controller<Estado> controllerEstado) {
		this.controllerEstado = controllerEstado;
	}

	public Controller<Plano> getControllerPlano() {
		return controllerPlano;
	}

	public void setControllerPlano(Controller<Plano> controllerPlano) {
		this.controllerPlano = controllerPlano;
	}

	public Controller<Cidade> getControllerCidade() {
		return controllerCidade;
	}

	public void setControllerCidade(Controller<Cidade> controllerCidade) {
		this.controllerCidade = controllerCidade;
	}

	public void setController(Controller<Empresa> controller) {
		this.controller = controller;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public boolean save(Empresa empresa, boolean pessoaFisica) {
		if (this.validaEmpresa(empresa, false, pessoaFisica)) {
			try {
				this.controller.insert(empresa);
			} catch (ConstraintViolationException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean update(Empresa empresa, boolean pessoaFisica) {
		if (this.validaEmpresa(empresa, true, pessoaFisica)) {
			try {
				this.controller.update(empresa);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean remove(Empresa empresa) throws Exception {
		this.controller.delete(empresa);
		return true;
	}

	public boolean validaEmpresa(Empresa empresa, boolean update,
			boolean pessoaFisica) {
		if ((empresa.getCidade() == null)
				|| (empresa.getCidade().getId() == null)) {
			Mensagem.send(Mensagem.MSG_INCOMPLETO, Mensagem.ERROR);
			return false;
		} else if ((empresa.getCnpj() == null)
				|| (empresa.getCnpj().trim().equals(""))) {
			Mensagem.send(Mensagem.MSG_INCOMPLETO, Mensagem.ERROR);
			return false;
		} else if (!this.validaCNPJ(empresa.getCnpj())
				&& (!validaCPF(empresa.getCnpj()))) {
			if (!pessoaFisica) {
				Mensagem.send(Mensagem.MSG_CNPJ, Mensagem.ERROR);
			} else {
				Mensagem.send(Mensagem.MSG_CPF, Mensagem.ERROR);
			}
			return false;
		} else if ((empresa.getEmail() == null)
				|| (empresa.getEmail().trim().equals(""))) {
			Mensagem.send(Mensagem.MSG_INCOMPLETO, Mensagem.ERROR);
			return false;
		} else if (!this.validaEmail(empresa.getEmail())) {
			Mensagem.send(Mensagem.MSG_EMAIL, Mensagem.ERROR);
			return false;
		} else if ((empresa.getLogin() == null)
				|| (empresa.getLogin().trim().equals(""))) {
			Mensagem.send(Mensagem.MSG_INCOMPLETO, Mensagem.ERROR);
			return false;
		} else if ((empresa.getNomeFantasia() == null)
				|| (empresa.getNomeFantasia().trim().equals(""))) {
			Mensagem.send(Mensagem.MSG_INCOMPLETO, Mensagem.ERROR);
			return false;
		} else if ((empresa.getRazaoSocial() == null)
				|| (empresa.getRazaoSocial().trim().equals(""))) {
			Mensagem.send(Mensagem.MSG_INCOMPLETO, Mensagem.ERROR);
			return false;
		} else if ((empresa.getSenha() == null)
				|| (empresa.getSenha().trim().equals(""))) {
			Mensagem.send(Mensagem.MSG_INCOMPLETO, Mensagem.ERROR);
			return false;
		} else if ((!update) && (!validaUnique(empresa, pessoaFisica))) {
			return false;
		} else {
			if (!update) {
				empresa.setStatus("New");
			} else {
				// verifica se a senha foi alterada
				try {
					Empresa temp = new Empresa();
					temp.setId(empresa.getId());
					temp = this.controller.find(temp);
					if (!temp.getSenha().equals(empresa.getSenha())) {
						// criptografia da senha
						empresa.setSenha(this.crip(empresa.getSenha()));
					}
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		empresa.setSenha(this.crip(empresa.getSenha()));
		return true;
	}

	public boolean validaCPF(Object value) {
		String cpf = (String) value;
		if (cpf.length() != 11
				|| !calcularDigitoVerificador(cpf.substring(0, 9)).equals(
						cpf.substring(9, 11))) {
			return false;
		}
		return true;
	}

	private String calcularDigitoVerificador(String num) {
		Integer primDig, segDig;
		int soma = 0, peso = 10;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}
		if (soma % 11 == 0 | soma % 11 == 1) {
			primDig = new Integer(0);
		} else {
			primDig = new Integer(11 - (soma % 11));
		}
		soma = 0;
		peso = 11;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}
		soma += primDig.intValue() * 2;
		if (soma % 11 == 0 | soma % 11 == 1) {
			segDig = new Integer(0);
		} else {
			segDig = new Integer(11 - (soma % 11));
		}
		return primDig.toString() + segDig.toString();
	}

	// Da uma mensagem personalizada para Razao social, cnpj e login repetidos
	private boolean validaUnique(Empresa empresa, boolean pessoaFisica) {
		Empresa temp = new Empresa();
		try {
			temp = this.controller
					.getObjectByHQLCondition("from Empresa where razaoSocial = '"
							+ empresa.getRazaoSocial() + "'");
		} catch (Exception e) {
			temp = null;
			e.printStackTrace();
		}
		if (temp != null) {
			Mensagem.send(Mensagem.MSG_RAZAO_SOCIAL, Mensagem.ERROR);
			return false;
		}

		try {
			temp = this.controller
					.getObjectByHQLCondition("from Empresa where cnpj = '"
							+ empresa.getCnpj() + "'");
		} catch (Exception e) {
			temp = null;
			e.printStackTrace();
		}
		if (temp != null) {
			if (pessoaFisica) {
				Mensagem.send(Mensagem.MSG_CPF_UNIQUE, Mensagem.ERROR);
			} else {
				Mensagem.send(Mensagem.MSG_CNPJ_UNIQUE, Mensagem.ERROR);
			}

			return false;
		}

		try {
			temp = this.controller
					.getObjectByHQLCondition("from Empresa where login = '"
							+ empresa.getLogin() + "'");
		} catch (Exception e) {
			temp = null;
			e.printStackTrace();
		}
		if (temp != null) {
			Mensagem.send(Mensagem.MSG_LOGIN, Mensagem.ERROR);
			return false;
		}

		try {
			temp = this.controller
					.getObjectByHQLCondition("from Empresa where email = '"
							+ empresa.getEmail() + "'");
		} catch (Exception e) {
			temp = null;
			e.printStackTrace();
		}
		if (temp != null) {
			Mensagem.send(Mensagem.MSG_EMAIL, Mensagem.ERROR);
			return false;
		}
		return true;
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

	private String desfazCrip(String senha) {
		char[] crip = new char[senha.length()];
		char c;
		int charVal = 0;
		for (int i = 0; i < senha.length(); i++) {
			c = senha.charAt(i);
			charVal = c - 1;
			c = (char) charVal;
			crip[i] = c;
		}
		return String.valueOf(crip);
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
			// System.out.println("O E-mail " + email + " ï¿½ invï¿½lido");
			return false;
		}
	}

	private boolean validaCNPJ(String str_cnpj) {
		if (str_cnpj.length() != 14)
			return false;

		int soma = 0, dig;
		String cnpj_calc = str_cnpj.substring(0, 12);

		if (str_cnpj.length() != 14)
			return false;

		char[] chr_cnpj = str_cnpj.toCharArray();

		/* Primeira parte */
		for (int i = 0; i < 4; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
		dig = 11 - (soma % 11);

		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		/* Segunda parte */
		soma = 0;
		for (int i = 0; i < 5; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
		dig = 11 - (soma % 11);
		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		return str_cnpj.equals(cnpj_calc);
	}

	public List<Empresa> findList(Empresa empresa) {
		try {
			return controller.findList(empresa);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Empresa find(Empresa empresa) {

		try {
			return this.controller.find(empresa,
					Controller.SEARCH_EQUALS_STRING);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Estado> findEstados(Estado estado) {
		try {
			return (List<Estado>) this.controllerEstado.findList(estado);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public List<Cidade> findCidades(Estado estado) {
		Cidade cidade = new Cidade();
		cidade.setEstado(estado);
		try {
			return (List<Cidade>) this.controllerCidade.findList(cidade);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Plano> findPlanos(Plano plano) {
		try {
			return (List<Plano>) this.controllerPlano.findList(plano);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

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

	public Empresa validaRecovery() {
		// Parametros necessarios para recuperar: ID, RAZAO SOCIAL EMPRESA,
		try {
			Map<String, String> rec = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			String id = rec.get(this.crip("id"));
			String razaosSocial = rec.get(this.crip("razao"));
			Empresa temp = new Empresa();
			if ((id != null) && (razaosSocial != null)) {
				temp.setId(Long.valueOf(id));
				temp.setRazaoSocial(this.desfazCrip(razaosSocial));
				temp = this.controller.find(temp);
				return temp;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public Empresa enviarEmailRecuperarSenha(Empresa empresa) {
		try {
			empresa = this.controller.find(empresa);
			if (empresa != null) {
				String assunto = "Recuperar Senha";
				String mensagem = "Clique no link para alterar sua senha: \n \n extratosfacil.com.br/recuperar.html?je="
						+ empresa.getId()
						+ "&sb{bp="
						+ this.crip(empresa.getRazaoSocial());
				Email.sendEmail(empresa.getEmail(), empresa.getNomeFantasia(),
						assunto, mensagem);
				return empresa;
			}
		} catch (Exception e) {
			return new Empresa();
		}
		Mensagem.send(Mensagem.MSG_EMAIL_INVALIDO, Mensagem.ERROR);
		return new Empresa();
	}

	public Empresa validaConfirmar() {
		// Parametros necessarios para recuperar: ID, RAZAO SOCIAL EMPRESA,
		try {
			Map<String, String> rec = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			String razao = rec.get(this.crip("razaoSocial"));
			Empresa temp = new Empresa();
			if (razao != null) {
				temp.setRazaoSocial(this.desfazCrip(razao));
				temp = this.controller.find(temp);
				return temp;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void sendEmailConfirmacao(Empresa empresa) {
		String link = "www.extratosfacil.com.br/confirmar.html?sb{bpTpdjbm="
				+ this.crip(empresa.getRazaoSocial());
		String mensagem = "Cadastro realizado com sucesso. Clique no link para confirmar: "
				+ link
				+ "\n\n Caso nao consiga clicar no link copie e cole em seu navegador.";
		String assunto = "Cadastro Extratos Fácil";
		try {
			Email.sendEmail(empresa.getEmail(), empresa.getNomeFantasia(),
					assunto, mensagem);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public void validaVencimento(Empresa empresa) {
		Date hoje = new Date();
		if (empresa.getPlano() != null) {
			Date vencimento = empresa.getPlano().getVencimento();
			if (vencimento != null) {
				if (hoje.after(vencimento)) {
					SessionPlano sPlano = new SessionPlano();
					sPlano.bloquear(empresa.getPlano());
				}
			}
		}
	}
}
