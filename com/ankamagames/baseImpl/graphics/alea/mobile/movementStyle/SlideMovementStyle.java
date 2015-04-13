package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump.*;

public class SlideMovementStyle implements PathMovementStyle
{
    private static int SLIDE_CELL_SPEED;
    private static final SlideMovementStyle m_instance;
    
    public static SlideMovementStyle getInstance() {
        return SlideMovementStyle.m_instance;
    }
    
    @Override
    public JumpTrajectory getJumpDown() {
        return JumpHelper.NullJumpTrajectory;
    }
    
    @Override
    public JumpTrajectory getJumpUp() {
        return JumpHelper.NullJumpTrajectory;
    }
    
    @Override
    public int getCellSpeed(final StyleMobile actor) {
        return SlideMovementStyle.SLIDE_CELL_SPEED;
    }
    
    @Override
    public void onStandingOnLastCell(final StyleMobile actor) {
        actor.resetMovementSelector();
    }
    
    @Override
    public void onStandingOnFirstCell(final StyleMobile actor, final Direction8 nextDirection) {
    }
    
    @Override
    public int getDelayOnFirstCell() {
        return -1;
    }
    
    @Override
    public void onMovingOnAir(final StyleMobile actor, final int deltaZ, final Phase jumpPhase) {
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
    }
    
    @Override
    public void onMovingSpecialJump(final StyleMobile actor) {
    }
    
    public void onWaiting(final StyleMobile actor) {
    }
    
    @Override
    public void onDirectionChanged(final StyleMobile actor, final Direction8 newDirection) {
    }
    
    @Override
    public boolean createPathOnSetPosition(final StyleMobile actor) {
        return true;
    }
    
    @Override
    public boolean isAirImpulsionNeeded(final StyleMobile actor, final int dz) {
        return false;
    }
    
    @Override
    public String getStyleName() {
        return MovementStyleManager.SLIDE_STYLE;
    }
    
    static {
        SlideMovementStyle.SLIDE_CELL_SPEED = 150;
        m_instance = new SlideMovementStyle();
    }
}
