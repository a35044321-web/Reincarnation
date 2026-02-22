package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Characters;
import service.Characters.CharactersService;
import service.Characters.CharactersServiceImpl;

public class Admin_UI extends JFrame {
    private CharactersService charService = new CharactersServiceImpl();
    private JTable charTable;
    private DefaultTableModel tableModel;

    public Admin_UI() {
        initWindow();
        initTabbedPane();
    }

    private void initWindow() {
        setTitle("九霄天道宮 - 管理聖殿");
        setBounds(100, 100, 1100, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 20, 25));
    }

    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
        
        // 🚀 1. 修士名錄分頁
        tabbedPane.addTab("👥 修士名錄", createCharManagerPanel());
        
        // 🚀 2. 法寶倉庫分頁 (預留)
        tabbedPane.addTab("📦 法寶管理", createItemManagerPanel());

        add(tabbedPane);
    }
    private JPanel createItemManagerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(20, 25, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 表格設定 (維持原樣)
        String[] columns = {"ID", "法寶名稱", "類型", "攻擊加成", "防禦加成", "敘述"};
        DefaultTableModel itemModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable itemTable = new JTable(itemModel);
        itemTable.setRowHeight(30);
        
        dao.Items.Items_DAO itemsDao = new dao.Items.Items_DAO_Impl();
        java.util.List<model.Items> allItems = itemsDao.findAll();
        for (model.Items i : allItems) {
            itemModel.addRow(new Object[]{
                i.getItem_id(), i.getItem_name(), i.getItem_type(), 
                i.getAtk_bonus(), i.getDef_bonus(), i.getDescription()
            });
        }
        panel.add(new JScrollPane(itemTable), BorderLayout.CENTER);

        // 2. 下方功能鈕區域
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setOpaque(false);
        
        // --- 導出按鈕 (維持原樣) ---
        JButton btnExportItems = new JButton("📑 導出並列印法寶帳本");
        btnExportItems.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("九霄法寶百科.xls"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                util.ExcelExporter.exportItemsToExcel(path, allItems);
                JOptionPane.showMessageDialog(this, "📜 法寶百科已導出至：" + path);
                try { java.awt.Desktop.getDesktop().print(new java.io.File(path)); } 
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "⚠️ 自動列印失敗。"); }
            }
        });

        // 🚀 核心新增：【🎁 賞賜修士】邏輯
        JButton btnGift = new JButton("🎁 賞賜修士");
        btnGift.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "請先在表格中選中一件【法寶】！");
                return;
            }

            // 1. 取得法寶資訊
            int itemId = (int) itemModel.getValueAt(selectedRow, 0);
            String itemName = (String) itemModel.getValueAt(selectedRow, 1);

            // 2. 獲取全服修士清單
            java.util.List<model.Characters> allChars = charService.findAllOrderByExp();
            String[] charNames = allChars.stream().map(model.Characters::getCharacters_name).toArray(String[]::new);

            // 3. 彈出下拉式選擇選單
            String targetName = (String) JOptionPane.showInputDialog(this, 
                    "請選擇要接收【" + itemName + "】的修士：", "天道敕封",
                    JOptionPane.QUESTION_MESSAGE, null, charNames, charNames[0]);

            if (targetName != null) {
                // 4. 找到目標修士 ID
                model.Characters target = allChars.stream()
                    .filter(c -> c.getCharacters_name().equals(targetName))
                    .findFirst().orElse(null);

                if (target != null) {
                    // 💡 執行賞賜：呼叫 itemService 將物品塞入對方背包 (數量 1)
                    // 這裡會使用到您之前寫在 ItemServiceImpl 的 addLootToPlayer
                    service.Item.ItemService itemService = new service.Item.ItemServiceImpl();
                    itemService.addLootToPlayer(target.getCharacters_id(), itemId, 1);
                    
                    JOptionPane.showMessageDialog(this, "✨ 敕封成功！\n修士：" + targetName + "\n獲得：" + itemName);
                }
            }
        });

        JButton btnEditItem = new JButton("🛠️ 修改法寶");
        btnEditItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "法寶重塑功能尚在煉製中...");
        });

        btnPanel.add(btnExportItems); 
        btnPanel.add(btnGift);
        btnPanel.add(btnEditItem);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
    private JPanel createCharManagerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(20, 25, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 表格設定 (維持原樣)
        String[] columns = {"ID", "道號", "境界", "修為(EXP)", "體力", "攻擊", "防禦"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        charTable = new JTable(tableModel);
        charTable.setRowHeight(30);
        charTable.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        
        refreshCharTable(); 
        JScrollPane scroll = new JScrollPane(charTable);
        panel.add(scroll, BorderLayout.CENTER);

        // 2. 下方功能鈕區域
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // 增加間距
        btnPanel.setOpaque(false);
        
        // 🚀 新增：【導出並列印天道帳本】按鈕
        JButton btnExport = new JButton("📑 導出並列印帳本");
        btnExport.addActionListener(e -> {
            // A. 讓管理員選擇存放路徑
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("天道修士名錄.xls"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                List<Characters> allChars = charService.findAllOrderByExp();
                
                // B. 呼叫工具類執行 POI 寫入
                util.ExcelExporter.exportRankToExcel(path, allChars);
                
                JOptionPane.showMessageDialog(this, "📜 帳本已封存在：" + path);
                
                // 🚀 C. 執行【列印】功能 (感應系統印表機)
                try {
                    java.awt.Desktop.getDesktop().print(new java.io.File(path));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "⚠️ 自動列印失敗，請手動開啟 Excel 列印。");
                }
            }
        });

        JButton btnEdit = new JButton("🛠️ 修改修為/屬性");
        btnEdit.addActionListener(e -> handleEditChar());
        
        JButton btnRefresh = new JButton("🔄 刷新名錄");
        btnRefresh.addActionListener(e -> refreshCharTable());
        
        // 按鈕依序加入
        btnPanel.add(btnExport); // 👈 放在最前面
        btnPanel.add(btnEdit);
        btnPanel.add(btnRefresh);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshCharTable() {
        tableModel.setRowCount(0);
        List<Characters> list = charService.findAllOrderByExp(); // 使用現成的方法
        for (Characters c : list) {
            tableModel.addRow(new Object[]{
                c.getCharacters_id(), c.getCharacters_name(), c.getRealm(),
                c.getExp(), c.getStamina(), c.getBase_atk(), c.getBase_def()
            });
        }
    }

    private void handleEditChar() {
        int row = charTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "請先選擇一位修士！");
            return;
        }
        int charId = (int) tableModel.getValueAt(row, 0);
        // 🚀 呼叫第二張 UI：資料修改彈窗
        new AdminEdit_UI(this, charId).setVisible(true);
        refreshCharTable(); // 關閉修改視窗後刷新
    }
}