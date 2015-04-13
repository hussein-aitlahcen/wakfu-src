package com.ankamagames.wakfu.client.core.game.newCraft;

import gnu.trove.*;

public class NewIngredientManager
{
    public static final NewIngredientManager INSTANCE;
    private final TIntObjectHashMap<TIntHashSet> m_ingredientsToCrafts;
    
    private NewIngredientManager() {
        super();
        this.m_ingredientsToCrafts = new TIntObjectHashMap<TIntHashSet>();
    }
    
    public void addIngredientTo(final int itemId, final int craftId) {
        TIntHashSet set = this.m_ingredientsToCrafts.get(itemId);
        if (set == null) {
            set = new TIntHashSet();
            this.m_ingredientsToCrafts.put(itemId, set);
        }
        set.add(craftId);
    }
    
    public TIntHashSet getCraftsUsing(final int itemId) {
        return this.m_ingredientsToCrafts.get(itemId);
    }
    
    static {
        INSTANCE = new NewIngredientManager();
    }
}
