package com.ankamagames.wakfu.client.alea.animation;

import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class WalkWithWeaponMovementStyle extends WalkMovementStyle
{
    private final short m_weaponType;
    
    public WalkWithWeaponMovementStyle(final short weaponType) {
        super();
        this.m_weaponType = weaponType;
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        actor.setAnimation("AnimMarche-" + this.m_weaponType);
    }
    
    @Override
    public String getStyleName() {
        return "WALK_WEAPON";
    }
}
