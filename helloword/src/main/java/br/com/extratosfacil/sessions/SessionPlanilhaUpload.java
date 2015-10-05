package br.com.extratosfacil.sessions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.constantes.Sessao;
import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Veiculo;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaDownload;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaUpload;
import br.com.extratosfacil.entities.planilha.PlanilhaUpload;
import br.com.jbc.controller.Controller;

/**
 * Session que representa as regras de negÃ³cios da entidade PlanilhaUpload
 * 
 * @author Paulo Henrique da Silva
 * @since 05/08/2015
 * @version 1.0
 * @category Session
 */

public class SessionPlanilhaUpload {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	private Controller<PlanilhaUpload> controller = new Controller<PlanilhaUpload>();

	public Controller<Veiculo> controllerVeiculo = new Controller<Veiculo>();

	public long vezes = 0;

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Controller<PlanilhaUpload> getController() {
		return controller;
	}

	public Controller<Veiculo> getControllerVeiculo() {
		return controllerVeiculo;
	}

	public void setControllerVeiculo(Controller<Veiculo> controllerVeiculo) {
		this.controllerVeiculo = controllerVeiculo;
	}

	public void setController(Controller<PlanilhaUpload> controller) {
		this.controller = controller;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	/**
	 * MÃ©todo que faz a leitura da planilha de upload e salva
	 * 
	 * @param planilhaUpload
	 * @return
	 */

	public List<ItemPlanilhaDownload> carregaPlanilha(String path, boolean xlsx) {

		if (xlsx) {
			// return this.lerXlsx(path);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = null;
			return this.lerPlanilha(path, workbook, sheet);
		} else {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = null;
			return this.lerPlanilha(path, workbook, sheet);
			// return this.lerXls(path);
		}
	}

	private List<ItemPlanilhaDownload> lerPlanilha(String path,
			Object workbook, Object sheet) {

		// formatar a data da planilha para o Obj
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// Formatar a Hora da Planilha para o objeto
		SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

		FileInputStream fisPlanilha = null;

		// Lista de veiculos da planilha
		List<ItemPlanilhaUpload> lista = new ArrayList<ItemPlanilhaUpload>();

		ItemPlanilhaUpload item = new ItemPlanilhaUpload();

		try {
			File file = new File(path);
			fisPlanilha = new FileInputStream(file);
			Iterator<Row> rowIterator = null;

			if (workbook instanceof XSSFWorkbook) {
				workbook = new XSSFWorkbook(fisPlanilha);
				sheet = ((XSSFWorkbook) workbook)
						.getSheet("Passagens de Pedágio");
				// retorna todas as linhas da planilha 0 (aba 1)
				rowIterator = ((XSSFSheet) sheet).iterator();
			} else {
				workbook = new HSSFWorkbook(fisPlanilha);
				sheet = ((HSSFWorkbook) workbook)
						.getSheet("Passagens de Pedágio");
				// retorna todas as linhas da planilha 0 (aba 1)
				rowIterator = ((HSSFSheet) sheet).iterator();
			}

			// varre todas as linhas da planilha 0
			while (rowIterator.hasNext()) {

				// recebe cada linha da planilha
				Row row = rowIterator.next();

				if (row.getRowNum() == 0) {
					continue;
				}

				// pegamos todas as celulas desta linha
				Iterator<Cell> cellIterator = row.iterator();

				// varremos todas as celulas da linha atual
				while (cellIterator.hasNext()) {

					// criamos uma celula
					Cell cell = cellIterator.next();

					if (cell.getColumnIndex() == 0) {
						item.setPlaca(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 2) {
						item.setCategoria(Integer.valueOf(cell
								.getStringCellValue()));
					} else if (cell.getColumnIndex() == 3) {
						try {
							item.setData(sdf.parse(cell.getStringCellValue()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (cell.getColumnIndex() == 4) {
						try {
							item.setHora(shf.parse(cell.getStringCellValue()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (cell.getColumnIndex() == 5) {
						item.setConcessionaria(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 6) {
						item.setPraca(cell.getStringCellValue());
					}
					if (cell.getColumnIndex() == 7) {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							item.setValor(cell.getNumericCellValue());
						} else {
							item.setValor(0.0);
						}
						if (item.getValor() > 0) {
							lista.add(item);
						}
						item = new ItemPlanilhaUpload();
					}

				}

			}

		} catch (IOException ex) {
			Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
					Level.SEVERE, null, ex);
			ex.printStackTrace();
		} finally {
			try {
				fisPlanilha.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

		return this.makeTheMagic(lista);
	}

	private String getNomeEmpresa() {
		// Pega a empresa da sessao para dar nome a pasta da planilha
		Empresa empresa = Sessao.getEmpresaSessao();
		if (empresa != null) {
			return empresa.getRazaoSocial();
		}
		return "empresa";
	}

	public List<ItemPlanilhaDownload> makeTheMagic(
			List<ItemPlanilhaUpload> itensPlanilha) {
		// fazemos a comparacao dos veiculos da planilha upload com os
		// cadastrados

		// Lista de veiculos com cobrancas incorretas
		List<ItemPlanilhaDownload> itensIncorretos = new ArrayList<ItemPlanilhaDownload>();

		List<Integer> remover = new ArrayList<Integer>();

		// Cria um veiculo temporï¿½rio
		Veiculo temp = new Veiculo();

		// Setar a empresa no veiculo para a busca, no momento esta sendo um
		// objeto vazio, posteriormente pegaremos a empresa da sessao do
		// usuario
		temp.setEmpresa(Sessao.getEmpresaSessao());
		// Buscar todos os veiculos da empresa
		List<Veiculo> veiculos = new ArrayList<Veiculo>();
		try {
			veiculos = this.controllerVeiculo
					.getListByHQLCondition("from Veiculo where empresa_id = "
							+ temp.getEmpresa().getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		for (int j = 0; j < veiculos.size(); j++) {
			vezes += 1;
			temp = new Veiculo();

			for (int i = 0; i < itensPlanilha.size(); i++) {
				vezes += 1;
				temp = veiculos.get(j);
				if (temp.getPlacaVeiculo().equalsIgnoreCase(
						itensPlanilha.get(i).getPlaca())) {

					if (isDuplicado(itensPlanilha, i)) {
						itensIncorretos.add(this.criaItemDownload(
								itensPlanilha.get(i), temp, true));
						remover.add(i);
					} else {
						if ((itensPlanilha.get(i).getCategoria() > temp
								.getMaximoEixo())) {

							itensIncorretos.add(this.criaItemDownload(
									itensPlanilha.get(i), temp, false));
							remover.add(i);
						} else {
							remover.add(i);
						}
					}
				}
			}

			int x = 0;
			for (int i = 0; i < remover.size(); i++) {
				x = remover.get(i);
				itensPlanilha.remove(x);
			}

		}
		System.out.println(vezes);
		vezes = 0;
		return itensIncorretos;
	}

	private boolean isDuplicado(List<ItemPlanilhaUpload> itensPlanilha, int i) {
		if (i > 0) {
			int j = i - 1;
			if (itensPlanilha.get(i).getPlaca()
					.equalsIgnoreCase(itensPlanilha.get(j).getPlaca())) {
				if (itensPlanilha.get(i).getPraca()
						.equalsIgnoreCase(itensPlanilha.get(j).getPraca())) {
					if (itensPlanilha.get(i).getData().getTime() == itensPlanilha
							.get(j).getData().getTime()) {
						if (itensPlanilha.get(i).getHora().getTime() == itensPlanilha
								.get(j).getHora().getTime()) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	private ItemPlanilhaDownload criaItemDownload(
			ItemPlanilhaUpload itemPlanilhaUpload, Veiculo temp,
			boolean duplicado) {

		ItemPlanilhaDownload item = new ItemPlanilhaDownload();

		// Caso a categoria seja com dois digitos tipo 61
		// if (item.getCategoria() > 10) {
		// item.setCategoria(this.sumInt(item.getCategoria()));
		// }

		item.setCategoria(itemPlanilhaUpload.getCategoria());
		item.setCategoriaCorreta(temp.getMaximoEixo());
		item.setConcessionaria(itemPlanilhaUpload.getConcessionaria());
		item.setData(itemPlanilhaUpload.getData());
		item.setHora(itemPlanilhaUpload.getHora());
		item.setPlaca(itemPlanilhaUpload.getPlaca());
		item.setPraca(itemPlanilhaUpload.getPraca());
		item.setValor(itemPlanilhaUpload.getValor());
		item.setValorCorreto(item.getValor()
				/ item.formataCategoria(item.getCategoria())
				* item.formataCategoria(item.getCategoriaCorreta()));
		if (duplicado) {
			item.setValorRestituicao(item.getValor());
		} else {
			item.setValorRestituicao(item.getValor() - item.getValorCorreto());
		}
		item.setObs(duplicado ? "Passagem Duplicada"
				: "Número de Eixos incorreto");
		return item;

	}

	public boolean validaPlanilha(PlanilhaUpload planilhaUpload) {
		planilhaUpload.setEmpresa(Sessao.getEmpresaSessao());
		return true;
	}

	public boolean save(PlanilhaUpload planilhaUpload) {
		if (this.validaPlanilha(planilhaUpload)) {
			try {
				this.controller.insert(planilhaUpload);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public boolean update(PlanilhaUpload planilhaUpload) {
		if (this.validaPlanilha(planilhaUpload)) {
			try {
				this.controller.update(planilhaUpload);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public boolean validaPlanilha(String path, Object workbook, Object sheet) {

		FileInputStream fisPlanilha = null;

		try {
			File file = new File(path);
			fisPlanilha = new FileInputStream(file);

			if (workbook instanceof XSSFWorkbook) {
				workbook = new XSSFWorkbook(fisPlanilha);
				sheet = ((XSSFWorkbook) workbook)
						.getSheet("Passagens de Pedágio");
			} else {
				workbook = new HSSFWorkbook(fisPlanilha);
				sheet = ((HSSFWorkbook) workbook)
						.getSheet("Passagens de Pedágio");
			}

			if (sheet == null) {
				Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
				return false;
			}

			if (workbook instanceof XSSFWorkbook) {
				// workbook = new XSSFWorkbook(fisPlanilha);
				sheet = ((XSSFWorkbook) workbook).getSheet("Resumo da Fatura");
			} else {
				// workbook = new HSSFWorkbook(fisPlanilha);
				sheet = ((HSSFWorkbook) workbook).getSheet("Resumo da Fatura");
			}

			if (sheet == null) {
				Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
				return false;
			}
			//
			// if (!this.validaEmpresaPlanilha(workbook)) {
			// Mensagem.send(Mensagem.MSG_EMPRESA_INVALIDA, Mensagem.ERROR);
			// return false;
			// }
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public boolean validaPlanilha(String path, boolean xlsx) {
		if (xlsx) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = null;
			return validaPlanilha(path, workbook, sheet);
		} else {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = null;
			return validaPlanilha(path, workbook, sheet);
		}
	}

	public List<ItemPlanilhaDownload> lerXml(String path) {

		List<ItemPlanilhaUpload> lista = new ArrayList<ItemPlanilhaUpload>();

		ItemPlanilhaUpload item = new ItemPlanilhaUpload();

		try {
			File file = new File(path);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(file.toString()));

			Element raiz = doc.getDocumentElement();

			NodeList worksheets = raiz.getElementsByTagName("Worksheet");

			Element sheet = null;

			for (int i = 0; i < worksheets.getLength(); i++) {
				sheet = (Element) worksheets.item(i);

				String nomeShhet = sheet.getAttribute("ss:Name");
				if (nomeShhet.equalsIgnoreCase("PASSAGENS PEDÁGIO")) {
					break;
				}
			}

			NodeList rows = sheet.getElementsByTagName("Row");

			for (int i = 1; i < rows.getLength(); i++) {
				Element row = (Element) rows.item(i);

				NodeList cells = row.getElementsByTagName("Cell");

				for (int j = 0; j < cells.getLength(); j++) {
					Element cell = (Element) cells.item(j);
					NodeList data = cell.getElementsByTagName("Data");
					if (data != null && data.item(0) != null
							&& data.item(0).getFirstChild() != null) {
						createItem(data.item(0).getFirstChild().getNodeValue(),
								j, item);
					}
				}
				if (item.getValor() > 0) {
					lista.add(item);
				}
				item = new ItemPlanilhaUpload();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.makeTheMagic(lista);

	}

	private void createItem(String conteudo, int index, ItemPlanilhaUpload item)
			throws ParseException {

		// formatar a data da planilha para o Obj
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// Formatar a Hora da Planilha para o objeto
		SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

		if (index == 0) {
			conteudo = removeHifen(conteudo);
			item.setPlaca(conteudo);
		} else if (index == 4) {
			item.setCategoria(Integer.valueOf(conteudo));
		} else if (index == 5) {
			item.setData(sdf.parse(conteudo));
		} else if (index == 6) {
			item.setHora(shf.parse(conteudo));
		} else if (index == 7) {
			item.setConcessionaria(conteudo);
		} else if (index == 8) {
			item.setPraca(conteudo);
		} else if (index == 9) {
			item.setValor(Double.valueOf(conteudo));
		}

	}

	private static String removeHifen(String conteudo) {
		return conteudo.replaceAll("-", "");
	}

	public boolean validaXml(String caminho) {
		File file = new File(caminho);
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(file.toString()));

			Element raiz = doc.getDocumentElement();

			NodeList worksheets = raiz.getElementsByTagName("Worksheet");

			Element sheet = null;

			for (int i = 0; i < worksheets.getLength(); i++) {
				sheet = (Element) worksheets.item(i);

				String nomeShhet = sheet.getAttribute("ss:Name");
				if (nomeShhet.equalsIgnoreCase("PASSAGENS PEDÁGIO")) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
			return false;
		}
		Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
		return false;
	}

	public List<ItemPlanilhaDownload> carregaXml(String caminho) {
		return this.lerXml(caminho);
	}

}