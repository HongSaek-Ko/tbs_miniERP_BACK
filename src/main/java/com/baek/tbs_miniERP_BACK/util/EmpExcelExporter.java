package com.baek.tbs_miniERP_BACK.util;

import com.baek.tbs_miniERP_BACK.dto.EmpDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class EmpExcelExporter {
    private static final String[] HEADERS = {
        "사번",
        "성명",
        "직위",
        "소속",
        "재직 상태"
    };

    public static byte[] export(List<EmpDTO> emps) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("직원목록");

            // 헤더
            Row headerRow= sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            int rowIdx = 1;
            for (EmpDTO emp : emps) {
                Row row = sheet.createRow(rowIdx++);

                int col=0;

                row.createCell(col++).setCellValue(nvl(String.valueOf(emp.getEmpId())));
                row.createCell(col++).setCellValue(nvl(emp.getEmpName()));
                row.createCell(col++).setCellValue(nvl(emp.getEmpPos()));
                row.createCell(col++).setCellValue(nvl(emp.getTeamName()));
                String est = nvl(emp.getEmpStatus()).toLowerCase();
                if(est.contains("emp") || est.contains("재직")) {
                    est = "재직";
                } else if (est.contains("resign") || est.contains("retire") || est.contains("퇴직")) {
                    est = "퇴직";
                } else {
                    est = "휴직/기타";
                }
                row.createCell(col).setCellValue(est);
            }

            workbook.write(bos);

            // 컬럼 폭 자동 조정
            return bos.toByteArray();

        }catch (IOException e) {
            throw new RuntimeException("직원 엑셀 파일 생성 중 오류", e);
        }
    }

    private static String nvl(String s) {
        return s == null ?"" : s;
    }
}
