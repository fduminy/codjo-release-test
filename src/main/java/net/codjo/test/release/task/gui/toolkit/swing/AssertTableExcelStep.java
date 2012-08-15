/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.common.excel.ExcelUtil;
import net.codjo.test.release.task.Util;
import net.codjo.test.release.task.gui.ColumnOrderException;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTable} via un fichier Excel.
 */
public class AssertTableExcelStep<T extends net.codjo.test.release.task.gui.AssertTableExcelStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JTable.class, step.getName());
        JTable table = (JTable)findOnlyOne(finder, context, 0, step);
        if (table == null) {
            throw new GuiFindException("La table '" + step.getName() + "' est introuvable.");
        }

        if (table.getRowCount() != step.getExpectedRowCount()) {
            throw new GuiAssertException(computeRowCountErrorMessage(step.getExpectedRowCount(), table.getRowCount()));
        }

        if (step.getExpectedColumnCount() != -1 && table.getColumnCount() != step.getExpectedColumnCount()) {
            throw new GuiAssertException(computeColumnCountErrorMessage(step.getExpectedColumnCount(),
                                                                        table.getColumnCount()));
        }

        HSSFSheet currentSheet = getExpectedSheet(context, step);
        HSSFRow headRow = currentSheet.getRow(0);

        StringBuilder assertStringBuilder = new StringBuilder();
        int previousColumnIndex = -1;

        for (int i = 0; i <= headRow.getLastCellNum(); i++) {
            HSSFCell cell = headRow.getCell(i);
            if (cell == null) {
                continue;
            }
            String columnName = cell.getStringCellValue().trim();
            if (step.getExcludedColumns().contains(columnName)) {
                continue;
            }

            int columnIndex = TableTools.searchColumn(table, columnName);

            if (step.isCheckColumnOrder() && columnIndex <= previousColumnIndex) {
                throw new ColumnOrderException(
                      "La colonne \""
                      + table.getColumnName(previousColumnIndex) + "\" de la table \"" + step.getName()
                      + "\" devrait �tre avant la colonne \"" + columnName + "\"");
            }

            previousColumnIndex = columnIndex;

            checkTableColumn(table, columnIndex, currentSheet, i, columnName, assertStringBuilder, step);
        }
        if (assertStringBuilder.length() > 0) {
            throw new GuiAssertException(assertStringBuilder.toString());
        }
    }


    public static String computeColumnCountErrorMessage(int expectedColumnCount, int actualColumnCount) {
        return "Le nombre de colonnes de la table diff�re du nombre de colonnes attendu : (attendu '"
               + expectedColumnCount + "', obtenu '" + actualColumnCount + "')";
    }


    static String computeRowCountErrorMessage(int expectedRowCount, int actualRowCount) {
        return "Le nombre de lignes de la table diff�re du nombre de lignes attendu : (attendu '"
               + expectedRowCount + "', obtenu '" + actualRowCount + "')";
    }


    private List<String> getExcludedColumns(T step) {
        List<String> excludedColumnList = new ArrayList<String>();
        if (Util.isNotEmpty(step.getExcludedColumns())) {
            StringTokenizer st = new StringTokenizer(step.getExcludedColumns(), ",");
            while (st.hasMoreTokens()) {
                excludedColumnList.add(st.nextToken().trim());
            }
        }
        return excludedColumnList;
    }


    private void checkTableColumn(JTable table, int columnIndex, HSSFSheet sheet, int sheetColumn,
                                  String columnName, StringBuilder assertStringBuilder, T step) {
        for (short i = 1; i <= step.getExpectedRowCount(); i++) {
            try {
                HSSFRow sheetRow = sheet.getRow(i);
                String cellValue = "";
                if (sheetRow != null) {
                    HSSFCell expectedCell = sheetRow.getCell(sheetColumn);
                    if (expectedCell != null) {
                        cellValue = step.getCellValueStringifier().toString(expectedCell);
                    }
                }

                String tableValue = getTableValue(table, i - 1, columnIndex);
                assertExpectedValue(cellValue, tableValue, i, columnName, step);
            }
            catch (GuiAssertException gae) {
                assertStringBuilder.append(gae.getMessage());
            }
            catch (GuiConfigurationException e) {
                throw new GuiAssertException("Composant table '" + table + "' : cellule [" + i + ", "
                                             + sheetColumn + "] incompr�hensible.");
            }
        }
    }


    private String getTableValue(JTable table, int row, int realColumn) {
        String value;
        Object modelValue = table.getValueAt(row, realColumn);
        TableCellRenderer renderer = table.getCellRenderer(row, realColumn);
        if (renderer == null) {
            value = modelValue.toString();
        }
        else {
            final Component rendererComponent =
                  renderer.getTableCellRendererComponent(table, modelValue, false, false, row, realColumn);
            if (rendererComponent instanceof JLabel) {
                value = ((JLabel)rendererComponent).getText();
            }
            else if (rendererComponent instanceof JCheckBox) {
                value = String.valueOf(((JCheckBox)rendererComponent).isSelected());
            }
            else if (rendererComponent instanceof JTree) {
                value = ((JTree)rendererComponent).getPathForRow(row).getLastPathComponent().toString();
            }
            else {
                throw new GuiAssertException("Unexpected renderer type for Table");
            }
        }
        return value.trim();
    }


    private void assertExpectedValue(String expectedValue, String tableValue, int row, String column, T step) {
        if (replaceWhiteSpace(expectedValue).compareTo(replaceWhiteSpace(tableValue)) != 0) {
            throw new GuiAssertException(computeUnexpectedValueErrorMessage(step.getName(), row, column,
                                                                            expectedValue, tableValue));
        }
    }


    private String replaceWhiteSpace(String spaced) {
        return spaced.replace(Character.toChars(160)[0], Character.toChars(32)[0]);
    }


    static String computeUnexpectedValueErrorMessage(String tableName, int row, String column,
                                                     String expectedValue, String actualValue) {
        return "Composant Table '" + tableName + "' [ligne '" + row + "', colonne '" + column
               + "'] : attendu='" + expectedValue + "' obtenu='" + actualValue + "'\n";
    }


    private HSSFSheet getExpectedSheet(TestContext context, T step) {
        String testDirectory = context.getProperty(StepPlayer.TEST_DIRECTORY);
        HSSFSheet sheet;
        try {
            HSSFWorkbook workbook = ExcelUtil.loadWorkbook(new File(testDirectory, step.getFile()));

            if (Util.isNotEmpty(step.getSheetName())) {
                sheet = workbook.getSheet(step.getSheetName());
            }
            else {
                sheet = workbook.getSheetAt(0);
            }
        }
        catch (Exception ex) {
            throw new GuiConfigurationException("impossible de charger le fichier Excel !", ex);
        }
        return sheet;
    }
}
