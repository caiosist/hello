package br.com.extratosfacil.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.extratosfacil.entities.pedagio.Categoria;
import br.com.extratosfacil.entities.pedagio.Concessionaria;
import br.com.extratosfacil.entities.pedagio.Praca;
import br.com.extratosfacil.entities.pedagio.Rodovia;
import br.com.extratosfacil.sessions.SessionPedagio;

/**
 * Bean que representa a entidade Veiculo
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Bean
 */

@ManagedBean(name = "beanPedagio", eager = true)
@SessionScoped
public class BeanPedagio {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	@Autowired
	private List<Categoria> categorias = new ArrayList<Categoria>();

	@Autowired
	private List<Praca> pracas = new ArrayList<Praca>();

	@Autowired
	private List<Rodovia> rodovias = new ArrayList<Rodovia>();

	@Autowired
	private List<Concessionaria> concessionarias = new ArrayList<Concessionaria>();

	private Rodovia rodovia = new Rodovia();

	private Concessionaria concessionaria = new Concessionaria();

	private Praca praca = new Praca();

	private Categoria categoria = new Categoria();

	@Autowired
	private SessionPedagio session = new SessionPedagio();

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanPedagio() {
		this.carregaConcessionaria();
		// this.carregaPraca();
	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Praca> getPracas() {
		return pracas;
	}

	public void setPracas(List<Praca> pracas) {
		this.pracas = pracas;
	}

	public List<Rodovia> getRodovias() {
		return rodovias;
	}

	public void setRodovias(List<Rodovia> rodovias) {
		this.rodovias = rodovias;
	}

	public List<Concessionaria> getConcessionarias() {
		return concessionarias;
	}

	public void setConcessionarias(List<Concessionaria> concessionarias) {
		this.concessionarias = concessionarias;
	}

	public Rodovia getRodovia() {
		return rodovia;
	}

	public void setRodovia(Rodovia rodovia) {
		this.rodovia = rodovia;
	}

	public Concessionaria getConcessionaria() {
		return concessionaria;
	}

	public void setConcessionaria(Concessionaria concessionaria) {
		this.concessionaria = concessionaria;
	}

	public Praca getPraca() {
		return praca;
	}

	public void setPraca(Praca praca) {
		this.praca = praca;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public SessionPedagio getSession() {
		return session;
	}

	public void setSession(SessionPedagio session) {
		this.session = session;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void carregaConcessionaria() {
		this.concessionarias = this.session
				.findConcessionaria(new Concessionaria());
	}

	public void carregaPraca() {
		this.praca.setConcessionaria(this.concessionaria);
		this.pracas = this.session.findPraca(this.praca);
	}

	public void carregaCategoria() {
		this.categorias = this.session.findCategoria(this.categoria);
	}

	public void caregaPracas() {
		try {
			this.concessionaria = this.session.getControlConcessionaria().find(
					concessionaria);
			this.pracas = this.session.getControlPraca().getListByHQLCondition(
					"from Praca where concessionaria_id = "
							+ this.concessionaria.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}