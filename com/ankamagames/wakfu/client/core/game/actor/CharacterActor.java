package com.ankamagames.wakfu.client.core.game.actor;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.alea.graphics.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.wakfu.client.sound.validator.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.alea.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.actions.*;
import com.ankamagames.wakfu.client.alea.graphics.anm.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class CharacterActor extends Actor implements CharacterInfoPropertyEventsHandler
{
    private static final Logger m_logger;
    public static final boolean DEBUG_MODE = false;
    private final CharacterInfo m_characterInfo;
    private final FightParticleHelper m_fightParticleHelper;
    private final HMIHelper m_hmiHelper;
    private boolean m_inFightBubble;
    private AttackType m_currentAttack;
    private float m_altitudeIncrement;
    private AnmGuildBlazonApplicator m_guildBlasonApplicator;
    private boolean m_doNotShowAgain;
    private MonsterSkin m_monsterSkin;
    private boolean m_hideAllEquipments;
    
    public CharacterActor(final CharacterInfo character) {
        super(character.getId());
        this.m_fightParticleHelper = new FightParticleHelper(this);
        this.m_hmiHelper = new HMIHelper(this);
        this.m_currentAttack = NoneAttack.getInstance();
        this.m_altitudeIncrement = 0.0f;
        this.m_guildBlasonApplicator = null;
        if (character.isLocalPlayer()) {
            this.m_soundValidator = SoundValidatorAll.INSTANCE;
            this.setDeltaZ(LayerOrder.PLAYER.getDeltaZ());
        }
        else {
            this.m_soundValidator = new CharacterInfoSoundValidator(character);
            this.setDeltaZ(LayerOrder.MOBILE.getDeltaZ());
        }
        (this.m_characterInfo = character).registerCharacterInfoPropertyEventsHandler(this);
        this.setWatchersVisible(this.isVisible());
        this.onPositionChanged(this.m_characterInfo);
        this.onAppearanceChanged(this.m_characterInfo);
        this.onDirectionChanged(this.m_characterInfo);
        this.removeAllSelectionChangedListener();
        this.addSelectionChangedListener(new CharacterActorSelectionChangeListener(this));
    }
    
    @Override
    public int getCurrentFightId() {
        return this.getCharacterInfo().getCurrentFightId();
    }
    
    public CharacterInfo getCharacterInfo() {
        return this.m_characterInfo;
    }
    
    public void onAppearanceChangedExternally() {
        this.m_characterInfo.fireActorAppearanceChanged();
    }
    
    @Override
    public void onAppearanceChanged(final BasicCharacterInfo info) {
        if (info.getGfxId() != 0) {
            this.setGfx(String.valueOf(info.getGfxId()));
        }
    }
    
    @Override
    public void onIdentityChanged(final BasicCharacterInfo info) {
    }
    
    @Override
    public void onNameChanged(final BasicCharacterInfo info) {
    }
    
    @Override
    public void onSexChanged(final BasicCharacterInfo info) {
    }
    
    @Override
    public void onSymbiotChanged(final BasicCharacterInfo info) {
    }
    
    @Override
    public void onBreedChanged(final BasicCharacterInfo info) {
    }
    
    @Override
    public void onPositionChanged(final BasicCharacterInfo info) {
        this.setWorldPosition(info.getWorldCellX(), info.getWorldCellY(), info.getWorldCellAltitude());
    }
    
    @Override
    public void onDirectionChanged(final BasicCharacterInfo info) {
        this.setDirection(info.getDirection());
    }
    
    @Override
    public void onCharacteristicChanged(final BasicCharacterInfo info) {
    }
    
    @Override
    public void onPropertyChanged(final BasicCharacterInfo info) {
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        this.getCharacterInfo().refreshDisplayEquipment();
    }
    
    @Override
    public long getId() {
        return super.getId();
    }
    
    @Override
    public byte getPhysicalRadius() {
        if (this.m_characterInfo != null) {
            return this.m_characterInfo.getPhysicalRadius();
        }
        return super.getPhysicalRadius();
    }
    
    @Override
    public short getJumpCapacity() {
        if (this.m_characterInfo != null) {
            return this.m_characterInfo.getJumpCapacity();
        }
        return super.getJumpCapacity();
    }
    
    @Override
    public short getHeight() {
        if (this.m_characterInfo != null) {
            return this.m_characterInfo.getHeight();
        }
        return super.getHeight();
    }
    
    @Override
    public final boolean isVisible() {
        if (this.m_doNotShowAgain) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return true;
        }
        final Fight fight = localPlayer.getCurrentOrObservedFight();
        if (fight != null) {
            final FightMap fightMap = fight.getFightMap();
            if (!fight.isFighterCurrentlyInFight(this.m_characterInfo) && fight.isHideExternalCharacter() && fightMap.isInsideMapShape(this.getWorldCellX(), this.getWorldCellY())) {
                if (!this.m_inFightBubble) {
                    this.m_inFightBubble = true;
                    this.m_visibleChanged = true;
                    this.fireVisibilityChanged(false, VisibleChangedListener.VisibleChangedCause.FIGHT);
                }
                return false;
            }
        }
        if (this.m_inFightBubble) {
            this.m_inFightBubble = false;
            this.fireVisibilityChanged(this.m_visibleChanged = true, VisibleChangedListener.VisibleChangedCause.FIGHT);
        }
        return !this.m_characterInfo.isFleeing() && super.isVisible();
    }
    
    @Override
    protected boolean allowMoboSterileMovement() {
        return !(this.m_characterInfo instanceof NonPlayerCharacter);
    }
    
    @Override
    protected boolean allowGaps() {
        if (!(this.m_characterInfo instanceof PlayerCharacter)) {
            return false;
        }
        final PlayerCharacter characterInfo = (PlayerCharacter)this.m_characterInfo;
        return characterInfo.hasProperty(WorldPropertyType.RELIC_SPECIAL_JUMP) && !this.m_hmiHelper.hasAnimStaticChanges() && !this.m_hmiHelper.hasAnimChanges() && !this.m_hmiHelper.hasAppearanceChanges();
    }
    
    @Override
    public int getMaxWalkDistance() {
        return this.m_characterInfo.getMaxWalkDistance();
    }
    
    @Override
    public void setPath(final PathFindResult pathResult, final boolean recompute, final boolean useSpline) {
        super.setPath(pathResult, recompute, useSpline);
        final int[] lastStep = pathResult.getLastStep();
        if (lastStep != null && lastStep.length == 3) {
            this.getCharacterInfo().setPositionWithoutNotifyActor(lastStep[0], lastStep[1], (short)lastStep[2]);
        }
        if (this.m_characterInfo.getCurrentFight() != null && this.m_characterInfo.getBreedId() == AvatarBreed.XELOR.getBreedId()) {
            this.m_fightParticleHelper.hideTeamParticleSystem();
            this.m_fightParticleHelper.hideActiveParticleSystem();
            this.m_fightParticleHelper.hideDirectionParticleSystem();
            this.addEndPositionListener(new MobileEndPathListener() {
                @Override
                public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                    mobile.removeEndPositionListener(this);
                    if (CharacterActor.this.m_characterInfo.getCurrentFight() != null) {
                        CharacterActor.this.m_fightParticleHelper.showTeamParticleSystem();
                        CharacterActor.this.m_fightParticleHelper.showActiveParticleSystem();
                        CharacterActor.this.m_fightParticleHelper.showDirectionParticleSystem();
                    }
                }
            });
        }
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        if (direction == null) {
            CharacterActor.m_logger.error((Object)"Unable to set a Direction8 to null", (Throwable)new Exception());
            return;
        }
        super.setDirection(direction);
        this.getCharacterInfo().setDirectionWithoutNotifyActor(direction);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final Fight currentFight = this.getCharacterInfo().getCurrentFight();
        if (this.getCurrentFightId() != -1 && this.getCurrentFightId() == localPlayer.getCurrentOrObservedFightId() && this.getCharacterInfo().isOnFight() && currentFight != null && !currentFight.isEnding()) {
            this.addDirectionParticleSystem(direction);
        }
    }
    
    @Override
    public boolean addToScene(final AleaWorldScene scene) {
        if (!super.addToScene(scene)) {
            return false;
        }
        if (this.m_characterInfo != null && this.m_characterInfo.hasProperty(FightPropertyType.DISPLAYED_LIKE_A_DECORATION)) {
            this.hideAllParticleSystems();
        }
        return true;
    }
    
    @Override
    public byte getAvailableDirections() {
        if (this.m_characterInfo.isOnFight()) {
            return 4;
        }
        return super.getAvailableDirections();
    }
    
    public void setDirectionWithNotification(final Direction8 direction) {
        this.setDirection(direction);
        final ActorDirectionChangeRequestMessage netMessage = new ActorDirectionChangeRequestMessage(direction);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    public void highlightCharacter() {
        MobileColorizeHelper.onHover(this);
    }
    
    public void unHighlightCharacter() {
        this.resetColor();
    }
    
    public void applyAltitudeIncrement(final float deltaAltitude) {
        this.m_altitudeIncrement += deltaAltitude;
        this.m_screenPositionNeedsRecompute = true;
    }
    
    public void unapplyAltitudeIncrement(final float deltaAltitude) {
        this.m_altitudeIncrement -= deltaAltitude;
        this.m_screenPositionNeedsRecompute = true;
    }
    
    @Override
    public float getAltitude() {
        return super.getAltitude() + this.m_altitudeIncrement;
    }
    
    @Override
    public void onAnimatedObjectActionFlag(final ArrayList<AnmAction> actions) {
        final AnimatedInteractiveElement hitElement = this.getCharacterInfo().getCurrentInteractiveElement();
        for (int actionsSize = actions.size(), i = 0; i < actionsSize; ++i) {
            final AnmAction action1 = actions.get(i);
            if (hitElement == null || !hitElement.playAction(action1)) {
                action1.run(this);
            }
        }
    }
    
    @Override
    public DefaultAnmActionFactory getActionFactory() {
        return WakfuAnmActionFactory.INSTANCE;
    }
    
    public void showAllParticleSystems() {
        this.m_fightParticleHelper.showActiveParticleSystem();
        this.m_fightParticleHelper.showTeamParticleSystem();
        this.m_fightParticleHelper.showDirectionParticleSystem();
        this.m_hmiHelper.showHMIActionParticleSystems();
    }
    
    public void hideAllParticleSystems() {
        this.m_fightParticleHelper.hideActiveParticleSystem();
        this.m_fightParticleHelper.hideTeamParticleSystem();
        this.m_fightParticleHelper.hideDirectionParticleSystem();
        this.m_hmiHelper.hideHMIActionParticleSystem();
    }
    
    @Override
    public void hideDirectionParticleSystem() {
        this.m_fightParticleHelper.hideDirectionParticleSystem();
    }
    
    @Override
    public void showDirectionParticleSystem() {
        this.m_fightParticleHelper.showDirectionParticleSystem();
    }
    
    public void clearAllParticleSystems() {
        this.m_fightParticleHelper.clearAllParticleSystems();
    }
    
    @Override
    public String getAnimationSuffix() {
        final CharacterInfo characterInfo = this.getCharacterInfo();
        if (characterInfo == null) {
            CharacterActor.m_logger.error((Object)("Actor sans characterInfo id=" + this.getId() + " gfxId=" + this.getGfxId()));
            return super.getAnimationSuffix();
        }
        final Breed breed = characterInfo.getBreed();
        if (breed != null && (breed == AvatarBreed.ZOBAL || breed.getBreedId() == 2382)) {
            final int attackType = (this.getCurrentAttack() == null) ? Integer.MAX_VALUE : this.getCurrentAttack().getType();
            if (attackType == 1 || attackType == 0) {
                return null;
            }
        }
        return super.getAnimationSuffix();
    }
    
    public AttackType getCurrentAttack() {
        return this.m_currentAttack;
    }
    
    public void setCurrentAttack(final AttackType currentAttack) {
        assert currentAttack != null : "utiliser plutot NoneAttack.getInstance()";
        this.m_currentAttack = currentAttack;
    }
    
    public int applyFightAnimation(final AttackType attack) {
        if (this.m_currentAttack.equals(attack)) {
            WeaponAnimHelper.changeWeapon(this, this.m_currentAttack.getWeaponGfxId(), attack.getWeaponGfxId());
            return 0;
        }
        return WeaponAnimHelper.changeAttack(this, attack);
    }
    
    public void setReadyForFight() {
        if (this.m_characterInfo instanceof PlayerCharacter) {
            this.m_characterInfo.changeToSpellAttackIfNecessary();
        }
        this.m_fightParticleHelper.addCrossSwordParticleSystem((byte)(-1));
    }
    
    public void unSetReadyForFight() {
        if (this.m_characterInfo instanceof PlayerCharacter) {
            WeaponAnimHelper.changeAttack(this, NoneAttack.getInstance());
        }
        this.m_fightParticleHelper.clearCrossSwordParticleSystem();
    }
    
    public int getTextOffset() {
        if (this.m_characterInfo != null) {
            return this.m_characterInfo.getBreed().getHeight();
        }
        return 0;
    }
    
    @Override
    public int getIconId() {
        return 0;
    }
    
    @Override
    public Color getOverHeadborderColor() {
        if (this.m_characterInfo instanceof NonPlayerCharacter && (this.m_characterInfo.isDead() || this.m_characterInfo.isActiveProperty(WorldPropertyType.DEAD))) {
            return Color.PURPLE;
        }
        if (this.m_characterInfo.isActiveProperty(WorldPropertyType.CHALLENGE_NPC)) {
            return Color.RED;
        }
        if (this.m_characterInfo.isActiveProperty(WorldPropertyType.ADMIN_NPC)) {
            return Color.BLUE;
        }
        return Color.WHITE;
    }
    
    public void playDeathParticleSystem() {
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(15000);
        particle.setTarget(this);
        if (this.m_characterInfo != null && this.m_characterInfo.isOnFight()) {
            particle.setFightId(this.m_characterInfo.getCurrentFightId());
        }
        IsoParticleSystemManager.getInstance().addParticleSystem(particle);
    }
    
    @Override
    public void dispose() {
        this.clearAllParticleSystems();
        this.m_hmiHelper.clear(this);
        this.clearMovement();
        UIOverHeadInfosFrame.getInstance().hideOverHead(this);
        this.removeGuildAppearance();
        super.dispose();
    }
    
    public void release() {
        this.dispose();
    }
    
    public void addActiveParticleSystem() {
        this.m_fightParticleHelper.addActiveParticleSystem();
    }
    
    public void addDirectionParticleSystem(final Direction8 direction) {
        this.m_fightParticleHelper.addDirectionParticleSystem(direction);
    }
    
    public void addSelectedParticleSystem() {
        this.m_fightParticleHelper.addSelectedParticleSystem();
    }
    
    public void addExtraTourParticleSystem() {
        this.m_fightParticleHelper.addExtraTourParticleSystem();
    }
    
    public void clearActiveParticleSystem() {
        this.m_fightParticleHelper.clearActiveParticleSystem();
    }
    
    public void clearSelectedParticleSystem() {
        this.m_fightParticleHelper.clearSelectedParticleSystem();
    }
    
    public void clearExtraTourParticleSystem() {
        this.m_fightParticleHelper.clearExtraTourParticleSystem();
    }
    
    public void addPassiveTeamParticleSystem(final byte teamId) {
        this.m_fightParticleHelper.addPassiveTeamParticleSystem(teamId);
    }
    
    public void addActiveTeamParticleSystem(final byte teamId) {
        this.m_fightParticleHelper.addActiveTeamParticleSystem(teamId);
    }
    
    public void applyOnUnapplicationHMIAction(final WakfuRunningEffect effect, final boolean onEffectCell) {
        this.m_hmiHelper.applyOnUnapplicationHMIAction(effect, onEffectCell);
    }
    
    public void applyOnApplicationHMIAction(final WakfuRunningEffect effect, final boolean onEffectCell) {
        this.m_hmiHelper.applyOnApplicationHMIAction(effect, onEffectCell);
    }
    
    public void applyOnExecutionHMIAction(final WakfuRunningEffect effect, final boolean onEffectCell) {
        this.m_hmiHelper.applyOnExecutionHMIAction(effect, onEffectCell);
    }
    
    public void addCrossSwordParticleSystem(final byte elementId) {
        this.m_fightParticleHelper.addCrossSwordParticleSystem(elementId);
    }
    
    public void addTauntParticleSystem() {
        this.m_fightParticleHelper.addTauntParticleSystem();
    }
    
    public void clearCrossSwordParticleSystem() {
        this.m_fightParticleHelper.clearCrossSwordParticleSystem();
    }
    
    public void clearTeamParticleSystem() {
        this.m_fightParticleHelper.clearTeamParticleSystem();
    }
    
    public void clearDirectionParticleSystem() {
        this.m_fightParticleHelper.clearDirectionParticleSystem();
    }
    
    @Override
    public final String getMoveEndAnimationKey() {
        if (this.m_characterInfo instanceof PlayerCharacter) {
            return this.getAnimation() + "-Fin";
        }
        return super.getMoveEndAnimationKey();
    }
    
    @Override
    public MovementSpeed getWalkMovementSpeed() {
        return this.m_characterInfo.getBreed().getWalkTimeBetweenCells();
    }
    
    @Override
    public MovementSpeed getRunMovementSpeed() {
        return this.m_characterInfo.getBreed().getRunTimeBetweenCells();
    }
    
    @Override
    protected void debugTrajectory() {
        if (this.getAvailableDirections() < 8) {
            return;
        }
        final FreeParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(999998);
        particleSystem.setPosition(this.m_worldX, this.m_worldY, this.m_altitude);
        IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
        this.m_debugParticleSystemsIds.add(particleSystem.getId());
    }
    
    @Override
    protected void debugPathFind() {
        if (this.getAvailableDirections() < 8) {
            return;
        }
        try {
            final String CURRENT_PATH_LAYER_NAME = "currentPathLayer";
            final float[] PATH_FIND_CELL_COLOR = { 1.0f, 0.0f, 0.0f, 0.9f };
            HighLightLayer highLightPathLayer = HighLightManager.getInstance().getLayer("currentPathLayer");
            if (highLightPathLayer == null) {
                highLightPathLayer = HighLightManager.getInstance().createLayer("currentPathLayer");
            }
            final String CURRENT_PATH_CELL_LAYER_NAME = "currentPathCellLayer";
            final float[] PATH_FIND_CURRENT_CELL_COLOR = { 0.0f, 0.0f, 1.0f, 0.9f };
            HighLightLayer highLightPathCellLayer = HighLightManager.getInstance().getLayer("currentPathCellLayer");
            if (highLightPathCellLayer == null) {
                highLightPathCellLayer = HighLightManager.getInstance().createLayer("currentPathCellLayer");
            }
            if (this.m_currentPath != null) {
                final DisplayedScreenWorld screenWorld = DisplayedScreenWorld.getInstance();
                HighLightManager.getInstance().clearLayer("currentPathLayer");
                HighLightManager.getInstance().clearLayer("currentPathCellLayer");
                for (int i = 0; i < this.m_currentPath.getPathLength(); ++i) {
                    final int[] currentCell = this.m_currentPath.getPathStep(i);
                    final DisplayedScreenElement element = screenWorld.getElementAtTop(currentCell[0], currentCell[1], currentCell[2], ElementFilter.NOT_EMPTY);
                    if (element != null) {
                        if (i != this.m_currentPathCell) {
                            highLightPathLayer.setColor(PATH_FIND_CELL_COLOR);
                            HighLightManager.getInstance().add(element.getLayerReference(), "currentPathLayer");
                        }
                        else {
                            highLightPathCellLayer.setColor(PATH_FIND_CURRENT_CELL_COLOR);
                            HighLightManager.getInstance().add(element.getLayerReference(), "currentPathCellLayer");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            CharacterActor.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    public void setVisualAnimation(final int visualId, final boolean endAnimation) {
        try {
            final ActionVisual actionVisual = ActionVisualManager.getInstance().get(visualId);
            if (ActionVisualHelper.isAnimationEmpty(actionVisual)) {
                return;
            }
            if (endAnimation) {
                ActionVisualHelper.applyActionVisual(this, actionVisual, true);
            }
            else {
                final ItemEquipment equipmentInventory = this.m_characterInfo.getEquipmentInventory();
                if (equipmentInventory != null) {
                    final Item it = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
                    if (it != null) {
                        this.applyEquipment(it.getReferenceItem(), EquipmentPosition.ACCESSORY.m_id);
                    }
                }
                else {
                    CharacterActor.m_logger.error((Object)("Characterinfo sans equipmentInventory " + this.m_characterInfo.getId()));
                }
                ActionVisualHelper.applyActionVisual(this, actionVisual);
            }
        }
        catch (Exception e) {
            CharacterActor.m_logger.error((Object)("Erreur d'animation li\u00e9e au visuel " + visualId), (Throwable)e);
            this.setAnimation(this.getStaticAnimationKey());
        }
    }
    
    public void applyEquipment(final AbstractReferenceItem item, final short position) {
        this.applyEquipment(item, position, this.m_characterInfo.getSex());
    }
    
    @Override
    public boolean isHighlightable() {
        final CharacterInfo character = this.getCharacterInfo();
        if (character == null) {
            return false;
        }
        if (character.isOnFight()) {
            return super.isHighlightable();
        }
        if (character.isLocalPlayer()) {
            return super.isHighlightable();
        }
        final AbstractMRUAction[] mruActions = character.getMRUActions();
        if (mruActions != null && mruActions.length > 0) {
            for (int i = 0; i < mruActions.length; ++i) {
                final AbstractMRUAction mruAction = mruActions[i];
                if (mruAction != null) {
                    mruAction.initFromSource(character);
                    if (mruAction.isUsable() && mruAction.isRunnable()) {
                        return super.isHighlightable();
                    }
                }
            }
        }
        return false;
    }
    
    public void onCellChanged(final int x, final int y, final int z) {
        this.onCellTransition(new int[] { x, y, z }, new int[] { this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude() });
    }
    
    @Override
    protected void onCellTransition(final int[] nextCell, final int[] previousCell) {
        super.onCellTransition(nextCell, previousCell);
        if (this.m_characterInfo == null || !this.m_characterInfo.isLocalPlayer() || this.m_currentPath == null || this.m_currentPathCell < 0 || this.getMovementStyle() instanceof WalkMovementStyle || this.m_currentPathCell == this.m_currentPath.getPathLength()) {
            return;
        }
        final FreeParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800189);
        particleSystem.setPosition(nextCell[0], nextCell[1], nextCell[2]);
        IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
    }
    
    public HMIHelper getHmiHelper() {
        return this.m_hmiHelper;
    }
    
    public void applyHmiAppearenceModifiers() {
        this.m_hmiHelper.applyAppearenceModifiersOn(this);
    }
    
    public void applyHmiAppearenceColorModifiers() {
        this.m_hmiHelper.applyAppearenceColorModifiersOn(this);
    }
    
    public void reloadHmiActions() {
        this.m_hmiHelper.reload();
    }
    
    @Override
    public boolean isLocalPlayer() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer != null && this.getId() == localPlayer.getId();
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_characterInfo, "visible");
    }
    
    @Override
    protected String getGfxFilePath(final int fileId) {
        try {
            return Actor.getGfxFile(fileId);
        }
        catch (PropertyException e) {
            CharacterActor.m_logger.error((Object)"Exception levee", (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected boolean shouldNotifyMovementStyleAfterClearing() {
        return super.shouldNotifyMovementStyleAfterClearing();
    }
    
    public void removeGuildAppearance() {
        if (this.m_guildBlasonApplicator != null) {
            this.m_guildBlasonApplicator.removeGuildAppearance();
            this.m_guildBlasonApplicator = null;
        }
    }
    
    public void setGuildAppearance(final ClientGuildInformationHandler guildHandler) {
        this.m_guildBlasonApplicator = AnmGuildBlazonApplicator.create(this, guildHandler, "blason", null);
    }
    
    public void doNotShowAgain(final boolean doNotShowAgain) {
        this.m_doNotShowAgain = doNotShowAgain;
    }
    
    @Override
    public PathFindResult getPathResult(final int toX, final int toY, final short toZ, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        if (this.m_characterInfo.getBreed().getBreedId() == 1841) {
            CharacterActor.PATHFINDER_PARAMETERS.m_searchLimit = 600;
        }
        else {
            CharacterActor.PATHFINDER_PARAMETERS.m_searchLimit = 400;
        }
        return super.getPathResult(toX, toY, toZ, stopBeforeEndCell, useDiagonal);
    }
    
    public void setMonsterSkin(final MonsterSkin monsterSkin) {
        this.m_monsterSkin = monsterSkin;
    }
    
    public MonsterSkin getMonsterSkin() {
        return this.m_monsterSkin;
    }
    
    public boolean isHideAllEquipments() {
        return this.m_hideAllEquipments;
    }
    
    public void setHideAllEquipments(final boolean hideAllEquipments) {
        this.m_hideAllEquipments = hideAllEquipments;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterActor.class);
    }
}
