package service.user;
import dao.Users.Users_DAO;
import dao.Users.Users_DAO_impl;
import model.Users;
public class UserServiceImpl implements UserService{
	private Users_DAO userDao = new Users_DAO_impl();
	private dao.Characters.Characters_DAO charDao = new dao.Characters.Characters_DAO_impl();
	//登入功能。
	@Override
	public Users login(String account, String password) {
		return userDao.login(account, password);
	}
	//註冊功能。
	public String registerWithCharacter(Users user, String charName, String job) {
	    // 1. 檢查帳號是否存在
	    if (userDao.isAccountExists(user.getAccount())) return "此帳號已被天道佔用";

	    // 2. 寫入 Users 表
	    boolean userSuccess = userDao.register(user);
	    
	    if (userSuccess) {
	        // 3. 抓回剛產生的 users_id (為了連動 Characters 表)
	        Users newUser = userDao.login(user.getAccount(), user.getPassword());
	        
	        // 4. 建立初始角色
	        model.Characters hero = new model.Characters();
	        hero.setUsers_id(newUser.getUsers_id());
	        hero.setCharacters_name(charName);
	        hero.setJob_type(job); // 體修 或 法修
	        hero.setRealm(1);      // 煉氣期
	        hero.setStamina(100);
	        
	        // 根據職業給予不同初始路徑
	        if ("法修".equals(job)) {
	            hero.setAvatar_path("mowan_role_piture1.jpg");
	            hero.setBase_atk(60); // 法修攻擊稍高
	        } else {
	            hero.setAvatar_path("man_role_piture1.jpg");
	            hero.setBase_atk(50); // 體修防禦(未來可加)或血量較高
	        }
	        
	        charDao.add(hero); // 寫入 Characters 表
	        return "註冊成功，歡迎踏入仙途！";
	    }
	    return "天道異常，註冊失敗";
	}
	
	//查詢功能。
	@Override
	public boolean isAccountExists(String account) {
		return userDao.isAccountExists(account);
	}

}
