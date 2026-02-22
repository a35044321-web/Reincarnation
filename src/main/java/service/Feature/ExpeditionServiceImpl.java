package service.Feature;

import dao.Characters.Characters_DAO_impl;
import dao.Game_event.Game_events_DAO_impl;
import dao.Items.Items_DAO;
import dao.Items.Items_DAO_Impl;
import dao.Player_items.Player_items_DAO;
import dao.Player_items.Player_items_DAO_impl;
import dao.Realm_config.Realm_config_DAO;
import dao.Realm_config.Realm_config_DAO_impl;
import model.Characters;
import model.ExpeditionResult;
import model.GameEvents;
import model.Items;
import model.RealmConfig;
import service.Item.ItemService;
import service.Item.ItemServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.Characters.Characters_DAO;
import dao.Game_event.Game_events_DAO;


public class ExpeditionServiceImpl implements ExpeditionService{
	
	 private Characters_DAO charDao = new Characters_DAO_impl();
	 private Game_events_DAO eventDao = new Game_events_DAO_impl();
	 private Player_items_DAO inventoryDao= new Player_items_DAO_impl();
	 private Realm_config_DAO realmDao=new Realm_config_DAO_impl();
	 private Items_DAO itemsDao = new Items_DAO_Impl();
	 private ItemService itemService= new ItemServiceImpl();
	 private service.Characters.CharactersService charService = new service.Characters.CharactersServiceImpl();
	
	//此私有方法提供給這張class使用，用來根據玩家數值與怪物數值來做判斷戰鬥成功機率。
	 private boolean calculateCombat(Characters character, GameEvents event) {
		    // 🚀 1. 直接調用「三位一體」Service 算出最終實時數值
		    int finalAtk = charService.calculateFinalAtk(character);
		    int finalDef = charService.calculateFinalDef(character);
		    int finalHP  = charService.calculateFinalHP(character);

		    // 🚀 2. 玩家綜合鬥法值 (權重分配：生命 30%, 攻擊 50%, 防禦 20%)
		    double playerPower = (finalHP * 0.3) + (finalAtk * 0.5) + (finalDef * 0.2);
		    
		    // 🚀 3. 怪物戰力 (同樣套用境界加成公式)
		    // 公式：基礎值 * (1 + (怪境界-1) * 0.3)
		    double monsterRealmMultiplier = 1.0 + (event.getMin_realm() - 1) * 0.3;
		    double monsterPower = event.getEffect_value() * monsterRealmMultiplier;

		    // 🚀 4. 隨機亂數因子 (0.8 ~ 1.2 波動)
		    double randomFactor = 0.8 + (Math.random() * 0.4);
		    
		    // 5. 判斷勝負：玩家最終戰力是否大於怪物
		    return (playerPower * randomFactor) >= monsterPower;
		}
	
	 @Override
	 public ExpeditionResult startExpedition(Characters character) {
	     // (1) 體力檢查 (消耗 15)
	     if (character.getStamina() < 15) {
	         return ExpeditionResult.failure("體力不足，請先打坐休息。");
	     }

	     // (2) 抽取事件
	     GameEvents event = eventDao.findRandomEventByRealm(character.getRealm());
	     if (event == null) return ExpeditionResult.failure("此地靈氣稀薄，無事發生。");

	     // (3) 戰鬥演算
	     boolean isVictory = calculateCombat(character, event);

	     // (4) 扣除體力 (無論勝敗)
	     character.setStamina(character.getStamina() - 15);
	     
	     if (isVictory) {
	         // 🏆 勝利：獲得經驗 (由 Event 的 EffectValue 決定基礎)
	         int gainExp = event.getEffect_value();
	         character.setExp(character.getExp() + gainExp);
	         
	         // 🚀 調用 ItemService 產生掉落
	         List<Items> loots = itemService.generateExpeditionLoot(event.getEvent_type(), character.getRealm());
	         
	         // 落地存檔
	         charDao.update(character); 
	         
	         String victoryMsg = "【勝利】" + event.getEvents_name() + "\n   ➔ " + event.getDescription();
	         ExpeditionResult result = ExpeditionResult.success(event, victoryMsg);
	         result.setLoot(loots); 
	         return result;
	     } else {
	         // 💀 戰敗：體力已扣，不給經驗或掉落 (依您需求可給 1/3 經驗)
	         charDao.update(character);
	         return ExpeditionResult.failure("【戰敗】你被 " + event.getEvents_name() + " 震懾，負傷而逃。", event);
	     }
	 }

	@Override
	public boolean isReadyToBreakthrough(Characters character) {
		
		RealmConfig config = realmDao.getByLevel(character.getRealm());
		if (config == null) {
	        System.err.println("警告：找不到境界等級 " + character.getRealm() + " 的配置！");
	        return false;
	    }
		return character.getExp() >= config.getExp_required();
	}

}
