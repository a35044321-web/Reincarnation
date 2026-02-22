package model;

public class RealmConfig {
	private int realm_level;
	private String realm_name;
	private long exp_required;
	private double base_success_rate;   // 新增：基礎成功率
    private double bonus_per_kill_point; // 新增：積分加成係數
    
	public RealmConfig() {
		super();
	}
	
	 public double getBase_success_rate() {
		 return base_success_rate; 
		 }
	 
	 public void setBase_success_rate(double base_success_rate) {
	    	this.base_success_rate = base_success_rate; 
	    	}
	 
	 public double getBonus_per_kill_point() {
	    	return bonus_per_kill_point; 
	    	}
	 
	 public void setBonus_per_kill_point(double bonus_per_kill_point) {
	    	this.bonus_per_kill_point = bonus_per_kill_point; 
	    	}

	public int getRealm_level() {
		return realm_level;
	}

	public void setRealm_level(int realm_level) {
		this.realm_level = realm_level;
	}

	public String getRealm_name() {
		return realm_name;
	}

	public void setRealm_name(String realm_name) {
		this.realm_name = realm_name;
	}

	public long getExp_required() {
		return exp_required;
	}

	public void setExp_required(long exp_required) {
		this.exp_required = exp_required;
	}
	
	
}
