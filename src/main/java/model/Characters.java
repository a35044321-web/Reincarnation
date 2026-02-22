package model;

import java.time.LocalDateTime;

public class Characters {
    private int characters_id;
    private int users_id;
    private String characters_name;
    private String job_type;       // 新增：法修/體修
    private int realm;
    private long exp;
    private int spirit_stones;
    private int health;
    private int stamina;
    private double kill_points;    // 新增：擊殺難度累加 (DECIMAL -> double)
    private int is_cultivating;    // 新增：0停止, 1自動修煉中 (TINYINT -> int)
    private LocalDateTime last_save_time; // 新增：存檔時間
    private String avatar_path; // 新增：頭像路徑 (如 "warrior.png")
    private int base_atk;
    private int base_def;
    private Integer weapon_id = 0; // 0 代表無穿戴武器
    private Integer armor_id = 0;  // 0 代表無穿戴護甲

    public Characters() { super(); }

    public int getCharacters_id() {
		return characters_id;
	}

	public String getAvatar_path() {
		return avatar_path;
	}

	public void setAvatar_path(String avatar_path) {
		this.avatar_path = avatar_path;
	}

	public void setCharacters_id(int characters_id) {
		this.characters_id = characters_id;
	}

	public int getUsers_id() {
		return users_id;
	}

	public Integer getWeapon_id() {
		return weapon_id;
	}

	public void setWeapon_id(Integer weapon_id) {
		this.weapon_id = weapon_id;
	}

	public Integer getArmor_id() {
		return armor_id;
	}

	public void setArmor_id(Integer armor_id) {
		this.armor_id = armor_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public String getCharacters_name() {
		return characters_name;
	}

	public void setCharacters_name(String characters_name) {
		this.characters_name = characters_name;
	}

	public int getRealm() {
		return realm;
	}

	public void setRealm(int realm) {
		this.realm = realm;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getSpirit_stones() {
		return spirit_stones;
	}

	public void setSpirit_stones(int spirit_stones) {
		this.spirit_stones = spirit_stones;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getBase_atk() {
		return base_atk;
	}

	public void setBase_atk(int base_atk) {
		this.base_atk = base_atk;
	}

	public int getBase_def() {
		return base_def;
	}

	public void setBase_def(int base_def) {
		this.base_def = base_def;
	}

	// 原有 Getter/Setter 保持不變，下方新增屬性的方法
    public String getJob_type() { return job_type; }
    public void setJob_type(String job_type) { this.job_type = job_type; }

    public double getKill_points() { return kill_points; }
    public void setKill_points(double kill_points) { this.kill_points = kill_points; }

    public int getIs_cultivating() { return is_cultivating; }
    public void setIs_cultivating(int is_cultivating) { this.is_cultivating = is_cultivating; }

    public LocalDateTime getLast_save_time() { return last_save_time; }
    public void setLast_save_time(LocalDateTime last_save_time) { this.last_save_time = last_save_time; }

    // 商業邏輯優化：加入擊殺積分對成功率的影響
    public boolean canBreakthrough(long requiredExp) {
        return this.exp >= requiredExp && this.stamina >= 10;
    }

    // [專家建議]：計算最終突破機率的方法
    public double calculateChance(double baseRate, double bonusPerPoint) {
        return Math.min(100.0, baseRate + (this.kill_points * bonusPerPoint));
    }
    public int getFinalAtk() {
        // 1. 每 10 點修為 +1 攻擊
        int expBonus = (int) (this.exp / 10);
        // 2. 每個境界 +30% (假設 1 境界 = 1.0, 2 境界 = 1.3)
        double realmMultiplier = 1 + (this.realm - 1) * 0.3;
        return (int) ((this.base_atk + expBonus) * realmMultiplier);
    }
}