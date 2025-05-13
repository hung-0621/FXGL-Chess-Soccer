package com.exp.server.util;

import com.exp.server.EntityConfig;

/**
 * Pixel ↔ Meter 轉換工具
 */
public class UnitConverter {

    /** 每公尺對應的像素數 (Pixels Per Meter) */
    public static final float PPM = EntityConfig.PPM;

    /** 遊戲場景高度 (像素) – 用來做 Y 軸翻轉 */
    public static final float SCENE_HEIGHT = EntityConfig.SCENE_HEIGHT;

    /** px → m */
    public static float pxToMeter(float px) {
        return px / PPM;
    }

    /** m → px */
    public static float meterToPx(float m) {
        return m * PPM;
    }

    /**
     * FXGL/JavaFX Y＋向下  →  JBox2D Y＋向上
     * (px) → (m)
     */
    public static float screenYToWorldMeter(float screenY) {
        float invertedPx = SCENE_HEIGHT - screenY;
        return pxToMeter(invertedPx);
    }

    /**
     * JBox2D Y＋向上 → FXGL/JavaFX Y＋向下
     * (m) → (px)
     */
    public static float worldMeterToScreenY(float worldMy) {
        float py = meterToPx(worldMy);
        return SCENE_HEIGHT - py;
    }
}

