package com.ankamagames.wakfu.client.core.action.fight;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public final class ItemPickedUpAction extends AbstractFightTimedAction
{
    private static final String ANIMATION = "AnimCueillette-Debut";
    private static final int EXEC_TIME_IN_MS = 500;
    private boolean m_isSuccess;
    private long m_itemUid;
    
    public ItemPickedUpAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final ItemPickedUpInFightMessage msg) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_isSuccess = msg.isSuccess();
        this.m_itemUid = msg.getItemId();
    }
    
    @Override
    protected long onRun() {
        if (this.m_isSuccess) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            localPlayer.getActor().setAnimation("AnimCueillette-Debut");
        }
        else {
            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.lootInFightImpossible.inventoryFull"));
            chatMessage.setPipeDestination(3);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
        return 500L;
    }
    
    @Override
    protected void onActionFinished() {
        if (this.m_isSuccess) {
            this.finishAnimation();
            final FloorItem floorItem = this.addItemToBagsAndDespawnIt();
            this.displayInformations(floorItem);
        }
        super.onActionFinished();
    }
    
    private void displayInformations(final FloorItem floorItem) {
        if (floorItem == null) {
            return;
        }
        ItemFeedbackHelper.displayFlyingItemGain(floorItem.getItem());
    }
    
    private FloorItem addItemToBagsAndDespawnIt() {
        final FloorItem floorItem = FloorItemManager.getInstance().getFloorItem(this.m_itemUid);
        WakfuGameEntity.getInstance().getLocalPlayer().getBags().addItemToBags(floorItem.getItem());
        floorItem.unspawn();
        return floorItem;
    }
    
    private void finishAnimation() {
        final String endAnimation = AnimationConstants.setAnimationEndSuffix(WakfuGameEntity.getInstance().getLocalPlayer().getActor().getAnimation(), true);
        WakfuGameEntity.getInstance().getLocalPlayer().getActor().setAnimation(endAnimation);
    }
}
