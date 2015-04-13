package com.ankamagames.wakfu.common.game.craft;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import gnu.trove.*;

class Craft implements Releasable
{
    private static final Logger m_logger;
    private static final MonitoredPool m_pool;
    private int m_refId;
    private long m_xp;
    private int m_plantationCounter;
    private int m_nonDestructiveCollectCounter;
    private int m_destructiveCollectCounter;
    private int m_recipeCounter;
    private final TIntHashSet m_learnedRecipes;
    
    private Craft() {
        super();
        this.m_learnedRecipes = new TIntHashSet();
    }
    
    protected Craft(final int refId) {
        super();
        this.m_learnedRecipes = new TIntHashSet();
        this.m_refId = refId;
    }
    
    long getXp() {
        return this.m_xp;
    }
    
    public int getPlantationCounter() {
        return this.m_plantationCounter;
    }
    
    void incPlantationCounter() {
        ++this.m_plantationCounter;
    }
    
    public int getDestructiveCollectCounter() {
        return this.m_destructiveCollectCounter;
    }
    
    public int getNonDestructiveCollectCounter() {
        return this.m_nonDestructiveCollectCounter;
    }
    
    void incCollectCounter(final boolean destructive) {
        if (destructive) {
            ++this.m_destructiveCollectCounter;
        }
        else {
            ++this.m_nonDestructiveCollectCounter;
        }
    }
    
    public int getRecipeCounter() {
        return this.m_recipeCounter;
    }
    
    void incRecipeCounter() {
        ++this.m_recipeCounter;
    }
    
    long addQuickXp(final long toAdd) {
        final long newXp = this.m_xp + toAdd;
        if (CraftXPUtil.getCurrentLevel(newXp) >= 100) {
            return this.m_xp = CraftXPUtil.getTotalXPNeeded((short)100);
        }
        return this.m_xp += toAdd;
    }
    
    public boolean addKnownRecipe(final int recipeId) {
        return this.m_learnedRecipes.add(recipeId);
    }
    
    boolean isKnownRecipe(final int recipeId) {
        return this.m_learnedRecipes.contains(recipeId);
    }
    
    static Craft fromRaw(final CharacterSerializedCraft.RawCraft rawCraft) {
        final int craftRefId = rawCraft.refCraftId;
        final Craft craft = checkout(craftRefId);
        craft.m_xp = rawCraft.xp;
        final CraftCounter counter = rawCraft.craftCounter;
        final CraftCounter.PlantationCounter c1 = counter.plantationCounter;
        final CraftCounter.NonDestructiveCollectCounter c2 = counter.nonDestructiveCollectCounter;
        final CraftCounter.DestructiveCollectCounter c3 = counter.destructiveCollectCounter;
        final CraftCounter.RecipeCounter c4 = counter.recipeCounter;
        craft.m_plantationCounter = ((c1 != null) ? c1.counter : 0);
        craft.m_nonDestructiveCollectCounter = ((c2 != null) ? c2.counter : 0);
        craft.m_destructiveCollectCounter = ((c3 != null) ? c3.counter : 0);
        craft.m_recipeCounter = ((c4 != null) ? c4.counter : 0);
        for (int i = 0; i < rawCraft.rawLearnedRecipes.size(); ++i) {
            craft.m_learnedRecipes.add(rawCraft.rawLearnedRecipes.get(i).recipeId);
        }
        return craft;
    }
    
    CharacterSerializedCraft.RawCraft toRaw() {
        final CharacterSerializedCraft.RawCraft rawCraft = new CharacterSerializedCraft.RawCraft();
        rawCraft.refCraftId = this.m_refId;
        rawCraft.xp = this.m_xp;
        final CraftCounter counter = rawCraft.craftCounter;
        if (this.m_plantationCounter > 0) {
            counter.plantationCounter = new CraftCounter.PlantationCounter();
            counter.plantationCounter.counter = this.m_plantationCounter;
        }
        if (this.m_nonDestructiveCollectCounter > 0) {
            counter.nonDestructiveCollectCounter = new CraftCounter.NonDestructiveCollectCounter();
            counter.nonDestructiveCollectCounter.counter = this.m_nonDestructiveCollectCounter;
        }
        if (this.m_destructiveCollectCounter > 0) {
            counter.destructiveCollectCounter = new CraftCounter.DestructiveCollectCounter();
            counter.destructiveCollectCounter.counter = this.m_destructiveCollectCounter;
        }
        if (this.m_recipeCounter > 0) {
            counter.recipeCounter = new CraftCounter.RecipeCounter();
            counter.recipeCounter.counter = this.m_recipeCounter;
        }
        final TIntIterator recipeIt = this.m_learnedRecipes.iterator();
        while (recipeIt.hasNext()) {
            final CharacterSerializedCraft.RawCraft.RawLearnedRecipe rawRecipe = new CharacterSerializedCraft.RawCraft.RawLearnedRecipe();
            rawRecipe.recipeId = recipeIt.next();
            rawCraft.rawLearnedRecipes.add(rawRecipe);
        }
        return rawCraft;
    }
    
    static Craft checkout(final int referenceCraftId) {
        Craft craft;
        try {
            craft = (Craft)Craft.m_pool.borrowObject();
        }
        catch (Exception e) {
            Craft.m_logger.error((Object)("Erreur lors d'un checkout de " + Craft.class), (Throwable)e);
            craft = new Craft();
        }
        craft.m_refId = referenceCraftId;
        return craft;
    }
    
    @Override
    public void release() {
        try {
            Craft.m_pool.returnObject(this);
        }
        catch (Exception e) {
            Craft.m_logger.error((Object)("Erreur lors d'un release de " + Craft.class), (Throwable)e);
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_refId = 0;
        this.m_xp = 0L;
        this.m_plantationCounter = 0;
        this.m_destructiveCollectCounter = 0;
        this.m_nonDestructiveCollectCounter = 0;
        this.m_recipeCounter = 0;
        this.m_learnedRecipes.clear();
    }
    
    @Override
    public void onCheckIn() {
        this.m_refId = -1;
        this.m_xp = -1L;
        this.m_plantationCounter = -1;
        this.m_destructiveCollectCounter = -1;
        this.m_nonDestructiveCollectCounter = -1;
        this.m_recipeCounter = -1;
        this.m_learnedRecipes.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Craft.class);
        m_pool = new MonitoredPool(new ObjectFactory<Craft>() {
            @Override
            public Craft makeObject() {
                return new Craft(null);
            }
        });
    }
}
