package view;

import java.awt.*;
import javax.swing.*;
import model.Characters;
import dao.Characters.Characters_DAO_impl;

public class AdminEdit_UI extends JDialog {
    private JTextField txtExp, txtStamina, txtAtk, txtRealm;
    private Characters targetHero;

    public AdminEdit_UI(Frame owner, int charId) {
        super(owner, "天道干預 - 數值重塑", true);
        setSize(400, 500);
        setLayout(new GridLayout(6, 2, 10, 20));
        setLocationRelativeTo(owner);

        // 抓取該角色最新數據
        targetHero = new Characters_DAO_impl().findByUserId(charId); // 這裡需確認您的 DAO 方法

        add(new JLabel("道號：" + targetHero.getCharacters_name()));
        add(new JLabel("ID: " + charId));

        add(new JLabel("調整境界 (Realm):"));
        add(txtRealm = new JTextField(String.valueOf(targetHero.getRealm())));

        add(new JLabel("調整修為 (Exp):"));
        add(txtExp = new JTextField(String.valueOf(targetHero.getExp())));

        add(new JLabel("調整體力 (Stamina):"));
        add(txtStamina = new JTextField(String.valueOf(targetHero.getStamina())));

        add(new JLabel("基礎攻擊 (BaseAtk):"));
        add(txtAtk = new JTextField(String.valueOf(targetHero.getBase_atk())));

        JButton btnSave = new JButton("🔥 施展天道修正");
        btnSave.addActionListener(e -> {
            try {
                targetHero.setRealm(Integer.parseInt(txtRealm.getText()));
                targetHero.setExp(Long.parseLong(txtExp.getText()));
                targetHero.setStamina(Integer.parseInt(txtStamina.getText()));
                targetHero.setBase_atk(Integer.parseInt(txtAtk.getText()));
                
                new Characters_DAO_impl().update(targetHero); // 🚀 存回資料庫
                JOptionPane.showMessageDialog(this, "因果已重塑！");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "格式輸入錯誤，請檢查數值！");
            }
        });

        add(btnSave);
    }
}