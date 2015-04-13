package com.ankamagames.wakfu.client.core.world.dynamicElement;

import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.*;

public class NationDynamicElementTypeProvider extends WakfuDynamicElementTypeProvider
{
    private ProtectorNationChangedListener m_listener;
    
    @Override
    public void initialize(final DynamicElement elt) {
        final Protector protector = getProtectorFor(elt);
        if (protector == null) {
            return;
        }
        protector.addProtectorListener(this.m_listener = new ProtectorNationChangedListener() {
            @Override
            public void onNationChanged(final ProtectorBase protector, final Nation nation) {
                elt.setAnimation(elt.getBaseAnimation() + "-" + nation.getNationId());
            }
        });
        this.m_listener.onNationChanged(protector, protector.getCurrentNation());
    }
    
    @Override
    public void clear(final DynamicElement elt) {
        final Protector protector = getProtectorFor(elt);
        if (protector == null) {
            return;
        }
        protector.removeProtectorListener(this.m_listener);
        this.m_listener = null;
    }
    
    private static Protector getProtectorFor(final DynamicElement elt) {
        final Territory territory = TerritoriesView.INSTANCE.getFromWorldPosition(elt.getWorldCellX(), elt.getWorldCellY());
        if (territory == null) {
            NationDynamicElementTypeProvider.m_logger.error((Object)("Aucun Territoire d\u00e9fini pour l'\u00e9l\u00e9ment " + elt));
            return null;
        }
        return (Protector)territory.getProtector();
    }
}
