package br.com.extratosfacil.sessions;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.constantes.Sessao;
import br.com.extratosfacil.entities.Compra;
import br.com.extratosfacil.entities.Email;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Plano;
import br.com.jbc.controller.Controller;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.NotificationService;

/**
 * Session que representa as regras de negócios da entidade Plano
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Session
 */

public class SessionPlano {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	@Autowired
	private Controller<Plano> controller = new Controller<Plano>();
	private Controller<Empresa> controllerEmpresa = new Controller<Empresa>();
	private Controller<Compra> controllerCompra = new Controller<Compra>();

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Controller<Plano> getController() {
		return controller;
	}

	public Controller<Empresa> getControllerEmpresa() {
		return controllerEmpresa;
	}

	public void setControllerEmpresa(Controller<Empresa> controllerEmpresa) {
		this.controllerEmpresa = controllerEmpresa;
	}

	public Controller<Compra> getControllerCompra() {
		return controllerCompra;
	}

	public void setControllerCompra(Controller<Compra> controllerCompra) {
		this.controllerCompra = controllerCompra;
	}

	public void setController(Controller<Plano> controller) {
		this.controller = controller;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public boolean save(Plano plano) {
		if (this.validaPlano(plano)) {
			try {
				this.controller.insertReturnId(plano);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public boolean update(Plano plano) {
		if (this.validaPlano(plano)) {
			try {
				this.controller.update(plano);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean remove(Plano plano) {
		try {
			this.controller.delete(plano);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean validaPlano(Plano plano) {
		return true;
	}

	public List<Plano> findList(Plano plano) {
		try {
			return controller.findList(plano);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Plano find(Plano plano) {

		try {
			return this.controller.find(plano, Controller.SEARCH_EQUALS_STRING);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Float getValorPlano(Integer periodo, Long numeroVeiculos) {
		Float valor = numeroVeiculos * new Float(4.99);
		if (periodo == 3) {
			valor = 3 * valor * new Float(0.94);
		}
		if (periodo == 6) {
			valor = 6 * valor * new Float(0.90);
		}
		if (periodo == 12) {
			valor = 12 * valor * new Float(0.87);
		}
		return valor;
	}

	public Date getVencimento(Integer periodo) {
		Date data = new Date();
		Integer dias = 30 * periodo;

		// Adicionamos 30,90,180 ou 360 dias para o vencimento
		Calendar c = Calendar.getInstance();
		c.setTime(data);

		c.add(Calendar.DATE, +dias);

		// Obtemos a data alterada
		data = c.getTime();

		return data;
	}

	public String getNomePlano(Integer periodo) {
		if (periodo == 3) {
			return "Plano Trimestral";
		} else if (periodo == 6) {
			return "Plano Semestral";
		} else if (periodo == 12) {
			return "Plano Anual";
		} else {
			return "Plano Mensal";
		}
	}

	public void setPlanoEmpresa(Plano plano) {

		Empresa empresa = Sessao.getEmpresaSessao();

		Controller<Empresa> cEmpresa = new Controller<Empresa>();

		empresa.setStatus("Pendente");
		empresa.setPlano(plano);
		try {
			cEmpresa.update(empresa);
			Sessao.setEmpresaSessao(empresa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void assinar(Plano plano, Integer periodo) {
		Float valor = this
				.getValorPlano(periodo, plano.getQuantidadeVeiculos());
		if (plano.getCredito() == null) {
			plano.setCredito(0f);
		} else {
			valor = valor - plano.getCredito();
		}
		plano.setValorPlano(valor);
		// plano.setVencimento(this.getVencimento(periodo));
		plano.setPeriodo(periodo);
		plano.setNomePlano(this.getNomePlano(periodo));
		plano.setStatus("Aguardando Pagamento");
		this.save(plano);
		this.setPlanoEmpresa(plano);
		//Sessao.redireciona("views/plano/meuPlano.html");
	}

	public void pagar() {
		// Atualiza a empresa para Ativa
		Empresa empresa = Sessao.getEmpresaSessao();
		empresa.setStatus("Ativo");
		Controller<Empresa> cEmpresa = new Controller<Empresa>();
		try {
			cEmpresa.update(empresa);
			Sessao.setEmpresaSessao(empresa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Atualiza o plano pra pago
		Plano plano = empresa.getPlano();
		plano.setStatus("Ativo");
		plano.setVencimento(this.getVencimento(plano.getPeriodo()));
		this.update(plano);

	}

	public Plano alterar(Plano novoPlano, Integer periodo) {
		// Atualiza a empresa para Ativa
		Empresa empresa = Sessao.getEmpresaSessao();
		Plano plano = empresa.getPlano();

		if (plano.getStatus().equals("Ativo")) {
			Date dataHoje = new Date();
			// verifica quantos dias restam do plano
			int diasRestantes = (int) ((plano.getVencimento().getTime() - dataHoje
					.getTime()) / 86400000L);
			Float credito = 0f;
			if (diasRestantes > 0) {
				// Verifica a porcentagem do plano restante
				int totalDias = plano.getPeriodo() * 30;
				credito = Float.valueOf((diasRestantes * 100) / totalDias);
				// seta a porcentagem de credito
				credito = ((credito / 100) * (plano.getValorPlano() + plano
						.getCredito()));
			}

			Float valor = this.getValorPlano(periodo,
					novoPlano.getQuantidadeVeiculos())
					- credito;

			if (valor < 0) {
				Mensagem.send(Mensagem.MSG_VALOR_PLANO, Mensagem.ERROR);
				return plano;
			}

			novoPlano.setCredito(credito);
			novoPlano.setValorPlano(valor);
			novoPlano.setStatus("Aguardando Pagamento");
			novoPlano.setNomePlano(this.getNomePlano(periodo));
			novoPlano.setPeriodo(periodo);
			novoPlano.setId(plano.getId());
			this.setPlanoEmpresa(novoPlano);
			this.update(novoPlano);
		} else {

			Float valor = this.getValorPlano(periodo,
					novoPlano.getQuantidadeVeiculos());
			if (plano.getCredito() == null) {
				novoPlano.setCredito(0f);
			} else {
				valor = valor - plano.getCredito();
			}

			if (valor < 0) {
				Mensagem.send(Mensagem.MSG_VALOR_PLANO, Mensagem.ERROR);
				return plano;
			}

			novoPlano.setId(plano.getId());
			novoPlano.setCredito(plano.getCredito());
			novoPlano.setValorPlano(valor);
			novoPlano.setPeriodo(periodo);
			novoPlano.setNomePlano(this.getNomePlano(periodo));
			novoPlano.setStatus("Aguardando Pagamento");
			this.update(novoPlano);
			this.setPlanoEmpresa(novoPlano);
		}
		// this.remove(plano);
		return novoPlano;

	}

	public void bloquear(Plano plano) {
		// Atualiza a empresa para Ativa
		Empresa empresa = Sessao.getEmpresaSessao();
		empresa.setStatus("Pendente");
		Controller<Empresa> cEmpresa = new Controller<Empresa>();
		try {
			cEmpresa.update(empresa);
			Sessao.setEmpresaSessao(empresa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Atualiza o plano pra pago
		plano = empresa.getPlano();
		plano.setStatus("Pendente");
		this.update(plano);

	}

	public void criarCheckout() {
		Locale.setDefault(Locale.US);
		Empresa empresa = Sessao.getEmpresaSessao();
		Plano plano = empresa.getPlano();
		Checkout checkout = new Checkout();

		Double valor = Double.valueOf(plano.getValorPlano());
		BigDecimal valorFormatado = BigDecimal.valueOf(valor);
		valorFormatado = formataDecimal(valorFormatado);

		String nomePlano = "Plano - " + plano.getQuantidadeVeiculos()
				+ " Veiculos - " + plano.getPeriodo();

		if (plano.getPeriodo() == 1) {
			nomePlano += " Mês";
		} else {
			nomePlano += " Mêses";
		}

		checkout.addItem(String.valueOf(plano.getId()), //
				nomePlano, //
				Integer.valueOf(1), //
				valorFormatado, //
				null, null);

		checkout.setCurrency(Currency.BRL);
		checkout.setReference(String.valueOf(empresa.getId()));
		checkout.setRedirectURL("http://extratosfacil.com/retorno_compra.html");

		Compra compra = new Compra();
		compra.setEmpresa(empresa);
		compra.setPlano(plano);
		compra.setReferencia(String.valueOf(empresa.getId()));
		compra.setStatus("Aguardando Pagamento");

		try {

			Boolean onlyCheckoutCode = false;

			String checkoutURL = checkout.register(
					PagSeguroConfig.getAccountCredentials(), onlyCheckoutCode);

			controllerCompra.insert(compra);
			Sessao.redirect(checkoutURL);

		} catch (Exception e) {
			try {
				Email.sendEmail("problemas@extratosfacil.com.br",
						empresa.getRazaoSocial(), "Erro no Pagamento",
						"Erro no pagamento Pagseguro" + e.getMessage(), "");
			} catch (EmailException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public static BigDecimal formataDecimal(BigDecimal vlrFator) {
		BigDecimal numFormatado = vlrFator.setScale(2, BigDecimal.ROUND_UP);
		return numFormatado;
	}

	public void checkRetorno() {
		// try {
		// Map<String, String> rec = FacesContext.getCurrentInstance()
		// .getExternalContext().getRequestParameterMap();
		// String transaction = rec.get("transaction_id");
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public void checkNotificacao() {
		Map<String, String> rec = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();

		String notificationCode = rec.get("notificationCode");

		Transaction transaction = null;

		try {

			transaction = NotificationService.checkTransaction(
					PagSeguroConfig.getAccountCredentials(), notificationCode);

			if (transaction != null) {
				if (transaction.getStatus() == TransactionStatus.PAID) {

					Empresa empresa = new Empresa();
					empresa.setId(Long.valueOf(transaction.getReference()));
					empresa = controllerEmpresa.find(empresa);

					if (empresa != null) {
						empresa.getPlano().setStatus("Ativo");
						empresa.setStatus("Ativo");
						controllerEmpresa.update(empresa);
						empresa.getPlano().setVencimento(
								this.getVencimento(empresa.getPlano()
										.getPeriodo()));
						this.update(empresa.getPlano());
					} else {
						this.sendEmailErroReference(transaction.getReference());
					}
				}
			}
		} catch (PagSeguroServiceException e1) {
			this.sendEmailErroNotificacao(notificationCode, e1);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	private void sendEmailErroNotificacao(String notificationCode,
			PagSeguroServiceException e1) {
		try {
			Email.sendEmail("problemas@extratosfacil.com.br", notificationCode,
					"erro notificacao", "A notificacao: " + notificationCode
							+ "Deu erro: " + e1.getMessage(), "");
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendEmailErroReference(String referencia) {
		try {
			Email.sendEmail("problemas@extratosfacil.com.br", referencia,
					"erro apos pagamento", "A referencia da empresa de ID "
							+ referencia + "Deu erro", "");
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}