package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRUGuildCreatorAction extends MRUInteractifMachine
{
    @Override
    public MRUGuildCreatorAction getCopy() {
        return new MRUGuildCreatorAction();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isInGuild();
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        final TextWidgetFormater sb = new TextWidgetFormater().append(label);
        if (WakfuGameEntity.getInstance().getLocalPlayer().isInGuild()) {
            sb.newLine().openText().addColor(MRUGuildCreatorAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.guild.alreadyInGuild")).closeText();
        }
        return sb.finishAndToString();
    }
}
