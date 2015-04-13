package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class StaticEffectAreaManagerImpl extends StaticEffectAreaManager
{
    private final BinaryLoader<AreaEffectBinaryData> m_creator;
    
    public StaticEffectAreaManagerImpl(final BinaryLoader<AreaEffectBinaryData> creator) {
        super();
        this.m_creator = creator;
    }
    
    @Override
    public AbstractTrapEffectArea getTrapEffectArea(final long trapId) {
        if (super.getTrapEffectArea(trapId) == null) {
            this.loadAreaFromStorage(trapId);
        }
        return super.getTrapEffectArea(trapId);
    }
    
    private void loadAreaFromStorage(final long trapId) {
        if (trapId <= 0L) {
            return;
        }
        final AreaEffectBinaryData binaryData = this.m_creator.createFromId((int)trapId);
        if (binaryData == null) {
            return;
        }
        final AbstractEffectArea effectArea = EffectAreaFactory.INSTANCE.createEffectArea(binaryData);
        if (effectArea == null) {
            return;
        }
        effectArea.setCanBeComputedAndExecuted(false);
        this.addEffectArea(effectArea);
        if (binaryData.getEffectIds() == null) {
            return;
        }
        for (final int effectId : binaryData.getEffectIds()) {
            final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
            if (effect != null) {
                ((BasicEffectArea<WakfuEffect, P>)effectArea).addEffect(effect);
            }
            else {
                StaticEffectAreaManagerImpl.m_logger.error((Object)("Probl\u00e8me de chargmeent de effectArea " + binaryData.getId()));
            }
        }
    }
    
    @Override
    public AbstractGlyphEffectArea getGlyph(final long glyphId) {
        if (super.getGlyph(glyphId) == null) {
            this.loadAreaFromStorage(glyphId);
        }
        return super.getGlyph(glyphId);
    }
    
    @Override
    public AbstractBeaconEffectArea getBeacon(final long beaconId) {
        if (super.getBeacon(beaconId) == null) {
            this.loadAreaFromStorage(beaconId);
        }
        return super.getBeacon(beaconId);
    }
    
    @Override
    public AbstractAuraEffectArea getAura(final long auraId) {
        if (super.getAura(auraId) == null) {
            this.loadAreaFromStorage(auraId);
        }
        return super.getAura(auraId);
    }
    
    @Override
    public AbstractBattlegroundBorderEffectArea getBorderCellArea(final long borderCellId) {
        if (super.getBorderCellArea(borderCellId) == null) {
            this.loadAreaFromStorage(borderCellId);
        }
        return super.getBorderCellArea(borderCellId);
    }
    
    @Override
    public AbstractHourEffectArea getHourAreas(final long hourAreaBaseId) {
        if (super.getHourAreas(hourAreaBaseId) == null) {
            this.loadAreaFromStorage(hourAreaBaseId);
        }
        return super.getHourAreas(hourAreaBaseId);
    }
    
    @Override
    public AbstractWallEffectArea getWallArea(final long wallAreaBaseId) {
        if (super.getWallArea(wallAreaBaseId) == null) {
            this.loadAreaFromStorage(wallAreaBaseId);
        }
        return super.getWallArea(wallAreaBaseId);
    }
    
    @Override
    public AbstractWarpEffectArea getWarpArea(final long warpAreaBaseId) {
        if (super.getWarpArea(warpAreaBaseId) == null) {
            this.loadAreaFromStorage(warpAreaBaseId);
        }
        return super.getWarpArea(warpAreaBaseId);
    }
    
    @Override
    public AbstractEffectArea getAreaFromId(final long areaId) {
        if (super.getAreaFromId(areaId) == null) {
            this.loadAreaFromStorage(areaId);
        }
        return super.getAreaFromId(areaId);
    }
    
    @Override
    public AbstractBombEffectArea getBombArea(final long id) {
        final AbstractBombEffectArea effectArea = super.getBombArea(id);
        if (effectArea != null) {
            return effectArea;
        }
        this.loadAreaFromStorage(id);
        return super.getBombArea(id);
    }
}
