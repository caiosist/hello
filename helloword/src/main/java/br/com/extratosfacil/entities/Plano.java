package br.com.extratosfacil.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe que representa um Plano
 *
 * @author Caio Cesar Correia
 * @since 28/07/2015
 * @version 1.0
 * @category Entity
 */

@Entity
@Table(name = "TBL_PLANO")
public class Plano implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 
	 */
	@Column(length = 50, unique = true, nullable = false)
	private String nomePlano;

	/**
	 * 
	 */
	@Column(nullable = false)
	private Float valorPlano;

	/**
	 * 
	 */
	@Column(nullable = false)
	private Long quantidadeVeiculos;

	/**
	 * 
	 */
	@Column(length = 255, nullable = false)
	private String detalhes;

	/*-------------------------------------------------------------------
	 *				 		     CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	public Plano() {
	}

	/*-------------------------------------------------------------------
	 *				 		     GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomePlano() {
		return nomePlano;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public void setNomePlano(String nomePlano) {
		this.nomePlano = nomePlano;
	}

	public Float getValorPlano() {
		return valorPlano;
	}

	public void setValorPlano(Float valorPlano) {
		this.valorPlano = valorPlano;
	}

	public Long getQuantidadeVeiculos() {
		return quantidadeVeiculos;
	}

	public void setQuantidadeVeiculos(Long quantidadeVeiculos) {
		this.quantidadeVeiculos = quantidadeVeiculos;
	}

	@Override
	public String toString() {
		return this.getNomePlano();
	}
}