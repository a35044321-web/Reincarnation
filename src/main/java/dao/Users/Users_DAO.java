package dao.Users;

import model.Users;

public interface Users_DAO {
	// 註冊：成功回傳 true
    boolean register(Users user);
    
    // 登入：成功回傳 User 物件，失敗回傳 null
    Users login(String account, String password);
    
    // 檢查帳號重複
    boolean isAccountExists(String account);
}
