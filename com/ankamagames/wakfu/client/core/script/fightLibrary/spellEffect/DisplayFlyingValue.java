package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.client.core.game.IsoWorldTarget.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

final class DisplayFlyingValue extends SpellEffectActionFunction
{
    private static final String NAME = "displayFlyingValue";
    private static final String DESC = "Affiche un visuel correspondant a l'effet (valeur au dessus de la cible, gfx de l'etat correspondant...)";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private final int FLYING_EFFECT_DURATION = 3500;
    private final int FLYING_IMAGE_EFFECT_DURATION = 5000;
    private int FLYING_EFFECT_WAITING_TIME;
    private static final int MAX_CLAMPING_VALUE = 500;
    private static final int NUM_STEPS = 10;
    private static final float MIN_SCALE = 0.65f;
    private static final float MAX_SCALE = 1.3f;
    
    DisplayFlyingValue(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
        this.FLYING_EFFECT_WAITING_TIME = 1000;
    }
    
    @Override
    public String getName() {
        return "displayFlyingValue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return DisplayFlyingValue.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final float r = (float)this.getParamDouble(0);
        final float g = (float)this.getParamDouble(1);
        final float b = (float)this.getParamDouble(2);
        float rInfo = 1.0f;
        float gInfo = 1.0f;
        float bInfo = 1.0f;
        String font = this.getParamString(3);
        int style = 0;
        final String suffix = this.getParamString(4);
        boolean negate = false;
        boolean caster = false;
        int value = this.m_spellEffectAction.getEffectValue();
        boolean displayValue = true;
        boolean displayInformation = false;
        boolean displayImage = false;
        String information = "";
        String imagePath = null;
        boolean timelineBuff = false;
        StateClient state = null;
        final WakfuRunningEffect runningEffect = this.m_spellEffectAction.getRunningEffect();
        if (runningEffect != null && runningEffect instanceof ApplyState) {
            final ApplyState sre = (ApplyState)runningEffect;
            state = (StateClient)StateManager.getInstance().getState(sre.getStateId());
        }
        switch (this.m_spellEffectAction.getExecutionStatus()) {
            case 4: {
                information = WakfuTranslator.getInstance().getString("exec.absorb");
                rInfo = 0.0f;
                gInfo = 0.0f;
                bInfo = 1.0f;
                displayInformation = true;
                displayValue = true;
                break;
            }
            case 1: {
                information = WakfuTranslator.getInstance().getString("exec.failed");
                rInfo = 1.0f;
                gInfo = 0.0f;
                bInfo = 0.0f;
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 2: {
                information = WakfuTranslator.getInstance().getString("exec.immune");
                rInfo = 1.0f;
                gInfo = 1.0f;
                bInfo = 1.0f;
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 13: {
                information = WakfuTranslator.getInstance().getString("exec.seduction.immunity");
                rInfo = 1.0f;
                gInfo = 1.0f;
                bInfo = 1.0f;
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 6: {
                displayInformation = false;
                displayValue = false;
                break;
            }
            case 3: {
                information = WakfuTranslator.getInstance().getString("exec.resist");
                rInfo = 1.0f;
                gInfo = 1.0f;
                bInfo = 1.0f;
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 11: {
                information = WakfuTranslator.getInstance().getString("exec.seduction.resist");
                rInfo = 1.0f;
                gInfo = 1.0f;
                bInfo = 1.0f;
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 12: {
                information = WakfuTranslator.getInstance().getString("exec.level.too.high");
                rInfo = 1.0f;
                gInfo = 1.0f;
                bInfo = 1.0f;
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 0: {
                information = WakfuTranslator.getInstance().getString("exec.success");
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 14: {
                information = WakfuTranslator.getInstance().getString("exec.seduction.success");
                displayInformation = true;
                displayValue = false;
                break;
            }
            case 5: {
                displayValue = false;
                if (state == null || !state.isShownInTimeline()) {
                    break;
                }
                if (((RunningEffect<FX, WakfuEffectContainer>)runningEffect).getEffectContainer() != null) {
                    timelineBuff = (((RunningEffect<FX, WakfuEffectContainer>)runningEffect).getEffectContainer().getContainerType() == 21);
                }
                if (!(runningEffect.getTarget() instanceof BasicCharacterInfo)) {
                    return;
                }
                final BasicCharacterInfo target = (BasicCharacterInfo)runningEffect.getTarget();
                if (target.hasProperty(FightPropertyType.CANT_BE_DIFFERENTIATED_FROM_COPIES) && runningEffect.getTarget() == runningEffect.getCaster()) {
                    displayImage = false;
                }
                else {
                    displayImage = true;
                    imagePath = state.getIconUrl();
                }
                break;
            }
            case 10: {
                final WakfuRunningEffect effect = this.m_spellEffectAction.getRunningEffect();
                if (effect != null && effect.getEndTime() != null && WakfuGameEntity.getInstance().getLocalPlayer() != null && WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() != null && WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight().getTimeline() != null) {
                    final int remainingTurn = effect.getEndTime().getTableTurn() - WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight().getTimeline().getCurrentTableturn();
                    information = WakfuTranslator.getInstance().getString("exec.regen", remainingTurn);
                    rInfo = 0.6f;
                    gInfo = 0.0f;
                    bInfo = 0.0f;
                    displayInformation = true;
                    displayValue = false;
                    break;
                }
                displayInformation = false;
                displayValue = false;
                break;
            }
            case 7: {
                final FightInfo fight = FightManager.getInstance().getFightById(this.m_spellEffectAction.getFightId());
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (fight == null || localPlayer == null || this.m_spellEffectAction.getFightId() != localPlayer.getCurrentFightId() || !(this.m_spellEffectAction instanceof SpellEffectAction)) {
                    break;
                }
                final CharacterInfo target2 = fight.getFighterFromId(this.m_spellEffectAction.getTargetId());
                final CharacterInfo effectCaster = fight.getFighterFromId(this.m_spellEffectAction.getCasterId());
                if (target2 != null) {
                    Direction8 direction;
                    if (effectCaster != null && target2.getId() != effectCaster.getId()) {
                        direction = target2.getPosition().getDirection4To(effectCaster.getPosition());
                    }
                    else {
                        direction = target2.getDirection();
                    }
                    final int index = direction.getIndex();
                    int apsId = 0;
                    switch (index) {
                        case 1: {
                            apsId = WakfuClientConstants.HP_LOSS_BLOCK_APS[0];
                            break;
                        }
                        case 3: {
                            apsId = WakfuClientConstants.HP_LOSS_BLOCK_APS[1];
                            break;
                        }
                        case 5: {
                            apsId = WakfuClientConstants.HP_LOSS_BLOCK_APS[2];
                            break;
                        }
                        case 7: {
                            apsId = WakfuClientConstants.HP_LOSS_BLOCK_APS[3];
                            break;
                        }
                        default: {
                            return;
                        }
                    }
                    final FreeParticleSystem aps = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId);
                    aps.setTarget(target2.getActor());
                    aps.setFightId(target2.getCurrentFightId());
                    IsoParticleSystemManager.getInstance().addParticleSystem(aps);
                    break;
                }
                break;
            }
        }
        int size = 20;
        boolean isHpLoss = false;
        if (paramCount > 5) {
            negate = this.getParamBool(5);
        }
        if (paramCount > 6) {
            size = Math.max(1, this.getParamInt(6));
        }
        if (paramCount > 7) {
            isHpLoss = this.getParamBool(7);
        }
        if (paramCount > 8) {
            caster = this.getParamBool(8);
        }
        if (paramCount > 9) {
            value = this.getParamInt(9);
        }
        if (font.isEmpty()) {
            style = 5;
            font = "wci";
            size = 23;
        }
        WorldPositionable target3 = null;
        InteractiveIsoWorldTarget isotarget;
        if (caster) {
            isotarget = InteractiveIsoWorldTargetManager.getInstance().getInteractiveIsoWorldTarget(this.m_spellEffectAction.getCasterId());
        }
        else {
            isotarget = InteractiveIsoWorldTargetManager.getInstance().getInteractiveIsoWorldTarget(this.m_spellEffectAction.getTargetId());
        }
        if (isotarget != null) {
            if (isotarget.isVisible()) {
                target3 = isotarget;
            }
        }
        else if (caster) {
            target3 = this.m_spellEffectAction.getRunningEffect().getCaster();
        }
        else {
            target3 = this.m_spellEffectAction.getRunningEffect().getTarget();
        }
        if (target3 == null) {
            return;
        }
        final String svalue = (negate ? "-" : "+") + value + suffix;
        final EffectContext context = this.m_spellEffectAction.getRunningEffect().getContext();
        if (context instanceof WakfuFightEffectContextInterface) {
            final int fightId = ((WakfuFightEffectContextInterface)context).getFightId();
            if (!FightVisibilityManager.getInstance().canDisplayFlyingValue(fightId)) {
                return;
            }
        }
        if (displayInformation && information != null && information.length() > 0) {
            this.displayText(rInfo, gInfo, bInfo, "wci", 5, 30, false, 0, target3, information);
        }
        if (displayValue && svalue != null && svalue.length() > 0) {
            this.displayText(r, g, b, font, style, size, isHpLoss, value, target3, svalue);
        }
        if (displayImage && imagePath != null) {
            this.displayImage(imagePath, target3, timelineBuff);
        }
    }
    
    private void displayImage(final String imagePath, final WorldPositionable target, final boolean timelineBuff) {
        if (target instanceof CharacterActor && HoodedMonsterFightEventListener.isVisuallyHooded(((CharacterActor)target).getCharacterInfo())) {
            return;
        }
        FlyingImageDeformer imageDef;
        if (timelineBuff) {
            final ElementMap map = null;
            if (map == null) {
                return;
            }
            final Widget widget = (Widget)map.getElement("numBonusLabel");
            imageDef = new FlyingImage.LinkFromUIFlyingImageDeformer(WakfuClientInstance.getInstance().getWorldScene(), new FlyingWidgetWidgetUIDelegate(widget));
        }
        else {
            imageDef = new FlyingImage.DefaultFlyingImageDeformer();
        }
        final FlyingImage flyingImage = new FlyingImage(imagePath, 32, 32, imageDef, 5000);
        flyingImage.setTarget(target);
        final int adviserCount = AdviserManager.getInstance().countAdviserOfType(target, 4);
        flyingImage.setWaitingTime(adviserCount * 1000);
        AdviserManager.getInstance().addAdviser(flyingImage);
    }
    
    private void displayText(final float r, final float g, final float b, final String font, final int style, final int size, final boolean hpLoss, final int hpValue, final WorldPositionable target, final String text) {
        final Point3Positionable positionable = new Point3Positionable(target);
        final int adviserSize = AdviserManager.getInstance().countAdviserOfType(positionable, 3);
        int xOffset = 0;
        int yOffset = 0;
        switch (adviserSize) {
            case 0: {
                xOffset = 0;
                yOffset = 0;
                break;
            }
            case 1: {
                xOffset = MathHelper.random(-20, -10);
                yOffset = MathHelper.random(-20, -10);
                break;
            }
            case 2: {
                xOffset = MathHelper.random(10, 20);
                yOffset = MathHelper.random(-20, -10);
                break;
            }
            default: {
                xOffset = MathHelper.random(-20, 20);
                yOffset = MathHelper.random(-20, -1);
                break;
            }
        }
        FlyingTextDeformer deformer;
        int duration;
        if (hpLoss) {
            deformer = new FlyingText.DragonicaTextDeformer(0, 400, xOffset, 80 + yOffset, getScale(hpValue), 50);
            duration = 1000;
        }
        else {
            deformer = new FlyingText.DefaultFlyingTextDeformer(xOffset, yOffset);
            duration = 3500;
        }
        final FlyingText flyingText = new FlyingText(FontFactory.createFont(font, style, size), text, deformer, duration);
        flyingText.setColor(r, g, b, 1.0f);
        flyingText.setTarget(positionable);
        if (hpLoss) {
            flyingText.setWaitingTime(adviserSize * 100);
        }
        else {
            flyingText.setWaitingTime(adviserSize * 100);
        }
        AdviserManager.getInstance().addAdviser(flyingText);
    }
    
    private static float getScale(final int value) {
        final int clampedValue = MathHelper.clamp(value, 0, 500);
        final int step = clampedValue * 10 / 500;
        final float percentage = step / 10.0f;
        return MathHelper.lerp(0.65f, 1.3f, percentage);
    }
    
    @Override
    public String getDescription() {
        return "Affiche un visuel correspondant a l'effet (valeur au dessus de la cible, gfx de l'etat correspondant...)";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("R", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("G", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("B", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("font", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("suffix", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("negatesValue", null, LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("size", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("isHpLoss", null, LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("caster", null, LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("value", null, LuaScriptParameterType.NUMBER, true) };
    }
}
