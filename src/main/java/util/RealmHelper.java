package util;

public class RealmHelper {
    public static String getRealmName(int level) {
        String[] names = {"凡人", "練氣前期", "練氣中期", "練氣後期", "築基初期", "築基中期", "築基後期", "金丹大能", "元神真君"};
        if (level <= 0) return names[0];
        if (level >= names.length) return "九天真仙";
        return names[level];
    }
}
