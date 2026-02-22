package dao.Characters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Characters;
import util.Tool;

public class Characters_DAO_impl implements Characters_DAO{
	
	
	//此方法是內部類別，提供給select by id or select all 使用，這樣底下的方法不用寫2遍。
	private Characters mapRow(ResultSet rs) throws SQLException{
		Characters c = new Characters();
	    c.setCharacters_id(rs.getInt("characters_id"));
	    c.setUsers_id(rs.getInt("users_id"));
	    c.setCharacters_name(rs.getString("characters_name"));
	    c.setJob_type(rs.getString("job_type"));
	    c.setRealm(rs.getInt("realm"));
	    c.setExp(rs.getLong("exp"));
	    c.setSpirit_stones(rs.getInt("spirit_stones"));
	    c.setHealth(rs.getInt("health"));
	    c.setStamina(rs.getInt("stamina"));
	    c.setKill_points(rs.getDouble("kill_points"));
	    c.setIs_cultivating(rs.getInt("is_cultivating"));
	    c.setAvatar_path(rs.getString("avatar_path"));
	    c.setBase_atk(rs.getInt("base_atk"));
	    c.setBase_def(rs.getInt("base_def"));
	    c.setWeapon_id(rs.getInt("weapon_id"));
	    c.setArmor_id(rs.getInt("armor_id"));

	    // --- 時間轉換邏輯 ---
	    // 從 ResultSet 取得 Timestamp (SQL 型別)
	    Timestamp ts = rs.getTimestamp("last_save_time");
	    if (ts != null) {
	        // 使用 toLocalDateTime() 直接轉換為 Java 8 LocalDateTime
	        c.setLast_save_time(ts.toLocalDateTime());
	    }
	    // ------------------

	    return c; // 確保回傳 c 而非 null
		
	}

	public static void main(String[] args) {
		
		
	}
	//提供連線
	private Connection conn=Tool.getConn();
	
	//此方法提供根據帳號ID來抓取關於角色的所有資料。
	@Override
	public Characters findByUserId(int users_id) {
	    String sql = "SELECT * FROM characters WHERE users_id = ?";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, users_id);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // 🚀 直接調用您剛才修正過（含 weapon_id）的 mapRow
	            return mapRow(rs); 
	        }
	    } catch (SQLException e) { e.printStackTrace(); }
	    return null;
	}

	//此方法透過將最新資料放入character變數，並呼叫此方法將資料庫的角色資料進行更新。
	//
	@Override
	public void update(Characters character) {
	    // 🚀 1. SQL 語句必須包含 weapon_id, armor_id
	    String sql = "UPDATE characters SET characters_name=?, realm=?, exp=?, spirit_stones=?, health=?, " +
	                 "stamina=?, kill_points=?, is_cultivating=?, base_atk=?, base_def=?, " +
	                 "weapon_id=?, armor_id=?, last_save_time=NOW() " + // 💡 新增這兩格
	                 "WHERE characters_id=?"; 
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, character.getCharacters_name());
	        pstmt.setInt(2, character.getRealm());
	        pstmt.setLong(3, character.getExp());
	        pstmt.setInt(4, character.getSpirit_stones());
	        pstmt.setInt(5, character.getHealth());
	        pstmt.setInt(6, character.getStamina());
	        pstmt.setDouble(7, character.getKill_points());
	        pstmt.setInt(8, character.getIs_cultivating());
	        pstmt.setInt(9, character.getBase_atk());
	        pstmt.setInt(10, character.getBase_def());
	        
	        // 🚀 2. 注入裝備 ID
	        pstmt.setInt(11, character.getWeapon_id());
	        pstmt.setInt(12, character.getArmor_id());
	        
	        pstmt.setInt(13, character.getCharacters_id()); // 💡 原本的第 11 位變成 13
	        
	        pstmt.executeUpdate();
	    } catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void add(Characters character) {
		String sql = "INSERT INTO characters (users_id, characters_name, job_type, realm, exp, " +
                "spirit_stones, health, stamina, kill_points, is_cultivating, avatar_path, " +
                "base_atk, base_def, weapon_id, armor_id) " + // 💡 增加欄位
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // 💡 15 個問號
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, character.getUsers_id());
	        pstmt.setString(2, character.getCharacters_name());
	        pstmt.setString(3, character.getJob_type());
	        pstmt.setInt(4, character.getRealm());
	        pstmt.setLong(5, character.getExp());
	        pstmt.setInt(6, character.getSpirit_stones());
	        pstmt.setInt(7, character.getHealth());
	        pstmt.setInt(8, character.getStamina());
	        pstmt.setDouble(9, character.getKill_points());
	        pstmt.setInt(10, character.getIs_cultivating());
	        pstmt.setString(11, character.getAvatar_path());
	        pstmt.setInt(12, character.getBase_atk());
	        pstmt.setInt(13, character.getBase_def());
	        pstmt.setInt(14, 0); // 預設武器 ID: 0
	        pstmt.setInt(15, 0); // 預設護甲 ID: 0
	        
	        pstmt.executeUpdate();
	    } catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public List<Characters> findAllOrderByExp() {
	    List<Characters> list = new ArrayList<Characters>(); // 1. 準備籃子
	    String sql = "SELECT * FROM characters ORDER BY exp DESC LIMIT 50";
	    
	    try (PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {
	        
	        // 2. 使用 while 迴圈跑遍所有 50 筆資料
	        while (rs.next()) { 
	            // 3. 呼叫 mapRow 轉換後，存入 list 籃子裡
	            list.add(mapRow(rs)); 
	        }
	        
	    } catch (SQLException e) { 
	        e.printStackTrace(); 
	    }
	    
	    // 4. 最後回傳整份清單 (如果沒資料，則是空的 list 而非 null)
	    return list; 
	}

}
