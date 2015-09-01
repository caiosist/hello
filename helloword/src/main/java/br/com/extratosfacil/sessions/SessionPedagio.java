package br.com.extratosfacil.sessions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.extratosfacil.entities.pedagio.Categoria;
import br.com.extratosfacil.entities.pedagio.Concessionaria;
import br.com.extratosfacil.entities.pedagio.Praca;
import br.com.extratosfacil.entities.pedagio.Rodovia;
import br.com.jbc.controller.Controller;

/**
 * Session que representa as regras de neg�cios da entidade Veiculo
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Session
 */

public class SessionPedagio {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	@Autowired
	private Controller<Concessionaria> controlConcessionaria = new Controller<Concessionaria>();

	@Autowired
	private Controller<Rodovia> controlRodovia = new Controller<Rodovia>();

	@Autowired
	private Controller<Categoria> controlCategoria = new Controller<Categoria>();

	@Autowired
	private Controller<Praca> controlPraca = new Controller<Praca>();

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Controller<Concessionaria> getControlConcessionaria() {
		return controlConcessionaria;
	}

	public void setControlConcessionaria(
			Controller<Concessionaria> controlConcessionaria) {
		this.controlConcessionaria = controlConcessionaria;
	}

	public Controller<Rodovia> getControlRodovia() {
		return controlRodovia;
	}

	public void setControlRodovia(Controller<Rodovia> controlRodovia) {
		this.controlRodovia = controlRodovia;
	}

	public Controller<Categoria> getControlCategoria() {
		return controlCategoria;
	}

	public void setControlCategoria(Controller<Categoria> controlCategoria) {
		this.controlCategoria = controlCategoria;
	}

	public Controller<Praca> getControlPraca() {
		return controlPraca;
	}

	public void setControlPraca(Controller<Praca> controlPraca) {
		this.controlPraca = controlPraca;
	}
	
	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public List<Concessionaria> findConcessionaria(Concessionaria concessionaria) {
		try {
			return controlConcessionaria.findList(concessionaria);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Praca> findPraca(Praca praca) {
		try {
			return controlPraca.findList(praca);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Rodovia> findRodovia(Rodovia rodovia) {
		try {
			return controlRodovia.findList(rodovia);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Categoria> findCategoria(Categoria categoria) {
		try {
			return controlCategoria.findList(categoria);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}