package dao.Realm_config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.RealmConfig;
import util.Tool;

public class Realm_config_DAO_impl implements Realm_config_DAO{
	
	private RealmConfig mapRow(ResultSet rs) throws SQLException {
        RealmConfig rc = new RealmConfig();
        rc.setRealm_level(rs.getInt("realm_level"));
        rc.setRealm_name(rs.getString("realm_name"));
        rc.setExp_required(rs.getLong("exp_required"));
        // 💡 必須讀取渡劫邏輯相關欄位
        rc.setBase_success_rate(rs.getDouble("base_success_rate"));
        rc.setBonus_per_kill_point(rs.getDouble("bonus_per_kill_point"));
        return rc;
    }


	private Connection conn= Tool.getConn();
	//查詢抓出所有資料庫資料
	@Override
    public List<RealmConfig> getAllConfigs() {
        List<RealmConfig> list = new ArrayList<RealmConfig>();
        // 修正 SQL 拼字: from
        String sql = "SELECT * FROM realm_config ORDER BY realm_level ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

	 @Override
	    public RealmConfig getByLevel(int level) {
	        String sql = "SELECT * FROM realm_config WHERE realm_level = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, level);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) return mapRow(rs);
	            }
	        } catch (SQLException e) { e.printStackTrace(); }
	        return null;
	    }
	

	 @Override
	    public Map<Integer, RealmConfig> getAllConfigsMap() {
	        // 先取得清單，再轉換為高效查詢的 Map 結構
	        List<RealmConfig> allConfigs = this.getAllConfigs();
	        
	        // Java 8/11 Stream API: 顯式型別宣告以利理解
	        Map<Integer, RealmConfig> configMap = allConfigs.stream()
	                .collect(Collectors.toMap(
	                    RealmConfig::getRealm_level, // Key
	                    config -> config             // Value
	                ));
	        return configMap;
	}
	
}
