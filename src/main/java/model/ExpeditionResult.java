package model;

import java.util.List;

public class ExpeditionResult {
	// 顯式型別宣告，封裝戰鬥結果
    private boolean isSuccess;     // 是否戰鬥勝利
    private String message;        // 播報文字 (如：你擊敗了山精)
    private GameEvents event;      // 觸發的事件物件 (內含圖片路徑)
    private int rewardExp;         // 獲得經驗
    private List<Items> loot;      // 掉落物品清單 (從 Player_items_DAO 獲取)
    private int expGain;
    // 建構子與 Getter/Setter...
    public ExpeditionResult(boolean isSuccess, String message, GameEvents event) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.event = event;
    }

    // 靜態工廠方法，讓 Service 呼叫更優雅
    public static ExpeditionResult success(GameEvents event, String msg) {
        return new ExpeditionResult(true, msg, event);
    }

    public static ExpeditionResult failure(String msg, GameEvents event) {
        return new ExpeditionResult(false, msg, event);
    }
    public static ExpeditionResult failure(String msg) {
        return new ExpeditionResult(false, msg, null);
    }
    
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public int getExpGain() { return expGain; }
    public void setExpGain(int expGain) { this.expGain = expGain; }
	public GameEvents getEvent() {
		return event;
	}

	public void setEvent(GameEvents event) {
		this.event = event;
	}

	public int getRewardExp() {
		return rewardExp;
	}

	public void setRewardExp(int rewardExp) {
		this.rewardExp = rewardExp;
	}

	public List<Items> getLoot() {
		return loot;
	}

	public void setLoot(List<Items> loot) {
		this.loot = loot;
	}
    
}
