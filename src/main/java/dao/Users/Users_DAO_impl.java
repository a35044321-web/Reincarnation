package dao.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Users;
import util.Tool;

public class Users_DAO_impl implements Users_DAO{

	public static void main(String[] args) {
	

	}
	 // 預先取得連線物件，供全類別方法使用
    private Connection conn = Tool.getConn();
    
    
    
    //此為提供註冊的方法。
    @Override
    public boolean register(Users user) {
    	String sql = "INSERT INTO users (account, password, name, phone, email, role, created_at) " +
                "VALUES (?, ?, ?, ?, ?, 'PLAYER', NOW())";
        try (Connection conn = Tool.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getAccount());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getEmail());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    //此為提供登入的方法。
    @Override
    public Users login(String account, String password) {
        String sql = "SELECT * FROM users WHERE account = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users();
                    user.setUsers_id(rs.getInt("users_id"));
                    user.setAccount(rs.getString("account"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role")); 
                    user.setName(rs.getString("name"));
                    user.setPhone(rs.getString("phone"));
                    user.setEmail(rs.getString("email"));
                    
                    user.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean isAccountExists(String account) {
    	//SELECT count(*)：叫資料庫算出數量，而不是抓取整筆資料（這樣效率更高）。
        String sql = "SELECT count(*) FROM users WHERE account = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 

}
