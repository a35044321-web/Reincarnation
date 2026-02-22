package view;

import java.awt.*;
import javax.swing.*;
import model.Characters;
import util.UIHelper;
import util.ImageLoader;
import util.RealmHelper;
import service.Characters.CharactersService;
import service.Characters.CharactersServiceImpl;

public class Status_UI extends JFrame {
    private Characters hero;
    private JPanel contentPane;
    private JProgressBar expBar;
    private JLabel lblAtk, lblDef, lblHP, lblRealm, lblStamina, lblSuccessRate;
    private JButton btnBreakthrough;
    private CharactersService charService = new CharactersServiceImpl();

    public Status_UI(Characters hero) {
        this.hero = hero;
        initWindow();      
        initBackground();  
        initStatusPanel(); 
        initHeroDisplay(); 
        initActionArea();  
        startSyncTimer();  
       
    }

    private void initWindow() {
        setTitle("道體視察 - " + hero.getCharacters_name());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(150, 150, 1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initBackground() {
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = ImageLoader.getIcon("bg", "background_piture2.jpg", 1000, 700);
                if (bg != null) g.drawImage(bg.getImage(), 0, 0, this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void initStatusPanel() {
        // 💡 1. 外部透明框架
        JPanel infoPanel = UIHelper.createTransparentPanel("--- 仙道根基 ---");
        infoPanel.setBounds(50, 50, 420, 480);
        contentPane.add(infoPanel);

        // 💡 2. 【核心修正】內部實色墨黑畫板，徹底遮蓋背景圖
        JPanel statusCanvas = new JPanel(null);
        statusCanvas.setBounds(15, 40, 390, 420);
        statusCanvas.setBackground(new Color(15, 20, 25)); // 🌑 實色墨黑
        statusCanvas.setOpaque(true);
        infoPanel.add(statusCanvas);

        // 🚀 3. 所有標籤改加在 statusCanvas 上
        lblRealm = createStatusLabel("✨ 當前境界： " + RealmHelper.getRealmName(hero.getRealm()), 30, statusCanvas);
        lblAtk = createStatusLabel("⚔️ 肉身強度(攻擊)： " + charService.calculateFinalAtk(hero), 80, statusCanvas);
        lblDef = createStatusLabel("🛡️ 護體真氣(防禦)： " + charService.calculateFinalDef(hero), 130, statusCanvas);
        lblHP = createStatusLabel("❤️ 仙道生命(血量)： " + charService.calculateFinalHP(hero), 180, statusCanvas);
        lblStamina = createStatusLabel("🔋 氣血神完(體力)： " + hero.getStamina(), 230, statusCanvas);
        
        lblSuccessRate = createStatusLabel("⚡ 突破成功率：計算中...", 280, statusCanvas);
        lblSuccessRate.setForeground(new Color(255, 69, 0));

        // 經驗條
        JLabel lblExp = new JLabel("📜 修為進度：");
        lblExp.setForeground(Color.WHITE);
        lblExp.setBounds(30, 335, 100, 30);
        statusCanvas.add(lblExp);

        expBar = new JProgressBar(0, 1000); 
        expBar.setStringPainted(true);
        expBar.setForeground(new Color(138, 43, 226)); 
        expBar.setBounds(30, 370, 330, 30);
        statusCanvas.add(expBar);
    }

    private JLabel createStatusLabel(String text, int y, JPanel p) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Microsoft JhengHei", Font.BOLD, 20));
        lbl.setForeground(new Color(184, 134, 11)); 
        lbl.setBounds(30, y, 350, 35);
        p.add(lbl);
        return lbl;
    }

    private void initHeroDisplay() {
        JLabel lblHero = new JLabel();
        lblHero.setBounds(500, 50, 450, 550);
        lblHero.setIcon(ImageLoader.getIcon("avatars", hero.getAvatar_path(), 450, 550));
        contentPane.add(lblHero);
    }

    private void initActionArea() {
        btnBreakthrough = new JButton("尋求突破 (雷劫)");
        btnBreakthrough.setBounds(100, 560, 320, 70);
        btnBreakthrough.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        btnBreakthrough.setForeground(Color.WHITE);
        btnBreakthrough.setFocusPainted(false);
        btnBreakthrough.addActionListener(e -> handleBreakthrough());
        contentPane.add(btnBreakthrough);
    }

    private void startSyncTimer() {
        Timer syncTimer = new Timer(1000, e -> {
            if (hero != null) {
                expBar.setValue((int) hero.getExp());
                expBar.setString(hero.getExp() + " / 1000");
                lblRealm.setText("✨ 當前境界： " + RealmHelper.getRealmName(hero.getRealm()));
                lblAtk.setText("⚔️ 肉身強度(攻擊)： " + charService.calculateFinalAtk(hero));
                lblDef.setText("🛡️ 護體真氣(防禦)： " + charService.calculateFinalDef(hero));
                lblHP.setText("❤️ 仙道生命(血量)： " + charService.calculateFinalHP(hero));
                lblStamina.setText("🔋 氣血神完(體力)： " + hero.getStamina() + " / 100");

                double rate = calculateSuccessRate();
                lblSuccessRate.setText("⚡ 突破成功率估算：" + String.format("%.1f", rate) + "%");
                
                if (hero.getExp() >= 1000) {
                    btnBreakthrough.setBackground(new Color(138, 43, 226));
                    btnBreakthrough.setText("尋求突破 (天劫感應中)");
                } else {
                    btnBreakthrough.setBackground(new Color(75, 0, 130));
                    btnBreakthrough.setText("修為尚淺 (需 1000)");
                }
            }
        });
        syncTimer.start();
    }

    private double calculateSuccessRate() {
        double rate = 20.0 + (charService.calculateFinalAtk(hero) / 100.0) + (hero.getExp() / 500.0);
        return Math.min(99.9, rate);
    }

    private void handleBreakthrough() {
        if (hero.getExp() < 1000) {
            JOptionPane.showMessageDialog(this, "<html><font color='red'>⚠️ 修為尚淺，強行突破必遭雷劈！</font></html>");
            return;
        }

        final int[] countArr = {0}; // 💡 修正變數命名
        final Point originalLoc = getLocation(); 

        Timer thunderTimer = new Timer(50, e -> {
            countArr[0]++;
            if (countArr[0] % 2 == 1) {
                contentPane.setBackground(Color.WHITE); 
                setLocation(originalLoc.x - 15, originalLoc.y + 10);
            } else {
                contentPane.setBackground(null);
                setLocation(originalLoc.x + 15, originalLoc.y - 10);
            }

            if (countArr[0] >= 12) {
                ((Timer)e.getSource()).stop();
                contentPane.setBackground(null);
                setLocation(originalLoc);
                processLevelUp(); 
            }
        });
        thunderTimer.start();
    }

    private void processLevelUp() {
        double rate = calculateSuccessRate();
        double roll = Math.random() * 100;

        if (roll <= rate) {
            hero.setRealm(hero.getRealm() + 1);
            hero.setExp(hero.getExp() - 1000); 
            JOptionPane.showMessageDialog(this, "🎊 【" + RealmHelper.getRealmName(hero.getRealm()) + "】 突破成功！戰力飛躍！");
        } else {
            long penalty = hero.getExp() / 3; 
            hero.setExp(hero.getExp() - penalty);
            JOptionPane.showMessageDialog(this, "💀 雷劫反噬！突破失敗！損耗修為：" + penalty);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Characters mock = new Characters();
                mock.setCharacters_name("測試大能");
                mock.setRealm(1);
                mock.setExp(1200L); 
                mock.setBase_atk(50);
                mock.setBase_def(20);
                mock.setHealth(100);
                mock.setAvatar_path("man_role_piture1.jpg");
                new Status_UI(mock).setVisible(true);
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
}