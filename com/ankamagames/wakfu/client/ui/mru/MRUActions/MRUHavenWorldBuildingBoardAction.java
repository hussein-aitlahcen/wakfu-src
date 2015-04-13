package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class MRUHavenWorldBuildingBoardAction extends MRUGenericInteractiveAction
{
    public static final int ENABLED = 0;
    public static final int IS_NOT_SUBSCRIBED = 1;
    private int m_disabledReason;
    
    public MRUHavenWorldBuildingBoardAction(final String name, final int gfxId) {
        super();
        this.m_name = name;
        this.m_gfxId = gfxId;
    }
    
    public MRUHavenWorldBuildingBoardAction() {
        super();
    }
    
    @Override
    public MRUGenericInteractiveAction getCopy() {
        return new MRUHavenWorldBuildingBoardAction(this.m_name, this.m_gfxId);
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer)) {
            this.m_disabledReason = 1;
            return false;
        }
        return this.m_enabled;
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.isEnabled() ? MRUHavenWorldBuildingBoardAction.DEFAULT_TOOLTIP_COLOR : MRUHavenWorldBuildingBoardAction.NOK_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString(this.m_name))._b();
        if (!this.isEnabled()) {
            switch (this.m_disabledReason) {
                case 1: {
                    sb.newLine().addColor(MRUHavenWorldBuildingBoardAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
                    break;
                }
            }
        }
        return sb.finishAndToString();
    }
    
    @Override
    public void run() {
        super.run();
        final UIBuildingPanelFrame uiFrame = UIBuildingPanelFrame.getInstance();
        uiFrame.setBoard((HavenWorldBuildingBoard)this.m_source);
        NetHavenWorldFrame.INSTANCE.setCacheOccupation(new ManageHavenWorldOccupation(uiFrame));
    }
}
