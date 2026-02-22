package service.Item;

import java.util.ArrayList;
import java.util.List;
import model.Characters; // 確保導入的是你自己的 model
import model.Items;
import dao.Characters.Characters_DAO;
import dao.Characters.Characters_DAO_impl;
import dao.Player_items.Player_items_DAO;
import dao.Player_items.Player_items_DAO_impl;
import dao.Items.Items_DAO;
import dao.Items.Items_DAO_Impl;



import model.Items;

public interface ItemService {
	// 執行裝備動作，並更新 Characters 的屬性快取 (Atk/Def)
    void equipItem(Characters hero, Items item);
    // 根據事件階級產生掉落清單
    public List<Items> generateExpeditionLoot(String eventType, int playerRealm);
    
    public List<Items> findAll();
    
    public void addLootToPlayer(int playerId, int itemId, int amount);
    
    public List<Items> findPlayerItems(int playerId);
    
    public void discardItem(int playerId, int itemId);
}
