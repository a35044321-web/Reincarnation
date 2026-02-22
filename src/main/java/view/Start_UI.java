package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Users;
import service.user.UserService;
import service.user.UserServiceImpl;
import util.MusicPlayer;
import util.ImageLoader;

public class Start_UI extends JFrame {

    private JPanel contentPane;
    private JTextField txtAccount;
    private JPasswordField txtPassword;
    private UserService userService = new UserServiceImpl();

    public Start_UI() {
        initWindow();      
        initBackground();  
        initLoginPanel();  
        MusicPlayer.stopBGM();
        MusicPlayer.playBGM("Background.mp3");
    }

    private void initWindow() {
        setTitle("九霄尋道 - 登入玄門");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1376, 768);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initBackground() {
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = ImageLoader.getIcon("bg", "background_piture3.jpg", 1376, 768);
                if (bg != null) g.drawImage(bg.getImage(), 0, 0, this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void initLoginPanel() {
        // 💡 1. 外部透明裝飾框
        JPanel loginWrapper = new JPanel(null);
        loginWrapper.setBackground(new Color(0, 0, 0, 100)); // 極淺透明
        loginWrapper.setBounds(488, 180, 400, 380); 
        loginWrapper.setBorder(new LineBorder(new Color(184, 134, 11, 100), 1));
        contentPane.add(loginWrapper);

        // 💡 2. 【核心修正】實色墨黑畫板，徹底斬斷背景圖浮現
        JPanel darkCanvas = new JPanel(null);
        darkCanvas.setBounds(15, 15, 370, 350);
        darkCanvas.setBackground(new Color(15, 20, 25)); // 🌑 實色墨黑
        darkCanvas.setOpaque(true);
        loginWrapper.add(darkCanvas);

        // 標題
        JLabel lblTitle = new JLabel("--- 仙 途 登 入 ---", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(184, 134, 11)); 
        lblTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 26));
        lblTitle.setBounds(35, 30, 300, 40);
        darkCanvas.add(lblTitle);

        // 3. 輸入欄位對齊 (y 坐標精準分配)
        createInputField(darkCanvas, "道號：", txtAccount = new JTextField(), 100);
        createInputField(darkCanvas, "密鑰：", txtPassword = new JPasswordField(), 160);

        // 4. 登入按鈕 (墨綠質感)
        JButton btnLogin = new JButton("感 應 靈 氣 (登入)");
        btnLogin.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        btnLogin.setBackground(new Color(34, 139, 34));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(60, 230, 250, 45);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new LineBorder(new Color(184, 134, 11), 1));
        btnLogin.addActionListener(e -> handlePlayerLogin());
        darkCanvas.add(btnLogin);
        
        // 5. 註冊按鈕 (淡藍連結感)
        JButton btnRegister = new JButton("初入玄門？點此開拓仙路");
        btnRegister.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        btnRegister.setForeground(new Color(173, 216, 230));
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorder(null);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setBounds(60, 290, 250, 30);
        btnRegister.addActionListener(e -> handleRegistration());
        darkCanvas.add(btnRegister);
    }

    private void createInputField(JPanel panel, String label, JTextField field, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
        lbl.setBounds(45, y, 60, 35);
        
        field.setBounds(110, y, 200, 35);
        field.setBackground(new Color(30, 30, 30));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE); // 游標白色
        field.setBorder(new LineBorder(new Color(100, 100, 100), 1));
        
        panel.add(lbl);
        panel.add(field);
    }
    // --- 事件處理與主程式 (維持原本邏輯) ---
    private void handlePlayerLogin() {
    	 String acc = txtAccount.getText().trim();
    	    String pwd = new String(txtPassword.getPassword());
    	    
    	    model.Users user = userService.login(acc, pwd);
    	    
    	    if (user != null) {
    	        // 🚀 1. 偵錯感應：在控制台印出權限，確認是否有抓到 "ADMIN"
    	        System.out.println("🎭 [身分感應] 帳號: " + user.getAccount() + " | 權限: [" + user.getRole() + "]");

    	        // 🚀 2. 嚴謹比對：使用 .equalsIgnoreCase 且處理可能為 null 的情況
    	        if (user.getRole() != null && user.getRole().trim().equalsIgnoreCase("ADMIN")) {
    	            
    	            // 🔱 A. 天道管理員分支
    	            JOptionPane.showMessageDialog(this, "✨ 天道敕令：管理者回歸，開啟管理聖殿。");
    	            new Admin_UI().setVisible(true);
    	            this.dispose();
    	            
    	        } else {
    	            // 🧘 B. 普通修士分支
    	            dao.Characters.Characters_DAO charDao = new dao.Characters.Characters_DAO_impl();
    	            model.Characters hero = charDao.findByUserId(user.getUsers_id());

    	            if (hero != null) {
    	                util.MusicPlayer.stopBGM(); 
    	                new Character_UI(hero).setVisible(true);
    	                this.dispose();
    	            } else {
    	                JOptionPane.showMessageDialog(this, "⚠️ 尚未感悟靈根，請先創立角色。");
    	            }
    	        }
    	    } else {
    	        JOptionPane.showMessageDialog(this, "❌ 密鑰錯誤或道號不存在。");
    	    }
    }

    private void handleRegistration() {
        new Register_UI().setVisible(true);
    }

    private void handleAdminAuth() {
        String adminPwd = JOptionPane.showInputDialog(this, "請輸入天道管理權限密鑰：");
        if ("企業內部密碼".equals(adminPwd)) { // 💡 建議將此字串改為您的私人密鑰
            // 🚀 開啟管理系統
            new Admin_UI().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "權限不足，天威不可犯！");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Start_UI().setVisible(true);
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
}