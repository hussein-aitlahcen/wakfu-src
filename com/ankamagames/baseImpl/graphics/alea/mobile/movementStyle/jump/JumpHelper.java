package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class JumpHelper
{
    public static final JumpTrajectory NullJumpTrajectory;
    
    public static void setJumpAnimation(final StyleMobile actor, final int deltaZ, final Phase jumpPhase, final boolean isSlowJump) {
        switch (jumpPhase) {
            case BEFORE:
            case AFTER: {}
            case ASCENDING:
            case STABLE:
            case DESCENDING: {
                if (!isSlowJump) {
                    actor.setAnimation("AnimSaut");
                    break;
                }
                actor.setAnimation("AnimSaut-Marche");
                break;
            }
        }
    }
    
    static {
        NullJumpTrajectory = new JumpTrajectory(0.5f, 0.5f, 0.5f, 0.5f) {
            @Override
            protected float getStartZ(final int cellStartZ, final int cellEndZ, final Phase phase) {
                return cellStartZ;
            }
            
            @Override
            protected float getEndZ(final int cellStartZ, final int cellEndZ, final Phase phase) {
                return cellEndZ;
            }
        };
    }
}
