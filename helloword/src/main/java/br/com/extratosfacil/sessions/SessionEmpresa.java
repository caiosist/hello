package br.com.extratosfacil.sessions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import org.hibernate.exception.ConstraintViolationException;

import br.com.extratosfacil.entities.Email;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Plano;
import br.com.extratosfacil.entities.location.Cidade;
import br.com.extratosfacil.entities.location.Estado;
import br.com.extratosfacil.messages.Mensagem;
import br.com.jbc.controller.Controller;

/**
 * Session que representa as regras de negócios da entidade Empresa
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

	public boolean save(Empresa empresa) {
		if (this.validaEmpresa(empresa, false)) {
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

	public boolean update(Empresa empresa) {
		if (this.validaEmpresa(empresa, true)) {
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

	public boolean validaEmpresa(Empresa empresa, boolean update) {
		if ((empresa.getCidade() == null)
				|| (empresa.getCidade().getId() == null)) {
			Mensagem.msgIncompleto();
			return false;
		} else if ((empresa.getCnpj() == null)
				|| (empresa.getCnpj().trim().equals(""))) {
			Mensagem.msgIncompleto();
			return false;
		} else if (!this.validaCNPJ(empresa.getCnpj())) {
			Mensagem.msgCNPJ();
			return false;
		} else if ((empresa.getEmail() == null)
				|| (empresa.getEmail().trim().equals(""))) {
			Mensagem.msgIncompleto();
			return false;
		} else if (!this.validaEmail(empresa.getEmail())) {
			Mensagem.msgEmail();
			return false;
		} else if ((empresa.getLogin() == null)
				|| (empresa.getLogin().trim().equals(""))) {
			Mensagem.msgIncompleto();
			return false;
		} else if ((empresa.getNomeFantasia() == null)
				|| (empresa.getNomeFantasia().trim().equals(""))) {
			Mensagem.msgIncompleto();
			return false;
		} else if ((empresa.getRazaoSocial() == null)
				|| (empresa.getRazaoSocial().trim().equals(""))) {
			Mensagem.msgIncompleto();
			return false;
		} else if ((empresa.getPlano() == null)
				|| (empresa.getPlano().getId() == null)) {
			Mensagem.msgIncompleto();
			return false;
		} else if ((empresa.getSenha() == null)
				|| (empresa.getSenha().trim().equals(""))) {
			Mensagem.msgIncompleto();
			return false;
		} else if ((!update) && (!validaUnique(empresa))) {
			return false;
		} else {
			if (!update) {
				empresa.setStatus("New");
				Date data = new Date();

				// Adicionamos 30 dias para o vencimento
				Calendar c = Calendar.getInstance();
				c.setTime(data);
				c.add(Calendar.DATE, +30);

				// Obtemos a data alterada
				data = c.getTime();
				empresa.setVencimento(data);
			
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

	// Da uma mensagem personalizada para Razao social, cnpj e login repetidos
	private boolean validaUnique(Empresa empresa) {
		Empresa temp = new Empresa();
		try {
			temp = this.controller.getObjectByHQLCondition("from Empresa where razaoSocial = '"	+ empresa.getRazaoSocial() + "'");
		} catch (Exception e) {
			temp = null;
			e.printStackTrace();
		}
		if (temp != null) {
			Mensagem.msgRasaoSocial();
			return false;
		}

		try {
			temp = this.controller.getObjectByHQLCondition("from Empresa where cnpj = '" + empresa.getCnpj() + "'");
		} catch (Exception e) {
			temp = null;
			e.printStackTrace();
		}
		if (temp != null) {
			Mensagem.msgCNPJ();
			return false;
		}

		try {
			temp = this.controller.getObjectByHQLCondition("from Empresa where login = '" + empresa.getLogin() + "'");
		} catch (Exception e) {
			temp = null;
			e.printStackTrace();
		}
		if (temp != null) {
			Mensagem.msgLogin();
			return false;
		}
		return true;
	}

	private String crip(String senha) {
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
			// System.out.println("O E-mail " + email + " � inv�lido");
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

	public List<Cidade> findCidades(Cidade cidade) {
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
				empresa = this.controller.find(empresa, Controller.SEARCH_EQUALS_STRING);
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
				String mensagem = "Clique no Link para alterar sua Senha \n \n extratosfacil.com.br/recuperar.html?je=1&sb{bp=uftuf";
				Email.sendEmail(empresa.getEmail(), empresa.getNomeFantasia(),
						assunto, mensagem);
				return empresa;
			}
		} catch (Exception e) {
			return new Empresa();
		}
		Mensagem.msgEmailInvalido();
		return new Empresa();
	}

	public Empresa validaConfirmar() {
		// Parametros necessarios para recuperar: ID, RAZAO SOCIAL EMPRESA,
		try {
			Map<String, String> rec = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			String id = rec.get(this.crip("id"));
			Empresa temp = new Empresa();
			if (id != null) {
				temp.setId(Long.valueOf(id));
				temp = this.controller.find(temp);
				return temp;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
