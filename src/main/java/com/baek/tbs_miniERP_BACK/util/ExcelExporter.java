package com.baek.tbs_miniERP_BACK.util;

import com.baek.tbs_miniERP_BACK.dto.AssetListDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {
    private static final String[] HEADERS = {
            "품번",
            "종류",
            "제조사",
            "제조년월",
            "모델명",
            "시리얼번호",
            "성명",
            "직위",
            "소속",
            "설치장소",
            "지급일",
            "비고"
    };

    public static byte[] export(List<AssetListDTO> assets) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("자산목록");

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
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

            int rowIdx = 1; // 4행부터 데이터

            for (AssetListDTO asset : assets) {
                Row row = sheet.createRow(rowIdx++);

                // 품번 (assetId)
                int col = 0;
                row.createCell(col++).setCellValue(
                        asset.getAssetId() != null ? asset.getAssetId() : ""
                );

                // 종류
                row.createCell(col++).setCellValue(
                        nvl(asset.getAssetType())
                );

                // 제조사
                row.createCell(col++).setCellValue(
                        nvl(asset.getAssetManufacturer())
                );

                // 제조년월 (LocalDateTime → yyyy-MM)
                if (asset.getAssetManufacturedAt() != null) {
                    row.createCell(col++).setCellValue(
                            asset.getAssetManufacturedAt().format(yearMonthFormatter)
                    );
                } else {
                    row.createCell(col++).setCellValue("");
                }

                // 모델명
                row.createCell(col++).setCellValue(
                        nvl(asset.getAssetModelName())
                );

                // 시리얼번호
                row.createCell(col++).setCellValue(
                        nvl(asset.getAssetSn())
                );

                // 성명
                row.createCell(col++).setCellValue(
                        nvl(asset.getEmpName())
                );

                // 직위
                row.createCell(col++).setCellValue(
                        nvl(asset.getEmpPos())
                );

                // 소속
                row.createCell(col++).setCellValue(
                        nvl(asset.getTeamName())
                );

                // 설치장소
                row.createCell(col++).setCellValue(
                        nvl(asset.getAssetLoc())
                );

                // 지급일
                if (asset.getAssetIssuanceDate() != null) {
                    row.createCell(col++).setCellValue(
                            asset.getAssetIssuanceDate().format(dateFormatter)
                    );
                } else {
                    row.createCell(col++).setCellValue("");
                }

                // 비고
                row.createCell(col).setCellValue(
                        nvl(asset.getAssetDesc())
                );
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
