package br.com.extratosfacil.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.context.RequestContext;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.constantes.Sessao;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Veiculo;
import br.com.extratosfacil.sessions.SessionVeiculo;

/**
 * Bean que representa a entidade Veiculo
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Bean
 */

@ManagedBean
@ViewScoped
public class BeanVeiculo {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private Veiculo veiculo = new Veiculo();

	private Veiculo selected = new Veiculo();

	private Veiculo filtro = new Veiculo();

	private List<Veiculo> listaVeiculos = new ArrayList<Veiculo>();

	private SessionVeiculo session = new SessionVeiculo();

	private List<String> categorias = new ArrayList<String>();

	private List<Integer> eixos = new ArrayList<Integer>();

	private CommandButton botaoNovo;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanVeiculo() {
		this.carragaCategoria();
		this.carregaEixos();
		this.carregaVeiculo();
		this.verificaPlano();
	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	public SessionVeiculo getSession() {
		return session;
	}

	public void setSession(SessionVeiculo session) {
		this.session = session;
	}

	public Veiculo getFiltro() {
		return filtro;
	}

	public List<Integer> getEixos() {
		return eixos;
	}

	public void setEixos(List<Integer> eixos) {
		this.eixos = eixos;
	}

	public boolean isStatusBotaoNovo() {
		return this.verificaPlano();
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public CommandButton getBotaoNovo() {
		return botaoNovo;
	}

	public void setBotaoNovo(CommandButton botaoNovo) {
		this.botaoNovo = botaoNovo;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}

	public void setFiltro(Veiculo filtro) {
		this.filtro = filtro;
	}

	public Veiculo getSelected() {
		return selected;
	}

	public void setSelected(Veiculo selected) {
		this.selected = selected;
	}

	public List<Veiculo> getListaVeiculos() {
		return listaVeiculos;
	}

	public void setListaVeiculos(List<Veiculo> listaVeiculos) {
		this.listaVeiculos = listaVeiculos;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void reinit() {
		this.veiculo = new Veiculo();
		this.filtro = new Veiculo();
		this.selected = new Veiculo();
		this.listaVeiculos = new ArrayList<Veiculo>();
		this.carregaVeiculo();
	}

	public void limpar() {
		this.filtro = new Veiculo();
	}

	public String save() throws Exception {
		
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.veiculo.getId() != null) {
			return this.update();
		}
		if (this.session.validaVeiculo(this.veiculo)) {
			if (this.session.save(veiculo)) {
				this.reinit();
				this.carregaVeiculo();
				Mensagem.send(Mensagem.MSG_SALVA, Mensagem.INFO);
				sucesso = true;
				context.addCallbackParam("sucesso", sucesso);
				this.verificaPlano();
				return "";
			}
		}
		context.addCallbackParam("sucesso", sucesso);
		return "getVeiculo";
	}

	public String update() {
		
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.session.validaVeiculo(this.veiculo)) {
			if (this.session.update(this.veiculo)) {
				this.reinit();
				this.carregaVeiculo();
				sucesso = true;
				context.addCallbackParam("sucesso", sucesso);
				this.verificaPlano();
				Mensagem.send(Mensagem.MSG_UPDATE, Mensagem.INFO);
				return "";
			}
		}
		context.addCallbackParam("sucesso", sucesso);
		return null;

	}

	public String remove() {
		try {
			this.session.remove(this.veiculo);
			this.reinit();
			this.carregaVeiculo();
			this.verificaPlano();
			Mensagem.send(Mensagem.MSG_REMOVE, Mensagem.INFO);
			return "";
		} catch (Exception e) {
			Mensagem.send(Mensagem.MSG_NOT_REMOVE, Mensagem.ERROR);
			return "";
		}
	}

	public void carregaVeiculo() {
		this.veiculo.setEmpresa(Sessao.getEmpresaSessao());
		this.listaVeiculos = this.session.findList(this.veiculo);
	}

	public void find() throws Exception {
		this.filtro.setEmpresa(Sessao.getEmpresaSessao());
		this.listaVeiculos = this.session.findList(this.filtro);
	}

	public void novo() {
		this.setVeiculo(new Veiculo());
	}

	private void carragaCategoria() {
		this.categorias.add("Caminhão 2 eixos");
		this.categorias.add("Caminhão 3 eixos");
		this.categorias.add("Caminhão 4 eixos");
		this.categorias.add("Caminhão 5 eixos");
		this.categorias.add("Caminhão 6 eixos");
		this.categorias.add("Caminhão 7 eixos");
		this.categorias.add("Caminhão 8 eixos");
		this.categorias.add("Caminhão 9 eixos");
		this.categorias.add("Caminhão 10 eixos");
		this.categorias.add("Ônibus 2 eixos");
		this.categorias.add("Ônibus 3 eixos");
		this.categorias.add("Ônibus 4 eixos");
	}

	private void carregaEixos() {
		this.eixos.add(2);
		this.eixos.add(3);
		this.eixos.add(4);
		this.eixos.add(5);
		this.eixos.add(6);
		this.eixos.add(61);
		this.eixos.add(62);
		this.eixos.add(63);
		this.eixos.add(64);
	}

	private boolean verificaPlano() {
		Empresa empresaTemp = Sessao.getEmpresaSessao();

		if ((empresaTemp != null) && (botaoNovo != null)) {

			if (empresaTemp.getPlano().getQuantidadeVeiculos() <= this.listaVeiculos
					.size()) {
				this.botaoNovo.setDisabled(true);
			} else {
				this.botaoNovo.setDisabled(false);
			}
			return this.botaoNovo.isDisabled();
		}
		return false;

	}

	public String goToVeiculo() {
		this.reinit();

		Empresa empresa = Sessao.getEmpresaSessao();
		if (empresa.getStatus().equals("Ativo")) {
			return "views/veiculo/consultaVeiculo";
		}
		return "index";

	}

}