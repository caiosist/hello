package br.com.extratosfacil.entities;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class Email {

	private static String smtpServer = "mail.todentro.net";
	private static Integer smtpPort = 587;
	private static String fromEmail = "contato@todentro.net";
	private static String fromName = "teste";
	private static String login = "contato@todentro.net";
	private static String password = "adc360!!";

	public static void sendEmail(String destinatario, String nomeDestinatario,
			String assunto, String mensagem) throws EmailException {

		SimpleEmail email = new SimpleEmail();
		// Utilize o hostname do seu provedor de email
		// System.out.println("alterando hostname...");
		email.setHostName(smtpServer);
		// Quando a porta utilizada não é a padrão (integrator = 587)
		email.setSmtpPort(smtpPort);
		// Adicione os destinatários
		email.addTo(destinatario, nomeDestinatario);
		// Configure o seu email do qual enviará
		email.setFrom(fromEmail, fromName);
		// Adicione um assunto
		email.setSubject(assunto);
		// Adicione a mensagem do email
		email.setMsg(mensagem);
		// Para autenticar no servidor é necessário chamar os dois métodos
		// abaixo
		// System.out.println("autenticando...");
		// email.setSSL(true);
		// email.setSslSmtpPort("587");
		email.setAuthentication(login, password);
		// System.out.println("enviando...");
		email.send();
		// System.out.println("Email enviado!");
	}
}
