package com.ankamagames.wakfu.common.game.item.xp;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public class ItemXpFactory
{
    public static ItemXp create(final ItemXpDefinition def) {
        return new ItemXpModel(def);
    }
    
    public static ItemXp create(final RawItemXp raw) {
        final ItemXp xpModel = new ItemXpModel();
        xpModel.fromRaw(raw);
        return xpModel;
    }
    
    public static ItemXp copy(final ItemXp xp) {
        final ItemXpModel copy = new ItemXpModel(xp.getDefinition());
        copy.setXp(xp.getXp());
        return copy;
    }
}
