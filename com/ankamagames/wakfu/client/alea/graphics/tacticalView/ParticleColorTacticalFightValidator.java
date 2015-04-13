package com.ankamagames.wakfu.client.alea.graphics.tacticalView;

import com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper.*;

public class ParticleColorTacticalFightValidator implements ColorHelperValidator
{
    @Override
    public boolean canApplyColor() {
        return !TacticalViewManager.getInstance().isActivated();
    }
}
