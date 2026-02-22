package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import model.Users;
import service.user.UserService;
import service.user.UserServiceImpl;
import util.MusicPlayer;

public class Register_UI extends JFrame {

    // 1. 成員變數 (Model & Service)
    private UserService userService = new UserServiceImpl();
    private String selectedJob = "體修";

    // 2. UI 組件
    private JPanel contentPane;
    private JTextField txtAcc, txtPwd, txtName, txtPhone, txtEmail, txtCharName;
    private JLabel lblJobPreview, lblStatus;
    private JTextArea txtJobDesc;
    private JButton btnBody, btnSoul, btnSubmit;

    public Register_UI() {
        initWindowConfig();  // 初始化視窗與背景
        initPanels();        // 初始化左右面板與組件
        setupEventListeners(); // 綁定所有事件
        MusicPlayer.stopBGM();
        MusicPlayer.playBGM("Background.mp3");
    }

    /**
     * 💡 視窗基礎設定
     */
    private void initWindowConfig() {
        setTitle("九霄尋道 - 踏入仙途");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1376, 768);
        setResizable(false);
        setLocationRelativeTo(null); // 置中顯示

        // 背景設定
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = util.ImageLoader.getIcon("bg", "background_piture4.jpg", 1376, 768);
                if (bg != null) g.drawImage(bg.getImage(), 0, 0, this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    /**
     * 💡 佈局所有 UI 面板 (MVC 中的 View 佈局)
     */
    private void initPanels() {
        // --- 左側：帳號資料區 ---
        JPanel leftPanel = createTransparentPanel("--- 天道留名 (帳號註冊) ---");
        leftPanel.setBounds(30, 80, 400, 550);
        contentPane.add(leftPanel);

        addFormField(leftPanel, "帳號名：", txtAcc = new JTextField(), 80);
        addFormField(leftPanel, "修行密鑰：", txtPwd = new JPasswordField(), 150);
        addFormField(leftPanel, "真實姓名：", txtName = new JTextField(), 220);
        addFormField(leftPanel, "千里傳音：", txtPhone = new JTextField(), 290);
        addFormField(leftPanel, "飛鴿傳書：", txtEmail = new JTextField(), 360);

        lblStatus = new JLabel("--- 等待靈根感應 ---");
        lblStatus.setBounds(40, 450, 320, 30);
        lblStatus.setForeground(new Color(150, 150, 150));
        lblStatus.setFont(new Font("Microsoft JhengHei", Font.ITALIC, 16));
        leftPanel.add(lblStatus);

        // --- 右側：角色創造區 ---
        JPanel rightPanel = createTransparentPanel("--- 靈根覺醒 (角色創造) ---");
        rightPanel.setBounds(460, 80, 880, 550);
        contentPane.add(rightPanel);

        // 職業描述 (左側文字區) - 設為不透明封鎖背景
        txtJobDesc = new JTextArea("請感應靈根...\n選擇您的修煉之路。");
        txtJobDesc.setBounds(40, 50, 320, 200);
        txtJobDesc.setOpaque(true); 
        txtJobDesc.setBackground(new Color(15, 20, 25)); // 實色封鎖圖片
        txtJobDesc.setEditable(false);
        txtJobDesc.setLineWrap(true);
        txtJobDesc.setForeground(new Color(200, 200, 200));
        txtJobDesc.setFont(new Font("Microsoft JhengHei", Font.ITALIC, 18));
        rightPanel.add(txtJobDesc);

        // 職業選擇按鈕
        btnBody = new JButton("煉體之士 (體修)");
        btnSoul = new JButton("聚靈之輩 (法修)");
        btnBody.setBounds(40, 430, 160, 40);
        btnSoul.setBounds(220, 430, 160, 40);
        rightPanel.add(btnBody);
        rightPanel.add(btnSoul);

        // 巨大立繪預覽 (右側圖片區)
        lblJobPreview = new JLabel();
        lblJobPreview.setBounds(420, 30, 450, 500);
        updatePreview("man_role_piture2.jpg"); // 預設顯示體修
        rightPanel.add(lblJobPreview);

        // 道號輸入
        JLabel lblChar = new JLabel("賦予道號：");
        lblChar.setForeground(Color.WHITE);
        lblChar.setBounds(40, 350, 100, 30);
        rightPanel.add(lblChar);
        
        txtCharName = new JTextField();
        txtCharName.setBounds(140, 350, 240, 40);
        txtCharName.setFont(new Font("Microsoft JhengHei", Font.BOLD, 20));
        rightPanel.add(txtCharName);

        // --- 底部：正式踏入按鈕 ---
        btnSubmit = new JButton("啟動靈根 ‧ 正式踏入仙途");
        btnSubmit.setFont(new Font("Microsoft JhengHei", Font.BOLD, 26));
        btnSubmit.setBounds(488, 650, 400, 60);
        btnSubmit.setForeground(new Color(200, 200, 200));
        btnSubmit.setContentAreaFilled(false);
        btnSubmit.setBorder(new LineBorder(new Color(184, 134, 11), 2));
        btnSubmit.setFocusPainted(false);
        contentPane.add(btnSubmit);
    }

    /**
     * 💡 事件監聽綁定 (MVC 中的 Controller 橋接)
     */
    private void setupEventListeners() {
        // 職業切換
        btnBody.addActionListener(e -> {
            selectedJob = "體修";
            updatePreview("man_role_piture2.jpg");
            txtJobDesc.setText("【體修】\n以肉身破萬法。\n強大的體魄讓您在歷練中\n擁有極高的生存能力。");
        });

        btnSoul.addActionListener(e -> {
            selectedJob = "法修";
            updatePreview("mowan_role_piture2.jpg"); // 保持您的 mowan 拼法
            txtJobDesc.setText("【法修】\n御劍乘風，聚氣成刃。\n追求極致的輸出實力，\n瞬間爆發力驚人。");
        });

        // 註冊按鈕
        btnSubmit.addActionListener(e -> handleFullRegister());

        // 按鈕靈氣感應 (Hover)
        btnSubmit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnSubmit.setForeground(Color.WHITE);
                btnSubmit.setBackground(new Color(139, 0, 0));
                btnSubmit.setContentAreaFilled(true);
                btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                btnSubmit.setForeground(new Color(200, 200, 200));
                btnSubmit.setContentAreaFilled(false);
            }
        });
    }

    private void handleFullRegister() {
        Users u = new Users();
        u.setAccount(txtAcc.getText().trim());
        u.setPassword(new String(((JPasswordField)txtPwd).getPassword()));
        u.setName(txtName.getText().trim());
        u.setPhone(txtPhone.getText().trim());
        u.setEmail(txtEmail.getText().trim());

        String charName = txtCharName.getText().trim();
        String result = userService.registerWithCharacter(u, charName, selectedJob);

        if (result != null && result.contains("成功")) {
            lblStatus.setText("<html><font color='#00FF00'>✨ " + result + "</font></html>");
            Timer timer = new Timer(1500, e -> this.dispose());
            timer.setRepeats(false);
            timer.start();
        } else {
            lblStatus.setText("<html><font color='#FF4500'>⚠️ " + (result != null ? result : "連線中斷") + "</font></html>");
        }
    }

    private void updatePreview(String imgName) {
        ImageIcon icon = util.ImageLoader.getIcon("avatars", imgName, 450, 500);
        lblJobPreview.setIcon(icon);
    }

    private JPanel createTransparentPanel(String title) {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(15, 20, 25, 200));
        p.setBorder(new TitledBorder(new LineBorder(new Color(184, 134, 11)), title, 
                    TitledBorder.LEADING, TitledBorder.TOP, 
                    new Font("Microsoft JhengHei", Font.BOLD, 18), new Color(184, 134, 11)));
        return p;
    }

    private void addFormField(JPanel p, String label, JTextField field, int y) {
        JLabel l = new JLabel(label);
        l.setForeground(Color.WHITE);
        l.setBounds(40, y, 120, 30);
        field.setBounds(160, y, 200, 30);
        p.add(l);
        p.add(field);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Register_UI frame = new Register_UI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}