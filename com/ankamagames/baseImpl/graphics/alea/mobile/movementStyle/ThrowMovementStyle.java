package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump.*;

public class ThrowMovementStyle implements PathMovementStyle
{
    private static int THROW_CELL_SPEED;
    private int m_distance;
    
    public ThrowMovementStyle() {
        super();
        this.m_distance = 0;
    }
    
    @Override
    public int getCellSpeed(final StyleMobile actor) {
        return (this.m_distance > 1) ? ThrowMovementStyle.THROW_CELL_SPEED : 300;
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
    public JumpTrajectory getJumpDown() {
        return JumpHelper.NullJumpTrajectory;
    }
    
    @Override
    public JumpTrajectory getJumpUp() {
        return JumpHelper.NullJumpTrajectory;
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
        return true;
    }
    
    @Override
    public String getStyleName() {
        return MovementStyleManager.THROW_STYLE;
    }
    
    public void setDistance(final int distance) {
        this.m_distance = distance;
    }
    
    static {
        ThrowMovementStyle.THROW_CELL_SPEED = 60;
    }
}
