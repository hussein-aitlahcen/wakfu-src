package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class DefaultAnmActionFactory implements AnmActionFactory
{
    public static final DefaultAnmActionFactory INSTANCE;
    
    @Override
    public AnmAction fromId(final AnmActionTypes actionType) {
        switch (actionType) {
            case GO_TO_ANIMATION: {
                return new AnmActionGoToAnimation();
            }
            case GO_TO_STATIC_ANIMATION: {
                return AnmActionGoToStaticAnimation.INSTANCE;
            }
            case RUN_SCRIPT: {
                return new AnmActionRunScript();
            }
            case GO_TO_RANDOM_ANIMATION: {
                return new AnmActionGoToRandomAnimation();
            }
            case GO_TO_IF_PREVIOUS_ANIMATION: {
                return new AnmActionGoToIfPreviousAnimation();
            }
            case ADD_PARTICLE: {
                return new AnmActionAddParticle();
            }
            case SET_RADIUS: {
                return new AnmActionSetRadius();
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        INSTANCE = new DefaultAnmActionFactory();
    }
}
