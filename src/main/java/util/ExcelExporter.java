package util;

import java.io.*;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import model.Characters;
import model.Items;
import service.Characters.CharactersService;
import service.Characters.CharactersServiceImpl;

public class ExcelExporter {
    private static CharactersService charService = new CharactersServiceImpl();
    public static void exportItemsToExcel(String filePath, List<Items> itemList) {
        String sheetName = "全服法寶百科";
        String[] titles = {"法寶ID", "名稱", "類型", "攻擊加成", "防禦加成", "敘述"};

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet(sheetName);
            HSSFRow headerRow = sheet.createRow(0);

            // 1. 建立標題列
            for (int i = 0; i < titles.length; i++) {
                headerRow.createCell(i).setCellValue(titles[i]);
            }

            // 2. 填入法寶數據
            for (int i = 0; i < itemList.size(); i++) {
                Items item = itemList.get(i);
                HSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getItem_id());
                row.createCell(1).setCellValue(item.getItem_name());
                row.createCell(2).setCellValue(item.getItem_type());
                row.createCell(3).setCellValue(item.getAtk_bonus());
                row.createCell(4).setCellValue(item.getDef_bonus());
                row.createCell(5).setCellValue(item.getDescription());
            }

            // 3. 寫入檔案
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 🔱 天道特供：將全服修士名錄匯出至 Excel
     */
    public static void exportRankToExcel(String filePath, List<Characters> charList) {
        String sheetName = "全服修士名錄";
        String[] titles = {"排名", "道號", "境界", "修為(EXP)", "當前體力", "最終攻擊", "最終防禦"};

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet(sheetName);
            HSSFRow headerRow = sheet.createRow(0);

            // 1. 建立金色標題列
            for (int i = 0; i < titles.length; i++) {
                headerRow.createCell(i).setCellValue(titles[i]);
            }

            // 2. 填入修士數據
            for (int i = 0; i < charList.size(); i++) {
                Characters c = charList.get(i);
                HSSFRow row = sheet.createRow(i + 1);
                
                row.createCell(0).setCellValue(i + 1); // 排名
                row.createCell(1).setCellValue(c.getCharacters_name());
                row.createCell(2).setCellValue(util.RealmHelper.getRealmName(c.getRealm()));
                row.createCell(3).setCellValue(c.getExp());
                row.createCell(4).setCellValue(c.getStamina());
                
                // 🚀 導出經過 Service 計算後的最終戰力
                row.createCell(5).setCellValue(charService.calculateFinalAtk(c));
                row.createCell(6).setCellValue(charService.calculateFinalDef(c));
            }

            // 3. 寫入檔案
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}