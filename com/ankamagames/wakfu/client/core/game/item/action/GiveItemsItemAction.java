package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class GiveItemsItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_itemReferenceId;
    private short m_quantity;
    
    public GiveItemsItemAction(final int id) {
        super(id);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            GiveItemsItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    @Override
    public void parseParameters(final String[] params) {
        this.m_itemReferenceId = Integer.parseInt(params[0]);
        this.m_quantity = Short.parseShort(params[1]);
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.GIVE_ITEMS;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GiveItemsItemAction.class);
    }
}
