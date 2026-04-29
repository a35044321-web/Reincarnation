# 🌌 九霄尋道 (Nine Heavens Cultivation)
> **基於 Java Swing 與 MySQL 構建，深度整合 AI 協作技術的沉浸式修仙模擬 RPG**

《九霄尋道》是一款融合了 **文字養成**、**即時戰鬥動畫**與**天道管理體系**的 Java 開發專案。本專案除了深研物件導向架構（OOP），更全面引入 **AI 驅動開發（AI-Driven Development）**，從視覺美學到戰鬥音效，展現了現代工程師與 AI 協作的高效生產力。

---

## 📸 仙途紀實 (System Showcases)

### 🎭 角色登入與創生
<div align="center">
  <img src="images/login_ui.png" width="400px">
  <br>
  <img src="images/create_male_role_ui.png" width="300px">
  <img src="images/create_female_role_ui.png" width="300px">
  <p><i>由上至下：仙途入口、男女修士角色創建介面</i></p>
</div>

### ⚔️ 靈壓戰鬥與歷練
<div align="center">
  <img src="images/homepage_ui.png" width="500px">
  <br>
  <img src="images/battle_ui.png" width="400px">
  <img src="images/battle_end_ui.png" width="350px">
  <p><i>主城地圖、水墨風對撞戰鬥系統及戰利品結算</i></p>
</div>

### 📦 乾坤袋與修士詳情
<div align="center">
  <img src="images/character_detail_ui.png" width="350px">
  <img src="images/items_ui.png" width="350px">
  <p><i>修士屬性面板與乾坤袋（Inventory）管理系統</i></p>
</div>

### 🔱 天道管理宮 (Admin System)
<div align="center">
  <img src="images/admin_login_ui.png" width="300px">
  <img src="images/account_managing_ui.png" width="300px">
  <img src="images/items_managing_ui.png" width="300px">
  <br>
  <img src="images/billboard_ui.png" width="600px">
  <p><i>天道管理後台：含修士帳號管理、法寶百科維護與全服公告系統</i></p>
</div>

---

## 💎 核心亮點：AI 創意協作 (AI Collaboration)

本專案在開發過程中深度整合多項 AI 技術，實現了個人開發者難以企及的視聽規模：

* **視覺資產生成**：
    * 利用 **AI 繪圖技術** 生成高品質的場景背景、極具仙氣的角色立繪。
    * 針對法寶與怪物模型，透過 AI 確保了整體美術風格的一致性。
* **戰鬥音效設計**：
    * 透過 **AI 音頻生成技術** 製作沉浸式的背景音樂（BGM）與精準的戰鬥打擊音效，強化了 Java Swing 視窗程式的遊戲感。
* **開發協作**：
    * 運用 AI 輔助優化複雜演算法與 SQL 語句，將開發重心精確投入於架構設計與系統穩定度。

---

## 🧬 核心系統 (Core Systems)

### 1. ⚡ 雷劫渡劫與突破
* **視覺震撼**：突破時觸發全螢幕閃電特效與視窗抖動，模擬天道威壓。
* **動態門檻**：從資料庫 `realm_config` 實時讀取境界需求，實現隨等級遞增的修煉難度。

### 2. 📊 數據持久化技術
* **JDBC 批次處理**：優化背包與屬性同步，降低資料庫負擔。
* **天道帳本**：整合 **Apache POI**，支援全服修士數據導出至 Excel。

---

## 🛠️ 技術棧 (Technical Stack)

| 層級 | 技術實作 |
| :--- | :--- |
| **表現層 (View)** | Java Swing, AWT (Custom Graphics2D Rendering) |
| **業務層 (Service)** | OOP Logic, Multithreading (Stamina Recovery) |
| **持久層 (DAO)** | JDBC, MySQL 8.0, PreparedStatement |
| **AI 協作** | Midjourney / ChatGPT / AI Audio Generator |

---

## 👨‍💻 開發者心得
在《九霄尋道》的開發過程中，我致力於挑戰 **Java Swing** 在遊戲開發上的界限。最令我感到興奮的，是成功將「嚴謹的後端邏輯」與「靈活的 AI 生成資產」相結合。透過 AI 協作，我能夠以一人之力完成包含視覺、聽覺與複雜數據庫設計的完整 RPG 系統，這體現了現代開發者整合多元技術資源、解決跨領域問題的核心競爭力。

---

**願道友早日證得大道，九霄凌雲！**
