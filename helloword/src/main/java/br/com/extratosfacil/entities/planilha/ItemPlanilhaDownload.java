package br.com.extratosfacil.entities.planilha;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe que representa os itens da planilha de download
 *
 * @author Paulo Henrique da Silva
 * @since 11/08/2015
 * @version 1.0
 * @category Entity
 */

public class ItemPlanilhaDownload implements Serializable {

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
	@Column(length = 8, nullable = false)
	private String placa;

	/**
	 * 
	 */
	@Column
	@Temporal(TemporalType.DATE)
	private Date data;

	/**
	 * 
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date hora;

	/**
	 * 
	 */
	@Column(length = 100, nullable = false)
	private String concessionaria;

	/**
	 * 
	 */
	@Column(length = 100, nullable = false)
	private String praca;

	/**
	 * 
	 */
	@Column(nullable = false)
	private Integer categoria;

	/**
	 * 
	 */
	@Column(nullable = false)
	private Integer categoriaCorreta;

	/**
	 * 
	 */
	@Column
	private Double valor;

	/**
	 * 
	 */
	@Column(length = 20, nullable = false)
	private Double valorCorreto;

	/**
	 * 
	 */
	@Column(length = 20, nullable = false)
	private Double valorRestituicao;

	/**
	 * 
	 */
	@Column(length = 100, nullable = false)
	private String obs;

	/*-------------------------------------------------------------------
	 *				 		     CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	public ItemPlanilhaDownload() {
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

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValorCorreto() {
		return valorCorreto;
	}

	public void setValorCorreto(Double valorCorreto) {
		this.valorCorreto = valorCorreto;
	}

	public Double getValorRestituicao() {
		return valorRestituicao;
	}

	public void setValorRestituicao(Double valorRestituicao) {
		this.valorRestituicao = valorRestituicao;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public String getConcessionaria() {
		return concessionaria;
	}

	public void setConcessionaria(String concessionaria) {
		this.concessionaria = concessionaria;
	}

	public String getPraca() {
		return praca;
	}

	public void setPraca(String praca) {
		this.praca = praca;
	}

	public Integer getCategoria() {
		return categoria;
	}

	public Integer getCategoriaCorreta() {
		return categoriaCorreta;
	}

	public void setCategoriaCorreta(Integer categoriaCorreta) {
		this.categoriaCorreta = categoriaCorreta;
	}

	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer formataCategoria(Integer cat) {
		if (cat > 6) {
			int soma = 0;
			while (cat != 0) {
				soma += cat % 10;
				cat = cat / 10;
			}
			cat = soma;
		}
		return cat;
	}
}