package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class RestOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private final CharacterInfo m_character;
    
    public RestOccupation(final CharacterInfo character) {
        super();
        this.m_character = character;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 1;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    @Override
    public void begin() {
        RestOccupation.m_logger.trace((Object)("Lancement de l'occupation REST pour le joueur " + this.m_character.getId()));
        this.m_character.cancelCurrentOccupation(false, true);
        final CharacterActor actor = this.m_character.getActor();
        if (!actor.getDirection().isDirection4()) {
            actor.setDirection(actor.getDirection().getNextDirection4(1));
        }
        actor.setAnimation("AnimEmote-Repos");
        this.m_character.setCurrentOccupation(this);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        if (sendMessage) {
            final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
            netMsg.setModificationType((byte)3);
            netMsg.setOccupationType(this.getOccupationTypeId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
        return this.finish();
    }
    
    @Override
    public boolean finish() {
        RestOccupation.m_logger.trace((Object)("On arrete l'occupation REST pour le joueur " + this.m_character.getId()));
        final CharacterActor actor = this.m_character.getActor();
        if (!actor.getDirection().isDirection4()) {
            actor.setDirection(actor.getDirection().getNextDirection4(1));
        }
        if (actor.getAnimation().startsWith("AnimPosingAssis")) {
            final String animation = AnimationConstants.setAnimationEndSuffix(actor.getAnimation(), true);
            actor.setAnimation(animation);
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RestOccupation.class);
    }
}
