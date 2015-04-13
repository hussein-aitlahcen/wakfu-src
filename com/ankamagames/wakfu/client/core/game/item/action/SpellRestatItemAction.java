package com.ankamagames.wakfu.client.core.game.item.action;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class SpellRestatItemAction extends AbstractClientItemAction
{
    public SpellRestatItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String... params) {
    }
    
    @Override
    public boolean run(final Item item) {
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.SPELL_RESTAT;
    }
}
