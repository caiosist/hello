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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.extratosfacil.constantes.Mensagem;
import br.com.extratosfacil.entities.Veiculo;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaDownload;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaUpload;
import br.com.jbc.controller.Controller;

/**
 * Session que representa as regras de neg√≥cios da entidade Empresa
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

	private Controller<Veiculo> controller = new Controller<Veiculo>();

	private int erros;

	/*-------------------------------------------------------------------
	 * 		 					GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/

	public Controller<Veiculo> getController() {
		return controller;
	}

	public void setController(Controller<Veiculo> controller) {
		this.controller = controller;
	}

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
			Veiculo veiculo) {

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

			// cria um workbook = planilha toda com todas as abas
			XSSFWorkbook workbook = new XSSFWorkbook(fisPlanilha);

			// recuperamos apenas a primeira aba ou primeira planilha
			XSSFSheet sheet = workbook.getSheetAt(3);

			// retorna todas as linhas da planilha 0 (aba 1)
			Iterator<Row> rowIterator = sheet.iterator();

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
						// item.setValor(cell.getNumericCellValue());
						lista.add(item);
						item = new ItemPlanilhaUpload();
					}

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				fisPlanilha.close();
			} catch (IOException ex) {
				Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
						Level.SEVERE, null, ex);
				ex.printStackTrace();
			}

		}

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
			if (veiculo.getPlacaVeiculo().equals(
					itensPlanilha.get(j).getPlaca())) {
				if ((itensPlanilha.get(j).getCategoria() > veiculo
						.getMaximoEixo())) {
					erros++;
					if (itensIncorretos.size() < 1) {
						itensIncorretos.add(this.criaItemDownload(
								itensPlanilha.get(j), veiculo));
					}
				}
			}
		}

		return itensIncorretos;
	}

	private ItemPlanilhaDownload criaItemDownload(
			ItemPlanilhaUpload itemPlanilhaUpload, Veiculo temp) {

		ItemPlanilhaDownload item = new ItemPlanilhaDownload();
		item.setCategoria(itemPlanilhaUpload.getCategoria());
		item.setCategoriaCorreta(temp.getMaximoEixo());
		item.setConcessionaria(itemPlanilhaUpload.getConcessionaria());
		item.setData(itemPlanilhaUpload.getData());
		item.setHora(itemPlanilhaUpload.getHora());
		item.setPlaca(itemPlanilhaUpload.getPlaca());
		item.setPraca(itemPlanilhaUpload.getPraca());
		return item;

	}

	// verifica se o nome da primeira e quarta abas da planilha estao corretos
	// para validar se È a planilha verdadeira
	@SuppressWarnings("resource")
	public boolean validaPlanilha(String path) {

		FileInputStream fisPlanilha = null;

		try {
			File file = new File(path);
			fisPlanilha = new FileInputStream(file);

			XSSFWorkbook workbook = new XSSFWorkbook(fisPlanilha);

			XSSFSheet sheet = workbook.getSheetAt(3);

			if (!sheet.getSheetName().equals("Passagens de Ped·gio")) {
				Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
				return false;
			}
			sheet = workbook.getSheetAt(0);
			if (!sheet.getSheetName().equals("Resumo da Fatura")) {
				Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

}
