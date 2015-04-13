package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import java.awt.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.io.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.ia.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public abstract class CharacterInfo extends BasicCharacterInfo implements StateAffinitiesUpdateListener, Releasable, CharacteristicUpdateListener, FieldProvider, MRUable, InteractiveElementActionRunner, InteractiveElementUser, MobileProvider
{
    public static final int LEVEL_GAINED_PARTICLE_SYSTEM_ID = 999999;
    public static final int WAKFU_GAINED_PARTICLE_SYSTEM_ID = 800067;
    public static final int STASIS_GAINED_PARTICLE_SYSTEM_ID = 800068;
    public static final String SMILEY_ANIM_PREFIX = "Smiley_";
    public static final String NAME_FIELD = "name";
    public static final String SEX_FIELD = "sex";
    public static final String BREED_INFO_FIELD = "breedInfo";
    public static final String BREED_ID_FIELD = "breedId";
    public static final String SMILEY_DESCRIPTOR_LIBRARY_FIELD = "smileyDescriptorLibrary";
    public static final String ACTOR_DESCRIPTOR_LIBRARY_FIELD = "actorDescriptorLibrary";
    public static final String ACTOR_ANIMATION_NAME_FIELD = "actorAnimationName";
    public static final String ACTOR_ANIMATION_PATH_FIELD = "actorAnimationPath";
    public static final String ACTOR_LINKAGE_FIELD = "actorLinkage";
    public static final String ACTOR_ANIMATION_FIELD = "actorAnimation";
    public static final String ACTOR_EQUIPMENT_FIELD = "actorEquipment";
    public static final String ACTOR_FIELD = "actor";
    public static final String DIRECTION_FIELD = "direction";
    public static final String AVATAR_URL_FIELD = "avatarUrl";
    public static final String LEVEL_FIELD = "level";
    public static final String REROLL_XP_BONUS = "rerollXpBonus";
    public static final String REROLL_XP_BONUS_DESC = "rerollXpBonusDescription";
    public static final String HAS_REROLL_XP_BONUS = "hasRerollXpBonus";
    public static final String NAME_AND_LEVEL_FIELD = "nameAndLevel";
    public static final String LEVEL_VALUE_FIELD = "levelValue";
    public static final String COMPANION_REAL_LEVEL_FIELD = "companionRealLevel";
    public static final String ID_FIELD = "id";
    public static final String TIMELINE_DESCRIPTION_FIELD = "timelineDescription";
    public static final String DEBUFF_AP_VISIBLE_FIELD = "debuffApVisible";
    public static final String DEBUFF_MP_VISIBLE_FIELD = "debuffMpVisible";
    public static final String STATES_FIELD = "states";
    public static final String PROTECTOR_STATES_FIELD = "protectorStates";
    public static final String ALL_STATES_FIELD = "allStates";
    public static final String SPELLS_INVENTORY_FIELD = "spellsInventory";
    public static final String TIMELINE_POSITION_FIELD = "timelinePosition";
    public static final String CHARACTERISTICS = "characteristics";
    public static final String FIGHT_STATE_BAR_DISPLAYED_FIELD = "isFightStateBarDisplayed";
    public static final String IS_KO_FIELD = "isKO";
    public static final String SKIN_COLORS_FIELD = "skinColors";
    public static final String HAIR_COLORS_FIELD = "hairColors";
    public static final String PUPIL_COLORS_FIELD = "pupilColors";
    public static final String SECONDARY_SKIN_COLORS_FIELD = "secondarySkinColors";
    public static final String SECONDARY_HAIR_COLORS_FIELD = "secondaryHairColors";
    public static final String LEADER_SHIP_COST_FIELD = "leaderShipCost";
    public static final String STATE_AFFINITIES = "stateAffinities";
    public static final String VISIBLE = "visible";
    public static final String HEAL_RESIST_DESCRIPTION = "healResistDescription";
    public static final String[] FIELDS;
    public static final String HAIR_PART_NAME = "CHEVEUXCUSTOM";
    public static final String DRESS_PART_NAME = "VETEMENTCUSTOM";
    public CharacterActor m_actor;
    private final ArrayList<MRUActions> m_mruActions;
    private static final MovementSelector PROTECTOR_MOVEMENT_SELECTOR;
    private boolean m_isFightStateBarDisplayed;
    private AnimatedInteractiveElement m_currentInteractiveElement;
    private Fight m_currentFight;
    private int m_currentFightId;
    private ActorSkillEndMovementListener m_skillEndMovementListener;
    private DebugFightAccessSquareMessage m_debugMessage;
    private final RunningEffectFieldProviderManager m_refpManager;
    private ExternalFightInfo m_currentExternalFightInfo;
    private FighterFieldProvider m_fighterFieldProvider;
    private final HashSet<CharacterInfo> m_fighters;
    protected ArrayList<RunningEffectFieldProvider> m_statesCache;
    protected ArrayList<RunningEffectFieldProvider> m_protectorStatesCache;
    protected ArrayList<RunningEffectFieldProvider> m_allStatesCache;
    protected final StateAffinitiesView m_stateAffinitiesView;
    protected CharacterAdditionalAppearance m_additionalAppearance;
    protected CharacteristicViewProvider m_characteristicViewProvider;
    private boolean m_spawnInWorld;
    private boolean m_spawnInMyFight;
    private AnimatedElementWithDirection m_smiley;
    private boolean m_waitEndAnimationToBeDespawned;
    private byte m_clothIndex;
    private byte m_faceIndex;
    private MovementSelector m_beforeCarryMovementSelector;
    private CharacterInfoManagerListener m_listenerForSummonInitialize;
    private byte m_beginRefreshDisplayEquipement;
    protected ApplyGuildVisual m_applyGuildVisual;
    private boolean m_addActorToManager;
    private TByteIntHashMap m_equimentAppearance;
    
    protected CharacterInfo() {
        super();
        this.m_mruActions = new ArrayList<MRUActions>();
        this.m_currentFightId = -1;
        this.m_refpManager = new RunningEffectFieldProviderManager();
        this.m_fighters = new HashSet<CharacterInfo>();
        this.m_clothIndex = -1;
        this.m_faceIndex = -1;
        this.m_addActorToManager = true;
        this.m_fighters.clear();
        this.m_fighters.add(this);
        this.m_ownContext.setListener(new UniqueEffectUserExecutionListener(this));
        this.m_stateAffinitiesView = new StateAffinitiesView();
    }
    
    public void removeAdditionalAppearance() {
        if (this.m_additionalAppearance != null) {
            this.m_additionalAppearance.unApplyParticle();
        }
    }
    
    public void updateAdditionalAppearance() {
        if (this.m_additionalAppearance != null) {
            this.m_additionalAppearance.updateAdditionalAppearance();
        }
    }
    
    public void copyAdditionalAppearance(final CharacterAdditionalAppearance appearanceToCopy) {
        if (this.m_additionalAppearance == null) {
            this.m_additionalAppearance = new CharacterAdditionalAppearance(this);
        }
        this.m_additionalAppearance.copyFrom(appearanceToCopy);
    }
    
    public CharacterAdditionalAppearance getAdditionalAppearance() {
        return this.m_additionalAppearance;
    }
    
    public boolean isSpawnInWorld() {
        return this.m_spawnInWorld;
    }
    
    public void setSpawnInWorld(final boolean spawnInWorld) {
        this.m_spawnInWorld = spawnInWorld;
    }
    
    public boolean isSpawnInMyFight() {
        return this.m_spawnInMyFight;
    }
    
    public void setSpawnInMyFight(final boolean spawnInMyFight) {
        this.m_spawnInMyFight = spawnInMyFight;
    }
    
    @Override
    public void excludeFromFight() {
        super.excludeFromFight();
        if (this.getCurrentFight() != null) {
            this.despawnFromFight(this, this.getCurrentFight().getFighters());
        }
    }
    
    public void despawnFromFight(final CharacterInfo characterInfo, final Collection<CharacterInfo> fighters) {
        if (characterInfo == WakfuGameEntity.getInstance().getLocalPlayer()) {
            for (final CharacterInfo fighter : fighters) {
                NetActorsFrame.getInstance().despawnActor(fighter.getId(), fighter.getActorTypeId(), true, false);
            }
        }
        else {
            NetActorsFrame.getInstance().despawnActor(characterInfo.getId(), characterInfo.getActorTypeId(), true, false);
        }
    }
    
    @Override
    public String toString() {
        return this.getControllerName();
    }
    
    @Override
    public long getClientId() {
        return 0L;
    }
    
    public BinarSerialPart[] getSharedDatasParts() {
        return new BinarSerialPart[] { BinarSerialPart.EMPTY };
    }
    
    @Override
    public AbstractBreedManager getBreedManager() {
        return PlayerCharacterBreedManager.getInstance();
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("nameAndLevel")) {
            return this.getNameAndLevel(this.getName());
        }
        if (fieldName.equals("sex")) {
            return this.getSex();
        }
        if (fieldName.equals("breedInfo")) {
            return this.getBreedInfo();
        }
        if (fieldName.equals("breedId")) {
            return this.m_breed.getBreedId();
        }
        if (fieldName.equals("smileyDescriptorLibrary")) {
            return this.getSmiley();
        }
        if (fieldName.equals("actorDescriptorLibrary")) {
            return this.getActor();
        }
        if (fieldName.equals("actorAnimationName")) {
            return "AnimStatique";
        }
        if (fieldName.equals("actorAnimationPath")) {
            return this.getActor().getPath();
        }
        if (fieldName.equals("actorLinkage")) {
            return this.getActor().getDisplayObjectLinkage();
        }
        if (fieldName.equals("actorAnimation")) {
            return this.getActor().getAnimation();
        }
        if (fieldName.equals("actorEquipment")) {
            return this.getActor().getAnmInstance();
        }
        if (fieldName.equals("actor")) {
            return this.getActor();
        }
        if (fieldName.equals("id")) {
            return this.m_id;
        }
        if (fieldName.equals("levelValue")) {
            return this.getLevel();
        }
        if (fieldName.equals("level")) {
            return this.getLevelShortDescription();
        }
        if (fieldName.equals("rerollXpBonus")) {
            return this.getXpRerollBonusText();
        }
        if (fieldName.equals("rerollXpBonusDescription")) {
            return this.getXpRerollBonusDescription();
        }
        if (fieldName.equals("hasRerollXpBonus")) {
            return this.hasRerollXpBonus();
        }
        if (fieldName.equals("timelineDescription")) {
            return this.getTimelineDescription(this);
        }
        if (fieldName.equals("leaderShipCost")) {
            return (this.getType() == 1) ? (this.isActiveProperty(FightPropertyType.CAN_BE_SEDUCED) ? MonsterBreedManager.getInstance().getBreedFromId(this.getBreedId()).getRequiredLeadershipPoints() : -1) : null;
        }
        if (fieldName.equals("characteristics")) {
            return this.m_characteristicViewProvider;
        }
        if (fieldName.equals("debuffApVisible")) {
            final int pow = this.getCharacteristicValue(FighterCharacteristicType.AP_DEBUFF_POWER);
            final int res = this.getCharacteristicValue(FighterCharacteristicType.AP_DEBUFF_RES);
            return pow != 0 && res != 0;
        }
        if (fieldName.equals("debuffMpVisible")) {
            final int pow = this.getCharacteristicValue(FighterCharacteristicType.MP_DEBUFF_POWER);
            final int res = this.getCharacteristicValue(FighterCharacteristicType.MP_DEBUFF_RES);
            return pow != 0 && res != 0;
        }
        if (fieldName.equals("states")) {
            return this.getStatesField();
        }
        if (fieldName.equals("protectorStates")) {
            return this.getProtectorStatesField();
        }
        if (fieldName.equals("allStates")) {
            return this.getAllStatesField();
        }
        if (fieldName.equals("stateAffinities")) {
            return this.m_stateAffinitiesView;
        }
        if (fieldName.equals("timelinePosition")) {
            final Fight fight = this.getCurrentFight();
            if (fight == null) {
                return null;
            }
            final int count = fight.getInPlayFightersCount();
            final int position = fight.getTimeline().getFighterPosition(this.getId());
            final int currentPosition = Math.max(0, fight.getTimeline().getCurrentFighterPosition());
            if (position == -1) {
                return null;
            }
            final int pos = (position - currentPosition + count) % count;
            if (pos == -1) {
                return null;
            }
            return pos + 1;
        }
        else {
            if (fieldName.equals("isFightStateBarDisplayed")) {
                return this.m_isFightStateBarDisplayed;
            }
            if (fieldName.equals("direction")) {
                return this.getDirection().m_index;
            }
            if (fieldName.equals("isKO")) {
                return this.isOffPlay();
            }
            if (fieldName.equals("spellsInventory")) {
                return this.getSpellInventoryManager();
            }
            if (fieldName.equals("skinColors")) {
                return this.getSkinColors();
            }
            if (fieldName.equals("hairColors")) {
                return this.getHairColors();
            }
            if (fieldName.equals("pupilColors")) {
                return this.getPupilColors();
            }
            if (fieldName.equals("visible")) {
                return this.getActor().isVisible();
            }
            if (fieldName.equals("healResistDescription")) {
                return (this.getFinalHealResist() > 0.0f) ? WakfuTranslator.getInstance().getString("desc.healRes", (int)this.getFinalHealResist()) : null;
            }
            if (this.m_fighterFieldProvider != null) {
                return this.m_fighterFieldProvider.getFieldValue(fieldName);
            }
            return null;
        }
    }
    
    private String getXpRerollBonusDescription() {
        String key;
        if (this.getBreedId() == AvatarBreed.SOUL.getBreedId()) {
            key = "rerollXp.soul.description";
        }
        else {
            final SubscriptionLevel subscriptionLevel = SubscriptionLevel.fromId(WakfuGameEntity.getInstance().getLocalAccount().getSubscriptionLevel());
            if (subscriptionLevel.hasRight(SubscriptionRight.REROLL_BONUS_XP)) {
                key = "rerollXp.info.hasRight";
            }
            else {
                key = "rerollXp.info.notRight";
            }
        }
        return WakfuTranslator.getInstance().getString(key);
    }
    
    private boolean hasRerollXpBonus() {
        if (this.getBreedId() == AvatarBreed.SOUL.getBreedId()) {
            return false;
        }
        final SubscriptionLevel subscriptionLevel = SubscriptionLevel.fromId(WakfuGameEntity.getInstance().getLocalAccount().getSubscriptionLevel());
        return subscriptionLevel.hasRight(SubscriptionRight.REROLL_BONUS_XP);
    }
    
    private String getXpRerollBonusText() {
        return WakfuTranslator.getInstance().getString("rerollXp.factor", (int)this.getXpRerollBonus());
    }
    
    public float getXpRerollBonus() {
        final long ownerId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
        final ArrayList<CharacterInfo> characterInfos = new ArrayList<CharacterInfo>();
        HeroesManager.INSTANCE.forEachHero(ownerId, new TObjectProcedure<BasicCharacterInfo>() {
            @Override
            public boolean execute(final BasicCharacterInfo characterInfo) {
                characterInfos.add(characterInfo);
                return true;
            }
        });
        final TShortArrayList charactersLevel = new TShortArrayList();
        for (final CharacterInfo characterInfo : characterInfos) {
            charactersLevel.add(characterInfo.getLevel());
        }
        float rerollXpFactor;
        if (this.getBreedId() == AvatarBreed.SOUL.getBreedId()) {
            rerollXpFactor = 1.0f;
        }
        else {
            rerollXpFactor = RerollXpFactorComputer.getRerollXpFactor(this.getLevel(), charactersLevel);
        }
        return rerollXpFactor;
    }
    
    public String getNameAndLevel(final String name) {
        return new TextWidgetFormater().append(name).openText().addColor(Color.GOLD.getRGBtoHex()).append(" ").append(this.getLevelShortDescription()).finishAndToString();
    }
    
    public String getTimelineDescription(final CharacterInfo controlledCharacter) {
        return WakfuTranslator.getInstance().getString("fight.timeline.fighterDescription", controlledCharacter.getName(), this.getLevelShortDescription(), this.getHpDescription(controlledCharacter), this.getApDescription(controlledCharacter), this.getMpDescription(controlledCharacter), this.getWpDescription(controlledCharacter), this.getInitDescription(controlledCharacter));
    }
    
    private Object getLevelShortDescription() {
        return WakfuTranslator.getInstance().getString("levelShort.custom", this.getLevel());
    }
    
    public String getWpDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        try {
            sb.addImage(WakfuTextImageProvider._getIconUrl((byte)5), 16, 16, null).append(" ");
        }
        catch (PropertyException e) {
            CharacterInfo.m_logger.warn((Object)e.getMessage());
            sb.append(WakfuTranslator.getInstance().getString("WPShort")).append(" : ");
        }
        sb.append(controlledCharacter.getCharacteristicValue(FighterCharacteristicType.WP)).append("/").append(controlledCharacter.getCharacteristicMax(FighterCharacteristicType.WP));
        return sb.finishAndToString();
    }
    
    public String getWpMinMaxDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(controlledCharacter.getCharacteristicValue(FighterCharacteristicType.WP)).append("/").append(controlledCharacter.getCharacteristicMax(FighterCharacteristicType.WP));
        return sb.finishAndToString();
    }
    
    public String getApDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        try {
            sb.addImage(WakfuTextImageProvider._getIconUrl((byte)1), 16, 16, null).append(" ");
        }
        catch (PropertyException e) {
            CharacterInfo.m_logger.warn((Object)e.getMessage());
            sb.append(WakfuTranslator.getInstance().getString("APShort")).append(" : ");
        }
        sb.append(controlledCharacter.getCharacteristicValue(FighterCharacteristicType.AP)).append("/").append(controlledCharacter.getCharacteristicMax(FighterCharacteristicType.AP));
        return sb.finishAndToString();
    }
    
    public String getApMinMaxDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(controlledCharacter.getCharacteristicValue(FighterCharacteristicType.AP)).append("/").append(controlledCharacter.getCharacteristicMax(FighterCharacteristicType.AP));
        return sb.finishAndToString();
    }
    
    public String getMpDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        try {
            sb.addImage(WakfuTextImageProvider._getIconUrl((byte)2), 16, 16, null).append(" ");
        }
        catch (PropertyException e) {
            CharacterInfo.m_logger.warn((Object)e.getMessage());
            sb.append(WakfuTranslator.getInstance().getString("MPShort")).append(" : ");
        }
        sb.append(controlledCharacter.getCharacteristicValue(FighterCharacteristicType.MP)).append("/").append(controlledCharacter.getCharacteristicMax(FighterCharacteristicType.MP));
        return sb.finishAndToString();
    }
    
    public String getMpMinMaxDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(controlledCharacter.getCharacteristicValue(FighterCharacteristicType.MP)).append("/").append(controlledCharacter.getCharacteristicMax(FighterCharacteristicType.MP));
        return sb.finishAndToString();
    }
    
    public Object getHpDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(this.m_characteristicViewProvider.getCharacteristicView(FighterCharacteristicType.HP).getValueDescription());
        if (this.getCharacteristicMax(FighterCharacteristicType.VIRTUAL_HP) != 0) {
            sb.newLine().append(this.m_characteristicViewProvider.getCharacteristicView(FighterCharacteristicType.VIRTUAL_HP).getValueDescription());
        }
        return sb.finishAndToString();
    }
    
    public Object getInitDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(WakfuTranslator.getInstance().getString("INITShort")).append(" : ");
        sb.append(controlledCharacter.getCharacteristicValue(FighterCharacteristicType.INIT));
        return sb.finishAndToString();
    }
    
    public Object getResistDescription(final CharacterInfo controlledCharacter) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final int resAll = controlledCharacter.getCharacteristicValue(FighterCharacteristicType.RES_IN_PERCENT);
        sb.append("Res.F").append(" : ");
        int value = controlledCharacter.getCharacteristicValue(FighterCharacteristicType.RES_FIRE_PERCENT) + resAll;
        sb.append(value).append("% ");
        sb.append("Res.E").append(" : ");
        value = controlledCharacter.getCharacteristicValue(FighterCharacteristicType.RES_WATER_PERCENT) + resAll;
        sb.append(value).append("% ");
        sb.append("Res.T").append(" : ");
        value = controlledCharacter.getCharacteristicValue(FighterCharacteristicType.RES_EARTH_PERCENT) + resAll;
        sb.append(value).append("% ");
        sb.append("Res.V").append(" : ");
        value = controlledCharacter.getCharacteristicValue(FighterCharacteristicType.RES_AIR_PERCENT) + resAll;
        sb.append(value).append("%");
        return sb.finishAndToString();
    }
    
    public String getStatesIconDescription() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        ArrayList<RunningEffectFieldProvider> list = this.getStatesField();
        if (list != null && list.size() > 0) {
            for (int i = 0, size = list.size(); i < size; ++i) {
                final RunningEffectFieldProvider re = list.get(i);
                sb.addImage(re.getIconUrl(), 16, 16, null);
            }
        }
        list = this.getProtectorStatesField();
        if (list != null && list.size() > 0) {
            for (int i = 0, size = list.size(); i < size; ++i) {
                final RunningEffectFieldProvider re = list.get(i);
                sb.addImage(re.getIconUrl(), 16, 16, null);
            }
        }
        return (sb.length() > 0) ? sb.finishAndToString() : null;
    }
    
    protected void computeStatesField() {
        if (this.m_statesCache == null || this.m_protectorStatesCache == null || this.m_allStatesCache == null) {
            this.m_protectorStatesCache = new ArrayList<RunningEffectFieldProvider>();
            this.m_statesCache = new ArrayList<RunningEffectFieldProvider>();
            this.m_allStatesCache = new ArrayList<RunningEffectFieldProvider>();
            for (final RunningEffect re : this.getRunningEffectManager()) {
                if (this.canDisplayEffectInStateBar(re)) {
                    final RunningEffectFieldProvider ref = this.m_refpManager.getRunningEffectProvider(re, -1L);
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
    
    protected void sortStates() {
        Collections.sort(this.m_statesCache);
        Collections.sort(this.m_allStatesCache);
        Collections.sort(this.m_protectorStatesCache);
    }
    
    private ArrayList<RunningEffectFieldProvider> getAllStatesField() {
        if (HoodedMonsterFightEventListener.isVisuallyHooded(this)) {
            return null;
        }
        if (this.m_allStatesCache == null) {
            this.computeStatesField();
        }
        return this.m_allStatesCache.isEmpty() ? null : this.m_allStatesCache;
    }
    
    public ArrayList<RunningEffectFieldProvider> getProtectorStatesField() {
        if (this.m_protectorStatesCache == null) {
            this.computeStatesField();
        }
        return this.m_protectorStatesCache.isEmpty() ? null : this.m_protectorStatesCache;
    }
    
    private ArrayList<RunningEffectFieldProvider> getStatesField() {
        if (this.m_statesCache == null) {
            this.computeStatesField();
        }
        return this.m_statesCache.isEmpty() ? null : this.m_statesCache;
    }
    
    protected final boolean canDisplayEffectInStateBar(final RunningEffect re) {
        if (re.getEffectContainer() == null) {
            return false;
        }
        if (re.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
            return false;
        }
        if (re.getId() == RunningEffectConstants.SET_CADRAN.getId()) {
            return false;
        }
        if (re instanceof SetEffectArea) {
            return false;
        }
        if (re.getEffectContainer() != null && re.getEffectContainer().getContainerType() == 1) {
            final StateClient state = re.getEffectContainer();
            return state.isShownInTimeline();
        }
        final int containerType = re.getEffectContainer().getContainerType();
        return containerType == 16 || containerType == 18 || containerType == 26 || containerType == 28 || containerType == 34 || containerType == 30 || containerType == 32 || containerType == 33 || containerType == 19 || (this.isOnFight() && ((containerType == 12 && re.getGenericEffect() != null && ((WakfuEffect)re.getGenericEffect()).isAnUsableEffect()) || (containerType != 12 && containerType != 14 && containerType != 31 && containerType != 17 && (re.hasDuration() || re.mustBeTriggered()))));
    }
    
    @Override
    public String[] getFields() {
        return CharacterInfo.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return fieldName.equals("name");
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("isFightStateBarDisplayed")) {
            if (value instanceof Boolean) {
                this.m_isFightStateBarDisplayed = (boolean)value;
                if (WakfuGameEntity.getInstance().getLocalPlayer() != null && this.getCurrentFight() != null && WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() == this.getCurrentFight()) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(this, "isFightStateBarDisplayed");
                }
            }
            else {
                CharacterInfo.m_logger.error((Object)"Tentative de modifier l'attribut isFightStateBarDisplayed avec une valeur non bool\u00e9enne");
            }
        }
        if (fieldName.equals("name")) {
            this.setName(value.toString());
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "name");
        }
    }
    
    @Override
    public boolean isOnFight() {
        return (this.m_currentFight != null || this.m_currentFightId != -1) && this.m_onFight;
    }
    
    public int getControlledCount() {
        return this.m_fighters.size();
    }
    
    @Override
    public Collection<CharacterInfo> getControlled() {
        return this.m_fighters;
    }
    
    @Override
    public CharacterInfo getControlled(final long id) {
        for (final CharacterInfo characterInfo : this.getControlled()) {
            if (characterInfo != null && characterInfo.getId() == id) {
                return characterInfo;
            }
        }
        return null;
    }
    
    @Override
    public void setFight(final int currentFightId) {
        this.m_currentFightId = currentFightId;
        if (this.m_currentFight != null && this.m_currentFight.getId() != currentFightId) {
            if (currentFightId != -1) {
                CharacterInfo.m_logger.error((Object)"Pas de fight alors qu'on a un fightId ?");
            }
            this.setCurrentFight(null);
        }
        this.m_onFight = (currentFightId != -1);
    }
    
    protected void setCurrentFight(final Fight fight) {
        this.m_currentFight = fight;
    }
    
    public FighterFieldProvider getFighterFieldProvider() {
        return this.m_fighterFieldProvider;
    }
    
    public void setFighterFieldProvider(final FighterFieldProvider fighterFieldProvider) {
        this.m_fighterFieldProvider = fighterFieldProvider;
    }
    
    @Override
    public CharacterInfo summonMonster(final long newMonsterId, final Point3 summonPos, final short monsterTypeId, final BasicInvocationCharacteristics charac, final boolean independant, final PropertyManager<FightPropertyType> fightProperties) {
        final MonsterBreed monsterCharac = MonsterBreedManager.getInstance().getBreedFromId(monsterTypeId);
        if (monsterCharac == null) {
            CharacterInfo.m_logger.error((Object)("Impossible d'invoquer la cr\u00e9ature " + monsterTypeId + " : non enregistr\u00e9 dans le MonsterCharacteristicManager"));
            return null;
        }
        final CharacterInfo summoning = NonPlayerCharacter.createNpc();
        this.summonFighter(summoning, newMonsterId, summonPos, charac, independant, monsterCharac, fightProperties);
        return summoning;
    }
    
    private void summonFighter(final CharacterInfo summoning, final long newMonsterId, final Point3 summonPos, final BasicInvocationCharacteristics charac, final boolean independant, final MonsterBreed monsterCharac, final PropertyManager<FightPropertyType> properties) {
        this.setSummonBaseData(summoning, newMonsterId, summonPos, charac, monsterCharac);
        summoning.initialize();
        charac.initializeSummoning(summoning, this);
        summoning.setType((byte)2);
        CharacterInfo controller;
        if (independant) {
            controller = summoning;
        }
        else {
            controller = this;
        }
        if (properties != null && !properties.isEmpty()) {
            if (summoning.getFightProperties() == null) {
                summoning.loadFightData();
            }
            final PropertyManager<FightPropertyType> summoningProperties = summoning.getFightProperties();
            for (final int propertyId : properties.getActiveProperties()) {
                final FightPropertyType prop = FightPropertyType.getPropertyFromId(propertyId);
                if (prop != null) {
                    summoningProperties.add(prop);
                }
                else {
                    CharacterInfo.m_logger.error((Object)("id d'une propri\u00e9t\u00e9 de base incorrect :" + propertyId));
                }
            }
        }
        final byte teamId = (charac.getTeamId() == -1) ? this.getTeamId() : charac.getTeamId();
        this.addSummonToFight(summoning, controller, teamId);
        summoning.onSummonInitialized(controller);
        CharacterInfoManager.getInstance().addAndSpawnCharacter(summoning);
        summoning.getActor().addPassiveTeamParticleSystem(summoning.getTeamId());
        summoning.getActor().addDirectionParticleSystem(summoning.getDirection());
        UIFightFrame.getInstance();
        UIFightFrame.hideFighter(summoning);
        FightVisibilityManager.getInstance().onFighterJoinFight(summoning, summoning.getCurrentFightId());
        WeaponAnimHelper.prepareAnimForFight(summoning);
        summoning.reloadItemEffects();
        summoning.refreshDisplayEquipment();
    }
    
    private void setSummonBaseData(final CharacterInfo summoning, final long newMonsterId, final Point3 summonPos, final BasicInvocationCharacteristics charac, final MonsterBreed monsterBreed) {
        summoning.setId(newMonsterId);
        summoning.setBreed(monsterBreed);
        if (charac != null) {
            summoning.setLevel(charac.getLevel());
            summoning.setSex(charac.getSex());
        }
        else {
            CharacterInfo.m_logger.warn((Object)("Charac null a l'initisalisation d'une invoc, uid : " + newMonsterId));
        }
        summoning.setPosition(summonPos);
        final Direction8 direction = charac.getDirection();
        if (direction == Direction8.NONE) {
            summoning.setDirection(this.getDirection());
        }
        else {
            summoning.setDirection(direction);
        }
        summoning.setSummoned(true);
        summoning.m_applyGuildVisual = this.m_applyGuildVisual;
    }
    
    private void addSummonToFight(final CharacterInfo summoning, final CharacterInfo controller, final byte teamId) {
        summoning.setSpawnInWorld(true);
        if (this.getCurrentFight() != null) {
            this.getCurrentFight().addFighter(summoning, teamId, true, controller);
            summoning.setSpawnInMyFight(true);
        }
        else if (this.getCurrentExternalFightInfo() != null) {
            this.getCurrentExternalFightInfo().addFighter(summoning, this.getTeamId());
        }
    }
    
    @Override
    public boolean carry(final CarryTarget carriedFighter) {
        final boolean result = super.carry(carriedFighter);
        if (result) {
            final CharacterActor actor = this.getActor();
            this.m_beforeCarryMovementSelector = actor.getMovementSelector();
            actor.setMovementSelector(false, MovementStyleManager.WALK_CARRY_STYLE);
            actor.carry(((MobileProvider)carriedFighter).getMobile());
            actor.addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    final Mobile carriedMobile = ((MobileProvider)carriedFighter).getMobile();
                    if (actor.getCarriedMobile() != carriedMobile || carriedMobile == null) {
                        return;
                    }
                    carriedMobile.setWorldPosition(actor.getCurrentWorldX(), actor.getCurrentWorldY(), actor.getCurrentAltitude() + actor.getHeight());
                    actor.removeAnimationEndedListener(this);
                }
            });
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.isControlledByLocalPlayer()) {
            localPlayer.updateShortcutBars();
        }
        return result;
    }
    
    @Override
    public boolean uncarryTo_effect(final Point3 pos) {
        return this.uncarryTo(pos, false);
    }
    
    @Override
    public boolean uncarryTo(final Point3 position) {
        return this.uncarryTo(position, true);
    }
    
    private void carriedGoDown() {
        final CharacterActor actor = this.getActor();
        actor.setAnimationSuffix(null);
        actor.getCurrentAttack().startUsageAndNotify(actor);
        if (this.m_currentCarryTarget == null) {
            CharacterInfo.m_logger.trace((Object)"Essaye de d\u00e9poser alors qu'il ne porte personne");
            return;
        }
        if (this.m_currentCarryTarget instanceof BarrelEffectArea) {
            actor.setAnimation("Anim03PorteTonneau");
        }
        else {
            actor.setAnimation("Anim04Porte");
        }
    }
    
    private boolean uncarryTo(final Point3 position, final boolean carriedGoesDown) {
        return this.uncarryTo(position, carriedGoesDown, carriedGoesDown);
    }
    
    private boolean uncarryTo(final Point3 position, final boolean carriedGoesDown, final boolean playUncarryTrajectory) {
        final CharacterActor actor = this.getActor();
        if (carriedGoesDown) {
            this.carriedGoDown();
        }
        else {
            actor.getCurrentAttack().startUsageAndNotify(actor);
        }
        final Mobile uncarried = actor.uncarry(playUncarryTrajectory, position);
        if (this.m_beforeCarryMovementSelector == null) {
            if (uncarried == null) {
                CharacterInfo.m_logger.error((Object)"On d\u00e9pose rien (?) . double uncarry ?");
            }
            else {
                CharacterInfo.m_logger.error((Object)"Reset le movementSelector \u00e0 null (interdit!!)");
                actor.setMovementSelector(SimpleMovementSelector.getInstance());
            }
        }
        else {
            actor.setMovementSelector(this.m_beforeCarryMovementSelector);
            this.m_beforeCarryMovementSelector = null;
        }
        return super.uncarryTo(position);
    }
    
    @Override
    public void forceUncarry() {
        final CharacterActor actor = this.getActor();
        actor.resetMovementSelector();
        super.forceUncarry();
        actor.uncarry();
    }
    
    @Override
    public void onUncarryEvent() {
        super.onUncarryEvent();
        this.getActor().getCurrentAttack().setMovementSelector(this.getActor());
    }
    
    @Override
    public void onCarryEvent(final Carrier carrier) {
        super.onCarryEvent(carrier);
        this.getActor().setMovementSelector(NoneMovementSelector.getInstance());
    }
    
    @Override
    public void onControllerEvent(final int eventId, final Object param) {
        switch (eventId) {
            case 300: {
                this.setCurrentFight((Fight)param);
                this.m_currentFightId = this.m_currentFight.getId();
                this.updateAdditionalAppearance();
                break;
            }
            case 301: {
                this.m_isFightStateBarDisplayed = false;
                this.setCurrentFight(null);
                this.m_currentFightId = -1;
                this.m_fighterFieldProvider = null;
                if (this.getActor().getStatus() == 1) {
                    this.getActor().setStatus((byte)0);
                }
                this.updateAdditionalAppearance();
                break;
            }
        }
    }
    
    @Override
    public CharacterInfo getController() {
        return BasicCharacterInfo.getControllerFromFightOrSelf(this.getCurrentFight(), this);
    }
    
    @Override
    public CharacterInfo getOriginalController() {
        return BasicCharacterInfo.getOriginalControllerFromFightOrSelf(this.getCurrentFight(), this);
    }
    
    @Override
    public void onSpecialFighterEvent(final SpecialEvent fightEventType) {
        switch (fightEventType.getId()) {
            case 1000: {
                final WakfuRunningEffect effect = ((EffectAppliedEvent)fightEventType).getEffect();
                if (!this.canDisplayEffectInStateBar(effect)) {
                    break;
                }
                this.updateStateDisplay(true);
                if (this == WakfuGameEntity.getInstance().getLocalPlayer() && effect instanceof StateRunningEffect) {
                    final State state = ((StateRunningEffect)effect).getState();
                    if (state != null) {
                        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventStateApplied(state.getStateBaseId()));
                    }
                    else {
                        CharacterInfo.m_logger.warn((Object)"Etat inexistant on ne peut pas lancer l'\u00e9v\u00e8nement");
                    }
                    break;
                }
                break;
            }
            case 1002: {
                final EffectModifiedEvent event = (EffectModifiedEvent)fightEventType;
                if (this == WakfuGameEntity.getInstance().getLocalPlayer()) {
                    for (final Map.Entry<Long, RunningEffect> entry : event.getPairs().entrySet()) {
                        this.m_refpManager.getRunningEffectProvider(entry.getValue(), entry.getKey());
                    }
                    this.m_statesCache = null;
                    this.m_protectorStatesCache = null;
                    this.m_allStatesCache = null;
                    PropertiesProvider.getInstance().firePropertyValueChanged(this, "states", "protectorStates", "allStates");
                    break;
                }
                break;
            }
            case 1001: {
                this.updateStateDisplay(false);
                break;
            }
            case 1003: {
                this.m_statesCache = null;
                this.m_protectorStatesCache = null;
                this.m_allStatesCache = null;
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "states", "protectorStates", "allStates");
                break;
            }
            case 101: {
                if (WakfuGameEntity.getInstance().getLocalPlayer() == this || !this.isInvisibleForLocalPlayer()) {
                    this.getActor().addActiveParticleSystem();
                    break;
                }
                break;
            }
            case 102: {
                this.getActor().clearActiveParticleSystem();
                break;
            }
        }
    }
    
    protected void updateStateDisplay(final boolean apply) {
        if (WakfuGameEntity.getInstance().getLocalPlayer() == null) {
            return;
        }
        this.m_refpManager.clear();
        this.m_statesCache = null;
        this.m_protectorStatesCache = null;
        this.m_allStatesCache = null;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "states", "protectorStates", "allStates");
        if (this.m_currentFight != null && this.m_currentFight.consernLocalPlayer()) {
            final Timeline timeline = this.getCurrentFight().getTimeline();
            if (timeline != null) {
                timeline.updateFirstTimelineFighterListProperty();
            }
        }
    }
    
    @Override
    public AbstractMRUAction[] getMRUActions() {
        final ArrayList<AbstractMRUAction> mru = new ArrayList<AbstractMRUAction>();
        for (int i = 0; i < this.m_mruActions.size(); ++i) {
            mru.add(this.m_mruActions.get(i).getMRUAction());
        }
        return mru.toArray(new AbstractMRUAction[mru.size()]);
    }
    
    @Override
    public boolean isMRUPositionable() {
        return true;
    }
    
    @Override
    public Point getMRUScreenPosition() {
        final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
        final Point2 p = IsoCameraFunc.getScreenPositionFromBottomLeft(scene, this.getWorldX(), this.getWorldY(), this.getAltitude() + this.getHeight());
        final int x = MathHelper.fastRound(p.m_x);
        final int y = MathHelper.fastRound(p.m_y);
        return new Point(x, y);
    }
    
    @Override
    public short getMRUHeight() {
        return (short)(this.getBreed().getHeight() * 10.0f);
    }
    
    @Override
    public ItemSetManager getItemSetManager() {
        return ItemSetManager.getInstance();
    }
    
    @Override
    protected CitizenComportment createCitizenComportment() {
        return ClientCitizenComportment.createClientComportment(this);
    }
    
    @Override
    public ReferenceItemManager getItemManager() {
        return ReferenceItemManager.getInstance();
    }
    
    @Override
    public boolean fireAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        final boolean executed = !this.onAction(action, user);
        if (!executed) {
            CharacterInfo.m_logger.warn((Object)("Action " + action + " non prise en compte par ce CharacterInfo, type=" + this.getClass().getName() + ", id=" + this.getId()));
        }
        return executed;
    }
    
    @Override
    public void sendActionMessage(final InteractiveElementAction action) {
        throw new UnsupportedOperationException("Les actions ne sont pas forward\u00e9e au serveur pour le moment, il existe encore des protocoles sous-jacents.");
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        throw new UnsupportedOperationException("Pas d'action cliente prise en compte sur ce type d'\u00e9l\u00e9ment interactif.");
    }
    
    @Override
    public InteractiveElementAction getDefaultAction() {
        return null;
    }
    
    @Override
    public InteractiveElementAction[] getUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public EffectContext<WakfuEffect> getFightEffectContext() {
        final EffectContext<WakfuEffect> context = super.getFightEffectContext();
        if (context != null) {
            return context;
        }
        if (this.getCurrentFightId() != -1 && this.m_onFight && FightManager.getInstance().getFightById(this.getCurrentFightId()) != null) {
            return (EffectContext<WakfuEffect>)FightManager.getInstance().getFightById(this.getCurrentFightId()).getContext();
        }
        return null;
    }
    
    @Override
    public EffectContext<WakfuEffect> getInstanceEffectContext() {
        return WakfuInstanceEffectContext.getInstance();
    }
    
    @Override
    public void goOffPlay(final EffectUser killer) {
    }
    
    @Override
    public void goBackInPlay(final EffectUser caster) {
    }
    
    @Override
    public void goOutOfPlay(final EffectUser killer) {
    }
    
    @Override
    public boolean canChangePlayStatus() {
        return false;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.addStateAffinityUpdateListener(this);
        this.m_clothIndex = -1;
        this.m_faceIndex = -1;
        this.m_addActorToManager = true;
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_currentFight != null) {
            this.m_currentFight.removeControlledFighter(this);
        }
        if (this.m_currentExternalFightInfo != null) {
            this.m_currentExternalFightInfo.removeFighter(this);
        }
        super.onCheckIn();
        this.m_isFightStateBarDisplayed = false;
        this.m_currentInteractiveElement = null;
        this.setCurrentFight(null);
        this.m_currentFightId = -1;
        this.m_fighterFieldProvider = null;
        this.m_skillEndMovementListener = null;
        this.m_debugMessage = null;
        this.m_currentExternalFightInfo = null;
        this.setActor(null);
        this.m_fighters.clear();
        this.m_refpManager.clear();
        this.removeAdditionalAppearance();
        CharacterInfoManager.getInstance().removeListener(this.m_listenerForSummonInitialize);
        this.m_listenerForSummonInitialize = null;
        this.m_addActorToManager = true;
        if (this.m_additionalAppearance != null) {
            this.m_additionalAppearance.clean();
        }
        if (this.m_smiley != null) {
            this.m_smiley.dispose();
            this.m_smiley = null;
        }
        this.m_applyGuildVisual = null;
    }
    
    @Override
    public void onBackInPlay() {
        super.onBackInPlay();
        this.m_characteristicViewProvider.updateViews(FighterCharacteristicType.HP);
    }
    
    @Override
    public void onGoesOffPlay() {
        super.onGoesOffPlay();
        if (this.m_actor != null) {
            UIOverHeadInfosFrame.getInstance().hideOverHead(this.m_actor);
        }
    }
    
    @Override
    public void onGoesOutOfPlay() {
        if (this.m_actor == null) {
            CharacterInfo.m_logger.error((Object)"actor null ? ", (Throwable)new Exception());
        }
        else {
            UITimelineFrame.getInstance().closeFighterDescription(this);
            this.m_actor.hideAllParticleSystems();
        }
        super.onGoesOutOfPlay();
    }
    
    @Override
    public void onJoinFight(final BasicFight basicFight) {
        super.onJoinFight(basicFight);
        if (this.m_currentFight == null) {
            this.setCurrentFight((Fight)basicFight);
            this.m_currentFightId = this.m_currentFight.getId();
        }
        this.m_actor.unapplyEquipment(EquipmentPosition.ACCESSORY.m_id);
        this.addFightCharacteristicsListeners();
    }
    
    @Override
    public void onLeaveFight() {
        super.onLeaveFight();
        this.onSpecialFighterEvent(new EffectClearedEvent());
        if (this.isNotEcosystemNpc()) {
            final CharacterActor summonActor = this.getActor();
            MobileManager.getInstance().removeMobile(summonActor);
        }
        final ItemEquipment equipmentInventory = this.getEquipmentInventory();
        if (equipmentInventory != null) {
            final Item item = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
            if (item != null) {
                this.m_actor.applyEquipment(item.getReferenceItem(), EquipmentPosition.ACCESSORY.m_id);
            }
        }
        this.removeFightCharacteristicsListeners();
        if (this.m_actor != null && this.m_actor.getHmiHelper() != null) {
            this.m_actor.getHmiHelper().showHMIActionParticleSystems();
        }
    }
    
    @Override
    public void returnToOriginalController() {
        BasicCharacterInfo.returnToOriginalControllerUsingFight(this.getCurrentFight(), this);
        if (this.getCurrentFight() != null && !this.getCurrentFight().isEnding()) {
            this.updateTeamAppearance();
        }
    }
    
    @Override
    public void setCurrentController(final long controlledId, final long newControllerId) {
        super.setCurrentController(controlledId, newControllerId);
        if (this.getCurrentFight() == null) {
            return;
        }
        final CharacterInfo controlled = this.getCurrentFight().getFighterFromId(controlledId);
        if (controlled != null) {
            controlled.updateTeamAppearance();
        }
    }
    
    @Override
    public void setTeamId(final byte teamId) {
        super.setTeamId(teamId);
        this.updateTeamAppearance();
    }
    
    public void updateTeamAppearance() {
        this.updateTeamParticleSystem();
        this.updateDirectionParticleSystem();
        if (this.m_fighterFieldProvider != null) {
            this.m_fighterFieldProvider.updateTeamField();
        }
    }
    
    public void updateTeamParticleSystem() {
        if (this.m_currentFight == null) {
            return;
        }
        if (!this.m_currentFight.isInPlay(this)) {
            return;
        }
        final boolean currentFighter = this.m_currentFight.getTimeline().isCurrentFighter(this.m_id) && this.canBeFoundByUI();
        if (currentFighter) {
            this.getActor().addActiveTeamParticleSystem(this.getTeamId());
        }
        else {
            this.getActor().addPassiveTeamParticleSystem(this.getTeamId());
        }
    }
    
    private void updateDirectionParticleSystem() {
        if (this.m_currentFight == null) {
            return;
        }
        if (!this.m_currentFight.isInPlay(this)) {
            return;
        }
        this.getActor().addDirectionParticleSystem(this.getDirection());
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        final CharacteristicType type = charac.getType();
        final LocalPlayerCharacter localPlayerCharacter = WakfuGameEntity.getInstance().getLocalPlayer();
        if (type.getCharacteristicType() == 0) {
            switch ((FighterCharacteristicType)charac.getType()) {
                case LEADERSHIP: {
                    SymbiotView.getInstance().updateLeaderShipCapacity();
                }
                case HP:
                case MP: {
                    if (WakfuGameEntity.getInstance().hasFrame(UIFightFrame.getInstance())) {
                        UIFightFrame.getInstance();
                        UIFightFrame.updateMovementDisplay();
                    }
                    if (WakfuGameEntity.getInstance().hasFrame(UIFightMovementFrame.getInstance())) {
                        UIFightMovementFrame.getInstance().refreshPathSelection();
                    }
                }
                case AP:
                case WP:
                case INIT:
                case CHRAGE:
                case TACKLE:
                case DODGE:
                case PROSPECTION:
                case WISDOM:
                case HEAL_IN_PERCENT:
                case FEROCITY:
                case MECHANICS: {
                    if (localPlayerCharacter != null && (this.isLocalPlayer() || !this.isControlledByAI())) {
                        this.executeAfterAllUpdate("updateShortcut", new Runnable() {
                            @Override
                            public void run() {
                                final ShortcutBarManager shortcutBarManager = localPlayerCharacter.getShortcutBarManager();
                                shortcutBarManager.updateLeftAndRighthands();
                                shortcutBarManager.updateShortcutBars();
                                if (CharacterInfo.this.isLocalPlayer() && localPlayerCharacter.getSpellInventoryManager() != null) {
                                    localPlayerCharacter.getSpellInventoryManager().updateSpellsField();
                                }
                            }
                        });
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void onPropertyUpdated(final PropertyType prop) {
        if (prop == null) {
            return;
        }
        if (prop.getPropertyTypeId() == 1) {
            switch ((FightPropertyType)prop) {
                case AP_AS_MP: {
                    this.m_characteristicViewProvider.updateViews(FighterCharacteristicType.AP);
                    break;
                }
                case INVERT_DMG_AND_RES: {
                    this.m_characteristicViewProvider.updateViews(FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT);
                    break;
                }
                case ROOTED:
                case DO_NOT_MOVE_IN_FIGHT:
                case LAME:
                case SEVEN_LEAGUE_BOOTS: {
                    if (this.m_currentFight != null && this.m_currentFight == WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFight()) {
                        UIFightFrame.getInstance();
                        UIFightFrame.updateMovementDisplay();
                        UIFightMovementFrame.getInstance().refreshPathSelection();
                        break;
                    }
                    break;
                }
                case INVISIBLE_SUPERIOR:
                case INVISIBLE: {
                    if (this.isInLocalPlayerTeam()) {
                        UIFightFrame.hideFighter(this);
                    }
                    else {
                        this.getActor().resetAlpha();
                        if (this.getFightProperties() != null) {
                            final boolean visible = !this.getFightProperties().isActiveProperty((FightPropertyType)prop);
                            this.getActor().setVisible(visible);
                            if (visible) {
                                this.getActor().showAllParticleSystems();
                            }
                            else {
                                this.getActor().hideAllParticleSystems();
                            }
                        }
                    }
                    final Fight fight = this.getCurrentFight();
                    if (fight == null) {
                        break;
                    }
                    final boolean visible2 = !this.getFightProperties().isActiveProperty((FightPropertyType)prop);
                    if (visible2) {
                        ((WakfuClientFightMap)fight.getFightMap()).removeInvisibleCharacter(this);
                        break;
                    }
                    ((WakfuClientFightMap)fight.getFightMap()).addInvisibleCharacter(this);
                    break;
                }
            }
        }
    }
    
    public boolean isInLocalPlayerTeam() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }
        final Fight localPlayerFight = localPlayer.getCurrentFight();
        return this.isLocalPlayer() || (localPlayerFight != null && localPlayerFight == this.getCurrentFight() && localPlayer.getTeamId() == this.getTeamId());
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        this.getActor().setDirection(direction);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "direction");
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
        super.setPosition(x, y, alt);
        this.getActor().setWorldPosition(x, y, alt);
    }
    
    @Override
    public void setPosition(final Point3 pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public void teleport(final int x, final int y, final short z) {
        if (this.getCarrier() != null) {
            ((CharacterInfo)this.getCarrier()).uncarryTo(new Point3(x, y, z), true, false);
        }
        this.teleport(x, y, z, false);
    }
    
    public void teleport(final int x, final int y, final short z, final boolean tryToMove) {
        this.removeSkillEndMovementListener();
        final Point3 pos = new Point3(x, y, z);
        super.setPosition(pos);
        this.getActor().teleportTo(pos, tryToMove, false, false);
    }
    
    public abstract void addFightCharacteristicsListeners();
    
    public abstract void removeFightCharacteristicsListeners();
    
    public void addMRUAction(final MRUActions action) {
        this.m_mruActions.add(action);
    }
    
    public void removeMRUAction(final int actionId) {
        for (int i = 0, size = this.m_mruActions.size(); i < size; ++i) {
            if (this.m_mruActions.get(i).getActionId() == actionId) {
                this.m_mruActions.remove(i);
            }
        }
    }
    
    public void addSkillXp(final int skillId, final long xpGained, final boolean levelGained) {
        if (levelGained && !this.isOnFight()) {
            this.applyLevelUpParticleSystem();
        }
    }
    
    public void addSpellXp(final int spellId, final long xpGained, final boolean levelGained) {
        if (levelGained && !this.isOnFight()) {
            this.applyLevelUpParticleSystem();
        }
    }
    
    public void applyLevelUpParticleSystem() {
        if (WakfuGameEntity.getInstance().getLocalPlayer().isOnFight() && this.getCurrentFightId() != WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFightId()) {
            return;
        }
        final Iterator<IsoParticleSystem> it = IsoParticleSystemManager.getInstance().particleSystemIterator();
        while (it.hasNext()) {
            final IsoParticleSystem previousParticleSystem = it.next();
            if (previousParticleSystem instanceof FreeParticleSystem) {
                final FreeParticleSystem pps = (FreeParticleSystem)previousParticleSystem;
                if (pps.getFileId() == 999999 && pps.getTarget() == this.getActor()) {
                    return;
                }
                continue;
            }
        }
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(999999);
        if (system == null) {
            return;
        }
        system.setTarget(this.getActor());
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
    }
    
    public void cleanUp() {
        this.cleanUp(false);
    }
    
    public void cleanUp(final boolean force) {
        if (force) {
            this.setActor(null);
        }
    }
    
    protected final void fireEquipmentChanged() {
        final PropertiesProvider propertiesProvider = PropertiesProvider.getInstance();
        propertiesProvider.firePropertyValueChanged(this, "actorAnimation", "actorAnimationPath", "actorDescriptorLibrary", "actorEquipment");
    }
    
    @Override
    public void fromBuild(final byte[] serializedCharacter) {
        this.beginRefreshDisplayEquipment();
        super.fromBuild(serializedCharacter);
        this.endRefreshDisplayEquipment();
    }
    
    public void beginRefreshDisplayEquipment() {
        ++this.m_beginRefreshDisplayEquipement;
    }
    
    public void endRefreshDisplayEquipment() {
        assert this.m_beginRefreshDisplayEquipement > 0;
        --this.m_beginRefreshDisplayEquipement;
        this.refreshDisplayEquipment();
    }
    
    public final void refreshDisplayEquipment() {
        if (this.m_beginRefreshDisplayEquipement > 0) {
            return;
        }
        try {
            final CharacterActor actor = this.m_actor;
            if (actor == null) {
                return;
            }
            if (actor.getAnmInstance() == null) {
                return;
            }
            actor.unapplyAllEquipments();
            actor.getAnmInstance().unapplyAllParts();
            final MonsterSkin forceSkin = actor.getMonsterSkin();
            final boolean applyEquipment = (forceSkin == null || forceSkin.m_displayEquipment) && !actor.isHideAllEquipments();
            if (forceSkin == null) {
                actor.setGfx(Integer.toString(this.getGfxId()));
                this.applyCustoms();
            }
            else {
                forceSkin.applyTo(this);
            }
            if (applyEquipment) {
                this.applyAllEquipements();
                this.applyEquipmentModifiers();
                this.updateColors();
                this.applyGuildVisual();
                this.applyEquipmentColorModifiers();
            }
            this.applyMount();
            this.applyMisc();
            if (applyEquipment) {
                this.applyRankStuff();
            }
        }
        catch (Exception e) {
            CharacterInfo.m_logger.error((Object)("probl\u00e8me lors de l'application de l'equipement sur l'acteur " + this.getId() + " " + this.getName()), (Throwable)e);
        }
        CharacterImageGenerator.getInstance().deleteCharacterImage(this.m_id);
        this.fireEquipmentChanged();
    }
    
    private void applyMount() {
        final BasicOccupation currentOccupation = this.getCurrentOccupation();
        if (currentOccupation != null && currentOccupation.getOccupationTypeId() == 14) {
            ((RideOccupation)currentOccupation).applyAnimation();
        }
    }
    
    protected void applyGuildVisual() {
        if (this.m_applyGuildVisual != null) {
            this.m_applyGuildVisual.applyOn(this.getActor());
        }
    }
    
    protected void applyRankStuff() {
        final CitizenComportment comportment = this.getCitizenComportment();
        final NationRank rank = comportment.getRank();
        if (rank == null) {
            return;
        }
        NationRankEquipmentHelper.foreachEquipement(rank, comportment.getNationId(), new TObjectIntProcedure<EquipmentPosition>() {
            @Override
            public boolean execute(final EquipmentPosition position, final int gfxId) {
                CharacterInfo.this.getActor().applyEquipment(gfxId, position.getId(), true);
                return true;
            }
        });
    }
    
    protected void applyMisc() {
        final BasicOccupation occupation = this.getCurrentOccupation();
        if (occupation != null && occupation.getOccupationTypeId() == 16) {
            final SitOccupation sitOcc = (SitOccupation)occupation;
            sitOcc.insertStoolInActor();
        }
    }
    
    protected void updateColors() {
    }
    
    protected void applyCustoms() {
        final String dressStyle = this.getCurrentDressStyle();
        final String currentHairStyle = this.getCurrentHairStyle();
        if (dressStyle == null) {
            if (currentHairStyle == null) {
                return;
            }
        }
        String equipmentFileName;
        try {
            equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
        }
        catch (PropertyException e) {
            CharacterInfo.m_logger.error((Object)"Erreur au chargement d'une propri\u00e9t\u00e9", (Throwable)e);
            return;
        }
        if (dressStyle != null) {
            final String dressFileName = String.format(equipmentFileName, dressStyle);
            this.getActor().applyParts(dressFileName, AnmPartHelper.getParts("VETEMENTCUSTOM"));
        }
        if (currentHairStyle != null) {
            final String hairFileName = String.format(equipmentFileName, currentHairStyle);
            this.getActor().applyParts(hairFileName, AnmPartHelper.getParts("CHEVEUXCUSTOM"));
        }
    }
    
    protected void applyEquipmentModifiers() {
        if (this.m_actor != null) {
            this.getActor().applyHmiAppearenceModifiers();
        }
    }
    
    protected void applyEquipmentColorModifiers() {
    }
    
    public void applyAllEquipements() {
        final TByteIntHashMap equipmentAppearance = this.getEquipmentAppearance();
        if (equipmentAppearance == null) {
            return;
        }
        final TByteIntIterator it = equipmentAppearance.iterator();
        while (it.hasNext()) {
            it.advance();
            final byte position = it.key();
            final int itemId = it.value();
            final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(itemId);
            if (referenceItem == null) {
                CharacterInfo.m_logger.error((Object)("Le ReferenceItem d'id " + itemId + " n'existe pas"));
            }
            else {
                this.getActor().applyEquipment(referenceItem, position);
            }
        }
    }
    
    @Override
    public void setForcedGfxId(final int forcedGfxId) {
        super.setForcedGfxId(forcedGfxId);
        for (final CharacterInfoPropertyEventsHandler handler : this.m_characterInfoEventsHandler) {
            handler.onAppearanceChanged(this);
        }
    }
    
    public AvatarBreedInfo getBreedInfo() {
        return AvatarBreedInfoManager.getInstance().getBreedInfo(this.getBreed().getBreedId());
    }
    
    @Nullable
    public TByteIntHashMap getEquipmentAppearance() {
        return this.m_equimentAppearance;
    }
    
    public void forceEquipmentAppearance(final TByteIntHashMap equipment) {
        this.m_equimentAppearance = equipment;
    }
    
    public CharacterColor[] getHairColors() {
        return BreedColorsManager.getInstance().getHairColors(this.getBreedId(), this.getSex());
    }
    
    public CharacterColor[] getPupilColors() {
        return BreedColorsManager.getInstance().getPupilColors(this.getBreedId(), this.getSex());
    }
    
    public long getInteractivityId() {
        return this.getId();
    }
    
    public CharacterColor[] getSkinColors() {
        return BreedColorsManager.getInstance().getSkinColors(this.getBreedId(), this.getSex());
    }
    
    public BasicSpellInventoryManager getSpellInventoryManager() {
        return null;
    }
    
    public SpellLevel getSpellLevelById(final long uid) {
        return null;
    }
    
    public Iterable<SpellLevel> getSpellLevels() {
        return null;
    }
    
    @Override
    public boolean addSpellToTemporaryInventory(final int spellId, final short spellLevel) {
        if (!this.hasTemporarySpellInventory()) {
            CharacterInfo.m_logger.error((Object)"Trying to add a spell to a temporary inventory, but no temporary inventory");
            return false;
        }
        final Spell spell = SpellManager.getInstance().getSpell(spellId);
        if (spell == null) {
            CharacterInfo.m_logger.error((Object)("Trying to add spell " + spellId + " to temporary inventory, but this spell doesn't exist"));
            return false;
        }
        final SpellLevel sp = new SpellLevel(spell, spellLevel, spell.getId());
        try {
            if (!this.getSpellInventoryManager().getTemporarySpellInventory().add(sp)) {
                return false;
            }
        }
        catch (InventoryCapacityReachedException e) {
            CharacterInfo.m_logger.error((Object)"Exception levee", (Throwable)e);
            return false;
        }
        catch (ContentAlreadyPresentException e2) {
            CharacterInfo.m_logger.error((Object)"Exception levee", (Throwable)e2);
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo currentlyPlayingFighter = UIFightTurnFrame.getInstance().getConcernedFighter();
        if (currentlyPlayingFighter == this && (this.getOriginalController() == localPlayer || this.isControlledByLocalPlayer())) {
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            shortcutBarManager.updateShortcutBars();
            final SymbiotShortcutBar symbiotShortcutBar = shortcutBarManager.getSymbiotShortcutBar();
            if (symbiotShortcutBar != null) {
                symbiotShortcutBar.forceClean();
            }
            symbiotShortcutBar.setControlledCharacter(localPlayer);
            shortcutBarManager.showSymbiotBar();
        }
        return true;
    }
    
    @Override
    public void createTemporaryInventory() {
        if (this.getSpellInventoryManager() == null) {
            return;
        }
        this.getSpellInventoryManager().createTemporarySpellInventory(this);
    }
    
    @Override
    protected void initTemporarySpellInventory(final int breedId, final MonsterSpellsLevel levelType) {
        this.changeToSpellAttackIfNecessary();
        if (this.getSpellInventoryManager() == null) {
            return;
        }
        final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId((short)breedId);
        if (breed == null) {
            return;
        }
        final ArrayList<IntObjectPair<Spell>> spells = breed.getSpells();
        short level = 0;
        switch (levelType) {
            case PLAYER_LEVEL: {
                level = this.getLevel();
                break;
            }
            default: {
                level = breed.getLevelMin();
                break;
            }
        }
        final List<SpellLevel> spellLevels = new ArrayList<SpellLevel>();
        for (final IntObjectPair<Spell> spellAndLevel : spells) {
            final Spell spell = spellAndLevel.getSecond();
            final SpellLevel spellLevel = new SpellLevel(spell, level, spell.getId());
            spellLevels.add(spellLevel);
        }
        this.getSpellInventoryManager().initTemporarySpellInventory(spellLevels, this);
    }
    
    public boolean changeToSpellAttackIfNecessary() {
        return this.changeToSpellAttackIfNecessary(null);
    }
    
    public boolean changeToSpellAttackIfNecessary(final AttackTypeListener newAttackListener) {
        if (this.m_currentFight == null) {
            return false;
        }
        final AttackType currentAttack = this.getActor().getCurrentAttack();
        if (currentAttack instanceof SpellAttack) {
            return false;
        }
        final AttackType spellAttack = new SpellAttack(this);
        if (newAttackListener != null) {
            spellAttack.addListener(newAttackListener);
        }
        if (currentAttack != null) {
            WeaponAnimHelper.changeAttack(this.getActor(), spellAttack);
            return true;
        }
        this.getActor().setCurrentAttack(spellAttack);
        spellAttack.startUsageAndNotify(this.getActor());
        return true;
    }
    
    @Override
    public void resetTemporarySpellInventory() {
        if (this.getSpellInventoryManager() == null) {
            return;
        }
        this.getSpellInventoryManager().resetTemporarySpellInventory();
    }
    
    @Override
    public boolean hasTemporarySpellInventory() {
        return this.getSpellInventoryManager() != null && this.getSpellInventoryManager().hasTemporarySpellInventory();
    }
    
    public boolean hasActor() {
        return this.m_actor != null;
    }
    
    public boolean isFightStateBarDisplayed() {
        return this.m_isFightStateBarDisplayed;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return !this.isInvisibleForLocalPlayer() && super.isBlockingMovement();
    }
    
    public boolean isLocalPlayer() {
        return false;
    }
    
    public boolean isControlledByLocalPlayer() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer != null && this.getController().getOwnerId() == localPlayer.getOwnerId();
    }
    
    public void removeSkillEndMovementListener() {
        if (this.m_skillEndMovementListener != null) {
            final CharacterActor actor = this.getActor();
            if (actor == null) {
                return;
            }
            actor.removeEndPositionListener(this.m_skillEndMovementListener);
            this.m_skillEndMovementListener = null;
        }
    }
    
    public void resetFighterAfterFight() {
        final CharacterActor characterActor = this.getActor();
        boolean isSpecialAnim = false;
        if (this.isDead()) {
            isSpecialAnim = true;
            characterActor.setCurrentAttack(NoneAttack.getInstance());
        }
        else {
            final String animation = characterActor.getAnimation();
            if ((this.isDead() && animation.equalsIgnoreCase("AnimKO-SortieHS")) || animation.equalsIgnoreCase("AnimKO-Debut") || (this.isDead() && animation.equalsIgnoreCase("AnimDesincarnation")) || (this.isDead() && animation.equalsIgnoreCase("AnimTombe")) || animation.equalsIgnoreCase("AnimEmote-Victoire")) {
                isSpecialAnim = true;
            }
        }
        if (!this.isDead()) {
            if (isSpecialAnim) {
                characterActor.setCurrentAttack(NoneAttack.getInstance());
                NoneAttack.getInstance().endUsage(characterActor);
            }
            else {
                WeaponAnimHelper.changeAttack(characterActor, NoneAttack.getInstance());
            }
        }
        characterActor.resetMovementSelector();
        characterActor.enableDefaultPreferedAlphaMask(false);
        characterActor.setDesiredAlpha(characterActor.getOriginalAlpha());
        characterActor.resetColor();
        characterActor.removeAllEndPositionListener();
        characterActor.clearAllParticleSystems();
        characterActor.setVisible(true);
    }
    
    public void setCurrentExternalFightInfo(final ExternalFightInfo currentExternalFightInfo) {
        this.m_currentExternalFightInfo = currentExternalFightInfo;
        if (currentExternalFightInfo != null) {
            this.m_currentFightId = currentExternalFightInfo.getId();
            this.m_onFight = true;
        }
        else {
            this.m_currentFightId = -1;
            this.m_onFight = false;
        }
    }
    
    public void setDebugFightMessage(final DebugFightAccessSquareMessage msg) {
        this.m_debugMessage = msg;
    }
    
    public void setDirectionWithoutNotifyActor(final Direction8 direction) {
        assert direction != null;
        final boolean needToUpdateProperty = !this.getDirection().equals(direction);
        if (needToUpdateProperty) {
            super.setDirection(direction);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "direction");
        }
    }
    
    public void setFightStateBarDisplayed(final boolean fightStateBarDisplayed) {
        this.m_isFightStateBarDisplayed = fightStateBarDisplayed;
    }
    
    public void setPositionWithoutNotifyActor(final int x, final int y, final short alt) {
        super.setPosition(x, y, alt);
    }
    
    public void setSkillEndMovementListener(final ActorSkillEndMovementListener skillEndMovementListener) {
        if (skillEndMovementListener == null) {
            return;
        }
        final CharacterActor actor = this.getActor();
        if (actor == null) {
            return;
        }
        actor.addEndPositionListener(this.m_skillEndMovementListener = skillEndMovementListener);
    }
    
    @Nullable
    public AnimatedElementWithDirection getSmiley() {
        if (this.m_smiley == null) {
            final AnimatedElementWithDirection element = new AnimatedElementWithDirection(GUIDGenerator.getGUID(), this.getActor().getWorldX(), this.getActor().getWorldY(), this.getActor().getAltitude());
            final String fileName = this.getSmileyGfxFileName();
            try {
                element.load(String.format(WakfuConfiguration.getInstance().getString(this.getGfxPathKey()), fileName), true);
            }
            catch (IOException e) {
                CharacterInfo.m_logger.error((Object)"", (Throwable)e);
                element.dispose();
                return null;
            }
            catch (PropertyException e2) {
                CharacterInfo.m_logger.error((Object)"", (Throwable)e2);
                element.dispose();
                return null;
            }
            element.setGfxId(fileName);
            this.m_smiley = element;
        }
        return this.m_smiley;
    }
    
    protected abstract String getSmileyGfxFileName();
    
    protected abstract String getGfxPathKey();
    
    @NotNull
    public CharacterActor getActor() {
        if (this.m_actor == null) {
            this.setActor(new CharacterActor(this));
            if (this.getPhysicalRadius() >= 1) {
                this.m_actor.setMovementSelector(CharacterInfo.PROTECTOR_MOVEMENT_SELECTOR);
            }
        }
        return this.m_actor;
    }
    
    @Override
    public Mobile getMobile() {
        return this.getActor();
    }
    
    public ExternalFightInfo getCurrentExternalFightInfo() {
        return this.m_currentExternalFightInfo;
    }
    
    public void onJoinExternalFight() {
    }
    
    public void onLeaveExternalFight() {
        this.m_isDead = false;
        this.m_isFleeing = false;
        if (this.m_actor != null) {
            this.m_actor.setVisible(true);
        }
        this.unloadFightData();
    }
    
    @Override
    public Fight getCurrentFight() {
        return this.m_currentFight;
    }
    
    public int getCurrentFightId() {
        return this.m_currentFightId;
    }
    
    public AnimatedInteractiveElement getCurrentInteractiveElement() {
        return this.m_currentInteractiveElement;
    }
    
    public DebugFightAccessSquareMessage getDebugMessage() {
        return this.m_debugMessage;
    }
    
    public void setCurrentInteractiveElement(final AnimatedInteractiveElement currentInteractiveElement) {
        this.m_currentInteractiveElement = currentInteractiveElement;
    }
    
    public RunningEffectFieldProviderManager getRunningEffectFieldProvider() {
        return this.m_refpManager;
    }
    
    public void setActor(final CharacterActor actor) {
        if (actor == this.m_actor) {
            return;
        }
        if (this.m_actor != null) {
            MobileManager.getInstance().removeMobile(this.m_actor);
            this.m_actor.release();
        }
        this.m_actor = actor;
        if (this.m_addActorToManager && this.m_actor != null) {
            MobileManager.getInstance().addMobile(this.m_actor);
        }
    }
    
    public void setAddActorToManager(final boolean addActorToManager) {
        this.m_addActorToManager = addActorToManager;
    }
    
    public void initializeAfterCharacterAddedToWorld() {
        this.initializeSummon();
    }
    
    private void initializeSummon() {
        final BasicInvocationCharacteristics summonCharacs = this.getSummonCharacteristics();
        if (summonCharacs != null) {
            final CharacterInfoManager manager = CharacterInfoManager.getInstance();
            final long summonerId = summonCharacs.getSummonerId();
            final CharacterInfo summoner = manager.getCharacter(summonerId);
            if (summoner == null && this.m_listenerForSummonInitialize == null) {
                manager.addListener(this.m_listenerForSummonInitialize = new CharacterInfoManagerListener() {
                    @Override
                    public void onCharacterAdded(final CharacterInfo character) {
                        if (character.getId() != summonerId) {
                            return;
                        }
                        summonCharacs.initializeSummoning(CharacterInfo.this, character);
                        CharacterInfo.this.onSummonInitialized(character);
                        manager.removeListenerAfterExecution(this);
                    }
                });
            }
            else {
                summonCharacs.initializeSummoning(this, summoner);
                this.onSummonInitialized(summoner);
            }
        }
    }
    
    protected void onSummonInitialized(final CharacterInfo summoner) {
        throw new UnsupportedOperationException("Doit \u00eatre un NPC");
    }
    
    public abstract byte getActorTypeId();
    
    @Override
    public byte getTeamId() {
        if (this.getCurrentExternalFightInfo() != null) {
            return this.getCurrentExternalFightInfo().getTeamId(this.getId());
        }
        return super.getTeamId();
    }
    
    public int getDefeatScript() {
        if (this.getBreed() == null) {
            return 30000;
        }
        return this.getBreed().getDefeatScriptId();
    }
    
    public void reloadCharacterForFight(final Fight concernedFight, final byte[] serializedFighterDatas, final byte[] serializedEffectuserDatas) {
        this.recoverEffects(serializedEffectuserDatas, false, concernedFight);
        this.recoverFighterData(serializedFighterDatas, true);
    }
    
    public void recoverEffects(final byte[] serializedEffectUserDatas) {
        this.recoverEffects(serializedEffectUserDatas, true, null);
    }
    
    public void recoverEffects(final byte[] serializedEffectuserDatas, final boolean dontExecuteEffects, final BasicFight concernedFight) {
        final int hpValue = this.getCharacteristicValue(FighterCharacteristicType.HP);
        this.unserializeEffectUser(serializedEffectuserDatas);
        if (!this.isNotEcosystemNpc()) {
            this.initialiseCharacteristicsToBaseValue();
        }
        if (dontExecuteEffects) {
            this.notifyApplicationAndExecutionForAllStoredEffects();
        }
        else {
            this.executeUnserializedEffect();
        }
        if (!dontExecuteEffects && concernedFight != null) {
            this.reloadBuffs(concernedFight.getContext());
        }
        this.setHpValue(hpValue);
    }
    
    public void recoverFighterData(final byte[] serializedFighterDatas, final boolean setApMpWpToMax) {
        final PropertyManager<FightPropertyType> fightProperties = this.getFightProperties();
        if (fightProperties != null) {
            fightProperties.reset();
        }
        this.unserializeFighterDatas(serializedFighterDatas);
        if (setApMpWpToMax) {
            this.setApPmPwToMax();
        }
        this.addFightCharacteristicsListeners();
    }
    
    private void executeUnserializedEffect() {
        final LinkedList<RunningEffect> effectsToApply = this.getSortedEffectsForExecution();
        for (int i = 0; i < effectsToApply.size(); ++i) {
            final RunningEffect runningEffect = effectsToApply.get(i);
            if (runningEffect instanceof StateRunningEffect && ((StateRunningEffect)runningEffect).isTransmigrable()) {
                runningEffect.enableValueComputation();
            }
            else {
                runningEffect.disableValueComputation();
            }
            if (runningEffect.mustBeTriggered() || runningEffect.hasExecutionDelay()) {
                this.getRunningEffectManager().removeEffect(runningEffect);
            }
            else {
                runningEffect.onApplication();
                runningEffect.askForExecution();
            }
        }
    }
    
    private void notifyApplicationAndExecutionForAllStoredEffects() {
        final LinkedList<RunningEffect> effects = this.getSortedEffectsForExecution();
        this.notifyApplicationAndExecutionForEffects(effects);
    }
    
    private void notifyApplicationAndExecutionForEffects(final LinkedList<RunningEffect> effects) {
        for (int i = 0; i < effects.size(); ++i) {
            final RunningEffect runningEffect = effects.get(i);
            if (runningEffect instanceof StateRunningEffect) {
                runningEffect.disableValueComputation();
            }
            runningEffect.onApplication();
            final WakfuRunningEffect wakfuRunningEffect = (WakfuRunningEffect)runningEffect;
            if (wakfuRunningEffect.isExecuted()) {
                wakfuRunningEffect.notifyExecution(null, false);
            }
        }
    }
    
    private LinkedList<RunningEffect> getSortedEffectsForExecution() {
        final Iterator<RunningEffect> it = this.getRunningEffectManager().iterator();
        final LinkedList<RunningEffect> effectsToApply = new LinkedList<RunningEffect>();
        while (it.hasNext()) {
            final RunningEffect runningEffect = it.next();
            if (runningEffect instanceof StateRunningEffect) {
                effectsToApply.addFirst(runningEffect);
            }
            else {
                effectsToApply.addLast(runningEffect);
            }
        }
        return effectsToApply;
    }
    
    public final void reloadCharacterAfterExternalFightEnded() {
        final List<StateRunningEffect> toTransmigrate = this.getStatesToTransmigrate(this.m_ownContext);
        this.getRunningEffectManager().clear();
        this.transmigrateStates(toTransmigrate);
        this.reloadItemEffects();
    }
    
    @Override
    public void reloadItemEffects() {
        if (this.getType() == 5) {
            return;
        }
        super.reloadItemEffects();
    }
    
    public void reloadItemEffectsWithoutCheck() {
        super.reloadItemEffects();
    }
    
    @Override
    protected void onFightInfoUpdated() {
        if (this.m_currentFight == null) {
            return;
        }
        final FightMap fightMap = this.m_currentFight.getFightMap();
        if (fightMap == null) {
            return;
        }
        final FightObstacle obstacleFromId = fightMap.getObstacleFromId(this.getObstacleId());
        if (obstacleFromId == null) {
            fightMap.assignObstacleWithId(this);
        }
        else if (obstacleFromId != this) {
            CharacterInfo.m_logger.error((Object)("Obstacle de meme id " + obstacleFromId + " different de nous " + this + " id = " + this.getObstacleId()));
        }
    }
    
    public void fireActorAppearanceChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "actorDescriptorLibrary");
    }
    
    public int getNationId() {
        if (this.getCitizenComportment() == null) {
            return 0;
        }
        return this.getCitizenComportment().getNationId();
    }
    
    @Override
    public int getCurrentTerritoryNationId() {
        final Territory territory = TerritoriesView.INSTANCE.getFromWorldPosition(this.m_position.getX(), this.m_position.getY());
        if (territory == null || territory.getProtector() == null) {
            return 0;
        }
        return territory.getProtector().getCurrentNationId();
    }
    
    @Override
    public int getCurrentTerritoryId() {
        final Territory territory = TerritoriesView.INSTANCE.getFromWorldPosition(this.m_position.getX(), this.m_position.getY());
        return (territory != null) ? territory.getId() : -1;
    }
    
    public boolean hasGobgobInvoked() {
        return this.getCurrentFight() != null && this.getRunningEffectManager().getRunningState(977) != null;
    }
    
    @Override
    public int getSummoningLeadershipScore() {
        int score = 0;
        if (this.getCurrentFight() != null) {
            for (final BasicFighter basicFighter : this.getCurrentFight().getFighters()) {
                if (basicFighter instanceof NonPlayerCharacter) {
                    final NonPlayerCharacter npcFighter = (NonPlayerCharacter)basicFighter;
                    if (!npcFighter.isSummoned() || npcFighter.isDead() || npcFighter.getController() != this || npcFighter.hasProperty(FightPropertyType.BYPASS_SUMMONS_COUNT)) {
                        continue;
                    }
                    score += npcFighter.getBreed().getLevelMax();
                }
            }
        }
        else {
            for (final BasicCharacterInfo fighter : this.getControlled()) {
                if (fighter.isSummoned() && !fighter.isDead() && !fighter.hasProperty(FightPropertyType.BYPASS_SUMMONS_COUNT) && fighter instanceof NonPlayerCharacter) {
                    score += ((NonPlayerCharacter)fighter).getBreed().getLevelMax();
                }
            }
        }
        return score;
    }
    
    @NotNull
    public FightPathFindMethods getFightPathFindMethod() {
        return PathFindMethodSelector.selectPathFindMethod(this);
    }
    
    @Nullable
    public CharacterSpecificRangeDisplayer getSpecificRangeDisplayer() {
        return null;
    }
    
    public void onControlledNPCDeath() {
    }
    
    public void setWaitEndAnimationToBeDespawned(final boolean waitEndAnimationToBeDespawned) {
        this.m_waitEndAnimationToBeDespawned = waitEndAnimationToBeDespawned;
    }
    
    public boolean isWaitEndAnimationToBeDespawned() {
        return this.m_waitEndAnimationToBeDespawned;
    }
    
    @Override
    public void onStateResistanceUpdate(final short stateId, final int value) {
        this.m_stateAffinitiesView.setResistanceBonus(stateId, value);
    }
    
    @Override
    public void onStateApplicationUpdate(final short stateId, final int value) {
        this.m_stateAffinitiesView.setApplicationBonus(stateId, value);
    }
    
    public boolean isInvisibleForLocalPlayer() {
        return !this.isInLocalPlayerTeam() && ((!this.isActiveProperty(FightPropertyType.INVISIBLE_REVEALED) && this.isActiveProperty(FightPropertyType.INVISIBLE)) || this.isActiveProperty(FightPropertyType.INVISIBLE_SUPERIOR));
    }
    
    public String getCurrentHairStyle() {
        if (this.m_faceIndex < 0) {
            return null;
        }
        return BreedColorsManager.getInstance().getHairStyle(this.m_breed.getBreedId(), this.getSex() == 0, this.m_faceIndex);
    }
    
    public String getCurrentDressStyle() {
        if (this.m_clothIndex < 0) {
            return null;
        }
        return BreedColorsManager.getInstance().getDressStyle(this.m_breed.getBreedId(), this.getSex() == 0, this.m_clothIndex);
    }
    
    public byte getClothIndex() {
        return this.m_clothIndex;
    }
    
    public void setClothIndex(final byte clothIndex, final boolean apply) {
        this.m_clothIndex = clothIndex;
        if (apply) {
            this.refreshDisplayEquipment();
        }
    }
    
    public byte getFaceIndex() {
        return this.m_faceIndex;
    }
    
    public void setFaceIndex(final byte faceIndex, final boolean apply) {
        this.m_faceIndex = faceIndex;
        if (apply) {
            this.refreshDisplayEquipment();
        }
    }
    
    public boolean canBeFoundByUI() {
        if (!this.getActor().isVisible()) {
            return false;
        }
        if (this.isActiveProperty(FightPropertyType.DISPLAYED_LIKE_A_DECORATION)) {
            return false;
        }
        if (this.isActiveProperty(FightPropertyType.IS_A_COPY_OF_HIS_CONTROLLER)) {
            return false;
        }
        if (this.isActiveProperty(FightPropertyType.CANT_BE_DIFFERENTIATED_FROM_COPIES)) {
            final Fight fight = this.getCurrentFight();
            if (fight != null) {
                for (final CharacterInfo controlled : fight.getFightersInPlay()) {
                    if (!controlled.isSummoned()) {
                        continue;
                    }
                    if (!controlled.isActiveProperty(FightPropertyType.IS_A_COPY_OF_HIS_CONTROLLER)) {
                        continue;
                    }
                    if (controlled.getOriginalController() != this) {
                        continue;
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    public int getDeathParticleSystemId() {
        return 900016;
    }
    
    public int getResurrectionParticleSystemId() {
        return 900019;
    }
    
    public void updateCharacteristicViews(final FighterCharacteristicType... types) {
        this.m_characteristicViewProvider.updateViews(types);
    }
    
    public CharacteristicViewProvider getCharacteristicViewProvider() {
        return this.m_characteristicViewProvider;
    }
    
    static {
        FIELDS = new String[] { "name", "sex", "breedInfo", "breedId", "smileyDescriptorLibrary", "actorDescriptorLibrary", "actorAnimationName", "actorAnimationPath", "actorLinkage", "actorAnimation", "actorEquipment", "actor", "avatarUrl", "levelValue", "level", "skinColors", "hairColors", "pupilColors", "secondarySkinColors", "secondaryHairColors", "direction", "characteristics", "debuffApVisible", "debuffMpVisible", "states", "protectorStates", "allStates", "timelinePosition", "isFightStateBarDisplayed", "isKO", "spellsInventory", "leaderShipCost", "stateAffinities", "visible", "healResistDescription" };
        PROTECTOR_MOVEMENT_SELECTOR = new MovementSelector() {
            @Override
            public PathMovementStyle selectMovementStyle(final StyleMobile mobile, final int pathLength) {
                return WalkMovementStyle.getInstance();
            }
            
            @Override
            public void onMovementEnded(final StyleMobile mobile) {
            }
            
            @Override
            public void resetMovementSelector(final StyleMobile mobile) {
            }
        };
    }
}
