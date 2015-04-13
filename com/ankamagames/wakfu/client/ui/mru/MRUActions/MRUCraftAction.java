package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class MRUCraftAction extends AbstractMRUAction implements MobileEndPathListener
{
    private static final int NOT_DISABLED = 0;
    private static final int DOES_NOT_KNOW_CRAFT = 2;
    private static final int NOT_SUBSCRIBED = 3;
    private static final int NOT_SUBSCRIPTION_RIGHT = 4;
    private static final int[] AUTHORIZED_CRAFT_ID;
    private boolean m_freeMode;
    private ActionVisual m_visual;
    private int m_disableReason;
    
    public MRUCraftAction(final boolean freeMode) {
        super();
        this.m_disableReason = 0;
        this.m_freeMode = freeMode;
    }
    
    public MRUCraftAction(final ActionVisual visual, final boolean freeMode) {
        super();
        this.m_disableReason = 0;
        this.m_visual = visual;
        this.m_freeMode = freeMode;
    }
    
    @Override
    public MRUCraftAction getCopy() {
        return new MRUCraftAction(this.m_visual, this.m_freeMode);
    }
    
    public void setVisual(final ActionVisual visual) {
        this.m_visual = visual;
    }
    
    @Override
    public String getTranslatorKey() {
        return this.m_visual.getMruLabelKey();
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.b().append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey()));
        if (this.m_freeMode) {
            sb.append(" (").append(WakfuTranslator.getInstance().getString("craft.improvise")).append(")");
        }
        sb._b();
        if (!this.isEnabled() && this.m_disableReason != 0) {
            sb.append("\n").openText().addColor(MRUCraftAction.NOK_TOOLTIP_COLOR);
            switch (this.m_disableReason) {
                case 2: {
                    sb.append(WakfuTranslator.getInstance().getString("craft.unknown")).closeText();
                    break;
                }
                case 3: {
                    sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed")).closeText();
                    break;
                }
                case 4: {
                    sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscriptionRight")).closeText();
                    break;
                }
            }
        }
        return sb.finishAndToString();
    }
    
    @Override
    public boolean isRunnable() {
        this.setEnabled(true);
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer)) {
            this.m_disableReason = 3;
            return false;
        }
        final int craftId = ((CraftInteractiveElement)this.m_source).getCraftId();
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!player.hasSubscriptionRight(SubscriptionRight.CRAFT) && !ArrayUtils.contains(MRUCraftAction.AUTHORIZED_CRAFT_ID, craftId)) {
            this.m_disableReason = 4;
            return false;
        }
        if (localPlayer.getCraftHandler().contains(craftId)) {
            this.m_disableReason = 0;
            return true;
        }
        this.m_disableReason = 2;
        return false;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUCraftAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final WakfuClientMapInteractiveElement element = (WakfuClientMapInteractiveElement)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        localPlayer.getActor().addEndPositionListener(this);
        if (!localPlayer.moveTo(false, true, element.getApproachPoints())) {
            localPlayer.getActor().removeEndPositionListener(this);
            if (!localPlayer.getActor().isMoving() && element.isInApproachPoint(localPlayer.getActor().getWorldCoordinates())) {
                this.loadCraftFrame();
            }
            else {
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("too.far.to.interact"));
                chatMessage.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage);
            }
        }
    }
    
    @Override
    public MRUActions tag() {
        return this.m_freeMode ? MRUActions.OPEN_CRAFT_FREE_ACTION : MRUActions.OPEN_CRAFT_ACTION;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        final WakfuClientMapInteractiveElement element = (WakfuClientMapInteractiveElement)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (element.isInApproachPoint(localPlayer.getActor().getWorldCoordinates())) {
            this.loadCraftFrame();
        }
    }
    
    @Override
    protected int getGFXId() {
        return this.m_freeMode ? MRUGfxConstants.INTERROGATION_MARK.m_id : this.m_visual.getMruGfx();
    }
    
    protected void loadCraftFrame() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CraftInteractiveElement craftInteractiveElement = (CraftInteractiveElement)this.m_source;
        localPlayer.getActor().setDirection(localPlayer.getPosition().getDirectionTo(craftInteractiveElement.getWorldCellX(), craftInteractiveElement.getWorldCellY(), (short)0));
        UICraftTableFrame.getInstance().setCraftTable((CraftInteractiveElement)this.m_source, this.m_freeMode);
    }
    
    static {
        AUTHORIZED_CRAFT_ID = new int[] { 40, 64 };
    }
}
