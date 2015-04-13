package com.ankamagames.wakfu.common.datas.specific.symbiot;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public abstract class AbstractSymbiot
{
    protected static final Logger m_logger;
    public static final int MAXIMUM_CREATURES = 5;
    private boolean m_needUpdate;
    protected final BasicInvocationCharacteristics[] m_capturedCreatures;
    protected final boolean[] m_availableCreatures;
    private final SymbiotFightData m_symbiotFightData;
    protected byte m_currentCreatureIndex;
    protected byte m_nbCapturedCreatures;
    protected SymbioticCharacter m_owner;
    
    public AbstractSymbiot() {
        super();
        this.m_needUpdate = false;
        this.m_capturedCreatures = new BasicInvocationCharacteristics[5];
        this.m_availableCreatures = new boolean[5];
        this.m_symbiotFightData = new SymbiotFightData();
        this.m_currentCreatureIndex = 0;
    }
    
    public int getMaximumCreatures() {
        final byte maximumSeducableCreatures = this.m_owner.getMaximumSeducableCreatures();
        return Math.min(maximumSeducableCreatures, 5);
    }
    
    public void onMaximumSeducableCreatureChange() {
        for (byte i = 0; i < 5; ++i) {
            this.setCreatureAvailability(i, i < this.m_owner.getMaximumSeducableCreatures());
        }
    }
    
    public boolean canAddCreature() {
        int nb = 0;
        for (final BasicInvocationCharacteristics creature : this.m_capturedCreatures) {
            if (creature != null) {
                ++nb;
            }
        }
        return nb < this.m_owner.getMaximumSeducableCreatures();
    }
    
    public void setOwner(final SymbioticCharacter owner) {
        this.m_owner = owner;
    }
    
    public BasicInvocationCharacteristics getCurrentCreatureParameters() {
        return this.getCreatureParametersFromIndex(this.m_currentCreatureIndex);
    }
    
    public BasicInvocationCharacteristics getCreatureParametersFromIndex(final byte index) {
        if (index < 0 || index >= this.m_capturedCreatures.length) {
            AbstractSymbiot.m_logger.error((Object)("[SYMBIOT] index invalide " + index));
            return null;
        }
        return this.m_capturedCreatures[index];
    }
    
    public byte size() {
        return this.m_nbCapturedCreatures;
    }
    
    public byte getIndexByCreature(final BasicInvocationCharacteristics creature) {
        if (creature == null) {
            return -1;
        }
        for (byte i = 0; i < 5; ++i) {
            if (this.m_capturedCreatures[i] == creature) {
                return i;
            }
        }
        return -1;
    }
    
    public byte getIndexBySummonId(final long id) {
        for (byte i = 0; i < 5; ++i) {
            if (this.m_capturedCreatures[i] != null && this.m_capturedCreatures[i].getSummonId() == id) {
                return i;
            }
        }
        return -1;
    }
    
    public byte getCurrentCreatureIndex() {
        return this.m_currentCreatureIndex;
    }
    
    public void setCurrentCreatureFromIndex(final byte index) {
        this.m_currentCreatureIndex = index;
    }
    
    public byte addCreaturesParameters(final BasicInvocationCharacteristics characs) {
        if (!this.canAddCreature()) {
            return -1;
        }
        byte emptyRow = -1;
        for (byte i = 4; i >= 0; --i) {
            if (this.m_capturedCreatures[i] == null) {
                emptyRow = i;
            }
            else if (this.m_capturedCreatures[i] == characs) {
                return -1;
            }
        }
        if (emptyRow != -1) {
            this.addCreaturesParametersToIndex(characs, emptyRow);
        }
        return emptyRow;
    }
    
    public void addCreaturesParametersToIndex(final BasicInvocationCharacteristics characs, final byte index) {
        if (characs == null) {
            AbstractSymbiot.m_logger.warn((Object)"[SYMBIOT] ajout des param\u00e8tres d'invoc null");
            return;
        }
        AbstractSymbiot.m_logger.info((Object)("[SYMBIOT] ajout des param\u00e8tres d'invoc " + characs.getSummonId() + " \u00e0 l'emplacement " + index));
        try {
            if (this.m_capturedCreatures[index] == null) {
                this.initializeBreed(this.m_capturedCreatures[index] = characs);
                this.m_availableCreatures[index] = true;
            }
            this.m_needUpdate = true;
            if (this.m_nbCapturedCreatures == 0) {
                this.setCurrentCreatureFromIndex(index);
            }
            ++this.m_nbCapturedCreatures;
            if (this.m_owner != null) {
                this.m_owner.onSymbiotAddCreature(index);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            AbstractSymbiot.m_logger.error((Object)"[SYMBIOT] index en dehors du tableau de cible lors de l'ajout");
        }
    }
    
    public void removeFromIndex(final byte index) {
        AbstractSymbiot.m_logger.info((Object)("[Symbiot] on retire l'invoc d'emplacement " + index));
        try {
            this.m_needUpdate = true;
            final BasicInvocationCharacteristics characteristics = this.m_capturedCreatures[index];
            this.m_capturedCreatures[index] = null;
            this.m_availableCreatures[index] = false;
            if (characteristics != null) {
                --this.m_nbCapturedCreatures;
            }
            if (this.m_nbCapturedCreatures == 0) {
                this.setCurrentCreatureFromIndex((byte)(-1));
            }
            else if (index == this.m_currentCreatureIndex) {
                for (byte i = 0; i < 5; ++i) {
                    if (this.m_capturedCreatures[i] != null) {
                        this.setCurrentCreatureFromIndex(i);
                        break;
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            AbstractSymbiot.m_logger.error((Object)"[SYMBIOT] on demande de supprimer une ligne en dehors du tableau de cible");
        }
    }
    
    public void destroyCreaturesOnIndex(final byte index) {
        AbstractSymbiot.m_logger.info((Object)("[Symbiot] on vide les invocs sur l'emplacement " + index));
        try {
            this.removeFromIndex(index);
            if (this.m_owner != null) {
                this.m_owner.onSymbiotReleaseCreature(index);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            AbstractSymbiot.m_logger.error((Object)"[SYMBIOT] on demande de destroy une ligne en dehors du tableau de cible");
        }
    }
    
    public void setCreatureAvailable(final long summonId) {
        this.setCreatureAvailability(this.getIndexBySummonId(summonId), true);
    }
    
    public void setCurrentCreatureUnavailable() {
        this.setCreatureAvailability(this.m_currentCreatureIndex, false);
    }
    
    public void setCreatureAvailability(final byte index, final boolean availability) {
        try {
            if (this.m_capturedCreatures[index] != null && index < this.m_owner.getMaximumSeducableCreatures()) {
                this.m_availableCreatures[index] = availability;
            }
            else if (this.m_capturedCreatures[index] != null) {
                this.m_availableCreatures[index] = false;
            }
            else {
                AbstractSymbiot.m_logger.error((Object)("[SYMBIOT] on demande de rendre une creature accessible mais son index est invalide " + index));
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            AbstractSymbiot.m_logger.error((Object)("[SYMBIOT] on demande rendre une creature accessible mais son index est invalide " + index));
        }
    }
    
    public boolean isAvailable(final byte index) {
        try {
            if (this.m_capturedCreatures[index] != null && index < this.m_owner.getMaximumSeducableCreatures()) {
                return this.m_availableCreatures[index];
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            AbstractSymbiot.m_logger.error((Object)"[SYMBIOT] on demande de supprimer une ligne en dehors du tableau de cible");
        }
        return false;
    }
    
    public void clearAll() {
        this.m_nbCapturedCreatures = 0;
        for (int i = 0; i < 5; ++i) {
            this.m_capturedCreatures[i] = null;
            this.m_availableCreatures[i] = false;
        }
        this.m_currentCreatureIndex = 0;
        this.m_needUpdate = false;
    }
    
    public void onExternalModificationDone() {
        this.m_needUpdate = true;
    }
    
    public boolean needUpdate() {
        return this.m_needUpdate;
    }
    
    public void onUpdateDone() {
        this.m_needUpdate = false;
    }
    
    public boolean toRawSymbiot(final RawSymbiot rawSymbiot) {
        for (byte i = 0; i < 5; ++i) {
            final BasicInvocationCharacteristics capturedCreature = this.m_capturedCreatures[i];
            if (capturedCreature != null) {
                final RawSymbiot.CapturedCreature rawCreature = new RawSymbiot.CapturedCreature();
                rawCreature.capturedCreature.index = i;
                rawCreature.capturedCreature.typeId = capturedCreature.getTypeId();
                rawCreature.capturedCreature.quantity = 1;
                rawCreature.capturedCreature.name = capturedCreature.getName();
                rawSymbiot.capturedCreatures.add(rawCreature);
            }
        }
        rawSymbiot.currentCreatureIndex = this.m_currentCreatureIndex;
        return true;
    }
    
    public boolean fromRawSymbiot(final RawSymbiot rawSymbiot) {
        this.clearAll();
        for (final RawSymbiot.CapturedCreature content : rawSymbiot.capturedCreatures) {
            final BasicInvocationCharacteristics capturedCreature = this.getNewSpecificInvocationCharacteristics(content.capturedCreature.typeId);
            if (capturedCreature == null) {
                continue;
            }
            capturedCreature.setName(content.capturedCreature.name);
            this.m_capturedCreatures[content.capturedCreature.index] = capturedCreature;
            this.m_availableCreatures[content.capturedCreature.index] = true;
            ++this.m_nbCapturedCreatures;
        }
        this.m_currentCreatureIndex = rawSymbiot.currentCreatureIndex;
        this.m_owner.onSymbiotReset();
        this.initializeBreed();
        return true;
    }
    
    protected void initializeBreed() {
        for (final BasicInvocationCharacteristics charac : this.m_capturedCreatures) {
            if (charac != null) {
                this.initializeBreed(charac);
            }
        }
    }
    
    protected abstract void initializeBreed(final BasicInvocationCharacteristics p0);
    
    public abstract BasicInvocationCharacteristics getNewSpecificInvocationCharacteristics(final short p0);
    
    public int[] getHealthList() {
        final int[] list = new int[this.m_capturedCreatures.length];
        for (int i = 0; i < this.m_capturedCreatures.length; ++i) {
            if (this.m_capturedCreatures[i] != null) {
                list[i] = this.m_capturedCreatures[i].getCurrentHp();
            }
            else {
                list[i] = 0;
            }
        }
        return list;
    }
    
    public void onCreatureStartTurn(final BasicCharacterInfo fighter) {
    }
    
    public void incrementTurnPlayedBySummoner() {
        this.m_symbiotFightData.incrementTurnPlayedBySummoner();
    }
    
    public void resetFightData() {
        for (int i = 0; i < this.m_availableCreatures.length; ++i) {
            this.m_availableCreatures[i] = true;
        }
        this.m_symbiotFightData.reset();
    }
    
    public void setCurrentCreatureNextIndex() {
    }
    
    public int getSpaceForCreature(final short breedId) {
        int space = 0;
        for (byte index = 0; index < 5; ++index) {
            if (this.m_capturedCreatures[index] != null) {
                final BasicInvocationCharacteristics creature = this.m_capturedCreatures[index];
                if (creature.getTypeId() == breedId) {
                    ++space;
                }
            }
        }
        for (byte i = (byte)(this.getMaximumCreatures() - 1); i >= 0; --i) {
            if (this.m_capturedCreatures[i] == null) {
                ++space;
            }
        }
        return space;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractSymbiot.class);
    }
}
