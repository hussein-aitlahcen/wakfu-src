package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.companion.*;
import java.util.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ItemHelper
{
    public static boolean checkWePossessTheItem(final Item item) {
        return ((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).contains(item) || WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(item.getUniqueId());
    }
    
    public static boolean checkCompanionItem(final Item item) {
        final long clientId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
        final List<CompanionModel> companions = CompanionManager.INSTANCE.getCompanions(clientId);
        for (final CompanionModel companion : companions) {
            if (((ArrayInventoryWithoutCheck<Item, R>)companion.getItemEquipment()).contains(item)) {
                return true;
            }
        }
        return false;
    }
    
    public static void requestRepack() {
        requestRepack((byte)(-1));
    }
    
    public static void requestRepack(final byte bagId) {
        final InventoryRepackRequestMessage msg = new InventoryRepackRequestMessage(bagId, new short[0]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        networkEntity.sendMessage(msg);
    }
}
