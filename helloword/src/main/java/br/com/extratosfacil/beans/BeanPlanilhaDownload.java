package br.com.extratosfacil.beans;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.planilha.PlanilhaDownload;
import br.com.extratosfacil.sessions.SessionPlanilhaDownload;

/**
 * Bean que representa a entidade Planilha Download
 * 
 * @author Paulo Henrique da Silva
 * @since 05/07/2015
 * @version 1.0
 * @category Bean
 */

@ManagedBean
@SessionScoped
public class BeanPlanilhaDownload {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private PlanilhaDownload planilhaDownload = new PlanilhaDownload();

	private PlanilhaDownload selected = new PlanilhaDownload();

	private SessionPlanilhaDownload session = new SessionPlanilhaDownload();

	private List<PlanilhaDownload> planilhas = new ArrayList<PlanilhaDownload>();

	private List<PlanilhaDownload> lastPlanilhas = new ArrayList<PlanilhaDownload>();

	private Date inicio = new Date();

	private Date fim = new Date();

	private StreamedContent file;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanPlanilhaDownload() {
		this.find();
	}

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public PlanilhaDownload getPlanilhaDownload() {
		return planilhaDownload;
	}

	public SessionPlanilhaDownload getSession() {
		return session;
	}

	public void setLastPlanilhas(List<PlanilhaDownload> lastPlanilhas) {
		this.lastPlanilhas = lastPlanilhas;
	}

	public Date getInicio() {
		return inicio;
	}

	public PlanilhaDownload getSelected() {
		return selected;
	}

	public void setSelected(PlanilhaDownload selected) {
		this.selected = selected;
	}

	public StreamedContent getFile() {
		String path = this.selected.getPath();
		String contentType = FacesContext.getCurrentInstance()
				.getExternalContext().getMimeType(path);
		try {
			file = new DefaultStreamedContent(new FileInputStream(path),
					contentType, "Correção" + this.getData(selected.getData())
							+ ".xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public List<PlanilhaDownload> getLastPlanilhas() {
		this.carregaLastPlanilha();
		return lastPlanilhas;
	}

	public void setSession(SessionPlanilhaDownload session) {
		this.session = session;
	}

	public void setPlanilhaDownload(PlanilhaDownload planilhaDownload) {
		this.planilhaDownload = planilhaDownload;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void reinit() {
		this.planilhaDownload = new PlanilhaDownload();
		this.selected = new PlanilhaDownload();
		this.planilhas = new ArrayList<PlanilhaDownload>();
		this.find();
	}

	public List<PlanilhaDownload> getPlanilhas() {
		return planilhas;
	}

	public void setPlanilhas(List<PlanilhaDownload> planilhas) {
		this.planilhas = planilhas;
	}

	public void find() {
		this.planilhaDownload = new PlanilhaDownload();
		this.planilhaDownload.setEmpresa((Empresa) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("empresa"));
		this.planilhas = this.session.find(this.planilhaDownload);
	}

	public void findByDate() {
		this.planilhas = this.session.find(this.inicio, this.fim);
	}

	@SuppressWarnings("deprecation")
	private String getData(Date data) {
		String d = String.valueOf(data.getDate());
		d = d + "-" + String.valueOf(data.getMonth() + 1);
		return d;
	}

	public void carregaLastPlanilha() {
		this.lastPlanilhas = this.session.findLast();
		int size = this.lastPlanilhas.size() -1;
		if (size >= 2) {
			for (int i = size; i > 2; i--) {
				this.lastPlanilhas.remove(i);
			}
		}
		
	}

	public String goToHistorico() {
		this.reinit();
		return "views/historico/historico";
	}
}
