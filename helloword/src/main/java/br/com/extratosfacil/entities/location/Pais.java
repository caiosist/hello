package br.com.extratosfacil.entities.location;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe que representa um País
 *
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Entity
 */

@Entity
@Table(name = "TBL_PAIS")
public class Pais implements Serializable {

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
	private String nomePais;

	/**
	 * 
	 */
	@Column(length = 4, unique = true, nullable = false)
	private String sigla;

	/*-------------------------------------------------------------------
	 *				 		     CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	public Pais() {
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

	public String getNomePais() {
		return nomePais;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setNomePais(String nomePais) {
		this.nomePais = nomePais;
	}
}