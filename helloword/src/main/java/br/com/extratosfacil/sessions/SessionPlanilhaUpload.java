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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
			XSSFWorkbook workbook = null;
			XSSFSheet sheet = null;
			return this.lerPlanilha(path, workbook, sheet);
		} else {
			HSSFWorkbook workbook = null;
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
						}else{
							item.setValor(0.0);
						}
						
						lista.add(item);
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

			temp = new Veiculo();

			// percorre a lista de veiculos da planilha
			for (int i = 0; i < itensPlanilha.size(); i++) {
				temp = veiculos.get(j);
				// compara a cada veiculo se correponde ao mesmo vindo da
				// planilha pela placa
				if (temp.getPlacaVeiculo().equals(
						itensPlanilha.get(i).getPlaca()) && itensPlanilha.get(i).getValor() > 0) {
					// se esta encontrado compara a categoria da planilha com o
					// maximo de eixos, onde o max de eixos deve ser maior ou
					// igual a categoria para estar correto
					if ((itensPlanilha.get(i).getCategoria() > temp
							.getMaximoEixo())) {
						// se estiver incorreto add a lista de incorretos
						// ja como itemPlanilhaDownload pra a nova planilha
						itensIncorretos.add(this.criaItemDownload(
								itensPlanilha.get(i), temp));
					}
				}
			}
		}

		return itensIncorretos;
	}

	private ItemPlanilhaDownload criaItemDownload(
			ItemPlanilhaUpload itemPlanilhaUpload, Veiculo temp) {

		ItemPlanilhaDownload item = new ItemPlanilhaDownload();
		
		// Caso a categoria seja com dois digitos tipo 61
//		if (item.getCategoria() > 10) {
//			item.setCategoria(this.sumInt(item.getCategoria()));
//		}

		item.setCategoria(itemPlanilhaUpload.getCategoria());
		item.setCategoriaCorreta(temp.getMaximoEixo());
		item.setConcessionaria(itemPlanilhaUpload.getConcessionaria());
		item.setData(itemPlanilhaUpload.getData());
		item.setHora(itemPlanilhaUpload.getHora());
		item.setPlaca(itemPlanilhaUpload.getPlaca());
		item.setPraca(itemPlanilhaUpload.getPraca());
		item.setValor(itemPlanilhaUpload.getValor());
		item.setValorCorreto(item.getValor() / item.getCategoria()
				* item.getCategoriaCorreta());
		item.setValorRestituicao(item.getValor() - item.getValorCorreto());
		return item;

	}

	private int sumInt(Integer numero) {
		int soma = 0;
		while (numero != 0) {
			soma += numero % 10;
			numero = numero / 10;
		}
		return soma;
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
				workbook = new XSSFWorkbook(fisPlanilha);
				sheet = ((XSSFWorkbook) workbook)
						.getSheet("Resumo da Fatura");
			} else {
				workbook = new HSSFWorkbook(fisPlanilha);
				sheet = ((HSSFWorkbook) workbook)
						.getSheet("Resumo da Fatura");
			}

			if (sheet == null) {
				Mensagem.send(Mensagem.MSG_PLANILHA_ERRADA, Mensagem.ERROR);
				return false;
			}
//
//			if (!this.validaEmpresaPlanilha(workbook)) {
//				Mensagem.send(Mensagem.MSG_EMPRESA_INVALIDA, Mensagem.ERROR);
//				return false;
			// }
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	private boolean validaEmpresaPlanilha(Object workbook) {
		Iterator<Row> rowIterator = null;

		if (workbook instanceof XSSFWorkbook) {
			XSSFSheet sheet = ((XSSFWorkbook) workbook).getSheetAt(0);
			rowIterator = sheet.iterator();
		} else {
			HSSFSheet sheet = ((HSSFWorkbook) workbook).getSheetAt(0);
			rowIterator = sheet.iterator();
		}

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() == 0) {
				continue;
			}

			Iterator<Cell> cellIterator = row.iterator();
			while (cellIterator.hasNext()) {

				Cell cell = cellIterator.next();

				if ((cell.getRowIndex() == 1) && (cell.getColumnIndex() == 1)) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						String razaoSocial = cell.getStringCellValue().trim();
						if (razaoSocial.trim().equals(this.getNomeEmpresa())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean validaPlanilha(String path, boolean xlsx) {
		if (xlsx) {
			XSSFWorkbook workbook = null;
			XSSFSheet sheet = null;
			return validaPlanilha(path, workbook, sheet);
		} else {
			HSSFWorkbook workbook = null;
			HSSFSheet sheet = null;
			return validaPlanilha(path, workbook, sheet);
		}
	}

}