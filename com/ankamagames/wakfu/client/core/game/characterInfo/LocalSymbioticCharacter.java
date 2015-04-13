package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.BreedSpecific.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;

public final class LocalSymbioticCharacter implements SymbioticCharacter<Symbiot>
{
    private Symbiot m_symbiot;
    private byte m_maximumSeducableCreatures;
    
    public LocalSymbioticCharacter() {
        super();
        this.m_maximumSeducableCreatures = 1;
    }
    
    @Override
    public Symbiot getSymbiot() {
        return this.m_symbiot;
    }
    
    @Override
    public void setSymbiot(final Symbiot symbiot) {
        this.m_symbiot = symbiot;
    }
    
    @Override
    public void onSymbiotAddCreature(final byte index) {
        final BasicInvocationCharacteristics charac = this.m_symbiot.getCreatureParametersFromIndex(index);
        if (charac.getName() == null || charac.getName().isEmpty()) {
            final String name = WakfuTranslator.getInstance().getString(7, charac.getTypeId(), new Object[0]);
            charac.setName(name);
            final OsamodasSymbiotRenameCreatureMessage msg = new OsamodasSymbiotRenameCreatureMessage();
            msg.setCreatureIndex(index);
            msg.setCreatureName(name);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(SymbiotView.getInstance(), SymbiotView.FIELDS);
    }
    
    @Override
    public void onSymbiotReleaseCreature(final byte index) {
        final OsamodasSymbiotReleaseCreatureMessage netMessage = new OsamodasSymbiotReleaseCreatureMessage();
        netMessage.setCreatureId(index);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        PropertiesProvider.getInstance().firePropertyValueChanged(SymbiotView.getInstance(), SymbiotView.FIELDS);
    }
    
    @Override
    public void onSymbiotReset() {
        if (this.m_symbiot != null) {
            for (byte index = 0; index < 5; ++index) {
                final BasicInvocationCharacteristics charac = this.m_symbiot.getCreatureParametersFromIndex(index);
                if (charac == null) {
                    this.onSymbiotReleaseCreature(index);
                }
                else {
                    this.onSymbiotAddCreature(index);
                }
            }
        }
        SymbiotView.getInstance().setSymbiot(this.m_symbiot);
        PropertiesProvider.getInstance().setPropertyValue("osamodasSymbiot", SymbiotView.getInstance());
    }
    
    @Override
    public byte getMaximumSeducableCreatures() {
        return this.m_maximumSeducableCreatures;
    }
    
    @Override
    public void setMaximumSeducableCreatures(final byte newMax) {
        this.m_maximumSeducableCreatures = newMax;
        this.m_symbiot.onMaximumSeducableCreatureChange();
        PropertiesProvider.getInstance().firePropertyValueChanged(SymbiotView.getInstance(), SymbiotView.FIELDS);
    }
}
