package br.com.extratosfacil.entities;

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

import br.com.extratosfacil.entities.location.Cidade;

/**
 * Classe que representa uma Empresa
 *
 * @author Caio Cesar Correia
 * @since 28/07/2015
 * @version 1.0
 * @category Entity
 */

@Entity
@Table(name = "TBL_EMPRESA")
public class Empresa implements Serializable {

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
	@Column(length = 100, unique = true, nullable = false)
	private String razaoSocial;

	/**
	 * 
	 */
	@Column(length = 100, nullable = false)
	private String nomeFantasia;

	/**
	 * 
	 */
	@Column(length = 18, unique = true, nullable = false)
	private String cnpj;

	/**
	 * 
	 */
	@ManyToOne
	private Cidade cidade;

	/**
	 * 
	 */
	@ManyToOne
	private Plano plano;

	/**
	 * 
	 */
	@Column(length = 20, unique = true, nullable = false)
	private String login;

	/**
	 * 
	 */
	@Column(length = 20, nullable = false)
	private String senha;

	/**
	 * 
	 */
	@Column(length = 100, unique = true, nullable = false)
	private String email;

	/**
	 * 
	 */
	@Column(length = 20, nullable = false)
	private String status;

	/**
	 * 
	 */
	@Column
	@Temporal(TemporalType.DATE)
	private Date vencimento;

	/*-------------------------------------------------------------------
	 *				 		     CONSTRUCTORS
	 *-------------------------------------------------------------------*/

	public Empresa() {
		this.cidade = new Cidade();
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

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}
}