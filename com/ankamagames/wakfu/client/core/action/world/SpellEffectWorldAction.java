package com.ankamagames.wakfu.client.core.action.world;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect.*;
import com.ankamagames.wakfu.client.core.effect.*;
import java.nio.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.text.*;
import java.util.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;

public class SpellEffectWorldAction extends ScriptedAction implements SpellEffectActionInterface
{
    private final WakfuRunningEffect m_runningEffect;
    private final byte[] m_serializedRunningEffect;
    private final byte[] m_serializedTarget;
    private final boolean m_isTriggered;
    
    public SpellEffectWorldAction(final int uniqueId, final int actionType, final int actionId, final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect, final byte[] serializedRunningEffectWithoutTargets, final boolean isTriggered, final byte[] serializedTarget) {
        super(uniqueId, actionType, actionId);
        this.m_runningEffect = (WakfuRunningEffect)staticEffect.newInstance(null, EffectManager.getInstance());
        this.m_isTriggered = isTriggered;
        this.m_serializedRunningEffect = serializedRunningEffectWithoutTargets;
        this.m_serializedTarget = serializedTarget;
        this.addJavaFunctionsLibrary(new SpellEffectFunctionsLibrary(this));
        this.setScriptFileId(this.getScriptIdFromEffectId(staticEffect.getId()));
    }
    
    @Override
    public long getCasterId() {
        return this.getInstigatorId();
    }
    
    @Override
    public int getFightId() {
        return -1;
    }
    
    @Override
    public long onRun() {
        if (this.m_runningEffect != null) {
            this.m_runningEffect.setContext(WakfuInstanceEffectContext.getInstance());
            this.m_runningEffect.fromBuild(this.m_serializedRunningEffect);
            if (this.m_serializedTarget != null) {
                this.m_runningEffect.getTargetBinarSerialPart().unserialize(ByteBuffer.wrap(this.m_serializedTarget), Version.SERIALIZATION_VERSION);
            }
            if (this.m_runningEffect.getTarget() != null) {
                this.setTargetId(this.m_runningEffect.getTarget().getId());
            }
            if (this.m_runningEffect.getCaster() != null) {
                this.setInstigatorId(this.m_runningEffect.getCaster().getId());
            }
        }
        final int scriptFileId = this.getGenericEffectScriptFileId();
        if (scriptFileId != 0) {
            this.setScriptFileId(scriptFileId);
        }
        return super.onRun();
    }
    
