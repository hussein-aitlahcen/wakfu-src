package com.ankamagames.wakfu.client.alea.graphics.anm;

import com.ankamagames.baseImpl.graphics.alea.animatedElement.actions.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class WakfuAnmActionFactory extends DefaultAnmActionFactory
{
    public static final WakfuAnmActionFactory INSTANCE;
    
    @Override
    public AnmAction fromId(final AnmActionTypes actionType) {
        switch (actionType) {
            case HIT: {
                return AnmActionHit.INSTANCE;
            }
            case END: {
                return AnmActionEnd.INSTANCE;
            }
            case DELETE: {
                return AnmActionDelete.INSTANCE;
            }
            default: {
                return super.fromId(actionType);
            }
        }
    }
    
    static {
        INSTANCE = new WakfuAnmActionFactory();
    }
}
