package com.ankamagames.wakfu.common.game.item.validator;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class RecyclableItemValidator implements InventoryContentValidator<Item>
{
    private final BasicCharacterInfo m_player;
    
    public RecyclableItemValidator(final BasicCharacterInfo player) {
        super();
        this.m_player = player;
    }
    
    @Override
    public boolean isValid(final Item content) {
        if (content.isRent()) {
            return false;
        }
        if (content.getReferenceItem().hasItemProperty(ItemProperty.NOT_RECYCLABLE)) {
            return false;
        }
        final SimpleCriterion criterion = content.getReferenceItem().getCriterion(ActionsOnItem.DELETE);
        final boolean recyclable = content.getType().isRecyclable();
        final boolean critOk = criterion == null || criterion.isValid(this.m_player, null, null, this.m_player.getOwnContext());
        return recyclable && critOk;
    }
}
