package br.com.extratosfacil.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.entities.Usuario;
import br.com.extratosfacil.sessions.SessionUsuario;

/**
 * Bean que representa a entidade Plano
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Bean
 */

public class BeanUsuario {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private SessionUsuario session = new SessionUsuario();

	private List<Usuario> usuarios = new ArrayList<Usuario>();

	private Usuario usuario = new Usuario();

	private Usuario filtro = new Usuario();

	private Boolean usuarioLogado = false;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanUsuario() {

	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	public Usuario getUsuario() {
		this.usuario = (Usuario) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("usuario");
		if (usuario == null) {
			return new Usuario();
		}
		return usuario;
	}

	public SessionUsuario getSession() {
		return session;
	}

	public void setSession(SessionUsuario session) {
		this.session = session;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getFiltro() {
		return filtro;
	}

	public void setFiltro(Usuario filtro) {
		this.filtro = filtro;
	}

	public Boolean getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void findList() {
		this.usuarios = this.session.findList(filtro);
		this.filtro = new Usuario();
	}

	public void find() {
		// Usuario u = this.session.find(filtro);
		// this.filtro = new Usuario();
	}

	public void save() {

		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.session.save(usuario)) {
			Mensagem.msgSave();
			sucesso = true;
			this.findList();
		}
		context.addCallbackParam("sucesso", sucesso);
	}

	public void update() {
		this.session.update(usuario);
		this.findList();
	}

	public void delete() {
		if (this.session.delete(usuario)) {
			Mensagem.msgRemove();
			this.findList();
			return;
		} else {
			Mensagem.msgNotRemove();
			return;
		}
	}

	public void novo() {
		this.usuario = new Usuario();
	}

	public void logout() {
		this.usuario = new Usuario();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("usuario", null);

		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/helloword/login.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
	}

	public void fazerLogin() {

		try {
			Usuario u = this.session.efetuarLogin(usuario);

			if (u == null) {
				// Mensagem.msgUsuarioNaoEncontrado();
			} else {
				usuarioLogado = Boolean.TRUE;
				this.usuario = u;
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/projetoES/index.jsf");
				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("usuario", this.usuario);
			}

		} catch (Exception e) {
			// Mensagens.msgUsuarioNaoEncontrado();
		}
	}

}
