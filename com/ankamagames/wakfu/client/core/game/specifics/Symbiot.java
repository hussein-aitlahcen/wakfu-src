package com.ankamagames.wakfu.client.core.game.specifics;

import com.ankamagames.wakfu.common.datas.specific.symbiot.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class Symbiot extends AbstractSymbiot
{
    @Override
    public void setCreatureAvailability(final byte index, final boolean availability) {
        super.setCreatureAvailability(index, availability);
        try {
            final SymbiotInvocationCharacteristics invoc = (SymbiotInvocationCharacteristics)this.m_capturedCreatures[index];
            if (invoc != null) {
                invoc.setAvailability(this.isAvailable(index));
            }
        }
        catch (Exception e) {
            Symbiot.m_logger.error((Object)"[SYMBIOT] on demande de modifier un index non valide");
        }
        SymbiotView.getInstance().updateLeaderShipCapacity();
        this.updateAll();
    }
    
    @Override
    public void addCreaturesParametersToIndex(final BasicInvocationCharacteristics characs, final byte index) {
        super.addCreaturesParametersToIndex(characs, index);
        updateCreaturesList();
        SymbiotView.getInstance().onCreatureCaptured();
    }
    
    @Override
    public void removeFromIndex(final byte index) {
        super.removeFromIndex(index);
        updateCreaturesList();
    }
    
    @Override
    public void setCurrentCreatureFromIndex(final byte index) {
        super.setCurrentCreatureFromIndex(index);
        updateSelectedCreature();
    }
    
    @Override
    public void clearAll() {
        super.clearAll();
        updateCreaturesList();
        updateSelectedCreature();
    }
    
    @Override
    public boolean fromRawSymbiot(final RawSymbiot rawSymbiot) {
        final boolean ret = super.fromRawSymbiot(rawSymbiot);
        updateCreaturesList();
        updateSelectedCreature();
        return ret;
    }
    
    private static void updateCreaturesList() {
        SymbiotView.getInstance().updateCreaturesList();
    }
    
    private static void updateSelectedCreature() {
        SymbiotView.getInstance().updateSelectedCreature();
    }
    
    @Override
    public BasicInvocationCharacteristics getNewSpecificInvocationCharacteristics(final short breedId) {
        final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId(breedId);
        final short levelMin = breed.getLevelMin();
        return new SymbiotInvocationCharacteristics(breedId, breed.getName(), breed.getCharacteristicManager().getCharacteristicValue(FighterCharacteristicType.HP, levelMin), 0L, (short)0);
    }
    
    @Override
    protected void initializeBreed() {
        for (final BasicInvocationCharacteristics charac : this.m_capturedCreatures) {
            this.initializeBreed(charac);
        }
    }
    
    @Override
    protected void initializeBreed(final BasicInvocationCharacteristics charac) {
        if (charac != null) {
            charac.setBreed(MonsterBreedManager.getInstance().getBreedFromId(charac.getTypeId()));
        }
    }
    
    public void updateCreatures() {
        try {
            for (final BasicInvocationCharacteristics charac : this.m_capturedCreatures) {
                if (charac != null) {
                    PropertiesProvider.getInstance().firePropertyValueChanged((FieldProvider)charac, SymbiotInvocationCharacteristics.FIELDS);
                }
            }
        }
        catch (Exception e) {
            Symbiot.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    @Override
    public void setCreatureAvailable(final long summonId) {
        super.setCreatureAvailable(summonId);
        SymbiotView.getInstance().updateLeaderShipCapacity();
        this.updateCreatures();
        updateCreaturesList();
    }
    
    @Override
    public void setCurrentCreatureUnavailable() {
        super.setCurrentCreatureUnavailable();
        SymbiotView.getInstance().updateLeaderShipCapacity();
    }
    
    @Override
    public void setCurrentCreatureNextIndex() {
        byte nextIndex = this.m_currentCreatureIndex;
        byte count = 1;
        for (int creaturesSize = this.m_nbCapturedCreatures; count <= creaturesSize; ++count) {
            ++nextIndex;
            if (nextIndex >= creaturesSize) {
                nextIndex -= (byte)creaturesSize;
            }
            if (this.isAvailable(nextIndex)) {
                break;
            }
        }
        final UIMessage message = new UIMessage();
        message.setId(16814);
        message.setByteValue(nextIndex);
        Worker.getInstance().pushMessage(message);
        updateSelectedCreature();
    }
    
    public void updateAll() {
        PropertiesProvider.getInstance().firePropertyValueChanged(SymbiotView.getInstance(), SymbiotView.FIELDS);
    }
    
    @Override
    public void resetFightData() {
        super.resetFightData();
        this.updateCreatures();
        this.updateAll();
    }
}
