package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.moderationNew.panel.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUOpenModerationPanelAction extends AbstractMRUAction
{
    private String m_disabledReason;
    
    @Override
    public MRUActions tag() {
        return MRUActions.OPEN_MODERATION_PANEL_ACTION;
    }
    
    @Override
    public void run() {
        final PlayerCharacter target = (PlayerCharacter)this.m_source;
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPlayer(new ModeratedPlayerView(target.getName()));
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPage(ModerationPanelPage.PLAYER);
        if (!WakfuGameEntity.getInstance().hasFrame(UIModerationPanelFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().pushFrame(UIModerationPanelFrame.INSTANCE);
        }
    }
    
    @Override
    public boolean isEnabled() {
        if (!super.isEnabled()) {
            return false;
        }
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer != null && !AdminRightHelper.checkRights(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightHelper.NO_RIGHT);
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer != null && !AdminRightHelper.checkRights(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightHelper.NO_RIGHT);
    }
    
    @Override
    public String getTranslatorKey() {
        return "openModerationPanel";
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (!(this.m_source instanceof PlayerCharacter)) {
            return null;
        }
        sb.b().addColor(MRUOpenModerationPanelAction.DEFAULT_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey(), ((PlayerCharacter)this.m_source).getName()))._b();
        if (!this.isEnabled()) {
            sb.newLine().addColor(this.isEnabled() ? MRUOpenModerationPanelAction.DEFAULT_TOOLTIP_COLOR : MRUOpenModerationPanelAction.NOK_TOOLTIP_COLOR);
            sb.append(WakfuTranslator.getInstance().getString(this.m_disabledReason));
        }
        return sb.finishAndToString();
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUOpenModerationPanelAction();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.KICK.m_id;
    }
}
