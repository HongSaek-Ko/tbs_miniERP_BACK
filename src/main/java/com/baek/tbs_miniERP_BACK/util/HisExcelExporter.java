package com.baek.tbs_miniERP_BACK.util;

import com.baek.tbs_miniERP_BACK.dto.AssetHistoryListDTO;
import com.baek.tbs_miniERP_BACK.dto.AssetListDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HisExcelExporter {
    private static final String[] HEADERS = {
            "현재 소유자",
            "변동(이관) 사유",
            "변동(이관) 시간",
    };

    public static byte[] export(List<AssetHistoryListDTO> assets) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("변동 이력");

            // 3행: 헤더
            Row headerRow = sheet.createRow(0);
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

            // 날짜 포맷 (지급일, 제조년월 표시용)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

            int rowIdx = 1; // 4행부터 데이터

            for (AssetHistoryListDTO asset : assets) {
                Row row = sheet.createRow(rowIdx++);

                int col = 0;

                // 성명
                row.createCell(col++).setCellValue(
                        nvl(asset.getAssetHoldEmp())
                );

                // 변동 사유
                row.createCell(col++).setCellValue(
                        nvl(asset.getAssetHistoryDesc())
                );

                // 변동 시간
                row.createCell(col++).setCellValue(
                        asset.getAssetHistoryDate().format(dateFormatter));
            }

            workbook.write(bos);
            return bos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 오류", e);
        }
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
