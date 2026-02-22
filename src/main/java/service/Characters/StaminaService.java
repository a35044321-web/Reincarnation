package service.Characters;
import model.Characters;
public interface StaminaService {
	// 處理離線補償與自動回復
    Characters recoverStamina(Characters hero);
    
    // 檢查體力是否足夠執行動作 (如歷練需 15)
    boolean hasEnoughStamina(Characters hero, int amount);
}
