package service.user;
import model.Users;

public interface UserService {
	/**
     * 處理登入驗證
     * @param account 帳號
     * @param password 密碼
     * @return 成功回傳 User 物件，失敗回傳 null
     */
    Users login(String account, String password);

    /**
     * 處理新帳號註冊
     * @param user 包含帳號密碼的物件
     * @return 註冊結果訊息 (如：註冊成功、帳號已存在等)
     */
    public String registerWithCharacter(Users user, String charName, String job);
    
    /**
     * 檢查帳號是否存在
     */
    boolean isAccountExists(String account);
	
}
