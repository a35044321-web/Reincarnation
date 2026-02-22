package controller;

import model.Users;
import service.user.UserService;
import service.user.UserServiceImpl;

public class RegisterController {
	private UserService userService = new UserServiceImpl();

    // 💡 業務邏輯：驗證資料並呼叫 Service
    public String processRegistration(String acc, String pwd, String name, String phone, String email, String charName, String job) {
        if (acc.isEmpty() || pwd.isEmpty() || charName.isEmpty()) {
            return "⚠️ 天道提示：帳號、密鑰與道號不可為空！";
        }
        
        Users u = new Users();
        u.setAccount(acc);
        u.setPassword(pwd);
        u.setName(name);
        u.setPhone(phone);
        u.setEmail(email);

        return userService.registerWithCharacter(u, charName, job);
    }
}
