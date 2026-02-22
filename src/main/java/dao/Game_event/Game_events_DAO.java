package dao.Game_event;

import java.util.List;

import model.GameEvents;

public interface Game_events_DAO {
	// 核心歷練功能：隨機抓取符合境界的事件
    GameEvents findRandomEventByRealm(int player_realm);
    
    // 根據 ID 查詢 (用於後台修改)
    GameEvents findById(int id);
    
    // 查詢所有 (用於企業管理清單)
    List<GameEvents> findAll();
    
    // 企業後台維護功能
    void add(GameEvents event);
    void update(GameEvents event);
    void delete(int id);
}
