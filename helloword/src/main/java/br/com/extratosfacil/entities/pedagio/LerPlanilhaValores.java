/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.extratosfacil.entities.pedagio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.jbc.controller.Controller;

/**
 *
 * @author Br
 */
public class LerPlanilhaValores {

	/**
	 * @param args
	 *            the command line arguments
	 */

	public static Concessionaria concessionaria = new Concessionaria();

	public static Rodovia rodovia = new Rodovia();

	public static Praca praca = new Praca();

	public static Categoria categoria = new Categoria();

	public static boolean rod = false;

	public static boolean cons = false;

	public static boolean inicio = true;

	// array utilizado para guardar as categorias da praca
	public static List<String> categorias = new ArrayList<String>();

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		FileInputStream fisPlanilha = null;

		try {
			File file = new File("D:\\teste.xlsx");
			fisPlanilha = new FileInputStream(file);

			// cria um workbook = planilha toda com todas as abas
			XSSFWorkbook workbook = new XSSFWorkbook(fisPlanilha);

			// recuperamos apenas a primeira aba ou primeira planilha
			XSSFSheet sheet = workbook.getSheetAt(0);

			// retorna todas as linhas da planilha 0 (aba 1)
			Iterator<Row> rowIterator = sheet.iterator();

			Row linha = null;

			while (rowIterator.hasNext()) {

				if (inicio) {
					linha = getInformacaoInicial(rowIterator);

					getCategorias(linha, categorias);

					linha = getPracas(rowIterator, categorias);

					inicio = false;
				}

				if (rod) {
					while (rod) {
						criaRodovia(linha.cellIterator().next()
								.getStringCellValue());
						rod = false;
						categorias = new ArrayList<String>();
						getCategorias(rowIterator.next(), categorias);
						linha = getPracas(rowIterator, categorias);
					}
				}

				if (cons) {
					criaConcessionaria(linha.cellIterator().next()
							.getStringCellValue());
					criaRodovia(rowIterator.next().cellIterator().next()
							.getStringCellValue());
					cons = false;
					rod = false;
					categorias = new ArrayList<String>();
					getCategorias(rowIterator.next(), categorias);
					linha = getPracas(rowIterator, categorias);
				}
			}

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fisPlanilha.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private static Row getPracas(Iterator<Row> rowIterator,
			List<String> categorias) {

		Row row = null;
		while (rowIterator.hasNext()) {

			if ((rod) || (cons)) {
				return row;
			}

			row = rowIterator.next();

			// para saber qual categoria esta
			int indice = 0;

			// pegamos todas as celulas desta linha
			Iterator<Cell> cellIterator = row.iterator();

			// varremos todas as celulas da linha atual
			while (cellIterator.hasNext()) {
				// criamos uma celula
				Cell cell = cellIterator.next();

				if ((cell.getColumnIndex() == 1)
						&& (cell.getCellType() == Cell.CELL_TYPE_STRING)
						&& (cell.getStringCellValue().indexOf("Rodovia") >= 0)) {
					System.out
							.println(cellIterator.next().getStringCellValue());
					rod = true;
					break;
				}

				if ((cell.getColumnIndex() == 0)
						&& (cell.getCellType() == Cell.CELL_TYPE_STRING)) {

					if (cell.getStringCellValue().indexOf("Tarifa especial") >= 0) {
						continue;
					}

					if (isUpperCase(cell.getStringCellValue())) {
						if ((cellIterator.next().getStringCellValue().trim()
								.equals(""))) {
							cons = true;
							break;
						}
					}
					if (!cell.getStringCellValue().trim().equals("")) {
						criarPraca(cell.getStringCellValue());
					}
				}

				if (cell.getColumnIndex() >= 4) {
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						criarCategoria(cell.getNumericCellValue(), indice);
						indice++;
					}
				}
			}
		}
		return row;

	}

	private static void criarCategoria(double valor, int indice) {
		categoria = new Categoria();
		categoria.setValor(valor);
		categoria.setNome(categorias.get(indice));
		categoria.setPraca(praca);
		salvarCategoria(categoria);
	}

	private static void getCategorias(Row linha, List<String> categorias) {

		boolean achouCategoria = true;

		// pegamos todas as celulas desta linha
		Iterator<Cell> cellIterator = linha.iterator();

		// varremos todas as celulas da linha atual
		while (cellIterator.hasNext()) {
			// criamos uma celula
			Cell cell = cellIterator.next();
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

				if ((cell.getStringCellValue().indexOf("Auto 2 eixos") < 0)
						&& (achouCategoria)) {
					continue;
				} else {
					achouCategoria = false;
				}

				categorias.add(cell.getStringCellValue());

			} else {
				System.out.println("Tipo errado");

			}
		}

	}

	private static Row getInformacaoInicial(Iterator<Row> rowIterator) {
		boolean inicio = true;
		// array utilizado para encontrar a concessionaria, rodovia e praca
		List<String> strs = new ArrayList<String>();

		Row retorno = null;

		// varre todas as linhas da planilha 0
		while ((rowIterator.hasNext()) && (inicio)) {

			// recebe cada linha da planilha
			Row row = rowIterator.next();
			System.out.println(row.getRowNum());
			retorno = row;

			// pegamos todas as celulas desta linha
			Iterator<Cell> cellIterator = row.iterator();

			// varremos todas as celulas da linha atual
			while (cellIterator.hasNext()) {

				// criamos uma celula
				Cell cell = cellIterator.next();

				if ((inicio)
						&& (!(cell.getStringCellValue().indexOf("Praça") >= 0))) {
					strs.add(cell.getStringCellValue());
					break;
				}
				if (cell.getStringCellValue().indexOf("Praça") >= 0) {
					criaConcessionaria(strs.get(strs.size() - 2));
					criaRodovia(strs.get(strs.size() - 1));
					inicio = false;
					strs = new ArrayList<String>();
					break;
				}
			}
		}
		return retorno;
	}

	private static void criarPraca(String nome) {
		praca = new Praca();
		praca.setNome(nome.toUpperCase());
		praca.setConcessionaria(concessionaria);
		praca.setRodovia(rodovia);
		salvarPraca(praca);

	}

	private static void salvarPraca(Praca praca2) {
		Controller<Praca> controller = new Controller<Praca>();
		try {
			controller.insertReturnId(praca);
		} catch (Exception e) {
			System.out.println("Falha ao inserir Praca");
			e.printStackTrace();
		}
	}

	private static void salvarCategoria(Categoria categoria) {
		Controller<Categoria> controller = new Controller<Categoria>();
		try {
			controller.insertReturnId(categoria);
		} catch (Exception e) {
			System.out.println("Falha ao inserir categoria");
			e.printStackTrace();
		}
	}

	private static void criaRodovia(String string) {
		rodovia = new Rodovia();
		rodovia.setNome(string.toUpperCase());
		Controller<Rodovia> controller = new Controller<Rodovia>();
		try {
			controller.insertReturnId(rodovia);
		} catch (Exception e) {
			System.out.println("Falha ao inserir Rodovia");
			e.printStackTrace();
		}
	}

	private static void criaConcessionaria(String string) {
		concessionaria = new Concessionaria();
		concessionaria.setNome(string.toUpperCase());
		Controller<Concessionaria> controller = new Controller<Concessionaria>();
		try {
			controller.insertReturnId(concessionaria);
		} catch (Exception e) {
			System.out.println("Falha ao inserir Concessionaria");
			e.printStackTrace();
		}
	}

	public static boolean isUpperCase(String string) {
		if (string.trim().equals("")) {
			return false;
		}
		return string.toUpperCase().equals(string);
	}

}
