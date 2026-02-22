package service.Characters;

import java.util.List;

import model.Characters;

public interface CharactersService {
    // 💡 處理修煉獲得經驗 (包含資料庫存檔)
    void processMeditation(Characters hero, int gainExp);
    
    // 💡 執行突破 (雷劫)
    boolean breakthrough(Characters hero);
    
    // 💡 根據您的公式計算目前「真實戰力」
    public int calculateFinalAtk(Characters hero);
    
    public int calculateFinalDef(Characters hero);
    
    public int calculateFinalHP(Characters hero);
    
    List<model.Characters> findAllOrderByExp();
    
    long getRequiredExp(int currentRealm);
}
