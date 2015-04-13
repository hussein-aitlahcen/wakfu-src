package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.change.quest.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.change.cosmetics.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.inventory.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

public final class NetInventoryFrame extends MessageRunnerFrame
{
    public static final NetInventoryFrame INSTANCE;
    
    private NetInventoryFrame() {
        super(new MessageRunner[] { new QuestInventoryChangeMessageRunner(), new TemporaryInventoryChangeMessageRunner(), new CosmeticsInventoryChangeMessageRunner() });
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
        INSTANCE = new NetInventoryFrame();
    }
    
    private static class QuestInventoryChangeMessageRunner implements MessageRunner<QuestInventoryChangeMessage>
    {
        @Override
        public boolean run(final QuestInventoryChangeMessage msg) {
            final LocalPlayerCharacter player = HeroesManager.INSTANCE.getHero(msg.getCharacterId());
            final QuestInventory inventory = (QuestInventory)player.getInventory(InventoryType.QUEST);
            final QuestInventoryController controller = new QuestInventoryController(inventory);
            final Iterator<QuestInventoryChange> it = msg.changesIterator();
            while (it.hasNext()) {
                it.next().compute(controller);
            }
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15998;
        }
    }
    
    private static class CosmeticsInventoryChangeMessageRunner implements MessageRunner<CosmeticsInventoryChangeMessage>
    {
        @Override
        public boolean run(final CosmeticsInventoryChangeMessage msg) {
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            final CosmeticsInventory inventory = (CosmeticsInventory)player.getInventory(msg.getInventoryType());
            final CosmeticsInventoryController controller = new CosmeticsInventoryController(inventory);
            final Iterator<CosmeticsInventoryChange> it = msg.changesIterator();
            while (it.hasNext()) {
                it.next().compute(controller);
            }
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15997;
        }
    }
    
    private static class TemporaryInventoryChangeMessageRunner implements MessageRunner<TemporaryInventoryChangeMessage>
    {
        @Override
        public boolean run(final TemporaryInventoryChangeMessage msg) {
            final LocalPlayerCharacter player = HeroesManager.INSTANCE.getHero(msg.getCharacterId());
            final TemporaryInventory inventory = (TemporaryInventory)player.getInventory(InventoryType.TEMPORARY_INVENTORY);
            final TemporaryInventoryController controller = new ClientTemporaryInventoryController(inventory);
            final Iterator<TemporaryInventoryChange> it = msg.changesIterator();
            while (it.hasNext()) {
                it.next().compute(controller);
            }
            UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15999;
        }
    }
}
