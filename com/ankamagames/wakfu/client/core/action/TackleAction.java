package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public class TackleAction extends AbstractFightTimedAction
{
    private static FightLogger m_fightLogger;
    
    public TackleAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    public long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.getTargetId());
        if (fighter == null) {
            return 0L;
        }
        final CharacterActor actor = fighter.getActor();
        actor.clearActiveParticleSystem();
        if (this.consernLocalPlayer()) {
            final String message = WakfuTranslator.getInstance().getString("fight.tackled", new TextWidgetFormater().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR).append(fighter.getControllerName()).finishAndToString());
            TackleAction.m_fightLogger.info(message);
        }
        final String animTackle = actor.getCurrentAttack().getAnimTackle();
        actor.setAnimation(animTackle);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getCurrentFight() == null || localPlayer.getCurrentFightId() == this.getFightId()) {
            final FreeParticleSystem freeParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800021);
            if (freeParticleSystem != null) {
                freeParticleSystem.setTarget(actor);
                freeParticleSystem.setFightId(this.getFightId());
                IsoParticleSystemManager.getInstance().addParticleSystem(freeParticleSystem);
            }
            if (localPlayer == fighter) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventTackled());
            }
        }
        if (fighter.hasCharacteristic(FighterCharacteristicType.MP)) {
            fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).toMin();
        }
        return actor.getAnimationDuration(animTackle);
    }
    
    static {
        TackleAction.m_fightLogger = new FightLogger();
    }
}
