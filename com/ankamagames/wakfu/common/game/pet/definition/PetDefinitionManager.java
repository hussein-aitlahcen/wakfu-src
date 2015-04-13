package com.ankamagames.wakfu.common.game.pet.definition;

import gnu.trove.*;

public class PetDefinitionManager
{
    public static final PetDefinitionManager INSTANCE;
    private final TIntObjectHashMap<PetDefinition> m_petDefinitions;
    private final TIntObjectHashMap<PetDefinition> m_itemPetMap;
    private TIntHashSet m_equipmentItemIds;
    
    private PetDefinitionManager() {
        super();
        this.m_petDefinitions = new TIntObjectHashMap<PetDefinition>();
        this.m_itemPetMap = new TIntObjectHashMap<PetDefinition>();
    }
    
    public void add(final PetDefinition def, final int itemRefId) {
        this.m_petDefinitions.put(def.getId(), def);
        assert !this.m_itemPetMap.contains(itemRefId);
        this.m_itemPetMap.put(itemRefId, def);
        this.m_equipmentItemIds = null;
    }
    
    public PetDefinition get(final int definitionId) {
        return this.m_petDefinitions.get(definitionId);
    }
    
    public PetDefinition getFromRefItemId(final int refItemId) {
        return this.m_itemPetMap.get(refItemId);
    }
    
    @Override
    public String toString() {
        return "PetDefinitionManager{m_petDefinitions=" + this.m_petDefinitions.size() + '}';
    }
    
    public boolean isPetEquipment(final int itemRefId) {
        if (this.m_equipmentItemIds == null) {
            this.m_equipmentItemIds = this.computeEquipmentsItemIds();
        }
        return this.m_equipmentItemIds.contains(itemRefId);
    }
    
    private TIntHashSet computeEquipmentsItemIds() {
        final TIntHashSet ids = new TIntHashSet();
        this.m_petDefinitions.forEachValue(new TObjectProcedure<PetDefinition>() {
            @Override
            public boolean execute(final PetDefinition object) {
                ids.addAll(object.getEquipmentItemIds());
                return true;
            }
        });
        return ids;
    }
    
    static {
        INSTANCE = new PetDefinitionManager();
    }
}
