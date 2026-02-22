package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class UIHelper {
    // 💡 製作高品質半透明面板
    public static JPanel createTransparentPanel(String title) {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(15, 20, 25, 200));
        p.setBorder(new TitledBorder(new LineBorder(new Color(184, 134, 11)), title, 
                    TitledBorder.LEADING, TitledBorder.TOP, 
                    new Font("Microsoft JhengHei", Font.BOLD, 18), new Color(184, 134, 11)));
        return p;
    }

    // 💡 快速產出帶標籤的輸入框
    public static void addFormField(JPanel p, String labelText, JTextField field, int y) {
        JLabel l = new JLabel(labelText);
        l.setForeground(Color.WHITE);
        l.setBounds(40, y, 120, 30);
        field.setBounds(160, y, 200, 30);
        p.add(l);
        p.add(field);
    }
}