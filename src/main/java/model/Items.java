package model;

public class Items {
	private int item_id;
    private String item_name;
    private String item_type;    // 裝備/消耗品
    private int atk_bonus;
    private int def_bonus;
    private int min_realm;       // 最低裝備境界限制
    private String description;
    private String image_path;

    public Items() { super(); }

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_type() {
		return item_type;
	}

	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}

	public int getAtk_bonus() {
		return atk_bonus;
	}

	public void setAtk_bonus(int atk_bonus) {
		this.atk_bonus = atk_bonus;
	}

	public int getDef_bonus() {
		return def_bonus;
	}

	public void setDef_bonus(int def_bonus) {
		this.def_bonus = def_bonus;
	}

	public int getMin_realm() {
		return min_realm;
	}

	public void setMin_realm(int min_realm) {
		this.min_realm = min_realm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
