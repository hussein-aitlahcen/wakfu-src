package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.pet.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.pet.change.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.companion.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class NetPetFrame extends MessageRunnerFrame
{
    private static final Logger m_logger;
    public static final NetPetFrame INSTANCE;
    
    private NetPetFrame() {
        super(new MessageRunner[] { new PetChangeMessageRunner() });
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetPetFrame.class);
        INSTANCE = new NetPetFrame();
    }
    
    private static class PetChangeMessageRunner implements MessageRunner<PetChangeMessage>
    {
        @Override
        public boolean run(final PetChangeMessage msg) {
            final long itemId = msg.getItemId();
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            Item item = player.getFromEquipmentOrInventory(itemId);
            final long clientId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
            final CompanionModel companionHoldingItem = CompanionManager.INSTANCE.getCompanionHoldingItem(clientId, itemId);
            if (companionHoldingItem != null) {
                item = ((ArrayInventoryWithoutCheck<Item, R>)companionHoldingItem.getItemEquipment()).getWithUniqueId(itemId);
            }
            final PetController controller = new PetController(item);
            final Iterator<PetChange> it = msg.changesIterator();
            while (it.hasNext()) {
                it.next().compute(controller);
            }
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15982;
        }
    }
}
