package service.Feature;

import model.Characters;
import model.ExpeditionResult;
import model.GameEvents;


public interface ExpeditionService {

    /**
     * 執行歷練核心邏輯
     * @param character 執行歷練的角色
     * @return 歷練結果物件 (包含是否成功、獲得經驗、掉落物、觸發事件)
     */
	public ExpeditionResult startExpedition(Characters character);

    /**
     * 判定是否達到渡劫門檻
     */
    boolean isReadyToBreakthrough(Characters character);
}
