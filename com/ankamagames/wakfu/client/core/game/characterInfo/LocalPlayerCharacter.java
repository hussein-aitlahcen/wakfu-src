package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.common.game.challenge.*;
import com.ankamagames.baseImpl.graphics.core.partitions.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.common.game.respawn.*;
import com.ankamagames.wakfu.client.core.game.item.cosmetic.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.client.core.game.title.*;
import com.ankamagames.wakfu.common.game.xp.character.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import com.ankamagames.wakfu.client.core.game.pet.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.zones.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.listener.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.fightChallenge.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.framework.script.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.mount.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.game.inventory.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.client.core.game.aptitude.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.aptitudenew.*;
import com.ankamagames.wakfu.common.game.zone.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific.*;
import com.ankamagames.wakfu.client.core.game.fight.spectator.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.common.game.item.rent.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.alea.graphics.tacticalView.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.emote.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.BreedSpecific.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.events.listeners.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.lock.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.antiAddiction.*;
import com.ankamagames.wakfu.common.game.antiAddiction.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class LocalPlayerCharacter extends PlayerCharacter implements SymbioticCharacter<Symbiot>, CharacteristicUpdateListener, InventoryObserver, PlayerCharacterLevelable, WalletEventListener, ChallengeUser, PartitionChangedListener<PathMobile, LocalPartition>, CraftUser, EmoteUser, MerchantClient, CharacterImageGenerator.Listener, GroupUser, InventoryUser, SpellXpLocker, LockContextProvider
{
    protected static final FightLogger m_fightLogger;
    public static final String SHORTCUT_BAR_MANAGER_FIELD = "shortcutBarManager";
    public static final String BAGS_FIELD = "bags";
    public static final String EQUIPMENT_FIELD = "equipment";
    public static final String EQUIPMENT_BONUS_FIELD = "equipmentBonus";
    public static final String SELECTED_ITEM_INVENTORY_DESCRIPTION = "selectedItemInventoryDescription";
    public static final String HEAD_EQUIPMENT_FIELD = "headEquipment";
    public static final String HAIR_EQUIPMENT_FIELD = "hairEquipment";
    public static final String FACE_EQUIPMENT_FIELD = "faceEquipment";
    public static final String SHOULDERS_EQUIPMENT_FIELD = "shoulderEquipment";
    public static final String NECK_EQUIPMENT_FIELD = "neckEquipment";
    public static final String CHEST_EQUIPMENT_FIELD = "chestEquipment";
    public static final String ARMS_EQUIPMENT_FIELD = "armsEquipment";
    public static final String LEFT_HAND_EQUIPMENT_FIELD = "leftHandEquipment";
    public static final String RIGHT_HAND_EQUIPMENT_FIELD = "rightHandEquipment";
    public static final String BELT_EQUIPMENT_FIELD = "beltEquipment";
    public static final String SKIRT_EQUIPMENT_FIELD = "skirtEquipment";
    public static final String TROUSERS_EQUIPMENT_FIELD = "trousersEquipment";
    public static final String LEGS_EQUIPMENT_FIELD = "legsEquipment";
    public static final String PET_EQUIPMENT_FIELD = "petEquipment";
    public static final String MOUNT_EQUIPMENT_FIELD = "mountEquipment";
    public static final String COSTUME_EQUIPMENT_FIELD = "costumeEquipment";
    public static final String BACK_EQUIPMENT_FIELD = "backEquipment";
    public static final String WING_EQUIPMENT_FIELD = "wingEquipment";
    public static final String FIRST_WEAPON_EQUIPMENT_FIELD = "firstWeaponEquipment";
    public static final String SECOND_WEAPON_EQUIPMENT_FIELD = "secondWeaponEquipment";
    public static final String ACCESSORY_EQUIPMENT_FIELD = "accessoryEquipment";
    public static final String WEAPON_SKILLS_FIELD = "weaponSkills";
    public static final String CRAFT_FIELD = "craft";
    public static final String WALLET_KAMAS = "kamas";
    public static final String WALLET_FORMATED_KAMAS = "formatedKamas";
    public static final String WALLET_TOKENS = "tokens";
    public static final String TEMPORARY_TRANSFER_INVENTORY = "temporaryTransferInventory";
    public static final String AVAILABLE_TITLES_FIELD = "availableTitles";
    public static final String CURRENT_LEVEL_PERCENTAGE_FIELD = "currentLevelPercentage";
    public static final String XP_RATIO_FIELD = "xpRatio";
    public static final String APTITUDES_FIELD = "aptitudes";
    public static final String COMMON_APTITUDES_FIELD = "commonAptitudes";
    public static final String BREED_APTITUDES_FIELD = "breedAptitudes";
    public static final String AVAILABLE_COMMON_POINTS = "availableCommonPoints";
    public static final String AVAILABLE_APTITUDES_POINTS = "availableAptitudePoints";
    public static final String HAS_APTITUDE_POINTS = "hasAptitudePoints";
    public static final String WAKFU_GAUGE_FIELD = "wakfuGauge";
    public static final String WAKFU_GAUGE_POPUP_TEXT_FIELD = "wakfuGaugePopupText";
    public static final String WAKFU_GAUGE_ICON_STYLE_FIELD = "wakfuGaugeIconStyle";
    public static final String WAKFU_GAUGE_COLOR_FIELD = "wakfuGaugeColor";
    public static final String NATION_FIELD = "nation";
    public static final String CHARACTER_ICON_URL = "characterIconUrl";
    public static final String IS_IN_SUBSCRIBER_ZONE = "isInSubscriberZone";
    public static final String CAN_LOCK_SPELL = "canLockSpell";
    public static final String HAS_MISSING_SPELL_XP = "hasMissingSpellXp";
    public static final String LOCKED_SPELL = "lockedSpell";
    public static final String COMPANION_LIST = "companionList";
    public static final String ACTOR_ANIMATION_NAME_FIELD = "actorAnimationName";
    public static final String ACTOR_STANDARD_SCALE = "actorStandardScale";
    public static final String COSMETICS_INVENTORY = "cosmeticsInventory";
    public static final String PET_COSMETICS_INVENTORY = "petCosmeticsInventory";
    static final String[] LOCAL_FIELDS;
    public static final String[] LOCAL_ALL_FIELDS;
    public static final String[] UPDATE_EQUIPMENT_PROPERTIES;
    protected final ItemEquipment m_equipmentInventory;
    private int m_lockedSpellId;
    private final HashMap<SpellLevel, Short> m_lastFightSpellUsage;
    private final HashMap<Item, Short> m_lastFightItemUsage;
    private SymbioticCharacter<Symbiot> m_symbioticCharacter;
    protected final SkillInventory<Skill> m_skillInventory;
    private final ShortcutBarManager m_shortcutBarManager;
    private final CraftHandler m_craftHandler;
    private final ClientEmoteHandler m_emoteHandler;
    private final PersonalSpaceHandler m_personalSpaceHandler;
    private final ClientMapHandler m_mapHandler;
    private final TravelHandler m_travelHandler;
    private final RespawnPointHandler m_respawnPointHandler;
    private final InventoryHandler m_inventoryHandler;
    private final ClientBagContainer m_bags;
    private boolean m_enableItemTransfer;
    protected boolean m_replaceFlag;
    private ClientTemporaryInventoryController m_temporaryInventoryControler;
    private CosmeticsInventoryView m_cosmeticsInventoryView;
    private PetCosmeticsInventoryView m_petCosmeticsInventoryView;
    private DimensionalBagView m_ownedDimensionalBag;
    private DimensionalBagView m_visitingDimentionalBag;
    private boolean m_waitingForResult;
    private String m_cachedEquipmentBonus;
    private final ActionInProgress m_actionInProgress;
    @NotNull
    private final PartyComportment m_partyComportment;
    private boolean m_canMoveAndInteract;
    private final LocalTimedRunningEffectManager m_runningEffectManager;
    private ArrayList<PlayerTitle> m_availableTitles;
    PlayerCharacterLevelable m_xp;
    private final AptitudeInventory m_aptitudeInventory;
    private final AptitudeBonusInventory m_aptitudeBonusInventory;
    private final LocalPlayerZoneBuffs m_zoneBuffs;
    private ClientFightChallengeContext m_fightChallenges;
    private LockContext m_locks;
    private Territory m_currentTerritory;
    private Fight m_observedFight;
    private SpectatorModeMovementRequestHandler m_spectatorModeMovementRequestHandler;
    private boolean m_activateUIArcadeDungeonFrame;
    private TIntHashSet m_guildEffects;
    private TIntHashSet m_havenWorldEffects;
    private TIntHashSet m_antiAddictionEffects;
    private final AntiAddictionDataHandler m_antiAddictionDataHandler;
    private short m_previousWorldId;
    private boolean m_characterChoiceState;
    
    @Override
    public LocalTimedRunningEffectManager getRunningEffectManager() {
        return this.m_runningEffectManager;
    }
    
    public LocalPlayerCharacter() {
        super();
        this.m_equipmentInventory = new ItemEquipment();
        this.m_lockedSpellId = 0;
        this.m_lastFightSpellUsage = new HashMap<SpellLevel, Short>();
        this.m_lastFightItemUsage = new HashMap<Item, Short>();
        this.m_skillInventory = new SkillInventory<Skill>(SkillProvider.getInstance());
        this.m_shortcutBarManager = new ShortcutBarManager();
        this.m_craftHandler = new CraftHandler();
        this.m_emoteHandler = new ClientEmoteHandler();
        this.m_personalSpaceHandler = new PersonalSpaceHandler();
        this.m_mapHandler = new ClientMapHandler();
        this.m_travelHandler = new TravelHandler();
        this.m_respawnPointHandler = new RespawnPointHandler();
        this.m_inventoryHandler = new InventoryHandler();
        this.m_bags = new ClientBagContainer();
        this.m_enableItemTransfer = true;
        this.m_replaceFlag = false;
        this.m_waitingForResult = false;
        this.m_actionInProgress = new ActionInProgress();
        this.m_partyComportment = new PartyComportment(this);
        this.m_canMoveAndInteract = true;
        this.m_runningEffectManager = new LocalTimedRunningEffectManager(this);
        this.m_xp = new PlayerCharacterXp(false);
        this.m_aptitudeInventory = new AptitudeInventoryImpl();
        this.m_aptitudeBonusInventory = new AptitudeBonusInventory();
        this.m_zoneBuffs = new LocalPlayerZoneBuffs();
        this.m_fightChallenges = new ClientFightChallengeContext();
        this.m_locks = null;
        this.m_spectatorModeMovementRequestHandler = new SpectatorModeMovementRequestHandler();
        this.m_antiAddictionDataHandler = new AntiAddictionDataHandler();
        this.m_characterChoiceState = false;
        ((FighterCharacteristicManager)this.getCharacteristics()).addListener(this);
        this.m_craftHandler.addListener(ClientCraftListener.INSTANCE);
        this.m_inventoryHandler.createInventory(InventoryType.QUEST);
        this.m_inventoryHandler.createInventory(InventoryType.TEMPORARY_INVENTORY);
        this.m_inventoryHandler.createInventory(InventoryType.COSMETICS);
        this.m_inventoryHandler.createInventory(InventoryType.PET_COSMETICS);
        final TemporaryInventory temporaryInventory = (TemporaryInventory)this.m_inventoryHandler.getInventory(InventoryType.TEMPORARY_INVENTORY);
        temporaryInventory.addListener(this.m_temporaryInventoryControler = new ClientTemporaryInventoryController(temporaryInventory));
        final CosmeticsInventory cosmeticsInventory = (CosmeticsInventory)this.m_inventoryHandler.getInventory(InventoryType.COSMETICS);
        cosmeticsInventory.addListener(this.m_cosmeticsInventoryView = new CosmeticsInventoryView());
        final CosmeticsInventory petCosmeticsInventory = (CosmeticsInventory)this.m_inventoryHandler.getInventory(InventoryType.PET_COSMETICS);
        petCosmeticsInventory.addListener(this.m_petCosmeticsInventoryView = new PetCosmeticsInventoryView());
        this.m_equipmentInventory.addObserver(new PetEquipmentInventoryListener(this));
        this.m_actionInProgress.setUiIsEnabled(true);
        this.m_initialized = false;
        this.m_zoneBuffs.addObserver(new LocalPlayerZoneBuffsObserver() {
            @Override
            public void onTick() {
                PropertiesProvider.getInstance().firePropertyValueChanged(LocalPlayerCharacter.this, "states");
            }
        });
        TimeManager.INSTANCE.addListener(this.getRunningEffectFieldProvider());
        this.m_aptitudeBonusInventory.addListener(new ReloadEffectsOnLevelUpdate(this));
    }
    
    @Override
    protected WakfuAccountInformationHandler createAccountInformationHandler() {
        final WakfuAccountInformationHandler handler = new WakfuAccountInformationHandler();
        handler.setListener(new WakfuAccountInformationListener(this));
        return handler;
    }
    
    @Override
    protected ClientGuildInformationHandler createGuildHandler() {
        return new GuildLocalInformationHandler();
    }
    
    @Override
    public boolean isLocalPlayer() {
        return true;
    }
    
    @Override
    public LockContext getLockContext() {
        return this.m_locks;
    }
    
    public ClientFightChallengeContext getFightChallengesContext() {
        return this.m_fightChallenges;
    }
    
    @Override
    public void setId(final long id) {
        super.setId(id);
        CharacterImageGenerator.getInstance().addListener(id, this);
    }
    
    public void removeCharacterImageGenerator() {
        CharacterImageGenerator.getInstance().removeListener(this.m_id, this);
    }
    
    @Override
    public void initialize() {
        this.m_runningEffectManager.setFieldProviderRef(this.getRunningEffectFieldProvider());
        this.getActor().enableAlphaMask(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY));
        this.setFight(-1);
        final int hpValue = this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).unboundedValue();
        this.loadBreedSpecificities();
        this.initializeSkillsOwnerBreed();
        this.m_runningEffectManager.clear();
        this.initialiseCharacteristicsToBaseValue();
        this.m_spellInventoryManager.initialize();
        this.addEquipmentObserver();
        this.m_shortcutBarManager.initializeItemShortcuts(this.m_equipmentInventory, this.m_bags);
        this.getHpRegenHandler().initializeRegeneration();
        CraftDisplayer.INSTANCE.addAllCrafts(this.m_craftHandler.getKnownCrafts());
        this.m_cosmeticsInventoryView.refreshSelected();
        this.m_initialized = true;
        this.initializeRunningEffectManagerAndLoadEffects();
        this.unserializeOutFightEffects();
        this.setHpValue(hpValue);
        this.updateStateDisplay(true);
        final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(this.getId());
        context.getListener().onInitialize(context);
        this.m_fightChallenges.setListener(FightChallengeEventListener.INSTANCE);
        this.m_bags.onInventoriesCreated();
    }
    
    private void unserializeOutFightEffects() {
        if (this.m_stateREToUnserializeAtInit != null) {
            this.m_runningEffectManager.fromRawStateRunningEffects(this.m_stateREToUnserializeAtInit, this.m_ownContext, this);
            this.m_stateREToUnserializeAtInit = null;
        }
    }
    
    private void loadBreedSpecificities() {
        if (this.m_breed == AvatarBreed.OSAMODAS && this.getSymbiot() == null) {
            this.m_symbioticCharacter = new LocalSymbioticCharacter();
            this.setSymbiot(new Symbiot());
        }
    }
    
    private void initializeSkillsOwnerBreed() {
        for (final Skill skill : this.m_skillInventory) {
            skill.setOwnerBreed((AvatarBreed)this.m_breed);
        }
    }
    
    private void addEquipmentObserver() {
        this.m_equipmentInventory.addObserver(new InventoryObserver() {
            @Override
            public void onInventoryEvent(final InventoryEvent event) {
                if (!(event instanceof InventoryItemModifiedEvent)) {
                    return;
                }
                final InventoryItemModifiedEvent imEvent = (InventoryItemModifiedEvent)event;
                final Item concernedItem = (Item)imEvent.getConcernedItem();
                final InventoryEvent.Action action = event.getAction();
                if (action == InventoryEvent.Action.ITEM_ADDED || action == InventoryEvent.Action.ITEM_ADDED_AT) {
                    final int itemReferenceId = concernedItem.getReferenceId();
                    ScriptEventManager.getInstance().fireEvent(new ItemEquippedScriptEvent(itemReferenceId));
                    if (!concernedItem.hasPet()) {
                        return;
                    }
                    final Pet pet = concernedItem.getPet();
                    if (pet.getDefinition().hasMount()) {
                        final StartRidingRequestMessage msg = new StartRidingRequestMessage();
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                    }
                }
                else if (action == InventoryEvent.Action.ITEM_REMOVED || action == InventoryEvent.Action.ITEM_REMOVED_AT) {
                    if (!concernedItem.hasPet()) {
                        return;
                    }
                    final Pet pet2 = concernedItem.getPet();
                    if (pet2.getDefinition().hasMount()) {
                        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
                        netMsg.setModificationType((byte)2);
                        netMsg.setOccupationType((short)14);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                    }
                }
            }
        });
    }
    
    @Override
    public boolean isValidForEffectExecution() {
        return this.m_initialized;
    }
    
    @Override
    public void initializeSerializer() {
        super.initializeSerializer();
        new LocalPlayerCharacterPartBags(this.m_serializer.getBagsPart());
        new LocalPlayerCharacterPartBreedSpecific(this.m_serializer.getBreedSpecificPart());
        new LocalPlayerCharacterPartDimensionalBagForClient(this.m_serializer.getDimensionalBagForLocalClientPart());
        new LocalPlayerCharacterPartChallenges(this.m_serializer.getChallengesPart());
        new LocalPlayerCharacterPartShortcutInventories(this.m_serializer.getShortcutInventoriesPart());
        new LocalPlayerCharacterPartEmoteInventory(this.m_serializer.getEmoteInventoryPart());
        new LocalPlayerCharacterPartLandMarkInventory(this.m_serializer.getLandMarkInventoryPart());
        new LocalPlayerCharacterPartDiscoveredItems(this.m_serializer.getDiscoveredItemsPart());
        new LocalPlayerCharacterPartSkillInventory(this.m_serializer.getSkillInventoryPart());
        new LocalPlayerCharacterPartCraft(this.m_serializer.getCraftPart());
        new LocalPlayerCharacterPartAptitudeInventory(this.m_serializer.getAptitudeInventoryPart());
        new LocalPlayerCharacterPartAptitudeBonusInventory(this.m_serializer.getAptitudeBonusInventoryPart());
        new LocalPlayerCharacterPartSpellInventory(this.m_serializer.getSpellInventoryPart());
        new LocalPlayerCharacterPartEquipmentInventory(this.m_serializer.getEquipmentInventoryPart());
        new LocalPlayerCharacterPartTitle(this.m_serializer.getTitlePart());
        new LocalPlayerCharacterPartAccountInformation(this.m_serializer.getAccountInformationPart());
        new LocalPlayerCharacterPartDimensionalBagViewsInventory(this.m_serializer.getDimensionalBagViewInventoryPart());
        new LocalPlayerCharacterPartInventories(this.m_serializer.getInventoriesPart());
        new LocalPlayerCharacterPartGuildLocalInfo(this.m_serializer.getGuildLocalInfoPart());
        new LocalPlayerCharacterPartLocks(this.m_serializer.getLockClientPart());
        new PlayerCharacterPartPersonalEffects(this.m_serializer.getPersonalEffectsPart());
        new LocalPlayerCharacterPartAntiAddiction(this.m_serializer.getAntiAddictionPart());
        new PlayerCharacterPartNationPvpMoney(this.m_serializer.getNationPvpMoneyPart());
    }
    
    @Override
    public void initialiseCharacteristicsToBaseValue() {
        this.getCharacteristics().makeDefault();
        this.initialiseCharacteristicsProcedures();
        this.initialiseCharacteristicsToDefault(this);
        this.updateSecondaryCharacteristicsFromLevel();
        for (final Elements element : Elements.values()) {
            if (element.hasMasteryCharacteristic()) {
                this.updateElementMastery(element);
            }
        }
    }
    
    private void forceHPWhileDead() {
        this.getHpRegenHandler().setCustomRegen(0);
        this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(0);
    }
    
    public void updateAllElementMasteries() {
        for (final Elements element : Elements.values()) {
            if (element.hasMasteryCharacteristic()) {
                this.updateElementMastery(element);
            }
        }
    }
    
    private void updateSecondaryCharacteristicsFromLevel() {
        if (!(this.m_breed instanceof AvatarBreed)) {
            return;
        }
        ((AvatarBreed)this.m_breed).getSecondaryCharacsCalculator().applyForInitialize(this.getCharacteristics(), this.getLevel());
    }
    
    public void initialiseCharacteristicsToDefault(final EffectUser effectUser) {
        for (final FighterCharacteristicType type : FighterCharacteristicType.values()) {
            if (type.isExpandable()) {
                effectUser.getCharacteristic(type).setMax(this.m_breed.getBaseCharacteristicValue(type));
            }
            else {
                effectUser.getCharacteristic(type).set(this.m_breed.getBaseCharacteristicValue(type));
            }
        }
        this.updateCharacsFromBonusPoint();
        effectUser.getCharacteristic(FighterCharacteristicType.AP).toMax();
        effectUser.getCharacteristic(FighterCharacteristicType.WP).toMax();
        effectUser.getCharacteristic(FighterCharacteristicType.MP).toMax();
        effectUser.getCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH).toMax();
        this.updateCharacBoundWithLevel();
    }
    
    private void updateCharacsFromBonusPoint() {
        final TByteShortIterator it1 = this.m_bonusPointCharacteristics.getXpBonusPointIterator();
        while (it1.hasNext()) {
            it1.advance();
            final byte characTypeId = it1.key();
            final FighterCharacteristicType characType = FighterCharacteristicType.getCharacteristicTypeFromId(characTypeId);
            final int points = this.m_bonusPointCharacteristics.computeXpBonusPointToValue(this.getBreed(), characTypeId);
            if (characType.isExpandable()) {
                this.getCharacteristic((CharacteristicType)characType).updateMaxValue(points);
            }
            else {
                this.getCharacteristic((CharacteristicType)characType).add(points);
            }
        }
    }
    
    @Override
    protected void updateLocalXpFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentLevelPercentage", "xpRatio");
    }
    
    @Override
    public ItemEquipment getEquipmentInventory() {
        return this.m_equipmentInventory;
    }
    
    public Item getFromEquipmentOrInventory(final long itemId) {
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipmentInventory).getWithUniqueId(itemId);
        if (item != null && item.isActive()) {
            return item;
        }
        return this.m_bags.getItemFromInventories(itemId);
    }
    
    public void setCharacterChoiceState(final boolean characterChoiceState) {
        this.m_characterChoiceState = characterChoiceState;
    }
    
    private void refreshEquipmentAppearance() {
        if (this.m_equipementAppearance == null) {
            this.m_equipementAppearance = new TByteIntHashMap();
        }
        else {
            this.m_equipementAppearance.clear();
        }
        final EquipmentPosition[] equipmentPositions = EquipmentPosition.values();
        for (int i = 0; i < equipmentPositions.length; ++i) {
            final Item item = ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipmentInventory).getFromPosition(equipmentPositions[i].m_id);
            if (item != null) {
                this.m_equipementAppearance.put(equipmentPositions[i].m_id, item.getReferenceId());
            }
        }
    }
    
    @Nullable
    @Override
    public TByteIntHashMap getEquipmentAppearance() {
        if (!this.m_characterChoiceState) {
            this.refreshEquipmentAppearance();
        }
        return this.m_equipementAppearance;
    }
    
    @Override
    public SkillInventory<Skill> getSkillInventory() {
        return this.m_skillInventory;
    }
    
    @Override
    public Symbiot getSymbiot() {
        if (this.m_symbioticCharacter != null) {
            return this.m_symbioticCharacter.getSymbiot();
        }
        return null;
    }
    
    @Override
    public void setSymbiot(final Symbiot symbiot) {
        if (this.m_symbioticCharacter == null) {
            this.m_symbioticCharacter = new LocalSymbioticCharacter();
        }
        this.m_symbioticCharacter.setSymbiot(symbiot);
        symbiot.setOwner(this);
    }
    
    @Override
    public void onSymbiotReset() {
        if (this.m_symbioticCharacter != null) {
            this.m_symbioticCharacter.onSymbiotReset();
        }
    }
    
    @Override
    public byte getMaximumSeducableCreatures() {
        if (this.m_symbioticCharacter != null) {
            return this.m_symbioticCharacter.getMaximumSeducableCreatures();
        }
        return 0;
    }
    
    @Override
    public void setMaximumSeducableCreatures(final byte newMax) {
        if (this.m_symbioticCharacter != null) {
            this.m_symbioticCharacter.setMaximumSeducableCreatures(newMax);
        }
    }
    
    @Override
    public void onSymbiotAddCreature(final byte index) {
        if (this.m_symbioticCharacter != null) {
            this.m_symbioticCharacter.onSymbiotAddCreature(index);
        }
    }
    
    @Override
    public void onSymbiotReleaseCreature(final byte index) {
        if (this.m_symbioticCharacter != null) {
            this.m_symbioticCharacter.onSymbiotReleaseCreature(index);
        }
    }
    
    public boolean isEnableItemTransfer() {
        return this.m_enableItemTransfer;
    }
    
    public void setEnableItemTransfer(final boolean enableItemTransfer) {
        if (this.m_enableItemTransfer != enableItemTransfer) {
            this.m_enableItemTransfer = enableItemTransfer;
            UIEquipmentFrame.getInstance().setEnableBagTransfer(this.m_enableItemTransfer);
        }
    }
    
    public void transfertItem(final Item item, final short quantity, final byte sourcePosition, final byte destinationPosition, final long sourceId, final long destinationId) {
        if (!this.m_enableItemTransfer) {
            return;
        }
        if (sourceId != 2L && destinationId != 2L) {
            this.TIContainerToContainer(item, quantity, sourceId, destinationId, sourcePosition, destinationPosition);
        }
        else if (sourceId == 2L && destinationId != 2L) {
            this.TIEquipmentToContainer(item, destinationId, sourcePosition, destinationPosition);
        }
        else if (sourceId != 2L) {
            final long target = UIEquipmentFrame.getInstance().getCharacterId();
            if (item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP).isValid(HeroesManager.INSTANCE.getHero(target), item, null, this.getEffectContext())) {
                ErrorsMessageTranslator.getInstance().pushMessage(58, 3, new Object[0]);
                return;
            }
            this.TIContainerToEquipment(item, sourceId, destinationPosition, target);
        }
        else {
            if (sourcePosition == destinationPosition) {
                return;
            }
            if (item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP).isValid(this, item, null, this.getEffectContext())) {
                return;
            }
            this.TIEquipmentToEquipment(item, sourcePosition, destinationPosition);
        }
        if (sourceId == 2L || destinationId == 2L) {
            EquipmentDialogActions.onChangeEquipement(this);
        }
        UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
        MasterRootContainer.getInstance().cancelDragNDrop();
    }
    
    private void TIContainerToContainer(final Item item, final short quantity, final long sourceContainerId, final long destinationContainerId, final byte sourcePosition, final byte destinationPosition) {
        if (quantity == 0) {
            return;
        }
        int result = 1;
        long newId = 0L;
        final long itemUID = item.getUniqueId();
        final AbstractBag sourceContainer = this.m_bags.get(sourceContainerId);
        final AbstractBag destinationContainer = HeroUtils.getBagFromHero(this.m_ownerId, destinationContainerId);
        if (destinationContainer == null) {
            return;
        }
        if (item.getQuantity() > quantity && quantity != -1) {
            final Item newItem = item.getCopy(false);
            try {
                newId = newItem.getUniqueId();
                result = InventoryToInventoryExchanger.getInstance().moveItem(sourceContainer.getInventory(), destinationContainer.getInventory(), destinationPosition, item, quantity, newItem, this, this.getEffectContext());
            }
            catch (Exception ex) {
                LocalPlayerCharacter.m_logger.error((Object)"Exception survenue durant le transfert de bag ? bag (avec split) : ", (Throwable)ex);
            }
        }
        else {
            try {
                result = InventoryToInventoryExchanger.getInstance().moveItem(sourceContainer.getInventory(), destinationContainer.getInventory(), destinationPosition, item, quantity, null, this, this.getEffectContext());
            }
            catch (Exception ex2) {
                LocalPlayerCharacter.m_logger.error((Object)"Exception survenue durant le transfert de bag ? bag (sans split) : ", (Throwable)ex2);
            }
        }
        if (result == 0) {
            final ItemInventoryMoveRequestMessage netMessage = new ItemInventoryMoveRequestMessage();
            netMessage.setDestination(destinationContainerId);
            netMessage.setSource(sourceContainerId);
            netMessage.setDestinationPosition(destinationPosition);
            netMessage.setItemUId(itemUID);
            netMessage.setNewUid(newId);
            netMessage.setQuantity(quantity);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
    }
    
    private void TIEquipmentToContainer(final Item item, final long destinationId, final byte sourcePosition, final byte destinationPosition) {
        final AbstractBag destinationContainer = HeroUtils.getBagFromHero(this.m_ownerId, destinationId);
        if (destinationContainer == null) {
            return;
        }
        try {
            short destPos = destinationPosition;
            final Item targetItem = destinationContainer.getFromPosition(destinationPosition);
            if (destinationPosition >= 0 && targetItem != null && !item.canStackWith(targetItem)) {
                return;
            }
            final int removeResult = ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipmentInventory).getContentChecker().canRemoveItem((Inventory<Item>)this.m_equipmentInventory, item);
            final int addResult = destinationContainer.getContentChecker().canAddItem((Inventory<Item>)destinationContainer.getInventory(), item);
            if (destPos == -1) {
                destPos = destinationContainer.getFirstStackableIndeForContent(item);
                if (destPos == -1) {
                    destPos = destinationContainer.getFirstFreeIndex();
                }
            }
            if (removeResult < 0 || addResult < 0) {
                return;
            }
            final EquipmentToItemInventoryMoveRequestMessage netMessage = new EquipmentToItemInventoryMoveRequestMessage();
            netMessage.setTargetBagId(destinationId);
            netMessage.setItemId(item.getUniqueId());
            netMessage.setTargetPosition(destinationPosition);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
        catch (Exception ex) {
            LocalPlayerCharacter.m_logger.error((Object)"L'objet n a pas pu etre ajout? : ", (Throwable)ex);
        }
    }
    
    private void TIContainerToEquipment(final Item item, final long sourceContainerId, final byte destinationPosition, final long target) {
        final LocalPlayerCharacter hero = HeroesManager.INSTANCE.getHero(target);
        if (item == null) {
            LocalPlayerCharacter.m_logger.error((Object)"On tente de transf\u00e9rer un item null !");
            return;
        }
        if ((this.isOnFight() && this.getCurrentFight().getStatus() != AbstractFight.FightStatus.PLACEMENT) || !item.getReferenceItem().getItemType().isEquipmentPositionValid(EquipmentPosition.fromId(destinationPosition))) {
            ErrorsMessageTranslator.getInstance().pushMessage(58, 3, new Object[0]);
            return;
        }
        final ItemEquipment equipment = hero.m_equipmentInventory;
        boolean cantEquip = false;
        if (equipment.isPositionLocked(destinationPosition)) {
            cantEquip = true;
        }
        if (!EquipmentInventoryChecker.petCanBeEquipped(item)) {
            cantEquip = true;
        }
        if (!((ArrayInventoryWithoutCheck<Item, R>)equipment).getContentChecker().checkCriterion(item, hero, this.getAppropriateContext())) {
            cantEquip = true;
        }
        if (cantEquip) {
            ErrorsMessageTranslator.getInstance().pushMessage(60, 3, new Object[0]);
            return;
        }
        final Item equippedItem = ((ArrayInventoryWithoutCheck<Item, R>)equipment).getFromPosition(destinationPosition);
        Item realItem;
        if ((realItem = equippedItem) != null) {
            if (!equippedItem.isActive()) {
                final EquipmentPosition[] arr$;
                final EquipmentPosition[] positions = arr$ = equippedItem.getReferenceItem().getItemType().getEquipmentPositions();
                for (final EquipmentPosition pos : arr$) {
                    final Item realItem2 = ((ArrayInventoryWithoutCheck<Item, R>)equipment).getFromPosition(pos.m_id);
                    if (realItem2.getReferenceId() == realItem.getReferenceId()) {
                        realItem = realItem2;
                        break;
                    }
                }
            }
            final int removeResult = ((ArrayInventoryWithoutCheck<Item, R>)equipment).getContentChecker().canRemoveItem((Inventory<Item>)equipment, realItem);
            if (removeResult < 0) {
                ErrorsMessageTranslator.getInstance().pushMessage(InventoryCheckerError.getResultCode(removeResult, 60), 3, new Object[0]);
                return;
            }
            final int replaceResult = ((ArrayInventoryWithoutCheck<Item, R>)equipment).getContentChecker().canReplaceItem((Inventory<Item>)equipment, equippedItem, item);
            if (replaceResult < 0) {
                ErrorsMessageTranslator.getInstance().pushMessage(InventoryCheckerError.getResultCode(replaceResult, 60), 3, new Object[0]);
                return;
            }
        }
        else {
            final int addResult = ((ArrayInventoryWithoutCheck<Item, R>)equipment).getContentChecker().canAddItem((Inventory<Item>)equipment, item, destinationPosition);
            if (addResult < 0) {
                ErrorsMessageTranslator.getInstance().pushMessage(InventoryCheckerError.getResultCode(addResult, 60), 3, new Object[0]);
                return;
            }
        }
        if (item.hasBind() && !item.isBound() && !item.getBind().getType().isOnPickup()) {
            final String msgText = WakfuTranslator.getInstance().getString("item.bound.onEquipQuestion");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        final ItemInventoryToEquipmentMoveRequestMessage netMessage = new ItemInventoryToEquipmentMoveRequestMessage();
                        netMessage.setCharacterId(target);
                        netMessage.setSource(sourceContainerId);
                        netMessage.setDestinatairePosition(destinationPosition);
                        netMessage.setItemUId(item.getUniqueId());
                        netMessage.setNewUid(item.getUniqueId());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                    }
                }
            });
        }
        else {
            final ItemInventoryToEquipmentMoveRequestMessage netMessage = new ItemInventoryToEquipmentMoveRequestMessage();
            netMessage.setCharacterId(target);
            netMessage.setSource(sourceContainerId);
            netMessage.setDestinatairePosition(destinationPosition);
            netMessage.setItemUId(item.getUniqueId());
            netMessage.setNewUid(item.getUniqueId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
    }
    
    private void TIEquipmentToEquipment(final Item item, final byte sourcePosition, final byte destinationPosition) {
        boolean bOk;
        try {
            bOk = (EquipmentToEquipmentExchanger.getInstance().moveItem(this.m_equipmentInventory, sourcePosition, this.m_equipmentInventory, destinationPosition, item, this, this.getEffectContext()) == 0);
        }
        catch (Exception ex) {
            LocalPlayerCharacter.m_logger.error((Object)"Exception", (Throwable)ex);
            bOk = false;
        }
        if (bOk) {
            final EquipmentItemMoveRequestMessage netMessage = new EquipmentItemMoveRequestMessage();
            netMessage.setItemUId(item.getUniqueId());
            netMessage.setDestinationPosition(destinationPosition);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
        this.m_replaceFlag = false;
    }
    
    public void throwAwayItem(final Item item, final short quantity, final long sourceId) {
        if (item.isRent()) {
            return;
        }
        if (sourceId == 2L) {
            if (((ArrayInventoryWithoutCheck<Item, R>)this.m_equipmentInventory).remove(item)) {
                final EquipmentPosition[] equipmentPositions = item.getReferenceItem().getItemType().getLinkedPositions();
                if (equipmentPositions != null) {
                    for (int i = 0; i < equipmentPositions.length; ++i) {
                        final EquipmentPosition equipmentPosition = equipmentPositions[i];
                        this.m_equipmentInventory.removeAt(equipmentPosition.m_id);
                    }
                }
                final ItemInventoryToFloorMoveRequestMessage netMessage = new ItemInventoryToFloorMoveRequestMessage();
                netMessage.setSource(sourceId);
                netMessage.setItemUId(item.getUniqueId());
                netMessage.setQuantity((short)(-1));
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
            }
        }
        else {
            final AbstractBag bag = this.m_bags.get(sourceId);
            final ArrayInventory<Item, RawInventoryItem> sourceContainer = bag.getInventory();
            if (quantity == 0) {
                return;
            }
            final SimpleCriterion dropCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.DROP);
            final SimpleCriterion exchangeCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE);
            if (item.isBound() || (dropCriterion != null && !dropCriterion.isValid(this, this.getPosition(), item, this.getEffectContext())) || (exchangeCriterion != null && !exchangeCriterion.isValid(this, this.getPosition(), item, this.getEffectContext()))) {
                return;
            }
            if (quantity < item.getQuantity() && quantity > 0) {
                if (sourceContainer.updateQuantity(item.getUniqueId(), (short)(-quantity))) {
                    final ItemInventoryToFloorMoveRequestMessage netMessage2 = new ItemInventoryToFloorMoveRequestMessage();
                    netMessage2.setSource(bag.getUid());
                    netMessage2.setQuantity(quantity);
                    netMessage2.setItemUId(item.getUniqueId());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
                }
            }
            else if (sourceContainer.remove(item)) {
                final ItemInventoryToFloorMoveRequestMessage netMessage2 = new ItemInventoryToFloorMoveRequestMessage();
                netMessage2.setSource(bag.getUid());
                netMessage2.setItemUId(item.getUniqueId());
                netMessage2.setQuantity((short)(-1));
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
            }
        }
    }
    
    @Override
    public WakfuEffectContextForUniqueUser getOwnContext() {
        return this.m_ownContext;
    }
    
    public void resetLastFightSpellUsage() {
        this.m_lastFightSpellUsage.clear();
    }
    
    public void incrementFightSpellUsage(final SpellLevel spell) {
        Short usage = this.m_lastFightSpellUsage.get(spell);
        if (usage == null) {
            usage = 0;
        }
        usage = (short)(usage + 1);
        this.m_lastFightSpellUsage.put(spell, usage);
    }
    
    public ArrayList<ObjectPair<SpellLevel, Short>> getAllFightSpellUsage() {
        final ArrayList<ObjectPair<SpellLevel, Short>> spellUsage = new ArrayList<ObjectPair<SpellLevel, Short>>();
        for (final SpellLevel spell : this.m_lastFightSpellUsage.keySet()) {
            final Short value = this.m_lastFightSpellUsage.get(spell);
            if (value != null) {
                spellUsage.add(new ObjectPair<SpellLevel, Short>(spell, value));
            }
        }
        return spellUsage;
    }
    
    public void resetLastFightItemUsage() {
        this.m_lastFightItemUsage.clear();
    }
    
    public void incrementFightSpellUsage(final Item item) {
        Short usage = this.m_lastFightItemUsage.get(item);
        if (usage == null) {
            usage = 0;
        }
        usage = (short)(usage + 1);
        this.m_lastFightItemUsage.put(item, usage);
    }
    
    public ArrayList<ObjectPair<Item, Short>> getAllFightItemUsage() {
        final ArrayList<ObjectPair<Item, Short>> itemUsage = new ArrayList<ObjectPair<Item, Short>>();
        for (final Item item : this.m_lastFightItemUsage.keySet()) {
            final Short value = this.m_lastFightItemUsage.get(item);
            if (value != null) {
                itemUsage.add(new ObjectPair<Item, Short>(item, value));
            }
        }
        return itemUsage;
    }
    
    public final void askForJoinAllyFight(final long allyId) {
        final boolean locked = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY);
        final FightJoinRequestMessage fightJoinRequestMessage = new FightJoinRequestMessage(allyId, locked);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(fightJoinRequestMessage);
    }
    
    private CharacterInfo getPotentialAlly(final CharacterInfo opponent) {
        final ExternalFightInfo opponentFight = opponent.getCurrentExternalFightInfo();
        final byte opponentTeam = opponentFight.getTeamId(opponent.getId());
        final Collection<CharacterInfo> allyTeam = opponentFight.getFightersNotInTeam(opponentTeam);
        final Iterator<CharacterInfo> it = allyTeam.iterator();
        CharacterInfo ally = null;
        if (it.hasNext()) {
            ally = it.next();
        }
        return ally;
    }
    
    public void askForFightCreation(final CharacterInfo opponent) {
        final FightCreationRequestMessage message = new FightCreationRequestMessage();
        message.setTargetId(opponent.getId());
        message.setTargetPosition(opponent.getWorldCellX(), opponent.getWorldCellY(), opponent.getWorldCellAltitude());
        if (WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY)) {
            message.lockInitially();
        }
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
    }
    
    public void askForProtectorStake(final int protectorId) {
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new GetProtectorFightStakeRequestMessage(protectorId));
    }
    
    @Override
    public void partitionChanged(final PathMobile pathMobile, final LocalPartition oldPartition, final LocalPartition newPartition) {
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventPartitionChanged(oldPartition, newPartition));
        final Territory territory = TerritoriesView.INSTANCE.getFromPartition(newPartition.getX(), newPartition.getY());
        if (territory != this.m_currentTerritory) {
            try {
                this.onTerritoryChanged(territory);
            }
            catch (Exception e) {
                LocalPlayerCharacter.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public Fight getCurrentOrObservedFight() {
        if (this.getCurrentFight() != null) {
            return this.getCurrentFight();
        }
        return this.m_observedFight;
    }
    
    public int getCurrentOrObservedFightId() {
        final Fight fight = this.getCurrentOrObservedFight();
        return (fight == null) ? -1 : fight.getId();
    }
    
    public boolean canActivateInteractiveElements() {
        return !this.isOnFight() && this.canMoveAndInteract();
    }
    
    public boolean canAffordRecipe(final CraftRecipe craftRecipe) {
        final TIntShortIterator it = craftRecipe.ingredientsIterator();
        while (it.hasNext()) {
            it.advance();
            if (!this.possessedItemQuantity(it.key(), it.value())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean possessedItemQuantity(final int referenceId, final short quantity) {
        int count = 0;
        for (final Item item : this.m_bags.getAllWithReferenceId(referenceId)) {
            if (!item.isRent()) {
                count += item.getQuantity();
            }
            if (count >= quantity) {
                return true;
            }
        }
        final QuestInventory inventory = (QuestInventory)this.getInventory(InventoryType.QUEST);
        final QuestItem item2 = inventory.getItem(referenceId);
        return item2 != null && item2.getQuantity() >= quantity;
    }
    
    public int getMaxRecipeNumber(final CraftRecipe craftRecipe) {
        int max = Integer.MAX_VALUE;
        final TIntShortIterator it = craftRecipe.ingredientsIterator();
        while (it.hasNext()) {
            it.advance();
            final int referenceId = it.key();
            int quantity = this.m_bags.getItemQuantity(referenceId);
            if (quantity == 0) {
                final QuestInventory inventory = (QuestInventory)this.getInventory(InventoryType.QUEST);
                final QuestItem item = inventory.getItem(referenceId);
                if (item != null) {
                    quantity = item.getQuantity();
                }
            }
            final int currentMax = quantity / it.value();
            if (currentMax < max) {
                max = currentMax;
            }
        }
        return max;
    }
    
    public boolean isOnFriendNation() {
        final Protector currentZoneProtector = ProtectorView.getInstance().getProtector();
        if (currentZoneProtector == null) {
            return true;
        }
        final Nation playerNation = this.getCitizenComportment().getNation();
        final NationAlignement alignement = playerNation.getDiplomacyManager().getAlignment(currentZoneProtector.getCurrentNationId());
        return alignement == NationAlignement.ALLIED;
    }
    
    public boolean isInOwnHavenWorld() {
        final short instanceId = this.getInstanceId();
        final HavenWorldDefinition definition = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(instanceId);
        return definition != null && definition.getId() == this.getGuildHandler().getHavenWorldId();
    }
    
    public boolean isInHavenWorld() {
        final short instanceId = this.getInstanceId();
        final HavenWorldDefinition definition = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(instanceId);
        return definition != null;
    }
    
    public boolean isOnEnemyNationButNotPrisonInstance() {
        final Nation travellingNation = this.getTravellingNation();
        return travellingNation != null && travellingNation.getJailInstanceId() != this.getInstanceId() && this.getCitizenComportment().isNationEnemy();
    }
    
    public void displayCraftLevelGainedParticle() {
        this.displayParticleOnCharacter(800200);
    }
    
    private void displayParticleOnCharacter(final int apsId) {
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId);
        if (system == null) {
            return;
        }
        system.setTarget(this.getActor());
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
    }
    
    @Override
    public com.ankamagames.wakfu.common.game.inventory.reborn.definition.Inventory getInventory(final InventoryType type) {
        return this.m_inventoryHandler.getInventory(type);
    }
    
    @Override
    public boolean hasLockedSpell() {
        return this.m_lockedSpellId > 0;
    }
    
    @Override
    public int getLockedSpellId() {
        return this.m_lockedSpellId;
    }
    
    @Override
    public void registerLockedSpellId(final int spellId) {
        this.m_lockedSpellId = spellId;
    }
    
    @Override
    public void unregisterLockedSpell() {
        this.m_lockedSpellId = 0;
    }
    
    @Override
    public void changePlayerSpellsByMonsterOnes(final int breedId, final MonsterSpellsLevel level) {
        super.changePlayerSpellsByMonsterOnes(breedId, level);
        this.m_shortcutBarManager.showSymbiotBar();
    }
    
    @Override
    protected void initTemporarySpellInventory(final int breedId, final MonsterSpellsLevel levelType) {
        super.initTemporarySpellInventory(breedId, levelType);
        final SymbiotShortcutBar bar = this.m_shortcutBarManager.getSymbiotShortcutBar();
        if (bar != null) {
            bar.setControlledCharacter(this);
            bar.forceClean();
        }
        this.m_shortcutBarManager.showSymbiotBar();
    }
    
    @Override
    public void resetTemporarySpellInventory() {
        super.resetTemporarySpellInventory();
        this.m_shortcutBarManager.hideSymbiotBar();
    }
    
    public void setActivateUIArcadeDungeonFrame(final boolean activateUIArcadeDungeonFrame) {
        this.m_activateUIArcadeDungeonFrame = activateUIArcadeDungeonFrame;
    }
    
    public void resetEquipmentBonusCache() {
        this.m_cachedEquipmentBonus = null;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "equipmentBonus");
    }
    
    @Override
    public void onDone(final Texture texture, final String errorMsg) {
        if (texture != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "characterIconUrl");
        }
        else {
            LocalPlayerCharacter.m_logger.warn((Object)errorMsg);
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("cosmeticsInventory")) {
            return this.m_cosmeticsInventoryView;
        }
        if (fieldName.equals("petCosmeticsInventory")) {
            return this.m_petCosmeticsInventoryView;
        }
        if (fieldName.equals("shortcutBarManager")) {
            return this.m_shortcutBarManager;
        }
        if (fieldName.equals("characterIconUrl")) {
            return CharacterImageGenerator.getInstance().getCharacterImage(this.getId());
        }
        if (fieldName.equals("equipment")) {
            final ArrayList<Item> equipment = new ArrayList<Item>();
            for (final Item itemEquipment : this.m_equipmentInventory) {
                equipment.add(itemEquipment);
            }
            return equipment;
        }
        if (fieldName.equals("equipmentBonus")) {
            if (this.m_cachedEquipmentBonus == null) {
                this.m_cachedEquipmentBonus = PlayerCharacter.getEquipmentBonus(this, this.m_equipmentInventory);
            }
            return this.m_cachedEquipmentBonus;
        }
        if (fieldName.equals("bags")) {
            return this.m_bags;
        }
        if (fieldName.equals("actorAnimationName")) {
            return "AnimStatique";
        }
        if (fieldName.equals("headEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.HEAD.m_id);
        }
        if (fieldName.equals("hairEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.HAIR.m_id);
        }
        if (fieldName.equals("faceEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.FACE.m_id);
        }
        if (fieldName.equals("shoulderEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.SHOULDERS.m_id);
        }
        if (fieldName.equals("neckEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.NECK.m_id);
        }
        if (fieldName.equals("chestEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.CHEST.m_id);
        }
        if (fieldName.equals("armsEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.ARMS.m_id);
        }
        if (fieldName.equals("leftHandEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.LEFT_HAND.m_id);
        }
        if (fieldName.equals("rightHandEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.RIGHT_HAND.m_id);
        }
        if (fieldName.equals("beltEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.BELT.m_id);
        }
        if (fieldName.equals("skirtEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.SKIRT.m_id);
        }
        if (fieldName.equals("trousersEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.TROUSERS.m_id);
        }
        if (fieldName.equals("legsEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.LEGS.m_id);
        }
        if (fieldName.equals("backEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.BACK.m_id);
        }
        if (fieldName.equals("wingEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.WING.m_id);
        }
        if (fieldName.equals("firstWeaponEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.FIRST_WEAPON.m_id);
        }
        if (fieldName.equals("secondWeaponEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.SECOND_WEAPON.m_id);
        }
        if (fieldName.equals("accessoryEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
        }
        if (fieldName.equals("petEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.PET.m_id);
        }
        if (fieldName.equals("mountEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.MOUNT.m_id);
        }
        if (fieldName.equals("costumeEquipment")) {
            return ((ArrayInventoryWithoutCheck<Object, R>)this.m_equipmentInventory).getFromPosition(EquipmentPosition.COSTUME.m_id);
        }
        if (fieldName.equals("weaponSkills")) {
            final Iterator<Skill> abstractSkills = this.m_skillInventory.iterator();
            final ArrayList<Skill> skills = new ArrayList<Skill>();
            while (abstractSkills.hasNext()) {
                final Skill skill = abstractSkills.next();
                if (skill.getReferenceSkill().getType() == SkillType.WEAPON_SKILL) {
                    skills.add(skill);
                }
            }
            return skills;
        }
        if (fieldName.equals("craft")) {
            return CraftDisplayer.INSTANCE.isEmpty() ? null : CraftDisplayer.INSTANCE;
        }
        if (fieldName.equals("temporaryTransferInventory")) {
            return this.m_temporaryInventoryControler;
        }
        if (fieldName.equals("availableTitles")) {
            return this.computeAvailableTitles();
        }
        if (fieldName.equals("currentLevelPercentage")) {
            return this.getCurrentLevelPercentage();
        }
        if (fieldName.equals("xpRatio")) {
            if (this.getLevel() >= XpConstants.getPlayerCharacterLevelCap()) {
                return WakfuTranslator.getInstance().getString("maxLevel");
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(WakfuTranslator.getInstance().getString("xpRatio", WakfuTranslator.getInstance().formatNumber(this.getXpTable().getXpInLevel(this.getCurrentXp())), WakfuTranslator.getInstance().formatNumber(this.getXpTable().getLevelExtent(this.getLevel()))));
            return sb.toString();
        }
        else {
            if (fieldName.equals("wakfuGauge")) {
                return 1.0f - (this.getWakfuGaugeValue() + 1.0f) / 2.0f;
            }
            if (fieldName.equals("wakfuGaugePopupText")) {
                final float percentage = this.getWakfuGaugeValue() * 100.0f;
                final String absValue = Math.abs((int)percentage) + "% ";
                final TextWidgetFormater f = new TextWidgetFormater();
                f.append(WakfuTranslator.getInstance().getString("wakfu.gaugePopup"));
                if (percentage > 0.0f) {
                    f.append("\n").addColor("28d2c4").append(absValue).append(WakfuTranslator.getInstance().getString("WAKFU")).closeText();
                }
                else {
                    if (percentage >= 0.0f) {
                        return null;
                    }
                    f.append("\n").addColor("ab00ff").append(absValue).append(WakfuTranslator.getInstance().getString("STASIS")).closeText();
                }
                return f.finishAndToString();
            }
            if (fieldName.equals("wakfuGaugeColor")) {
                final float percentage = this.getWakfuGaugeValue();
                final float absValue2 = Math.abs(percentage);
                final float[] color = (percentage < 0.0f) ? WakfuClientConstants.STASIS_COLOR_FLOAT_ARRAY : WakfuClientConstants.WAKFU_COLOR_FLOAT_ARRAY;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(MathHelper.lerp(1.0f, color[0], absValue2)).append(",");
                sb2.append(MathHelper.lerp(1.0f, color[1], absValue2)).append(",");
                sb2.append(MathHelper.lerp(1.0f, color[2], absValue2));
                return sb2.toString();
            }
            if (fieldName.equals("wakfuGaugeIconStyle")) {
                final float gauge = this.getWakfuGaugeValue();
                return (gauge < 0.0f) ? "StasisIcon" : ((gauge > 0.0f) ? "wakfuIcon" : "");
            }
            if (fieldName.equals("aptitudes")) {
                return this.m_aptitudeInventory;
            }
            if (fieldName.equals("commonAptitudes")) {
                return this.m_aptitudeInventory.getCommonAptitudes();
            }
            if (fieldName.equals("breedAptitudes")) {
                return this.m_aptitudeInventory.getBreedAptitudes();
            }
            if (fieldName.equals("availableAptitudePoints")) {
                return this.m_aptitudeInventory.getAvailablePoints(AptitudeType.SPELL) - AptitudeDisplayerImpl.getInstance().getTotalAddedPoints(AptitudeType.SPELL);
            }
            if (fieldName.equals("hasAptitudePoints")) {
                return this.hasAptitudePoints();
            }
            if (fieldName.equals("availableCommonPoints")) {
                return this.m_aptitudeInventory.getAvailablePoints(AptitudeType.COMMON) - AptitudeDisplayerImpl.getInstance().getTotalAddedPoints(AptitudeType.COMMON);
            }
            if (fieldName.equals("nation")) {
                return ((ClientCitizenComportment)this.getCitizenComportment()).getView();
            }
            if (fieldName.equals("isInSubscriberZone")) {
                return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(this);
            }
            if (fieldName.equals("canLockSpell")) {
                return SpellXpComputer.getPlayerSpellLockValidity(this) == SpellLockValidity.VALID;
            }
            if (fieldName.equals("hasMissingSpellXp")) {
                return SpellXpComputer.getPlayerSpellLockValidity(this) == SpellLockValidity.CAP_REACHED_BUT_REMAINING_XP;
            }
            if (fieldName.equals("lockedSpell")) {
                return this.getSpellInventory().getFirstWithReferenceId(this.m_lockedSpellId);
            }
            if (fieldName.equals("companionList")) {
                return this.getCompanionViews();
            }
            if (fieldName.equals("actorStandardScale")) {
                return 1.5f;
            }
            return super.getFieldValue(fieldName);
        }
    }
    
    private ArrayList<PlayerTitle> computeAvailableTitles() {
        final ArrayList<PlayerTitle> res = new ArrayList<PlayerTitle>();
        for (final PlayerTitle title : this.m_availableTitles) {
            if (SubscriptionEmoteAndTitleLimitations.isAuthorizedTitle(this.getAccountInformationHandler().getActiveSubscriptionLevel(), title.getId())) {
                res.add(title);
            }
        }
        return res;
    }
    
    public ShortCharacterView[] getCompanionViews() {
        final int nbViews = (this.m_partyComportment.getParty() != null) ? (this.m_partyComportment.getParty().getMemberCount() + 1) : 1;
        final ShortCharacterView[] shortCharacterViews = new ShortCharacterView[nbViews];
        shortCharacterViews[0] = new PlayerCompanionViewShort(this.getId(), this.getName(), this.getBreedId(), this.getSex());
        if (!this.m_partyComportment.isInParty()) {
            return shortCharacterViews;
        }
        final TLongObjectHashMap<PartyMemberInterface> members = this.m_partyComportment.getParty().getMembers();
        int i = 1;
        final TLongObjectIterator<PartyMemberInterface> it = members.iterator();
        while (it.hasNext()) {
            it.advance();
            final PartyMemberInterface member = it.value();
            if (member instanceof CompanionPartyMemberModel) {
                final CompanionModel companionModel = ((CompanionPartyMemberModel)member).getCompanionModel();
                final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
                if (CompanionManager.INSTANCE.getCompanion(accountId, companionModel.getId()) == null) {
                    continue;
                }
                shortCharacterViews[i++] = new CompanionViewShort(companionModel);
            }
        }
        return shortCharacterViews;
    }
    
    private boolean hasAptitudePoints() {
        return this.m_aptitudeInventory.getAvailablePoints(AptitudeType.SPELL) - AptitudeDisplayerImpl.getInstance().getTotalAddedPoints(AptitudeType.SPELL) > 0 || AptitudesView.INSTANCE.hasAvailablePoints();
    }
    
    @Override
    protected void computeStatesField() {
        if (this.m_statesCache == null || this.m_protectorStatesCache == null) {
            this.m_statesCache = new ArrayList<RunningEffectFieldProvider>();
            this.m_protectorStatesCache = new ArrayList<RunningEffectFieldProvider>();
            this.m_allStatesCache = new ArrayList<RunningEffectFieldProvider>();
            for (final RunningEffect re : this.m_runningEffectManager) {
                if (this.canDisplayEffectInStateBar(re)) {
                    long remaining = -1L;
                    switch (re.getEffectContainer().getContainerType()) {
                        case 18: {
                            if (!this.isOnFight()) {
                                for (final ZoneBuffInstance buffInstance : this.getActiveZoneBuffs()) {
                                    if (buffInstance.getBuffId() == re.getEffectContainer().getEffectContainerId()) {
                                        final int remainingTime = buffInstance.getRemainingTime();
                                        remaining = ((remainingTime != -1) ? (remainingTime / 1000) : -1L);
                                        break;
                                    }
                                }
                                break;
                            }
                            break;
                        }
                        case 1: {
                            if (re.getContext().getContextType() != 1) {
                                final int stateId = re.getEffectContainer().getStateBaseId();
                                final StateRunningEffect linkedStateEffect = this.m_runningEffectManager.getRunningState(stateId);
                                if (linkedStateEffect != null) {
                                    remaining = this.m_runningEffectManager.getRemainingSec(linkedStateEffect.getUniqueId());
                                }
                                break;
                            }
                            break;
                        }
                        default: {
                            remaining = this.m_runningEffectManager.getRemainingSec(re.getUniqueId());
                            break;
                        }
                    }
                    final RunningEffectFieldProvider ref = this.getRunningEffectFieldProvider().getRunningEffectProvider(re, remaining);
                    if (ref == null) {
                        continue;
                    }
                    switch (re.getEffectContainer().getContainerType()) {
                        case 19:
                        case 33: {
                            if (!this.m_protectorStatesCache.contains(ref)) {
                                this.m_protectorStatesCache.add(ref);
                                break;
                            }
                            break;
                        }
                        default: {
                            if (!this.m_statesCache.contains(ref)) {
                                this.m_statesCache.add(ref);
                                break;
                            }
                            break;
                        }
                    }
                    if (this.m_allStatesCache.contains(ref)) {
                        continue;
                    }
                    this.m_allStatesCache.add(ref);
                }
            }
            this.sortStates();
        }
    }
    
    @Override
    public String[] getFields() {
        return LocalPlayerCharacter.LOCAL_ALL_FIELDS;
    }
    
    @Nullable
    @Override
    public ShortcutInventory getShortcutInventory(final ShortCutBarType shortcutType, final byte shortcurBarNumber) {
        ShortcutBar bar = null;
        try {
            bar = this.m_shortcutBarManager.getBar(shortcutType, shortcurBarNumber);
            return bar.getShortCutItems();
        }
        catch (Exception e) {
            LocalPlayerCharacter.m_logger.error((Object)("Exception while trying to het shortcutbar " + shortcutType + " / " + shortcurBarNumber), (Throwable)e);
            return null;
        }
    }
    
    public ShortcutBarManager getShortcutBarManager() {
        return this.m_shortcutBarManager;
    }
    
    @Override
    public CraftHandler getCraftHandler() {
        return this.m_craftHandler;
    }
    
    @Override
    public ClientEmoteHandler getEmoteHandler() {
        return this.m_emoteHandler;
    }
    
    public TravelHandler getTravelHandler() {
        return this.m_travelHandler;
    }
    
    public RespawnPointHandler getRespawnPointHandler() {
        return this.m_respawnPointHandler;
    }
    
    public void updateShortcutBars() {
        this.m_shortcutBarManager.updateShortcutBars();
    }
    
    @Override
    public void createSpellInventoryFromRaw(final RawSpellLevelInventory rawSpellLevelInventory) {
    }
    
    public short getVirtualLevel() {
        final SpellInventory<SpellLevel> spellInventory = this.m_spellInventoryManager.getSpellInventory();
        if (spellInventory.isEmpty()) {
            return 0;
        }
        int level = 0;
        for (final SpellLevel spell : spellInventory) {
            level += spell.getLevel();
        }
        return (short)(level / spellInventory.size());
    }
    
    public boolean isWaitingForResult() {
        return this.m_waitingForResult;
    }
    
    public boolean isCrafing() {
        if (this.getCurrentOccupation() == null) {
            return false;
        }
        switch (this.getCurrentOccupation().getOccupationTypeId()) {
            case 2:
            case 3:
            case 6:
            case 8: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public void setWaitingForResult(final boolean waitingForResult) {
        this.m_waitingForResult = waitingForResult;
    }
    
    public boolean canMoveAndInteract() {
        return this.m_canMoveAndInteract;
    }
    
    public void setCanMoveAndInteract(final boolean canMoveAndInteract) {
        this.m_canMoveAndInteract = canMoveAndInteract;
    }
    
    @Override
    public void setCurrentTitle(final short id) {
        if (this.m_availableTitles != null) {
            for (int i = this.m_availableTitles.size() - 1; i >= 0; --i) {
                final PlayerTitle title = this.m_availableTitles.get(i);
                if (title.getId() == id) {
                    this.setCurrentTitle(title);
                    return;
                }
            }
        }
        this.setCurrentTitle(new PlayerTitle(id));
    }
    
    public void setAvailableTitles(final short[] availableTitlesTab) {
        final int size = availableTitlesTab.length;
        if (this.m_availableTitles == null) {
            this.m_availableTitles = new ArrayList<PlayerTitle>(size + 1);
        }
        else {
            this.m_availableTitles.clear();
            this.m_availableTitles.ensureCapacity(size + 1);
        }
        this.m_availableTitles.add(new PlayerTitle((short)(-1)));
        for (int i = 0; i < size; ++i) {
            this.m_availableTitles.add(new PlayerTitle(availableTitlesTab[i]));
        }
        PlayerTitle toSet = null;
        final PlayerTitle currentTitle = this.getCurrentTitleObject();
        if (this.m_availableTitles != null && currentTitle != null) {
            for (int j = this.m_availableTitles.size() - 1; j >= 0; --j) {
                final PlayerTitle title = this.m_availableTitles.get(j);
                if (title.getId() == currentTitle.getId()) {
                    toSet = title;
                }
            }
        }
        this.setCurrentTitle(toSet);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "availableTitles");
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        super.onCharacteristicUpdated(charac);
        this.executeAfterAllUpdate("characUpdate", new Runnable() {
            @Override
            public void run() {
                LocalPlayerCharacter.this.resetEquipmentCache();
                LocalPlayerCharacter.this.resetEquipmentBonusCache();
                LocalPlayerCharacter.this.resetInventoryCache();
            }
        });
    }
    
    private void resetEquipmentCache() {
        for (final Item item : this.m_equipmentInventory) {
            item.resetCache();
        }
    }
    
    private void resetInventoryCache() {
        final TLongObjectIterator<AbstractBag> bagsIt = this.m_bags.getBagsIterator();
        while (bagsIt.hasNext()) {
            bagsIt.advance();
            for (final Item item : bagsIt.value()) {
                item.resetCache();
            }
        }
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        if (event.getInventory().equals(this.m_equipmentInventory)) {
            switch (event.getAction()) {
                case ITEM_ADDED:
                case ITEM_ADDED_AT: {
                    final InventoryItemModifiedEvent modEvent = (InventoryItemModifiedEvent)event;
                    if (modEvent.getConcernedItem() instanceof Item) {
                        final Item concernedItem = (Item)modEvent.getConcernedItem();
                        final int hpValue = this.getCharacteristicValue(FighterCharacteristicType.HP);
                        this.reloadItemEffects();
                        this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(hpValue);
                        this.m_shortcutBarManager.onItemAdded(concernedItem, modEvent);
                        concernedItem.resetCache();
                        if (modEvent.getPosition() == EquipmentPosition.COSTUME.getId()) {
                            this.m_cosmeticsInventoryView.refreshSelected();
                        }
                        WakfuSoundManager.getInstance().equipItem(EquipmentPosition.fromId((byte)modEvent.getPosition()));
                    }
                    break;
                }
                case ITEM_REMOVED:
                case ITEM_REMOVED_AT: {
                    final InventoryItemModifiedEvent modEvent = (InventoryItemModifiedEvent)event;
                    if (modEvent.getConcernedItem() instanceof Item) {
                        final Item concernedItem = (Item)modEvent.getConcernedItem();
                        final int hpValue = this.getCharacteristicValue(FighterCharacteristicType.HP);
                        if (concernedItem.getReferenceItem().getSetId() != 0) {
                            final ItemSet set = ItemSetManager.getInstance().getItemSet(concernedItem.getReferenceItem().getSetId());
                            this.unapplyItemOnEquipEffect(concernedItem, set);
                        }
                        else {
                            this.unapplyItemOnEquipEffect(concernedItem);
                        }
                        this.reloadItemEffects();
                        this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(hpValue);
                        this.m_shortcutBarManager.onItemRemoved(concernedItem);
                        if (modEvent.getPosition() == EquipmentPosition.COSTUME.getId()) {
                            this.m_cosmeticsInventoryView.refreshSelected();
                        }
                        this.m_replaceFlag = false;
                        concernedItem.resetCache();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public AbstractMRUAction[] getMRUActions() {
        final AnimatedInteractiveElement interactiveElement = this.getCurrentInteractiveElement();
        if (interactiveElement != null && interactiveElement instanceof WakfuClientInteractiveAnimatedElementSceneView) {
            final WakfuClientInteractiveAnimatedElementSceneView ie = (WakfuClientInteractiveAnimatedElementSceneView)interactiveElement;
            final MRUable mruable = (MRUable)ie.getInteractiveElement();
            return mruable.getMRUActions();
        }
        return AbstractMRUAction.EMPTY_ARRAY;
    }
    
    @Override
    public ClientBagContainer getBags() {
        return this.m_bags;
    }
    
    @Override
    public AptitudeInventory getAptitudeInventory() {
        return this.m_aptitudeInventory;
    }
    
    @Override
    public AptitudeBonusInventory getAptitudeBonusInventory() {
        return this.m_aptitudeBonusInventory;
    }
    
    public void reloadAptitudeEffects(final EffectContext context) {
        super.reloadAptitudeEffects(context);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "aptitudes", "breedAptitudes", "commonAptitudes", "hasAptitudePoints", "availableAptitudePoints", "availableCommonPoints");
    }
    
    @Override
    public void reloadProtectorBuffs() {
        super.reloadProtectorBuffs();
        this.updateStateDisplay(true);
    }
    
    @Override
    public void reloadHavenWorldBuffs() {
        super.reloadHavenWorldBuffs();
        ClientEffectApplier.INSTANCE.reloadHavenWorldEffects(this);
        this.updateStateDisplay(true);
    }
    
    public TIntHashSet getHavenWorldEffects() {
        return this.m_havenWorldEffects;
    }
    
    @Override
    public void reloadGuildBuffs() {
        ClientEffectApplier.INSTANCE.reloadGuildEffects(this);
        this.updateStateDisplay(true);
    }
    
    @Override
    public void reloadSubscriptionState() {
        try {
            ClientEffectApplier.INSTANCE.reloadSubscriptionState(this);
            this.updateStateDisplay(true);
        }
        catch (Exception e) {
            LocalPlayerCharacter.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    @Override
    public void reloadAntiAddictionBuffs() {
        ClientEffectApplier.INSTANCE.reloadAntiAddictionEffects(this);
        this.updateStateDisplay(true);
    }
    
    public TIntHashSet getGuildEffects() {
        return this.m_guildEffects;
    }
    
    public TIntHashSet getAntiAddictionEffects() {
        return this.m_antiAddictionEffects;
    }
    
    public AntiAddictionDataHandler getAntiAddictionDataHandler() {
        return this.m_antiAddictionDataHandler;
    }
    
    public boolean isTemporaryTransferInventoryActive() {
        final TLongIterator it = HeroesManager.INSTANCE.getHeroesInParty(this.m_ownerId).iterator();
        while (it.hasNext()) {
            final TemporaryInventory inventory = (TemporaryInventory)HeroesManager.INSTANCE.getHero(it.next()).getInventory(InventoryType.TEMPORARY_INVENTORY);
            if (!inventory.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public DimensionalBagView getVisitingDimentionalBag() {
        return this.m_visitingDimentionalBag;
    }
    
    public void setVisitingDimentionalBag(final DimensionalBagView visitingDimentionalBag) {
        this.m_visitingDimentionalBag = visitingDimentionalBag;
    }
    
    public DimensionalBagView getOwnedDimensionalBag() {
        return this.m_ownedDimensionalBag;
    }
    
    @Override
    public int getKamasCount() {
        return this.m_ownedDimensionalBag.getWallet().getAmountOfCash();
    }
    
    @Override
    public int substractKamas(final int kamas) {
        final int kamaCount = this.m_ownedDimensionalBag.getWallet().getAmountOfCash();
        this.m_ownedDimensionalBag.getWallet().substractAmount(kamas);
        return kamaCount - this.m_ownedDimensionalBag.getWallet().getAmountOfCash();
    }
    
    @Override
    public int addKamas(final int kamas) {
        final int kamaCount = this.m_ownedDimensionalBag.getWallet().getAmountOfCash();
        this.m_ownedDimensionalBag.getWallet().addAmount(kamas);
        return kamaCount - this.m_ownedDimensionalBag.getWallet().getAmountOfCash();
    }
    
    @Override
    public void addSkillXp(final int skillId, final long xpGained, final boolean levelGained) {
        super.addSkillXp(skillId, xpGained, levelGained);
        final AbstractSkill<ReferenceSkill> skill = this.m_skillInventory.getFirstWithReferenceId(skillId);
        if (skill == null) {
            return;
        }
        final XpModification xpModification = skill.addXp(xpGained);
        final long nextIn = skill.getXpTable().getXpByLevel(skill.getActualLevel() + 1) - skill.getXp();
        final ReferenceSkill skillRef = skill.getReferenceSkill();
        if (!levelGained || skillRef.getType().getTypeId() == SkillType.WEAPON_SKILL.getTypeId()) {
            return;
        }
    }
    
    @Override
    public void addSpellXp(final int spellId, final long xpGained, final boolean levelGained) {
        final SpellLevel level = this.getSpellInventory().getFirstWithReferenceId(spellId);
        if (level == null) {
            return;
        }
        super.addSpellXp(spellId, xpGained, levelGained);
        final XpModification xpModification = level.addXp(xpGained);
        final long nextIn = level.getXpTable().getLevelExtent(level.getLevel()) - level.getXpTable().getXpInLevel(level.getXp());
        final String translationKey = (xpGained > 0L) ? "infoPop.xpGain" : "infoPop.xpLoss";
        final String message = WakfuTranslator.getInstance().getString(translationKey, level.getSpell().getName(), xpGained, nextIn, ChatConstants.CHAT_FIGHT_EFFECT_COLOR, xpModification.getLevelDifference());
        LocalPlayerCharacter.m_fightLogger.info(message);
        this.updateElementMastery(Elements.getElementFromId(level.getSpell().getElementId()));
        if (levelGained) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_spellInventoryManager, this.m_spellInventoryManager.getFields());
        }
        if (!levelGained || !this.isOnFight()) {
            return;
        }
    }
    
    @Override
    public void addCharacterXp(final long xpDifference, final short levelDifference) {
        this.addXp(xpDifference);
        final long nextIn = this.getXpTable().getXpByLevel(this.getLevel() + 1) - this.getCurrentXp();
        final String message = WakfuTranslator.getInstance().getString("infoPop.xpGain", this.getName(), xpDifference, nextIn, ChatConstants.CHAT_FIGHT_EFFECT_COLOR, levelDifference);
        LocalPlayerCharacter.m_fightLogger.info(message);
        super.addCharacterXp(xpDifference, levelDifference);
    }
    
    @Override
    public void addFightCharacteristicsListeners() {
    }
    
    @Override
    public void removeFightCharacteristicsListeners() {
    }
    
    @Override
    public void onJoinFight(final BasicFight basicFight) {
        this.m_shortcutBarManager.sendShortcutsUpdateMessageIfNeeded();
        super.onJoinFight(basicFight);
        final short breedId = this.getBreedId();
        if (breedId == AvatarBreed.ECAFLIP.getBreedId()) {
            EcaflipFightListenerManager.INSTANCE.registerCharacter(this);
        }
        else if (breedId == AvatarBreed.PANDAWA.getBreedId()) {
            PandawaFightListenerManager.INSTANCE.registerCharacter(this);
        }
        else if (breedId == AvatarBreed.ROUBLARD.getBreedId()) {
            RoublardFightListenerManager.INSTANCE.registerCharacter(this);
        }
        else if (breedId == AvatarBreed.XELOR.getBreedId()) {
            XelorFightListenerManager.INSTANCE.registerCharacter(this);
        }
        if (this.isInSpectatorMode()) {
            new LeaveSpectatorModeProcedure().execute();
        }
        FightVisibilityManager.getInstance().setParticipatingFight(basicFight.getId());
        FightVisibilityManager.getInstance().updateFightVisibility();
        if (this.m_activateUIArcadeDungeonFrame && basicFight instanceof Fight) {
            UIArcadeDungeonFrame.getInstance().activate((Fight)basicFight);
        }
        this.m_fightChallenges.init();
    }
    
    @Override
    public void onLeaveFight() {
        if (this.getCurrentFight() == null || this.getCurrentFight().getModel().decreaseRent()) {
            RentUpdater.updateType(1, this);
        }
        super.onLeaveFight();
        final AvatarBreed breed = this.getBreed();
        if (breed == AvatarBreed.OSAMODAS) {
            final Symbiot symbiot = this.getSymbiot();
            if (symbiot != null) {
                symbiot.resetFightData();
            }
        }
        else if (breed == AvatarBreed.PANDAWA) {
            PandawaFightListenerManager.INSTANCE.unregisterCharacter(this);
        }
        else if (breed == AvatarBreed.ECAFLIP) {
            EcaflipFightListenerManager.INSTANCE.unregisterCharacter(this);
        }
        else if (breed == AvatarBreed.ROUBLARD) {
            RoublardFightListenerManager.INSTANCE.unregisterCharacter(this);
        }
        else if (breed == AvatarBreed.XELOR) {
            XelorFightListenerManager.INSTANCE.unregisterCharacter(this);
        }
        this.m_fightChallenges.clear();
        this.updateStateDisplay(true);
    }
    
    public void onXpModification(final Levelable object, final short levelDiff, final long xpDiff) {
    }
    
    public void emptyEquipment() {
        for (final Item item : this.m_equipmentInventory) {
            ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipmentInventory).remove(item);
        }
    }
    
    public void emptyBags() {
        final TLongObjectIterator<AbstractBag> it = this.m_bags.getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().removeAll();
        }
    }
    
    public void releaseSkillInfo() {
        final AnimatedInteractiveElement element = this.getCurrentInteractiveElement();
        if (element != null) {
            this.setCurrentInteractiveElement(null);
        }
    }
    
    private boolean checkForMovement() {
        if (this.isTemporaryTransferInventoryActive()) {
            UITemporaryInventoryFrame.getInstance().askForTemporaryInventoryDestruction();
            return false;
        }
        if (this.isInSpectatorMode()) {
            this.m_spectatorModeMovementRequestHandler.movementRequest();
            return false;
        }
        return true;
    }
    
    @Override
    protected void setCurrentFight(final Fight fight) {
        super.setCurrentFight(fight);
        TacticalViewManager.getInstance().setFight(fight);
        AchievementUIHelper.displayFollowedAchievements();
        if (WakfuGameEntity.getInstance().hasFrame(UICharacterSheetFrame.getInstance())) {
            if (fight != null) {
                UICharacterSheetFrame.getInstance().onFight(fight);
            }
            else {
                UICharacterSheetFrame.getInstance().onLeaveFight();
            }
        }
    }
    
    public void setObservedFight(final Fight observedFight) {
        this.m_observedFight = observedFight;
        TacticalViewManager.getInstance().setFight(observedFight);
    }
    
    public Fight getObservedFight() {
        return this.m_observedFight;
    }
    
    public boolean isInSpectatorMode() {
        return this.m_observedFight != null;
    }
    
    public boolean moveNearTarget(final Target t, final boolean useDiagonal, final boolean stopOnAxisCell) {
        return this.checkForMovement() && this.getActor().moveNearTarget(t, useDiagonal, stopOnAxisCell);
    }
    
    public boolean moveTo(final Point3 point, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        return this.checkForMovement() && this.getActor().moveTo(point, stopBeforeEndCell, useDiagonal);
    }
    
    public boolean moveTo(final boolean stopBeforeEndCell, final boolean useDiagonal, final List<Point3> destinations) {
        return this.checkForMovement() && this.getActor().moveTo(stopBeforeEndCell, useDiagonal, destinations);
    }
    
    public boolean moveTo(final int x, final int y, final short z, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        PvpInteractionManager.INSTANCE.cancelInteraction();
        return this.checkForMovement() && this.getActor().moveTo(x, y, z, stopBeforeEndCell, useDiagonal);
    }
    
    public boolean applyPathResult(final PathFindResult path, final boolean stop) {
        return this.checkForMovement() && this.getActor().applyPathResult(path, stop);
    }
    
    public ActionInProgress getActionInProgress() {
        return this.m_actionInProgress;
    }
    
    @Override
    public void onWalletUpdated(final Wallet wallet, final int delta) {
        if (delta != 0) {
            final String key = (delta < 0) ? "kama.loss" : "kama.gain";
            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString(key, Math.abs(delta)));
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
        final int cash = wallet.getAmountOfCash();
        PropertiesProvider.getInstance().setPropertyValue("kamas", cash);
        PropertiesProvider.getInstance().setPropertyValue("formatedKamas", WakfuTranslator.getInstance().formatNumber(cash));
    }
    
    @Override
    public boolean hasChallenge(final int challengeId) {
        final ChallengeData challengeData = ChallengeManager.getInstance().getChallengeData(challengeId);
        return challengeData != null;
    }
    
    @Override
    public long getCurrentXp() {
        return this.m_xp.getCurrentXp();
    }
    
    @Override
    public short getLevel() {
        return this.m_xp.getLevel();
    }
    
    @Override
    public XpTable getXpTable() {
        return this.m_xp.getXpTable();
    }
    
    @Override
    public float getCurrentLevelPercentage() {
        return this.m_xp.getCurrentLevelPercentage();
    }
    
    @Override
    public XpModification setXp(final long xp) {
        return this.m_xp.setXp(xp);
    }
    
    @Override
    public XpModification addXp(final long xp) {
        final XpModification actualModification = this.m_xp.addXp(xp);
        this.onPlayerXpModification(actualModification);
        return actualModification;
    }
    
    void onPlayerXpModification(final XpModification actualModification) {
        if (actualModification.doesLevelUp()) {
            final short levelDiff = actualModification.getLevelDifference();
            UIControlCenterContainerFrame.getInstance().highLightCharacterInformationButton();
            final AptitudeInventory aptitudeInventory = this.m_aptitudeInventory;
            final int gain = actualModification.getLevelDifference() * 5;
            aptitudeInventory.setAvailablePoints(AptitudeType.COMMON, aptitudeInventory.getAvailablePoints(AptitudeType.COMMON) + gain);
            aptitudeInventory.setAvailablePoints(AptitudeType.SPELL, aptitudeInventory.getAvailablePoints(AptitudeType.SPELL) + gain);
            final AptitudeBonusInventoryController controller = new AptitudeBonusInventoryController(this.m_aptitudeBonusInventory);
            controller.givePointsForLevelUp(actualModification, this.getLevel());
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "availableCommonPoints", "availableAptitudePoints", "hasAptitudePoints");
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_spellInventoryManager, this.m_spellInventoryManager.getFields());
            if (this.m_breed instanceof AvatarBreed && !this.isOnFight()) {
                ((AvatarBreed)this.m_breed).getSecondaryCharacsCalculator().applyForLevelUp(this.getCharacteristics(), (short)(this.getLevel() - levelDiff), this.getLevel());
                this.updateCharacBoundWithLevel();
            }
            AreaChallengeInformation.getInstance().updateCurrentChallengeInUI();
        }
        final long previousXP = this.m_xp.getCurrentXp() - actualModification.getXpDifference();
        final int previousLevel = this.m_xp.getLevel() - actualModification.getLevelDifference();
        final float previousPercentage = this.getXpTable().getPercentageInLevel((short)previousLevel, previousXP);
        UIXPGainFrame.getInstance().onXPGain(actualModification, previousPercentage, this.m_xp.getCurrentLevelPercentage());
        this.updateXpFields();
    }
    
    @Override
    public XpModification setPlayerCharacterLevel(final short level) {
        final XpModification actualModification = this.m_xp.setPlayerCharacterLevel(level);
        this.onPlayerXpModification(actualModification);
        return actualModification;
    }
    
    @NotNull
    public PartyComportment getPartyComportment() {
        return this.m_partyComportment;
    }
    
    public long getPartyId() {
        return this.m_partyComportment.getPartyId();
    }
    
    @Override
    public long getClientId() {
        return WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
    }
    
    @Override
    public boolean addAvailableTitle(final int titleId) {
        super.addAvailableTitle(titleId);
        boolean alreadyPresent = false;
        for (int i = 0; i < this.m_availableTitles.size(); ++i) {
            if (this.m_availableTitles.get(i).getId() == titleId) {
                alreadyPresent = true;
                break;
            }
        }
        if (!alreadyPresent) {
            this.m_availableTitles.add(new PlayerTitle((short)titleId));
        }
        return !alreadyPresent;
    }
    
    @Override
    public void setWakfuGauge(final int wakfuGauge) {
        super.setWakfuGauge(wakfuGauge);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "wakfuGauge", "wakfuGaugePopupText", "wakfuGaugeIconStyle", "wakfuGaugeColor");
    }
    
    @Override
    public Collection<ZoneBuffInstance> getActiveZoneBuffs() {
        return this.m_zoneBuffs.getActiveBuffs();
    }
    
    public LocalPlayerZoneBuffs getZoneBuffManager() {
        return this.m_zoneBuffs;
    }
    
    @Override
    public void onCitizenScoreChanged(final int nationId, final CitizenComportment comportment, final CitizenRank oldRank, final int newPoints, final int deltaScore) {
        super.onCitizenScoreChanged(nationId, comportment, oldRank, newPoints, deltaScore);
        final NationRank rank = this.getCitizenComportment().getRank();
        if (this.m_initialized && rank != null && newPoints < rank.getCitizenScoreLine()) {
            final String title = WakfuTranslator.getInstance().getString("notification.nationRankLostTitle");
            final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.nationRankLostText", WakfuTranslator.getInstance().getString(57, (int)rank.getId(), new Object[0])), NotificationMessageType.NATION);
            final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
            Worker.getInstance().pushMessage(uiNotificationMessage);
        }
        if (this.getCurrentFight() == null) {
            this.reloadItemEffects();
        }
        final CitizenRank newRank = CitizenRankManager.getInstance().getRankFromCitizenScore(newPoints);
        if (this.m_initialized && oldRank != newRank) {
            final String title2 = WakfuTranslator.getInstance().getString("notification.citizenRankTitle");
            final String text2 = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.citizenRankText", WakfuTranslator.getInstance().getString(newRank.getTranslationKey())), NotificationMessageType.CITIZEN);
            final UINotificationMessage uiNotificationMessage2 = new UINotificationMessage(title2, text2, NotificationMessageType.CITIZEN);
            Worker.getInstance().pushMessage(uiNotificationMessage2);
        }
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventCitizenRankChanged(newRank, newPoints));
    }
    
    public void onTerritoryChanged(final Territory territory) {
        this.m_currentTerritory = territory;
        if (territory != null && territory.getProtector() != null) {
            final ProtectorWelcome welcomeEvent = (ProtectorWelcome)ProtectorEvents.PROTECTOR_WELCOME.create();
            welcomeEvent.setProtector((Protector)territory.getProtector());
            ProtectorEventDispatcher.INSTANCE.dispatch(welcomeEvent);
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventTerritoryEntrance(territory.getId()));
        }
        else {
            UIMessage.send((short)16002);
            AreaChallengeInformation.getInstance().setChallengeInZone(null);
            AreaChallengeInformation.getInstance().updateProperties();
            WakfuMonsterZoneManager.getInstance().clear();
            WakfuResourceZoneManager.getInstance().clear();
        }
        MapManagerHelper.loadMap();
    }
    
    public Territory getCurrentTerritory() {
        return this.m_currentTerritory;
    }
    
    @Override
    public void setInstanceId(final short instanceId) {
        this.m_previousWorldId = this.getInstanceId();
        super.setInstanceId(instanceId);
    }
    
    @Override
    public short getPreviousWorldId() {
        return this.m_previousWorldId;
    }
    
    @Override
    public void onControlledNPCDeath() {
        super.onControlledNPCDeath();
        SymbiotView.getInstance().updateLeaderShipCapacity();
        this.m_shortcutBarManager.updateShorctutBarUsability();
    }
    
    @Override
    public void setCustomHpRegen(final int hpRegen) {
        this.getHpRegenHandler().setCustomRegen(hpRegen);
    }
    
    @Override
    public Wallet getWallet() {
        return this.m_ownedDimensionalBag.getWallet();
    }
    
    @Override
    public boolean canStockItem(final Item item) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public AbstractBag stockItem(final Item item) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public AbstractPartition getTransactionLocalisation() {
        return LocalPartitionManager.getInstance().getCurrentPartition();
    }
    
    @Override
    public boolean hasAGroup(final GroupType groupType) {
        switch (groupType) {
            case PARTY: {
                return this.getPartyId() > 0L;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public long getGroupId(final GroupType groupType) {
        switch (groupType) {
            case PARTY: {
                return this.m_partyComportment.getPartyId();
            }
            default: {
                return 0L;
            }
        }
    }
    
    @Override
    public boolean hasAGroup() {
        return this.m_partyComportment.getPartyId() > 0L || this.getGuildId() > 0L;
    }
    
    @Override
    public boolean uncarryTo_effect(final Point3 pos) {
        final boolean res = super.uncarryTo_effect(pos);
        this.updateShortcutBars();
        return res;
    }
    
    public ClientMapHandler getMapHandler() {
        return this.m_mapHandler;
    }
    
    public PersonalSpaceHandler getPersonalSpaceHandler() {
        return this.m_personalSpaceHandler;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_shortcutBarManager.release();
        if (this.m_ownedDimensionalBag != null) {
            this.m_ownedDimensionalBag.release();
            this.m_ownedDimensionalBag = null;
        }
        TimeManager.INSTANCE.removeListener(this.getRunningEffectFieldProvider());
        this.m_lockedSpellId = 0;
    }
    
    @Override
    public void onPropertyUpdated(final PropertyType prop) {
        if (prop == null) {
            return;
        }
        super.onPropertyUpdated(prop);
        if (prop.getPropertyTypeId() == 0) {
            switch ((WorldPropertyType)prop) {
                case CHAT_UI_INTERACTION_DISABLED: {
                    PropertiesProvider.getInstance().setPropertyValue("chat.enableInteractions", !this.hasProperty(WorldPropertyType.CHAT_UI_INTERACTION_DISABLED));
                    break;
                }
                case FOLLOW_ACHIEVEMENT_UI_FORCE_OPENED: {
                    final boolean forceOpen = this.hasProperty(WorldPropertyType.FOLLOW_ACHIEVEMENT_UI_FORCE_OPENED);
                    PropertiesProvider.getInstance().setPropertyValue("followedAchievements.forceOpen", forceOpen);
                    AchievementUIHelper.displayFollowedAchievements();
                    break;
                }
                case CALL_HELP_DISABLED: {
                    PropertiesProvider.getInstance().setPropertyValue("callHelpDisabled", this.isActiveProperty(prop));
                    break;
                }
                case FIGHT_LOCK_DISABLED: {
                    PropertiesProvider.getInstance().setPropertyValue("lockFightDisabled", this.isActiveProperty(prop));
                    break;
                }
                case CELL_REPORT_DISABLED: {
                    PropertiesProvider.getInstance().setPropertyValue("cellReportDisabled", this.isActiveProperty(prop));
                    break;
                }
                case HIDE_FIGHTERS_DISABLED: {
                    PropertiesProvider.getInstance().setPropertyValue("hideFightersDisabled", this.isActiveProperty(prop));
                    break;
                }
                case GIVE_UP_DISABLED: {
                    PropertiesProvider.getInstance().setPropertyValue("giveUpDisabled", this.isActiveProperty(prop));
                    break;
                }
            }
        }
    }
    
    @Override
    public void setCurrentOccupation(final BasicOccupation currentOccupation) {
        super.setCurrentOccupation(currentOccupation);
        PropertiesProvider.getInstance().setPropertyValue("hasOccupation", true);
    }
    
    @Override
    public boolean finishCurrentOccupation() {
        if (super.finishCurrentOccupation()) {
            PropertiesProvider.getInstance().setPropertyValue("hasOccupation", false);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean cancelCurrentOccupation(final boolean fromServer, final boolean sendMessage) {
        if (super.cancelCurrentOccupation(fromServer, sendMessage)) {
            PropertiesProvider.getInstance().setPropertyValue("hasOccupation", false);
            return true;
        }
        return false;
    }
    
    public PetCosmeticsInventoryView getPetCosmeticsInventory() {
        return this.m_petCosmeticsInventoryView;
    }
    
    static {
        m_fightLogger = new FightLogger();
        LOCAL_FIELDS = new String[] { "shortcutBarManager", "bags", "equipment", "equipmentBonus", "actorAnimationName", "headEquipment", "hairEquipment", "faceEquipment", "shoulderEquipment", "neckEquipment", "chestEquipment", "armsEquipment", "leftHandEquipment", "rightHandEquipment", "beltEquipment", "skirtEquipment", "trousersEquipment", "legsEquipment", "petEquipment", "mountEquipment", "backEquipment", "wingEquipment", "firstWeaponEquipment", "secondWeaponEquipment", "accessoryEquipment", "costumeEquipment", "weaponSkills", "kamas", "formatedKamas", "tokens", "temporaryTransferInventory", "availableTitles", "currentLevelPercentage", "xpRatio", "wakfuGauge", "wakfuGaugePopupText", "wakfuGaugeIconStyle", "isInSubscriberZone", "canLockSpell", "lockedSpell", "hasMissingSpellXp", "companionList", "cosmeticsInventory", "petCosmeticsInventory" };
        LOCAL_ALL_FIELDS = new String[LocalPlayerCharacter.LOCAL_FIELDS.length + PlayerCharacter.ALL_FIELDS.length];
        System.arraycopy(LocalPlayerCharacter.LOCAL_FIELDS, 0, LocalPlayerCharacter.LOCAL_ALL_FIELDS, 0, LocalPlayerCharacter.LOCAL_FIELDS.length);
        System.arraycopy(LocalPlayerCharacter.ALL_FIELDS, 0, LocalPlayerCharacter.LOCAL_ALL_FIELDS, LocalPlayerCharacter.LOCAL_FIELDS.length, PlayerCharacter.ALL_FIELDS.length);
        UPDATE_EQUIPMENT_PROPERTIES = new String[] { "actorAnimationName", "headEquipment", "hairEquipment", "faceEquipment", "shoulderEquipment", "neckEquipment", "chestEquipment", "armsEquipment", "leftHandEquipment", "rightHandEquipment", "beltEquipment", "skirtEquipment", "trousersEquipment", "legsEquipment", "petEquipment", "mountEquipment", "backEquipment", "wingEquipment", "firstWeaponEquipment", "secondWeaponEquipment", "accessoryEquipment", "costumeEquipment", "actorEquipment" };
    }
    
    private final class LocalPlayerCharacterPartDimensionalBagForClient extends CharacterInfoPart
    {
        private final CharacterSerializedDimensionalBagForClient m_part;
        
        private LocalPlayerCharacterPartDimensionalBagForClient(final CharacterSerializedDimensionalBagForClient part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Le sac dimensionnel ne devrait pas \u00eatre s\u00e9rialis\u00e9 par le client");
        }
        
        @Override
        public void onDataChanged() {
            if (LocalPlayerCharacter.this.m_ownedDimensionalBag != null) {
                LocalPlayerCharacter.this.m_ownedDimensionalBag.release();
            }
            LocalPlayerCharacter.this.m_ownedDimensionalBag = new DimensionalBagView();
            if (!LocalPlayerCharacter.this.m_ownedDimensionalBag.fromRaw(this.m_part.bag)) {
                LocalPlayerCharacter.m_logger.error((Object)"Erreur durant la r\u00e9cup\u00e9ration des donn\u00e9es du sac dimensionel du joueur.");
            }
        }
        
        @Override
        public String toString() {
            return "LocalPlayerCharacterPartDimensionalBagForClient{m_part=" + this.m_part + '}';
        }
    }
    
    private final class LocalPlayerCharacterPartShortcutInventories extends CharacterInfoPart
    {
        private final CharacterSerializedShortcutInventories m_part;
        
        private LocalPlayerCharacterPartShortcutInventories(final CharacterSerializedShortcutInventories part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Le client ne devrait pas s?rialiser les racourcis");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_shortcutBarManager.onDataChanged(this.m_part);
        }
    }
    
    private final class LocalPlayerCharacterPartEmoteInventory extends CharacterInfoPart
    {
        private final CharacterSerializedEmoteInventory m_part;
        
        private LocalPlayerCharacterPartEmoteInventory(final CharacterSerializedEmoteInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Le client ne devrait pas s\u00e9rialiser les emotes");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_emoteHandler.fromRaw(this.m_part);
        }
    }
    
    private final class LocalPlayerCharacterPartDimensionalBagViewsInventory extends CharacterInfoPart
    {
        private final CharacterSerializedDimensionalBagViewInventory m_part;
        
        private LocalPlayerCharacterPartDimensionalBagViewsInventory(final CharacterSerializedDimensionalBagViewInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Le client ne devrait pas s\u00e9rialiser les customs de havre-sac");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_personalSpaceHandler.fromRaw(this.m_part);
        }
    }
    
    private final class LocalPlayerCharacterPartInventories extends CharacterInfoPart
    {
        private final CharacterSerializedInventories m_part;
        
        private LocalPlayerCharacterPartInventories(final CharacterSerializedInventories part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de serialisation cliente");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_inventoryHandler.fromRaw(this.m_part.inventoryHandler);
        }
        
        @Override
        public String toString() {
            return "LocalPlayerCharacterPartInventories{m_part=" + this.m_part + "} ";
        }
    }
    
    private final class LocalPlayerCharacterPartGuildLocalInfo extends CharacterInfoPart
    {
        private final CharacterSerializedLocalGuildInfo m_part;
        
        private LocalPlayerCharacterPartGuildLocalInfo(final CharacterSerializedLocalGuildInfo part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de serialisation de part ici.");
        }
        
        @Override
        public void onDataChanged() {
            ((GuildLocalInformationHandler)LocalPlayerCharacter.this.getGuildHandler()).setSerializedGuild(this.m_part.guild);
            ((GuildLocalInformationHandler)LocalPlayerCharacter.this.getGuildHandler()).setHavenWorldId(this.m_part.havenWorldId);
            ((GuildLocalInformationHandler)LocalPlayerCharacter.this.getGuildHandler()).setModerationBonusLearningFactor(this.m_part.moderationBonusLearningFactor);
            LocalPlayerCharacter.this.reloadItemEffects();
        }
    }
    
    private final class LocalPlayerCharacterPartLandMarkInventory extends CharacterInfoPart
    {
        private final CharacterSerializedLandMarkInventory m_part;
        
        private LocalPlayerCharacterPartLandMarkInventory(final CharacterSerializedLandMarkInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Le client ne devrait pas s\u00e9rialiser les landMarks");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_mapHandler.fromRaw(this.m_part);
        }
    }
    
    private final class LocalPlayerCharacterPartDiscoveredItems extends CharacterInfoPart
    {
        private final CharacterSerializedDiscoveredItemsInventory m_part;
        
        private LocalPlayerCharacterPartDiscoveredItems(final CharacterSerializedDiscoveredItemsInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Le client ne devrait pas s\u00e9rialiser les items decouverts");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_travelHandler.fromRaw(this.m_part);
            LocalPlayerCharacter.this.m_respawnPointHandler.fromRaw(this.m_part);
        }
    }
    
    private final class LocalPlayerCharacterPartBreedSpecific extends CharacterInfoPart
    {
        private final CharacterSerializedBreedSpecific m_part;
        
        private LocalPlayerCharacterPartBreedSpecific(final CharacterSerializedBreedSpecific part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les breed specifics ne devraient pas ?tre s?rialis?s par le client.");
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.osaSpecific != null) {
                final Symbiot symbiot = new Symbiot();
                LocalPlayerCharacter.this.setSymbiot(symbiot);
                if (symbiot.fromRawSymbiot(this.m_part.osaSpecific.symbiot)) {
                    for (byte index = 0; index < symbiot.size(); ++index) {
                        final BasicInvocationCharacteristics charac = LocalPlayerCharacter.this.m_symbioticCharacter.getSymbiot().getCreatureParametersFromIndex(index);
                        if (charac != null && (charac.getName() == null || charac.getName().isEmpty())) {
                            final String name = WakfuTranslator.getInstance().getString(7, charac.getTypeId(), new Object[0]);
                            charac.setName(name);
                            final OsamodasSymbiotRenameCreatureMessage msg = new OsamodasSymbiotRenameCreatureMessage();
                            msg.setCreatureIndex(index);
                            msg.setCreatureName(name);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                        }
                    }
                    PropertiesProvider.getInstance().firePropertyValueChanged(SymbiotView.getInstance(), SymbiotView.FIELDS);
                    for (final CharacterInfoPropertyEventsHandler handler : LocalPlayerCharacter.this.m_characterInfoEventsHandler) {
                        handler.onSymbiotChanged(LocalPlayerCharacter.this);
                    }
                }
                else {
                    LocalPlayerCharacter.m_logger.error((Object)"Erreur lors de la r?cup?ration du symbiote d?s?rialis?");
                }
            }
        }
    }
    
    private final class LocalPlayerCharacterPartSpellInventory extends CharacterInfoPart
    {
        private final CharacterSerializedSpellInventory m_part;
        
        private LocalPlayerCharacterPartSpellInventory(final CharacterSerializedSpellInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les spells ne devraient pas ?tre s?rialis?s par le client.");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_spellInventoryManager = new SpellInventoryManager(LocalPlayerCharacter.this);
            LocalPlayerCharacter.this.m_spellInventoryManager.getSpellInventory().fromRaw(this.m_part.spellInventory);
            LocalPlayerCharacter.this.registerLockedSpellId(this.m_part.lockedSpellId);
        }
    }
    
    private final class LocalPlayerCharacterPartSkillInventory extends CharacterInfoPart
    {
        private final CharacterSerializedSkillInventory m_part;
        
        private LocalPlayerCharacterPartSkillInventory(final CharacterSerializedSkillInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les skills ne devraient pas ?tre s?rialis?s par le client.");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_skillInventory.fromRaw(this.m_part.skillInventory);
        }
    }
    
    private final class LocalPlayerCharacterPartCraft extends CharacterInfoPart
    {
        private final CharacterSerializedCraft m_part;
        
        private LocalPlayerCharacterPartCraft(final CharacterSerializedCraft part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de s\u00e9rialisation dans le client");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_craftHandler.fromRaw(this.m_part);
        }
    }
    
    private final class LocalPlayerCharacterPartBags extends CharacterInfoPart
    {
        private final CharacterSerializedBags m_part;
        
        private LocalPlayerCharacterPartBags(final CharacterSerializedBags part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les bags ne devraient pas \u00eatre s\u00e9rialis\u00e9s par le client.");
        }
        
        @Override
        public void onDataChanged() {
            for (final RawBagContainer.Bag bag : this.m_part.bagContainer.bags) {
                final RawBag rawBag = bag.bag;
                AbstractBag refBag = LocalPlayerCharacter.this.m_bags.get(rawBag.uniqueId);
                if (refBag == null) {
                    refBag = new Bag(0L, 0, BagInventoryContentChecker.INSTANCE, (short)0, LocalPlayerCharacter.this.m_bags);
                    if (!refBag.fromRaw(rawBag)) {
                        LocalPlayerCharacter.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration du bag uniqueId=" + rawBag.uniqueId + ", on ignore les bags restants"));
                        return;
                    }
                    LocalPlayerCharacter.this.m_bags.addContainer(refBag);
                    refBag.addObserver(ClientEventLocalPlayerInventoryListener.INSTANCE);
                }
                else {
                    refBag.destroyAll();
                    refBag.fromRaw(rawBag);
                }
            }
        }
    }
    
    private final class LocalPlayerCharacterPartEquipmentInventory extends CharacterInfoPart
    {
        private final CharacterSerializedEquipmentInventory m_part;
        
        private LocalPlayerCharacterPartEquipmentInventory(final CharacterSerializedEquipmentInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"L'\u00e9quipement ne devrait pas etre s\u00e9rialis\u00e9 par le client.");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_equipmentInventory.addObserver(LocalPlayerCharacter.this.getActor());
            LocalPlayerCharacter.this.beginRefreshDisplayEquipment();
            LocalPlayerCharacter.this.m_equipmentInventory.fromRaw(this.m_part.equipment);
            LocalPlayerCharacter.this.m_equipmentInventory.addObserver(LocalPlayerCharacter.this);
            LocalPlayerCharacter.this.m_equipmentInventory.addObserver(ClientEventLocalPlayerInventoryListener.INSTANCE);
            for (final Item item : LocalPlayerCharacter.this.m_equipmentInventory) {
                if (item.getReferenceItem().getItemType().getLinkedPositions() != null && item.isActive()) {
                    for (final EquipmentPosition pos : item.getReferenceItem().getItemType().getLinkedPositions()) {
                        final Item placeHolder = item.getInactiveCopy();
                        try {
                            ((ArrayInventoryWithoutCheck<Item, R>)LocalPlayerCharacter.this.m_equipmentInventory).addAt(placeHolder, pos.m_id);
                        }
                        catch (Exception e) {
                            LocalPlayerCharacter.m_logger.fatal((Object)"On a s\u00e9rialis\u00e9 un inventaire incoh\u00e9rent.");
                        }
                    }
                }
            }
            LocalPlayerCharacter.this.m_cosmeticsInventoryView.refreshSelected();
            LocalPlayerCharacter.this.m_shortcutBarManager.setLeftAndRightHand(((ArrayInventoryWithoutCheck<Item, R>)LocalPlayerCharacter.this.m_equipmentInventory).getFromPosition(EquipmentPosition.FIRST_WEAPON.m_id), ((ArrayInventoryWithoutCheck<Item, R>)LocalPlayerCharacter.this.m_equipmentInventory).getFromPosition(EquipmentPosition.SECOND_WEAPON.getId()));
            LocalPlayerCharacter.this.endRefreshDisplayEquipment();
        }
    }
    
    private static final class LocalPlayerCharacterPartChallenges extends CharacterInfoPart
    {
        private CharacterSerializedChallenges m_part;
        
        private LocalPlayerCharacterPartChallenges(final CharacterSerializedChallenges part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les challenges ne devraient pas ?tre s?rialis?s par le client.");
        }
        
        @Override
        public void onDataChanged() {
            ChallengeManager.getInstance().fromRawScenarioManager(this.m_part.challenges);
        }
    }
    
    private final class LocalPlayerCharacterPartTitle extends CharacterInfoPart
    {
        private final CharacterSerializedTitle m_part;
        
        private LocalPlayerCharacterPartTitle(final CharacterSerializedTitle part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
        }
        
        @Override
        public void onDataChanged() {
            final int size = this.m_part.availableTitles.size();
            final short[] availableTitles = new short[size];
            for (int i = 0; i < size; ++i) {
                availableTitles[i] = this.m_part.availableTitles.get(i).availableTitle;
            }
            LocalPlayerCharacter.this.setAvailableTitles(availableTitles);
        }
    }
    
    private final class LocalPlayerCharacterPartAptitudeInventory extends CharacterInfoPart
    {
        private final CharacterSerializedAptitudeInventory m_part;
        
        private LocalPlayerCharacterPartAptitudeInventory(final CharacterSerializedAptitudeInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les aptitudes ne devraient pas \u00eatre s\u00e9rialis\u00e9es par le client.");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_aptitudeInventory.fromRaw(this.m_part.aptitudeInventory);
            try {
                LocalPlayerCharacter.this.reloadAptitudeEffects(LocalPlayerCharacter.this.getAppropriateContext());
            }
            catch (Exception e) {
                LocalPlayerCharacter.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    private final class LocalPlayerCharacterPartAptitudeBonusInventory extends CharacterInfoPart
    {
        private final CharacterSerializedAptitudeBonusInventory m_part;
        
        private LocalPlayerCharacterPartAptitudeBonusInventory(final CharacterSerializedAptitudeBonusInventory part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les aptitudes ne devraient pas \u00eatre s\u00e9rialis\u00e9es par le client.");
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.optional == null) {
                return;
            }
            LocalPlayerCharacter.this.m_aptitudeBonusInventory.fromRaw(this.m_part.optional.aptitudeInventory);
            LocalPlayerCharacter.this.reloadNewAptitudeEffects(LocalPlayerCharacter.this.m_ownContext);
            AptitudesView.INSTANCE.reset(LocalPlayerCharacter.this.m_aptitudeBonusInventory);
            PropertiesProvider.getInstance().firePropertyValueChanged(AptitudesView.INSTANCE, AptitudesView.FIELDS);
        }
    }
    
    private final class LocalPlayerCharacterPartLocks extends CharacterInfoPart
    {
        private final CharacterSerializedLocksForClient m_part;
        
        private LocalPlayerCharacterPartLocks(final CharacterSerializedLocksForClient part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"Les Locks ne sont pas serialis\u00e9s par le client");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.m_locks = new LockContext();
            LockManager.INSTANCE.initializeContext(LocalPlayerCharacter.this.m_locks, !LocalPlayerCharacter.this.hasSubscriptionRight(SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION));
            final ArrayList<CharacterSerializedLocksForClient.Locks> locks = this.m_part.content;
            for (int i = 0, size = locks.size(); i < size; ++i) {
                final CharacterSerializedLocksForClient.Locks lock = locks.get(i);
                LocalPlayerCharacter.this.m_locks.setLockDate(lock.lockId, GameDate.fromLong(lock.lockDate), GameDate.fromLong(lock.unlockDate));
                LocalPlayerCharacter.this.m_locks.setCurrentLockValue(lock.lockId, lock.currentLockValue);
                LocalPlayerCharacter.this.m_locks.setCurrentLockValueDate(lock.lockId, GameDate.fromLong(lock.currentLockValueLastModification));
            }
        }
    }
    
    private final class LocalPlayerCharacterPartAntiAddiction extends CharacterInfoPart
    {
        private final CharacterSerializedAntiAddiction m_part;
        
        private LocalPlayerCharacterPartAntiAddiction(final CharacterSerializedAntiAddiction part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de modification de part ici.");
        }
        
        @Override
        public void onDataChanged() {
            final CharacterSerializedAntiAddiction.AddictionData addictionData = this.m_part.addictionData;
            if (addictionData == null) {
                LocalPlayerCharacter.this.m_antiAddictionDataHandler.setEnabled(false);
            }
            else {
                final AntiAddictionLevel previousLevel = AntiAddictionHelper.getCurrentLevel(LocalPlayerCharacter.this.m_antiAddictionDataHandler.getLastConnectionDate(), LocalPlayerCharacter.this.m_antiAddictionDataHandler.getCurrentUsedQuota(), WakfuGameCalendar.getInstance().getDate());
                LocalPlayerCharacter.this.m_antiAddictionDataHandler.setEnabled(true);
                LocalPlayerCharacter.this.m_antiAddictionDataHandler.setLastConnectionDate(GameDate.fromLong(addictionData.lastConnectionDate));
                LocalPlayerCharacter.this.m_antiAddictionDataHandler.setCurrentUsedQuota(GameInterval.fromLong(addictionData.currentUsedQuota));
                final AntiAddictionLevel currentLevel = AntiAddictionHelper.getCurrentLevel(LocalPlayerCharacter.this.m_antiAddictionDataHandler.getLastConnectionDate(), LocalPlayerCharacter.this.m_antiAddictionDataHandler.getCurrentUsedQuota(), WakfuGameCalendar.getInstance().getDate());
                AntiAddictionClientHelper.sendNotification(currentLevel);
            }
        }
    }
    
    private final class PlayerCharacterPartNationPvpMoney extends CharacterInfoPart
    {
        private final CharacterSerializedNationPvpMoney m_part;
        
        PlayerCharacterPartNationPvpMoney(final CharacterSerializedNationPvpMoney part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            LocalPlayerCharacter.m_logger.error((Object)"[NATION] Pas de s?rialization de la nation PUBLIC dans le client pour l'instant", (Throwable)new UnsupportedOperationException());
        }
        
        @Override
        public void onDataChanged() {
            final CitizenComportment comportment = LocalPlayerCharacter.this.getCitizenComportment();
            comportment.setPvpMoneyAmount(this.m_part.pvpMoneyAmount);
            comportment.setDailyPvpMoneyAmount(this.m_part.dailyPvpMoneyAmount);
        }
    }
    
    private final class PlayerCharacterPartPersonalEffects extends CharacterInfoPart
    {
        private final CharacterSerializedPersonalEffects m_part;
        
        private PlayerCharacterPartPersonalEffects(final CharacterSerializedPersonalEffects part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de modification de part ici.");
        }
        
        @Override
        public void onDataChanged() {
            final TIntHashSet previousGuildEffects = LocalPlayerCharacter.this.m_guildEffects;
            final TIntHashSet previousHavenEffects = LocalPlayerCharacter.this.m_havenWorldEffects;
            final TIntHashSet previousAntiAddictionEffect = LocalPlayerCharacter.this.m_antiAddictionEffects;
            if (this.m_part.guildEffects != null) {
                LocalPlayerCharacter.this.m_guildEffects = new TIntHashSet(this.m_part.guildEffects);
            }
            else {
                LocalPlayerCharacter.this.m_guildEffects = null;
            }
            if (this.m_part.havenWorldEffects != null) {
                LocalPlayerCharacter.this.m_havenWorldEffects = new TIntHashSet(this.m_part.havenWorldEffects);
            }
            else {
                LocalPlayerCharacter.this.m_havenWorldEffects = null;
            }
            if (this.m_part.antiAddictionEffects != null) {
                LocalPlayerCharacter.this.m_antiAddictionEffects = new TIntHashSet(this.m_part.antiAddictionEffects);
            }
            else {
                LocalPlayerCharacter.this.m_antiAddictionEffects = null;
            }
            if (!LocalPlayerCharacter.this.isOnFight() && this.hasChanged(previousAntiAddictionEffect, LocalPlayerCharacter.this.m_antiAddictionEffects)) {
                LocalPlayerCharacter.this.reloadAntiAddictionBuffs();
            }
            if (!LocalPlayerCharacter.this.isOnFight() && this.hasChanged(previousGuildEffects, LocalPlayerCharacter.this.m_guildEffects)) {
                LocalPlayerCharacter.this.reloadGuildBuffs();
            }
            if (!LocalPlayerCharacter.this.isOnFight() && this.hasChanged(previousHavenEffects, LocalPlayerCharacter.this.m_havenWorldEffects)) {
                LocalPlayerCharacter.this.reloadHavenWorldBuffs();
            }
        }
        
        private boolean hasChanged(final TIntHashSet previousEffects, final TIntHashSet currentEffects) {
            if (previousEffects == null) {
                return currentEffects != null;
            }
            return !previousEffects.equals(currentEffects);
        }
    }
    
    private final class LocalPlayerCharacterPartAccountInformation extends CharacterInfoPart
    {
        private final CharacterSerializedAccountInformation m_part;
        
        private LocalPlayerCharacterPartAccountInformation(final CharacterSerializedAccountInformation part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Les informations de compte de sont pas s\u00e9rialis\u00e9s par le client");
        }
        
        @Override
        public void onDataChanged() {
            LocalPlayerCharacter.this.getAccountInformationHandler().fromBuild(this.m_part);
            PropertiesProvider.getInstance().setPropertyValue("subscribedAccount", WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(LocalPlayerCharacter.this));
            PropertiesProvider.getInstance().setPropertyValue("subscribedZoneAccount", WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(LocalPlayerCharacter.this));
            PropertiesProvider.getInstance().setPropertyValue("politicInteractionRight", LocalPlayerCharacter.this.hasSubscriptionRight(SubscriptionRight.POLITIC_INTERACTION));
            PropertiesProvider.getInstance().firePropertyValueChanged(LocalPlayerCharacter.this, "isInSubscriberZone");
        }
    }
}
