package br.com.extratosfacil.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.constantes.Sessao;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Plano;
import br.com.extratosfacil.entities.location.Cidade;
import br.com.extratosfacil.entities.location.Estado;
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

	private Boolean usuarioLogin = false;

	private boolean recuperar = false;

	private boolean confirmar = false;

	private String confSenha = new String();

	private String senha = new String();

	private boolean pessoaFisica = false;

	private boolean aceito = false;

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

	public Boolean getUsuarioLogin() {
		if (usuarioLogado) {
			Sessao.redireciona("index.html");
		}
		return usuarioLogin;
	}

	public void setUsuarioLogin(Boolean usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
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

	public boolean isPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(boolean pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
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
			Sessao.redireciona("login.html");
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

	public boolean isAceito() {
		return aceito;
	}

	public void setAceito(boolean aceito) {
		this.aceito = aceito;
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
		this.aceito = false;
		this.pessoaFisica = false;
		this.empresa = new Empresa();
		this.filtro = new Empresa();
		this.selected = new Empresa();
		this.empresas = new ArrayList<Empresa>();
	}

	public String save() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;
		if (empresa.getSenha().equals(this.confSenha) && this.aceito) {

			if (this.empresa.getId() != null) {
				return this.update();
			}

			if (this.session.save(this.empresa, this.pessoaFisica)) {
				this.session.sendEmailConfirmacao(this.empresa);
				Mensagem.send(Mensagem.MSG_SALVA, Mensagem.INFO);
				sucesso = true;
				context.addCallbackParam("sucesso", sucesso);
				if (this.empresa.getStatus().equals("New")) {
					this.reinit();
					Sessao.redireciona("subscribe.html");
					return "";
				}
				this.reinit();
				return "";
			}
		} else {
			if (aceito) {
				Mensagem.send(Mensagem.MSG_CONF_SENHA, Mensagem.ERROR);
			} else {
				Mensagem.send(Mensagem.MSG_ACEITO, Mensagem.ERROR);
			}

		}
		context.addCallbackParam("sucesso", sucesso);
		return "";
	}

	public String update() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.session.update(this.empresa, this.pessoaFisica)) {
			this.reinit();
			this.carregaEmpresa();
			sucesso = true;
			context.addCallbackParam("sucesso", sucesso);
			Mensagem.send(Mensagem.MSG_UPDATE, Mensagem.INFO);
		}

		context.addCallbackParam("sucesso", sucesso);
		return null;

	}

	public String remove() {
		try {
			this.session.remove(this.empresa);
			this.reinit();
			this.carregaEmpresa();
			Mensagem.send(Mensagem.MSG_REMOVE, Mensagem.INFO);
			return "";
		} catch (Exception e) {
			Mensagem.send(Mensagem.MSG_NOT_REMOVE, Mensagem.ERROR);
			return "";
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
				Mensagem.send(Mensagem.MSG_USER_NAO_ENCONTRADO, Mensagem.ERROR);
			} else {
				if (u.getStatus().equals("New")) {
					Mensagem.send(Mensagem.MSG_USER_NAO_CONFIRMADO,
							Mensagem.ERROR);
					return;
				}
				usuarioLogado = true;
				this.empresa = u;
				Sessao.setEmpresaSessao(this.empresa);
				this.session.validaVencimento(this.empresa);
				Sessao.redireciona("");
			}

		} catch (Exception e) {
			Mensagem.send(Mensagem.MSG_USER_NAO_ENCONTRADO, Mensagem.ERROR);
		}
	}

	public void logout() {
		this.usuario = new Empresa();
		Sessao.setEmpresaSessao(null);

		Sessao.redireciona("login.html");

		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
	}

	private boolean validaRecovery() {
		this.empresa = this.session.validaRecovery();
		if (this.empresa == null) {
			this.empresa = new Empresa();
			Sessao.redireciona("login.html");
			return false;
		}
		return true;
	}

	public void alterarSenha() {
		if (this.senha.equals(this.confSenha)) {
			this.empresa.setSenha(this.senha);
			this.update();
		} else {
			Mensagem.send(Mensagem.MSG_CONF_SENHA, Mensagem.ERROR);
		}
	}

	public void enviarEmailRecuperarSenha() {
		if (this.empresa.getEmail() == null
				|| this.empresa.getEmail().equals("")) {
			Mensagem.send(Mensagem.MSG_EMAIL_INVALIDO, Mensagem.ERROR);
			return;
		}
		this.empresa = this.session.enviarEmailRecuperarSenha(this.empresa);
		if (empresa.getId() != null) {
			// Sessao.redireciona("login.html");
			Mensagem.send(Mensagem.EMAIL_ENVIADO, Mensagem.INFO);
			this.empresa = new Empresa();
		}
	}

	public boolean confirmaCadastro() {
		this.empresa = this.session.validaConfirmar();
		if (this.empresa == null) {
			this.empresa = new Empresa();
			Sessao.redireciona("login.html");
			return false;
		}
		this.empresa.setStatus("Pendente");
		this.update();
		return true;
	}

	public void redirecionaLogin() {
		Sessao.redireciona("login.html");
	}

	public void reenviarEmail() {
		if (this.empresa.getEmail() == null
				|| this.empresa.getEmail().equals("")) {
			Mensagem.send(Mensagem.MSG_EMAIL_INVALIDO, Mensagem.ERROR);
			return;
		}
		this.empresa = this.session.find(this.empresa);
		if (empresa == null) {
			Mensagem.send(Mensagem.MSG_EMAIL_INVALIDO, Mensagem.ERROR);
			this.empresa = new Empresa();
		} else {
			this.session.sendEmailConfirmacao(this.empresa);
			Mensagem.send(Mensagem.MSG_EMAIL_ENVIADO, Mensagem.INFO);
		}
		this.reinit();
	}

}
