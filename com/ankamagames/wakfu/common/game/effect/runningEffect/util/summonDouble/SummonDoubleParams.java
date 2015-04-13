package com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble;

import com.ankamagames.wakfu.common.datas.specific.*;
import org.jetbrains.annotations.*;

public class SummonDoubleParams
{
    public static final short UNLIMITED = -1;
    private short m_breedId;
    private short m_maxSpellsCount;
    private short m_maxElementalSpellsCount;
    private short m_maxSupportSpellsCount;
    private boolean m_isUsingCasterAsModel;
    private boolean m_isDoubleIndependant;
    private boolean m_shouldUseLevelGains;
    private final DoubleInvocationCharacteristics m_defaultCharacInstance;
    private final DoubleSpellsFinder m_spellsFinder;
    
    public SummonDoubleParams(@NotNull final DoubleInvocationCharacteristics defaultInstance, @NotNull final DoubleSpellsFinder spellsFinder) {
        super();
        this.m_defaultCharacInstance = defaultInstance;
        this.m_spellsFinder = spellsFinder;
        this.m_breedId = 0;
        this.m_maxSpellsCount = 10;
        this.m_maxSupportSpellsCount = -1;
        this.m_maxElementalSpellsCount = -1;
        this.m_isUsingCasterAsModel = true;
        this.m_isDoubleIndependant = false;
        this.m_shouldUseLevelGains = true;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public short getMaxElementalSpellsCount() {
        return (this.m_maxElementalSpellsCount != -1) ? this.m_maxElementalSpellsCount : this.m_maxSpellsCount;
    }
    
    public short getMaxSupportSpellsCount() {
        return (this.m_maxSupportSpellsCount != -1) ? this.m_maxSupportSpellsCount : this.m_maxSpellsCount;
    }
    
    public short getMaxSpellsCount() {
        return this.m_maxSpellsCount;
    }
    
    public boolean isUsingCasterAsModel() {
        return this.m_isUsingCasterAsModel;
    }
    
    public boolean isDoubleIndependant() {
        return this.m_isDoubleIndependant;
    }
    
    public boolean shouldUseLevelGains() {
        return this.m_shouldUseLevelGains;
    }
    
    public DoubleInvocationCharacteristics getDefaultCharacteristicsInstance() {
        return this.m_defaultCharacInstance;
    }
    
    public DoubleSpellsFinder getSpellsFinder() {
        return this.m_spellsFinder;
    }
    
    public void setBreedId(final short breedId) {
        this.m_breedId = breedId;
    }
    
    public void setMaxSpellsCount(final short maxTotal) {
        this.m_maxSpellsCount = maxTotal;
    }
    
    public void setMaxSpellsCount(final short maxTotal, final short maxElemental, final short maxSupport) {
        this.m_maxSpellsCount = maxTotal;
        this.m_maxElementalSpellsCount = maxElemental;
        this.m_maxSupportSpellsCount = maxSupport;
    }
    
    public void setUsingCasterAsModel(final boolean usingCasterAsModel) {
        this.m_isUsingCasterAsModel = usingCasterAsModel;
    }
    
    public void setDoubleIndependant(final boolean doubleIndependant) {
        this.m_isDoubleIndependant = doubleIndependant;
    }
    
    public void setShouldUseLevelGains(final boolean shouldUseLevelGains) {
        this.m_shouldUseLevelGains = shouldUseLevelGains;
    }
}
