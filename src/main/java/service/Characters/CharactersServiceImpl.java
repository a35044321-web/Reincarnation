package service.Characters;

import model.Characters;

import java.util.List;

import dao.Characters.Characters_DAO;
import dao.Characters.Characters_DAO_impl;
import dao.Realm_config.Realm_config_DAO;
import dao.Realm_config.Realm_config_DAO_impl;

public class CharactersServiceImpl implements CharactersService {
    private Characters_DAO charDao = new Characters_DAO_impl();
    private Realm_config_DAO realmDao=new Realm_config_DAO_impl();
    @Override
    public void processMeditation(Characters hero, int gainExp) {
        // 1. 增加經驗
        hero.setExp(hero.getExp() + gainExp);
        // 2. 更新資料庫 (讓掛機有意義)
        charDao.update(hero);
    }

    @Override
    public boolean breakthrough(Characters hero) {
        // 門檻檢查 (假設 1000 經驗可突破)
        if (hero.getExp() >= 1000) {
            hero.setRealm(hero.getRealm() + 1);
            hero.setExp(hero.getExp() - 1000); // 扣除消耗
            charDao.update(hero);
            return true;
        }
        return false;
    }

	@Override
	public int calculateFinalAtk(Characters hero) {
	    // 1. 基礎與修為加成
	    int totalAtk = hero.getBase_atk() + (int)(hero.getExp() / 10);
	    
	    // 🚀 2. 裝備加成：如果身上有武器，加上武器的 Atk_bonus
	    if (hero.getWeapon_id() != null && hero.getWeapon_id() > 0) {
	        // 透過 Items_DAO 抓取該武器資料
	        model.Items weapon = new dao.Items.Items_DAO_Impl().findById(hero.getWeapon_id());
	        if (weapon != null) {
	            totalAtk += weapon.getAtk_bonus();
	        }
	    }
	    
	    // 3. 境界加成 (1.3 倍階梯)
	    double multiplier = 1.0 + (hero.getRealm() - 1) * 0.3;
	    
	    return (int) (totalAtk * multiplier);
	}

	@Override
	public int calculateFinalDef(Characters hero) {
	    // 1. 基礎 (5) + 修為 (exp/10)
	    int totalDef = hero.getBase_def() + (int)(hero.getExp() / 10);
	    
	    // 🚀 2. 加上「護甲」加成
	    if (hero.getArmor_id() != null && hero.getArmor_id() > 0) {
	        model.Items armor = new dao.Items.Items_DAO_Impl().findById(hero.getArmor_id());
	        if (armor != null) totalDef += armor.getDef_bonus();
	    }
	    
	    // 3. 境界乘數 (1.3倍)
	    double multiplier = 1.0 + (hero.getRealm() - 1) * 0.3;
	    return (int) (totalDef * multiplier);
	}


	@Override
	public int calculateFinalHP(Characters hero) {
		 // ❤️ 生命值：(基礎 + 修為/5) * 境界加成 (假設生命成長較快，每 5 點修為 +1)
	    double multiplier = 1.0 + (hero.getRealm() - 1) * 0.3;
	    return (int) ((hero.getHealth() + (hero.getExp() / 5)) * multiplier);
	}

	@Override
	public List<Characters> findAllOrderByExp() {
		return charDao.findAllOrderByExp();
	}

	@Override
	public long getRequiredExp(int currentRealm) {
	    // 🚀 1. 使用類別上方已經宣告好的 realmDao (變數名要對齊)
	    // 💡 呼叫 getByLevel 並傳入當前境界等級
	    model.RealmConfig config = realmDao.getByLevel(currentRealm);
	    
	    if (config != null) {
	        return config.getExp_required();
	    }
	    // 💡 防呆：若沒設定則給極大值
	    return 9999999L;
	}
}
