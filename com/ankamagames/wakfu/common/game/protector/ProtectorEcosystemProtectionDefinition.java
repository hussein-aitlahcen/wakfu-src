package com.ankamagames.wakfu.common.game.protector;

import gnu.trove.*;

public class ProtectorEcosystemProtectionDefinition
{
    private final TIntIntHashMap m_monsterProtectionCosts;
    private final TIntIntHashMap m_monsterReintroductionCosts;
    private final TIntIntHashMap m_monsterReintroductionItemIds;
    private final TIntShortHashMap m_monsterReintroductionItemQty;
    private final TIntIntHashMap m_resourceProtectionCosts;
    private final TIntIntHashMap m_resourceReintroductionCosts;
    private final TIntIntHashMap m_resourceReintroductionItemIds;
    private final TIntShortHashMap m_resourceReintroductionItemQty;
    
    public ProtectorEcosystemProtectionDefinition() {
        super();
        this.m_monsterProtectionCosts = new TIntIntHashMap();
        this.m_monsterReintroductionCosts = new TIntIntHashMap();
        this.m_monsterReintroductionItemIds = new TIntIntHashMap();
        this.m_monsterReintroductionItemQty = new TIntShortHashMap();
        this.m_resourceProtectionCosts = new TIntIntHashMap();
        this.m_resourceReintroductionCosts = new TIntIntHashMap();
        this.m_resourceReintroductionItemIds = new TIntIntHashMap();
        this.m_resourceReintroductionItemQty = new TIntShortHashMap();
    }
    
    public void addMonsterProtectionDefinition(final int monsterFamilyId, final int protectionCost, final int reintroductionCost, final int reintroductionItemId, final short reintroductionItemQty) {
        this.m_monsterProtectionCosts.put(monsterFamilyId, protectionCost);
        this.m_monsterReintroductionCosts.put(monsterFamilyId, reintroductionCost);
        this.m_monsterReintroductionItemIds.put(monsterFamilyId, reintroductionItemId);
        this.m_monsterReintroductionItemQty.put(monsterFamilyId, reintroductionItemQty);
    }
    
    public void addResourceProtectionDefinition(final int resourceFamilyId, final int protectionCost, final int reintroductionCost, final int reintroductionItemId, final short reintroductionItemQty) {
        this.m_resourceProtectionCosts.put(resourceFamilyId, protectionCost);
        this.m_resourceReintroductionCosts.put(resourceFamilyId, reintroductionCost);
        this.m_resourceReintroductionItemIds.put(resourceFamilyId, reintroductionItemId);
        this.m_resourceReintroductionItemQty.put(resourceFamilyId, reintroductionItemQty);
    }
    
    public int getMonsterProtectionCost(final int monsterFamilyId) {
        return this.m_monsterProtectionCosts.get(monsterFamilyId);
    }
    
    public int getMonsterReintroductionCost(final int monsterFamilyId) {
        return this.m_monsterReintroductionCosts.get(monsterFamilyId);
    }
    
    public int getMonsterReintroductionItemId(final int monsterFamilyId) {
        return this.m_monsterReintroductionItemIds.get(monsterFamilyId);
    }
    
    public short getMonsterReintroductionItemQty(final int monsterFamilyId) {
        return this.m_monsterReintroductionItemQty.get(monsterFamilyId);
    }
    
    public int getResourceProtectionCost(final int resourceFamilyId) {
        return this.m_resourceProtectionCosts.get(resourceFamilyId);
    }
    
    public int getResourceReintroductionCost(final int monsterFamilyId) {
        return this.m_resourceReintroductionCosts.get(monsterFamilyId);
    }
    
    public int getResourceReintroductionItemId(final int monsterFamilyId) {
        return this.m_resourceReintroductionItemIds.get(monsterFamilyId);
    }
    
    public short getResourceReintroductionItemQty(final int monsterFamilyId) {
        return this.m_resourceReintroductionItemQty.get(monsterFamilyId);
    }
    
    public int[] getProtectibleMonsters() {
        return this.m_monsterProtectionCosts.keys();
    }
    
    public int[] getProtectibleResources() {
        return this.m_resourceProtectionCosts.keys();
    }
}
