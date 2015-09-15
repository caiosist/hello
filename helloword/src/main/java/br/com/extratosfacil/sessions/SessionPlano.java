package br.com.extratosfacil.sessions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
				this.controller.insert(plano);
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

	public boolean remove(Plano plano) throws Exception {
		this.controller.delete(plano);
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
}