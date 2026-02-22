package util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;

public class ImageLoader {

	public static ImageIcon getIcon(String category, String fileName, int width, int height) {
	    Image img = null;
	    
	    // 🚀 核心修正：將路徑改為 Classpath 格式 (以 / 開頭)
	    // 💡 根據您 pom.xml 的 <directory>resources</directory> 設定，
	    // 打包後 images 資料夾會直接出現在 JAR 的根目錄下。
	    String jarPath = "/images/" + category + "/" + fileName;
	    
	    // 1. 優先嘗試從 JAR 內部 (Classpath) 抓取
	    URL imgURL = ImageLoader.class.getResource(jarPath);
	    
	    if (imgURL != null) {
	        img = new ImageIcon(imgURL).getImage();
	    } else {
	        // 2. 備援方案：如果在 IDE 開發環境，嘗試物理路徑 (您原本的邏輯)
	        File file = new File("resources/images/" + category + "/" + fileName);
	        if (file.exists()) {
	            img = new ImageIcon(file.getAbsolutePath()).getImage();
	        }
	    }

	    // 🚨 終極防呆：如果還是找不到
	    if (img == null) {
	        System.err.println("❌ [天道遺失] 找不到資源: " + jarPath);
	        // 回傳一個空的透明圖片，避免程式崩潰
	        return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
	    }

	    // 平滑縮放並回傳
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