    private int getGenericEffectScriptFileId() {
        if (((RunningEffect<FX, WakfuEffectContainer>)this.m_runningEffect).getEffectContainer() != null && ((RunningEffect<FX, WakfuEffectContainer>)this.m_runningEffect).getEffectContainer().getContainerType() == 33) {
            return -1;
        }
        if (((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect() == null) {
            return 0;
        }
        return ((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect().getScriptFileId();
    }
    
    @Override
    protected void onActionFinished() {
        final int id = this.m_runningEffect.getId();
        try {
            this.chatInfo(id);
        }
        catch (Exception e) {
            SpellEffectWorldAction.m_logger.error((Object)"Exception levee lors de l'affichage, on continue cependant l'execution de notre effet", (Throwable)e);
        }
        this.effectExecution();
        this.m_runningEffect.releaseIfNeed();
    }
    
    private void effectExecution() {
        this.m_runningEffect.disableValueComputation();
        RunningEffect.resetLimitedApplyCount();
        if (this.m_isTriggered) {
            this.m_runningEffect.forceInstant();
            this.m_runningEffect.execute(null, true);
        }
        else {
            if (this.m_runningEffect.hasDuration()) {
                if (this.m_runningEffect.getTarget() != null && this.m_runningEffect.getTarget().getRunningEffectManager() != null) {
                    this.m_runningEffect.getTarget().getRunningEffectManager().storeEffect(this.m_runningEffect);
                }
                this.m_runningEffect.onApplication();
            }
            if (this.m_runningEffect.hasDuration() && !this.m_runningEffect.isInfinite()) {
                this.m_runningEffect.pushRunningEffectDurationTimeEventInTimeline();
            }
            if (!this.m_runningEffect.mustBeTriggered()) {
                this.m_runningEffect.execute(this.m_runningEffect.getParent(), false);
            }
        }
        this.m_runningEffect.releaseIfNeed();
    }
    
    private void chatInfo(final int id) {
        final LocalPlayerCharacter info = WakfuGameEntity.getInstance().getLocalPlayer();
        if (info != this.m_runningEffect.getCaster() && info != this.m_runningEffect.getTarget()) {
            return;
        }
        if (((RunningEffect<FX, WakfuEffectContainer>)this.m_runningEffect).getEffectContainer() != null && ((RunningEffect<FX, WakfuEffectContainer>)this.m_runningEffect).getEffectContainer().getContainerType() == 33) {
            return;
        }
        if (CharacterInfoManager.getInstance().getCharacter(this.getTargetId()) != null && id != 30 && id != 40 && id != 190 && !(this.m_runningEffect instanceof StateRunningEffect) && (((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect().notifyInChat() || (((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect().notifyInChatForCaster() && info == this.m_runningEffect.getCaster()) || (((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect().notifyInChatForTarget() && info == this.m_runningEffect.getTarget()))) {
            final TextWidgetFormater formattedMessage = new TextWidgetFormater();
            final ArrayList<String> values = new ArrayList<String>();
            formattedMessage.openText().addColor(ChatConstants.CHAT_FIGHT_TARGET_COLOR).append(CharacterInfoManager.getInstance().getCharacter(this.getTargetId()).getName()).append(" : ");
            String effectText = null;
            if (WakfuTranslator.getInstance().containsContentKey(13, this.m_runningEffect.getEffectId())) {
                effectText = WakfuTranslator.getInstance().getStringWithoutFormat(13, this.m_runningEffect.getEffectId());
            }
            if (effectText == null || effectText.length() == 0) {
                effectText = WakfuTranslator.getInstance().getStringWithoutFormat(30, id);
            }
            if (effectText.length() > 0) {
                formattedMessage.closeText();
                formattedMessage.openText().addColor(ChatConstants.CHAT_FIGHT_EFFECT_COLOR);
                formattedMessage.append(effectText);
                if (id == RunningEffectConstants.STATE_APPLY.getId() || id == RunningEffectConstants.APPLY_STATE_PERCENT_FUNCTION_AREA_HP.getId()) {
                    final ApplyState applyState = (ApplyState)this.m_runningEffect;
                    final StateClient state = (StateClient)StateManager.getInstance().getState(applyState.getStateId());
                    final String s1 = String.valueOf(applyState.getStateLevel());
                    values.add(state.getName());
                    values.add(s1);
                }
                else if (id == RunningEffectConstants.STATE_RESISTANCE.getId()) {
                    final StateResistance stateResistance = (StateResistance)this.m_runningEffect;
                    final StateClient state = (StateClient)StateManager.getInstance().getState(stateResistance.getStateId());
                    final String s1 = String.valueOf(stateResistance.getValue());
                    values.add(state.getName());
                    values.add(s1);
                }
                else if (id == RunningEffectConstants.STATE_FORCE_UNAPPLY.getId()) {
                    final StateClient state2 = (StateClient)StateManager.getInstance().getState(this.m_runningEffect.getValue());
                    values.add(state2.getName());
                }
                else if (id == RunningEffectConstants.SET_AURA.getId()) {
                    values.add(WakfuTranslator.getInstance().getString(6, this.m_runningEffect.getValue(), new Object[0]));
                }
                else {
                    values.add(String.valueOf(this.m_runningEffect.getValue()));
                }
                String message2 = "";
                try {
                    message2 = StringFormatter.format(formattedMessage.finishAndToString(), values.toArray());
                }
                catch (Exception e) {
                    SpellEffectWorldAction.m_logger.error((Object)"Exception", (Throwable)e);
                }
                final ChatMessage chatMessage = new ChatMessage(message2);
                chatMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage);
            }
        }
    }
    
    private int getScriptIdFromEffectId(final int effectId) {
        final RunningEffectDefinition definition = (RunningEffectDefinition)RunningEffectConstants.getInstance().getConstantDefinition(effectId);
        if (definition == null) {
            return -1;
        }
        return definition.getScriptId();
    }
    
    @Override
    public int getEffectValue() {
        return this.m_runningEffect.getValue();
    }
    
    @Override
    public byte getExecutionStatus() {
        return this.m_runningEffect.getExecutionStatus();
    }
    
    @Override
    public WakfuRunningEffect getRunningEffect() {
        return this.m_runningEffect;
    }
    
    @Override
    public Point3 getPosition() {
        final EffectUser target = this.m_runningEffect.getTarget();
        if (target != null) {
            return new Point3(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
        }
        return this.m_runningEffect.getTargetCell();
    }
    
    @Override
    public Point3 getTargetCell() {
        final Point3 targetCell = this.m_runningEffect.getTargetCell();
        if (targetCell != null) {
            return targetCell;
        }
        final EffectUser target = this.m_runningEffect.getTarget();
        if (target != null) {
            return new Point3(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
        }
        return null;
    }
    
    @Override
    public Point3 getCasterPosition() {
        final EffectUser caster = this.m_runningEffect.getCaster();
        if (caster != null) {
            return new Point3(caster.getWorldCellX(), caster.getWorldCellY(), caster.getWorldCellAltitude());
        }
        SpellEffectWorldAction.m_logger.error((Object)"ATTENTION : on veut r\u00e9cup\u00e9rer la position du caster d'un effet mais celui ci est inconnu, \u00e0 d\u00e9faut on renvoie la cellule cible");
        return this.m_runningEffect.getTargetCell();
    }
    
    @Override
    public Point3 getBearerPosition() {
        final RunningEffectManager manager = this.m_runningEffect.getManagerWhereIamStored();
        if (manager == null) {
            return null;
        }
        final EffectUser owner = manager.getOwner();
        if (owner == null) {
            return null;
        }
        return owner.getPosition();
    }
    
    @Override
    public int getArmorLossValue() {
        if (this.m_runningEffect instanceof ArmorLossProvider) {
            return ((ArmorLossProvider)this.m_runningEffect).getArmorLoss();
        }
        return 0;
    }
    
    @Override
    public int getBarrierLossValue() {
        if (this.m_runningEffect instanceof ArmorLossProvider) {
            return ((ArmorLossProvider)this.m_runningEffect).getBarrierLoss();
        }
        return 0;
    }
}
