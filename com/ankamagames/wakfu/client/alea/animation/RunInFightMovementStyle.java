package com.ankamagames.wakfu.client.alea.animation;

import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class RunInFightMovementStyle extends RunMovementStyle
{
    public static final String STYLE_NAME = "RUN_IN_FIGHT";
    private static final RunInFightMovementStyle m_instance;
    
    public static RunInFightMovementStyle getInstance() {
        return RunInFightMovementStyle.m_instance;
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        if (actor.containsAnimation("AnimCourse02")) {
            actor.setAnimation("AnimCourse02");
        }
        else if (actor.containsAnimation("AnimCourse")) {
            actor.setAnimation("AnimCourse");
        }
        else {
            actor.setAnimation("AnimMarche");
        }
    }
    
    @Override
    public String getStyleName() {
        return "RUN_IN_FIGHT";
    }
    
    static {
        m_instance = new RunInFightMovementStyle();
    }
}
