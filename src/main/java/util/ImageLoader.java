package util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;

public class ImageLoader {

    public static ImageIcon getIcon(String category, String fileName, int width, int height) {
        Image img = null;
        
        // 💡 針對道友的專案結構：reincarnation/resources/images/...
        // 嘗試三種物理路徑，確保絕對抓到「將錯就錯」的 armor_piture1.jpg
        String[] tryPaths = {
            "resources/images/" + category + "/" + fileName,        // 1. 根目錄下的 resources (你的結構)
            "src/main/resources/images/" + category + "/" + fileName, // 2. 標準 Maven 結構
            "target/classes/images/" + category + "/" + fileName     // 3. 編譯後的路徑
        };

        for (String p : tryPaths) {
            File file = new File(p);
            if (file.exists()) {
                img = new ImageIcon(file.getAbsolutePath()).getImage();
                break; // 💡 只要抓到一個就跳出
            }
        }

        if (img == null) {
            // 🚨 如果還是找不到，印出絕對路徑檢查 Java 到底在看哪裡
            System.err.println("❌ [ImageLoader] 資源失聯: " + fileName);
            System.err.println("🔍 檢查實體路徑: " + new File("resources/images/" + category + "/" + fileName).getAbsolutePath());
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        }

        return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * 💡 靈光濾鏡：背光發光效果 (保持裝備原色)
     */
    public static ImageIcon applyQualityFilter(ImageIcon baseIcon, Color qualityColor, int width, int height) {
        if (baseIcon == null) return null;
        BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffered.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (qualityColor != null && qualityColor.getAlpha() > 0) {
            int cx = width / 2, cy = height / 2;
            g2d.setColor(new Color(qualityColor.getRed(), qualityColor.getGreen(), qualityColor.getBlue(), 130));
            g2d.fillOval(cx - (int)(width*0.45), cy - (int)(height*0.45), (int)(width*0.9), (int)(height*0.9));
            g2d.setColor(new Color(qualityColor.getRed(), qualityColor.getGreen(), qualityColor.getBlue(), 200));
            g2d.fillOval(cx - (int)(width*0.3), cy - (int)(height*0.3), (int)(width*0.6), (int)(height*0.6));
        }

        g2d.setComposite(AlphaComposite.SrcOver);
        int iw = (int)(width * 0.8), ih = (int)(height * 0.8);
        g2d.drawImage(baseIcon.getImage(), (width-iw)/2, (height-ih)/2, iw, ih, null);
        
        g2d.dispose();
        return new ImageIcon(buffered);
    }
}