package com.ankamagames.wakfu.client.alea.animation;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;

public class OnRailsMovementStyle implements PathMovementStyle
{
    protected static Logger m_logger;
    private static final OnRailsMovementStyle m_instance;
    private final JumpTrajectory m_jumpDown;
    private final JumpTrajectory m_jumpUp;
    
    public static OnRailsMovementStyle getInstance() {
        return OnRailsMovementStyle.m_instance;
    }
    
    protected OnRailsMovementStyle() {
        super();
        this.m_jumpDown = new JumpTrajectoryDown(0.0f, 0.5f, 0.5f, 1.0f);
        this.m_jumpUp = new JumpTrajectoryUp(0.0f, 0.5f, 0.5f, 1.0f);
    }
    
    @Override
    public final int getCellSpeed(final StyleMobile actor) {
        return MovementSpeed.NORMAL_RUN_SPEED.getTimeBetweenCells() / 2;
    }
    
    @Override
    public JumpTrajectory getJumpDown() {
        return this.m_jumpDown;
    }
    
    @Override
    public JumpTrajectory getJumpUp() {
        return this.m_jumpUp;
    }
    
    @Override
    public void onStandingOnLastCell(final StyleMobile actor) {
        final String currentAnimation = actor.getAnimation();
        actor.setAnimation("AnimRail-Fin");
    }
    
    @Override
    public void onStandingOnFirstCell(final StyleMobile actor, final Direction8 nextDirection) {
        actor.setDirection(nextDirection);
    }
    
    @Override
    public int getDelayOnFirstCell() {
        return 560;
    }
    
    @Override
    public void onMovingSpecialJump(final StyleMobile actor) {
    }
    
    @Override
    public void onMovingOnAir(final StyleMobile actor, final int deltaZ, final Phase jumpPhase) {
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        final String currentAnimation = actor.getAnimation();
        if (!currentAnimation.startsWith("AnimRail")) {
            actor.setAnimation("AnimRail-Debut");
        }
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
        return MovementStyleManager.RUN_STYLE;
    }
    
    static {
        OnRailsMovementStyle.m_logger = Logger.getLogger((Class)OnRailsMovementStyle.class);
        m_instance = new OnRailsMovementStyle();
    }
}
