package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump.*;

public class NoneMovementSelector implements MovementSelector
{
    private static final NoneMovementSelector m_instance;
    
    public static NoneMovementSelector getInstance() {
        return NoneMovementSelector.m_instance;
    }
    
    @Override
    public PathMovementStyle selectMovementStyle(final StyleMobile mobile, final int pathLength) {
        return NoneMovementStyle.getInstance();
    }
    
    @Override
    public void onMovementEnded(final StyleMobile mobile) {
    }
    
    @Override
    public void resetMovementSelector(final StyleMobile mobile) {
    }
    
    static {
        m_instance = new NoneMovementSelector();
    }
    
    public static class NoneMovementStyle implements PathMovementStyle
    {
        private static final NoneMovementStyle m_instance;
        
        public static NoneMovementStyle getInstance() {
            return NoneMovementStyle.m_instance;
        }
        
        @Override
        public int getCellSpeed(final StyleMobile actor) {
            return 0;
        }
        
        @Override
        public JumpTrajectory getJumpDown() {
            return null;
        }
        
        @Override
        public JumpTrajectory getJumpUp() {
            return null;
        }
        
        @Override
        public int getDelayOnFirstCell() {
            return -1;
        }
        
        @Override
        public void onStandingOnLastCell(final StyleMobile actor) {
        }
        
        @Override
        public void onStandingOnFirstCell(final StyleMobile actor, final Direction8 nextDirection) {
        }
        
        @Override
        public void onMovingOnAir(final StyleMobile actor, final int deltaZ, final Phase jumpPhase) {
        }
        
        @Override
        public void onMovingSpecialJump(final StyleMobile actor) {
        }
        
        @Override
        public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        }
        
        @Override
        public void onDirectionChanged(final StyleMobile actor, final Direction8 newDirection) {
        }
        
        @Override
        public boolean createPathOnSetPosition(final StyleMobile actor) {
            return false;
        }
        
        @Override
        public boolean isAirImpulsionNeeded(final StyleMobile actor, final int z) {
            return false;
        }
        
        @Override
        public String getStyleName() {
            return "none";
        }
        
        static {
            m_instance = new NoneMovementStyle();
        }
    }
}
