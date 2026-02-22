package dao.Game_event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.GameEvents;
import util.Tool;

public class Game_events_DAO_impl implements Game_events_DAO{
	
	 // 專家技巧：抽取重複邏輯，符合 Java 11 顯式型別
    private GameEvents mapRow(ResultSet rs) throws SQLException {
        GameEvents ev = new GameEvents();
        ev.setGame_events_id(rs.getInt("game_events_id"));
        ev.setEvents_name(rs.getString("events_name"));
        ev.setDescription(rs.getString("description"));
        ev.setEvent_type(rs.getString("event_type"));
        ev.setEffect_value(rs.getInt("effect_value"));
        ev.setMin_realm(rs.getInt("min_realm"));
        ev.setEvent_image(rs.getString("event_image"));
        return ev;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	private Connection conn = Tool.getConn();
	
	//此方法為:當玩家點擊「開始歷練」時，UI 會根據玩家目前的境界（如：築基期），隨機抓取一張奇遇圖片與描述顯示在畫面上。
	@Override
	public GameEvents findRandomEventByRealm(int player_realm) {
	    String sql = "SELECT * FROM game_events WHERE min_realm <= ? ORDER BY RAND() LIMIT 1";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, player_realm);
	        
	        // 💡 抓鬼列印 1：確認 SQL 編譯後的樣子
	        System.out.println("執行 SQL: " + pstmt.toString()); 
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // 💡 抓鬼列印 2：確認有沒有抓到東西
	                System.out.println("✅ 成功抓到事件：" + rs.getString("events_name"));
	                return mapRow(rs);
	            } else {
	                // 💡 抓鬼列印 3：連線成功但表裡沒資料
	                System.out.println("❌ 查無資料！請確認您的 Tool.getConn() 是否連到正確的 Database。");
	                // 檢查一下當前資料庫到底是哪一個
	                DatabaseMetaData meta = conn.getMetaData();
	                System.out.println("當前連線資料庫 URL: " + meta.getURL());
	            }
	        }
	    } catch (SQLException e) { 
	        System.err.println("🔥 SQL 報錯: " + e.getMessage());
	    }
	    return null;
	}
	//特定事件觸發：當玩家觸發了某個「固定劇情」或「天劫」時，我們透過 ID 直接抓取該事件的資料（如：劫雷傷害值、 event_image）。
	@Override
	public GameEvents findById(int id) {
		String sql="select * from game_events where game_events_id=?";
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				return mapRow(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//後台管理介面：如果您之後要寫一個「策劃工具」視窗來查看所有的事件列表，這個方法能一次列出所有遊戲內容。
	@Override
	public List<GameEvents> findAll() {
		 List<GameEvents> list = new ArrayList<GameEvents>(); // 顯式宣告
	        String sql = "SELECT * FROM game_events ORDER BY min_realm ASC";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                list.add(mapRow(rs));
	            }
	        } catch (SQLException e) { e.printStackTrace(); }
	        return list;
	}
	
	/*	遊戲平衡調整：在 UI 測試時，如果您發現某個奇遇太強（Effect_value 太高），
	 * 可以直接透過介面修改並調用 update 即時存入資料庫，不需重寫程式。*/
	@Override
	public void add(GameEvents event) {
		String sql = "INSERT INTO game_events (events_name, description, event_type, effect_value, min_realm, event_image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getEvents_name());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getEvent_type());
            pstmt.setInt(4, event.getEffect_value());
            pstmt.setInt(5, event.getMin_realm());
            pstmt.setString(6, event.getEvent_image());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
		
	}
	
	//內容剔除：移除過時或有 Bug 的活動事件。
	@Override
	public void update(GameEvents event) {
		String sql="UPDATE game_events SET events_name=?, description=?, event_type=?, "
	               + "effect_value=?, min_realm=?, event_image=? WHERE game_events_id=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, event.getEvents_name());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getEvent_type());
            pstmt.setInt(4, event.getEffect_value());
            pstmt.setInt(5, event.getMin_realm());
            pstmt.setString(6, event.getEvent_image());
            pstmt.setInt(7, event.getGame_events_id());
            int rowAffected = pstmt.executeUpdate();
            System.out.println("歷練事件更新成功，受影響行數: " + rowAffected);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void delete(int id) {
		String sql="delete from game_events where game_events_id=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, id);
            int rowAffected = pstmt.executeUpdate();
            System.out.println("歷練事件更新成功，受影響行數: " + rowAffected);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
