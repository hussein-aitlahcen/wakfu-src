package com.ankamagames.wakfu.common.game.aptitude;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;

public class Aptitude implements InventoryContent, WakfuEffectContainer, FieldProvider, Comparable<Aptitude>
{
    public static final byte LEVEL_UP_RESULT_OK = 0;
    public static final byte LEVEL_UP_RESULT_ERROR_WRONG_APTITUDE = 1;
    public static final byte LEVEL_UP_RESULT_ERROR_NOT_ENOUGH_POINTS = 2;
    public static final byte LEVEL_UP_RESULT_ERROR_ALREADY_MAX = 3;
    public static final byte LEVEL_UP_RESULT_ERROR_NOT_HIGH_ENOUGH_LEVEL = 4;
    protected static final Logger m_logger;
    private static final Iterator<WakfuEffect> EMPTY_EFFECT_ITERATOR;
    protected final ReferenceAptitude m_referenceAptitude;
    protected short m_level;
    protected short m_wonLevel;
    
    public Aptitude(final ReferenceAptitude referenceAptitude) {
        super();
        assert referenceAptitude != null;
        this.m_referenceAptitude = referenceAptitude;
    }
    
    @Override
    public final short getLevel() {
        return this.m_level;
    }
    
    public short getWonLevel() {
        return this.m_wonLevel;
    }
    
    public final short getMaxLevel() {
        return this.m_referenceAptitude.getMaxLevel();
    }
    
    public final void setLevel(final short level) {
        this.m_level = level;
        if (AptitudeComposer.fieldProvider() != null) {
            AptitudeComposer.fieldProvider().fireLevelChanged(this);
        }
    }
    
    public ReferenceAptitude getReferenceAptitude() {
        return this.m_referenceAptitude;
    }
    
    @Override
    public final void release() {
    }
    
    @Override
    public final long getUniqueId() {
        return this.getReferenceId();
    }
    
    @Override
    public final int getReferenceId() {
        return this.m_referenceAptitude.getReferenceId();
    }
    
    @Override
    public final short getQuantity() {
        return 1;
    }
    
    @Override
    public final void setQuantity(final short quantity) {
        throw new UnsupportedOperationException("Pas de quantit\u00e9 sur les aptitudes");
    }
    
    @Override
    public final void updateQuantity(final short quantityUpdate) {
        throw new UnsupportedOperationException("Pas de quantit\u00e9 sur les aptitudes");
    }
    
    @Override
    public final boolean canStackWith(final InventoryContent inv) {
        return false;
    }
    
    @Override
    public final short getStackMaximumHeight() {
        return 1;
    }
    
    @Override
    public final InventoryContent getCopy(final boolean pooled) {
        throw new UnsupportedOperationException("Pas de clone sur les aptitudes");
    }
    
    @Override
    public final InventoryContent getClone() {
        throw new UnsupportedOperationException("Pas de clone sur les aptitudes");
    }
    
    @Override
    public final boolean shouldBeSerialized() {
        return true;
    }
    
    @Override
    public short getAggroWeight() {
        return 0;
    }
    
    @Override
    public short getAllyEfficacity() {
        return 0;
    }
    
    @Override
    public short getFoeEfficacity() {
        return 0;
    }
    
    @Override
    public int getContainerType() {
        return 17;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.getUniqueId();
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return this.getEffectsForLevel(this.getLevel()).iterator();
    }
    
    public List<WakfuEffect> getEffectsForLevel(final short level) {
        if (level < 0 || level > this.m_referenceAptitude.getMaxLevel()) {
            Aptitude.m_logger.error((Object)("Pas d'effets d\u00e9finis pour l'aptitude " + this.m_referenceAptitude + " au level " + level));
            return Collections.emptyList();
        }
        final List<WakfuEffect> effects = this.m_referenceAptitude.getEffects();
        final List<WakfuEffect> effectsForLevel = new ArrayList<WakfuEffect>(effects.size());
        for (final WakfuEffect effect : effects) {
            if (level >= effect.getContainerMinLevel() && level <= effect.getContainerMaxLevel()) {
                effectsForLevel.add(effect);
            }
        }
        return effectsForLevel;
    }
    
    public boolean isMaxedOut() {
        return this.getLevel() >= this.m_referenceAptitude.getMaxLevel();
    }
    
    public int costForNextLevel(final BasicCharacterInfo characterInfo) {
        if (!this.isMaxedOut()) {
            return this.getPointsForLevel((short)(this.getLevel() + 1), characterInfo);
        }
        return -1;
    }
    
    public int getPointsForLevel(final short level, final BasicCharacterInfo character) {
        if (AptitudeAlternativeCostConstants.shouldUseReducedCost(character, this.getReferenceId(), level)) {
            return 1;
        }
        return this.m_referenceAptitude.getPointsForLevel(level);
    }
    
    public boolean toRaw(final RawAptitude raw) {
        raw.referenceId = (short)this.getReferenceId();
        raw.level = this.m_level;
        raw.wonLevel = this.m_wonLevel;
        return true;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        return AptitudeComposer.fieldProvider().getFieldValue(this, fieldName);
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public int compareTo(final Aptitude o) {
        return this.getReferenceAptitude().compareTo(o.getReferenceAptitude());
    }
    
    public void setWonLevel(final short wonLevel) {
        this.m_wonLevel = wonLevel;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Aptitude.class);
        EMPTY_EFFECT_ITERATOR = new EmptyIterator<WakfuEffect>();
    }
}
