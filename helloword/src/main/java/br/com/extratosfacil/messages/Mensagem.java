package br.com.extratosfacil.messages;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensagem {

	public static String msgSave() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Os dados foram salvos com sucesso", ""));
		return "";
	}

	public static String msgRemove() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Os dados foram excluídos com sucesso", ""));
		return "";
	}
	
	public static void msgUsuarioNaoConfirmado() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Conta aguardando confirmação!", "Por favor, verifique o seu e-mail."));
		
	}

	public static String msgNotRemove() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Não foi possível excluir!", ""));
		return "";
	}

	public static String msgUpdate() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Os dados foram atualizados com sucesso!", ""));
		return "";
	}

	public static String msgUpload() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Upload realizado com sucesso!", "Clique em \"Baixar Planilha\" para baixar a planilha com as correções!"));
		return "";
	}
	
	public static void msgIncompleto() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Por favor, preencha todos os campos!", ""));

	}

	public static void msgCNPJ() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ inválido!",
						""));

	}

	public static void msgEmail() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"E-mail inválido ou já cadastrado!", ""));
	}

	public static void msgUsuarioNaoEncontrado() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Usuário inválido!", ""));

	}

	public static void msgPlaca() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Já existe um veículo cadastrado com esta placa!", ""));

	}

	public static void msgLogin() {
		FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Login já existente, por favor escolha um login diferente!",
								""));

	}

	public static void msgCNPJUnique() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Já existe uma empresa cadastrada com este CNPJ!", ""));

	}

	public static void msgRasaoSocial() {
		FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Já existe uma empresa cadastrada com esta razão social!",
								""));

	}

	public static void msgPlanilhaErrada() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"A planilha enviada está incorreta ou foi modificada.",
						"Por favor, selecione a planilha correta!"));

	}

	public static void msgEmpresaInvalida() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"A planilha enviada não pertence a empresa cadastrada.",
						"Por favor, selecione a planilha correta!"));
		
	}

	public static void msgConfSenha() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"O campo Confirmar Senha não corresponde a senha digitada!",
						""));
		
	}

	public static void msgEmailInvalido() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"O e-mail informado não está cadastrado em nossa base de dados!",
						""));
	}
}