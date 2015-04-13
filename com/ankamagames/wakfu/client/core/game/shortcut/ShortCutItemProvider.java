package com.ankamagames.wakfu.client.core.game.shortcut;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ShortCutItemProvider implements InventoryContentProvider<ShortCutItem, RawShortcut>
{
    private static final ShortCutItemProvider m_instance;
    
    public static ShortCutItemProvider getInstance() {
        return ShortCutItemProvider.m_instance;
    }
    
    @Override
    public ShortCutItem unSerializeContent(final RawShortcut rawItem) {
        final ShortCutItem shortcut = ShortCutItem.checkOut();
        if (shortcut.fromRaw(rawItem)) {
            return shortcut;
        }
        return null;
    }
    
    static {
        m_instance = new ShortCutItemProvider();
    }
}
