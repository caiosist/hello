package br.com.extratosfacil.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.mail.EmailException;
import org.primefaces.context.RequestContext;

import br.com.extratosfacil.entities.Email;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Plano;
import br.com.extratosfacil.entities.location.Cidade;
import br.com.extratosfacil.entities.location.Estado;
import br.com.extratosfacil.messages.Mensagem;
import br.com.extratosfacil.sessions.SessionEmpresa;

/**
 * Bean que representa a entidade Empresa
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Bean
 */

@ManagedBean
@SessionScoped
public class BeanEmpresa {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private Empresa empresa = new Empresa();

	private Empresa usuario = new Empresa();

	private Empresa selected = new Empresa();

	private Empresa filtro = new Empresa();

	private List<Empresa> empresas = new ArrayList<Empresa>();

	private List<Estado> estados = new ArrayList<Estado>();

	private List<Cidade> cidades = new ArrayList<Cidade>();

	private List<Plano> planos = new ArrayList<Plano>();

	private SessionEmpresa session = new SessionEmpresa();

	private Boolean usuarioLogado = false;

	private boolean recuperar = false;

	private boolean confirmar = false;

	private String confSenha = new String();

	private String senha = new String();

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanEmpresa() {
		this.carregaEstados();
		this.carregaPlanos();

	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	public SessionEmpresa getSession() {
		return session;
	}

	public void setSession(SessionEmpresa session) {
		this.session = session;
	}

	public Empresa getFiltro() {
		return filtro;
	}

	public String getSenha() {
		return senha;
	}

	public Empresa getUsuario() {
		return usuario;
	}

	public void setUsuario(Empresa usuario) {
		this.usuario = usuario;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isRecuperar() {
		recuperar = this.validaRecovery();
		return recuperar;
	}

	public boolean isConfirmar() {
		this.confirmar = this.confirmaCadastro();
		return confirmar;
	}

	public void setConfirmar(boolean confirmar) {
		this.confirmar = confirmar;
	}

	public void setRecuperar(boolean recuperar) {
		this.recuperar = recuperar;
	}

	public void setFiltro(Empresa filtro) {
		this.filtro = filtro;
	}

	public Empresa getSelected() {
		return selected;
	}

	public String getConfSenha() {
		return confSenha;
	}

	public void setConfSenha(String confSenha) {
		this.confSenha = confSenha;
	}

	public Boolean getUsuarioLogado() {
		if (!usuarioLogado) {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/helloword/login.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return usuarioLogado;
	}

	public void setUsuarioLogado(Boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public void setSelected(Empresa selected) {
		this.selected = selected;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public List<Plano> getPlanos() {
		return planos;
	}

	public void setPlanos(List<Plano> planos) {
		this.planos = planos;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void reinit() {

		this.empresa = new Empresa();
		this.filtro = new Empresa();
		this.selected = new Empresa();
		this.empresas = new ArrayList<Empresa>();
	}

	public String save() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;
		if (empresa.getSenha().equals(this.confSenha)) {

			if (this.empresa.getId() != null) {
				return this.update();
			}

			if (this.session.save(this.empresa)) {
				this.session.sendEmailConfirmacao(this.empresa);
				Mensagem.msgSave();
				sucesso = true;
				context.addCallbackParam("sucesso", sucesso);
				if (this.empresa.getStatus().equals("New")) {
					this.reinit();
					this.redirecionaSubscribe();
					return "";
				}
				this.reinit();
				return "";
			}
		} else {
			Mensagem.msgConfSenha();
		}
		context.addCallbackParam("sucesso", sucesso);
		return "";
	}

	public String update() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.session.update(this.empresa)) {
			this.reinit();
			this.carregaEmpresa();
			sucesso = true;
			context.addCallbackParam("sucesso", sucesso);
			return Mensagem.msgUpdate();
		}

		context.addCallbackParam("sucesso", sucesso);
		return null;

	}

	public String remove() {
		try {
			this.session.remove(this.empresa);
			this.reinit();
			this.carregaEmpresa();
			return Mensagem.msgRemove();
		} catch (Exception e) {
			return Mensagem.msgNotRemove();
		}
	}

	public void carregaEstados() {
		this.estados = this.session.findEstados(new Estado());
	}

	public void carregaCidades() {
		// this.empresa.getCidade().setEstado(this.estado);
		this.cidades = this.session.findCidades(this.empresa.getCidade()
				.getEstado());
	}

	public void carregaPlanos() {
		this.planos = this.session.findPlanos(new Plano());
	}

	public void carregaEmpresa() {
		this.empresas = this.session.findList(new Empresa());
	}

	public void find() throws Exception {
		this.empresas = this.session.findList(this.filtro);
	}

	public void novo() {
		this.setEmpresa(new Empresa());
	}

	public void fazerLogin() {

		try {
			Empresa u = this.session.efetuarLogin(usuario);

			if (u == null) {
				Mensagem.msgUsuarioNaoEncontrado();
			} else {
				if (u.getStatus().equals("New")) {
					Mensagem.msgUsuarioNaoConfirmado();
					return;
				}
				usuarioLogado = true;
				this.empresa = u;
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/helloword/");
				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("empresa", this.empresa);
			}

		} catch (Exception e) {
			// Mensagens.msgUsuarioNaoEncontrado();
		}
	}

	public void logout() {
		this.usuario = new Empresa();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("empresa", null);

		this.redirecionarLogin();

		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
	}

	private boolean validaRecovery() {
		this.empresa = this.session.validaRecovery();
		if (this.empresa == null) {
			this.empresa = new Empresa();
			this.redirecionarLogin();
			return false;
		}
		return true;
	}

	public void alterarSenha() {
		if (this.senha.equals(this.confSenha)) {
			this.empresa.setSenha(this.senha);
			this.update();
			// this.redirecionarLogin();
		} else {
			Mensagem.msgConfSenha();
		}
	}

	public void redirecionarLogin() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/helloword/login.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void redirecionaSubscribe() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/helloword/subscribe.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enviarEmailRecuperarSenha() {
		this.empresa = this.session.enviarEmailRecuperarSenha(this.empresa);
		if (empresa.getId() != null) {
			this.redirecionarLogin();
			this.empresa = new Empresa();
		}
	}

	public boolean confirmaCadastro() {
		this.empresa = this.session.validaConfirmar();
		if (this.empresa == null) {
			this.empresa = new Empresa();
			this.redirecionarLogin();
			return false;
		}
		this.empresa.setStatus("Pendente");
		this.update();
		return true;
	}

}
