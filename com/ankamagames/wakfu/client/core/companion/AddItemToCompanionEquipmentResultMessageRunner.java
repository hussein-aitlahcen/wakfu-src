package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.companion.equipment.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.item.operation.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

final class AddItemToCompanionEquipmentResultMessageRunner implements MessageRunner<AddItemToCompanionEquipmentResultMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final AddItemToCompanionEquipmentResultMessage msg) {
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        final long companionId = msg.getCompanionId();
        final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(localAccount.getAccountId(), companionId);
        if (companion == null) {
            AddItemToCompanionEquipmentResultMessageRunner.m_logger.error((Object)("Impossible de modifier l'inventaire du compagnon " + companionId));
            return false;
        }
        Item removedItem = null;
        try {
            final CompanionController controller = new CompanionController(companion);
            final ItemEquipment itemEquipment = companion.getItemEquipment();
            removedItem = ((ArrayInventoryWithoutCheck<Item, R>)itemEquipment).removeAt(msg.getEquipmentPosition());
            controller.addToEquipment(msg.getEquipmentPosition(), itemEquipment, msg.getItem());
        }
        catch (Exception e) {
            AddItemToCompanionEquipmentResultMessageRunner.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        final long itemUid = msg.getOriginalItemUid();
        final InventoryObserver observer = new MultipleSlotEquipmentHandler();
        final LocalPlayerCharacter localPlayer = HeroUtils.getHeroWithItemUidFromBagsOrEquipment(localAccount.getAccountId(), itemUid);
        localPlayer.getEquipmentInventory().addObserver(observer);
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).removeWithUniqueId(itemUid);
        localPlayer.getEquipmentInventory().removeObserver(observer);
        if (item == null) {
            try {
                BagOperationComputer.applyOperations(msg.getOperations());
            }
            catch (Exception e2) {
                AddItemToCompanionEquipmentResultMessageRunner.m_logger.error((Object)"Exception levee", (Throwable)e2);
            }
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
        UICompanionsEmbeddedFrame.reloadCompanionItemEffects(companion, removedItem);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5556;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddItemToCompanionEquipmentResultMessageRunner.class);
    }
}
