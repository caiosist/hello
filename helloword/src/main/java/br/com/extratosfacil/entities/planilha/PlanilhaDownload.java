package br.com.extratosfacil.entities.planilha;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.extratosfacil.entities.Empresa;

/**
* Classe que representa a planilha que ser� dispon�vel para download
*
* @author Paulo Henrique da Silva
* @since 29/07/2015
* @version 1.0
* @category Entity
*/

@Entity
@Table(name="TBL_PLANILHA_DOWNLOAD")
public class PlanilhaDownload implements Serializable {

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

	@Column
	@Temporal(TemporalType.DATE)
	private Date data;

	/**
	 * 
	 */
	@Column(nullable = false)
	private String path;
	
	/**
	 * 
	 */
	@ManyToOne
	private Empresa empresa;
	
	/*-------------------------------------------------------------------
	 *				 		     CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	
	public PlanilhaDownload(){
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}