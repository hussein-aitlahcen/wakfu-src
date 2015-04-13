package com.ankamagames.wakfu.client.core.game.characterInfo.guild;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class ApplyGuildVisual
{
    private static final Logger m_logger;
    private final ClientGuildInformationHandler m_guildHandler;
    
    public ApplyGuildVisual(final ClientGuildInformationHandler guildHandler) {
        super();
        this.m_guildHandler = guildHandler;
    }
    
    public void applyOn(final CharacterActor actor) {
        if (this.m_guildHandler.getGuildId() == 0L) {
            actor.removeGuildAppearance();
            return;
        }
        actor.setGuildAppearance(this.m_guildHandler);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ApplyGuildVisual.class);
    }
}
