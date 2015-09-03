package br.com.extratosfacil.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaDownload;
import br.com.extratosfacil.entities.planilha.PlanilhaUpload;
import br.com.extratosfacil.messages.Mensagem;
import br.com.extratosfacil.sessions.SessionPlanilhaDownload;
import br.com.extratosfacil.sessions.SessionPlanilhaUpload;

/**
 * Bean que representa a entidade Planilha Upload
 * 
 * @author Paulo Henrique da Silva
 * @since 05/07/2015
 * @version 1.0
 * @category Bean
 */

@ManagedBean
@SessionScoped
public class BeanPlanilhaUpload {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private PlanilhaUpload planilhaUpload = new PlanilhaUpload();

	private SessionPlanilhaUpload session = new SessionPlanilhaUpload();

	private List<ItemPlanilhaDownload> itens = new ArrayList<ItemPlanilhaDownload>();

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanPlanilhaUpload() {

	}

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public PlanilhaUpload getPlanilhaUpload() {
		return planilhaUpload;
	}

	public SessionPlanilhaUpload getSession() {
		return session;
	}

	public void setSession(SessionPlanilhaUpload session) {
		this.session = session;
	}

	public List<ItemPlanilhaDownload> getItens() {
		return itens;
	}

	public void setItens(List<ItemPlanilhaDownload> itens) {
		this.itens = itens;
	}

	public void setPlanilhaUpload(PlanilhaUpload planilhaUpload) {
		this.planilhaUpload = planilhaUpload;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void fileUploadAction(FileUploadEvent event) {
		try {

			FacesContext aFacesContext = FacesContext.getCurrentInstance();
			ServletContext context = (ServletContext) aFacesContext
					.getExternalContext().getContext();

			String realPath = context.getRealPath("/");

			// pega o nome da empresa e a data para criar a pasta da planilha
			String nomeEmpresa = this.getNomeEmpresa();
			String data = this.getData();
			// Aqui cria o diretorio caso n�o exista
			String diretorio = realPath + "Empresas\\" + nomeEmpresa
					+ "\\Upload\\" + data + "\\";
			File file = new File(diretorio);
			file.mkdirs();

			byte[] arquivo = event.getFile().getContents();
			boolean xlsx = event.getFile().getFileName().indexOf("xlsx") >= 0;

			// Renomeia o arquivo upado
			String caminho = diretorio + "Extrato-" + this.getData() + ".xls";

			// esse trecho grava o arquivo no diretorio
			FileOutputStream fos = new FileOutputStream(caminho);
			fos.write(arquivo);
			fos.close();

			// salva o caminho da planilha no banco
			this.planilhaUpload.setPath(caminho);
			this.planilhaUpload.setData(new Date());

			if (this.session.validaPlanilha(planilhaUpload.getPath(), xlsx)) {
				itens = this.session.carregaPlanilha(planilhaUpload.getPath(),
						xlsx);
				this.save();
			}

		} catch (Exception ex) {
			Mensagem.msgPlanilhaErrada();
			System.out.println("Erro no upload da Planilha" + ex);
		}
	}

	@SuppressWarnings("deprecation")
	private String getData() {
		Date data = new Date();
		String d = String.valueOf(data.getDate());
		d = d + "-" + String.valueOf(data.getMonth() + 1);
		d = d + "-" + String.valueOf(data.getHours());
		d = d + "-" + String.valueOf(data.getMinutes());
		d = d + "-" + String.valueOf(data.getSeconds());
		return d;
	}

	private String getNomeEmpresa() {
		// Pega a empresa da sessao para dar nome a pasta da planilha
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("empresa");
		if (empresa != null) {
			return empresa.getRazaoSocial().trim();
		}
		return "empresa";
	}

	public void reinit() {
		this.planilhaUpload = new PlanilhaUpload();
	}

	public String save() throws Exception {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.planilhaUpload.getId() != null) {
			return this.update();
		}
		if (this.session.validaPlanilha(this.planilhaUpload)) {
			if (this.session.save(planilhaUpload)) {
				this.reinit();
				Mensagem.msgUpload();
				sucesso = true;
				context.addCallbackParam("sucesso", sucesso);
				return "";
			}
		}
		context.addCallbackParam("sucesso", sucesso);
		return "";
	}

	public String update() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean sucesso = false;

		if (this.session.validaPlanilha(this.planilhaUpload)) {
			if (this.session.update(this.planilhaUpload)) {
				this.reinit();
				// this.carregaEmpresa();
				sucesso = true;
				context.addCallbackParam("sucesso", sucesso);
				return Mensagem.msgUpdate();
			}
		}
		context.addCallbackParam("sucesso", sucesso);
		return null;
	}

	public void postProcessXLS(Object document) {
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		int width = sheet.getColumnWidth(0) * 2;
		sheet.setColumnWidth(0, width);
		sheet.setColumnWidth(1, width);
		sheet.setColumnWidth(2, width);
		sheet.setColumnWidth(3, width);
		sheet.setColumnWidth(4, width);
		sheet.setColumnWidth(5, width);
		sheet.setColumnWidth(6, width);
		sheet.setColumnWidth(7, width);
		Iterator<Row> rowIterator = sheet.iterator();
		Row row = rowIterator.next();

		if (row.getRowNum() == 0) {
			CellStyle style = wb.createCellStyle();// Create style
			Font font = wb.createFont();// Create font
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);// Make font bold
			style.setFont(font);// set it to bold

			for (int i = 0; i < row.getLastCellNum(); i++) {// For each cell in
															// the row
				row.getCell(i).setCellStyle(style);// Set the style
			}
		}

		SessionPlanilhaDownload sessionPD = new SessionPlanilhaDownload();
		sessionPD.criaPlanilhaDownload(wb);

	}
}
