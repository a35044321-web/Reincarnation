package view;

import java.awt.*;
import javax.swing.*;
import model.Items;
import util.ImageLoader;
import util.UIHelper;

public class Loot_UI extends JDialog {
	public static void main(String[] args) {
        // 1. 設定系統質感
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        EventQueue.invokeLater(() -> {
            try {
                // 2. 模擬從資料庫抓到的「極品」神兵數據
                model.Items mockLoot = new model.Items();
                mockLoot.setItem_name("龍吟古劍(極品)");
                mockLoot.setImage_path("sword_piture1.jpg"); // 💡 確保此圖在 resources/images/items/
                mockLoot.setAtk_bonus(80);
                
                // 3. 模擬獲得的修為數值
                int mockExp = 250;

                // 4. 啟動彈窗 ( owner 傳 null 則置中顯示 )
                Loot_UI dialog = new Loot_UI(null, mockLoot, mockExp);
                
                // 💡 增加一個提示，告訴玩家怎麼關閉
                JLabel lblHint = new JLabel("--- 點擊任意處感應靈氣並收納 ---", SwingConstants.CENTER);
                lblHint.setForeground(Color.GRAY);
                lblHint.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 12));
                lblHint.setBounds(100, 350, 300, 20);
                dialog.add(lblHint);
                
                dialog.setVisible(true);
                
                System.out.println("✨ [Loot_UI] 戰利品結算畫面已啟動！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    // 💡 使用 JDialog 產生「強制彈窗」效果
	public Loot_UI(Frame owner, Items droppedItem, int expGain) {
    super(owner, "--- 天道結算 ---", true);
    setSize(500, 450);
    setLayout(null);
    getContentPane().setBackground(new Color(15, 20, 25));
    setLocationRelativeTo(owner);
    setUndecorated(true);
    JButton btnClose = new JButton("感應靈氣並收納");
    btnClose.setBounds(125, 370, 250, 45); // 放置在底部中央
    btnClose.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
    btnClose.setForeground(new Color(184, 134, 11)); // 古金色
    btnClose.setBackground(Color.BLACK);
    btnClose.setFocusPainted(false);
    btnClose.setBorder(new javax.swing.border.LineBorder(new Color(184, 134, 11), 2));

    // 💡 點擊按鈕即關閉
    btnClose.addActionListener(e -> dispose());
    
    // 💡 滑鼠懸停特效 (選擇性)
    btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
            btnClose.setBackground(new Color(30, 30, 30));
        }
        public void mouseExited(java.awt.event.MouseEvent e) {
            btnClose.setBackground(Color.BLACK);
        }
    });

    add(btnClose);
    // 💡 核心邏輯調整：只要有掉落物，就視為勝利；沒掉落物則根據經驗判斷
    boolean hasLoot = (droppedItem != null);

    // 1. 無論勝敗，只要有經驗就顯示 (紫色修為感)
    if (expGain > 0) {
        JLabel lblExp = new JLabel("✨ 獲得修為：+" + expGain, SwingConstants.CENTER);
        lblExp.setForeground(new Color(138, 43, 226)); 
        lblExp.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        lblExp.setBounds(50, 40, 400, 40);
        add(lblExp);
    }

    // 2. 根據是否有掉落物來決定中間的顯示
    if (!hasLoot) {
        // 🍂 失敗或未掉落的視覺 (針對您說的 1/3 情況)
        String msg = (expGain > 0) ? "🍂 歷練受挫，略有感悟" : "💀 氣息紊亂，無所感悟";
        JLabel lblStatus = new JLabel(msg, SwingConstants.CENTER);
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        lblStatus.setBounds(50, 160, 400, 40);
        add(lblStatus);
        
        if (expGain > 0) {
            JLabel lblHint = new JLabel("(雖未能擊敗對手，但磨練了根基)", SwingConstants.CENTER);
            lblHint.setForeground(new Color(100, 100, 100));
            lblHint.setBounds(50, 200, 400, 30);
            add(lblHint);
        }
    } else {
        // ⚔️ 勝利且有掉落物
        JLabel lblItemImg = new JLabel();
        lblItemImg.setBounds(150, 100, 200, 200);
        
        Color qColor = getQualityColor(droppedItem.getItem_name());
        lblItemImg.setIcon(ImageLoader.applyQualityFilter(
            ImageLoader.getIcon("items", droppedItem.getImage_path(), 180, 180), qColor, 200, 200));
        add(lblItemImg);

        JLabel lblName = new JLabel("⚔️ " + droppedItem.getItem_name(), SwingConstants.CENTER);
        lblName.setForeground(qColor);
        lblName.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        lblName.setBounds(50, 310, 400, 30);
        add(lblName);
    }

    addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) { dispose(); }
    });
}

    private Color getQualityColor(String name) {
        if (name.contains("極品")) return new Color(255, 165, 0, 200);
        if (name.contains("上品")) return new Color(0, 191, 255, 200);
        if (name.contains("中品")) return new Color(50, 255, 50, 200);
        return new Color(255, 255, 255, 150);
    }
}