package com.ankamagames.framework.graphics.engine;

public class TextureConstants
{
    public static final long FONT_ID = -6196766170285080576L;
    public static final long ANM_ID = -4971768379942633472L;
    public static final long USER_CREATED_ID = -3746770589600186368L;
    public static final long USER_CREATED_MIN_MAP = 57421775717269503L;
    public static final long PARTICLE_ID = -2521772799257739264L;
    public static final long FX_ID = -1296775008915292160L;
    public static final long FX_MASK_ALPHA = -1296775008915292159L;
    public static final long FX_PERTURBATION = -1296775008915292158L;
    public static final long FX_SNOW = -1296775008915292151L;
    public static final long FX_RAIN = -1296775008915292150L;
    public static final long FX_GLOW = -1296775008915292149L;
    public static final long FX_RAINDROP = -1296775008915292148L;
    public static final long FX_WAKFU_EFFECT = -1296775011062775808L;
    public static final long RENDER_TARGET_ID = -71777218572845056L;
    private static long m_nextRenderTargetId;
    
    public static long getNextRenderTargetId() {
        return ++TextureConstants.m_nextRenderTargetId;
    }
    
    static {
        TextureConstants.m_nextRenderTargetId = -71777218572845056L;
    }
}
