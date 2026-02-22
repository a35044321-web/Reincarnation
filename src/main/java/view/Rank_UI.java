package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Characters;
import service.Characters.CharactersService;
import service.Characters.CharactersServiceImpl;
import util.RealmHelper;

public class Rank_UI extends JDialog {
    private CharactersService charService = new CharactersServiceImpl();

    /**
     * 🚀 天道榜獨立測試入口
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        EventQueue.invokeLater(() -> {
            try {
                // 💡 建立一個測試用的對話框 (無父視窗)
                Rank_UI dialog = new Rank_UI(null);
                dialog.setVisible(true);
                System.out.println("📜 [Rank_UI] 天道名錄感應成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Rank_UI(Frame owner) {
        super(owner, "--- 天道榜 ---", true);
        setSize(500, 650);
        setLayout(null);
        getContentPane().setBackground(new Color(15, 20, 25));
        setLocationRelativeTo(owner);
        setUndecorated(true); // 💡 去除邊框，更有古風法碑感

        // 1. 標題：天道榜 (使用金色漸層感的文字)
        JLabel lblTitle = new JLabel("📜 天 道 名 錄 📜", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 32));
        lblTitle.setForeground(new Color(255, 215, 0)); 
        lblTitle.setBounds(0, 40, 500, 50);
        add(lblTitle);

        // 2. 排行清單容器 (使用 BoxLayout 垂直排列)
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        
        // 🚀 核心邏輯：抓取數據 (如果 Service 沒資料，會顯示空白或報錯)
        List<Characters> topList;
        try {
            topList = charService.findAllOrderByExp();
        } catch (Exception e) {
            // 💡 預置模擬數據 (防止資料庫連線失敗時無法測試)
            topList = getMockData();
        }
        
        for (int i = 0; i < Math.min(10, topList.size()); i++) {
            Characters c = topList.get(i);
            listPanel.add(createRankRow(i + 1, c));
            listPanel.add(Box.createVerticalStrut(10)); // 間距
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBounds(50, 110, 400, 430);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        add(scroll);

        // 3. 退出按鈕
        JButton btnClose = new JButton("退 出 瞻 仰");
        btnClose.setBounds(150, 560, 200, 50);
        btnClose.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        btnClose.setForeground(new Color(184, 134, 11));
        btnClose.setBackground(Color.BLACK);
        btnClose.setFocusPainted(false);
        btnClose.setBorder(new LineBorder(new Color(184, 134, 11), 2));
        btnClose.addActionListener(e -> dispose());
        add(btnClose);
    }

    private JPanel createRankRow(int rank, Characters c) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(true);
        row.setBackground(new Color(30, 35, 40, 150));
        row.setMaximumSize(new Dimension(400, 50));
        row.setBorder(new LineBorder(new Color(184, 134, 11, 80), 1));

        String rankColor = (rank <= 3) ? "#FFD700" : "#D3D3D3";
        String name = c.getCharacters_name();
        String realm = RealmHelper.getRealmName(c.getRealm());
        int atk = charService.calculateFinalAtk(c);

        String text = "<html><body style='width: 300px; padding: 5px;'>" +
                "<font color='" + rankColor + "'><b>[" + rank + "]</b></font> " +
                "<font color='white'><b>" + name + "</b></font> " +
                "<font color='#5F9EA0'>(" + realm + ")</font> " +
                "<br><font color='#FF4500'>⚔️ 戰力: " + atk + "</font>" +
                "</body></html>";

        JLabel lblData = new JLabel(text);
        lblData.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
        row.add(lblData, BorderLayout.CENTER);

        return row;
    }

    // 💡 模擬數據：供獨立測試使用
    private List<Characters> getMockData() {
        List<Characters> list = new ArrayList<>();
        String[] names = {"天音仙子", "青雲劍客", "血影狂刀"};
        for (int i = 0; i < names.length; i++) {
            Characters c = new Characters();
            c.setCharacters_name(names[i]);
            c.setRealm(i + 1);
            c.setExp(2000L * (3 - i));
            c.setBase_atk(100);
            list.add(c);
        }
        return list;
    }
}