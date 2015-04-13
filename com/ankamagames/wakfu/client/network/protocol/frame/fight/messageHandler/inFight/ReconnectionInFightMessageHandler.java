package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.wakfu.common.game.fight.reconnection.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.fight.actionsOperations.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

final class ReconnectionInFightMessageHandler extends UsingFightMessageHandler<ReconnectionInFightMessage, Fight>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final ReconnectionInFightMessage msg) {
        final byte fightStatus = msg.getFightStatus();
        this.unserializeTimeScoreGauges(msg);
        this.setFightStatus(fightStatus);
        this.changeFightersAttackType();
        this.recoverEffectAreas(msg.getSerializedEffectArea());
        this.recoverCarried(msg.getCarriedInfos());
        this.recoverTimelineEvents(msg.getSerializedTimelineEvents());
        this.recoverFighterStates(msg.getSerializedFightersStates());
        this.pushFramesAndSetCountdown(msg, fightStatus);
        this.hideFighters();
        return false;
    }
    
    private void recoverFighterStates(final byte[] serializedFightersStates) {
        final FightProtagonists<CharacterInfo> protagonists = ((Fight)this.m_concernedFight).getProtagonists();
        final TLongObjectHashMap<FighterState> fighterStates = FighterStatesSerializer.unserialize(serializedFightersStates);
        fighterStates.forEachEntry(new TLongObjectProcedure<FighterState>() {
            @Override
            public boolean execute(final long a, final FighterState b) {
                protagonists.addFighterState(a, b);
                return true;
            }
        });
    }
    
    private void recoverTimelineEvents(final byte[] serializedTimelineEvents) {
        TimelineEventsSerializer.unserialize(serializedTimelineEvents, ((Fight)this.m_concernedFight).getTimeline().newUnmarshallingContext(((Fight)this.m_concernedFight).getContext()), ((Fight)this.m_concernedFight).getTimeline().getTimelineEvents());
    }
    
    private void recoverEffectAreas(final List<byte[]> serializedEffectArea) {
        final Fight concernedFight = (Fight)this.m_concernedFight;
        FightBuilderFromMessage.recoverEffectAreas(serializedEffectArea, concernedFight);
    }
    
    private void recoverCarried(final THashSet<CarryInfoForReconnection> carriedInfos) {
        carriedInfos.forEach(new TObjectProcedure<CarryInfoForReconnection>() {
            @Override
            public boolean execute(final CarryInfoForReconnection object) {
                ReconnectionInFightMessageHandler.this.recoverCarried(object);
                return true;
            }
        });
    }
    
    private boolean recoverCarried(final CarryInfoForReconnection carryInfo) {
        final CharacterInfo carrier = ((Fight)this.m_concernedFight).getFighterFromId(carryInfo.getCarrierId());
        EffectUser carried = ((BasicFight<EffectUser>)this.m_concernedFight).getFighterFromId(carryInfo.getCarriedId());
        if (carried == null) {
            carried = ((Fight)this.m_concernedFight).getEffectAreaManager().getActiveEffectAreaWithId(carryInfo.getCarriedId());
        }
        if (carrier == null || carried == null) {
            ReconnectionInFightMessageHandler.m_logger.error((Object)("Impossible de recr\u00e9er le port\u00e9 pour " + carrier + " et " + carried));
            return true;
        }
        final WakfuEffect effect = EffectManager.getInstance().getEffect(carryInfo.getEffectId());
        if (effect == null) {
            ReconnectionInFightMessageHandler.m_logger.error((Object)("Impossible de recr\u00e9er le port\u00e9 pour " + carrier + " et " + carried + ", l'effet est inconnu " + carryInfo.getEffectId()));
            return true;
        }
        final RunningEffect runningEffect = this.createEffect(carrier, carried, effect);
        final SpellEffectAction spellEffectAction = this.createSpellAction(carrier, effect, (WakfuRunningEffect)runningEffect);
        this.runSpellAction(spellEffectAction);
        return true;
    }
    
    private void runSpellAction(final SpellEffectAction spellEffectAction) {
        spellEffectAction.runWithoutInit();
        spellEffectAction.onActionFinished();
    }
    
    private SpellEffectAction createSpellAction(final CharacterInfo carrier, final WakfuEffect effect, final WakfuRunningEffect runningEffect) {
        final SpellEffectAction spellEffectAction = new SpellEffectAction(0, 0, 0, ((Fight)this.m_concernedFight).getId(), runningEffect);
        spellEffectAction.setInstigatorId(carrier.getId());
        spellEffectAction.setInitialized(true);
        spellEffectAction.setTargetId(runningEffect.getTarget().getId());
        spellEffectAction.setScriptFileId(effect.getScriptFileId());
        return spellEffectAction;
    }
    
    private RunningEffect createEffect(final CharacterInfo carrier, final EffectUser carried, final WakfuEffect effect) {
        final WakfuRunningEffect staticEffect = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
        final RunningEffect runningEffect = ((RunningEffect<WakfuEffect, EffectContainer>)staticEffect).newInstance(((Fight)this.m_concernedFight).getContext(), EffectManager.getInstance());
        runningEffect.setGenericEffect(effect);
        runningEffect.setTarget(carried);
        runningEffect.setCaster(carrier);
        return runningEffect;
    }
    
    private void hideFighters() {
        final Collection<CharacterInfo> fighters = ((Fight)this.m_concernedFight).getFighters();
        for (final CharacterInfo fighter : fighters) {
            UIFightFrame.hide(fighter, UIFightFrame.isHideFightOccluderActivated());
        }
    }
    
    private void setFightStatus(final byte fightStatus) {
        ((Fight)this.m_concernedFight).setStatus(AbstractFight.FightStatus.getStatusFromId(fightStatus));
    }
    
    private void unserializeTimeScoreGauges(final ReconnectionInFightMessage msg) {
        ((Fight)this.m_concernedFight).getTimeline().getTimeScoreGauges().unserialize(msg.getSerializedScoreGauges());
    }
    
    private void pushFramesAndSetCountdown(final ReconnectionInFightMessage msg, final byte fightStatus) {
        final ArrayList<MessageFrame> uiFrames = CreationActionSequenceOperations.getUiFrames();
        for (final MessageFrame uiFrame : uiFrames) {
            WakfuGameEntity.getInstance().pushFrame(uiFrame);
        }
        if (fightStatus == AbstractFight.FightStatus.ACTION.getId()) {
            this.pushUIFightTurnFrameIfNecessary(msg);
            this.setCurrentTurnCountDown(msg);
        }
    }
    
    private void changeFightersAttackType() {
        final Collection<CharacterInfo> fighters = ((Fight)this.m_concernedFight).getFighters();
        for (final CharacterInfo fighter : fighters) {
            fighter.getActor().onAnmLoaded(new Runnable() {
                @Override
                public void run() {
                    ReconnectionInFightMessageHandler.this.scheduleChangeAttack(fighter);
                }
            });
        }
    }
    
    private void scheduleChangeAttack(final CharacterInfo fighter) {
        final HMIHelper hmiHelper = fighter.getActor().getHmiHelper();
        final AttackTypeListener listener = new AttackTypeListener() {
            @Override
            public void onUsageStarted(final CharacterActor actor, final AttackType attackType) {
                hmiHelper.reloadStaticChanges();
                attackType.removeListener(this);
            }
        };
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                boolean attackChanged = false;
                if (((Fight)ReconnectionInFightMessageHandler.this.m_concernedFight).isInPlay(fighter)) {
                    attackChanged = fighter.changeToSpellAttackIfNecessary(listener);
                }
                else if (((Fight)ReconnectionInFightMessageHandler.this.m_concernedFight).isOffPlay(fighter)) {
                    ChangeActivityAction.changeActorForKO(fighter, true);
                }
                else if (((Fight)ReconnectionInFightMessageHandler.this.m_concernedFight).isOutOfPlay(fighter)) {
                    ChangeActivityAction.playerPlayerDeath(fighter, ((Fight)ReconnectionInFightMessageHandler.this.m_concernedFight).getId());
                }
                if (!attackChanged) {
                    ReconnectionInFightMessageHandler.this.reloadStaticChangesIfNecessary(hmiHelper);
                }
            }
        }, 0L, 1);
    }
    
    private void reloadStaticChangesIfNecessary(final HMIHelper hmiHelper) {
        final boolean hasAnimStaticChanges = hmiHelper.hasAnimStaticChanges();
        if (hasAnimStaticChanges) {
            hmiHelper.reloadStaticChanges();
        }
    }
    
    private void setCurrentTurnCountDown(final ReconnectionInFightMessage msg) {
        final long lag = WakfuGameCalendar.getInstance().current() - msg.getServerSendTime();
        final int lagSeconds = MathHelper.fastCeil(lag / 1000.0f);
        final int duration = msg.getTurnRemainingSeconds() - lagSeconds;
        ((Fight)this.m_concernedFight).startCountdown(duration);
    }
    
    private void pushUIFightTurnFrameIfNecessary(final ReconnectionInFightMessage msg) {
        final Timeline timeline = ((Fight)this.m_concernedFight).getTimeline();
        final CharacterInfo currentFighter = timeline.getCurrentFighter();
        if (currentFighter == null) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean useFighterTurnFrame = Fight.useFighterTurnFrame(currentFighter, localPlayer);
        if (useFighterTurnFrame && !WakfuGameEntity.getInstance().hasFrame(UIFightTurnFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIFightTurnFrame.getInstance());
            ((Fight)this.m_concernedFight).startTurnClock(currentFighter.getId());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReconnectionInFightMessageHandler.class);
    }
}
