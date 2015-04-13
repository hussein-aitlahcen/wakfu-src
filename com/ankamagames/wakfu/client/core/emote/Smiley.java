package com.ankamagames.wakfu.client.core.emote;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.*;

public class Smiley extends EmoteSmileyFieldProvider
{
    public Smiley(final int id, final String commandText) {
        super(id, commandText);
        this.setKnown(true);
    }
    
    @Override
    public String getName() {
        final SmileyEnum smiley = SmileyEnum.getSmileyFromId(this.getId());
        return WakfuTranslator.getInstance().getString(smiley.name());
    }
    
    @Override
    public String getCommandText() {
        final SmileyEnum smiley = SmileyEnum.getSmileyFromId(this.getId());
        return smiley.getDefaultSmiley();
    }
    
    @Override
    public String getIconUrl() {
        return WakfuConfiguration.getInstance().getEmoteIconUrl(this.m_id);
    }
}
