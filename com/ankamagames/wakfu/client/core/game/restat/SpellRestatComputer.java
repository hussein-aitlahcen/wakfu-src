package com.ankamagames.wakfu.client.core.game.restat;

import com.ankamagames.wakfu.common.game.effect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public interface SpellRestatComputer
{
    RestatType getRestatType();
    
    boolean isElementConcerned(@NotNull Elements p0);
    
    long getTotalXpToDistribute();
    
    long getTotalXpToDistribute(@Nullable Elements p0);
    
    long getTotalAlreadyDistributeXp();
    
    long getAlreadyDistributedXp(@Nullable Elements p0);
    
    long getRemainingXpToDistribute();
    
    long getRemainingXpToDistribute(@Nullable Elements p0);
    
    boolean isSpellActivated(@NotNull Elements p0, int p1);
    
    long getXpNeededToIncrementSpellLevel(@NotNull Elements p0, int p1);
    
    boolean canIncrementSpellLevel(@NotNull Elements p0, int p1);
    
    boolean incrementSpellLevel(@NotNull Elements p0, int p1);
    
    long getXpGainedByDecrementingSpellLevel(@NotNull Elements p0, int p1);
    
    boolean canDecrementSpellLevel(@NotNull Elements p0, int p1);
    
    boolean decrementSpellLevel(@NotNull Elements p0, int p1);
    
    boolean canValidateRestat();
    
    boolean validateRestat();
    
    void reset();
    
    @Nullable
    SpellLevel getSpellLevel(@NotNull Elements p0, int p1);
    
    public enum RestatType
    {
        GLOBAL, 
        PER_ELEMENT;
    }
}
