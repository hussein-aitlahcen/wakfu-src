package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CallHelpMessageHandler extends UsingFightMessageHandler<CallHelpMessage, ExternalFightInfo>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final CallHelpMessage msg) {
        final long fighterId = msg.getFighterId();
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(fighterId);
        if (character == null) {
            CallHelpMessageHandler.m_logger.error((Object)("Personnage id=" + fighterId + " lors du traitement d'un " + CallHelpMessage.class.getSimpleName()));
            return false;
        }
        final TextWidgetFormater textWidgetFormater = new TextWidgetFormater();
        ChatView.createLink(textWidgetFormater, "characterName_" + character.getId(), character.getName());
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("fight.callForHelp", textWidgetFormater.finishAndToString()));
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        return false;
    }
    
    private void playHelpAnim(final CharacterInfo character) {
        final CharacterActor actor = character.getActor();
        final String oldAnimation = actor.getAnimation();
        actor.setAnimation("AnimEmoteCombat-Aide");
        actor.addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                if (actor.getAnimation().equals("AnimEmoteCombat-Aide")) {
                    actor.setAnimation(oldAnimation);
                }
                actor.removeAnimationEndedListener(this);
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)CallHelpMessageHandler.class);
    }
}
