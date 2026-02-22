package dao.Characters;

import java.util.List;

import model.Characters;

public interface Characters_DAO {
	  // 根據 User ID 讀取角色（一個帳號登入後抓取他的角色）
	 Characters findByUserId(int users_id);
	    void update(Characters character);
	    void add(Characters character);
	    // 新增：取得全服排行榜 (前 50 名)
	    List<Characters> findAllOrderByExp();
}
