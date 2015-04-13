package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.framework.graphics.image.*;

public class TerritoryViewConstants
{
    public static final Color NONE;
    public static final Color FRIENDLY;
    public static final Color NEUTRAL;
    public static final Color ENNEMY;
    
    static {
        NONE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        FRIENDLY = new Color(0.4f, 1.0f, 0.4f, 1.0f);
        NEUTRAL = new Color(1.0f, 1.0f, 0.4f, 1.0f);
        ENNEMY = new Color(1.0f, 0.4f, 0.4f, 1.0f);
    }
}
