package br.com.extratosfacil.sessions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.constantes.Sessao;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Plano;
import br.com.jbc.controller.Controller;

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

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Controller<Plano> getController() {
		return controller;
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
		Sessao.redireciona("views/plano/meuPlano.html");
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
}