package view;

import java.awt.*;
import javax.swing.*;
import util.ImageLoader;

/**
 * 歷練戰鬥舞台：實作圖片對向衝鋒與碰撞特效
 */
public class CombatCanvas extends JPanel {
    private Image playerImg;
    private Image monsterImg;
    private int pX, mX;               // 座標
    private int shakeOffset = 0;      // 震動偏移量
    private Color dropGlow = null;    // 掉落物品質顏色
    private Timer animTimer;
    private boolean isCollided = false;

    public CombatCanvas() {
        setOpaque(false); // 透明背景以顯示底層背景圖
        setPreferredSize(new Dimension(800, 350));
    }

    /**
     * 啟動戰鬥動畫
     * @param pName 主角圖檔名 (從 avatars 資料夾)
     * @param mName 怪物圖檔名 (從 monsters 資料夾)
     * @param quality 掉落物顏色 (若無掉落傳入 null)
     * @param onFinish 動畫結束後的回調邏輯 (如顯示戰報)
     */
    public void startAnim(String pName, String mName, Color quality, Runnable onFinish) {
        // 💡 1. 尺寸加大到 380 (原本 250 * 1.5 ≈ 375)
        int imgSize = 380;
        playerImg = ImageLoader.getIcon("avatars", pName, imgSize, imgSize).getImage();
        monsterImg = ImageLoader.getIcon("monsters", mName, imgSize, imgSize).getImage();
        
        // 💡 2. 調整起點座標，讓大圖從更遠處衝進來
        final int startPX = -200; 
        final int startMX = 1200; 
        final int[] hitCount = {0}; 
        
        this.pX = startPX;
        this.mX = startMX;
        this.isCollided = false;

        if (animTimer != null && animTimer.isRunning()) animTimer.stop();

        animTimer = new Timer(16, e -> {
            // 💡 3. 碰撞距離也需加寬 (因為圖片變大了)
            if (pX < mX - 250) { 
                pX += 45; // 極速衝鋒感
                mX -= 45;
                isCollided = false;
            } else {
                if (!isCollided) {
                    isCollided = true;
                    hitCount[0]++;
                    shakeOffset = (hitCount[0] % 2 == 0) ? 80 : -80; // 震動幅度加大到 80
                }

                if (hitCount[0] < 3) {
                    pX = startPX;
                    mX = startMX;
                } else {
                    animTimer.stop();
                    shakeOffset = 0;
                    onFinish.run();
                }
            }
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 💡 繪製位置微調 (Y 軸下移避免互撞時太擠)
        if (playerImg != null) g2d.drawImage(playerImg, pX + shakeOffset, 50, this);
        if (monsterImg != null) g2d.drawImage(monsterImg, mX - shakeOffset, 50, this);

        // 💡 特效放大：靈氣光芒與「砰」
        if (isCollided) {
            int centerX = (pX + mX) / 2 + 80;
            int centerY = 150;

            // 巨型靈氣：半徑加大到 250
            if (dropGlow != null) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.4f));
                g2d.setColor(dropGlow);
                g2d.fillOval(centerX - 100, centerY - 100, 250, 250);
            }

            // 巨型文字：字體加大到 80
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2d.setColor(new Color(139, 0, 0)); // 深紅水墨色
            g2d.setFont(new Font("Microsoft JhengHei", Font.BOLD, 85));
            g2d.drawString("砰！！", centerX - 50, centerY);
        }
    }
}
