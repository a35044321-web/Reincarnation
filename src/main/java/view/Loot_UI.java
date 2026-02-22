package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Items;
import util.ImageLoader;

public class Loot_UI extends JDialog {

    /**
     * 🚀 修正版建構子：新增 isSuccess 參數，確保勝負判定準確
     * @param owner 父視窗
     * @param isSuccess 戰鬥是否勝利 (由 Service 傳回)
     * @param droppedItem 掉落的法寶 (若無則傳 null)
     * @param expGain 獲得的修為數值
     */
    public Loot_UI(Frame owner, boolean isSuccess, Items droppedItem, int expGain) {
        super(owner, "--- 天道結算 ---", true);
        setSize(500, 450);
        setLayout(null);
        getContentPane().setBackground(new Color(15, 20, 25));
        setLocationRelativeTo(owner);
        setUndecorated(true); // 去除邊框，更有質感
        
        // --- 1. 背景裝飾邊框 ---
        JPanel borderPanel = new JPanel(null);
        borderPanel.setBounds(0, 0, 500, 450);
        borderPanel.setOpaque(false);
        borderPanel.setBorder(new LineBorder(new Color(184, 134, 11, 150), 3));
        add(borderPanel);

        // --- 2. 頂部標題：根據勝負顯示 ---
        JLabel lblTitle = new JLabel("", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 32));
        lblTitle.setBounds(0, 30, 500, 50);
        
        if (isSuccess) {
            lblTitle.setText("<html><font color='#FFD700'>✨ 歷 練 大 捷 ✨</font></html>");
        } else {
            lblTitle.setText("<html><font color='#D3D3D3'>⚔️ 惜 敗 歸 陣 ⚔️</font></html>");
        }
        borderPanel.add(lblTitle);

        // --- 3. 修為數值顯示 ---
        JLabel lblExp = new JLabel("✨ 獲得修為：+" + expGain, SwingConstants.CENTER);
        lblExp.setForeground(new Color(138, 43, 226)); // 紫色靈氣感
        lblExp.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        lblExp.setBounds(0, 90, 500, 40);
        borderPanel.add(lblExp);

        // --- 4. 中間內容區：有寶物 vs 沒寶物 ---
        if (isSuccess && droppedItem != null) {
            // 🏆 勝利且掉寶：顯示法寶圖片與名稱
            JLabel lblItemImg = new JLabel();
            lblItemImg.setBounds(150, 140, 200, 200);
            
            Color qColor = getQualityColor(droppedItem.getItem_name());
            // 🚀 JAR 內感應圖片加載
            ImageIcon icon = ImageLoader.getIcon("items", droppedItem.getImage_path(), 180, 180);
            if (icon != null) {
                lblItemImg.setIcon(ImageLoader.applyQualityFilter(icon, qColor, 200, 200));
            }
            borderPanel.add(lblItemImg);

            JLabel lblName = new JLabel("⚔️ " + droppedItem.getItem_name(), SwingConstants.CENTER);
            lblName.setForeground(qColor);
            lblName.setFont(new Font("Microsoft JhengHei", Font.BOLD, 20));
            lblName.setBounds(0, 330, 500, 30);
            borderPanel.add(lblName);
            
        } else if (isSuccess) {
            // 💨 勝利但沒掉寶
            JLabel lblMsg = new JLabel("<html><center>此役大捷，唯機緣未至<br><font size='4' color='gray'>( 未能尋得法寶 )</font></center></html>", SwingConstants.CENTER);
            lblMsg.setForeground(Color.LIGHT_GRAY);
            lblMsg.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 20));
            lblMsg.setBounds(0, 180, 500, 80);
            borderPanel.add(lblMsg);
            
        } else {
            // 💀 戰敗
            JLabel lblMsg = new JLabel("<html><center>負傷敗退，此行無緣得寶<br><font size='4' color='red'>( 僅感悟到少量修為 )</font></center></html>", SwingConstants.CENTER);
            lblMsg.setForeground(new Color(150, 150, 150));
            lblMsg.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 20));
            lblMsg.setBounds(0, 180, 500, 80);
            borderPanel.add(lblMsg);
        }

        // --- 5. 底部按鈕 ---
        JButton btnClose = new JButton("收 納 靈 氣");
        btnClose.setBounds(150, 380, 200, 45);
        btnClose.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        btnClose.setForeground(new Color(184, 134, 11));
        btnClose.setBackground(Color.BLACK);
        btnClose.setFocusPainted(false);
        btnClose.setBorder(new LineBorder(new Color(184, 134, 11), 2));
        btnClose.addActionListener(e -> dispose());
        borderPanel.add(btnClose);
    }

    private Color getQualityColor(String name) {
        if (name == null) return Color.WHITE;
        if (name.contains("極品")) return new Color(255, 165, 0, 200);
        if (name.contains("上品")) return new Color(0, 191, 255, 200);
        if (name.contains("中品")) return new Color(50, 255, 50, 200);
        return new Color(255, 255, 255, 150);
    }
}