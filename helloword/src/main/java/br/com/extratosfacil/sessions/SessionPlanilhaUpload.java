package br.com.extratosfacil.sessions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.extratosfacil.entities.Empresa;
import br.com.extratosfacil.entities.Veiculo;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaDownload;
import br.com.extratosfacil.entities.planilha.ItemPlanilhaUpload;
import br.com.extratosfacil.entities.planilha.PlanilhaUpload;
import br.com.extratosfacil.messages.Mensagem;
import br.com.jbc.controller.Controller;

/**
 * Session que representa as regras de negócios da entidade PlanilhaUpload
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
	 * Método que faz a leitura da planilha de upload e salva
	 * 
	 * @param planilhaUpload
	 * @return
	 */

	public List<ItemPlanilhaDownload> carregaPlanilha(String path, boolean xlsx) {

		if (xlsx) {
			return this.lerXlsx(path);
		} else {
			return this.lerXls(path);
		}
	}

	private List<ItemPlanilhaDownload> lerXlsx(String path) {
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

			XSSFWorkbook workbook = new XSSFWorkbook(fisPlanilha);

			XSSFSheet sheet = workbook.getSheet("Passagens de Pedágio");

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
						item.setValor(cell.getNumericCellValue());
						lista.add(item);
						item = new ItemPlanilhaUpload();
					}

				}

			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
					Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
					Level.SEVERE, null, ex);
			ex.printStackTrace();
		} finally {
			try {
				fisPlanilha.close();
			} catch (IOException ex) {
				Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
						Level.SEVERE, null, ex);
				ex.printStackTrace();
			}

		}
		return this.makeTheMagic(lista);
	}

	private List<ItemPlanilhaDownload> lerXls(String path) {
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
			HSSFWorkbook workbook = new HSSFWorkbook(fisPlanilha);
			;
			// recuperamos apenas a primeira aba ou primeira planilha
			HSSFSheet sheet = workbook.getSheet("Passagens de Pedágio");

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
						item.setValor(cell.getNumericCellValue());
						lista.add(item);
						item = new ItemPlanilhaUpload();
					}

				}

			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
					Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
					Level.SEVERE, null, ex);
			ex.printStackTrace();
		} finally {
			try {
				fisPlanilha.close();
			} catch (IOException ex) {
				Logger.getLogger(SessionPlanilhaUpload.class.getName()).log(
						Level.SEVERE, null, ex);
				ex.printStackTrace();
			}

		}
		return this.makeTheMagic(lista);
	}

	private boolean validaEmpresaPlanilha(XSSFWorkbook workbook) {

		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();

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

	private String getNomeEmpresa() {
		// Pega a empresa da sessao para dar nome a pasta da planilha
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("empresa");
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

		// Cria um veiculo tempor�rio
		Veiculo temp = new Veiculo();

		// Setar a empresa no veiculo para a busca, no momento esta sendo um
		// objeto vazio, posteriormente pegaremos a empresa da sessao do
		// usuario
		temp.setEmpresa((Empresa) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("empresa"));
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
						itensPlanilha.get(i).getPlaca())) {
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
		item.setCategoria(itemPlanilhaUpload.getCategoria());
		item.setCategoriaCorreta(temp.getMaximoEixo());
		item.setConcessionaria(itemPlanilhaUpload.getConcessionaria());
		item.setData(itemPlanilhaUpload.getData());
		item.setHora(itemPlanilhaUpload.getHora());
		item.setPlaca(itemPlanilhaUpload.getPlaca());
		item.setPraca(itemPlanilhaUpload.getPraca());
		item.setValor(itemPlanilhaUpload.getValor());
		return item;

	}

	public boolean validaPlanilha(PlanilhaUpload planilhaUpload) {
		planilhaUpload.setEmpresa((Empresa) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("empresa"));
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

	// verifica se o nome da primeira e quarta abas da planilha estao corretos
	// para validar se � a planilha verdadeira
	@SuppressWarnings("resource")
	public boolean validaPlanilha(String path, boolean xlsx) {

		if (xlsx) {
			return validaXlsx(path);
		}

		FileInputStream fisPlanilha = null;

		try {
			File file = new File(path);
			fisPlanilha = new FileInputStream(file);

			HSSFWorkbook workbook = new HSSFWorkbook(fisPlanilha);
			HSSFSheet sheet = workbook.getSheet("Passagens de Pedágio");

			if (sheet == null) {
				Mensagem.msgPlanilhaErrada();
				return false;
			}
			sheet = workbook.getSheet("Resumo da Fatura");
			if (sheet == null) {
				Mensagem.msgPlanilhaErrada();
				return false;
			}
			if (!this.validaEmpresaPlanilhaXls(workbook)) {
				Mensagem.msgEmpresaInvalida();
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public boolean validaXlsx(String path) {

		FileInputStream fisPlanilha = null;

		try {
			File file = new File(path);
			fisPlanilha = new FileInputStream(file);

			XSSFWorkbook workbook = new XSSFWorkbook(fisPlanilha);
			XSSFSheet sheet = workbook.getSheet("Passagens de Pedágio");

			if (sheet == null) {
				Mensagem.msgPlanilhaErrada();
				return false;
			}
			sheet = workbook.getSheet("Resumo da Fatura");
			if (sheet == null) {
				Mensagem.msgPlanilhaErrada();
				return false;
			}
			if (!this.validaEmpresaPlanilha(workbook)) {
				Mensagem.msgEmpresaInvalida();
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	private boolean validaEmpresaPlanilhaXls(HSSFWorkbook workbook) {

		HSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();

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
						String razaoSocial = cell.getStringCellValue();
						if (razaoSocial.trim().equals(
								this.getNomeEmpresa().trim())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}