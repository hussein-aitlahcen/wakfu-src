package com.ankamagames.wakfu.common.game.craft;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import gnu.trove.*;
import java.util.*;

public class CraftHandler
{
    private static final Logger m_logger;
    private final IntObjectLightWeightMap<Craft> m_crafts;
    private final ArrayList<CraftListener> m_listeners;
    
    public CraftHandler() {
        super();
        this.m_crafts = new IntObjectLightWeightMap<Craft>();
        this.m_listeners = new ArrayList<CraftListener>();
    }
    
    public CraftResult learnCraft(@NotNull final ReferenceCraft refCraft) {
        if (this.m_crafts.contains(refCraft.getId())) {
            return CraftResult.ALREADY_LEARNED;
        }
        this.m_crafts.put(refCraft.getId(), Craft.checkout(refCraft.getId()));
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onCraftLearned(refCraft);
        }
        return CraftResult.NO_ERROR;
    }
    
    public CraftResult unLearnCraft(@NotNull final ReferenceCraft refCraft) {
        final Craft craft = this.m_crafts.remove(refCraft.getId());
        if (craft == null) {
            return CraftResult.UNKNOWN_CRAFT;
        }
        craft.release();
        return CraftResult.NO_ERROR;
    }
    
    public CraftResult learnRecipe(final int craftId, final int recipeId) {
        final Craft craft = this.m_crafts.get(craftId);
        if (craft == null) {
            return CraftResult.UNLEARN_CRAFT;
        }
        final boolean recipeAdded = craft.addKnownRecipe(recipeId);
        if (!recipeAdded) {
            return CraftResult.NO_ERROR;
        }
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onRecipeLearned(craftId, recipeId);
        }
        return CraftResult.NO_ERROR;
    }
    
    public long addXp(final int craftId, final long xp) {
        if (craftId == 0) {
            return 0L;
        }
        if (this.getLevel(craftId) >= 100) {
            return this.getXp(craftId);
        }
        final Craft craft = this.m_crafts.get(craftId);
        if (craft == null) {
            CraftHandler.m_logger.error((Object)("Tentative d'ajout d'XP \u00e0 un m\u00e9tier inconnu " + craftId), (Throwable)new IllegalArgumentException());
            return 0L;
        }
        final long old = craft.getXp();
        final long newXp = craft.addQuickXp(xp);
        if (old != newXp) {
            for (int i = 0; i < this.m_listeners.size(); ++i) {
                this.m_listeners.get(i).onCraftXpGained(craftId, xp);
            }
        }
        return newXp;
    }
    
    public void onPlantationSuccess(final int craftId) {
        if (craftId == 0) {
            return;
        }
        this.m_crafts.get(craftId).incPlantationCounter();
    }
    
    public void onCollectSuccess(final int craftId, final boolean isDestructive) {
        if (craftId == 0) {
            return;
        }
        this.m_crafts.get(craftId).incCollectCounter(isDestructive);
    }
    
    public void onRecipeSuccess(final int craftId) {
        if (craftId == 0) {
            return;
        }
        this.m_crafts.get(craftId).incRecipeCounter();
    }
    
    public boolean contains(final int craftId) {
        return craftId == 0 || this.m_crafts.contains(craftId);
    }
    
    public short getLevel(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (short)((craft != null) ? CraftXPUtil.getCurrentLevel(craft.getXp()) : 0);
    }
    
    public double getPercentLevelForXp(final int craftId, final long xp) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? CraftXPUtil.getCurrentPercentLevel(xp) : 0.0;
    }
    
    public double getCurrentPercentLevel(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? CraftXPUtil.getCurrentPercentLevel(craft.getXp()) : 0.0;
    }
    
    public long getNextInXp(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? CraftXPUtil.getNextLevelRemainingXp(craft.getXp()) : 0L;
    }
    
    public long getXp(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? craft.getXp() : 0L;
    }
    
    public int getPlantationCounter(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? craft.getPlantationCounter() : 0;
    }
    
    public int getNonDestructiveCollectCounter(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? craft.getNonDestructiveCollectCounter() : 0;
    }
    
    public int getDestructiveCollectCounter(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? craft.getDestructiveCollectCounter() : 0;
    }
    
    public int getRecipeCounter(final int craftId) {
        final Craft craft = this.m_crafts.get(craftId);
        return (craft != null) ? craft.getRecipeCounter() : 0;
    }
    
    @Deprecated
    public int[] getKnownCrafts() {
        return this.m_crafts.keys();
    }
    
    public boolean isKnownRecipe(final int craftId, final int recipeId) {
        final Craft craft = this.m_crafts.get(craftId);
        return craft != null && craft.isKnownRecipe(recipeId);
    }
    
    public void addListener(final CraftListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final CraftListener craftListener) {
        this.m_listeners.remove(craftListener);
    }
    
    public void clear() {
        for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
            this.m_crafts.getQuickValue(i).release();
        }
        this.m_crafts.clear();
    }
    
    public void toRaw(final CharacterSerializedCraft part) {
        for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
            final Craft craft = this.m_crafts.getQuickValue(i);
            final CharacterSerializedCraft.RawCraft raw = craft.toRaw();
            if (raw != null) {
                part.rawCrafts.add(raw);
            }
        }
    }
    
    public void fromRaw(final CharacterSerializedCraft part) {
        final IntLightWeightSet doneIds = new IntLightWeightSet(part.rawCrafts.size());
        final Iterator<CharacterSerializedCraft.RawCraft> it = part.rawCrafts.iterator();
        while (it.hasNext()) {
            final CharacterSerializedCraft.RawCraft rawCraft = it.next();
            final ReferenceCraft refCraft = CraftManager.INSTANCE.getCraft(rawCraft.refCraftId);
            if (refCraft == null) {
                CraftHandler.m_logger.error((Object)("[SERIALISATION] Chargement d'un m\u00e9tier inconnu : " + rawCraft.refCraftId), (Throwable)new IllegalArgumentException());
                it.remove();
            }
            else {
                this.m_crafts.put(rawCraft.refCraftId, Craft.fromRaw(rawCraft));
                doneIds.add(rawCraft.refCraftId);
            }
        }
        CraftManager.INSTANCE.foreachInnateCraft(new TObjectProcedure<ReferenceCraft>() {
            @Override
            public boolean execute(final ReferenceCraft object) {
                if (!doneIds.contains(object.getId())) {
                    CraftHandler.this.m_crafts.put(object.getId(), CraftHandler.createCraft(object));
                }
                return true;
            }
        });
    }
    
    public static Craft createCraft(final ReferenceCraft referenceCraft) {
        if (referenceCraft.isConceptualCraft()) {
            return CraftManager.INSTANCE.getCraftConcept(referenceCraft.getId());
        }
        return Craft.checkout(referenceCraft.getId());
    }
    
    static {
        m_logger = Logger.getLogger((Class)CraftHandler.class);
    }
}
