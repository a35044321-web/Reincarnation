package view;

import java.awt.*;
import javax.swing.*;
import model.Characters;
import model.ExpeditionResult;
import service.Characters.StaminaService;
import service.Characters.StaminaServiceImpl;
import service.Feature.ExpeditionService;
import service.Feature.ExpeditionServiceImpl;
import util.MusicPlayer;
import java.util.List;

public class ExpeditionFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JProgressBar staminaBar;
    private JTextArea logArea;
    private CombatCanvas combatCanvas; 
    private model.Characters hero;

    private service.Characters.StaminaService staminaService = new service.Characters.StaminaServiceImpl();
    private service.Feature.ExpeditionService expeditionService = new service.Feature.ExpeditionServiceImpl();
    private service.Item.ItemService itemService = new service.Item.ItemServiceImpl();

    public ExpeditionFrame(model.Characters hero) {
        this.hero = hero;
        MusicPlayer.stopBGM();
        MusicPlayer.playBGM("battle_music.mp3");

        setTitle("九霄尋道 - 歷練山河");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1376, 768);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = util.ImageLoader.getIcon("bg", "background_piture2.jpg", 1376, 768);
                if (bg != null) g.drawImage(bg.getImage(), 0, 0, this);
            }
        };
        bgPanel.setLayout(new BorderLayout(15, 15));
        setContentPane(bgPanel);
        this.contentPane = bgPanel;

        initComponents();
        initStaminaTimer();
    }
    
    private void initComponents() {
        // 1. 北部：資訊、體力條 ＋ 【返回按鈕】
        // 💡 修改：將 GridLayout 改為 BorderLayout 容納返回按鈕
        JPanel northPanel = new JPanel(new BorderLayout(20, 0));
        northPanel.setOpaque(false); 
        
        // 🚀 新增：返回洞府按鈕 (黑底金邊)
        JButton btnBack = new JButton("⬅ 返回洞府");
        btnBack.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
        btnBack.setForeground(new Color(184, 134, 11));
        btnBack.setBackground(Color.BLACK);
        btnBack.setOpaque(true);
        btnBack.setBorder(new javax.swing.border.LineBorder(new Color(184, 134, 11), 1));
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.addActionListener(e -> {
            util.MusicPlayer.stopBGM();
            new Character_UI(hero).setVisible(true);
            this.dispose();
        });
        northPanel.add(btnBack, BorderLayout.WEST);

        // 中間資訊容器
        JPanel infoWrapper = new JPanel(new GridLayout(1, 2, 20, 0));
        infoWrapper.setOpaque(false);

        // 🚀 核心修正：將數字轉化為「練氣前期」等稱號
        String realmName = util.RealmHelper.getRealmName(hero.getRealm());

        String infoText = "<html>" +
                "<font color='#B8860B'>【道友】" + hero.getCharacters_name() + "</font> " +
                "<font color='#D3D3D3'> | </font>" +
                "<font color='#5F9EA0'>境界: " + realmName + "</font>" + // 👈 使用變數
                "</html>";

        JLabel lblInfo = new JLabel(infoText);
        lblInfo.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));

        staminaBar = new JProgressBar(0, 100);
        staminaBar.setValue(hero.getStamina());
        staminaBar.setStringPainted(true);
        staminaBar.setString(hero.getStamina() + " / 100"); 
        staminaBar.setForeground(new Color(46, 139, 87));
        staminaBar.setBackground(new Color(20, 20, 20));
        staminaBar.setFont(new Font("Consolas", Font.BOLD, 16));
        
        infoWrapper.add(lblInfo);
        infoWrapper.add(staminaBar);
        northPanel.add(infoWrapper, BorderLayout.CENTER);
        
        contentPane.add(northPanel, BorderLayout.NORTH);

        // 2. 中部：戰鬥畫布
        combatCanvas = new CombatCanvas();
        contentPane.add(combatCanvas, BorderLayout.CENTER);

        // 3. 南部：戰報日誌與按鈕
        JPanel southPanel = new JPanel(new BorderLayout(0, 10));
        southPanel.setOpaque(false);

        logArea = new JTextArea(6, 20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        logArea.setOpaque(false);
        logArea.setBackground(new Color(0, 0, 0, 120));
        logArea.setForeground(Color.WHITE);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setEditable(false);
        logArea.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null); 
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnStart = new JButton("開始歷練 (消耗 15 體力)");
        btnStart.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        btnStart.setBackground(new Color(139, 69, 19));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.addActionListener(e -> handleExpedition(btnStart)); 
        southPanel.add(btnStart, BorderLayout.SOUTH);
        
        contentPane.add(southPanel, BorderLayout.SOUTH);
    }
    
    private void handleExpedition(JButton btn) {
        btn.setEnabled(false);
        
        // 1. 向天道(Service)請求歷練結果
        ExpeditionResult res = expeditionService.startExpedition(hero);
        
        if (res != null && res.getEvent() != null) {
            // 💡 若遇首領，戰場散發橘光
            Color glow = "首領".equals(res.getEvent().getEvent_type()) ? Color.ORANGE : null;
            
            // 2. 啟動水墨對撞動畫 (傳入頭像路徑與回調邏輯)
            combatCanvas.startAnim(
                hero.getAvatar_path(),
                res.getEvent().getEvent_image(),
                glow, 
                () -> {
                    // 🚀 1. 顯示 Service 傳回的戰鬥文字（由 Service 決定勝負文字）
                    logArea.append(res.getMessage() + "\n");
                    
                    // 2. 更新體力條
                    staminaBar.setValue(hero.getStamina());
                    staminaBar.setString(hero.getStamina() + " / 100");

                    // 🚀 3. 【核心修正】直接從 Service 傳回的 res 拿數據
                    // 不要在這裡寫 Math.random()，也不要在此處生成掉落物！
                    int expGain = res.getExpGain(); 
                    
                    // 從 Service 傳回的 List 中抓取第一件寶物
                    model.Items droppedItem = (res.getLoot() != null && !res.getLoot().isEmpty()) 
                                              ? res.getLoot().get(0) : null;

                    // 🚀 4. 更新本地 hero 物件的修為（讓大廳同步）
                    hero.setExp(hero.getExp() + expGain);

                    // 🚀 5. 啟動結算視窗
                    // 只要 res.isSuccess() 是 true，且 droppedItem 有值，Loot_UI 就會顯示大捷
                    new Loot_UI(this, res.isSuccess(), droppedItem, expGain).setVisible(true);

                    btn.setEnabled(true);
                }
            );
        } else {
             // 💡 異常處理：如體力不足或資料庫斷線
             String reason = (res != null) ? res.getMessage() : "天道連結異常";
             logArea.append("⚠️ 歷練中斷：" + reason + "\n");
             staminaBar.setValue(hero.getStamina());
             btn.setEnabled(true);
        }
    }
    private void initStaminaTimer() {
        new javax.swing.Timer(60000, e -> {
            hero = staminaService.recoverStamina(hero);
            staminaBar.setValue(hero.getStamina());
            staminaBar.setString(hero.getStamina() + " / 100");
        }).start();
    }
   
    
}