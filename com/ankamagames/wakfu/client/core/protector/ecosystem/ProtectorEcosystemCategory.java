package com.ankamagames.wakfu.client.core.protector.ecosystem;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.*;

public class ProtectorEcosystemCategory extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String IS_MONSTER = "isMonster";
    public static final String ITEMS = "items";
    public static final String HAS_ITEMS = "hasItems";
    private boolean m_isMonster;
    private final IntObjectLightWeightMap<ProtectorEcosystemElement> m_elements;
    
    public ProtectorEcosystemCategory(final ProtectorBase protector, final boolean monster) {
        super();
        this.m_elements = new IntObjectLightWeightMap<ProtectorEcosystemElement>();
        this.m_isMonster = monster;
        final ProtectorEcosystemProtectionDefinition def = ProtectorEcosystemProtectionManager.INSTANCE.get(protector.getId());
        if (this.m_isMonster) {
            for (final int id : def.getProtectibleMonsters()) {
                this.m_elements.put(id, new MonsterProtectorEcosystemElement(id, def.getMonsterProtectionCost(id), def.getMonsterReintroductionCost(id)));
            }
        }
        else {
            for (final int id : def.getProtectibleResources()) {
                this.m_elements.put(id, new ResourceProtectorEcosystemElement(id, def.getResourceProtectionCost(id), def.getResourceReintroductionCost(id)));
            }
        }
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_isMonster ? WakfuTranslator.getInstance().getString("protector.ecosystem.monster.category") : WakfuTranslator.getInstance().getString("protector.ecosystem.resource.category");
        }
        if (fieldName.equals("isMonster")) {
            return this.m_isMonster;
        }
        if (fieldName.equals("items")) {
            return this.m_elements;
        }
        if (fieldName.equals("hasItems")) {
            return this.m_elements.size() != 0;
        }
        return null;
    }
    
    public ProtectorEcosystemElement getElement(final int id) {
        return this.m_elements.get(id);
    }
}
