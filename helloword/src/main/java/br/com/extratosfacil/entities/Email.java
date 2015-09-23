package br.com.extratosfacil.entities;

import java.net.MalformedURLException;

import javax.print.DocFlavor.URL;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Email {

	private static String smtpServer = "mail.extratosfacil.com.br";
	private static Integer smtpPort = 587;
	private static String fromEmail = "contato@extratosfacil.com.br";
	private static String fromName = "Extratos Facil";
	private static String login = "contato@extratosfacil.com.br";
	private static String password = "Extratos*facil";

	public static void sendEmail(String destinatario, String nomeDestinatario,
			String assunto, String mensagem, String link) throws EmailException {
		mensagem = "<html><body>" + mensagem + "<a href=\"" + link
				+ "\">Link</a></body></html>";
		System.out.println(mensagem);
		HtmlEmail email = new HtmlEmail();
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
		email.setHtmlMsg(mensagem);
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
