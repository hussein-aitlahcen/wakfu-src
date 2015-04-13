package com.ankamagames.wakfu.client.core.game.item.listener;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ItemQuantityChangeListener implements com.ankamagames.wakfu.common.game.item.ItemQuantityChangeListener
{
    private static final ItemQuantityChangeListener m_instance;
    
    public static ItemQuantityChangeListener getInstance() {
        return ItemQuantityChangeListener.m_instance;
    }
    
    @Override
    public void onQuantityChanged(final Item item) {
        PropertiesProvider.getInstance().firePropertyValueChanged(item, "quantity");
        final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
        if (lpc != null) {
            lpc.getShortcutBarManager().onQuantityChanged(item);
        }
    }
    
    static {
        m_instance = new ItemQuantityChangeListener();
    }
}
