package br.com.extratosfacil.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.constantes.Sessao;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.sessions.SessionUsuario;

@ManagedBean
@SessionScoped
public class BeanLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean usuarioLogado = false;

	private Boolean usuarioLogin = false;

	private SessionUsuario session = null;

	private Empresa usuario = new Empresa();

	public Boolean getUsuarioLogado() {
		if (!usuarioLogado) {
			Sessao.redireciona("login.html");
		}
		return usuarioLogado;
	}

	public void setUsuarioLogado(Boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Empresa getUsuario() {
		return usuario;
	}

	public void setUsuario(Empresa usuario) {
		this.usuario = usuario;
	}

	public Boolean getUsuarioLogin() {
		if (usuarioLogado) {
			Sessao.redireciona("index.html");
		}
		return usuarioLogin;
	}

	public void setUsuarioLogin(Boolean usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	public SessionUsuario getSession() {
		return session;
	}

	public void setSession(SessionUsuario session) {
		this.session = session;
	}

	public String fazerLogin() {

		this.session = new SessionUsuario();

		this.usuario = session.efetuarLogin(this.usuario);

		if (this.usuario == null) {
			usuario = new Empresa();
			Mensagem.send(Mensagem.MSG_USER_NAO_ENCONTRADO, Mensagem.ERROR);
			return "";
		}

		if (this.usuario.getStatus().equals("New")) {
			Mensagem.send(Mensagem.MSG_USER_NAO_CONFIRMADO, Mensagem.ERROR);
			return "";
		}

		usuarioLogado = Boolean.TRUE;
		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		// .put("idUsuario", this.usuario.getId());

		this.session.validaVencimento(this.usuario);

		Sessao.setEmpresaSessao(usuario);

		session = null;
		Sessao.redireciona("index.html");
		return "";
	}

	public String logout() {
		this.usuario = new Empresa();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("usuario", null);

		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();

		return "login.html";
	}
}
