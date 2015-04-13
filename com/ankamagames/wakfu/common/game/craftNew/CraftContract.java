package com.ankamagames.wakfu.common.game.craftNew;

import java.util.*;
import com.ankamagames.wakfu.common.game.craftNew.constant.*;
import gnu.trove.*;

public class CraftContract
{
    private final long m_uniqueId;
    private final int m_recipeId;
    private final long m_requesterId;
    private final long m_requestedId;
    private final LinkedList<CraftContractComment> m_comments;
    private long m_kamas;
    private final TByteObjectHashMap<CraftContractRecipeComponent> m_components;
    private TIntObjectHashMap<CraftContractRecipeComponent> m_ingredients;
    private ContractState m_contractState;
    private CraftTask m_craftTask;
    
    public CraftContract(final long uniqueId, final int recipeId, final long requesterId, final long requestedId) {
        super();
        this.m_comments = new LinkedList<CraftContractComment>();
        this.m_components = new TByteObjectHashMap<CraftContractRecipeComponent>();
        this.m_ingredients = new TIntObjectHashMap<CraftContractRecipeComponent>();
        this.m_uniqueId = uniqueId;
        this.m_recipeId = recipeId;
        this.m_requesterId = requesterId;
        this.m_requestedId = requestedId;
        this.m_contractState = ContractState.SUBMITTED;
    }
    
    public long getUniqueId() {
        return this.m_uniqueId;
    }
    
    public int getRecipeId() {
        return this.m_recipeId;
    }
    
    public long getRequesterId() {
        return this.m_requesterId;
    }
    
    public long getRequestedId() {
        return this.m_requestedId;
    }
    
    public long getKamas() {
        return this.m_kamas;
    }
    
    public LinkedList<CraftContractComment> getComments() {
        return this.m_comments;
    }
    
    public void setKamas(final long kamas) {
        this.m_kamas = kamas;
    }
    
    public CraftContractComment getLastComment() {
        return this.m_comments.getFirst();
    }
    
    public void putComment(final CraftContractComment comment) {
        if (this.m_comments.size() >= 4) {
            this.m_comments.pop();
        }
        this.m_comments.addLast(comment);
    }
    
    public boolean containsComponent(final byte position) {
        return this.m_components.containsKey(position);
    }
    
    public CraftContractRecipeComponent getComponent(final byte position) {
        return this.m_components.get(position);
    }
    
    public void addComponent(final CraftContractRecipeComponent craftComponent) {
        this.m_components.put(craftComponent.getPosition(), craftComponent);
    }
    
    public CraftTask getCraftTask() {
        return this.m_craftTask;
    }
    
    public void setCraftTask(final CraftTask craftTask) {
        this.m_craftTask = craftTask;
    }
    
    public boolean containsIngredient(final int referenceItemId) {
        return this.m_ingredients.containsKey(referenceItemId);
    }
    
    public void putIngredient(final CraftContractRecipeComponent component) {
        this.m_ingredients.put(component.getItemReferenceId(), component);
    }
    
    public CraftContractRecipeComponent getIngredient(final int referenceItemId) {
        return this.m_ingredients.get(referenceItemId);
    }
    
    public boolean isSelfContract() {
        return this.m_requestedId == -1L;
    }
    
    public ContractState getContractState() {
        return this.m_contractState;
    }
    
    public void setContractState(final ContractState contractState) {
        this.m_contractState = contractState;
    }
    
    public void updateCraftTask() {
    }
    
    public byte[] serializeCraftContract() {
        return CraftSerializer.serializeCraftContract(this);
    }
    
    public void forEachComponent(final TByteObjectProcedure procedure) {
        this.m_components.forEachEntry(procedure);
    }
}
