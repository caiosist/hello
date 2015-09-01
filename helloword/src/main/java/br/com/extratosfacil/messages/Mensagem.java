package br.com.extratosfacil.messages;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensagem {

	public static String msgSave() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Salvo com sucesso", ""));
		return "";
	}

	public static String msgRemove() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Excluido com sucesso", ""));
		return "";
	}

	public static String msgNotRemove() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Não é possivel excluir!", ""));
		return "";
	}

	public static String msgUpdate() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Atualizado com sucesso!", ""));
		return "";
	}

	public static String msgUpload() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Upload realizado com sucesso!", "Clique em \"Baixar Planilha\" para Baixar a planilha com as Correções!"));
		return "";
	}
	
	public static void msgIncompleto() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Por Favor preencha todos os campos!", ""));

	}

	public static void msgCNPJ() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ Invalido!",
						""));

	}

	public static void msgEmail() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"E-mail Invalido!", ""));
	}

	public static void msgUsuarioNaoEncontrado() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Usuario Invalido!", ""));

	}

	public static void msgPlaca() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Já Existe um veículo cadastrado com esta Placa!", ""));

	}

	public static void msgLogin() {
		FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Login existente, por favor escolha um login diferente!",
								""));

	}

	public static void msgCNPJUnique() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Já Existe uma Empresa cadastrada com este CNPJ!", ""));

	}

	public static void msgRasaoSocial() {
		FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Já Existe uma Empresa cadastrada com esta Rasão Social!",
								""));

	}

	public static void msgPlanilhaErrada() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"A planilha enviada esta incorreta ou foi modificada!",
						"Por favor selecione a planilha correta!"));

	}

	public static void msgEmpresaInvalida() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"A planilha enviada não pertence a empresa cadastrada!",
						"Por favor selecione a planilha correta!"));
		
	}

	public static void msgConfSenha() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"O campo Confirmar Senha não corresponde a Senha digitada!",
						""));
		
	}

	public static void msgEmailInvalido() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"O E-mail informado não esta cadastrado em nossa base de dados!",
						""));
	}
}
