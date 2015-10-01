package br.com.extratosfacil.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.entities.Plano;
import br.com.extratosfacil.sessions.SessionPlano;

/**
 * Bean que representa a entidade Plano
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Bean
 */

@ManagedBean
@SessionScoped
public class BeanPlano {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	@Autowired
	private Plano plano = new Plano();

	@Autowired
	private Plano selected = new Plano();

	@Autowired
	private Plano filtro = new Plano();

	@Autowired
	private List<Plano> listaPlanos = new ArrayList<Plano>();

	@Autowired
	private SessionPlano session = new SessionPlano();

	private Integer periodo = 1;

	private Double desconto = (double) 0;

	private Double valorTotal = (double) 0;

	private boolean retorno = false;

	private boolean notificacao = false;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanPlano() {
	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	public SessionPlano getSession() {
		return session;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public boolean isRetorno() {
		this.checkRetorno();
		return retorno;
	}

	public void setRetorno(boolean retorno) {
		this.retorno = retorno;
	}

	public boolean isNotificacao() {
		this.checkNotificacao();
		return notificacao;
	}

	public void setNotificacao(boolean notificacao) {
		this.notificacao = notificacao;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setSession(SessionPlano session) {
		this.session = session;
	}

	public Plano getFiltro() {
		return filtro;
	}

	public void setFiltro(Plano filtro) {
		this.filtro = filtro;
	}

	public Plano getSelected() {
		return selected;
	}

	public void setSelected(Plano selected) {
		this.selected = selected;
	}

	public List<Plano> getListaPlanos() {
		return listaPlanos;
	}

	public void setListaPlanos(List<Plano> listaPlanos) {
		this.listaPlanos = listaPlanos;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void reinit() {
		this.plano = new Plano();
		this.filtro = new Plano();
		this.selected = new Plano();
		this.listaPlanos = new ArrayList<Plano>();
	}

	public String save() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.plano.getId() != null) {
			return this.update();
		}
		if (this.session.save(this.plano)) {
			this.reinit();
			Mensagem.send(Mensagem.MSG_SALVA, Mensagem.INFO);
			sucesso = true;
			context.addCallbackParam("sucesso", sucesso);
			return "";
		}

		context.addCallbackParam("sucesso", sucesso);
		return "";
	}

	public String update() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.session.validaPlano(this.plano)) {
			if (this.session.update(this.plano)) {
				this.reinit();
				this.carregaPlano();
				sucesso = true;
				context.addCallbackParam("sucesso", sucesso);
				Mensagem.send(Mensagem.MSG_UPDATE, Mensagem.INFO);
				return "";
			}
		}
		context.addCallbackParam("sucesso", sucesso);
		return null;

	}

	public String remove() {
		try {
			this.session.remove(this.plano);
			this.reinit();
			this.carregaPlano();
			Mensagem.send(Mensagem.MSG_REMOVE, Mensagem.INFO);
			return "";
		} catch (Exception e) {
			Mensagem.send(Mensagem.MSG_NOT_REMOVE, Mensagem.ERROR);
			return "";
		}
	}

	public void carregaPlano() {
		this.listaPlanos = this.session.findList(new Plano());
	}

	public void find() throws Exception {
		this.listaPlanos = this.session.findList(this.filtro);
	}

	public void novo() {
		this.setPlano(new Plano());
	}

	public String goToMeuPlano() {
		this.reinit();
		return "views/plano/meuPlano";
	}

	public String assinar() {

		if (this.validaPlano(this.plano)) {
			this.session.assinar(this.plano, this.periodo);
			return "views/plano/meuPlano";
		}
		return "";

	}

	public void pagar() {
		this.session.criarCheckout();
	}

	private boolean validaPlano(Plano p) {
		if ((p.getQuantidadeVeiculos() == null)
				|| (p.getQuantidadeVeiculos() == 0)) {
			Mensagem.send(Mensagem.MSG_QUANTIDADE_INVALIDA, Mensagem.ERROR);
			return false;
		}
		if ((this.periodo == null) || (this.periodo == 0)) {
			Mensagem.send(Mensagem.MSG_PERIODO, Mensagem.ERROR);
			return false;
		}
		return true;
	}

	public void alterar() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.validaPlano(this.plano)) {
			this.plano = this.session.alterar(this.plano, this.periodo);
			sucesso = true;
		}
		context.addCallbackParam("sucesso", sucesso);
	}

	public void aplicaDesconto() {
		this.desconto = (this.plano.getQuantidadeVeiculos() * 4.99);
		if (this.periodo == 3) {
			this.desconto = this.desconto * 0.06;
		} else if (this.periodo == 6) {
			this.desconto = this.desconto * 0.1;
		} else if (this.periodo == 12) {
			this.desconto = this.desconto * 0.13;
		} else {
			this.desconto = 0.0;
		}
		this.calculaValor();
	}

	public void calculaValor() {
		this.valorTotal = this.plano.getQuantidadeVeiculos() * 4.99;
		if (desconto > 0) {
			this.valorTotal = this.valorTotal - this.desconto;
		}
	}

	private void checkNotificacao() {
		this.session.checkNotificacao();
	}

	private void checkRetorno() {
		this.session.checkRetorno();

	}

}