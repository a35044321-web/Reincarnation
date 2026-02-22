package dao.Items;

import java.util.List;

import model.Items;

public interface Items_DAO {
	
	public Items findById(int item_id); 
	
	public List<Items> findAll();
	
	 // 專為企業後台新增的方法
    void add(Items item);
    void update(Items item);
    void delete(int item_id);
}
