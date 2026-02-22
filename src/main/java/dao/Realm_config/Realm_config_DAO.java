package dao.Realm_config;

import java.util.List;
import java.util.Map;
import model.RealmConfig;

public interface Realm_config_DAO {
    // 取得所有境界配置
    List<RealmConfig> getAllConfigs();
    
    // 根據等級取得特定境界資訊
    RealmConfig getByLevel(int level);

    /**
     * 新增：將所有境界轉為 Map 供遊戲邏輯快速比對
     * Key: realm_level (境界等級), Value: RealmConfig (物件)
     */
    Map<Integer, RealmConfig> getAllConfigsMap();
}