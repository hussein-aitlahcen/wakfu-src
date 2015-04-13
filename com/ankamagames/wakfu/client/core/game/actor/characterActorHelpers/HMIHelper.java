package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class HMIHelper
{
    private static final Logger m_logger;
    private static final boolean DEBUG_HMI_ACTIONS = false;
    private final CharacterActor m_actor;
    private AppearanceList m_additionalAppearances;
    private ColorPartList m_additionalColorPart;
    private ChangePartsList m_additionalParts;
    private CostumeList m_costumes;
    private VisibilityPartsList m_additionnalVisibleParts;
    private AnimSuffixList m_additionalAnimSuffix;
    private ChangeAnimStaticList m_additionalAnimStatic;
    private LinkMobileList m_additionalLinkedMobile;
    private ParticlesList m_additionalParticles;
    private LightsList m_additionalLights;
    private ChangeAnimList m_additionalAnim;
    private CustomMovementSelector m_movementSelector;
    private SetMonsterSkinList m_monsterSkins;
    private HideAllEquipmentsList m_hideAllEquipments;
    private final TLongHashSet m_appliedEffects;
    private String m_previousAnimStatic;
    
    public HMIHelper(final CharacterActor actor) {
        super();
        this.m_additionalAppearances = null;
        this.m_appliedEffects = new TLongHashSet();
        assert actor != null;
        this.m_actor = actor;
    }
    
    public ChangePartsList.Data getLastAdditionnalPart() {
        return (this.m_additionalParts == null) ? null : this.m_additionalParts.getLast();
    }
    
    public CharacterActor getActor() {
        return this.m_actor;
    }
    
    public void applyOnApplicationHMIAction(final WakfuRunningEffect effect, final boolean onEffectCell) {
        Iterator<HMIAction> it;
        if (effect instanceof StateRunningEffect) {
            it = ((StateRunningEffect)effect).getState().getHMIActions();
        }
        else {
            if (((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect() == null) {
                return;
            }
            it = ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect().getActionsToExecuteOnApplication();
        }
        this.apply(effect, onEffectCell, it);
    }
    
    public void applyOnUnapplicationHMIAction(final WakfuRunningEffect effect, final boolean onEffectCell) {
        final WakfuEffect genericEffect = ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect();
        if (effect.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
            final Iterator<HMIAction> stateActionsToUnapply = ((StateRunningEffect)effect).getState().getHMIActions();
            this.unapply(effect, onEffectCell, stateActionsToUnapply);
            return;
        }
        if (genericEffect == null) {
            return;
        }
        if (!this.m_appliedEffects.contains(effect.getUniqueId())) {
            return;
        }
        final List<HMIAction> hmiActions = genericEffect.getActionsOrder();
        if (hmiActions == null) {
            return;
        }
        for (int i = 0, n = hmiActions.size(); i < n; ++i) {
            final HMIAction hmiAction = hmiActions.get(i);
            if (genericEffect.isActionToExecuteOnUnapplication(hmiAction)) {
                this.apply(effect, onEffectCell, hmiAction);
            }
            if (genericEffect.isActionToStopOnUnapplication(hmiAction)) {
                this.unapply(effect, onEffectCell, hmiAction);
            }
        }
        this.m_appliedEffects.remove(effect.getUniqueId());
    }
    
    public void applyOnExecutionHMIAction(final WakfuRunningEffect effect, final boolean onEffectCell) {
        if (effect instanceof StateRunningEffect) {
            return;
        }
        final WakfuEffect genericEffect = ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect();
        if (genericEffect == null) {
            return;
        }
        if (this.m_appliedEffects.contains(effect.getUniqueId())) {
            return;
        }
        final List<HMIAction> actions = genericEffect.getActionsOrder();
        if (actions == null) {
            return;
        }
        for (int i = 0, n = actions.size(); i < n; ++i) {
            final HMIAction action = actions.get(i);
            if (genericEffect.isActionToExecuteOnExecution(action)) {
                this.apply(effect, onEffectCell, action);
            }
            if (genericEffect.isActionToStopOnExecution(action)) {
                this.unapply(effect, onEffectCell, action);
            }
        }
        this.m_appliedEffects.add(effect.getUniqueId());
    }
    
    private void shakeCamera(final HMICameraShakeAction action) {
        final CameraEffectShake shake = new CameraEffectShake();
        shake.setParams(action.getPeriod(), action.getAmplitude());
        shake.start(new TimedStrength(action.getStartDuration(), action.getMidDuration(), action.getEndDuration()));
        shake.setDirection(action.getDirection());
        final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
        shake.setCamera(scene.getIsoCamera());
        EffectManager.getInstance().addWorldEffect(shake);
    }
    
    private void playSound(final HMISoundAction soundAction) {
        WakfuSoundManager.getInstance().playSFXSound(soundAction.getSoundId());
    }
    
    private void addLightSource(final WakfuRunningEffect effect, final HMILightSourceAction light) {
        if (this.m_additionalLights == null) {
            this.m_additionalLights = new LightsList();
        }
        this.m_additionalLights.addAndApply(this.m_actor, new LightsList.Data(effect, light.getIntensity(), light.getRange(), light.isInstant()));
    }
    
    private void removeLightSource(final WakfuRunningEffect effect, final HMILightSourceAction light) {
        if (this.m_additionalLights == null) {
            return;
        }
        this.m_additionalLights.remove(this.m_actor, new LightsList.Data(effect, light.getIntensity(), light.getRange(), light.isInstant()));
        if (this.m_additionalLights.isEmpty()) {
            this.m_additionalLights = null;
        }
    }
    
    private void addAppearanceChange(final String gfxId, final WakfuRunningEffect uniqueObjectLinkedToAppearanceChange) {
        if (this.m_additionalAppearances == null) {
            this.m_additionalAppearances = new AppearanceList();
        }
        this.m_additionalAppearances.addAndApply(this.m_actor, new AppearanceList.Data(uniqueObjectLinkedToAppearanceChange, gfxId));
        this.m_actor.getCurrentAttack().endUsage(this.m_actor);
        this.m_previousAnimStatic = this.m_actor.getStaticAnimationKey();
        this.m_actor.setStaticAnimationKey("AnimStatique");
        this.m_actor.onAnmLoaded(new Runnable() {
            @Override
            public void run() {
                if (HMIHelper.this.m_actor.containsAnimation("AnimTransEffect-Debut")) {
                    HMIHelper.this.m_actor.setAnimation("AnimTransEffect-Debut");
                }
            }
        });
    }
    
    private void removeAppearanceChange(final String gfxId, final WakfuRunningEffect uniqueObjectLinkedToAppearanceChange) {
        if (this.m_additionalAppearances == null) {
            return;
        }
        final AppearanceList.Data current = this.m_additionalAppearances.getLast();
        final AppearanceList.Data removed = this.m_additionalAppearances.remove(new AppearanceList.Data(uniqueObjectLinkedToAppearanceChange, gfxId));
        this.m_actor.setStaticAnimationKey(this.m_previousAnimStatic);
        if (this.m_actor.containsAnimation("AnimTransEffect-Fin")) {
            this.m_actor.setAnimation("AnimTransEffect-Fin");
            this.m_actor.addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    HMIHelper.this.m_additionalAppearances.onRemoved(current, removed, HMIHelper.this.m_actor);
                    HMIHelper.this.m_actor.removeAnimationEndedListener(this);
                    if (HMIHelper.this.m_additionalAppearances.isEmpty()) {
                        HMIHelper.this.m_additionalAppearances = null;
                    }
                }
            });
        }
        else {
            this.m_actor.setAnimation(this.m_previousAnimStatic);
            this.m_additionalAppearances.onRemoved(current, removed, this.m_actor);
            if (this.m_additionalAppearances.isEmpty()) {
                this.m_additionalAppearances = null;
            }
        }
    }
    
    public void resetPreviousAnimStatic() {
        this.m_previousAnimStatic = null;
    }
    
    private void addSkinColorChange(final float[] color, final String partName, final WakfuRunningEffect effect) {
        final int index = Anm.getColorIndex(partName);
        if (this.m_additionalColorPart == null) {
            this.m_additionalColorPart = new ColorPartList();
        }
        this.m_additionalColorPart.addAndApply(this.m_actor, new ColorPartList.Data(effect, color, index));
    }
    
    private void removeSkinColorChange(final float[] color, final String partName, final WakfuRunningEffect effect) {
        if (this.m_additionalColorPart == null) {
            return;
        }
        final int index = Anm.getColorIndex(partName);
        this.m_additionalColorPart.remove(this.m_actor, new ColorPartList.Data(effect, color, index));
        if (this.m_additionalColorPart.isEmpty()) {
            this.m_additionalColorPart = null;
        }
    }
    
    private void addSkinPartChange(final String partSource, final String partDest, final WakfuRunningEffect effect) {
    }
    
    private void removeSkinPartChange(final WakfuRunningEffect effect) {
    }
    
    private void addCostumeChange(final int weight, final int particleId, final ArrayList<HMICostumeAction.Appearance> appearances, final WakfuRunningEffect effect) {
        if (this.m_costumes == null) {
            this.m_costumes = new CostumeList();
        }
        final CostumeListData data = new CostumeListData(effect, appearances, weight, particleId);
        if (!this.m_costumes.contains(data)) {
            this.m_costumes.addAndApply(this.m_actor, data);
        }
    }
    
    private void removeCostumeChange(final int weight, final int particleId, final ArrayList<HMICostumeAction.Appearance> appearances, final WakfuRunningEffect effect) {
        if (this.m_costumes == null) {
            return;
        }
        this.m_costumes.remove(this.m_actor, new CostumeListData(effect, appearances, weight, particleId));
        if (this.m_costumes.isEmpty()) {
            this.m_costumes = null;
        }
    }
    
    private void addSkinPartOtherChange(final String appearanceId, final int weight, final String[] partsToChange, final WakfuRunningEffect effect) {
        if (this.m_additionalParts == null) {
            this.m_additionalParts = new ChangePartsList();
        }
        final ChangePartsList.Data data = new ChangePartsList.Data(effect, appearanceId, weight, partsToChange);
        if (!this.m_additionalParts.contains(data)) {
            this.m_additionalParts.addAndApply(this.m_actor, data);
        }
    }
    
    private void removeSkinPartOtherChange(final String appearanceId, final int weight, final String[] partsToChange, final WakfuRunningEffect effect) {
        if (this.m_additionalParts == null) {
            return;
        }
        this.m_additionalParts.remove(this.m_actor, new ChangePartsList.Data(effect, appearanceId, weight, partsToChange));
        if (this.m_additionalParts.isEmpty()) {
            this.m_additionalParts = null;
        }
    }
    
    private void addSkinPartVisibility(final boolean visible, final String[] partsToChange, final WakfuRunningEffect effect) {
        if (this.m_additionnalVisibleParts == null) {
            this.m_additionnalVisibleParts = new VisibilityPartsList();
        }
        this.m_additionnalVisibleParts.addAndApply(this.m_actor, new VisibilityPartsList.Data(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect(), visible, partsToChange));
    }
    
    private void removeSkinPartVisibility(final boolean visible, final String[] partsToChange, final WakfuRunningEffect effect) {
        if (this.m_additionnalVisibleParts == null) {
            return;
        }
        this.m_additionnalVisibleParts.remove(this.m_actor, new VisibilityPartsList.Data(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect(), visible, partsToChange));
        if (this.m_additionnalVisibleParts.isEmpty()) {
            this.m_additionnalVisibleParts = null;
        }
    }
    
    private void addAnimSuffixChange(final String animSuffix, final WakfuRunningEffect effect) {
        if (this.m_additionalAnimSuffix == null) {
            this.m_additionalAnimSuffix = new AnimSuffixList();
        }
        this.m_additionalAnimSuffix.addAndApply(this.m_actor, new AnimSuffixList.Data(effect, animSuffix));
    }
    
    private void removeAnimSuffixChange(final String animSuffix, final WakfuRunningEffect effect) {
        if (this.m_additionalAnimSuffix == null) {
            return;
        }
        this.m_additionalAnimSuffix.remove(this.m_actor, new AnimSuffixList.Data(effect, animSuffix));
        if (this.m_additionalAnimSuffix.isEmpty()) {
            this.m_additionalAnimSuffix = null;
        }
    }
    
    private void addAnimStaticChange(final String animStaticKey, final WakfuRunningEffect effect) {
        if (this.m_additionalAnimStatic == null) {
            this.m_additionalAnimStatic = new ChangeAnimStaticList();
        }
        this.m_additionalAnimStatic.addAndApply(this.m_actor, new ChangeAnimStaticList.Data(effect, animStaticKey));
    }
    
    private void removeAnimStaticChange(final String animStaticKey, final WakfuRunningEffect effect) {
        if (this.m_additionalAnimStatic == null) {
            return;
        }
        this.m_additionalAnimStatic.remove(this.m_actor, new ChangeAnimStaticList.Data(effect, animStaticKey));
        if (this.m_additionalAnimStatic.isEmpty()) {
            this.m_additionalAnimStatic = null;
        }
    }
    
    private void addAnimChange(final String animStaticName, final WakfuRunningEffect effect) {
        if (this.m_additionalAnim == null) {
            this.m_additionalAnim = new ChangeAnimList();
        }
        this.m_additionalAnim.addAndApply(this.m_actor, new ChangeAnimList.Data(effect, animStaticName));
    }
    
    private void removeAnimChange(final String animStaticName, final WakfuRunningEffect effect) {
        if (this.m_additionalAnim == null) {
            return;
        }
        this.m_additionalAnim.remove(this.m_actor, new ChangeAnimList.Data(effect, animStaticName));
        if (this.m_additionalAnim.isEmpty()) {
            this.m_additionalAnim = null;
        }
    }
    
    private void addParticleSystemFromHMIAction(final int systemId, final boolean alwaysActivated, final ParticleLocalisation localisation, final boolean isInstant, final Point3 forcedPos, final WakfuRunningEffect effect) {
        if (this.m_additionalParticles == null) {
            this.m_additionalParticles = new ParticlesList();
        }
        this.m_additionalParticles.addAndApply(this.m_actor, ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect(), systemId, localisation, forcedPos, alwaysActivated, isInstant);
    }
    
    private void removeParticleFromHMIAction(final int systemId, final boolean alwaysActivated, final ParticleLocalisation localisation, final boolean isInstant, final Point3 forcedPos, final WakfuRunningEffect effect) {
        if (this.m_additionalParticles == null) {
            return;
        }
        this.m_additionalParticles.remove(this.m_actor, ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect(), systemId, localisation, forcedPos, alwaysActivated, isInstant);
        if (this.m_additionalParticles.isEmpty()) {
            this.m_additionalParticles = null;
        }
    }
    
    private void setCurrentParticleVisibility(final boolean visible) {
        if (this.m_additionalParticles == null) {
            return;
        }
        this.m_additionalParticles.setCurrentParticleVisibility(visible);
    }
    
    public void showHMIActionParticleSystems() {
        this.setCurrentParticleVisibility(true);
    }
    
    public void hideHMIActionParticleSystem() {
        this.setCurrentParticleVisibility(false);
    }
    
    private void setUiProperty(final WakfuRunningEffect effect, final String uiObjectId, final String propertyName, final String propertyValue) {
        UiPropertyHelper.initializeAndSet(effect, uiObjectId, propertyName, propertyValue);
    }
    
    private void removeUiProperty(final String uiObjectId, final String propertyName, final String propertyValue) {
        UiPropertyHelper.remove(uiObjectId, propertyName, propertyValue);
    }
    
    private void addAltitudeIncrement(final WakfuRunningEffect effect, final float altitude) {
        this.m_actor.applyAltitudeIncrement(altitude);
    }
    
    private void removeAltitudeIncrement(final WakfuRunningEffect effect, final float altitude) {
        this.m_actor.unapplyAltitudeIncrement(altitude);
    }
    
    private void addLinkedMobile(final String gfxId, final String anim, final WakfuRunningEffect effect) {
        if (this.m_additionalLinkedMobile == null) {
            this.m_additionalLinkedMobile = new LinkMobileList();
        }
        this.m_additionalLinkedMobile.addAndApply(this.m_actor, new LinkMobileList.Data(effect, gfxId, anim));
    }
    
    private void removeLinkedMobile(final String gfxId, final String anim, final WakfuRunningEffect effect) {
        if (this.m_additionalLinkedMobile == null) {
            return;
        }
        this.m_additionalLinkedMobile.remove(this.m_actor, new LinkMobileList.Data(effect, gfxId, anim));
        if (this.m_additionalLinkedMobile.isEmpty()) {
            this.m_additionalLinkedMobile = null;
        }
    }
    
    private void addChangeMovementStyle(final WakfuRunningEffect effect, final String walkStyle, final String runStyle) {
        if (this.m_movementSelector != null) {
            this.m_movementSelector.resetMovementSelector(this.m_actor);
        }
        this.m_movementSelector = new CustomMovementSelector(this.m_actor.getMovementSelector(), MovementStyleManager.getInstance().getMovementStyle(walkStyle), MovementStyleManager.getInstance().getMovementStyle(runStyle));
        this.m_actor.setMovementSelector(this.m_movementSelector);
    }
    
    private void removeChangeMovementStyle(final WakfuRunningEffect effect, final String walkStyle, final String runStyle) {
        if (this.m_movementSelector != null && this.m_actor.getMovementSelector() == this.m_movementSelector) {
            this.m_movementSelector.resetMovementSelector(this.m_actor);
        }
        this.m_movementSelector = null;
    }
    
    private void addHideAllEquipments(final WakfuRunningEffect effect) {
        if (this.m_hideAllEquipments == null) {
            this.m_hideAllEquipments = new HideAllEquipmentsList();
        }
        this.m_hideAllEquipments.addAndApply(this.m_actor, new HideAllEquipmentsListData(effect));
    }
    
    private void removeHideAllEquipments(final WakfuRunningEffect effect) {
        if (this.m_hideAllEquipments == null) {
            return;
        }
        this.m_hideAllEquipments.remove(this.m_actor, new HideAllEquipmentsListData(effect));
        if (this.m_hideAllEquipments.isEmpty()) {
            this.m_hideAllEquipments = null;
        }
    }
    
    private void addSetMonsterSkin(final WakfuRunningEffect effect, final String monsterId, final boolean displayEquipment) {
        if (this.m_monsterSkins == null) {
            this.m_monsterSkins = new SetMonsterSkinList();
        }
        this.m_monsterSkins.addAndApply(this.m_actor, new SetMonsterSkinList.Data(effect, monsterId, displayEquipment));
    }
    
    private void removeSetMonsterSkin(final WakfuRunningEffect effect, final String monsterId, final boolean displayEquipment) {
        if (this.m_monsterSkins == null) {
            return;
        }
        this.m_monsterSkins.remove(this.m_actor, new SetMonsterSkinList.Data(effect, monsterId, displayEquipment));
        if (this.m_monsterSkins.isEmpty()) {
            this.m_monsterSkins = null;
        }
    }
    
    private void apply(final WakfuRunningEffect effect, final boolean onEffectCell, final Iterator<HMIAction> hmiActionIter) {
        if (!hmiActionIter.hasNext() || this.m_appliedEffects.contains(effect.getUniqueId())) {
            return;
        }
        while (hmiActionIter.hasNext()) {
            final HMIAction hmiAction = hmiActionIter.next();
            this.apply(effect, onEffectCell, hmiAction);
        }
        this.m_appliedEffects.add(effect.getUniqueId());
    }
    
    private void apply(final WakfuRunningEffect effect, final boolean onEffectCell, final HMIAction hmiAction) {
        if (!isValidTarget(effect, hmiAction)) {
            return;
        }
        switch (hmiAction.getType()) {
            case PARTICLE_SYSTEM: {
                final HMIParticleSystemAction aps = (HMIParticleSystemAction)hmiAction;
                final Point3 forcedPos = onEffectCell ? effect.getTargetCell() : null;
                this.addParticleSystemFromHMIAction(aps.getParticleSystemId(), aps.isAlwaysActivated(), aps.getLocalisation(), aps.isInstant(), forcedPos, effect);
                break;
            }
            case LIGHT_SOURCE: {
                final HMILightSourceAction light = (HMILightSourceAction)hmiAction;
                this.addLightSource(effect, light);
                break;
            }
            case APPEARANCE_CHANGE: {
                final HMIChangeAppearanceAction changeAppearance = (HMIChangeAppearanceAction)hmiAction;
                this.addAppearanceChange(changeAppearance.getAppearanceId(), effect);
                break;
            }
            case CAMERA_SHAKE: {
                final HMICameraShakeAction cameraShake = (HMICameraShakeAction)hmiAction;
                this.shakeCamera(cameraShake);
                break;
            }
            case SOUND: {
                final HMISoundAction sound = (HMISoundAction)hmiAction;
                this.playSound(sound);
                break;
            }
            case SKIN_COLOR_CHANGE: {
                final HMIChangeSkinColorAction changeColorSkin = (HMIChangeSkinColorAction)hmiAction;
                this.addSkinColorChange(changeColorSkin.getColor(), changeColorSkin.getPartName(), effect);
                break;
            }
            case SKIN_PART_OTHER_CHANGE: {
                final HMIChangeSkinPartOtherAction changePartSkin = (HMIChangeSkinPartOtherAction)hmiAction;
                this.addSkinPartOtherChange(changePartSkin.getAppearanceId(), changePartSkin.getWeight(), changePartSkin.getPartsToChange(), effect);
                break;
            }
            case COSTUME: {
                final HMICostumeAction costumeAction = (HMICostumeAction)hmiAction;
                this.addCostumeChange(costumeAction.getWeight(), costumeAction.getParticleId(), costumeAction.getAppearances(), effect);
                break;
            }
            case ANIM_SUFFIX_CHANGE: {
                final HMIChangeAnimSuffixAction changeAnimSuffix = (HMIChangeAnimSuffixAction)hmiAction;
                this.addAnimSuffixChange(changeAnimSuffix.getAnimSuffix(), effect);
                break;
            }
            case ANIM_STATIC_CHANGE: {
                final HMIChangeAnimStaticAction changeAnimStatic = (HMIChangeAnimStaticAction)hmiAction;
                this.addAnimStaticChange(changeAnimStatic.getAnimStaticKey(), effect);
                break;
            }
            case ANIM_CHANGE: {
                final HMIChangeAnimAction changeAnim = (HMIChangeAnimAction)hmiAction;
                this.addAnimChange(changeAnim.getAnimName(), effect);
                break;
            }
            case SKIN_PART_VISIBILITY: {
                final HMIVisibilitySkinPartAction visibilitySkinPartAction = (HMIVisibilitySkinPartAction)hmiAction;
                this.addSkinPartVisibility(visibilitySkinPartAction.getVisibility(), visibilitySkinPartAction.getParts(), effect);
                break;
            }
            case SET_UI_PROPERTY: {
                final HMISetUiProperty setUiProperty = (HMISetUiProperty)hmiAction;
                this.setUiProperty(effect, setUiProperty.getUiObjectId(), setUiProperty.getPropertyName(), setUiProperty.getPropertyValue());
                break;
            }
            case LINK_MOBILE: {
                final HMILinkMobileAction linkMobileAction = (HMILinkMobileAction)hmiAction;
                this.addLinkedMobile(linkMobileAction.getGfxId(), linkMobileAction.getAnim(), effect);
                break;
            }
            case INCREMENT_ALTITUDE: {
                final HMIIncrementAltitudeAction hmiIncrementAltitudeAction = (HMIIncrementAltitudeAction)hmiAction;
                this.addAltitudeIncrement(effect, hmiIncrementAltitudeAction.getDeltaAltitude());
                break;
            }
            case CHANGE_MOVEMENT_STYLE: {
                final HMIChangeMovementStyle changeMovementStyle = (HMIChangeMovementStyle)hmiAction;
                this.addChangeMovementStyle(effect, changeMovementStyle.getWalkStyle(), changeMovementStyle.getRunStyle());
                break;
            }
            case SET_MONSTER_SKIN: {
                final HMISetMonsterSkinAction setMonsterSkin = (HMISetMonsterSkinAction)hmiAction;
                this.addSetMonsterSkin(effect, setMonsterSkin.getMonsterId(), setMonsterSkin.isDisplayEquipment());
                break;
            }
            case HIDE_ALL_EQUIPMENTS: {
                this.addHideAllEquipments(effect);
                break;
            }
        }
    }
    
    private void unapply(final WakfuRunningEffect effect, final boolean onEffectCell, final Iterator<HMIAction> hmiActionIter) {
        if (!hmiActionIter.hasNext() || !this.m_appliedEffects.contains(effect.getUniqueId())) {
            return;
        }
        while (hmiActionIter.hasNext()) {
            final HMIAction hmiAction = hmiActionIter.next();
            this.unapply(effect, onEffectCell, hmiAction);
        }
        this.m_appliedEffects.remove(effect.getUniqueId());
    }
    
    private void unapply(final WakfuRunningEffect effect, final boolean onEffectCell, final HMIAction hmiAction) {
        if (!isValidTarget(effect, hmiAction)) {
            return;
        }
        switch (hmiAction.getType()) {
            case PARTICLE_SYSTEM: {
                final HMIParticleSystemAction aps = (HMIParticleSystemAction)hmiAction;
                final Point3 forcedPos = onEffectCell ? effect.getTargetCell() : null;
                this.removeParticleFromHMIAction(aps.getParticleSystemId(), aps.isAlwaysActivated(), aps.getLocalisation(), aps.isInstant(), forcedPos, effect);
                break;
            }
            case LIGHT_SOURCE: {
                final HMILightSourceAction light = (HMILightSourceAction)hmiAction;
                this.removeLightSource(effect, light);
            }
            case CAMERA_SHAKE: {}
            case APPEARANCE_CHANGE: {
                final HMIChangeAppearanceAction changeAppearance = (HMIChangeAppearanceAction)hmiAction;
                this.removeAppearanceChange(changeAppearance.getAppearanceId(), effect);
                break;
            }
            case SKIN_COLOR_CHANGE: {
                final HMIChangeSkinColorAction changeColorSkin = (HMIChangeSkinColorAction)hmiAction;
                this.removeSkinColorChange(changeColorSkin.getColor(), changeColorSkin.getPartName(), effect);
                break;
            }
            case SKIN_PART_OTHER_CHANGE: {
                final HMIChangeSkinPartOtherAction changePartSkin = (HMIChangeSkinPartOtherAction)hmiAction;
                this.removeSkinPartOtherChange(changePartSkin.getAppearanceId(), changePartSkin.getWeight(), changePartSkin.getPartsToChange(), effect);
                break;
            }
            case COSTUME: {
                final HMICostumeAction costume = (HMICostumeAction)hmiAction;
                this.removeCostumeChange(costume.getWeight(), costume.getParticleId(), costume.getAppearances(), effect);
                break;
            }
            case ANIM_SUFFIX_CHANGE: {
                final HMIChangeAnimSuffixAction changeAnimSuffix = (HMIChangeAnimSuffixAction)hmiAction;
                this.removeAnimSuffixChange(changeAnimSuffix.getAnimSuffix(), effect);
                break;
            }
            case ANIM_STATIC_CHANGE: {
                final HMIChangeAnimStaticAction changeAnimStatic = (HMIChangeAnimStaticAction)hmiAction;
                this.removeAnimStaticChange(changeAnimStatic.getAnimStaticKey(), effect);
                break;
            }
            case ANIM_CHANGE: {
                final HMIChangeAnimAction changeAnim = (HMIChangeAnimAction)hmiAction;
                this.removeAnimChange(changeAnim.getAnimName(), effect);
                break;
            }
            case SKIN_PART_VISIBILITY: {
                final HMIVisibilitySkinPartAction visibilitySkinPartAction = (HMIVisibilitySkinPartAction)hmiAction;
                this.removeSkinPartVisibility(visibilitySkinPartAction.getVisibility(), visibilitySkinPartAction.getParts(), effect);
                break;
            }
            case SET_UI_PROPERTY: {
                final HMISetUiProperty setUiProperty = (HMISetUiProperty)hmiAction;
                this.removeUiProperty(setUiProperty.getUiObjectId(), setUiProperty.getPropertyName(), setUiProperty.getPropertyValue());
                break;
            }
            case LINK_MOBILE: {
                final HMILinkMobileAction linkMobileAction = (HMILinkMobileAction)hmiAction;
                this.removeLinkedMobile(linkMobileAction.getGfxId(), linkMobileAction.getAnim(), effect);
                break;
            }
            case INCREMENT_ALTITUDE: {
                final HMIIncrementAltitudeAction hmiIncrementAltitudeAction = (HMIIncrementAltitudeAction)hmiAction;
                this.removeAltitudeIncrement(effect, hmiIncrementAltitudeAction.getDeltaAltitude());
                break;
            }
            case CHANGE_MOVEMENT_STYLE: {
                final HMIChangeMovementStyle changeMovementStyle = (HMIChangeMovementStyle)hmiAction;
                this.removeChangeMovementStyle(effect, changeMovementStyle.getWalkStyle(), changeMovementStyle.getRunStyle());
                break;
            }
            case SET_MONSTER_SKIN: {
                final HMISetMonsterSkinAction setMonsterSkin = (HMISetMonsterSkinAction)hmiAction;
                this.removeSetMonsterSkin(effect, setMonsterSkin.getMonsterId(), setMonsterSkin.isDisplayEquipment());
                break;
            }
            case HIDE_ALL_EQUIPMENTS: {
                this.removeHideAllEquipments(effect);
                break;
            }
        }
    }
    
    private static boolean isValidTarget(final WakfuRunningEffect effect, final HMIAction hmiAction) {
        if (hmiAction.isTargetOnly()) {
            final long id = effect.getTarget().getId();
            WakfuClientInstance.getInstance();
            if (id != WakfuClientInstance.getGameEntity().getLocalPlayer().getId()) {
                return false;
            }
        }
        return true;
    }
    
    public void clear(final CharacterActor actor) {
        this.m_appliedEffects.clear();
        if (this.m_additionalAppearances != null) {
            this.m_additionalAppearances.clear(actor);
            this.m_additionalAppearances = null;
        }
        if (this.m_additionalColorPart != null) {
            this.m_additionalColorPart.clear(actor);
            this.m_additionalColorPart = null;
        }
        if (this.m_additionalParts != null) {
            this.m_additionalParts.clear(actor);
            this.m_additionalParts = null;
        }
        if (this.m_additionnalVisibleParts != null) {
            this.m_additionnalVisibleParts.clear(actor);
            this.m_additionnalVisibleParts = null;
        }
        if (this.m_costumes != null) {
            this.m_costumes.clear(actor);
            this.m_costumes = null;
        }
        if (this.m_additionalAnimSuffix != null) {
            this.m_additionalAnimSuffix.clear(actor);
            this.m_additionalAnimSuffix = null;
        }
        if (this.m_additionalAnimStatic != null) {
            this.m_additionalAnimStatic.clear(actor);
            this.m_additionalAnimStatic = null;
        }
        if (this.m_additionalAnim != null) {
            this.m_additionalAnim.clear(actor);
            this.m_additionalAnim = null;
        }
        if (this.m_additionalLinkedMobile != null) {
            this.m_additionalLinkedMobile.clear(actor);
            this.m_additionalLinkedMobile = null;
        }
        if (this.m_additionalParticles != null) {
            this.m_additionalParticles.clear(actor);
            this.m_additionalParticles = null;
        }
        if (this.m_additionalLights != null) {
            this.m_additionalLights.clear(actor);
            this.m_additionalLights = null;
        }
    }
    
    public void applyAppearenceModifiersOn(final CharacterActor actor) {
        if (this.m_additionalAppearances != null && !this.m_additionalAppearances.isEmpty()) {
            final AppearanceList.Data data = this.m_additionalAppearances.getLast();
            data.apply(actor, false);
        }
        if (this.m_additionalParts != null && !this.m_additionalParts.isEmpty()) {
            for (final ChangePartsList.Data additionnalPart : this.m_additionalParts.getSorted()) {
                additionnalPart.apply(actor, false);
            }
        }
        if (this.m_additionnalVisibleParts != null && !this.m_additionnalVisibleParts.isEmpty()) {
            for (final VisibilityPartsList.Data additionnalVisiblePart : this.m_additionnalVisibleParts) {
                additionnalVisiblePart.apply(actor);
            }
        }
        if (this.m_costumes != null && !this.m_costumes.isEmpty()) {
            this.m_costumes.getVisibleCostume().apply(actor, false);
        }
    }
    
    public void applyAppearenceColorModifiersOn(final CharacterActor actor) {
        if (this.m_additionalColorPart != null && !this.m_additionalColorPart.isEmpty()) {
            final Iterator<ColorPartList.Data> iter = this.m_additionalColorPart.iterator();
            while (iter.hasNext()) {
                iter.next().apply(actor, false);
            }
        }
    }
    
    public void reload() {
        this.reload((com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List<com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List.AdditionnalData>)this.m_additionalParticles);
        this.reload((com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List<com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List.AdditionnalData>)this.m_additionalLights);
    }
    
    public void reloadStaticChanges() {
        this.reload((com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List<com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List.AdditionnalData>)this.m_additionalAnimStatic);
    }
    
    private <T extends com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List.AdditionnalData> void reload(final com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.List<T> list) {
        if (list == null) {
            return;
        }
        int size = list.size();
        while (size-- > 0) {
            final T data = list.removeFirst();
            list.onRemoved(data, data, this.m_actor);
            list.addAndApply(this.m_actor, data);
        }
    }
    
    public void copyAndApplyAllTo(final CharacterActor actor) {
        if (actor == null) {
            return;
        }
        this.copyAndApplyParticlesTo(actor);
        this.copyAndApplyPartsChangesTo(actor);
        this.copyAndApplyCostumesTo(actor);
        this.copyAndApplyLightsTo(actor);
        this.copyAndApplyColorsTo(actor);
        this.copyAndApplyPartsVisibilityTo(actor);
    }
    
    public void copyAndApplyPartsVisibilityTo(@NotNull final CharacterActor actor) {
        final HMIHelper actorHelper = actor.getHmiHelper();
        if (actorHelper == null) {
            return;
        }
        if (this.m_additionnalVisibleParts != null) {
            if (actorHelper.m_additionnalVisibleParts == null) {
                actorHelper.m_additionnalVisibleParts = new VisibilityPartsList();
            }
            this.m_additionnalVisibleParts.copyToOtherActor(actor, actorHelper.m_additionnalVisibleParts);
        }
    }
    
    public void copyAndApplyColorsTo(final CharacterActor actor) {
        final HMIHelper actorHelper = actor.getHmiHelper();
        if (actorHelper == null) {
            return;
        }
        if (this.m_additionalColorPart != null) {
            if (actorHelper.m_additionalColorPart == null) {
                actorHelper.m_additionalColorPart = new ColorPartList();
            }
            this.m_additionalColorPart.copyToOtherActor(actor, actorHelper.m_additionalColorPart);
        }
    }
    
    public void copyAndApplyLightsTo(final CharacterActor actor) {
        final HMIHelper actorHelper = actor.getHmiHelper();
        if (actorHelper == null) {
            return;
        }
        if (this.m_additionalLights != null) {
            if (actorHelper.m_additionalLights == null) {
                actorHelper.m_additionalLights = new LightsList();
            }
            this.m_additionalLights.copyToOtherActor(actor, actorHelper.m_additionalLights);
        }
    }
    
    public void copyAndApplyPartsChangesTo(final CharacterActor actor) {
        final HMIHelper actorHelper = actor.getHmiHelper();
        if (actorHelper == null) {
            return;
        }
        if (this.m_additionalParts != null) {
            if (actorHelper.m_additionalParts == null) {
                actorHelper.m_additionalParts = new ChangePartsList();
            }
            this.m_additionalParts.copyToOtherActor(actor, actorHelper.m_additionalParts);
        }
    }
    
    public void copyAndApplyCostumesTo(final CharacterActor actor) {
        final HMIHelper actorHelper = actor.getHmiHelper();
        if (actorHelper == null) {
            return;
        }
        if (this.m_costumes != null) {
            if (actorHelper.m_costumes == null) {
                actorHelper.m_costumes = new CostumeList();
            }
            this.m_costumes.copyToOtherActor(actor, actorHelper.m_costumes);
        }
    }
    
    public void copyAndApplyParticlesTo(final CharacterActor actor) {
        final HMIHelper actorHelper = actor.getHmiHelper();
        if (actorHelper == null) {
            return;
        }
        if (this.m_additionalParticles != null) {
            if (actorHelper.m_additionalParticles == null) {
                actorHelper.m_additionalParticles = new ParticlesList();
            }
            this.m_additionalParticles.copyToOtherActor(actor, actorHelper.m_additionalParticles, new DataCopyFilter<ParticleListData>() {
                @Override
                public boolean hasToBeCopied(final ParticleListData data) {
                    return data.m_forcedPos == null;
                }
            });
        }
    }
    
    public boolean hasAnimStaticChanges() {
        return this.m_additionalAnimStatic != null && !this.m_additionalAnimStatic.isEmpty();
    }
    
    public boolean hasAnimChanges() {
        return this.m_additionalAnim != null && !this.m_additionalAnim.isEmpty();
    }
    
    public boolean hasMonsterSkin() {
        return this.m_monsterSkins != null && !this.m_monsterSkins.isEmpty();
    }
    
    public boolean hasAppearanceChanges() {
        return this.m_additionalAppearances != null && !this.m_additionalAppearances.isEmpty();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIHelper.class);
    }
}
