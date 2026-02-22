package dao.Player_items;

import java.util.List;
import model.Player_items;

public interface Player_items_DAO {
	// 取得該角色的所有背包物品
    List<Player_items> findByPlayerId(int player_id);
    
    // 增加物品數量（或是獲得新物品）
    void addOrUpdateItem(int player_id, int item_id, int amount);
    
    // 消耗物品 (扣數量，數量為 0 時仍保留格子或邏輯處理)
    void useItem(int player_id, int item_id, int amount);
    
    // 💡 新增：刪除該玩家背包中的特定道具
    void deleteItem(int player_id, int item_id);
    
   
}
