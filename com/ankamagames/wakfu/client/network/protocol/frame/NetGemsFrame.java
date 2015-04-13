package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gems.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class NetGemsFrame extends MessageRunnerFrame
{
    static final Logger m_logger;
    public static final NetGemsFrame INSTANCE;
    
    private NetGemsFrame() {
        super(new MessageRunner[] { new GemResultMessageRunner(), new GemRemovedMessageRunner(), new GemMergedMessageRunner(), new GemImprovedMessageRunner() });
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
        m_logger = Logger.getLogger((Class)NetGemsFrame.class);
        INSTANCE = new NetGemsFrame();
    }
    
    private static class GemResultMessageRunner implements MessageRunner<GemResultMessage>
    {
        @Override
        public boolean run(final GemResultMessage msg) {
            final long gemmedItemId = msg.getGemmedItemId();
            final int gemRefItemId = msg.getGemRefItemId();
            final byte index = msg.getIndex();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            boolean itemIsEquiped = false;
            Item item = localPlayer.getBags().getItemFromInventories(gemmedItemId);
            if (item == null) {
                item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getWithUniqueId(gemmedItemId);
                itemIsEquiped = true;
            }
            CompanionModel companionEquiped = null;
            if (item == null) {
                final long clientId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
                companionEquiped = CompanionManager.INSTANCE.getCompanionHoldingItem(clientId, gemmedItemId);
                if (companionEquiped != null) {
                    item = ((ArrayInventoryWithoutCheck<Item, R>)companionEquiped.getItemEquipment()).getWithUniqueId(gemmedItemId);
                }
            }
            if (item == null) {
                NetGemsFrame.m_logger.warn((Object)("On re\u00e7oit un GEM_RESULT_MESSAGE pour l'item " + gemmedItemId + " alors qu'il n'est pas dans notre inventaire ou equipement"));
                return false;
            }
            if (!item.hasGems()) {
                NetGemsFrame.m_logger.warn((Object)("On re\u00e7oit un GEM_RESULT_MESSAGE pour l'item " + item.getUniqueId() + " alors qu'il n'a pas de slots de gemmes"));
                return false;
            }
            final AbstractReferenceItem gem = ReferenceItemManager.getInstance().getReferenceItem(gemRefItemId);
            final Gems gems = item.getGems();
            try {
                gems.equipGem(gem, index);
            }
            catch (GemsException e) {
                NetGemsFrame.m_logger.warn((Object)("Probl\u00e8me \u00e0 l'application de la gem dans l'item " + item.getUniqueId()));
                return false;
            }
            item.resetCache();
            if (itemIsEquiped) {
                WakfuGameEntity.getInstance().getLocalPlayer().reloadItemEffects();
            }
            WakfuGameEntity.getInstance().getLocalPlayer().getBags().updateGemmedView();
            if (companionEquiped != null) {
                UICompanionsEmbeddedFrame.reloadCompanionItemEffects(companionEquiped, null);
                UICompanionsManagementFrame.INSTANCE.reloadItemEffects(companionEquiped);
            }
            UIGemItemsFrame.getInstance().setCurrentItem(item);
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 13102;
        }
    }
    
    private static class GemRemovedMessageRunner implements MessageRunner<GemRemovedMessage>
    {
        @Override
        public boolean run(final GemRemovedMessage msg) {
            final long gemmedItemId = msg.getGemmedItemId();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            boolean itemIsEquiped = false;
            Item item = localPlayer.getBags().getItemFromInventories(gemmedItemId);
            if (item == null) {
                item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getWithUniqueId(gemmedItemId);
                itemIsEquiped = true;
            }
            if (item == null) {
                NetGemsFrame.m_logger.warn((Object)("On re\u00e7oit un GEM_REMOVED_MESSAGE pour l'item " + gemmedItemId + " alors qu'il n'est pas dans notre inventaire ou equipement"));
                this.notifyUI();
                return false;
            }
            if (!item.hasGems()) {
                NetGemsFrame.m_logger.warn((Object)("On re\u00e7oit un GEM_REMOVED_MESSAGE pour l'item " + item.getUniqueId() + " alors qu'il n'a pas de slots de gemmes"));
                this.notifyUI();
                return false;
            }
            final Gems gems = item.getGems();
            gems.removeGem(msg.getIndex());
            item.resetCache();
            if (itemIsEquiped) {
                WakfuGameEntity.getInstance().getLocalPlayer().reloadItemEffects();
            }
            this.notifyUI();
            return true;
        }
        
        private void notifyUI() {
            WakfuGameEntity.getInstance().getLocalPlayer().getBags().updateGemmedView();
        }
        
        @Override
        public int getProtocolId() {
            return 13103;
        }
    }
    
    private static class GemMergedMessageRunner implements MessageRunner<GemMergedResultMessage>
    {
        @Override
        public boolean run(final GemMergedResultMessage msg) {
            this.notifyUI(msg.isSuccess());
            return true;
        }
        
        private void notifyUI(final boolean success) {
            UIMergeGemFrame.getInstance().onMergeResult(success);
        }
        
        @Override
        public int getProtocolId() {
            return 13106;
        }
    }
    
    private static class GemImprovedMessageRunner implements MessageRunner<GemImproveResultMessage>
    {
        @Override
        public boolean run(final GemImproveResultMessage msg) {
            this.notifyUI(msg.isSuccess());
            return true;
        }
        
        private void notifyUI(final boolean success) {
            UIImproveGemFrame.getInstance().onImproveResult();
        }
        
        @Override
        public int getProtocolId() {
            return 13108;
        }
    }
}
