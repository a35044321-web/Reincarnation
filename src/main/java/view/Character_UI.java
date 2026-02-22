package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Characters;
import service.Characters.CharactersService;
import service.Characters.CharactersServiceImpl;
import service.Characters.StaminaService;
import util.UIHelper;
import util.ImageLoader;
import util.MusicPlayer;

public class Character_UI extends JFrame {
    private Characters hero;
    private CharactersService charService = new CharactersServiceImpl();
    private JPanel contentPane;
    private JTextArea txtMeditateLog;
    private JLabel lblHeroPreview, lblQuickStatus; // 💡 統一管理
    private Timer meditateTimer;
    private StaminaService staminaservice=new service.Characters.StaminaServiceImpl();

    public Character_UI(Characters hero) {
        this.hero = hero;
        
        // 🚀 1. 剛進大廳，立刻感應「離線期間」恢復了多少靈氣(體力)
        this.hero = staminaservice.recoverStamina(this.hero); 

        initWindow();
        initBackground();
        initQuickStatus(); 
        initHeroStage();   
        initMenuButtons(); 
        startMeditation();
        
        // 🚀 2. 啟動全域體力恢復計時器 (每分鐘跳一次)
        initGlobalStaminaTimer();

        MusicPlayer.stopBGM();
        MusicPlayer.playBGM("Background.mp3");
    }
    
