package com.example.resultgen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clean single-class Excel persistence helper using Apache POI.
 * Writes a single sheet named "Student Results" containing ID, Name, subjects..., Total, Average, Grade, Status, Rank.
 */
public class DB {
    public static void persist(String url, String user, String pass, List<StudentResult> results, List<String> subjects) throws SQLException {
        if (url == null || url.trim().isEmpty()) throw new SQLException("No Excel output path provided");
        String path = url.trim();
        if (path.startsWith("excel:")) path = path.substring("excel:".length());
        if (!path.toLowerCase().endsWith(".xlsx")) path = path + ".xlsx";

        Path out = Paths.get(path).toAbsolutePath();
        XSSFWorkbook wb = null;
        try {
            if (out.getParent() != null) Files.createDirectories(out.getParent());

            // Open existing workbook if present
            if (Files.exists(out)) {
                try (FileInputStream fis = new FileInputStream(out.toFile())) {
                    wb = new XSSFWorkbook(fis);
                    int idx = wb.getSheetIndex("Student Results");
                    if (idx >= 0) wb.removeSheetAt(idx);
                }
            }

            if (wb == null) wb = new XSSFWorkbook();

            Sheet sheet = wb.createSheet("Student Results");
            Row header = sheet.createRow(0);
            int c = 0;
            header.createCell(c++).setCellValue("ID");
            header.createCell(c++).setCellValue("Name");
            for (String s : subjects) header.createCell(c++).setCellValue(s);
            header.createCell(c++).setCellValue("Total");
            header.createCell(c++).setCellValue("Average");
            header.createCell(c++).setCellValue("Grade");
            header.createCell(c++).setCellValue("Status");
            header.createCell(c++).setCellValue("Rank");

            int r = 1;
            for (StudentResult sr : results) {
                Row row = sheet.createRow(r++);
                c = 0;
                row.createCell(c++).setCellValue(sr.student.id == null ? "" : sr.student.id);
                row.createCell(c++).setCellValue(sr.student.name == null ? "" : sr.student.name);
                for (String subj : subjects) row.createCell(c++).setCellValue(sr.student.marks.getOrDefault(subj, 0));
                row.createCell(c++).setCellValue(sr.total);
                row.createCell(c++).setCellValue(sr.average);
                row.createCell(c++).setCellValue(sr.grade == null ? "" : sr.grade);
                row.createCell(c++).setCellValue(sr.pass ? "PASS" : "FAIL");
                row.createCell(c++).setCellValue(sr.rank);
            }

            // autosize
            for (int i = 0; i < header.getLastCellNum(); i++) sheet.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(out.toFile())) {
                wb.write(fos);
            }
        } catch (IOException e) {
            throw new SQLException("Failed to write Excel file: " + e.getMessage(), e);
        } finally {
            if (wb != null) {
                try { wb.close(); } catch (IOException ignored) {}
            }
        }
    }
}
