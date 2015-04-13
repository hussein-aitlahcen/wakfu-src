package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;

public class PandawaFightListener implements BreedSpecific, CharacteristicUpdateListener
{
    private static final String[] DIALOGS;
    public static final String PANDA_BARREL_LABEL_ID = "pandaBarrelLabel";
    private static final String LEVEL_0 = "-";
    private final PlayerCharacter m_character;
    private BarrelEffectArea m_barrel;
    private final List<Label> m_labels;
    
    public PandawaFightListener(final PlayerCharacter character) {
        super();
        this.m_labels = new ArrayList<Label>();
        this.m_character = character;
        for (final String mapId : PandawaFightListener.DIALOGS) {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(mapId);
            if (map != null) {
                final Label label = (Label)map.getElement("pandaBarrelLabel");
                if (label != null) {
                    this.m_labels.add(label);
                }
            }
        }
    }
    
    @Override
    public void onSpecialFighterEvent(final SpecialEvent event) {
    }
    
    @Override
    public void onBarrelCarried(final BasicEffectArea area) {
        if (this.m_barrel != null) {
            return;
        }
        if (area.getType() != EffectAreaType.BARREL.getTypeId()) {
            return;
        }
        final BarrelEffectArea barrel = (BarrelEffectArea)area;
        if (barrel.getOwner() != this.m_character) {
            return;
        }
        this.m_barrel = barrel;
        final AbstractCharacteristic hp = this.m_barrel.getCharacteristic(AbstractBarrelEffectArea.getHpCharac());
        hp.addListener(this);
        this.updateBarrelValue(this.computeBarrelHpInPercent(hp));
    }
    
    @Override
    public void onBarrelUncarried(final BasicEffectArea area) {
        if (area == this.m_barrel) {
            final AbstractCharacteristic hp = this.m_barrel.getCharacteristic(AbstractBarrelEffectArea.getHpCharac());
            hp.removeListener(this);
            this.m_barrel = null;
            this.updateBarrelValue(0);
        }
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        if (charac.getType() != AbstractBarrelEffectArea.getHpCharac()) {
            return;
        }
        this.updateBarrelValue(this.computeBarrelHpInPercent(charac));
    }
    
    private int computeBarrelHpInPercent(final AbstractCharacteristic charac) {
        return MathHelper.fastFloor(charac.value() / charac.max() * 100.0f);
    }
    
    private void updateBarrelValue(final int value) {
        for (int i = this.m_labels.size() - 1; i >= 0; --i) {
            this.m_labels.get(i).setText((value == 0) ? "-" : String.valueOf(value));
        }
    }
    
    @Override
    public void clear() {
        PandawaFightListenerManager.INSTANCE.unregisterCharacter(this.m_character);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PandawaFightListener");
        sb.append("{m_character=").append(this.m_character);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        DIALOGS = new String[] { "worldAndFightBarDialog" };
    }
}
