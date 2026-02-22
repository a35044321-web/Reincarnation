package dao.Player_items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Player_items;
import util.Tool;

public class Player_items_DAO_impl implements Player_items_DAO{

	public static void main(String[] args) {
		
	}
	private Connection conn = Tool.getConn();
	
	@Override
    public List<Player_items> findByPlayerId(int player_id) {
        List<Player_items> list = new ArrayList<Player_items>();
        String sql = "SELECT * FROM player_items WHERE player_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, player_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Player_items pi = new Player_items();
                    pi.setItems_id(rs.getInt("items_id"));
                    pi.setPlayer_id(rs.getInt("player_id"));
                    pi.setItem_id(rs.getInt("item_id"));
                    pi.setQuantity(rs.getInt("quantity"));
                    list.add(pi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void addOrUpdateItem(int player_id, int item_id, int amount) {
        // 核心邏輯：嘗試新增，若已存在則累加數量
        // 注意：這需要您的 player_items 表對 (player_id, item_id) 設定 UNIQUE INDEX
        String sql = "INSERT INTO player_items (player_id, item_id, quantity) " +
                     "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, player_id);
            pstmt.setInt(2, item_id);
            pstmt.setInt(3, amount); // 新增時的初始數量
            pstmt.setInt(4, amount); // 重複時累加的數量
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void useItem(int player_id, int item_id, int amount) {
        // 💡 邏輯：對於「銷毀」功能，直接執行 DELETE 最保險
        // 如果您未來有「丹藥」消耗（數量-1），才需要原本的 UPDATE 邏輯
        String sql = "DELETE FROM player_items WHERE player_id = ? AND item_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, player_id);
            pstmt.setInt(2, item_id);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ [DAO] 資料庫已移除物品 ID:" + item_id);
            }
        } catch (SQLException e) {
            System.err.println("❌ [DAO] 刪除失敗：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItem(int player_id, int item_id) {
        String sql = "DELETE FROM player_items WHERE player_id = ? AND item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, player_id);
            pstmt.setInt(2, item_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
