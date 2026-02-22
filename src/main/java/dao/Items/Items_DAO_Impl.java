package dao.Items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Items;
import util.Tool;

public class Items_DAO_Impl implements Items_DAO{
	
	
	//此為將我的程式碼的重複寫取的部分弄成一個method，這樣要改的時候只要這邊改就好
	private Items mapRow(ResultSet rs) throws SQLException {
	    Items item = new Items();
	    // 這裡集中處理「從 rs 搬運到 item 物件」的過程
	    item.setItem_id(rs.getInt("item_id"));
	    item.setItem_name(rs.getString("item_name"));
	    item.setItem_type(rs.getString("item_type"));
	    item.setAtk_bonus(rs.getInt("atk_bonus"));
	    item.setDef_bonus(rs.getInt("def_bonus"));
	    item.setMin_realm(rs.getInt("min_realm"));
	    item.setDescription(rs.getString("description"));
	    item.setImage_path(rs.getString("image_path"));
	    
	    return item;
	}

	public static void main(String[] args) {
		
	}
	
	 private Connection conn = Tool.getConn();

	 @Override
	 public Items findById(int item_id) {
	     String sql = "SELECT * FROM items WHERE item_id = ?";
	     try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	         pstmt.setInt(1, item_id);
	         try (ResultSet rs = pstmt.executeQuery()) {
	             if (rs.next()) {
	                 // 直接使用 mapRow，確保 image_path 等欄位完整讀取
	                 return mapRow(rs); 
	             }
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     return null;
	 }

	 @Override
	 public List<Items> findAll() {
		List<Items>list=new ArrayList<>();
		String sql="select * from items order by item_id ASC";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				//此為內部類別在此使用，呼叫內部類別的方法，將資料送入變數LIST
				list.add(mapRow(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	 }

	 @Override
	 public void add(Items item) {
		 String sql = "INSERT INTO items (item_name, item_type, atk_bonus, def_bonus, min_realm, description, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, item.getItem_name());
	            pstmt.setString(2, item.getItem_type());
	            pstmt.setInt(3, item.getAtk_bonus());
	            pstmt.setInt(4, item.getDef_bonus());
	            pstmt.setInt(5, item.getMin_realm());
	            pstmt.setString(6, item.getDescription());
	            pstmt.setString(7, item.getImage_path()); // 新增
	            pstmt.executeUpdate();
	        } catch (SQLException e) { e.printStackTrace(); }
		
	 }

	 @Override
	 public void update(Items item) {
		 String sql = "UPDATE items SET item_name=?, item_type=?, atk_bonus=?, def_bonus=?, min_realm=?, description=?, image_path=? WHERE item_id=?";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, item.getItem_name());
	            pstmt.setString(2, item.getItem_type());
	            pstmt.setInt(3, item.getAtk_bonus());
	            pstmt.setInt(4, item.getDef_bonus());
	            pstmt.setInt(5, item.getMin_realm());
	            pstmt.setString(6, item.getDescription());
	            pstmt.setString(7, item.getImage_path());
	            pstmt.setInt(8, item.getItem_id());
	            pstmt.executeUpdate();
	        } catch (SQLException e) { e.printStackTrace(); }
		
	 }

	 @Override
	 public void delete(int item_id) {
		 String sql = "DELETE FROM items WHERE item_id = ?";
		    
		    // 使用 try-with-resources 自動關閉 PreparedStatement
		    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        pstmt.setInt(1, item_id);
		        
		        int rowsAffected = pstmt.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("✅ 成功刪除道具 ID: " + item_id);
		        } else {
		            System.out.println("⚠️ 找不到道具 ID: " + item_id + "，未進行刪除。");
		        }
		    } catch (SQLException e) {
		        // 若發生外鍵約束錯誤 (例如該道具已在 player_items 中)，此處會噴出異常
		        System.err.println("❌ 刪除失敗：該道具可能已被玩家持有或資料庫連線異常。");
		        e.printStackTrace();
		    }
		
	 }

}
