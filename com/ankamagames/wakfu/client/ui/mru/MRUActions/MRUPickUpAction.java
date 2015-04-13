package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUPickUpAction extends AbstractMRUAction implements MobileEndPathListener
{
    private ArrayList<FloorItem> m_floorItems;
    private FloorItemInteractiveElement m_interactivesElement;
    public static final int ENABLED = 0;
    public static final int IS_NOT_SUBSCRIBED = 1;
    private int m_disableReason;
    
    public MRUPickUpAction() {
        super();
        this.m_floorItems = new ArrayList<FloorItem>();
        this.m_disableReason = 0;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUPickUpAction();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.hasSubscriptionRight(SubscriptionRight.PICK_UP_ITEM)) {
            this.m_disableReason = 1;
            return false;
        }
        return true;
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!(this.m_source instanceof FloorItemInteractiveElement)) {
            return false;
        }
        if (localPlayer.isWaitingForResult()) {
            return false;
        }
        if (localPlayer.isOnFight()) {
            return false;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        for (final FloorItem floorItem : ((FloorItemInteractiveElement)this.m_source).getFloorItems()) {
            if (!floorItem.isLocked() || floorItem.getLock().contains(localPlayer.getId())) {
                final SimpleCriterion crit = floorItem.getItem().getReferenceItem().getCriterion(ActionsOnItem.PICK_UP);
                final FloorItemInteractiveElement floorItemInteractiveElement = (FloorItemInteractiveElement)this.m_source;
                if (crit == null || crit.isValid(localPlayer, new Point3(floorItemInteractiveElement.getWorldCellX(), floorItemInteractiveElement.getWorldCellY(), floorItemInteractiveElement.getWorldCellAltitude()), floorItem, localPlayer.getEffectContext())) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUPickUpAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        this.m_interactivesElement = (FloorItemInteractiveElement)this.m_source;
        for (final FloorItem floorItem : ((FloorItemInteractiveElement)this.m_source).getFloorItems()) {
            if (!floorItem.isLocked() || floorItem.getLock().contains(WakfuGameEntity.getInstance().getLocalPlayer().getId())) {
                this.m_floorItems.add(floorItem);
            }
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (Math.abs(localPlayer.getWorldCellX() - this.m_interactivesElement.getWorldCellX()) <= 1 && Math.abs(localPlayer.getWorldCellY() - this.m_interactivesElement.getWorldCellY()) <= 1) {
            final PickUpItemToItemInventoryMessage netMessage = new PickUpItemToItemInventoryMessage();
            for (final FloorItem floorItem2 : this.m_floorItems) {
                netMessage.addItemToPickUp(floorItem2.getItem().getUniqueId());
            }
            netMessage.setInteractifElementId(this.m_interactivesElement.getId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
        else {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().addEndPositionListener(this);
            final FloorItemInteractiveElement floorItemInteractiveElement = (FloorItemInteractiveElement)this.m_source;
            WakfuGameEntity.getInstance().getLocalPlayer().moveTo(floorItemInteractiveElement.getWorldCellX(), floorItemInteractiveElement.getWorldCellY(), floorItemInteractiveElement.getWorldCellAltitude(), false, true);
        }
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.ITEM_PICK_UP_ACTION;
    }
    
    @Override
    public String getTranslatorKey() {
        return "pickup";
    }
    
    @Override
    public String getLabel() {
        if (!this.isEnabled()) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.b().append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey()))._b().append("\n");
            sb.openText().addColor(MRUPickUpAction.NOK_TOOLTIP_COLOR);
            switch (this.m_disableReason) {
                case 1: {
                    sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed")).closeText();
                    break;
                }
            }
            return sb.finishAndToString();
        }
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(MRUPickUpAction.DEFAULT_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString(this.getTranslatorKey()))._b();
        return sb.finishAndToString();
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        final PickUpItemToItemInventoryMessage netMessage = new PickUpItemToItemInventoryMessage();
        for (final FloorItem floorItem : this.m_floorItems) {
            final int dx = this.m_interactivesElement.getWorldCellX() - mobile.getWorldCellX();
            final int dy = this.m_interactivesElement.getWorldCellY() - mobile.getWorldCellY();
            if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
                if (dx != 0 && dy != 0) {
                    mobile.setDirection(Vector3i.getDirection8FromVector(dx, dy));
                }
                netMessage.addItemToPickUp(floorItem.getItem().getUniqueId());
            }
        }
        netMessage.setInteractifElementId(this.m_interactivesElement.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.HAND.m_id;
    }
}
