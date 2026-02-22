package service.Characters;

import java.time.LocalDateTime;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.Characters.Characters_DAO;
import dao.Game_event.Game_events_DAO;

public class StaminaServiceImpl implements StaminaService{
	private Characters_DAO charDao = new Characters_DAO_impl();
	 private Game_events_DAO eventDao = new Game_events_DAO_impl();
	 private Player_items_DAO inventoryDao= new Player_items_DAO_impl();
	 private Realm_config_DAO realmDao=new Realm_config_DAO_impl();
	 private Items_DAO itemsDao = new Items_DAO_Impl();
	public static void main(String[] args) {
		StaminaService staminaService = new StaminaServiceImpl();
	    dao.Characters.Characters_DAO charDao = new dao.Characters.Characters_DAO_impl();

	    // 1. 獲取測試角色 (ID: 1)
	    model.Characters hero = charDao.findByUserId(1);
	    if (hero == null) {
	        System.out.println("❌ 測試失敗：找不到 ID 為 1 的角色。");
	        return;
	    }

	    System.out.println("=== 「九霄尋道」體力系統 Service 測試啟動 ===");
	    System.out.println("初始狀態 -> 角色: " + hero.getCharacters_name() + " | 當前體力: " + hero.getStamina());

	    // 2. 模擬場景：將存檔時間手動設為 20 分鐘前 (模擬離線)
	    System.out.println("\n[情境模擬] 模擬玩家離線 20 分鐘...");
	    hero.setLast_save_time(java.time.LocalDateTime.now().minusMinutes(20));
	    
	    // 執行回復邏輯 (預期回復: 20min * 5 = 100 點)
	    hero = staminaService.recoverStamina(hero);
	    System.out.println("補償後狀態 -> 體力: " + hero.getStamina() + " | 存檔時間已更新至最新");

	    // 3. 測試體力門檻檢查 (hasEnoughStamina)
	    System.out.println("\n[功能測試] 檢查各項活動體力門檻：");
	    
	    int combatCost = 15; // 歷練消耗
	    int bossCost = 50;   // 挑戰首領消耗
	    int overkillCost = 999; // 錯誤數值測試

	    System.out.println(" - 執行一般歷練 (" + combatCost + " 體力): " + 
	        (staminaService.hasEnoughStamina(hero, combatCost) ? "✅ 許可" : "❌ 體力不足"));
	        
	    System.out.println(" - 執行首領挑戰 (" + bossCost + " 體力): " + 
	        (staminaService.hasEnoughStamina(hero, bossCost) ? "✅ 許可" : "❌ 體力不足"));
	        
	    System.out.println(" - 執行禁忌招式 (" + overkillCost + " 體力): " + 
	        (staminaService.hasEnoughStamina(hero, overkillCost) ? "✅ 許可" : "❌ 體力不足"));

	    System.out.println("\n=== 體力系統測試流程結束 ===");

	}

	@Override
	public Characters recoverStamina(Characters hero) {
		//宣告一個最後儲存時間，抓丟入物件的last_save_time
				LocalDateTime lastSave = hero.getLast_save_time();
				//宣告一個now，裡面放呼叫方法，現在的時間。
				LocalDateTime now=LocalDateTime.now();
				//使用duration.between，算出這兩個時間相差多少分鐘。
				long minuntesElapsed = java.time.Duration.between(lastSave, now).toMinutes();
				//將上面算出的相差時間轉成int，並根據我設計的1分鐘回復5點體力，將計算好的時間放入recoverAmount。
				if(minuntesElapsed>0) {
				int recoveryAmount =(int)(minuntesElapsed *5);
				int newStamina=Math.min(100, hero.getStamina()+recoveryAmount);
				//將計算好的體力，藉由set放入變數character。
				hero.setStamina(newStamina);
				//將存檔時間同步為現在，避免重複計算
				hero.setLast_save_time(now);
				//利用呼叫update來更新角色最新的體力數值。
				charDao.update(hero);
				System.out.println("⚡ 靈氣引導完成：自動回復了 " + recoveryAmount + " 點體力。");
				}
				return hero;
	}

	@Override
	public boolean hasEnoughStamina(Characters hero, int amount) {
		return hero.getStamina() >= amount;
	}

}
