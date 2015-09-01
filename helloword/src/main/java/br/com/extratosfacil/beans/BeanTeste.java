package br.com.extratosfacil.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;

import br.com.extratosfacil.entities.Veiculo;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaDownload;
import br.com.extratosfacil.sessions.SessionTeste;

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
public class BeanTeste {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private Veiculo veiculo = new Veiculo();

	private SessionTeste session = new SessionTeste();

	private List<ItemPlanilhaDownload> itens = new ArrayList<ItemPlanilhaDownload>();

	private int erros = 0;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/

	public BeanTeste() {

	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public int getErros() {
		return erros;
	}

	public void setErros(int erros) {
		this.erros = erros;
	}

	public SessionTeste getSession() {
		return session;
	}

	public void setSession(SessionTeste session) {
		this.session = session;
	}

	public List<ItemPlanilhaDownload> getItens() {
		return itens;
	}

	public void setItens(List<ItemPlanilhaDownload> itens) {
		this.itens = itens;
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

			// Aqui cria o diretorio caso n�o exista
			File file = new File(realPath + "try\\");
			file.mkdirs();

			byte[] arquivo = event.getFile().getContents();
			String caminho = realPath + "try\\" + event.getFile().getFileName();

			System.out.println(caminho);

			// esse trecho grava o arquivo no diret�rio
			FileOutputStream fos = new FileOutputStream(caminho);
			fos.write(arquivo);
			fos.close();

			this.erros = 0;
			if (this.session.validaPlanilha(caminho)) {
				itens = this.session.carregaPlanilha(caminho, this.veiculo);
				this.erros = session.getErros();
			}

			File f = new File(caminho);
			f.delete();

		} catch (Exception ex) {
			System.out.println("Erro no upload da Planilha" + ex);
		}
	}

}