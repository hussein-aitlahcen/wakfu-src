package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class SpellLevelProvider implements InventoryContentProvider<SpellLevel, RawSpellLevel>
{
    private final BasicCharacterInfo m_spellLevelUser;
    
    public SpellLevelProvider(final BasicCharacterInfo spellLevelUser) {
        super();
        this.m_spellLevelUser = spellLevelUser;
    }
    
    @Override
    public SpellLevel unSerializeContent(final RawSpellLevel rawItem) {
        SpellLevel level = null;
        switch (rawItem.type) {
            case 1: {
                level = new SpellLevel();
                break;
            }
            case 2: {
                level = new WeaponSkillSpellLevel(this.m_spellLevelUser);
                break;
            }
        }
        if (level != null && level.fromRaw(rawItem)) {
            return level;
        }
        return null;
    }
}
