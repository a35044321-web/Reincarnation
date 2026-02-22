# 🌌 九霄尋道 (Nine Heavens Cultivation)
> **基於 Java Swing 與 MySQL 構建的沉浸式修仙模擬 RPG**

![Java Version](https://img.shields.io)
![Database](https://img.shields.io)
![License](https://img.shields.io)
![Status](https://img.shields.io)

《九霄尋道》是一款融合了 **文字養成**、**即時戰鬥動畫**與**天道管理體系**的 Java 開發專案。透過物件導向設計與資料庫持久化技術，模擬修士從「凡人」歷練至「大能」的完整修仙因果。

---

## 🚀 快速開始 (Quick Start)

### 1. 資料庫建置
請在 MySQL 執行根目錄下的 [init_reincarnation.sql](./init_reincarnation.sql)，包含核心法陣：
* `characters`: 修士核心屬性
* `player_items`: 背包關聯表
* `items`: 法寶百科全書

### 2. 運行入口
執行 `src/main/java/view/Start_UI.java` 即可開啟仙途。
---

## 💎 核心系統 (Core Systems)

### 1. 🎭 靈壓戰鬥與歷練 (Expedition & Combat)
*   **動態渲染**：自定義 `Canvas` 實現水墨風格的對撞戰鬥動畫。
*   **因果掉落**：根據 **[Gaussion Distribution]** 邏輯，結合修士境界與怪物等級，動態生成神兵或護甲。
*   **境界壓制**：實時計算「基礎屬性 + 修為加成 + 裝備加持 * 境界倍率」的三位一體戰力公式。

### 2. ⚡ 雷劫渡劫與突破 (Breakthrough Mechanics)
*   **視覺震撼**：突破時觸發全螢幕閃電特效與視窗抖動，模擬天道威壓。
*   **動態門檻**：從資料庫 `realm_config` 實時讀取境界需求，實現隨等級遞增的修煉難度。

### 3. 📦 乾坤袋與裝備 (Inventory System)
*   **持久化標記**：具備 [E] (Equipped) 裝備狀態同步，確保重登後法寶依然「認主」。
*   **品質濾鏡**：自定義 `ImageLoader` 為不同品質（下品至極品）的法寶添加動態光效。

### 4. 🔱 天道管理宮 (Admin & Data Analysis)
*   **神權控制**：專屬 `ADMIN` 權限分流，具備修士數據重塑與一鍵敕封功能。
*   **天道帳本**：整合 **Apache POI**，實現全服數據一鍵導出至 Excel 並支援即時列印。

---

## 🛠️ 技術棧 (Technical Stack)


| 層級 | 技術實作 |
| :--- | :--- |
| **表現層 (View)** | Java Swing, AWT (Custom Graphics2D Rendering) |
| **業務層 (Service)** | OOP Logic, Multithreading (Stamina Recovery) |
| **持久層 (DAO)** | JDBC, MySQL 8.0, PreparedStatement |
| **依賴管理** | Maven (Apache POI, MySQL Connector) |

---

## 🚀 快速開始 (Quick Start)

### 1. 環境準備
*   安裝 [Java JDK 8](https://www.oracle.comtechnologies/javase/javase-jdk8-downloads.html) 或以上版本。
*   配置 [MySQL 8.0](https://dev.mysql.com) 資料庫。

### 2. 資料庫初始化
請執行專案目錄下的 `sql/init_reincarnation.sql`，建立核心法陣：
*   `characters`: 修士核心屬性
*   `player_items`: 背包關聯表
*   `realm_config`: 天道境界參數

### 3. 運行專案
透過 [Eclipse](https://www.eclipse.org) 或任何 Java IDE 執行：
`src/main/java/view/Start_UI.java`

---

## 👨‍💻 開發者心得
本專案挑戰了 **Java Swing** 在遊戲開發中的極限，解決了 UI 刷新頻率同步（FPS）、數據異步持久化以及跨視窗物件傳遞等技術難點。旨在展現 Java 在構建複雜邏輯系統時的嚴謹性與靈活性。

---

**願道友早日證得大道，九霄凌雲！**
