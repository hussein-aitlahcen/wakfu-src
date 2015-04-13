package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRUGuildStorageBoxAction extends MRUInteractifMachine
{
    @Override
    public MRUGuildStorageBoxAction getCopy() {
        return new MRUGuildStorageBoxAction();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer.isInGuild() && !this.isInOtherHavenWorld() && super.isEnabled();
    }
    
    private boolean isInOtherHavenWorld() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.isInGuild()) {
            return true;
        }
        final int havenWorldId = localPlayer.getGuildHandler().getHavenWorldId();
        final short instanceId = localPlayer.getInstanceId();
        final HavenWorldDefinition thisWorld = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(instanceId);
        return thisWorld != null && thisWorld.getId() != havenWorldId;
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        final TextWidgetFormater sb = new TextWidgetFormater().append(label);
        if (!WakfuGameEntity.getInstance().getLocalPlayer().isInGuild()) {
            sb.newLine().openText().addColor(MRUGuildStorageBoxAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("group.error.not_in_guild")).closeText();
        }
        return sb.finishAndToString();
    }
}
