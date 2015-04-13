package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.sound.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;

public class ChangeActivityAction extends AbstractFightTimedAction
{
    private static FightLogger m_fightLogger;
    private static long DIE_ANIMATION_DURATION;
    private String EXECUTION_TIME;
    private final byte m_activity;
    private final boolean m_animation;
    private int m_monsterScriptExecutionTime;
    
    public ChangeActivityAction(final int uniqueId, final int actionType, final int actionId, final byte activity, final int fightId, final boolean animation) {
        super(uniqueId, actionType, actionId, fightId);
        this.EXECUTION_TIME = "execution_Time";
        this.m_activity = activity;
        this.m_animation = animation;
    }
    
    public long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.getTargetId());
        if (fighter == null) {
            return 0L;
        }
        final boolean noListeners = this.m_listeners.isEmpty();
        switch (this.m_activity) {
            case 0: {
                final int duration = this.runFighterKo(fighter);
                break;
            }
            case 1: {
                final int duration = this.runFighterRaise(fighter);
                break;
            }
            case 2: {
                final int duration = this.runFighterDefeat(fighter);
                break;
            }
            case 3: {
                final int duration = this.runFighterFlee(fighter);
                break;
            }
            default: {
                final int duration = 0;
                break;
            }
        }
        int duration;
        return noListeners ? 0L : duration;
    }
    
    private int runFighterFlee(final CharacterInfo fighter) {
        final CharacterActor actor = fighter.getActor();
        String animFlee = "AnimFuite";
        if (!actor.containsAnimation(animFlee)) {
            animFlee = "AnimStatique";
        }
        actor.setAnimation(animFlee);
        final int time = actor.getAnimationDuration(animFlee);
        final int duration = (time == Integer.MAX_VALUE) ? 0 : time;
        this.cleanFighterActor(fighter);
        return duration;
    }
    
    private int runFighterDefeat(final CharacterInfo fighter) {
        final CharacterActor actor = fighter.getActor();
        if (this.doesFighterDefeatBePlayed()) {
            if (fighter instanceof PlayerCharacter) {
                if (this.getFight() == null || this.getFight().getModel().mustDieAtEndOfFight()) {
                    this.playPlayerDeath(fighter);
                }
            }
            else {
                this.playMonsterDefeat(fighter);
            }
            if (!fighter.isActiveProperty(FightPropertyType.NO_DEATH)) {
                actor.setStatus((byte)2);
            }
        }
        this.cleanFighterActor(fighter);
        final int time = (this.m_monsterScriptExecutionTime > 0) ? this.m_monsterScriptExecutionTime : actor.getAnimationDuration("AnimMort");
        return (time == Integer.MAX_VALUE) ? 0 : time;
    }
    
    private void playMonsterDefeat(final CharacterInfo fighter) {
        final boolean scriptPlayed = this.playMonsterDefeatScript(fighter);
        if (!scriptPlayed) {
            this.playDefaultMonsterDefeat(fighter);
        }
    }
    
    private boolean doesFighterDefeatBePlayed() {
        return this.m_animation;
    }
    
    private void cleanFighterActor(final CharacterInfo fighter) {
        final CharacterActor actor = fighter.getActor();
        actor.setStaticAnimationKey("AnimStatique");
        actor.clearAllParticleSystems();
    }
    
    private void playPlayerDeath(final CharacterInfo fighter) {
        final int fightId = this.getFightId();
        playerPlayerDeath(fighter, fightId);
    }
    
    public static void playerPlayerDeath(final CharacterInfo fighter, final int fightId) {
        final CharacterActor actor = fighter.getActor();
        actor.setAnimation("AnimKO-SortieHS");
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(fighter.getDeathParticleSystemId());
        if (particle == null) {
            ChangeActivityAction.m_logger.warn((Object)("le system de particule " + fighter.getDeathParticleSystemId() + "n'exsite pas"));
            return;
        }
        particle.setTarget(actor);
        particle.setFightId(fightId);
        IsoParticleSystemManager.getInstance().addParticleSystem(particle);
    }
    
    private void playDefaultMonsterDefeat(final CharacterInfo fighter) {
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(15000);
        WakfuSoundManager.getInstance().playSFXSound(15384L);
        if (particle == null) {
            ChangeActivityAction.m_logger.warn((Object)"le system de particule 15000n'exsite pas");
            return;
        }
        particle.setTarget(fighter.getActor());
        particle.setFightId(this.getFightId());
        IsoParticleSystemManager.getInstance().addParticleSystem(particle);
    }
    
    private boolean playMonsterDefeatScript(final CharacterInfo fighter) {
        final int defeatScript = fighter.getDefeatScript();
        if (defeatScript == -1) {
            fighter.getActor().setAnimation("AnimMort");
            return true;
        }
        LuaScript deathScript = LuaManager.getInstance().getScript(defeatScript);
        if (deathScript == null) {
            deathScript = LuaManager.getInstance().loadScript(defeatScript, null, true);
        }
        if (deathScript == null) {
            ChangeActivityAction.m_logger.warn((Object)("Impossible charger le script de mort d'id " + defeatScript));
            return false;
        }
        final LuaState scriptState = deathScript.getLuaState();
        try {
            scriptState.pushObjectValue((Object)fighter.getId());
            scriptState.setGlobal("killedId");
            scriptState.pushObjectValue((Object)fighter.getCurrentFightId());
            scriptState.setGlobal("fightId");
        }
        catch (LuaException e) {
            ChangeActivityAction.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        scriptState.resume(0);
        final LuaValue executionTime = deathScript.getValue(this.EXECUTION_TIME);
        if (executionTime != null && executionTime.getType() == LuaScriptParameterType.NUMBER) {
            this.m_monsterScriptExecutionTime = (int)executionTime.getValue();
        }
        return true;
    }
    
    private int runFighterRaise(final CharacterInfo fighter) {
        final CharacterActor actor = fighter.getActor();
        int duration = 0;
        if (this.m_animation) {
            if (actor.containsAnimation("AnimKO-SortieOK")) {
                duration = actor.getAnimationDuration("AnimKO-SortieOK");
                actor.setAnimation("AnimKO-SortieOK");
            }
            else {
                actor.setAnimation("AnimStatique");
            }
            actor.setStaticAnimationKey(this.getAnim(actor, "AnimStatique02", "AnimStatique"));
        }
        actor.setStatus((byte)0);
        actor.addPassiveTeamParticleSystem(fighter.getTeamId());
        actor.addDirectionParticleSystem(fighter.getDirection());
        return duration;
    }
    
    private int runFighterKo(final CharacterInfo fighter) {
        final boolean animation = this.m_animation;
        final int duration = changeActorForKO(fighter, animation);
        long groupId;
        if (fighter instanceof NonPlayerCharacter) {
            groupId = ((NonPlayerCharacter)fighter).getGroupId();
        }
        else {
            groupId = -1L;
        }
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventCharacterKo(fighter.getId(), fighter.getBreedId(), groupId));
        final FightInfo fight = this.getFight();
        if (fight instanceof Fight) {
            final Collection<CharacterInfo> inPlayTeammates = ((Fight)fight).getFightersInPlayInTeam(fighter.getTeamId());
            if (inPlayTeammates.size() == 1) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventGroupKilled(groupId));
            }
        }
        return duration;
    }
    
    public static int changeActorForKO(final CharacterInfo fighter, final boolean animation) {
        final CharacterActor actor = fighter.getActor();
        int duration = 0;
        if (animation && !fighter.isActiveProperty(FightPropertyType.NO_DEATH) && !fighter.isActiveProperty(FightPropertyType.NO_KO)) {
            if (actor.containsAnimation("AnimKO-Debut")) {
                actor.setAnimation("AnimKO-Debut");
                duration = actor.getAnimationDuration("AnimKO-Debut");
            }
            actor.setStaticAnimationKey("AnimKO-Boucle");
            actor.setStatus((byte)1);
        }
        actor.clearActiveParticleSystem();
        actor.clearTeamParticleSystem();
        actor.clearExtraTourParticleSystem();
        actor.clearDirectionParticleSystem();
        return duration;
    }
    
    @Override
    protected void onActionFinished() {
        final CharacterInfo fighter = this.getFighterById(this.getTargetId());
        this.chatInfosOnFinish(fighter);
        this.fightActionsOnFinish(fighter);
        super.onActionFinished();
    }
    
    private void chatInfosOnFinish(final CharacterInfo fighter) {
        switch (this.m_activity) {
            case 0: {
                this.chatInfosOnFighterKo(fighter);
                break;
            }
            case 2: {
                this.chatInfosOnFighterDefeat(fighter);
                break;
            }
            case 1: {
                this.chatInfosOnFighterRaise(fighter);
                break;
            }
        }
    }
    
    private void chatInfosOnFighterKo(final CharacterInfo fighter) {
        if (!this.consernLocalPlayer()) {
            return;
        }
        if (fighter == null) {
            return;
        }
        if (fighter.isActiveProperty(FightPropertyType.NO_KO)) {
            return;
        }
        if (fighter.hasCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH) && fighter.getCharacteristicValue(FighterCharacteristicType.KO_TIME_BEFORE_DEATH) > 0) {
            final String sentence = WakfuTranslator.getInstance().getString("fight.ko", new TextWidgetFormater().b().append(fighter.getControllerName())._b().finishAndToString());
            ChangeActivityAction.m_fightLogger.info(new TextWidgetFormater().openText().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR).append(sentence).closeText().finishAndToString());
        }
    }
    
    private void chatInfosOnFighterDefeat(final CharacterInfo fighter) {
        if (this.consernLocalPlayer() && fighter != null && !fighter.isActiveProperty(FightPropertyType.NO_DEATH) && (this.getFight() == null || this.getFight().getModel().mustDieAtEndOfFight())) {
            if (fighter.isLocalPlayer()) {
                ChangeActivityAction.m_fightLogger.info(WakfuTranslator.getInstance().getString("fight.endForMe"));
            }
            else {
                String sentence;
                if (!fighter.isActiveProperty(FightPropertyType.IS_A_COPY_OF_HIS_CONTROLLER)) {
                    sentence = WakfuTranslator.getInstance().getString("fight.die", new TextWidgetFormater().b().append(fighter.getName())._b().finishAndToString());
                }
                else {
                    sentence = WakfuTranslator.getInstance().getString("fight.die.for.copies", new TextWidgetFormater().b().append(fighter.getController().getName())._b().finishAndToString());
                }
                ChangeActivityAction.m_fightLogger.info(new TextWidgetFormater().openText().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR).append(sentence).closeText().finishAndToString());
            }
        }
    }
    
    private void chatInfosOnFighterRaise(final CharacterInfo fighter) {
        if (!this.consernLocalPlayer()) {
            return;
        }
        if (fighter == null) {
            return;
        }
        final String sentence = WakfuTranslator.getInstance().getString("fight.raise", new TextWidgetFormater().b().append(fighter.getControllerName())._b().finishAndToString());
        ChangeActivityAction.m_fightLogger.info(new TextWidgetFormater().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR).append(sentence).closeText().finishAndToString());
    }
    
    private void fightActionsOnFinish(final CharacterInfo fighter) {
        if (fighter == null) {
            return;
        }
        if (fighter.getCurrentFight() != null) {
            this.finishActionOnFight(fighter);
        }
        else {
            this.finishActionOnFighter(fighter);
        }
    }
    
    private void finishActionOnFight(final CharacterInfo fighter) {
        final Fight fight = fighter.getCurrentFight();
        switch (this.m_activity) {
            case 0: {
                fight.putFighterOffPlay(fighter);
                break;
            }
            case 1: {
                fight.putFighterBackInPlay(fighter);
                break;
            }
            case 3: {
                fighter.setFleeing(true);
            }
            case 2: {
                if (!fighter.isOffPlay()) {
                    fight.putFighterOffPlay(fighter);
                }
                if (fighter.isOffPlay()) {
                    fight.putFighterOutOfPlay(fighter);
                    break;
                }
                break;
            }
        }
    }
    
    private void finishActionOnFighter(final CharacterInfo fighter) {
        switch (this.m_activity) {
            case 0: {
                fighter.onGoesOffPlay();
                break;
            }
            case 1: {
                fighter.onBackInPlay();
                break;
            }
            case 2: {
                fighter.onGoesOutOfPlay();
                break;
            }
            case 3: {
                fighter.setFleeing(true);
                fighter.onGoesOutOfPlay();
                break;
            }
        }
    }
    
    private String getAnim(final CharacterActor actor, final String anim, final String defaultAnim) {
        if (actor.containsAnimation(anim)) {
            return anim;
        }
        return defaultAnim;
    }
    
    static {
        ChangeActivityAction.m_fightLogger = new FightLogger();
        ChangeActivityAction.DIE_ANIMATION_DURATION = 500L;
    }
}
