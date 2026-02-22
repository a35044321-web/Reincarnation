package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Tool {

    public static void main(String[] args) {
        
    }

    public static Connection getConn() {
        // 建議補上參數，增加穩定性；並修正資料庫名稱為 reincarnation
        String url = "jdbc:mysql://localhost:3306/reincarnation?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "1234";
        
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ 資料庫連線發生錯誤：");
            e.printStackTrace();
        }
        return conn;
    }
}