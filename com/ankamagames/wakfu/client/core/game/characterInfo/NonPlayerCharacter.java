package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.dialog.*;
import com.ankamagames.wakfu.common.game.dialog.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.item.rent.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.impl.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.group.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.dungeon.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;

public class NonPlayerCharacter extends CharacterInfo implements CharacteristicUpdateListener, DialogSource, Collectible
{
    private final ArrayList<MRUActions> m_mruActions;
    private static final SortedList<MRUMonsterCollectAction> MRU_COLLECT_HELPER;
    public static final boolean ENABLE_CORPSES = true;
    private short m_level;
    private final NPCSpellInventoryManager m_spellInventoryManager;
    private final float m_movementObstruction = 0.0f;
    private final NonPlayerCharacterSerializer m_serializer;
    private long m_groupId;
    private MonsterBehaviourAction m_currentBehaviourAction;
    private final TIntHashSet m_unavailableSkillActions;
    private short m_overrideAggroRadius;
    private short m_overrideSightRadius;
    private boolean m_pooled;
    private MonsterSpecialGfx m_specificGfx;
    private long m_companionControllerId;
    private long m_companionId;
    private FightEventListener m_fightEventListener;
    
    protected NonPlayerCharacter() {
        super();
        this.m_mruActions = new ArrayList<MRUActions>(Arrays.asList(MRUActions.CHARACTER_CAST_FIGHT_ACTION, MRUActions.CHARACTER_CAST_TRAINING_FIGHT_MODEL_ACTION, MRUActions.CHARACTER_CAST_TRAINING_WITHOUT_REPORT_FIGHT_MODEL_ACTION, MRUActions.CHARACTER_CAST_PROTECTOR_ASSAULT_FIGHT_MODEL_ACTION, MRUActions.CHARACTER_JOIN_FIGHT_ACTION, MRUActions.RESURRECT_PLAYER_ACTION, MRUActions.MANAGE_PROTECTOR_ACTION, MRUActions.VOTE_ACTION, MRUActions.PREVIOUS_VOTE_LIST_ACTION, MRUActions.ATTEND_FIGHT));
        this.m_spellInventoryManager = new NPCSpellInventoryManager();
        this.m_serializer = new NonPlayerCharacterSerializer();
        this.m_unavailableSkillActions = new TIntHashSet();
        this.m_overrideAggroRadius = -1;
        this.m_overrideSightRadius = -1;
        this.m_specificGfx = null;
        this.m_isControlledByAI = this.returnDefaultAIControl();
        this.m_type = 1;
        this.initializeSerializer();
        this.m_groupId = 0L;
        this.m_breed = MonsterBreed.NONE;
    }
    
    public static NonPlayerCharacter createNpc() {
        return new NonPlayerCharacter();
    }
    
    @Override
    public boolean returnDefaultAIControl() {
        return true;
    }
    
    @Override
    public void initialiseCharacteristicsToBaseValue() {
        if (this.m_breed instanceof MonsterBreed) {
            final MonsterBreed monsterBreed = (MonsterBreed)this.m_breed;
            this.m_characteristics = new LazyFighterCharacteristicManager(monsterBreed.getCharacteristicManager(), this.m_level);
            this.m_characteristicViewProvider = new CharacteristicViewProvider(this);
        }
    }
    
