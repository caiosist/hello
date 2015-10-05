package br.com.extratosfacil.constantes;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class Mensagem {

	public static String EMAIL_ENVIADO = "E-mail enviado com sucesso!";

	public static String MSG_ACEITO = "Você precisa Aceitar os Termos!";

	public static String MSG_SALVA = "Os dados foram salvos com sucesso!";

	public static String MSG_REMOVE = "Os dados foram Excluidos com sucesso!";

	public static String MSG_USER_NAO_CONFIRMADO = "Conta aguardando Confirmação!, Por favor, verifique o seu e-mail";

	public static String MSG_NOT_REMOVE = "Nao foi possivel excluir!";

	public static String MSG_UPDATE = "Os dados foram atualizados com sucesso!";

	public static String MSG_UPLOAD = "Upload realizado com sucesso! Clique em \"Download\" para baixar a planilha com as Correções!";

	public static String MSG_INCOMPLETO = "Por favor, preencha todos os campos!";

	public static String MSG_CNPJ = "CNPJ Invalido!";
	
	public static String MSG_CPF = "CPF Invalido!";

	public static String MSG_EMAIL = "E-mail Invalido ou já cadastrado!";
	
	public static String MSG_EMAIL_ENVIADO = "E-mail Enviado!";

	public static String MSG_USER_NAO_ENCONTRADO = "Usuário inválido!";

	public static String MSG_PLACA = "Já existe um veículo cadastrado com esta placa!";

	public static String MSG_LOGIN = "Login Já existente, por favor escolha um login diferente!";

	public static String MSG_CNPJ_UNIQUE = "Já existe uma empresa cadastrada com este CNPJ!";
	
	public static String MSG_CPF_UNIQUE = "Já existe um usuario cadastrado com este CPF!";

	public static String MSG_RAZAO_SOCIAL = "Já existe uma empresa cadastrada com esta razão social!";

	public static String MSG_PLANILHA_ERRADA = "A planilha enviada está incorreta ou foi modificada. Por favor, selecione a planilha correta";

	public static String MSG_EMPRESA_INVALIDA = "A planilha enviada não pertence a empresa cadastrada. Por favor, selecione a planilha correta!";

	public static String MSG_CONF_SENHA = "O campo Confirmar Senha não corresponde a senha digitada!";

	public static String MSG_EMAIL_INVALIDO = "O e-mail informado não está cadastrado em nossa base de dados!";

	public static String MSG_QUANTIDADE_INVALIDA = "Numero de veiculos nao pode ser 0 (Zero)";

	public static String MSG_PERIODO = "Selecione um periodo de pagamento!";

	public static String MSG_VALOR_PLANO = "Não é possivel alterar um plano já pago para um com valor inferior!";

	public static Severity INFO = FacesMessage.SEVERITY_INFO;

	public static Severity ERROR = FacesMessage.SEVERITY_ERROR;

	// Mostrar Mensagem ao ususario
	public static void send(String msg, Severity tipo) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(tipo, msg, ""));
	}

}