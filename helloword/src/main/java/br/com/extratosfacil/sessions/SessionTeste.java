package br.com.extratosfacil.sessions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.extratosfacil.entities.Veiculo;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaDownload;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaUpload;

/**
 * Session que representa as regras de negÃ³cios da entidade Empresa
 * 
 * @author Paulo Henrique da Silva
 * @since 29/07/2015
 * @version 1.0
 * @category Session
 */

public class SessionTeste {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	// private Controller<Veiculo> controller = new Controller<Veiculo>();

	private int erros;

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	// public Controller<Veiculo> getController() {
	// return controller;
	// }
	//
	// public void setController(Controller<Veiculo> controller) {
	// this.controller = controller;
	// }

	public int getErros() {
		return erros;
	}

	public void setErros(int erros) {
		this.erros = erros;
	}

	/*-------------------------------------------------------------------
	 * 		 					METHODS
	 *-------------------------------------------------------------------*/

	@SuppressWarnings("resource")
	public List<ItemPlanilhaDownload> carregaPlanilha(String path,
			Veiculo veiculo, boolean xlsx) throws IOException, ParseException {

		if (xlsx) {
			// return this.lerXlsx(path);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = null;
			return this.lerPlanilha(path, workbook, sheet, veiculo);
		} else {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = null;
			return this.lerPlanilha(path, workbook, sheet, veiculo);
			// return this.lerXls(path);
		}
	}

	private List<ItemPlanilhaDownload> lerPlanilha(String path,
			Object workbook, Object sheet, Veiculo veiculo) throws IOException,
			ParseException {

		// formatar a data da planilha para o Obj
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// Formatar a Hora da Planilha para o objeto
		SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

		FileInputStream fisPlanilha = null;

		// Lista de veiculos da planilha
		List<ItemPlanilhaUpload> lista = new ArrayList<ItemPlanilhaUpload>();

		ItemPlanilhaUpload item = new ItemPlanilhaUpload();

		File file = new File(path);
		fisPlanilha = new FileInputStream(file);
		Iterator<Row> rowIterator = null;

		if (workbook instanceof XSSFWorkbook) {
			workbook = new XSSFWorkbook(fisPlanilha);
			sheet = ((XSSFWorkbook) workbook).getSheet("Passagens de Pedágio");
			// retorna todas as linhas da planilha 0 (aba 1)
			rowIterator = ((XSSFSheet) sheet).iterator();
		} else {
			workbook = new HSSFWorkbook(fisPlanilha);
			sheet = ((HSSFWorkbook) workbook).getSheet("Passagens de Pedágio");
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
					item.setCategoria(Integer.valueOf(cell.getStringCellValue()));
				} else if (cell.getColumnIndex() == 3) {

					item.setData(sdf.parse(cell.getStringCellValue()));

				} else if (cell.getColumnIndex() == 4) {

					item.setHora(shf.parse(cell.getStringCellValue()));

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
					lista.add(item);
					item = new ItemPlanilhaUpload();
				}

			}

		}
		fisPlanilha.close();
		return this.makeTheMagic(lista, veiculo);
	}

	public List<ItemPlanilhaDownload> makeTheMagic(
			List<ItemPlanilhaUpload> itensPlanilha, Veiculo veiculo) {
		// fazemos a comparacao dos veiculos da planilha upload com os
		// cadastrados

		// Lista de veiculos com cobrancas incorretas
		List<ItemPlanilhaDownload> itensIncorretos = new ArrayList<ItemPlanilhaDownload>();

		erros = 0;
		for (int j = 0; j < itensPlanilha.size(); j++) {
			if (veiculo.getPlacaVeiculo().equalsIgnoreCase(
					itensPlanilha.get(j).getPlaca())
					&& itensPlanilha.get(j).getValor() > 0.0) {
				if (isDuplicado(itensPlanilha, j)) {
					if (itensIncorretos.size() < 5) {
						itensIncorretos.add(this.criaItemDownload(
								itensPlanilha.get(j), veiculo, true));
					}
					erros++;
				} else {
					if ((itensPlanilha.get(j).getCategoria() > veiculo
							.getMaximoEixo())) {
						erros++;
						if (itensIncorretos.size() < 5) {
							itensIncorretos.add(this.criaItemDownload(
									itensPlanilha.get(j), veiculo, false));
						}

					}
				}
			}
		}

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
		item.setCategoria(itemPlanilhaUpload.getCategoria());
		item.setCategoriaCorreta(temp.getMaximoEixo());
		item.setConcessionaria(itemPlanilhaUpload.getConcessionaria());
		item.setData(itemPlanilhaUpload.getData());
		item.setHora(itemPlanilhaUpload.getHora());
		item.setPlaca(itemPlanilhaUpload.getPlaca());
		item.setValor(itemPlanilhaUpload.getValor());
		item.setPraca(itemPlanilhaUpload.getPraca());
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

	public boolean validaPlanilha(String path, Object workbook, Object sheet)
			throws IOException {

		FileInputStream fisPlanilha = null;

		File file = new File(path);
		fisPlanilha = new FileInputStream(file);

		if (workbook instanceof XSSFWorkbook) {
			workbook = new XSSFWorkbook(fisPlanilha);
			sheet = ((XSSFWorkbook) workbook).getSheet("Passagens de Pedágio");
		}
		if (workbook instanceof HSSFWorkbook) {
			workbook = new HSSFWorkbook(fisPlanilha);
			sheet = ((HSSFWorkbook) workbook).getSheet("Passagens de Pedágio");
		}

		if (sheet == null) {
			return false;
		}

		return true;
	}

	public boolean validaPlanilha(String path, boolean xlsx) throws IOException {
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

	public List<ItemPlanilhaDownload> lerXml(String path, Veiculo veiculo)
			throws DOMException, ParseException, ParserConfigurationException,
			SAXException, IOException {

		List<ItemPlanilhaUpload> lista = new ArrayList<ItemPlanilhaUpload>();

		ItemPlanilhaUpload item = new ItemPlanilhaUpload();

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
					createItem(data.item(0).getFirstChild().getNodeValue(), j,
							item);
				}
			}
			lista.add(item);
			item = new ItemPlanilhaUpload();

		}

		return this.makeTheMagic(lista, veiculo);

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

	public boolean validaXml(String caminho)
			throws ParserConfigurationException, SAXException, IOException {
		File file = new File(caminho);

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

		return false;
	}

	public List<ItemPlanilhaDownload> carregaXml(String caminho, Veiculo veiculo)
			throws DOMException, ParseException, ParserConfigurationException,
			SAXException, IOException {
		return this.lerXml(caminho, veiculo);
	}
}
