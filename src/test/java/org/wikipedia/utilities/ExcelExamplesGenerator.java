package org.wikipedia.utilities;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class ExcelExamplesGenerator {
	public static void main(String[] args) throws Exception {
		// Template feature dir (original features)
		String inDirStr = System.getProperty("excel.in", "src/test/resources/Features");
		// Generated output dir (generated features by DDT)
		String outDirStr = System.getProperty("excel.out", "src/test/resources/Features/_generated");

		Path inDir = Paths.get(inDirStr);
		Path outDir = Paths.get(outDirStr);
		Files.createDirectories(outDir);

		DataFormatter fmt = new DataFormatter();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(inDir, "*.feature")) {
			for (Path feature : stream) {
				List<String> lines = Files.readAllLines(feature, StandardCharsets.UTF_8);
				List<String> out = new ArrayList<>();

				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i);

					if (line.trim().startsWith("Examples:") && line.contains("fromExcel:")) {
						String indent = line.substring(0, line.indexOf("Examples:"));
						// Write clean Examples: (remove the marker)
						out.add(indent + "Examples:");

						// Next line is the header row (keep it)
						String headerLine = lines.get(++i);
						out.add(headerLine);

						// Parse header cells between pipes
						List<String> headers = new ArrayList<>();
						for (String part : headerLine.split("\\|")) {
							String t = part.trim();
							if (!t.isEmpty())
								headers.add(t);
						}

						// Marker format: # fromExcel: TestData/LoginData.xlsx|SheetName
						// Marker format: # fromExcel: TestData/LoginData.xlsx|SheetName
						String token = line.substring(line.indexOf("fromExcel:") + "fromExcel:".length()).trim();
						String[] parts = token.split("\\|");
						String xlsxPath = "src/test/resources/" + parts[0].trim();
						String sheetName = parts[1].trim();

						System.out.println("[DDT] Reading: " + xlsxPath + "  sheet: '" + sheetName + "'");

						try (FileInputStream fis = new FileInputStream(xlsxPath);
								Workbook wb = WorkbookFactory.create(fis)) {

							Sheet sh = wb.getSheet(sheetName); // exact match

							// Try a case-insensitive / trimmed match if exact failed
							if (sh == null) {
								for (int si = 0; si < wb.getNumberOfSheets(); si++) {
									String nm = wb.getSheetName(si);
									if (nm != null && nm.trim().equalsIgnoreCase(sheetName)) {
										sh = wb.getSheetAt(si);
										break;
									}
								}
							}

							if (sh == null) {
								List<String> names = new ArrayList<>();
								for (int si = 0; si < wb.getNumberOfSheets(); si++) {
									names.add("'" + wb.getSheetName(si) + "'");
								}
								throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in '" + xlsxPath
										+ "'. Available sheets: " + names);
							}

							int last = sh.getLastRowNum();
							for (int r = 1; r <= last; r++) { // skip header row 0
								Row row = sh.getRow(r);
								if (row == null)
									continue;
								StringBuilder sb = new StringBuilder(indent + "  |");
								boolean empty = true;
								for (int c = 0; c < headers.size(); c++) {
									String v = fmt.formatCellValue(row.getCell(c)).trim();
									if (!v.isEmpty())
										empty = false;
									sb.append(' ').append(v).append(" |");
								}
								if (!empty)
									out.add(sb.toString());
							}
						}

						continue; // we handled this Examples block
					}

					out.add(line); // pass-through
				}

				Files.write(outDir.resolve(feature.getFileName()), out, StandardCharsets.UTF_8);
			}
		}

		System.out.println("Generated features in: " + outDir.toAbsolutePath());
	}
}
