package br.com.extratosfacil.sessions;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import br.com.extratosfacil.constantes.Sessao;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.planilha.PlanilhaDownload;
import br.com.jbc.controller.Controller;

/**
 * Session que representa as regras de negócios da entidade PlanilhaDownload
 * 
 * @author Paulo Henrique da Silva
 * @since 11/08/2015
 * @version 1.0
 * @category Session
 */

public class SessionPlanilhaDownload {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private Controller<PlanilhaDownload> controller = new Controller<PlanilhaDownload>();

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Controller<PlanilhaDownload> getController() {
		return controller;
	}

	public void setController(Controller<PlanilhaDownload> controller) {
		this.controller = controller;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	public void criaPlanilhaDownload(HSSFWorkbook wb) {
		ServletContext context = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String realPath = context.getRealPath("/");

		// pega o nome da empresa e a data para criar a pasta da planilha
		String nomeEmpresa = this.getNomeEmpresa();
		String data = this.getData();
		String diretorio = realPath + "Empresas\\" + nomeEmpresa
				+ "\\Download\\" + data + "\\";
		// Aqui cria o diretorio caso n�o exista
		File file = new File(diretorio);
		file.mkdirs();

		FileOutputStream fos = null;

		String caminho = diretorio + "Correcao-" + data + ".xls";
		String arquivo = caminho;
		try {
			// esse trecho grava o arquivo no diret�rio
			fos = new FileOutputStream(new File(caminho));
			wb.write(fos);
			System.out.println(caminho);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao gravar o arquivo");
		} finally {
			try {
				fos.flush();
				fos.close();
				this.save(arquivo);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void save(String arquivo) {
		PlanilhaDownload planilha = new PlanilhaDownload();
		planilha.setData(new Date());
		planilha.setPath(arquivo);
		planilha.setEmpresa(Sessao.getEmpresaSessao());
		try {
			this.controller.insert(planilha);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<PlanilhaDownload> find(PlanilhaDownload planilhaDownload) {
		try {
			return this.controller.findList(planilhaDownload);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<PlanilhaDownload> find(Date inicio, Date fim) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Empresa temp = Sessao.getEmpresaSessao();
		try {
			return this.controller
					.getListByHQLCondition("From PlanilhaDownload where data >= '"
							+ df.format(inicio)
							+ "' and data <= '"
							+ df.format(fim)
							+ "' and empresa_id = "
							+ temp.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
		Empresa empresa = Sessao.getEmpresaSessao();
		if (empresa != null) {
			return empresa.getRazaoSocial().trim();
		}
		return "empresa";
	}

	// retorna as ultimas 3 planilhas geradas se houver
	public List<PlanilhaDownload> findLast() {
		Empresa temp = Sessao.getEmpresaSessao();
		PlanilhaDownload plan = new PlanilhaDownload();
		plan.setEmpresa(temp);
		try {
			return this.controller.findList(plan);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}