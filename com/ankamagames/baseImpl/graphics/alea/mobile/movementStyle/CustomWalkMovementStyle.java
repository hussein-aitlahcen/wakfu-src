package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle;

import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class CustomWalkMovementStyle extends WalkMovementStyle
{
    String m_animation;
    int m_cellSpeed;
    
    public CustomWalkMovementStyle() {
        super();
        this.m_cellSpeed = MovementSpeed.NORMAL_WALK_SPEED.getTimeBetweenCells();
    }
    
    public void setAnimation(final String animation) {
        this.m_animation = animation;
    }
    
    public void setCellSpeed(final int cellSpeed) {
        this.m_cellSpeed = cellSpeed;
    }
    
    @Override
    public int getCellSpeed(final StyleMobile actor) {
        return this.m_cellSpeed;
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        actor.setAnimation(this.m_animation);
    }
    
    @Override
    public String getStyleName() {
        return MovementStyleManager.CUSTOM_STYLE;
    }
}
