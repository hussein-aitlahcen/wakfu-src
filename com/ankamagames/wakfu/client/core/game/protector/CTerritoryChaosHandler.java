package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class CTerritoryChaosHandler extends TerritoryChaosHandler<Territory>
{
    public CTerritoryChaosHandler(final Territory territory) {
        super(territory);
    }
    
    @Override
    public void startChaos() {
        super.startChaos();
        if (this.m_territory == WakfuGameEntity.getInstance().getLocalPlayer().getCurrentTerritory()) {
            WakfuAmbianceListener.getInstance().setInChaos(true);
            WakfuSoundManager.getInstance().onEvent(new WeatherSoundEvent(WeatherEventType.CHAOS_START));
        }
        else {
            final AleaIsoCamera camera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
            WeatherEffectManager.throwThunder(camera, ThunderEffect.DEFAULT_FLASH_COLOR);
        }
    }
    
    @Override
    public void endChaos() {
        super.endChaos();
        if (this.m_territory == WakfuGameEntity.getInstance().getLocalPlayer().getCurrentTerritory()) {
            WakfuSoundManager.getInstance().onEvent(new WeatherSoundEvent(WeatherEventType.CHAOS_END));
        }
        WakfuAmbianceListener.getInstance().setInChaos(false);
    }
}
