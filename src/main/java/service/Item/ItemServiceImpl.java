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


public class ItemServiceImpl implements ItemService{
	private Characters_DAO charDao = new Characters_DAO_impl();
    private Player_items_DAO inventoryDao = new Player_items_DAO_impl();
    private Items_DAO itemsDao = new Items_DAO_Impl();
	
	
    @Override
    public void equipItem(Characters hero, Items item) {
    	if ("武器".equals(item.getItem_type())) {
            hero.setWeapon_id(item.getItem_id());
        } else if ("護甲".equals(item.getItem_type())) {
            hero.setArmor_id(item.getItem_id());
        }
        charDao.update(hero);
        System.out.println("⚔️ [天道裝備] 玩家 " + hero.getCharacters_name() + " 已裝備: " + item.getItem_name());
    }
    
    @Override
    public List<Items> generateExpeditionLoot(String eventType, int playerRealm) {
        List<Items> lootList = new ArrayList<>();
        double dropChance = Math.random() * 100;

        // 1. 判定掉落門檻 (維持您的設定：首領 80%, 菁英 60%, 普通 30%)
        boolean isDropped = false;
        if ("首領".equals(eventType)) {
            if (dropChance <= 80.0) isDropped = true;
        } else if ("菁英".equals(eventType)) {
            if (dropChance <= 60.0) isDropped = true;
        } else { 
            if (dropChance <= 30.0) isDropped = true;
        }

        // 2. 執行掉落計算
        if (isDropped) {
            // 🚀 關鍵新增：決定掉落種類 (50% 武器, 50% 護甲)
            boolean isArmor = Math.random() > 0.5;
            
            // 🚀 關鍵調整：根據種類決定 baseId
            // 武器 baseId 依境界為 1, 11, 21...
            // 護甲 baseId 依境界為 10, 20, 30... (對應您 ID 10-13 的設計)
            int baseId = (playerRealm - 1) * 10 + (isArmor ? 10 : 1); 
            
            // 3. 判定品質偏移量 (維持您的 5%/35%/30%/30% 設定)
            double qualityRoll = Math.random() * 100;
            int offset = 0; 
            if (qualityRoll >= 95.0) offset = 3;      // 極品 (ID +3)
            else if (qualityRoll >= 60.0) offset = 2; // 上品 (ID +2)
            else if (qualityRoll >= 30.0) offset = 1; // 中品 (ID +1)
            
            // 4. 計算最終 ID 並抓取
            int finalItemId = baseId + offset;
            Items droppedItem = itemsDao.findById(finalItemId);
            
            if (droppedItem != null) {
                lootList.add(droppedItem);
                System.out.println("🎁 [天道掉落] 類型:" + (isArmor ? "護甲" : "武器") + " | ID:" + finalItemId);
            }
        }
        return lootList;
    }
    
	@Override
	public List<Items> findAll() {
		return itemsDao.findAll();
	}
	
	@Override
	public void addLootToPlayer(int playerId, int itemId, int amount) {
	    // 🚀 直接調用您寫好的 DAO 方法
	    // 這會自動判斷：若玩家沒這件裝備則新增，若已有則數量 +1
	    inventoryDao.addOrUpdateItem(playerId, itemId, amount);
	    
	    System.out.println("✨ [天道系統] 玩家 ID:" + playerId + " 獲得道具 ID:" + itemId + " 數量:" + amount);
	}
	
	@Override
	public List<Items> findPlayerItems(int playerId) {
		 List<Items> playerBag = new ArrayList<>();
		    
		    // 1. 先從關聯表抓出該玩家擁有的所有 ID
		    // 這裡調用您之前寫好的 inventoryDao.findByPlayerId
		    List<model.Player_items> relations = inventoryDao.findByPlayerId(playerId);
		    
		    // 2. 遍歷關聯，透過 ID 去 items 表抓取詳細圖文數據
		    for (model.Player_items pi : relations) {
		        Items detail = itemsDao.findById(pi.getItem_id());
		        if (detail != null) {
		            // 💡 可以在這裡把數量 set 進去 (如果您 Items model 有 quantity 欄位)
		            playerBag.add(detail);
		        }
		    }
		    return playerBag;
	}

	@Override
	public void discardItem(int playerId, int itemId) {
		inventoryDao.useItem(playerId, itemId, 1);
	    
	    System.out.println("🔥 [天道銷毀] 玩家 ID:" + playerId + " 已將道具 ID:" + itemId + " 歸還天地。");
	}

	

}
