package br.com.extratosfacil.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe que representa um Veículo
 *
 * @author Caio Cesar Correia
 * @since 28/07/2015
 * @version 1.0
 * @category Entity
 */

@Entity
@Table(name = "TBL_VEICULO")
public class Veiculo implements Serializable {

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
	@Column(length = 100, nullable = false)
	private String modeloVeiculo;

	/**
	 * 
	 */
	@Column(length = 8, nullable = false)
	private String placaVeiculo;

	/**
	 * 
	 */
	@Column(nullable = false)
	private String categoria;

	/**
	 * 
	 */
	@Column(nullable = false)
	private Integer maximoEixo;
	/**
	 * 
	 */
	@ManyToOne
	private Empresa empresa;

	/*-------------------------------------------------------------------
	 *				 		     CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	public Veiculo() {
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

	public String getModeloVeiculo() {
		return modeloVeiculo;
	}

	public void setModeloVeiculo(String modeloVeiculo) {
		this.modeloVeiculo = modeloVeiculo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getMaximoEixo() {
		return maximoEixo;
	}

	public void setMaximoEixo(Integer maximoEixo) {
		this.maximoEixo = maximoEixo;
	}

	@Override
	public boolean equals(Object obj) {
		Veiculo v = (Veiculo) obj;
		return this.placaVeiculo.equals(v.getPlacaVeiculo());
	}
}