    private void initWindow() {
        setTitle("九霄尋道 - 洞府修行");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 1376, 768);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initBackground() {
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = ImageLoader.getIcon("bg", "background_piture1.jpg", 1376, 768);
                if (bg != null) g.drawImage(bg.getImage(), 0, 0, this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    /**
     * 💡 解決重疊：統一狀態列位置與樣式
     */
    private void initQuickStatus() {
        lblQuickStatus = new JLabel("", SwingConstants.CENTER);
        lblQuickStatus.setBounds(438, 20, 500, 50); // 居中對齊立繪上方
        lblQuickStatus.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        lblQuickStatus.setForeground(new Color(218, 165, 32)); // 亮金色
        contentPane.add(lblQuickStatus);
        updateQuickStatus();
    }
    private void initGlobalStaminaTimer() {
        // 💡 60000 毫秒 = 1 分鐘
        Timer staminaTimer = new Timer(60000, e -> {
            // 🚀 核心：呼叫 Service 算出最新體力並更新資料庫
            this.hero = staminaservice.recoverStamina(this.hero);
            
            // 刷新上方金色狀態列 (確保戰力與體力同步)
            updateQuickStatus(); 
            
            System.out.println("🍀 [天道守護] 體力自動恢復完成，當前：" + hero.getStamina());
        });
        staminaTimer.start();
    }
    public void updateQuickStatus() {
    	 // 🚀 獲取最新加成後的數值
        int finalAtk = charService.calculateFinalAtk(hero);
        int finalDef = charService.calculateFinalDef(hero);
        
     // 💡 同步渲染到標籤 (增加防禦力顯示)
        lblQuickStatus.setText("<html>"
            + "道號：<font color='white'>" + hero.getCharacters_name() + "</font> "
            + " | 境界：<font color='#00FFFF'>" + util.RealmHelper.getRealmName(hero.getRealm()) + "</font> "
            + " | ⚔️戰力：<font color='#FF4500'>" + finalAtk + "</font>"
            + " | 🛡️防禦：<font color='#1E90FF'>" + finalDef + "</font>"
            + "</html>");
    }

    /**
     * 💡 解決日誌殘影：加入實色底盤
     */
    private void initHeroStage() {
        // 1. 【中央立繪】調整座標與尺寸，使其與右側面板對齊 (高度 550)
        lblHeroPreview = new JLabel();
        // 💡 x=420 (置中偏左), y=80 (與右側同高), 寬=450, 高=550
        lblHeroPreview.setBounds(420, 80, 450, 550); 
        lblHeroPreview.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 使用 ImageLoader 載入符合 450x550 比例的立繪
        lblHeroPreview.setIcon(util.ImageLoader.getIcon("avatars", "sitting_piture1.jpg", 450, 550));
        contentPane.add(lblHeroPreview);

        // 2. 【右側日誌】維持原有的 380x550 規格
        JPanel logPanel = UIHelper.createTransparentPanel("--- 冥想感悟 ---");
        logPanel.setBounds(950, 80, 380, 550);
        contentPane.add(logPanel);

        // 實色底盤 (用於徹底遮蓋背景)
        JPanel logCanvas = new JPanel(null);
        logCanvas.setBounds(15, 40, 350, 490);
        logCanvas.setBackground(new Color(15, 20, 25)); // 實色墨黑
        logCanvas.setOpaque(true);
        logPanel.add(logCanvas);

        txtMeditateLog = new JTextArea();
        txtMeditateLog.setEditable(false);
        txtMeditateLog.setOpaque(false);
        txtMeditateLog.setForeground(new Color(200, 200, 200));
        txtMeditateLog.setFont(new Font("Microsoft JhengHei", Font.ITALIC, 17));
        
        JScrollPane scroll = new JScrollPane(txtMeditateLog);
        scroll.setBounds(10, 10, 330, 470);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        logCanvas.add(scroll);
    }

    private void initMenuButtons() {
        // 💡 1. 調整為 5 列，為天道榜留位置
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 0, 30)); 
        menuPanel.setBounds(60, 150, 280, 480); // 稍微拉高一點，讓間距更均勻
        menuPanel.setOpaque(false);
        contentPane.add(menuPanel);

        // 💡 2. 增加「天道榜 (排行)」到按鈕名單
        String[] btnNames = {"乾坤袋 (背包)", "外出行進 (歷練)", "道體視察 (人物)", "天道名錄 (排行)", "隱退山林 (退出)"};
        
        for (String name : btnNames) {
            JButton btn = createMenuButton(name);
            menuPanel.add(btn);
            
            // 🚀 3. 事件綁定
            if(name.contains("背包")) btn.addActionListener(e -> new Items_UI(hero).setVisible(true));
            if(name.contains("歷練")) btn.addActionListener(e -> { 
                new ExpeditionFrame(hero).setVisible(true); 
                this.dispose(); 
            });
            if(name.contains("人物")) btn.addActionListener(e -> new Status_UI(hero).setVisible(true));
            
            // ✨ 新增：啟動天道榜
            if(name.contains("排行")) btn.addActionListener(e -> new Rank_UI(this).setVisible(true));
            
            if(name.contains("退出")) btn.addActionListener(e -> System.exit(0));
        }
    }

    /**
     * 💡 升級按鈕：全黑背景 + 3px 厚金邊
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        btn.setForeground(new Color(218, 165, 32)); // 金色字
        btn.setBackground(Color.BLACK); // 🌑 全黑背景
        btn.setOpaque(true);
        btn.setContentAreaFilled(true); // 💡 確保顏色填滿
        btn.setFocusPainted(false);
        // 💡 3像素厚金邊
        btn.setBorder(new LineBorder(new Color(184, 134, 11), 3)); 
        return btn;
    }

    private void startMeditation() {
        meditateTimer = new Timer(5000, e -> {
            int gainExp = 5 + (int)(Math.random() * 10);
            charService.processMeditation(hero, gainExp); // 更新 DB 與 Exp
            
            updateQuickStatus(); // 🚀 這裡會抓到最新戰力並更新 Label
            
            txtMeditateLog.append("🧘 氣納丹田，修為 +" + gainExp + " (戰力: " + charService.calculateFinalAtk(hero) + ")\n");
            txtMeditateLog.setCaretPosition(txtMeditateLog.getDocument().getLength());
        });
        meditateTimer.start();
    }

    
}