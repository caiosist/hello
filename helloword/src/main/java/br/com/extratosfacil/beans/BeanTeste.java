package br.com.extratosfacil.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;

import br.com.extratosfacil.constantes.Mensagem;
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

	private SessionTeste session;

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
			File file = new File(realPath + "try" + File.separator);
			file.mkdirs();
			// Mensagem.send("Criou Diretorio", Mensagem.ERROR);
			byte[] arquivo = event.getFile().getContents();
			boolean xlsx = event.getFile().getFileName().indexOf("xlsx") >= 0;
			String caminho = realPath + "try" + File.separator
					+ event.getFile().getFileName();

			System.out.println(caminho);

			// esse trecho grava o arquivo no diret�rio
			FileOutputStream fos = new FileOutputStream(caminho);
			fos.write(arquivo);
			fos.close();
			// Mensagem.send("Gravou o arquivo", Mensagem.ERROR);
			this.erros = 0;

			this.session = new SessionTeste();
			try {
				// Mensagem.send("Tentou rodar 1", Mensagem.ERROR);
				if (this.session.validaPlanilha(caminho, xlsx)) {
					itens = this.session.carregaPlanilha(caminho, this.veiculo,
							xlsx);
					this.erros = session.getErros();
				}
			} catch (Exception e) {
				// Mensagem.send("Tentou rodar 2", Mensagem.ERROR);
				if (this.session.validaXml(caminho)) {
					itens = this.session.carregaXml(caminho, this.veiculo);
					this.erros = session.getErros();
				}

			}
			// Mensagem.send("Finalizou", Mensagem.ERROR);
			File f = new File(caminho);
			f.delete();

			this.session = null;
			System.gc();

		} catch (Exception ex) {
			ex.printStackTrace();
			Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
			Mensagem.send(ex.getMessage(), Mensagem.ERROR);
		}
	}

}