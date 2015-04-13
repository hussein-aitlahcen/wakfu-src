package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

final class RemoveItemFromCompanionEquipmentResultMessageRunner implements MessageRunner<RemoveItemFromCompanionEquipmentResultMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final RemoveItemFromCompanionEquipmentResultMessage msg) {
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        final long companionId = msg.getCompanionId();
        final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(localAccount.getAccountId(), companionId);
        if (companion == null) {
            RemoveItemFromCompanionEquipmentResultMessageRunner.m_logger.error((Object)("Impossible de modifier l'inventaire du compagnon " + companionId));
            return false;
        }
        final long itemUid = msg.getItemUid();
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)companion.getItemEquipment()).removeWithUniqueId(itemUid);
        if (item == null) {
            RemoveItemFromCompanionEquipmentResultMessageRunner.m_logger.error((Object)("Aucun item n'a \u00e9t\u00e9 retir\u00e9 " + itemUid));
        }
        final boolean toAdd = UICompanionsEmbeddedFrame.containsCompanionView(companion.getBreedId());
        if (toAdd) {
            final NonPlayerCharacter npc = UICompanionsEmbeddedFrame.createNonPlayerCharacterCompanion(companion);
            final CharacterView characterView = new CharacteristicCompanionView(npc, new CompanionViewShort(companion));
            UICompanionsEmbeddedFrame.addCompanionView(characterView);
            UIEquipmentFrame.getInstance().refreshEquipementSlots();
        }
        else {
            UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
        }
        UICompanionsEmbeddedFrame.reloadCompanionItemEffects(companion, item);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5558;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemoveItemFromCompanionEquipmentResultMessageRunner.class);
    }
}
