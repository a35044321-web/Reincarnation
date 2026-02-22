package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javazoom.jl.player.Player;

public class MusicPlayer {
    private static Player mp3Player;
    private static Thread musicThread;

    public static void playBGM(String fileName) {
        musicThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    InputStream is = null;
                    
                    // 💡 軌道一：類路徑感應 (Maven target)
                    is = MusicPlayer.class.getResourceAsStream("/audio/" + fileName);
                    
                    // 💡 軌道二：針對您的平級結構 (reincarnation/resources/audio)
                    if (is == null) {
                        java.io.File file = new java.io.File("resources/audio/" + fileName);
                        if (file.exists()) is = new java.io.FileInputStream(file);
                    }

                    // 💡 軌道三：標準 Maven 實體路徑
                    if (is == null) {
                        java.io.File file = new java.io.File("src/main/resources/audio/" + fileName);
                        if (file.exists()) is = new java.io.FileInputStream(file);
                    }

                    if (is == null) {
                        System.err.println("❌ [MusicPlayer] 找不到靈音檔案: " + fileName);
                        // 印出絕對路徑供除錯
                        System.err.println("🔍 預期位置: " + new java.io.File("resources/audio/" + fileName).getAbsolutePath());
                        break; 
                    }

                    mp3Player = new javazoom.jl.player.Player(new java.io.BufferedInputStream(is));
                    System.out.println("🎵 [MusicPlayer] 靈音啟動: " + fileName);
                    mp3Player.play();
                }
            } catch (Exception e) {
                System.err.println("⚠️ [MusicPlayer] 播放中斷");
            }
        });
        musicThread.start();
    }

    public static void stopBGM() {
        if (mp3Player != null) {
            mp3Player.close();
            if (musicThread != null) musicThread.interrupt();
        }
    }
}