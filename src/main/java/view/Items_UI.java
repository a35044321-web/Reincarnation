package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Characters;
import model.Items;
import service.Item.ItemServiceImpl;
import util.UIHelper;
import util.ImageLoader;
import java.util.List;

public class Items_UI extends JFrame {
    private Characters hero;
    private JPanel contentPane;
    private JLabel lblItemBigPreview, lblItemName, lblItemStats;
    private JButton btnEquip;
    private JPanel gridPanel;
    private JButton btnDiscard;
    private ItemServiceImpl itemService = new ItemServiceImpl();
    
    public Items_UI(Characters hero) {
        this.hero = hero;
        initWindow();
        initBackground();
        initItemDetail();
        // 💡 核心修正：先初始化容器，再填入格子
        initBagGridContainer(); 
        refreshBagGrid();
    }

    private void initWindow() {
        setTitle("乾坤袋 - " + hero.getCharacters_name());
        setBounds(100, 100, 1376, 768);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initBackground() {
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = ImageLoader.getIcon("bg", "background_piture4.jpg", 1376, 768);
                if (bg != null) g.drawImage(bg.getImage(), 0, 0, this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void initItemDetail() {
    	JPanel detailPanel = UIHelper.createTransparentPanel("--- 神兵詳情 ---");
        detailPanel.setBounds(50, 80, 400, 600);
        contentPane.add(detailPanel);

        JPanel infoCanvas = new JPanel(null);
        infoCanvas.setBounds(20, 40, 360, 540);
        infoCanvas.setOpaque(true);
        infoCanvas.setBackground(new Color(15, 20, 25));
        detailPanel.add(infoCanvas);

        // 1. 圖片區 (300x300)
        lblItemBigPreview = new JLabel();
        lblItemBigPreview.setBounds(30, 10, 300, 300);
        infoCanvas.add(lblItemBigPreview);

        // 2. 名稱區 (y=310)
        lblItemName = new JLabel("請感應法寶...", SwingConstants.CENTER);
        lblItemName.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        lblItemName.setForeground(new Color(100, 100, 100));
        lblItemName.setBounds(30, 310, 300, 40);
        infoCanvas.add(lblItemName);

        // 3. 屬性描述區 (y=350, 高度縮減為 80)
        lblItemStats = new JLabel("", SwingConstants.CENTER);
        lblItemStats.setForeground(Color.WHITE);
        lblItemStats.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        lblItemStats.setBounds(30, 350, 300, 80); 
        infoCanvas.add(lblItemStats);

        // 🚀 4. 裝備按鈕 (y=430)
        btnEquip = new JButton("裝備至道體");
        btnEquip.setBounds(30, 435, 300, 45); // 💡 往下移開，不擋描述
        btnEquip.setBackground(new Color(46, 139, 87));
        btnEquip.setForeground(Color.WHITE);
        btnEquip.setVisible(false);
        infoCanvas.add(btnEquip);

        // 🚀 5. 銷毀按鈕 (y=485)
        btnDiscard = new JButton("將此寶銷毀");
        btnDiscard.setBounds(30, 485, 300, 45); // 💡 放在裝備按鈕下方
        btnDiscard.setBackground(new Color(139, 0, 0)); 
        btnDiscard.setForeground(Color.WHITE);
        btnDiscard.setVisible(false);
        infoCanvas.add(btnDiscard);
    }

    private void initBagGridContainer() {
        // 💡 建立 4x4 網格容器並設定座標
        gridPanel = new JPanel(new GridLayout(4, 4, 15, 15));
        gridPanel.setBounds(480, 80, 840, 600); // 確保座標正確
        gridPanel.setOpaque(false);
        contentPane.add(gridPanel);
    }

 private void refreshBagGrid() {
    gridPanel.removeAll(); 
    List<model.Items> itemList = itemService.findPlayerItems(hero.getCharacters_id());

    for (int i = 0; i < 16; i++) {
        final int index = i; // 用於內部類
        
        // 💡 建立一個具備「自定義繪圖」能力的按鈕
        JButton slot = new JButton() {
            @Override
            public void paint(Graphics g) {
                super.paint(g); // 先畫原本的圖片
                
                // 🚀 判定是否裝備 (需放在 paint 內即時感應)
                if (index < itemList.size()) {
                    model.Items item = itemList.get(index);
                    boolean isEquipped = (item.getItem_id() == hero.getWeapon_id() || 
                                          item.getItem_id() == hero.getArmor_id());
                    
                    if (isEquipped) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // 1. 畫金色小底色 (右下角)
                        g2.setColor(new Color(218, 165, 32)); // 金色
                        g2.fillRect(getWidth() - 30, getHeight() - 25, 25, 20);
                        
                        // 2. 畫白色邊框
                        g2.setColor(Color.WHITE);
                        g2.drawRect(getWidth() - 30, getHeight() - 25, 25, 20);
                        
                        // 3. 寫上字母 E
                        g2.setColor(Color.BLACK);
                        g2.setFont(new Font("Arial", Font.BOLD, 14));
                        g2.drawString("E", getWidth() - 22, getHeight() - 10);
                        
                        g2.dispose();
                    }
                }
            }
        };

        slot.setBackground(new Color(20, 25, 30));
        
        if (i < itemList.size()) {
            Items item = itemList.get(i);
            
            // 設定法寶圖示
            ImageIcon base = util.ImageLoader.getIcon("items", item.getImage_path(), 80, 80);
            slot.setIcon(util.ImageLoader.applyQualityFilter(base, getQualityColor(item.getItem_name()), 80, 80));
            
            // 判定裝備後的邊框色
            boolean isEquipped = (item.getItem_id() == hero.getWeapon_id() || item.getItem_id() == hero.getArmor_id());
            slot.setBorder(new LineBorder(isEquipped ? new Color(218, 165, 32) : new Color(184, 134, 11, 80), 2));
            
            slot.addActionListener(e -> handleItemSelect(item));
            System.out.println("檢查項目:" + item.getItem_id()+ " | 英雄武器ID:" + hero.getWeapon_id());
        } else {
            slot.setEnabled(false);
            slot.setBorder(new LineBorder(new Color(50, 50, 50), 1));
        }

        gridPanel.add(slot);
    }
    
    gridPanel.revalidate();
    gridPanel.repaint();
    
}

   private void handleItemSelect(model.Items item) {
    // 1. 更新詳情面板資訊 (維持原樣)
    lblItemName.setText(item.getItem_name());
    lblItemName.setForeground(getQualityColor(item.getItem_name()));
    lblItemStats.setText("<html><div style='text-align: center;'>⚔️ 攻擊: +" + item.getAtk_bonus() 
                       + " | 🛡️ 防禦: +" + item.getDef_bonus() + "<br><br>" 
                       + item.getDescription() + "</div></html>");
    
    String img = (item.getImage_path() == null) ? "item_default.jpg" : item.getImage_path();
    Color qColor = getQualityColor(item.getItem_name());
    ImageIcon bigBase = ImageLoader.getIcon("items", img, 280, 280);
    lblItemBigPreview.setIcon(ImageLoader.applyQualityFilter(bigBase, qColor, 280, 280));

    // 💡 2. 處理【裝備按鈕】邏輯
    for (java.awt.event.ActionListener al : btnEquip.getActionListeners()) btnEquip.removeActionListener(al);
    btnEquip.setVisible(true);
    btnEquip.addActionListener(e -> {
        itemService.equipItem(hero, item);
        String type = item.getItem_type().trim();
        if ("武器".equals(type)) {
            hero.setWeapon_id(item.getItem_id());
        } else if ("護甲".equals(type)) {
            hero.setArmor_id(item.getItem_id());
        }
        JOptionPane.showMessageDialog(this, "✨ 已成功裝備【" + item.getItem_name() + "】！");
        btnEquip.setVisible(false);
        btnDiscard.setVisible(false); // 💡 同時隱藏丟棄按鈕
        refreshBagGrid();
    });

    // 🚀 3. 新增：處理【丟棄按鈕】邏輯
    // 💡 請確保您在 initItemDetail 裡有先寫 btnDiscard = new JButton("將此寶銷毀");
    for (java.awt.event.ActionListener al : btnDiscard.getActionListeners()) btnDiscard.removeActionListener(al);
    btnDiscard.setVisible(true);
    btnDiscard.addActionListener(e -> {
        int opt = JOptionPane.showConfirmDialog(this, "確定要將此法寶化為飛灰嗎？");
        if (opt == JOptionPane.YES_OPTION) {
            // 1. 執行銷毀 (現在 DAO 會直接 DELETE)
            itemService.discardItem(hero.getCharacters_id(), item.getItem_id());

            // 2. 重要：如果是穿著的，也要歸零
            if (hero.getWeapon_id() != null && hero.getWeapon_id().equals(item.getItem_id())) hero.setWeapon_id(0);
            if (hero.getArmor_id() != null && hero.getArmor_id().equals(item.getItem_id())) hero.setArmor_id(0);

            JOptionPane.showMessageDialog(this, "🔥 法寶已毀，靈氣回歸天地。");
            
            // 3. 隱藏 UI 元件
            btnEquip.setVisible(false);
            btnDiscard.setVisible(false);
            
            // 🚀 4. 強制刷新：這會重新執行 SQL 抓取剩餘道具
            refreshBagGrid(); 
        }
    });
}

    private Color getQualityColor(String name) {
        if (name == null) return Color.WHITE;
        if (name.contains("極品")) return new Color(255, 165, 0, 200);
        if (name.contains("上品")) return new Color(0, 191, 255, 200);
        if (name.contains("中品")) return new Color(50, 255, 50, 200);
        return new Color(255, 255, 255, 150);
    }
}