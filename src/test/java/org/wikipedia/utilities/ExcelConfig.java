package org.wikipedia.utilities;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;


public class ExcelConfig {
	private static final Map<String, String> CONFIG = new HashMap<>();

	public static void load(String xlsxPath, String sheetName) throws Exception {
		try (FileInputStream fis = new FileInputStream(xlsxPath);
				Workbook wb = WorkbookFactory.create(fis)) {
					Sheet sheet = wb.getSheet(sheetName);
					DataFormatter fmt = new DataFormatter();
					for (int r = 1; r<= sheet.getLastRowNum(); r++) {
						Row row = sheet.getRow(r);
						if(row == null) continue;
						String key = fmt.formatCellValue(row.getCell(0)).trim();
						String val = fmt.formatCellValue(row.getCell(1)).trim();
						if (!key.isEmpty()) CONFIG.put(key,val);
					}
				}
	}

	public static String get(String key, String defaultVal) {
		return CONFIG.getOrDefault(key, defaultVal);
	}

}
