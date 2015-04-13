package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.baseImpl.common.clientAndServer.game.dialog.*;
import com.ankamagames.wakfu.common.game.title.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.client.core.game.title.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific.*;
import com.ankamagames.wakfu.common.game.wakfu.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.name.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import java.util.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;
import com.ankamagames.baseImpl.common.clientAndServer.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.death.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.survey.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class PlayerCharacter extends CharacterInfo implements DialogUser, TitleHolder, GroupUser, WakfuAccountInformationHolder, GuildUser, AchievementsContextProvider<ClientAchievementsContext>
{
    public static final String SKIN_COLOR_INDEX_FIELD = "skinColorIndex";
    public static final String HAIR_COLOR_INDEX_FIELD = "hairColorIndex";
    public static final String PUPIL_COLOR_INDEX_FIELD = "pupilColorIndex";
    public static final String SKIN_COLOR_FACTOR_FIELD = "skinColorFactor";
    public static final String HAIR_COLOR_FACTOR_FIELD = "hairColorFactor";
    public static final String CLOTH_INDEX_FIELD = "clothIndex";
    public static final String FACE_INDEX_FIELD = "faceIndex";
    public static final String SKIN_DESCRIPTION = "skinDescription";
    public static final String HAIR_DESCRIPTION = "hairDescription";
    public static final String PUPIL_DESCRIPTION = "pupilDescription";
    public static final String SKIN_AVAILABILITY = "skinAvailability";
    public static final String HAIR_AVAILABILITY = "hairAvailability";
    public static final String PUPIL_AVAILABILITY = "pupilAvailability";
    public static final String ILLUSTRATION_URL_FIELD = "illustrationUrl";
    public static final String CHARACTER_CHOICE_ILLUSTRATION_URL_FIELD = "characterChoiceIllustrationUrl";
    public static final String CHARACTER_SHEET_ILLUSTRATION_URL_FIELD = "characterSheetillustrationUrl";
    public static final String CHARACTER_SHEET_SMALL_ILLUSTRATION_URL = "characterSheetSmallIllustrationUrl";
    public static final String SET_INDEX_FIELD = "setIndex";
    public static final String CURRENT_TITLE_ID_FIELD = "currentTitleId";
    public static final String CURRENT_TITLE_FIELD = "currentTitle";
    public static final String CURRENT_TITLE_OBJECT_FIELD = "currentTitleObject";
    public static final String HAS_GUILD_FIELD = "hasGuild";
    public static final String HAS_NATION_FIELD = "hasNation";
    public static final String CHARCTER_LIST_NATION = "characterListNation";
    public static final String CHARCTER_LIST_NATION_ICON_URL = "characterListNationIconUrl";
    public static final String CITIZEN_FIELD = "citizen";
    public static final String BREED_APTITUDES_TITLE = "breedAptitudesTitle";
    public static final String[] FIELDS;
    public static final String[] ALL_FIELDS;
    protected final PlayerCharacterSerializer m_serializer;
    private final ArrayList<MRUActions> m_mruActions;
    protected byte m_skinColorIndex;
    protected byte m_skinColorFactor;
    protected byte m_hairColorIndex;
    protected byte m_hairColorFactor;
    protected byte m_pupilColorIndex;
    protected int m_gfxId;
    protected boolean m_afkStateActive;
    protected boolean m_dndStateActive;
    private boolean m_needsRecustom;
    private short m_recustomType;
    protected TByteIntHashMap m_equipementAppearance;
    protected byte m_setIndex;
    private PlayerTitle m_currentTitle;
    protected AbstractOccupation m_currentOccupation;
    private static final JavaFunctionsLibrary[] LIBRARIES;
    private BreedSpecific m_breedSpecific;
    protected BonusPointCharacteristics m_bonusPointCharacteristics;
    protected WakfuGauge m_wakfuGauge;
    public boolean m_initialized;
    protected RawStateRunningEffects m_stateREToUnserializeAtInit;
    private final SkillCharacteristics m_skillCharacteristics;
    private final CharacteristicRegenHandler m_hpRegenHandler;
    private final OtherPlayerItemEffectsApplier m_otherPlayerItemEffectsApplier;
    protected SpellInventoryManager m_spellInventoryManager;
    private final SacrieurArmsApparitionListener m_chrageListener;
    private final WakfuAccountInformationHandler m_accountInformationHandler;
    private final ClientGuildInformationHandler m_guildHandler;
    private PetMobileView m_petMobile;
    private PetMobileView m_mountMobile;
    private short m_level;
    
    public PlayerCharacter() {
        super();
        this.m_serializer = new PlayerCharacterSerializer();
        this.m_mruActions = new ArrayList<MRUActions>(Arrays.asList(MRUActions.CHARACTER_CAST_PVP_FIGHT_MODEL_ACTION, MRUActions.CHARACTER_CAST_PVP_RANKED_FIGHT_MODEL_ACTION, MRUActions.CHARACTER_CAST_DUEL_FIGHT_ACTION, MRUActions.CHARACTER_JOIN_FIGHT_ACTION, MRUActions.CHARACTER_EXCHANGE_ACTION, MRUActions.CHARACTER_KICK_ACTION, MRUActions.INVIT_TO_JOIN_PARTY, MRUActions.CREATE_PRIVATE_CHAT_ACTION, MRUActions.FOLLOW_PLAYER, MRUActions.RESURRECT_PLAYER_ACTION, MRUActions.ATTEND_FIGHT, MRUActions.OPEN_MODERATION_PANEL_ACTION));
        this.m_bonusPointCharacteristics = BonusPointCharacteristics.checkOut();
        this.m_skillCharacteristics = new SkillCharacteristicsForPlayer();
        this.m_otherPlayerItemEffectsApplier = new OtherPlayerItemEffectsApplier(this);
        this.m_chrageListener = new SacrieurArmsApparitionListener();
        this.m_additionalAppearance = new CharacterAdditionalAppearance(this);
        this.m_characteristics = new FighterCharacteristicManager();
        this.m_characteristicViewProvider = new CharacteristicViewProvider(this);
        this.m_hpRegenHandler = new CharacteristicRegenHandler(4000.0, this.m_characteristics.getCharacteristic(FighterCharacteristicType.HP));
        this.m_ownContext.setEffectManager(EffectManager.getInstance());
        this.m_accountInformationHandler = this.createAccountInformationHandler();
        this.m_guildHandler = this.createGuildHandler();
        this.m_applyGuildVisual = new ApplyGuildVisual(this.m_guildHandler);
        this.initializeSerializer();
    }
    
    protected WakfuAccountInformationHandler createAccountInformationHandler() {
        return new WakfuRemoteAccountInformationHandler();
    }
    
    protected ClientGuildInformationHandler createGuildHandler() {
        return new GuildRemoteInformationHandler();
    }
    
    public void initializeSerializer() {
        super.initializeSerializer();
        new PlayerCharacterPartAppearance(this.m_serializer.getAppearancePart());
        new PlayerCharacterPartEquipmentAppearance(this.m_serializer.getEquipmentAppearancePart());
        new PlayerCharacterPartHp(this.m_serializer.getHpPart());
        new PlayerCharacterPartXp(this.m_serializer.getXpPart());
        new PlayerCharacterPartXpCharacteristics(this.m_serializer.getXpCharacteristicsPart());
        new PlayerCharacterPartCreationData(this.m_serializer.getCreationDataPart());
        new PlayerCharacterPartRunningEffects(this.m_serializer.getRunningEffectsPart());
        new PlayerCharacterPartCurrentMovementPath(this.m_serializer.getCurrentMovementPathPart());
        new PlayerCharacterPartOccupation(this.m_serializer.getOccupationPart());
        new PlayerCharacterPartGuildRemoteInfo(this.m_serializer.getGuildRemoteInfoPart());
        new PlayerCharacterPartNationId(this.m_serializer.getNationIdPart());
        new PlayerCharacterPartCharacterListNationId(this.m_serializer.getCharacterListNationIdPart());
        new PlayerCharacterPartNationSynchro(this.m_serializer.getNationSynchroPart());
        new PlayerCharacterPartNationPvp(this.m_serializer.getNationPvpPart());
        new PlayerCharacterPartNationCitizenScore(this.m_serializer.getCitizenPart());
        new PlayerCharacterPartSocialStates(this.m_serializer.getSocialStatesPart());
        new PlayerCharacterPartPasseportInfo(this.m_serializer.getPasseportInfoPart());
        new PlayerCharacterPartRemoteAccountInformation(this.m_serializer.getRemoteAccountInformationPart());
        new PlayerCharacterPartPet(this.m_serializer.getPetPart());
        new PlayerCharacterPartVisibility(this.m_serializer.getVisibilityPart());
        new PlayerCharacterPartCharacteristics(this.m_serializer.getPublicCharacteristicsPart());
        new PlayerCharacterPartAchievements(this.m_serializer.getAchievementsPart());
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_bonusPointCharacteristics = BonusPointCharacteristics.checkOut();
        this.m_guildHandler.clear();
        this.m_needsRecustom = false;
        this.m_recustomType = 0;
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_breedSpecific != null) {
            this.m_breedSpecific.clear();
            this.m_breedSpecific = null;
        }
        super.onCheckIn();
        this.m_serializer.clear();
        this.m_bonusPointCharacteristics.release();
        this.m_bonusPointCharacteristics = null;
        this.removePetMobile();
        this.removeMountMobile();
        this.m_skillCharacteristics.reset();
        this.m_guildHandler.clear();
        final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(this.getId());
        if (context != null) {
            ((LocalPlayerCharacterAchievementsListener)context.getListener()).cleanUp();
        }
    }
    
    public void createPetMobileView(final int petDefinitionId, final int colorRefItemId, final int equippedRefItemId, final int sleepRefItemId, final int health) {
        if (this.m_petMobile == null) {
            final PetDefinition def = PetDefinitionManager.INSTANCE.get(petDefinitionId);
            final Pet pet = PetFactory.INSTANCE.createPet(def);
            this.m_petMobile = new PetMobileView(this, pet);
            this.getActor().addVisibleChangedListener(this.m_petMobile);
        }
        this.createFollowMobile(colorRefItemId, equippedRefItemId, sleepRefItemId, health, this.m_petMobile);
    }
    
    public void createMountMobileView(final int petDefinitionId, final int colorRefItemId, final int equippedRefItemId, final int sleepRefItemId, final int health) {
        if (this.m_mountMobile == null) {
            final PetDefinition def = PetDefinitionManager.INSTANCE.get(petDefinitionId);
            final Pet pet = PetFactory.INSTANCE.createPet(def);
            this.m_mountMobile = new PetMobileView(this, pet);
            this.getActor().addVisibleChangedListener(this.m_mountMobile);
        }
        this.createFollowMobile(colorRefItemId, equippedRefItemId, sleepRefItemId, health, this.m_mountMobile);
    }
    
    private void createFollowMobile(final int colorRefItemId, final int equippedRefItemId, final int sleepRefItemId, final int health, final PetMobileView view) {
        final PetController controller = new PetController(view);
        final Pet pet = view.getPet();
        try {
            controller.setColorItem(colorRefItemId);
            controller.setHealth(health);
            if (equippedRefItemId > 0) {
                controller.setEquipment(equippedRefItemId);
            }
            else if (equippedRefItemId <= 0 && pet.getEquippedRefItemId() > 0) {
                controller.removeEquipment();
            }
            if (sleepRefItemId > 0 && pet.getSleepRefItemId() <= 0) {
                controller.setSleepRefItemId(sleepRefItemId);
                controller.setSleepDate(WakfuGameCalendar.getInstance().getDate());
            }
            else if (sleepRefItemId <= 0 && pet.getSleepRefItemId() > 0) {
                controller.removeSleepRefItemId();
                controller.removeSleepDate();
            }
        }
        catch (PetControllerException e) {
            PlayerCharacter.m_logger.error((Object)("Erreur lors de l'initialisation du familier dans le monde pour le joueur " + this), (Throwable)e);
        }
    }
    
    public void removePetMobile() {
        this.removeFollowMobile(this.m_petMobile);
        this.m_petMobile = null;
    }
    
    public void removeMountMobile() {
        this.removeFollowMobile(this.m_mountMobile);
        this.m_mountMobile = null;
    }
    
    private void removeFollowMobile(final PetMobileView view) {
        if (view == null) {
            return;
        }
        if (this.hasActor()) {
            this.getActor().removeVisibleChangedListener(view);
        }
        view.detach();
    }
    
    public PetMobileView getPetMobile() {
        return this.m_petMobile;
    }
    
    public PetMobileView getMountMobile() {
        return this.m_mountMobile;
    }
    
    @Override
    public void onJoinFight(final BasicFight basicFight) {
        super.onJoinFight(basicFight);
        if (HeroesLeaderManager.INSTANCE.isHero(this.getOwnerId(), this.getId())) {
            this.getActor().setVisible(true);
        }
        this.removeAdditionalAppearance();
        this.updateAdditionalAppearance();
        this.applySacrieurArms();
    }
    
    public void applySacrieurArms() {
        this.m_chrageListener.setPreviousValue(100);
        this.m_chrageListener.onHpModification(this);
    }
    
    @NotNull
    public CharacteristicRegenHandler getHpRegenHandler() {
        return this.m_hpRegenHandler;
    }
    
    @Override
    protected Breed defaultBreed() {
        return AvatarBreed.NONE;
    }
    
    @Override
    protected byte defaultCharacterType() {
        return 0;
    }
    
    @Override
    public boolean returnDefaultAIControl() {
        return false;
    }
    
    @Override
    public void setGfxId(final int gfxId) {
        if (this.m_gfxId == gfxId) {
            return;
        }
        this.m_gfxId = gfxId;
        for (final CharacterInfoPropertyEventsHandler handler : this.m_characterInfoEventsHandler) {
            handler.onAppearanceChanged(this);
        }
        this.refreshDisplayEquipment();
    }
    
    @Override
    public AvatarBreed getBreed() {
        return (AvatarBreed)super.getBreed();
    }
    
    @Override
    public int getGfxId() {
        final int forcedGfxId = super.getGfxId();
        if (forcedGfxId != 0) {
            return forcedGfxId;
        }
        return this.m_gfxId;
    }
    
    public byte getSkinColorIndex() {
        return this.m_skinColorIndex;
    }
    
    public void setSkinColorIndex(final byte skinColorIndex, final byte skinColorFactor, final boolean apply) {
        this.m_skinColorIndex = skinColorIndex;
        this.m_skinColorFactor = skinColorFactor;
        if (apply) {
            this.updateSkinColor();
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "skinColorIndex", "skinColorFactor");
        }
    }
    
    public byte getSkinColorFactor() {
        return this.m_skinColorFactor;
    }
    
    private void updateSkinColor() {
        applySkinColor(this, this, false);
    }
    
    public byte getPupilColorIndex() {
        return this.m_pupilColorIndex;
    }
    
    public void setPupilColorIndex(final byte pupilColorIndex, final boolean apply) {
        this.m_pupilColorIndex = pupilColorIndex;
        if (apply) {
            this.updatePupilColor();
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "pupilColorIndex");
        }
    }
    
    private void updatePupilColor() {
        final CharacterColor pupilColor = BreedColorsManager.getInstance().getPupilColor(this.m_breed.getBreedId(), this.m_pupilColorIndex, this.getSex());
        CharacterColor.applyColor(pupilColor, this.getActor(), 8);
    }
    
    public byte getHairColorIndex() {
        return this.m_hairColorIndex;
    }
    
    public byte getHairColorFactor() {
        return this.m_hairColorFactor;
    }
    
    public boolean isNeedsRecustom() {
        return this.m_needsRecustom;
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        super.onCharacteristicUpdated(charac);
        if (charac.getType() == FighterCharacteristicType.HP) {
            this.m_chrageListener.onHpModification(this);
        }
    }
    
    public SacrieurArmsApparitionListener getChrageListener() {
        return this.m_chrageListener;
    }
    
    @Override
    public void addFightCharacteristicsListeners() {
        ((FighterCharacteristicManager)this.getCharacteristics()).addListener(this);
        TimeManager.INSTANCE.addListener(this.getRunningEffectFieldProvider());
    }
    
    @Override
    public void removeFightCharacteristicsListeners() {
        ((FighterCharacteristicManager)this.getCharacteristics()).removeListener(this);
        TimeManager.INSTANCE.removeListener(this.getRunningEffectFieldProvider());
    }
    
    public void setHairColorIndex(final byte hairColorIndex, final byte hairColorFactor, final boolean apply) {
        this.m_hairColorIndex = hairColorIndex;
        this.m_hairColorFactor = hairColorFactor;
        if (apply) {
            this.updateHairColor();
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "hairColorIndex", "hairColorFactor");
        }
    }
    
    private void updateHairColor() {
        applyHairColor(this, this, false);
    }
    
    public static void applyHairColor(final PlayerCharacter src, final CharacterInfo dest, final boolean fireChanged) {
        final CharacterColor hairColor = BreedColorsManager.getInstance().getHairColor(src);
        CharacterColor.applyColor(hairColor, dest.getActor(), 2);
        if (fireChanged) {
            PropertiesProvider.getInstance().firePropertyValueChanged(dest, "hairColorIndex");
        }
    }
    
    public static void applySkinColor(final PlayerCharacter src, final CharacterInfo dest, final boolean fireChanged) {
        final CharacterColor skinColor = BreedColorsManager.getInstance().getSkinColor(src);
        CharacterColor.applyColor(skinColor, dest.getActor(), 1);
        if (fireChanged) {
            PropertiesProvider.getInstance().firePropertyValueChanged(dest, "skinColorIndex");
        }
    }
    
    @Override
    protected final void updateColors() {
        this.updateHairColor();
        this.updatePupilColor();
        this.updateSkinColor();
    }
    
    @Override
    protected final void applyEquipmentColorModifiers() {
        super.applyEquipmentColorModifiers();
        this.getActor().applyHmiAppearenceColorModifiers();
    }
    
    public CharacterColor[] getSecondarySkinColors() {
        return BreedColorsManager.getInstance().getSecondarySkinColors(this.getBreedId(), this.m_skinColorIndex, this.getSex());
    }
    
    public CharacterColor[] getSecondaryHairColors() {
        return BreedColorsManager.getInstance().getSecondaryHairColors(this.getBreedId(), this.m_hairColorIndex, this.getSex());
    }
    
    public void adjustGfxFromBreedAndSex() {
        this.setGfxId(formatGfx(this.getBreed().getBreedId(), this.getSex()));
    }
    
    public static short formatGfx(final short breedId, final byte sex) {
        return Short.valueOf(String.valueOf(breedId) + sex);
    }
    
    public void setAvatarBreed(final Breed breed) {
        this.setBreed(breed);
        this.m_setIndex = 0;
        this.adjustGfxFromBreedAndSex();
    }
    
    @Override
    public void setSex(final byte sex) {
        super.setSex(sex);
        this.adjustGfxFromBreedAndSex();
    }
    
    @NotNull
    @Override
    public SkillCharacteristics getSkillCharacteristics() {
        return this.m_skillCharacteristics;
    }
    
    public void setDefaultColors() {
        final ObjectPair<Byte, Byte> defaultSkinColorIndex = BreedColorsManager.getInstance().getDefaultSkinColorIndex(this.getBreed().getBreedId(), this.getSex());
        if (defaultSkinColorIndex != null) {
            this.setSkinColorIndex(defaultSkinColorIndex.getFirst(), defaultSkinColorIndex.getSecond(), true);
        }
        final ObjectPair<Byte, Byte> defaultHairColorIndex = BreedColorsManager.getInstance().getDefaultHairColorIndex(this.getBreed().getBreedId(), this.getSex());
        if (defaultHairColorIndex != null) {
            this.setHairColorIndex(defaultHairColorIndex.getFirst(), defaultHairColorIndex.getSecond(), true);
        }
        final byte defaultPupilColorIndex = BreedColorsManager.getInstance().getDefaultPupilColorIndex(this.getBreed().getBreedId(), this.getSex());
        this.setPupilColorIndex(defaultPupilColorIndex, true);
    }
    
    public void setRandomName() {
        this.m_name = WakfuNameGenerator.getInstance().getRandomName();
    }
    
    public short getSetId() {
        final short[] sets = ItemSetManager.getInstance().getAllowedBreedSets(this.getBreed().getBreedId(), this.getSex());
        return sets[this.m_setIndex];
    }
    
    public void setSet() {
        final short[] sets = ItemSetManager.getInstance().getAllowedBreedSets(this.getBreed().getBreedId(), this.getSex());
        this.m_setIndex = 0;
        if (sets != null && sets.length > 0) {
            this.applySet(sets[this.m_setIndex]);
        }
    }
    
    @Override
    public AbstractOccupation getCurrentOccupation() {
        return this.m_currentOccupation;
    }
    
    @Override
    public void setCurrentOccupation(final BasicOccupation currentOccupation) {
        this.m_currentOccupation = (AbstractOccupation)currentOccupation;
    }
    
    @Override
    public boolean cancelCurrentOccupation(final boolean fromServer, final boolean sendMessage) {
        if (this.m_currentOccupation == null) {
            return true;
        }
        if (this.m_currentOccupation.cancel(fromServer, sendMessage)) {
            this.m_currentOccupation = null;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean finishCurrentOccupation() {
        if (this.m_currentOccupation == null) {
            return true;
        }
        if (this.m_currentOccupation.finish()) {
            this.m_currentOccupation = null;
            return true;
        }
        return false;
    }
    
    @Override
    public void raiseOutOfCombat() {
        this.finishCurrentOccupation();
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
    
    public void applySet(final short setId) {
        if (applySet(setId, this.getActor(), this.getSex())) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "setIndex");
        }
    }
    
    public static boolean applySet(final short setId, final Actor actor, final byte sex) {
        final ItemSet set = ItemSetManager.getInstance().getItemSet(setId);
        if (set != null) {
            boolean applied = false;
            for (final ReferenceItem refItem : set) {
                if (refItem != null) {
                    final Item item = new Item();
                    item.initializeWithReferenceItem(refItem);
                    if (item.getType() == null) {
                        continue;
                    }
                    final EquipmentPosition[] equipmentPositions = item.getType().getEquipmentPositions();
                    if (equipmentPositions.length <= 0) {
                        continue;
                    }
                    final short position = equipmentPositions[0].m_id;
                    actor.applyEquipment(refItem, position, sex);
                    applied = true;
                }
                else {
                    PlayerCharacter.m_logger.error((Object)"Impossible de cree l'item , reference item null");
                }
            }
            return applied;
        }
        PlayerCharacter.m_logger.error((Object)("Impossible d'appliquer le set " + setId + " \u00ef¿½ un personnage : set inconnu"));
        return false;
    }
    
    @Override
    public String[] getFields() {
        return PlayerCharacter.ALL_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("skinColorIndex")) {
            return this.m_skinColorIndex;
        }
        if (fieldName.equals("hairColorIndex")) {
            return this.m_hairColorIndex;
        }
        if (fieldName.equals("pupilColorIndex")) {
            return this.m_pupilColorIndex;
        }
        if (fieldName.equals("skinColorFactor")) {
            return this.m_skinColorFactor;
        }
        if (fieldName.equals("hairColorFactor")) {
            return this.m_hairColorFactor;
        }
        if (fieldName.equals("clothIndex")) {
            return this.getClothIndex();
        }
        if (fieldName.equals("faceIndex")) {
            return this.getFaceIndex();
        }
        if (fieldName.equals("hairAvailability")) {
            return BreedSkinAndHairManager.getInstance().getHairAvailability(this.getBreed().getBreedId(), this.getSex());
        }
        if (fieldName.equals("skinAvailability")) {
            return BreedSkinAndHairManager.getInstance().getSkinAvailability(this.getBreed().getBreedId(), this.getSex());
        }
        if (fieldName.equals("pupilAvailability")) {
            return BreedSkinAndHairManager.getInstance().getPupilAvailability(this.getBreed().getBreedId(), this.getSex());
        }
        if (fieldName.equals("hairDescription")) {
            final String key = String.format("breed.hairDescription.%d%d", this.getBreed().getBreedId(), this.getSex());
            return WakfuTranslator.getInstance().containsKey(key) ? WakfuTranslator.getInstance().getString(key) : WakfuTranslator.getInstance().getString("characterCreation.hair");
        }
        if (fieldName.equals("skinDescription")) {
            final String key = String.format("breed.skinDescription.%d%d", this.getBreed().getBreedId(), this.getSex());
            return WakfuTranslator.getInstance().containsKey(key) ? WakfuTranslator.getInstance().getString(key) : WakfuTranslator.getInstance().getString("characterCreation.skin");
        }
        if (fieldName.equals("illustrationUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedIllustrationPath"), this.getBreed().getBreedId(), this.getSex());
            }
            catch (PropertyException e) {
                PlayerCharacter.m_logger.error((Object)"Exception", (Throwable)e);
                return super.getFieldValue(fieldName);
            }
        }
        if (fieldName.equals("characterChoiceIllustrationUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedCharacterChoiceIllustrationPath"), this.getBreed().getBreedId(), this.getSex());
            }
            catch (PropertyException e) {
                PlayerCharacter.m_logger.error((Object)"Exception", (Throwable)e);
                return super.getFieldValue(fieldName);
            }
        }
        if (fieldName.equals("characterSheetillustrationUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedContactListIllustrationPath"), this.getBreedId(), this.getSex());
            }
            catch (PropertyException e) {
                PlayerCharacter.m_logger.error((Object)"Exception", (Throwable)e);
                return super.getFieldValue(fieldName);
            }
        }
        if (fieldName.equals("characterSheetSmallIllustrationUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), this.m_breed.getBreedId() + String.valueOf(this.m_sex));
            }
            catch (PropertyException e) {
                PlayerCharacter.m_logger.error((Object)"Exception", (Throwable)e);
                return super.getFieldValue(fieldName);
            }
        }
        if (fieldName.equals("currentTitle")) {
            return (this.m_currentTitle == null) ? null : this.m_currentTitle.getDescription();
        }
        if (fieldName.equals("currentTitleId")) {
            return (short)((this.m_currentTitle == null) ? -1 : this.m_currentTitle.getId());
        }
        if (fieldName.equals("secondarySkinColors")) {
            return this.getSecondarySkinColors();
        }
        if (fieldName.equals("secondaryHairColors")) {
            return this.getSecondaryHairColors();
        }
        if (fieldName.equals("currentTitleObject")) {
            return this.m_currentTitle;
        }
        if (fieldName.equals("hasGuild")) {
            return this.isInGuild();
        }
        if (fieldName.equals("hasNation")) {
            return this.hasNation();
        }
        if (fieldName.equals("characterListNation")) {
            return WakfuTranslator.getInstance().getString(39, LocalCharacterInfosManager.getInstance().getNationInformation(this), new Object[0]);
        }
        if (fieldName.equals("characterListNationIconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("nationFlagIconsPath", "defaultIconPath", LocalCharacterInfosManager.getInstance().getNationInformation(this));
        }
        if (fieldName.equals("citizen")) {
            return this.getCitizenComportment();
        }
        if (fieldName.equals("breedAptitudesTitle")) {
            return WakfuTranslator.getInstance().getString("breed.aptitudes", this.getBreedInfo().getName());
        }
        if (fieldName.equals("pupilDescription")) {
            final String key = String.format("breed.pupilDescription.%d%d", this.getBreed().getBreedId(), this.getSex());
            return WakfuTranslator.getInstance().containsKey(key) ? WakfuTranslator.getInstance().getString(key) : WakfuTranslator.getInstance().getString("characterCreation.pupil");
        }
        return super.getFieldValue(fieldName);
    }
    
    public boolean hasNation() {
        return this.getCitizenComportment().getNation() != Nation.VOID_NATION;
    }
    
    @Override
    public PlayerCharacterSerializer getSerializer() {
        return this.m_serializer;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return fieldName.equals("skinColorIndex") || fieldName.equals("hairColorIndex") || fieldName.equals("pupilColorIndex") || fieldName.equals("hairColorFactor") || fieldName.equals("skinColorFactor") || super.isFieldSynchronisable(fieldName);
    }
    
    @Nullable
    @Override
    public TByteIntHashMap getEquipmentAppearance() {
        return this.m_equipementAppearance;
    }
    
    @Override
    protected String getSmileyGfxFileName() {
        return "Smiley_" + this.getBreedId() + this.getSex();
    }
    
    @Override
    protected String getGfxPathKey() {
        return "playerGfxPath";
    }
    
    public short getRecustomType() {
        return this.m_recustomType;
    }
    
    @Override
    public ClientAchievementsContext getAchievementsContext() {
        return AchievementContextManager.INSTANCE.getContext(this.getId());
    }
    
    @Override
    public void initialize() {
        this.getRunningEffectManager().initialize(this, this.getEffectContext());
        this.reloadItemEffects();
        final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(this.getId());
        if (context != null) {
            context.getListener().onInitialize(context);
        }
    }
    
    public void onHealthRegenTick(final long realTime) {
        if (!this.isOnFight()) {
            this.m_hpRegenHandler.onRegenTick(realTime);
        }
    }
    
    @Override
    public void initializeAfterCharacterAddedToWorld() {
        final CharacterSerializedRunningEffects runningEffectsPart = this.m_serializer.getRunningEffectsPart();
        this.getRunningEffectFieldProvider().clear();
        if (runningEffectsPart != null) {
            if (runningEffectsPart.inFightData != null) {
                this.getRunningEffectManager().unserializeInFight(runningEffectsPart.inFightData.data, this.getEffectContext());
            }
            if (runningEffectsPart.outFightData != null) {
                this.getRunningEffectManager().fromRawStateRunningEffects(runningEffectsPart.outFightData.stateRunningEffects, this.m_ownContext, this);
            }
        }
        this.m_actor.addPositionListener(WakfuNationEnemyListener.INSTANCE);
    }
    
    @Override
    public void initialiseCharacteristicsToBaseValue() {
        if (this.isOnFight()) {
            this.m_characteristics.makeDefault();
            this.m_chrageListener.reset();
            this.addHpToChrageProcedureForSacrier();
        }
    }
    
    public PlayerTitle getCurrentTitleObject() {
        return this.m_currentTitle;
    }
    
    @Override
    public short getCurrentTitle() {
        return (short)((this.m_currentTitle != null) ? this.m_currentTitle.getId() : -1);
    }
    
    @Nullable
    public String getCurrentTitleDescription() {
        return (this.m_currentTitle != null) ? this.m_currentTitle.getDescription() : null;
    }
    
    @Override
    public void setCurrentTitle(final short titleId) {
        this.setCurrentTitle(new PlayerTitle(titleId));
    }
    
    public void setCurrentTitle(final PlayerTitle title) {
        this.m_currentTitle = title;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentTitleId", "currentTitle", "currentTitleObject");
    }
    
    @Override
    public void onLeaveFight() {
        super.onLeaveFight();
        final AttackType currentAttack = this.m_actor.getCurrentAttack();
        if (currentAttack != null && currentAttack != NoneAttack.getInstance()) {
            currentAttack.endUsage(this.m_actor);
        }
        this.m_actor.setCurrentAttack(NoneAttack.getInstance());
        NoneAttack.getInstance().startUsageAndNotify(this.m_actor);
        if (!this.getCurrentFight().getModel().mustDieAtEndOfFight()) {
            this.setDead(false);
        }
        if (!this.getCurrentFight().hasLost(this.getId()) && this.getCharacteristicValue(FighterCharacteristicType.HP) < 1 && this.isOffPlay()) {
            this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(1);
        }
        if (HeroesLeaderManager.INSTANCE.isHero(this.getOwnerId(), this.getId())) {
            this.getActor().setVisible(false);
        }
    }
    
    @Override
    protected void onDeadChange() {
        super.onDeadChange();
        this.updateAdditionalAppearance();
    }
    
    @Override
    public int getWalkTimeBetweenCells() {
        final int baseSpeed = super.getWalkTimeBetweenCells();
        final float factor = 1.0f;
        return (int)(baseSpeed / 1.0f);
    }
    
    @Override
    public int getRunTimeBetweenCells() {
        final int baseSpeed = super.getRunTimeBetweenCells();
        final float factor = 1.0f;
        return (int)(baseSpeed / 1.0f);
    }
    
    @Override
    public float getWakfuGaugeValue() {
        return (this.m_wakfuGauge == null) ? 0.0f : this.m_wakfuGauge.getUserFriendlyValue();
    }
    
    public void setWakfuGauge(final int actionValue) {
        final Float previousWakfuGauge = (this.m_wakfuGauge == null) ? null : this.m_wakfuGauge.getUserFriendlyValue();
        if (this.m_wakfuGauge == null) {
            this.m_wakfuGauge = new WakfuGauge();
        }
        this.m_wakfuGauge.setValue(actionValue);
        if (previousWakfuGauge == null) {
            return;
        }
        if (WakfuGameEntity.getInstance().getLocalPlayer().isOnFight() && this.getCurrentFightId() != WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFightId()) {
            return;
        }
        final boolean toWakfuSide = this.m_wakfuGauge.getUserFriendlyValue() - previousWakfuGauge > 0.0f;
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(toWakfuSide ? 800067 : 800068);
        if (system == null) {
            return;
        }
        system.setTarget(this.getActor());
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
    }
    
    private void setStateREToUnserializeAtInit(final RawStateRunningEffects stateREToUnserializeAtInit) {
        this.m_stateREToUnserializeAtInit = stateREToUnserializeAtInit;
    }
    
    @Override
    public void reloadItemEffects() {
        if (this.isLocalPlayer()) {
            super.reloadItemEffects();
        }
        else {
            this.m_otherPlayerItemEffectsApplier.reloadItemsEffectsForPlayer();
        }
        this.executeAfterAllUpdate("reloadItemEffects", new Runnable() {
            @Override
            public void run() {
                PlayerCharacter.this.refreshDisplayEquipment();
            }
        });
    }
    
    @Override
    public void onEffectUnApplication(final RunningEffect effect) {
        super.onEffectUnApplication(effect);
        final boolean isHavenWorldBuff = effect.getEffectContainer() != null && effect.getEffectContainer().getContainerType() == 28;
        final boolean isGuildBuff = effect.getEffectContainer() != null && effect.getEffectContainer().getContainerType() == 32;
        if (!isHavenWorldBuff && !isGuildBuff) {
            return;
        }
        this.getRunningEffectManager().removeEffect(effect);
    }
    
    @Nullable
    @Override
    public SpellInventory<SpellLevel> getSpellInventory() {
        if (this.m_spellInventoryManager == null) {
            return null;
        }
        return this.m_spellInventoryManager.getSpellInventory();
    }
    
    @Nullable
    @Override
    public Iterable<? extends AbstractSpellLevel> getPermanentSpellInventory() {
        if (this.m_spellInventoryManager == null) {
            return null;
        }
        return (Iterable<? extends AbstractSpellLevel>)this.m_spellInventoryManager.getPermanentSpellInventory();
    }
    
    @Override
    public void createSpellInventoryFromRaw(final RawSpellLevelInventory rawSpellLevelInventory) {
        if (this.m_spellInventoryManager == null) {
            this.m_spellInventoryManager = new SpellInventoryManager(this);
        }
        this.m_spellInventoryManager.getSpellInventory().fromRaw(rawSpellLevelInventory);
    }
    
    @Nullable
    @Override
    public SpellLevel getSpellLevelById(final long uid) {
        if (this.m_spellInventoryManager == null) {
            return null;
        }
        return this.m_spellInventoryManager.getSpellLevelById(uid);
    }
    
    @Nullable
    @Override
    public Iterable<SpellLevel> getSpellLevels() {
        if (this.m_spellInventoryManager == null) {
            return null;
        }
        return this.m_spellInventoryManager.getSpellLevels();
    }
    
    @Override
    public SpellInventoryManager getSpellInventoryManager() {
        return this.m_spellInventoryManager;
    }
    
    @Override
    public long getGroupId(final GroupType groupType) {
        switch (groupType) {
            case PARTY: {
                final PartyModelInterface party = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment().getParty();
                if (party == null) {
                    return -1L;
                }
                return (party.getMember(this.getId()) != null) ? party.getId() : -1L;
            }
            default: {
                return -1L;
            }
        }
    }
    
    @Deprecated
    @Override
    public boolean hasAGroup() {
        return false;
    }
    
    @Override
    public ClientGuildInformationHandler getGuildHandler() {
        return this.m_guildHandler;
    }
    
    @Override
    public WakfuAccountInformationHandler getAccountInformationHandler() {
        return this.m_accountInformationHandler;
    }
    
    public boolean hasSubscriptionRight(final SubscriptionRight right) {
        return this.getAccountInformationHandler() != null && this.getAccountInformationHandler().hasRight(right);
    }
    
    public boolean isInPrisonInstance() {
        return this.getCitizenComportment().getNation().getJailInstanceId() == this.getInstanceId();
    }
    
    public boolean isInGuild() {
        return this.m_guildHandler.getGuildId() > 0L;
    }
    
    @Override
    public long getGuildId() {
        return this.m_guildHandler.getGuildId();
    }
    
    @Override
    public int getGuildNationId() {
        return this.m_guildHandler.getNationId();
    }
    
    @Override
    public void playEmote(final int emoteId, @Nullable final HashMap<String, Object> vars, final boolean displayChatFeedback) {
        super.playEmote(emoteId, vars, displayChatFeedback);
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("playerId", this.getId());
        if (vars != null) {
            map.putAll(vars);
        }
        assert LuaManager.getInstance().getPath() != null;
        final String filename = String.format("emotes/%d%s", emoteId, LuaManager.getInstance().getExtension());
        LuaManager.getInstance().runScript(filename, PlayerCharacter.LIBRARIES, map, null, false);
    }
    
    public XpModification setXp(final long xp) {
        this.m_level = CharacterXpTable.getInstance().getLevelByXp(xp);
        return XpModification.NONE;
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    public void addCharacterXp(final long characterXp, final short levelGained) {
    }
    
    @Override
    public BonusPointCharacteristics getBonusPointCharacteristics() {
        return this.m_bonusPointCharacteristics;
    }
    
    public void updateXpFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "level", "levelValue");
        this.updateLocalXpFields();
    }
    
    protected void updateLocalXpFields() {
    }
    
    @Override
    public byte getActorTypeId() {
        return 0;
    }
    
    public int getCraftCharacteristicEfficiency(final CraftSkillType craftSkillType, final int craftId) {
        return this.m_skillCharacteristics.getCraftCharacteristicEfficiency(craftSkillType, craftId);
    }
    
    @Override
    public String toString() {
        return (this.getBreedInfo() != null) ? (super.toString() + " " + this.getBreedInfo().getName()) : super.toString();
    }
    
    @Override
    public void onSpecialFighterEvent(final SpecialEvent fightEventType) {
        super.onSpecialFighterEvent(fightEventType);
        if (this.m_breedSpecific != null) {
            this.m_breedSpecific.onSpecialFighterEvent(fightEventType);
        }
    }
    
    @Override
    public void onPropertyUpdated(final PropertyType prop) {
        if (prop == null) {
            return;
        }
        super.onPropertyUpdated(prop);
        if (prop.getPropertyTypeId() == 1) {
            switch ((FightPropertyType)prop) {
                case INVISIBLE_SUPERIOR:
                case INVISIBLE: {
                    this.updateAdditionalAppearance();
                    break;
                }
                case CANNOT_USE_ITEM_CAST: {
                    this.changeToSpellAttackIfNecessary();
                    break;
                }
            }
        }
    }
    
    @Override
    public CharacterInfo summonMonster(final long newMonsterId, final Point3 summonPos, final short monsterTypeId, final BasicInvocationCharacteristics charac, final boolean independant, final PropertyManager<FightPropertyType> fightProperties) {
        final CharacterInfo summoning = super.summonMonster(newMonsterId, summonPos, monsterTypeId, charac, independant, fightProperties);
        applyHairColor(this, summoning, true);
        applySkinColor(this, summoning, true);
        return summoning;
    }
    
    @Override
    public CharacterSpecificRangeDisplayer getSpecificRangeDisplayer() {
        if (this.m_breed != null && this.m_breed == AvatarBreed.XELOR) {
            return XelorSpecificRangeDisplayer.getInstance();
        }
        return super.getSpecificRangeDisplayer();
    }
    
    @Override
    public int getDeathParticleSystemId() {
        final float wakfuGauge = this.getWakfuGaugeValue();
        if (Math.abs(wakfuGauge) < 0.3f) {
            return super.getDeathParticleSystemId();
        }
        if (wakfuGauge < 0.0f) {
            return 900018;
        }
        return 900017;
    }
    
    @Override
    public int getResurrectionParticleSystemId() {
        final float wakfuGauge = this.getWakfuGaugeValue();
        if (Math.abs(wakfuGauge) < 0.3f) {
            return super.getResurrectionParticleSystemId();
        }
        if (wakfuGauge < 0.0f) {
            return 900021;
        }
        return 900020;
    }
    
    public BreedSpecific getBreedSpecific() {
        return this.m_breedSpecific;
    }
    
    public void setBreedSpecific(final BreedSpecific breedSpecific) {
        this.m_breedSpecific = breedSpecific;
    }
    
    @Override
    public boolean is(final CriterionUserType type) {
        return CriterionUserType.PLAYER_CHARACTER.is(type);
    }
    
    public static String getEquipmentBonus(final BasicCharacterInfo character, final Iterable<?> equipedItems) {
        final ArrayList<WakfuEffect> totalEffect = new ArrayList<WakfuEffect>();
        character.getRunningEffectManager();
        final TShortIntHashMap setBonusList = new TShortIntHashMap();
        final TIntHashSet itemCheckedForSet = new TIntHashSet();
        for (final Object o : equipedItems) {
            short setId = 0;
            Iterator<WakfuEffect> iteEffect;
            if (o instanceof Item) {
                final Item itemEquipment = (Item)o;
                if (!((ArrayInventoryWithoutCheck<Item, R>)character.getEquipmentInventory()).getContentChecker().checkCriterion(itemEquipment, character, character.getAppropriateContext())) {
                    continue;
                }
                final int refId = itemEquipment.getReferenceItem().getId();
                if (!itemCheckedForSet.contains(refId)) {
                    setId = itemEquipment.getReferenceItem().getSetId();
                    itemCheckedForSet.add(refId);
                }
                if (!itemEquipment.isActive()) {
                    continue;
                }
                iteEffect = itemEquipment.iterator();
            }
            else {
                if (!(o instanceof ReferenceItem)) {
                    continue;
                }
                final AbstractReferenceItem itemEquipment2 = (ReferenceItem)o;
                if (!itemCheckedForSet.contains(itemEquipment2.getId())) {
                    setId = itemEquipment2.getSetId();
                    itemCheckedForSet.add(itemEquipment2.getId());
                }
                iteEffect = (Iterator<WakfuEffect>)itemEquipment2.getEffectsIterator();
            }
            if (setId != 0) {
                setBonusList.put(setId, setBonusList.contains(setId) ? (setBonusList.get(setId) + 1) : 1);
            }
            while (iteEffect.hasNext()) {
                totalEffect.add(iteEffect.next());
            }
        }
        final TShortIntIterator iterator = setBonusList.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(iterator.key());
            final ArrayList<ItemSetLevel> itemSetLevels = itemSet.getEffectsToApplyByNbElements(iterator.value());
            for (int i = 0, size = itemSetLevels.size(); i < size; ++i) {
                final ItemSetLevel setLevel = itemSetLevels.get(i);
                for (final WakfuEffect effect : setLevel) {
                    totalEffect.add(effect);
                }
            }
        }
        final short itemBaseLevel = 0;
        final EffectDescription effectDescr = new EffectDescription(WakfuTranslator.getInstance(), totalEffect, (short)0);
        return effectDescr.createBonusDescription(character);
    }
    
    public PlayerCharacter getGraphicalCopy() {
        final LocalPlayerCharacter playerCharacter = new LocalPlayerCharacter();
        playerCharacter.setBreed(this.m_breed);
        playerCharacter.setSex(this.m_sex);
        playerCharacter.setName(this.getName());
        playerCharacter.setGfxId(this.m_gfxId);
        playerCharacter.setHairColorIndex(this.m_hairColorIndex, this.m_hairColorFactor, true);
        playerCharacter.setSkinColorIndex(this.m_skinColorIndex, this.m_skinColorFactor, true);
        playerCharacter.setPupilColorIndex(this.m_pupilColorIndex, true);
        playerCharacter.setFaceIndex(this.getFaceIndex(), true);
        playerCharacter.setClothIndex(this.getClothIndex(), true);
        playerCharacter.m_applyGuildVisual = this.m_applyGuildVisual;
        final ItemEquipment equipmentInventory = this.getEquipmentInventory();
        for (final Item item : equipmentInventory) {
            final AbstractItemType itemType = item.getReferenceItem().getItemType();
            for (final EquipmentPosition ep : itemType.getEquipmentPositions()) {
                try {
                    ((ArrayInventoryWithoutCheck<Item, R>)playerCharacter.getEquipmentInventory()).addAt(item, ep.getId());
                }
                catch (InventoryCapacityReachedException e) {
                    e.printStackTrace();
                }
                catch (ContentAlreadyPresentException e2) {
                    e2.printStackTrace();
                }
                catch (PositionAlreadyUsedException e3) {
                    e3.printStackTrace();
                }
            }
            for (final EquipmentPosition ep : itemType.getLinkedPositions()) {
                try {
                    ((ArrayInventoryWithoutCheck<Item, R>)playerCharacter.getEquipmentInventory()).addAt(item, ep.getId());
                }
                catch (InventoryCapacityReachedException e) {
                    e.printStackTrace();
                }
                catch (ContentAlreadyPresentException e2) {
                    e2.printStackTrace();
                }
                catch (PositionAlreadyUsedException e3) {
                    e3.printStackTrace();
                }
            }
        }
        playerCharacter.refreshDisplayEquipment();
        return playerCharacter;
    }
    
    @Override
    public AbstractMRUAction[] getMRUActions() {
        final ArrayList<AbstractMRUAction> mru = new ArrayList<AbstractMRUAction>(Arrays.asList(super.getMRUActions()));
        for (int i = 0; i < this.m_mruActions.size(); ++i) {
            mru.add(this.m_mruActions.get(i).getMRUAction());
        }
        return mru.toArray(new AbstractMRUAction[mru.size()]);
    }
    
    static {
        FIELDS = new String[] { "skinColorIndex", "hairColorIndex", "pupilColorIndex", "illustrationUrl", "characterChoiceIllustrationUrl", "setIndex", "hairDescription", "skinDescription", "pupilDescription", "currentTitleId", "currentTitle", "currentTitleObject", "hasGuild", "hasNation" };
        ALL_FIELDS = new String[PlayerCharacter.FIELDS.length + CharacterInfo.FIELDS.length];
        System.arraycopy(PlayerCharacter.FIELDS, 0, PlayerCharacter.ALL_FIELDS, 0, PlayerCharacter.FIELDS.length);
        System.arraycopy(CharacterInfo.FIELDS, 0, PlayerCharacter.ALL_FIELDS, PlayerCharacter.FIELDS.length, CharacterInfo.FIELDS.length);
        LIBRARIES = new JavaFunctionsLibrary[] { ParticleSystemFunctionsLibrary.getInstance(), MobileFunctionsLibrary.getInstance(), SoundFunctionsLibrary.getInstance() };
    }
    
    private final class PlayerCharacterPartAppearance extends CharacterInfoPart
    {
        private final CharacterSerializedAppearance m_part;
        
        PlayerCharacterPartAppearance(final CharacterSerializedAppearance part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.sex = PlayerCharacter.this.m_sex;
            this.m_part.skinColorIndex = PlayerCharacter.this.m_skinColorIndex;
            this.m_part.skinColorFactor = PlayerCharacter.this.m_skinColorFactor;
            this.m_part.hairColorIndex = PlayerCharacter.this.m_hairColorIndex;
            this.m_part.hairColorFactor = PlayerCharacter.this.m_hairColorFactor;
            this.m_part.pupilColorIndex = PlayerCharacter.this.m_pupilColorIndex;
            this.m_part.clothIndex = PlayerCharacter.this.getClothIndex();
            this.m_part.faceIndex = PlayerCharacter.this.getFaceIndex();
            this.m_part.currentTitle = PlayerCharacter.this.getCurrentTitle();
        }
        
        @Override
        public void onDataChanged() {
            for (final CharacterInfoPropertyEventsHandler handler : PlayerCharacter.this.m_characterInfoEventsHandler) {
                handler.onAppearanceChanged(PlayerCharacter.this);
            }
            PlayerCharacter.this.setSex(this.m_part.sex);
            PlayerCharacter.this.setSkinColorIndex(this.m_part.skinColorIndex, this.m_part.skinColorFactor, true);
            PlayerCharacter.this.setHairColorIndex(this.m_part.hairColorIndex, this.m_part.hairColorFactor, true);
            PlayerCharacter.this.setPupilColorIndex(this.m_part.pupilColorIndex, true);
            PlayerCharacter.this.beginRefreshDisplayEquipment();
            PlayerCharacter.this.setClothIndex(this.m_part.clothIndex, false);
            PlayerCharacter.this.setFaceIndex(this.m_part.faceIndex, false);
            PlayerCharacter.this.endRefreshDisplayEquipment();
            PlayerCharacter.this.setCurrentTitle(this.m_part.currentTitle);
        }
    }
    
    private final class PlayerCharacterPartEquipmentAppearance extends CharacterInfoPart
    {
        private final CharacterSerializedEquipmentAppearance m_part;
        
        PlayerCharacterPartEquipmentAppearance(final CharacterSerializedEquipmentAppearance part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.content.clear();
            for (final EquipmentPosition pos : EquipmentPosition.values()) {
                final Item item = ((ArrayInventoryWithoutCheck<Item, R>)PlayerCharacter.this.getEquipmentInventory()).getFromPosition(pos.m_id);
                if (item != null && item.isActive()) {
                    final CharacterSerializedEquipmentAppearance.EquipmentAppearance equipmentAppearance = new CharacterSerializedEquipmentAppearance.EquipmentAppearance();
                    equipmentAppearance.position = pos.m_id;
                    equipmentAppearance.referenceId = item.getReferenceId();
                    this.m_part.content.add(equipmentAppearance);
                }
            }
        }
        
        @Override
        public void onDataChanged() {
            try {
                if (PlayerCharacter.this.m_equipementAppearance == null) {
                    PlayerCharacter.this.m_equipementAppearance = new TByteIntHashMap();
                }
                else {
                    PlayerCharacter.this.m_equipementAppearance.clear();
                }
                for (final CharacterSerializedEquipmentAppearance.EquipmentAppearance equipmentAppearance : this.m_part.content) {
                    PlayerCharacter.this.m_equipementAppearance.put(equipmentAppearance.position, equipmentAppearance.referenceId);
                }
                PlayerCharacter.this.m_otherPlayerItemEffectsApplier.reloadItemsEffectsForPlayer();
                PlayerCharacter.this.refreshDisplayEquipment();
            }
            catch (Exception e) {
                PlayerCharacter.m_logger.error((Object)("Exception \u00ef¿½ la deserialisation de l'apparence d'un personnage id=" + PlayerCharacter.this.getId()), (Throwable)e);
            }
        }
    }
    
    private final class PlayerCharacterPartHp extends CharacterInfoPart
    {
        private final CharacterSerializedHp m_part;
        
        PlayerCharacterPartHp(final CharacterSerializedHp part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.hp = PlayerCharacter.this.getCharacteristicValue(FighterCharacteristicType.HP);
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).setMax(Integer.MAX_VALUE);
            PlayerCharacter.this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(this.m_part.hp);
        }
    }
    
    private final class PlayerCharacterPartXpCharacteristics extends CharacterInfoPart
    {
        private final CharacterSerializedXpCharacteristics m_part;
        
        PlayerCharacterPartXpCharacteristics(final CharacterSerializedXpCharacteristics part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"L'xp ne devrait pas \u00ef¿½tre s\u00ef¿½rialis\u00ef¿½e par le client.");
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.getBonusPointCharacteristics().fromRaw(this.m_part.bonusPointCharacteristics);
            PlayerCharacter.this.setWakfuGauge(this.m_part.wakfuGauge);
        }
    }
    
    private final class PlayerCharacterPartXp extends CharacterInfoPart
    {
        private final CharacterSerializedXp m_part;
        
        PlayerCharacterPartXp(final CharacterSerializedXp part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"L'xp ne devrait pas \u00ef¿½tre s\u00ef¿½rialis\u00ef¿½e par le client.");
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.setXp(this.m_part.xp);
        }
    }
    
    private final class PlayerCharacterPartCreationData extends CharacterInfoPart
    {
        private final CharacterSerializedCreationData m_part;
        
        PlayerCharacterPartCreationData(final CharacterSerializedCreationData part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            if (this.m_part.creationData == null) {
                this.m_part.creationData = new CharacterSerializedCreationData.CreationData();
            }
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.creationData != null) {
                PlayerCharacter.this.m_needsRecustom = this.m_part.creationData.needsRecustom;
                PlayerCharacter.this.m_recustomType = this.m_part.creationData.recustomValue;
            }
        }
    }
    
    private final class PlayerCharacterPartRunningEffects extends CharacterInfoPart
    {
        private final CharacterSerializedRunningEffects m_part;
        
        PlayerCharacterPartRunningEffects(final CharacterSerializedRunningEffects part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"Le client ne devrait pas s\u00ef¿½rialiser les running effects.");
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.getRunningEffectManager().clear();
            if (this.m_part.outFightData != null) {
                if (PlayerCharacter.this.m_initialized) {
                    PlayerCharacter.this.getRunningEffectManager().fromRawStateRunningEffects(this.m_part.outFightData.stateRunningEffects, PlayerCharacter.this.m_ownContext, PlayerCharacter.this);
                }
                else {
                    PlayerCharacter.this.setStateREToUnserializeAtInit(this.m_part.outFightData.stateRunningEffects);
                }
            }
        }
    }
    
    private final class PlayerCharacterPartCurrentMovementPath extends CharacterInfoPart
    {
        private final CharacterSerializedCurrentMovementPath m_part;
        
        PlayerCharacterPartCurrentMovementPath(final CharacterSerializedCurrentMovementPath part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"Le chemin en cours ne devrait pas \u00ef¿½tre envoy\u00ef¿½ par le client");
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.currentPath != null) {
                final ByteBuffer buffer = ByteBuffer.wrap(this.m_part.currentPath.encodedPath);
                final Direction8Path path = Direction8Path.decodeFromBuffer(buffer);
                if (path != null && PlayerCharacter.this.getActor() != null) {
                    PlayerCharacter.this.getActor().updateActorPath(path);
                }
            }
        }
    }
    
    private final class PlayerCharacterPartOccupation extends CharacterInfoPart
    {
        private final CharacterSerializedOccupation m_part;
        
        PlayerCharacterPartOccupation(final CharacterSerializedOccupation part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"L'occupation en cours ne devrait pas \u00ef¿½tre envoy\u00ef¿½e par le client");
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.occupation != null) {
                final short occupationId = this.m_part.occupation.occupationId;
                switch (occupationId) {
                    case 4: {
                        final DeadOccupation deadOccupation = new DeadOccupation(PlayerCharacter.this);
                        deadOccupation.build(this.m_part.occupation.occupationData);
                        deadOccupation.begin();
                        break;
                    }
                    case 20: {
                        final EmoteOccupation emoteOccupation = new EmoteOccupation(PlayerCharacter.this);
                        emoteOccupation.build(this.m_part.occupation.occupationData);
                        emoteOccupation.begin();
                        break;
                    }
                    case 1: {
                        final BasicOccupation restOccupation = new RestOccupation(PlayerCharacter.this);
                        restOccupation.begin();
                        break;
                    }
                    case 14: {
                        final BasicOccupation rideOccupation = new RideOccupation(PlayerCharacter.this);
                        rideOccupation.begin();
                        break;
                    }
                    case 16: {
                        final SitOccupation sitOccupation = new SitOccupation(PlayerCharacter.this);
                        sitOccupation.build(this.m_part.occupation.occupationData);
                        PlayerCharacter.this.setCurrentOccupation(sitOccupation);
                        break;
                    }
                    default: {
                        PlayerCharacter.m_logger.error((Object)("Occupation inconnue : id=" + occupationId));
                        break;
                    }
                }
            }
        }
    }
    
    private final class PlayerCharacterPartGuildRemoteInfo extends CharacterInfoPart
    {
        private final CharacterSerializedRemoteGuildInfo m_part;
        
        PlayerCharacterPartGuildRemoteInfo(final CharacterSerializedRemoteGuildInfo part) {
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
            final GuildRemoteInformationHandler handler = (GuildRemoteInformationHandler)PlayerCharacter.this.m_guildHandler;
            handler.setGuildId(this.m_part.guildId);
            handler.setBlazon(this.m_part.blazon);
            handler.setName(this.m_part.guildName);
            handler.setLevel(this.m_part.level);
            handler.setNationId(this.m_part.nationId);
        }
    }
    
    private final class PlayerCharacterPartNationId extends CharacterInfoPart
    {
        private final CharacterSerializedNationId m_part;
        
        PlayerCharacterPartNationId(final CharacterSerializedNationId part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"[NATION] Pas de s\u00ef¿½rialization de la nation ID dans le client pour l'instant", (Throwable)new UnsupportedOperationException());
        }
        
        @Override
        public void onDataChanged() {
            if (PlayerCharacter.this.getCitizenComportment().getNationId() != this.m_part.nationId) {
                final Nation newNation = NationManager.INSTANCE.getNationById(this.m_part.nationId);
                if (newNation != null) {
                    newNation.requestAddCitizen(PlayerCharacter.this);
                }
                else {
                    PlayerCharacter.m_logger.error((Object)("[NATION] On essaye d'ajouter le joueur " + PlayerCharacter.this.getId() + " \u00ef¿½ une nation inconnue du manager, nationId : " + this.m_part.nationId));
                }
            }
        }
    }
    
    private final class PlayerCharacterPartCharacterListNationId extends CharacterInfoPart
    {
        private final CharacterSerializedNationId m_part;
        
        PlayerCharacterPartCharacterListNationId(final CharacterSerializedNationId part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"[NATION] Pas de s\u00ef¿½rialization de la nation ID dans le client (SERVER => CLIENT)", (Throwable)new UnsupportedOperationException());
        }
        
        @Override
        public void onDataChanged() {
            LocalCharacterInfosManager.getInstance().addNationInformation(PlayerCharacter.this, this.m_part.nationId);
        }
    }
    
    private final class PlayerCharacterPartNationSynchro extends CharacterInfoPart
    {
        private final CharacterSerializedNationSynchro m_part;
        
        PlayerCharacterPartNationSynchro(final CharacterSerializedNationSynchro part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"[NATION] Pas de s\u00ef¿½rialization de la nation PUBLIC dans le client pour l'instant", (Throwable)new UnsupportedOperationException());
        }
        
        @Override
        public void onDataChanged() {
            final ClientCitizenComportment comportment = (ClientCitizenComportment)PlayerCharacter.this.getCitizenComportment();
            comportment.setRank(NationRank.getById(this.m_part.rank));
            comportment.setJobs(NationJob.fromLong(this.m_part.jobs));
            comportment.setVoteDate(GameDate.fromLong(this.m_part.voteDate));
            comportment.setGovernmentOpinion(GovernmentOpinion.fromId(this.m_part.governmentOpinion));
            comportment.setCandidate(this.m_part.isCandidate);
            comportment.setPvpState(NationPvpState.getFromId(this.m_part.pvpState));
            comportment.setPvpDate(GameDate.fromLong(this.m_part.pvpDate));
            comportment.setPvpRank(NationPvpRanks.getById(this.m_part.pvpRank));
            comportment.updatePvp(false);
        }
    }
    
    private final class PlayerCharacterPartNationPvp extends CharacterInfoPart
    {
        private final CharacterSerializedNationPvp m_part;
        
        PlayerCharacterPartNationPvp(final CharacterSerializedNationPvp part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"[NATION] Pas de s\u00ef¿½rialization de la nation PUBLIC dans le client pour l'instant", (Throwable)new UnsupportedOperationException());
        }
        
        @Override
        public void onDataChanged() {
            final ClientCitizenComportment comportment = (ClientCitizenComportment)PlayerCharacter.this.getCitizenComportment();
            final NationPvpState previousState = comportment.getPvpState();
            comportment.setPvpState(NationPvpState.getFromId(this.m_part.pvpState));
            comportment.setPvpDate(GameDate.fromLong(this.m_part.pvpDate));
            comportment.setPvpRank(NationPvpRanks.getById(this.m_part.pvpRank));
            comportment.setPvpMoneyAmount(this.m_part.pvpMoneyAmount);
            comportment.setDailyPvpMoneyAmount(this.m_part.dailyPvpMoneyAmount);
            comportment.updatePvp(previousState != comportment.getPvpState());
        }
    }
    
    private final class PlayerCharacterPartNationCitizenScore extends CharacterInfoPart
    {
        private final CharacterSerializedNationCitizenScore m_part;
        private final TIntArrayList m_citizenPointsNations;
        
        PlayerCharacterPartNationCitizenScore(final CharacterSerializedNationCitizenScore part) {
            super();
            this.m_citizenPointsNations = new TIntArrayList();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("[NATION] La Part NationEnemy ne devrait pas \u00ef¿½tre s\u00ef¿½rialis\u00ef¿½e par le client.");
        }
        
        @Override
        public void onDataChanged() {
            final ClientCitizenComportment comportment = (ClientCitizenComportment)PlayerCharacter.this.getCitizenComportment();
            final ArrayList<CharacterSerializedNationCitizenScore.NationCitizenScoreInfo> nations = this.m_part.nationCitizenScores;
            this.m_citizenPointsNations.add(comportment.getHasCitizenScoreNations());
            for (int i = 0, size = nations.size(); i < size; ++i) {
                final CharacterSerializedNationCitizenScore.NationCitizenScoreInfo nationEnemy = nations.get(i);
                final int nationId = nationEnemy.nationId;
                final int value = nationEnemy.citizenScore;
                final int old = comportment.setCitizenScore(nationId, value);
                TroveUtils.removeFirstValue(this.m_citizenPointsNations, nationId);
                if (old != value) {
                    comportment.onCitizenScoreChanged(nationId, old);
                }
            }
            for (int i = 0, size = this.m_citizenPointsNations.size(); i < size; ++i) {
                final int nationId2 = this.m_citizenPointsNations.get(i);
                final int old2 = comportment.setCitizenScore(nationId2, 0);
                comportment.onCitizenScoreChanged(nationId2, old2);
            }
            this.m_citizenPointsNations.clear();
            final ArrayList<CharacterSerializedNationCitizenScore.OffendedNations> offendedNations = this.m_part.offendedNations;
            final TIntHash pastOffendedNations = new TIntHashSet(comportment.getOffendedNations().toArray());
            comportment.resetOffendedNations();
            for (int j = 0, n = offendedNations.size(); j < n; ++j) {
                final CharacterSerializedNationCitizenScore.OffendedNations offendedNation = offendedNations.get(j);
                final int nationId3 = offendedNation.offendedNationId;
                if (!pastOffendedNations.contains(nationId3)) {
                    this.notification(nationId3);
                }
                comportment.addOffendedNation(NationManager.INSTANCE.getNationById(nationId3));
            }
            comportment.updateAppearance();
        }
        
        private void notification(final int nationId) {
            if (WakfuGameEntity.getInstance().getLocalPlayer() != PlayerCharacter.this) {
                return;
            }
            final String title = WakfuTranslator.getInstance().getString("notification.outlawTitle");
            final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.outlawText", WakfuTranslator.getInstance().getString(39, nationId, new Object[0])), NotificationMessageType.OUTLAW, nationId + "");
            final Message uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.OUTLAW);
            Worker.getInstance().pushMessage(uiNotificationMessage);
            final String nationName = new TextWidgetFormater().b().addColor("cc4444").append(WakfuTranslator.getInstance().getString(39, nationId, new Object[0]))._b().finishAndToString();
            final String textMessage = WakfuTranslator.getInstance().getString("nation.chatEnemy", nationName);
            ChatManager.getInstance().pushMessage(textMessage, 4);
        }
    }
    
    private final class PlayerCharacterPartPasseportInfo extends CharacterInfoPart
    {
        private final CharacterSerializedPasseportInfo m_part;
        
        PlayerCharacterPartPasseportInfo(final CharacterSerializedPasseportInfo part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("[NATION] La part ne devrait pas \u00ef¿½tre s\u00ef¿½rialis\u00ef¿½e par le client.");
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.getCitizenComportment().setPasseportActive(this.m_part.isPasseportActive);
        }
    }
    
    private final class PlayerCharacterPartRemoteAccountInformation extends CharacterInfoPart
    {
        private final CharacterSerializedRemoteAccountInformation m_part;
        
        PlayerCharacterPartRemoteAccountInformation(final CharacterSerializedRemoteAccountInformation part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Les informations de compte de sont pas s\u00ef¿½rialis\u00ef¿½s par le client");
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.m_accountInformationHandler.setSubscriptionLevel(SubscriptionLevel.fromId(this.m_part.subscriptionLevel));
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (PlayerCharacter.this == localPlayer) {
                final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
                if (localAccount != null) {
                    localAccount.setSubscriptionLevel(this.m_part.subscriptionLevel);
                }
                PlayerCharacter.this.reloadSubscriptionState();
            }
            try {
                SubscriptionEmoteAndTitleLimitations.resetCurrentTitleIfNecessary(PlayerCharacter.this, PlayerCharacter.this.m_accountInformationHandler.getActiveSubscriptionLevel());
            }
            catch (Exception e) {
                PlayerCharacter.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
            PlayerCharacter.this.m_accountInformationHandler.getAdditionalRights().clear();
            if (this.m_part.additionalRights == null) {
                return;
            }
            for (int i = 0; i < this.m_part.additionalRights.length; ++i) {
                final int right = this.m_part.additionalRights[i];
                PlayerCharacter.this.m_accountInformationHandler.addSubscriptionRight(SubscriptionRight.fromId(right));
            }
        }
    }
    
    private final class PlayerCharacterPartSocialStates extends CharacterInfoPart
    {
        private final CharacterSerializedSocialStates m_part;
        
        PlayerCharacterPartSocialStates(final CharacterSerializedSocialStates part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("[SOCIAL] La Part SocialStates ne devrait pas \u00ef¿½tre s\u00ef¿½rialis\u00ef¿½e par le client.");
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.m_afkStateActive = this.m_part.afkState;
            PlayerCharacter.this.m_dndStateActive = this.m_part.dndState;
        }
    }
    
    private final class PlayerCharacterPartAchievements extends CharacterInfoPart
    {
        private final CharacterSerializedAchievements m_part;
        
        private PlayerCharacterPartAchievements(final CharacterSerializedAchievements part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            PlayerCharacter.m_logger.error((Object)"Les achievements ne sont pas serialis\u00c3©s par le client");
        }
        
        @Override
        public void onDataChanged() {
            final ClientAchievementsContext context = AchievementsModel.INSTANCE.createContext();
            context.unserialize(this.m_part.serializedAchievementsContext);
            context.setEventListener(new LocalPlayerCharacterAchievementsListener(PlayerCharacter.this.m_id, context));
            AchievementContextManager.INSTANCE.registerContext(PlayerCharacter.this.getId(), context);
            this.m_part.serializedAchievementsContext = null;
        }
    }
    
    private final class PlayerCharacterPartPet extends CharacterInfoPart
    {
        private final CharacterSerializedPet m_part;
        
        PlayerCharacterPartPet(final CharacterSerializedPet part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de serialisation dans le client (Server->Client only)");
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.mount != null) {
                PlayerCharacter.this.createMountMobileView(this.m_part.mount.definitionId, this.m_part.mount.colorRefItemId, this.m_part.mount.equippedRefItemId, this.m_part.mount.sleepRefItemId, this.m_part.mount.health);
            }
            else {
                PlayerCharacter.this.removeMountMobile();
            }
            if (this.m_part.pet != null) {
                PlayerCharacter.this.createPetMobileView(this.m_part.pet.definitionId, this.m_part.pet.colorRefItemId, this.m_part.pet.equippedRefItemId, this.m_part.pet.sleepRefItemId, this.m_part.pet.health);
            }
            else {
                PlayerCharacter.this.removePetMobile();
            }
        }
    }
    
    private final class PlayerCharacterPartVisibility extends CharacterInfoPart
    {
        private final CharacterSerializedVisibility m_part;
        
        PlayerCharacterPartVisibility(final CharacterSerializedVisibility part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de serialisation dans le client (Server->Client only)");
        }
        
        @Override
        public void onDataChanged() {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (PlayerCharacter.this == localPlayer) {
                if (this.m_part.visible) {
                    PlayerCharacter.this.getActor().setAlpha(PlayerCharacter.this.getActor().getOriginalAlpha());
                }
                else {
                    PlayerCharacter.this.getActor().setDesiredAlpha(0.5f);
                }
            }
            else {
                PlayerCharacter.this.getActor().setVisible(this.m_part.visible);
            }
        }
    }
    
    private final class PlayerCharacterPartCharacteristics extends CharacterInfoPart
    {
        private final CharacterSerializedCharacteristics m_part;
        
        PlayerCharacterPartCharacteristics(final CharacterSerializedCharacteristics part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            throw new UnsupportedOperationException("Pas de serialisation dans le client (Server->Client only)");
        }
        
        @Override
        public void onDataChanged() {
            PlayerCharacter.this.getCharacteristics().fromRaw(this.m_part.characteristics);
        }
    }
}