    @Override
    public void onGobgobChangedLevel() {
        super.onGobgobChangedLevel();
        this.addFightCharacteristicsListeners();
        final FighterCharacteristicType[] characs = FighterCharacteristicType.values();
        for (int i = 0; i < characs.length; ++i) {
            final FighterCharacteristicType charac = characs[i];
            this.onCharacteristicUpdated(this.getCharacteristic((CharacteristicType)charac));
        }
        this.m_spellInventoryManager.initialize(((MonsterBreed)this.m_breed).getSpells(), (short)(this.m_level / 2));
        WakfuGameEntity.getInstance().getLocalPlayer().updateShortcutBars();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "level", "levelValue", "nameAndLevel");
    }
    
    @Override
    public long getDialogSourceId() {
        return this.getId();
    }
    
    @Override
    public AbstractDialogSourceType getDialogSourceType() {
        return DialogSourceType.NPC;
    }
    
    @Override
    public int getKamasCount() {
        return 0;
    }
    
    @Override
    public int substractKamas(final int kamas) {
        return 0;
    }
    
    @Override
    public int addKamas(final int kamas) {
        return 0;
    }
    
    @Override
    public AbstractBreedManager getBreedManager() {
        return MonsterBreedManager.getInstance();
    }
    
    @Override
    public ItemEquipment getEquipmentInventory() {
        final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(this.m_companionId);
        if (companion == null) {
            return null;
        }
        return companion.getItemEquipment();
    }
    
    @Nullable
    @Override
    public TByteIntHashMap getEquipmentAppearance() {
        final TByteIntHashMap forcedEquipment = super.getEquipmentAppearance();
        if (forcedEquipment != null) {
            return forcedEquipment;
        }
        if (this.m_breed != null) {
            return ((MonsterBreed)this.m_breed).getEquipmentAppearance();
        }
        return null;
    }
    
    @Nullable
    @Override
    public Protector getProtector() {
        final ArrayList<Protector> protectors = new ArrayList<Protector>();
        ProtectorManager.INSTANCE.findProtectors(new ProtectorSearchCriterion<Protector>() {
            @Override
            public boolean match(final Protector protector) {
                return protector.getNpcId() == NonPlayerCharacter.this.getId();
            }
        }, protectors);
        return protectors.isEmpty() ? null : protectors.get(0);
    }
    
    @Override
    public int getGfxId() {
        if (this.m_breed == null || !(this.m_breed instanceof MonsterBreed)) {
            return 0;
        }
        final int forcedGfx = super.getGfxId();
        if (forcedGfx != 0) {
            return forcedGfx;
        }
        return ((MonsterBreed)this.m_breed).getGfx();
    }
    
    @Override
    public SpellLevel getSpellLevelById(final long uid) {
        return this.m_spellInventoryManager.getSpellLevelById(uid);
    }
    
    @Override
    public Iterable<SpellLevel> getSpellLevels() {
        return this.m_spellInventoryManager.getSpellLevels();
    }
    
    @Override
    protected String getSmileyGfxFileName() {
        return "Smiley_" + this.getBreedId();
    }
    
    @Override
    protected String getGfxPathKey() {
        return "npcGfxPath";
    }
    
    @Override
    public void initialize() {
        if (this.m_breed == null) {
            NonPlayerCharacter.m_logger.error((Object)"On essaie d'initialiser un monster qui ne possede pas de BreedInfo");
            return;
        }
        final MonsterBreed monsterBreed = (MonsterBreed)this.m_breed;
        final CharacterActor actor = this.getActor();
        actor.setAvailableDirections((byte)4);
        actor.setJumpAnimAvailable(false);
        if (this.m_name == null || this.m_name.isEmpty()) {
            this.setName(monsterBreed.getName());
        }
        this.m_spellInventoryManager.initialize(monsterBreed.getSpells(), this.m_level);
        this.initialiseCharacteristicsToBaseValue();
        this.reloadWorldProperties(monsterBreed);
        this.reloadNaturalStates(monsterBreed);
    }
    
    @Override
    protected void onSummonInitialized(final CharacterInfo summoner) {
        if (this.isActiveProperty(FightPropertyType.SUMMONER_VISUAL_COPY) && summoner != null) {
            AppearanceCopy.copySummonerAppearance(this, summoner);
        }
    }
    
    private void reloadWorldProperties(final MonsterBreed monsterBreed) {
        if (monsterBreed.getBaseWorldProperties() != null) {
            for (final int propertyId : monsterBreed.getBaseWorldProperties()) {
                final WorldPropertyType prop = WorldPropertyType.getPropertyFromId(propertyId);
                if (prop != null) {
                    this.removeProperty(prop);
                    this.addProperty(prop);
                }
                else {
                    NonPlayerCharacter.m_logger.error((Object)("id d'une propri\u00e9t\u00e9 de base (World) incorrect :" + propertyId));
                }
            }
        }
    }
    
    private void reloadNaturalStates(final MonsterBreed monsterBreed) {
        this.m_stateAffinitiesView.reset();
        final short[] states = monsterBreed.getNaturalStates();
        if (states != null && states.length > 1) {
            for (short i = 0; i < states.length; i += 3) {
                final short stateId = states[i];
                final TimedRunningEffectManager rem = this.getRunningEffectManager();
                if (rem != null) {
                    rem.removeStatesFromId(stateId);
                }
                short statelevel = states[i + 1];
                if (statelevel == -1) {
                    statelevel = this.getLevel();
                }
                RunningEffect.resetLimitedApplyCount();
                final ApplyState applyState = ApplyState.checkout(this.m_ownContext, this, stateId, statelevel, states[i + 2] == 0);
                applyState.forceDontTriggerAnything();
                applyState.setCaster(this);
                applyState.bypassResistancesCheck();
                applyState.execute(null, false);
            }
        }
    }
    
    @Override
    protected void initializeSerializer() {
        super.initializeSerializer();
        new NonPlayerCharacterPartAppearance(this.m_serializer.getAppearancePart());
        new NonPlayerCharacterPartCharacteristics(this.m_serializer.getPublicCharacteristicsPart());
        new NonPlayerCharacterPartCharacteristics(this.m_serializer.getPrivateCharacteristicsPart());
        new NonPlayerCharacterPartCurrentMovementPath(this.m_serializer.getCurrentMovementPathPart());
        new NonPlayerCharacterPartGroup(this.m_serializer.getGroupPart());
        new NonPlayerCharacterPartUserTemplate(this.m_serializer.getTemplatePart());
        new NonPlayerCharacterPartCollect(this.m_serializer.getCollectPart());
        new NonPlayerCharacterPartCompanionInfo(this.m_serializer.getCompanionControllerIdPart());
    }
    
    @Override
    public boolean isNotEcosystemNpc() {
        return super.isNotEcosystemNpc() || this.m_companionId > 0L;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    @Override
    public void onGoesOutOfPlay() {
        super.onGoesOutOfPlay();
        if (this.isNotEcosystemNpc()) {
            this.onNPCDeath();
        }
    }
    
    @Override
    public void onJoinFight(final BasicFight basicFight) {
        super.onJoinFight(basicFight);
        if (this.m_currentBehaviourAction != null) {
            this.m_currentBehaviourAction.forceActionEnd();
            this.m_currentBehaviourAction = null;
        }
        if (this.getBreed() != null) {
            final MonsterBreed breed = (MonsterBreed)this.m_breed;
            this.reloadNaturalStates(breed);
            this.reloadFightProperties(breed);
        }
    }
    
    @Override
    public final void reloadPassiveSpells() {
        this.getRunningEffectManager().removeLinkedToContainerType(25);
        if (this.isOnFight() && this.getCurrentFight() != null && !this.getCurrentFight().getModel().isWithPassiveSpells()) {
            return;
        }
        final Iterable<SpellLevel> spellLevels = this.getSpellLevels();
        for (final AbstractSpellLevel spellLevel : spellLevels) {
            final AbstractSpell spell = spellLevel.getSpell();
            if (spellLevel.getLevel() <= 0 && !spell.isPassiveEnabledFromStart()) {
                continue;
            }
            if (!spell.isPassive()) {
                continue;
            }
            final WakfuEffectContainer effectContainer = spellLevel;
            for (final WakfuEffect e : effectContainer) {
                e.execute(spellLevel, this, this.getAppropriateContext(), RunningEffectConstants.getInstance(), this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this, null, false);
            }
        }
    }
    
    @Override
    public void onLeaveFight() {
        if (this.isSummonedFromSymbiot()) {
            final BasicCharacterInfo summoner = this.getOriginalController();
            if (summoner.getBreed() == AvatarBreed.OSAMODAS) {
                this.putBackInSymbiot(summoner);
            }
        }
        if (this.getType() == 5 && (this.getCurrentFight() == null || this.getCurrentFight().getModel().decreaseRent())) {
            RentUpdater.updateType(1, this);
        }
        super.onLeaveFight();
        if (this.getBreed() != null && !this.isDead()) {
            this.reloadNaturalStates(this.getBreed());
        }
        if (!this.isOutOfPlay()) {
            this.getCharacteristics().makeDefault();
        }
        if (this.isFighterLocalToCurrentFight()) {
            final long id = this.getId();
            this.getCurrentFight().addFightEndListener(new FightEndListener() {
                @Override
                public void onFightEnd(final Fight fight) {
                    if (NonPlayerCharacter.this.getId() == id) {
                        CharacterInfoManager.getInstance().removeCharacter(NonPlayerCharacter.this);
                    }
                }
            });
        }
        else {
            this.resetFighterAfterFight();
            final Fight fight = this.getCurrentFight();
            if (this.m_isDead) {
                if (!fight.hasWon(this.getId())) {
                    this.onNPCDeath();
                }
                else {
                    this.m_isDead = false;
                    this.resetFighterAfterFight();
                }
            }
        }
    }
    
    public void onNPCDeath() {
        if (this.isOnFight()) {
            this.getController().onControlledNPCDeath();
        }
        final CharacterActor actor = this.getActor();
        if (this.isNotEcosystemNpc() || this.isActiveProperty(WorldPropertyType.SEDUCE) || !this.getBreed().hasDeadEvolution()) {
            actor.setVisible(false);
            actor.doNotShowAgain(true);
            NPCGroupInformationManager.getInstance().onNonPlayerCharacterRemoved(this, true);
        }
        else {
            MonsterTimedPropertyManager.getInstance().removeAllStates(this.m_id);
            MonsterTimedPropertyManager.getInstance().addPropertyAndUpdate(this, WorldPropertyType.DEAD);
            NPCGroupInformationManager.getInstance().onNonPlayerCharacterRemoved(this, true);
            actor.setAnimation("AnimStatiqueCadavre");
            actor.setVisible(true);
            actor.setDeltaZ(LayerOrder.DEAD_NPC.getDeltaZ());
        }
    }
    
    @Override
    public void release() {
        this.onCheckIn();
    }
    
    @Override
    protected Breed defaultBreed() {
        return MonsterBreed.NONE;
    }
    
    @Override
    protected byte defaultCharacterType() {
        return 1;
    }
    
    @Override
    public void setName(String name) {
        if ((name == null || name.length() == 0) && this.m_breed instanceof MonsterBreed) {
            name = ((MonsterBreed)this.m_breed).getName();
        }
        super.setName(name);
    }
    
    @NotNull
    @Override
    public SkillCharacteristics getSkillCharacteristics() {
        return EmptySkillCharacteristics.INSTANCE;
    }
    
    public short getTypeId() {
        if (this.m_breed == null) {
            return 0;
        }
        return this.m_breed.getBreedId();
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public float getWakfuGaugeValue() {
        return 0.0f;
    }
    
    @Override
    public CharacterSerializer getSerializer() {
        return this.m_serializer;
    }
    
    @Override
    public NPCSpellInventoryManager getSpellInventoryManager() {
        return this.m_spellInventoryManager;
    }
    
    @Override
    public void setLevel(final short level) {
        this.m_level = level;
    }
    
    public long getGroupId() {
        return this.m_groupId;
    }
    
    public void setGroupId(final long groupId) {
        this.m_groupId = groupId;
    }
    
    public MonsterBehaviourAction getCurrentBehaviourAction() {
        return this.m_currentBehaviourAction;
    }
    
    public void setCurrentBehaviourAction(final MonsterBehaviourAction currentBehaviourAction) {
        this.m_currentBehaviourAction = currentBehaviourAction;
    }
    
    @Override
    public void setDead(final boolean dead) {
        super.setDead(dead);
    }
    
    @Override
    protected CitizenComportment createCitizenComportment() {
        return new NPCNotCitizenComportement(this);
    }
    
    public boolean isCollectAvailable(final int actionId) {
        return !this.m_unavailableSkillActions.contains(actionId);
    }
    
    public void setCollectAvailable(final int actionId, final boolean available) {
        if (available) {
            this.m_unavailableSkillActions.remove(actionId);
        }
        else {
            this.m_unavailableSkillActions.add(actionId);
        }
    }
    
    @Override
    public int getFamilyId() {
        return this.m_breed.getFamilyId();
    }
    
    public long getCompanionControllerId() {
        return this.m_companionControllerId;
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    public void setCompanionId(final long companionId) {
        this.m_companionId = companionId;
    }
    
    @Override
    public Iterable<? extends AbstractSpellLevel> getPermanentSpellInventory() {
        if (this.m_companionId > 0L) {
            return this.m_spellInventoryManager.getSpellLevels();
        }
        return null;
    }
    
    public void evolve(final short evolveBreedId, final short evolvedLevel) {
        final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId(evolveBreedId);
        this.setBreed(breed);
        this.setLevel(evolvedLevel);
        final CharacterActor newActor = new CharacterActor(this);
        this.setActor(newActor);
        final PropertyManager<WorldPropertyType> worldPropertyManager = this.getWorldProperties();
        if (worldPropertyManager != null) {
            worldPropertyManager.reset();
        }
        this.initialize();
        newActor.forceReloadAnimation();
        final NPCGroupInformation infoGroup = NPCGroupInformationManager.getInstance().getGroupInformation(this.getGroupId());
        if (infoGroup != null) {
            infoGroup.updateNpc(this);
        }
    }
    
    @Override
    public byte getActorTypeId() {
        return 1;
    }
    
    public short getAggroRadius() {
        if (this.m_overrideAggroRadius > 0) {
            return this.m_overrideAggroRadius;
        }
        if (this.m_breed instanceof MonsterBreed) {
            return (short)Math.min(((MonsterBreed)this.m_breed).getAggroRadius(), 6);
        }
        return 0;
    }
    
    public short getSightRadius() {
        if (this.m_overrideSightRadius > 0) {
            return this.m_overrideSightRadius;
        }
        if (this.m_breed instanceof MonsterBreed) {
            return (short)Math.min(((MonsterBreed)this.m_breed).getSightRadius(), 6);
        }
        return 0;
    }
    
    public boolean isAgressive() {
        return this.getSightRadius() > 0 && this.getAggroRadius() > 0;
    }
    
    @Override
    public boolean isDead() {
        return super.isDead() || this.isActiveProperty(WorldPropertyType.DEAD);
    }
    
    public void addControlledFighter(final CharacterInfo controlled) {
    }
    
    @Override
    public MonsterBreed getBreed() {
        if (!(this.m_breed instanceof MonsterBreed)) {
            NonPlayerCharacter.m_logger.error((Object)("Un NPC n'a pas de breed de type MonsterBreed " + this.m_breed + ", mob ID = " + this.m_id));
        }
        return (MonsterBreed)super.getBreed();
    }
    
    @Override
    protected void applyMisc() {
        super.applyMisc();
        MonsterSpecialGfxApplyer.applyDefaultAnims(this.getBreedSpecialGfx(), this);
        MonsterSpecialGfxApplyer.applyDefaultAnims(this.getSpecificSpecialGfx(), this);
    }
    
    @Override
    protected void updateColors() {
        super.updateColors();
        MonsterSpecialGfxApplyer.applyColors(this.getBreedSpecialGfx(), this);
        MonsterSpecialGfxApplyer.applyColors(this.getSpecificSpecialGfx(), this);
    }
    
    @Override
    protected void applyCustoms() {
        super.applyCustoms();
        MonsterSpecialGfxApplyer.applyCustoms(this.getBreedSpecialGfx(), this);
        MonsterSpecialGfxApplyer.applyCustoms(this.getSpecificSpecialGfx(), this);
    }
    
    public MonsterSpecialGfx getBreedSpecialGfx() {
        final MonsterBreed breed = this.getBreed();
        if (breed == null) {
            return null;
        }
        return breed.getSpecialGfx();
    }
    
    private MonsterSpecialGfx getSpecificSpecialGfx() {
        return this.m_specificGfx;
    }
    
    public void setSpecificSpecialGfx(final MonsterSpecialGfx specificGfx) {
        this.m_specificGfx = specificGfx;
    }
    
    @Override
    public void addFightCharacteristicsListeners() {
        ((LazyFighterCharacteristicManager)this.getCharacteristics()).addListener(this);
        TimeManager.INSTANCE.addListener(this.getRunningEffectFieldProvider());
    }
    
    @Override
    public void removeFightCharacteristicsListeners() {
        ((LazyFighterCharacteristicManager)this.getCharacteristics()).removeListener(this);
        TimeManager.INSTANCE.removeListener(this.getRunningEffectFieldProvider());
    }
    
    @Override
    public void addProperty(final PropertyType property) {
        super.addProperty(property);
        if (property == WorldPropertyType.HOODED_MONSTER) {
            this.m_fightEventListener = new HoodedMonsterFightEventListener(this);
        }
        if (this.m_fightEventListener == null) {
            return;
        }
        this.m_fightEventListener.addProperty(property);
    }
    
    @Override
    public void removeProperty(final PropertyType property) {
        super.removeProperty(property);
        if (this.m_fightEventListener == null) {
            return;
        }
        this.m_fightEventListener.removeProperty(property);
    }
    
    @Override
    public void onSpecialFighterEvent(final SpecialEvent fightEventType) {
        super.onSpecialFighterEvent(fightEventType);
        if (this.m_fightEventListener == null) {
            return;
        }
        this.m_fightEventListener.onSpecialFighterEvent(fightEventType);
    }
    
    @Override
    public void onControllerEvent(final int eventId, final Object param) {
        super.onControllerEvent(eventId, param);
        if (this.m_fightEventListener == null) {
            return;
        }
        this.m_fightEventListener.onControllerEvent(eventId, param);
    }
    
    public void setFightEventListener(final FightEventListener fightEventListener) {
        this.m_fightEventListener = fightEventListener;
    }
    
    @Override
    public String getName() {
        if (HoodedMonsterFightEventListener.isVisuallyHooded(this)) {
            return WakfuTranslator.getInstance().getString("hooded.monster");
        }
        return super.getName();
    }
    
    @Override
    public boolean is(final CriterionUserType type) {
        if (this.m_type == 5) {
            return CriterionUserType.COMPANION.is(type);
        }
        return CriterionUserType.NON_PLAYER_CHARACTER.is(type);
    }
    
    public int getArcadeScore() {
        return MonsterScoreCalculator.getScore(this.getLevel(), this.getBreed().getArcadePointMultiplicator());
    }
    
    @Override
    public AbstractMRUAction[] getMRUActions() {
        final ArrayList<AbstractMRUAction> mru = new ArrayList<AbstractMRUAction>(Arrays.asList(super.getMRUActions()));
        for (int i = 0; i < this.m_mruActions.size(); ++i) {
            mru.add(this.m_mruActions.get(i).getMRUAction());
        }
        NonPlayerCharacter.MRU_COLLECT_HELPER.clear();
        final TIntObjectIterator<CollectAction> it = ((MonsterBreed)this.m_breed).collectActionIterator();
        while (it.hasNext()) {
            it.advance();
            NonPlayerCharacter.MRU_COLLECT_HELPER.add(new MRUMonsterCollectAction(it.value()));
        }
        mru.addAll(NonPlayerCharacter.MRU_COLLECT_HELPER);
        NonPlayerCharacter.MRU_COLLECT_HELPER.clear();
        final ArrayList<AbstractClientMonsterAction> actions = this.getBreed().getActions();
        for (int j = 0; j < actions.size(); ++j) {
            mru.add(new MRUMonsterAction(actions.get(j)));
        }
        return mru.toArray(new AbstractMRUAction[mru.size()]);
    }
    
    static {
        MRU_COLLECT_HELPER = new SortedList<MRUMonsterCollectAction>(new Comparator<MRUMonsterCollectAction>() {
            @Override
            public int compare(final MRUMonsterCollectAction o1, final MRUMonsterCollectAction o2) {
                final String name1 = WakfuTranslator.getInstance().getString("desc.mru." + o1.getTranslatorKey());
                final String name2 = WakfuTranslator.getInstance().getString("desc.mru." + o2.getTranslatorKey());
                return name1.compareTo(name2);
            }
        });
    }
    
    private final class NonPlayerCharacterPartAppearance extends CharacterInfoPart
    {
        private final NPCSerializedAppearance m_part;
        
        private NonPlayerCharacterPartAppearance(final NPCSerializedAppearance part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            NonPlayerCharacter.m_logger.error((Object)"L'apparence du NPC ne devrait pas \u00eatre s\u00e9rialis\u00e9e par le client");
        }
        
        @Override
        public void onDataChanged() {
            NonPlayerCharacter.this.getActor().setVisible(this.m_part.show);
            NonPlayerCharacter.this.refreshDisplayEquipment();
        }
    }
    
    private final class NonPlayerCharacterPartCompanionInfo extends CharacterInfoPart
    {
        private final NPCCompanionInfo m_part;
        
        private NonPlayerCharacterPartCompanionInfo(final NPCCompanionInfo part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            NonPlayerCharacter.m_logger.error((Object)"L'apparence du NPC ne devrait pas \u00eatre s\u00e9rialis\u00e9e par le client");
        }
        
        @Override
        public void onDataChanged() {
            NonPlayerCharacter.this.m_companionControllerId = this.m_part.controllerId;
            NonPlayerCharacter.this.m_companionId = this.m_part.companionId;
        }
    }
    
    private final class NonPlayerCharacterPartCharacteristics extends CharacterInfoPart
    {
        private final NPCSerializedCharacteristics m_part;
        
        private NonPlayerCharacterPartCharacteristics(final NPCSerializedCharacteristics part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            NonPlayerCharacter.m_logger.error((Object)"Le client ne devrait pas s\u00e9rialiser les caract\u00e9ristiques");
        }
        
        @Override
        public void onDataChanged() {
            NonPlayerCharacter.this.m_level = this.m_part.level;
        }
    }
    
    private final class NonPlayerCharacterPartCurrentMovementPath extends CharacterInfoPart
    {
        private final CharacterSerializedCurrentMovementPath m_part;
        
        private NonPlayerCharacterPartCurrentMovementPath(final CharacterSerializedCurrentMovementPath part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            NonPlayerCharacter.m_logger.error((Object)"Le client ne devrait pas s\u00e9rialiser le chemin courant du NPC");
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.currentPath != null) {
                final ByteBuffer buffer = ByteBuffer.wrap(this.m_part.currentPath.encodedPath);
                final Direction8Path path = Direction8Path.decodeFromBuffer(buffer);
                if (path != null) {
                    NonPlayerCharacter.this.getActor().updateActorPath(path);
                }
            }
        }
    }
    
    private final class NonPlayerCharacterPartGroup extends CharacterInfoPart
    {
        private final NPCSerializedGroup m_part;
        
        private NonPlayerCharacterPartGroup(final NPCSerializedGroup part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            NonPlayerCharacter.m_logger.error((Object)"Le client ne devrait pas s\u00e9rialiser le groupe du NPC");
        }
        
        @Override
        public void onDataChanged() {
            NonPlayerCharacter.this.m_groupId = this.m_part.groupId;
            if (NonPlayerCharacter.this.m_groupId != 0L) {
                NPCGroupInformationManager.getInstance().updateGroupInformationFromNPC(NonPlayerCharacter.this, this.m_part.members);
            }
            else if (!NonPlayerCharacter.this.m_isDead && !NonPlayerCharacter.this.isNotEcosystemNpc()) {
                NonPlayerCharacter.m_logger.error((Object)("D\u00e9serialisation d'un NPC id=" + NonPlayerCharacter.this.m_id + " avec un groupId=0 : anormal"));
            }
        }
    }
    
    private final class NonPlayerCharacterPartUserTemplate extends CharacterInfoPart
    {
        private final NPCSerializedUserTemplate m_part;
        
        private NonPlayerCharacterPartUserTemplate(final NPCSerializedUserTemplate part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            NonPlayerCharacter.m_logger.error((Object)"Le client ne devrait pas s\u00e9rialiser les donn\u00e9es sp\u00e9cifiques templates NPC");
        }
        
        @Override
        public void onDataChanged() {
            NonPlayerCharacter.this.m_overrideSightRadius = this.m_part.sightRadius;
            NonPlayerCharacter.this.m_overrideAggroRadius = this.m_part.aggroRadius;
        }
    }
    
    private final class NonPlayerCharacterPartCollect extends CharacterInfoPart
    {
        private final NPCSerializedCollect m_part;
        
        private NonPlayerCharacterPartCollect(final NPCSerializedCollect part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Le client ne devrait pas s\u00e9rialiser les donn\u00e9es sp\u00e9cifiques de collect du NPC");
        }
        
        @Override
        public void onDataChanged() {
            for (int i = 0, size = this.m_part.unavailableActions.size(); i < size; ++i) {
                final NPCSerializedCollect.CollectAction action = this.m_part.unavailableActions.get(i);
                NonPlayerCharacter.this.m_unavailableSkillActions.add(action.collectId);
            }
        }
    }
}
