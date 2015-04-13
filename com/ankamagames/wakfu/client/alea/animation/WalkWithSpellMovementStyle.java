package com.ankamagames.wakfu.client.alea.animation;

import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class WalkWithSpellMovementStyle extends WalkMovementStyle
{
    public static final String STYLE_NAME = "WALK_SPELL";
    private static final WalkWithSpellMovementStyle m_instance;
    
    public static WalkWithSpellMovementStyle getInstance() {
        return WalkWithSpellMovementStyle.m_instance;
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        actor.setAnimation("AnimMarche02");
    }
    
    @Override
    public String getStyleName() {
        return "WALK_SPELL";
    }
    
    static {
        m_instance = new WalkWithSpellMovementStyle();
    }
}
