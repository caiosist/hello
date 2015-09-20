package br.com.extratosfacil.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	@Column(length = 50, nullable = false)
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
	private Float credito;

	/**
	 * 
	 */
	@Column(nullable = false)
	private Long quantidadeVeiculos;

	/**
	 * 
	 */
	@Column(length = 255, nullable = false)
	private String status;

	/**
	 * 
	 */
	@Column
	@Temporal(TemporalType.DATE)
	private Date vencimento;

	/**
	 * Numero de meses do plano
	 */
	@Column
	private Integer periodo;

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

	public Date getVencimento() {
		return vencimento;
	}

	public Float getCredito() {
		return credito;
	}

	public void setCredito(Float credito) {
		this.credito = credito;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNomePlano() {
		return nomePlano;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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