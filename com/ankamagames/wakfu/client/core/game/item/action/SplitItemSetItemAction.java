package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class SplitItemSetItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private short m_itemSetId;
    
    public SplitItemSetItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        this.m_itemSetId = Short.parseShort(params[0]);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = HeroUtils.getHeroWithItemUidInBags(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), item.getUniqueId());
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            SplitItemSetItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        final ItemSet set = ItemSetManager.getInstance().getItemSet(this.m_itemSetId);
        if (set == null) {
            SplitItemSetItemAction.m_logger.warn((Object)("[ItemAction] SplitItemSetItemAction : la panoplie est inexistante id=" + this.m_itemSetId));
            return false;
        }
        final boolean ok = ItemSetMergeHelper.checkInventoryForSplit(character, set, item);
        if (ok) {
            this.sendRequest(item.getUniqueId());
        }
        else {
            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("error.itemSetSplit.notEnoughSpaceInInventory"));
            chatMessage.setPipeDestination(3);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    public short getItemSetId() {
        return this.m_itemSetId;
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.SPLIT_ITEM_SET;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SplitItemSetItemAction.class);
    }
}
