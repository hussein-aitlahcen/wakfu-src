package com.ankamagames.wakfu.client.alea.animation;

import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.mount.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump.*;

public class MountMovementStyle implements PathMovementStyle
{
    protected static Logger m_logger;
    private final MountType m_mountType;
    private static final JumpTrajectory JUMP_DOWN;
    private static final JumpTrajectory JUMP_UP;
    
    public MountMovementStyle(final MountType mountType) {
        super();
        this.m_mountType = mountType;
    }
    
    @Override
    public int getCellSpeed(final StyleMobile actor) {
        return this.m_mountType.getMovementSpeed().getTimeBetweenCells();
    }
    
    @Override
    public JumpTrajectory getJumpDown() {
        return MountMovementStyle.JUMP_DOWN;
    }
    
    @Override
    public JumpTrajectory getJumpUp() {
        return MountMovementStyle.JUMP_UP;
    }
    
    @Override
    public void onStandingOnLastCell(final StyleMobile actor) {
        final String currentAnimation = actor.getAnimation();
        if (currentAnimation.equals(this.m_mountType.getAnimPlayerJump())) {
            actor.setAnimation(this.m_mountType.getAnimPlayerStatic());
        }
        else {
            actor.setAnimation(this.m_mountType.getAnimPlayerRunEnd());
        }
    }
    
    @Override
    public void onStandingOnFirstCell(final StyleMobile actor, final Direction8 nextDirection) {
    }
    
    @Override
    public int getDelayOnFirstCell() {
        return 0;
    }
    
    @Override
    public void onMovingOnAir(final StyleMobile actor, final int deltaZ, final Phase jumpPhase) {
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        actor.setIsInSpecialJump(false);
        actor.setAnimation(this.m_mountType.getAnimPlayerRun());
    }
    
    @Override
    public void onMovingSpecialJump(final StyleMobile actor) {
        actor.setIsInSpecialJump(true);
        if (!actor.getDirection().isDirection4()) {
            return;
        }
        final String previousAnimation = actor.getAnimation();
        actor.setAnimation(this.m_mountType.getAnimPlayerJump());
    }
    
    @Override
    public void onDirectionChanged(final StyleMobile actor, final Direction8 newDirection) {
        actor.setDirection(newDirection);
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
        return "Mount";
    }
    
    static {
        MountMovementStyle.m_logger = Logger.getLogger((Class)MountMovementStyle.class);
        JUMP_DOWN = new JumpTrajectoryDown(0.0f, 0.5f, 0.5f, 1.0f);
        JUMP_UP = new JumpTrajectoryUp(0.0f, 0.5f, 0.5f, 1.0f);
    }
}
