package com.ankamagames.wakfu.common.datas;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.basicImpl.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.jetbrains.annotations.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.wakfu.common.game.zone.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.common.game.characteristics.craft.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import gnu.trove.*;

public abstract class BasicCharacterInfo implements BasicFighter, PropertyUpdateListener, AggroUser, Validable, Carrier, CarryTarget, Citizen, TackleUser, ActionUser, CriterionUser, ResourcePlanter, PvpUser
{
    private static final boolean DEBUG = false;
    public static final FighterCharacteristicType[] PUBLIC_TYPES;
    public static final FighterCharacteristicType[] PRIVATE_TYPES;
    public static final FighterCharacteristicType[] FOR_FIGHT_TYPES;
    private static final int FINAL_HEAL_RESIST_MODIFIER = 3;
    protected static final Logger m_logger;
    private boolean m_inPool;
    private String m_checkInStackTrace;
    protected final Collection<CharacterInfoPropertyEventsHandler> m_characterInfoEventsHandler;
    protected boolean m_onFight;
    protected long m_id;
    protected long m_ownerId;
    private byte m_obstacleId;
    protected byte m_type;
    protected String m_name;
    protected short m_instanceId;
    protected byte m_sex;
    protected Direction8 m_direction;
    protected boolean m_inGame;
    protected final FourSidedPartLocalisator m_partLocalisator;
    protected CharacteristicManager<FighterCharacteristic> m_characteristics;
    protected boolean m_isKO;
    protected boolean m_isDead;
    protected boolean m_isControlledByAI;
    private boolean m_isAbandonning;
    protected final WakfuEffectContextForUniqueUser m_ownContext;
    protected final WakfuEffectContextForUniqueUser m_havenWorldEffectContext;
    protected Breed m_breed;
    protected final Point3 m_position;
    private TimedRunningEffectManager m_runningEffectManager;
    private boolean m_isSummoned;
    private boolean m_summonedFromSymbiot;
    private boolean m_alreadyReturnedToSymbiot;
    private BasicInvocationCharacteristics m_summonCharacteristics;
    private PropertyManager<FightPropertyType> m_fightProperties;
    private PropertyManager<WorldPropertyType> m_worldProperties;
    private final SpellLevelCastHistory m_spellCastHistory;
    private Direction8 m_onSpecialMovement;
    private boolean m_isPlayBeingChanged;
    private final CitizenComportment m_citizenComportment;
    private final StateAffinities m_stateAffinities;
    private final ItemEffectsApplier m_itemEffectsApplier;
    protected boolean m_isFleeing;
    private BasicCharacterInfo m_linkedCharacter;
    private boolean m_hasBeenRaisedByTrigger;
    private final List<CharacterFightListener> m_characterFightListeners;
    private final List<CharacterFightListener> m_characterFightListenersToRemove;
    private boolean m_wasCarriedOnLastCell;
    private float m_finalHealResist;
    private EffectUser m_resistanceTarget;
    private SpellCostModification m_spellCostModification;
    private int m_forcedGfxId;
    protected CarryTarget m_currentCarryTarget;
    protected Carrier m_carrier;
    private UpdateItemsDelegate m_updateEffectDelegate;
    private byte m_beginDelegateCount;
    private TLongShortHashMap m_aggressiveList;
    
    protected BasicCharacterInfo() {
        super();
        this.m_inPool = true;
        this.m_characterInfoEventsHandler = new ArrayList<CharacterInfoPropertyEventsHandler>();
        this.m_obstacleId = -1;
        this.m_type = -1;
        this.m_direction = Direction8.NORTH_EAST;
        this.m_partLocalisator = new FourSidedPartLocalisator();
        this.m_ownContext = new WakfuEffectContextForUniqueUser(this, (byte)3);
        this.m_havenWorldEffectContext = new WakfuEffectContextForUniqueUser(this, (byte)4);
        this.m_breed = AvatarBreed.NONE;
        this.m_position = new Point3();
        this.m_spellCastHistory = new SpellLevelCastHistory();
        this.m_stateAffinities = new StateAffinities();
        this.m_itemEffectsApplier = new ItemEffectsApplier(this);
        this.m_characterFightListeners = new ArrayList<CharacterFightListener>();
        this.m_characterFightListenersToRemove = new ArrayList<CharacterFightListener>();
        this.m_spellCostModification = new SpellCostModification();
        this.m_beginDelegateCount = 0;
        this.m_citizenComportment = this.createCitizenComportment();
    }
    
    public void setInvocationCharacteristic(final BasicInvocationCharacteristics charac) {
        this.m_summonCharacteristics = charac;
    }
    
    public BasicInvocationCharacteristics getSummonCharacteristics() {
        return this.m_summonCharacteristics;
    }
    
    public abstract int addKamas(final int p0);
    
    public abstract AbstractBreedManager getBreedManager();
    
    public abstract Collection<? extends BasicCharacterInfo> getControlled();
    
    public abstract BasicCharacterInfo getControlled(final long p0);
    
    public abstract ReferenceItemManager getItemManager();
    
    public abstract AbstractItemSetManager getItemSetManager();
    
    public abstract int getKamasCount();
    
    public abstract CharacterSerializer getSerializer();
    
    public abstract void initialiseCharacteristicsToBaseValue();
    
    public abstract boolean returnDefaultAIControl();
    
    protected abstract CitizenComportment createCitizenComportment();
    
    @NotNull
    @Override
    public CitizenComportment getCitizenComportment() {
        return this.m_citizenComportment;
    }
    
    @Override
    public long getGuildId() {
        return 0L;
    }
    
    @Override
    public int getGuildNationId() {
        return 0;
    }
    
    public boolean addCharacterFightListener(final CharacterFightListener o) {
        return !this.m_characterFightListeners.contains(o) && this.m_characterFightListeners.add(o);
    }
    
    public boolean removeCharacterFightListener(final CharacterFightListener o) {
        return this.m_characterFightListenersToRemove.add(o);
    }
    
    private void removeObsoleteFightListeners() {
        this.m_characterFightListeners.removeAll(this.m_characterFightListenersToRemove);
        this.m_characterFightListenersToRemove.clear();
    }
    
    private void notifyJoinFight() {
        for (int i = 0; i < this.m_characterFightListeners.size(); ++i) {
            try {
                this.m_characterFightListeners.get(i).onJoinFight(this);
            }
            catch (Exception e) {
                BasicCharacterInfo.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        this.removeObsoleteFightListeners();
    }
    
    private void notifyLeaveFight() {
        for (int i = 0; i < this.m_characterFightListeners.size(); ++i) {
            try {
                this.m_characterFightListeners.get(i).onLeaveFight(this);
            }
            catch (Exception e) {
                BasicCharacterInfo.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        this.removeObsoleteFightListeners();
    }
    
    protected void notifyWonFight() {
        for (int i = 0; i < this.m_characterFightListeners.size(); ++i) {
            try {
                this.m_characterFightListeners.get(i).onWonFight(this);
            }
            catch (Exception e) {
                BasicCharacterInfo.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        this.removeObsoleteFightListeners();
    }
    
    protected void notifyLoseFight() {
        for (int i = 0; i < this.m_characterFightListeners.size(); ++i) {
            try {
                this.m_characterFightListeners.get(i).onLoseFight(this);
            }
            catch (Exception e) {
                BasicCharacterInfo.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        this.removeObsoleteFightListeners();
    }
    
    public Nation getTravellingNation() {
        return NationManager.INSTANCE.getNationById(this.getCurrentTerritoryNationId());
    }
    
    public int getTravellingNationId() {
        final Nation travellingNation = this.getTravellingNation();
        return (travellingNation == null) ? -1 : travellingNation.getNationId();
    }
    
    protected void initialiseCharacteristicsProcedures() {
        final CharacteristicManager localManager = this.m_characteristics;
        if (localManager instanceof FighterCharacteristicManager) {
            final FighterCharacteristicManager characteristic = (FighterCharacteristicManager)localManager;
            final FighterCharacteristic vitality = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.VITALITY);
            if (vitality != null) {
                vitality.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.HP, this.m_breed.getRatio(BreedRatios.VITALITY_TO_HP), 1));
            }
            final FighterCharacteristic willPower = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.WILLPOWER);
            if (willPower != null) {
                willPower.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.AP_DEBUFF_POWER, 1.0f, 0));
                willPower.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.AP_DEBUFF_RES, 1.0f, 0));
                willPower.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.MP_DEBUFF_POWER, 1.0f, 0));
                willPower.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.MP_DEBUFF_RES, 1.0f, 0));
                willPower.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.STATE_APPLICATION_BONUS, 1.0f, 0));
                willPower.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.STATE_RESISTANCE_BONUS, 1.0f, 0));
            }
            final FighterCharacteristic airMastery = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.AIR_MASTERY);
            if (airMastery != null) {
                airMastery.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_AIR_PERCENT, 1.0f, 0));
            }
            final FighterCharacteristic earthMastery = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.EARTH_MASTERY);
            if (earthMastery != null) {
                earthMastery.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_EARTH_PERCENT, 1.0f, 0));
            }
            final FighterCharacteristic fireMastery = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.FIRE_MASTERY);
            if (fireMastery != null) {
                fireMastery.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_FIRE_PERCENT, 1.0f, 0));
            }
            final FighterCharacteristic waterMastery = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.WATER_MASTERY);
            if (waterMastery != null) {
                waterMastery.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_WATER_PERCENT, 1.0f, 0));
            }
            final FighterCharacteristic agility = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.AGILITY);
            if (agility != null) {
                agility.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_AIR_PERCENT, this.m_breed.getRatio(BreedRatios.AGILITY_TO_AIR_DMG), 0));
                agility.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.RES_AIR_PERCENT, this.m_breed.getRatio(BreedRatios.AGILITY_TO_AIR_RES), 0));
            }
            final FighterCharacteristic strength = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.STRENGTH);
            if (strength != null) {
                strength.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_EARTH_PERCENT, this.m_breed.getRatio(BreedRatios.STRENGTH_TO_EARTH_DMG), 0));
                strength.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.RES_EARTH_PERCENT, this.m_breed.getRatio(BreedRatios.STRENGTH_TO_EARTH_RES), 0));
            }
            final FighterCharacteristic intel = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.INTELLIGENCE);
            if (intel != null) {
                intel.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_FIRE_PERCENT, this.m_breed.getRatio(BreedRatios.INTELLIGENCE_TO_FIRE_DMG), 0));
                intel.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.RES_FIRE_PERCENT, this.m_breed.getRatio(BreedRatios.INTELLIGENCE_TO_FIRE_RES), 0));
            }
            final FighterCharacteristic luck = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.LUCK);
            if (luck != null) {
                luck.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.DMG_WATER_PERCENT, this.m_breed.getRatio(BreedRatios.LUCK_TO_WATER_DMG), 0));
                luck.addProcedure(new CharacBoostAnotherCharacProcedure(characteristic, FighterCharacteristicType.RES_WATER_PERCENT, this.m_breed.getRatio(BreedRatios.LUCK_TO_WATER_RES), 0));
            }
            final FighterCharacteristic waterDmg = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_WATER_PERCENT);
            if (waterDmg != null) {
                waterDmg.addProcedure(new StasisDmgRecomputeProcedure(characteristic));
            }
            final FighterCharacteristic fireDmg = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_FIRE_PERCENT);
            if (fireDmg != null) {
                fireDmg.addProcedure(new StasisDmgRecomputeProcedure(characteristic));
            }
            final FighterCharacteristic earthDmg = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_EARTH_PERCENT);
            if (earthDmg != null) {
                earthDmg.addProcedure(new StasisDmgRecomputeProcedure(characteristic));
            }
            final FighterCharacteristic airDmg = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_AIR_PERCENT);
            if (airDmg != null) {
                airDmg.addProcedure(new StasisDmgRecomputeProcedure(characteristic));
            }
            final FighterCharacteristic stasisMastery = characteristic.getCharacteristic((CharacteristicType)FighterCharacteristicType.STASIS_MASTERY);
            if (stasisMastery != null) {
                stasisMastery.addProcedure(new StasisDmgRecomputeProcedure(characteristic));
            }
            if (this.isOnFight()) {
                this.addHpToChrageProcedureForSacrier();
            }
            return;
        }
        BasicCharacterInfo.m_logger.error((Object)"Les NPCs ont des caract\u00e9ristiques paresseuses. Les proc\u00e9dures doivent \u00eatre ajout\u00e9es sur la breed.");
    }
    
    public void addHpToChrageProcedureForSacrier() {
        if (this.getBreed() != AvatarBreed.SACRIER) {
            return;
        }
        final FighterCharacteristic hp = this.m_characteristics.getCharacteristic(FighterCharacteristicType.HP);
        final FighterCharacteristic virtualHp = this.m_characteristics.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP);
        final FighterCharacteristic chrage = this.m_characteristics.getCharacteristic(FighterCharacteristicType.CHRAGE);
        if (chrage == null) {
            return;
        }
        final HpToChrageProcedure chrageLinkedToHpProcedure = new HpToChrageProcedure((FighterCharacteristicManager)this.m_characteristics, 0.9f);
        if (hp != null) {
            hp.addProcedure(chrageLinkedToHpProcedure);
        }
        if (virtualHp != null) {
            virtualHp.addProcedure(chrageLinkedToHpProcedure);
        }
    }
    
    public abstract void setFight(final int p0);
    
    public abstract int substractKamas(final int p0);
    
    public abstract BasicCharacterInfo summonMonster(final long p0, final Point3 p1, final short p2, final BasicInvocationCharacteristics p3, final boolean p4, final PropertyManager<FightPropertyType> p5);
    
    public boolean isStaff() {
        return false;
    }
    
    @Override
    public void onCheckOut() {
        this.m_summonCharacteristics = null;
        this.m_inPool = false;
        this.m_checkInStackTrace = null;
        this.m_id = 0L;
        this.m_ownerId = 0L;
        this.m_type = this.defaultCharacterType();
        this.m_instanceId = 0;
        this.m_sex = 0;
        this.m_name = "<undefined>";
        this.m_breed = this.defaultBreed();
        this.m_isKO = false;
        this.m_isDead = false;
        this.m_direction = Direction8.NORTH_EAST;
        this.m_characterInfoEventsHandler.clear();
        this.m_isControlledByAI = false;
        this.m_isControlledByAI = this.returnDefaultAIControl();
        this.m_carrier = null;
        this.m_currentCarryTarget = null;
        this.m_citizenComportment.reset();
        this.m_obstacleId = -1;
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_summonCharacteristics != null) {
            this.m_summonCharacteristics.clean();
        }
        this.m_summonCharacteristics = null;
        this.m_forcedGfxId = 0;
        this.m_inGame = false;
        this.m_aggressiveList = null;
        this.m_breed = this.defaultBreed();
        this.m_characterInfoEventsHandler.clear();
        this.m_direction = Direction8.NORTH_EAST;
        this.m_id = 0L;
        this.m_instanceId = 0;
        this.m_isControlledByAI = false;
        this.m_isKO = false;
        this.m_isDead = false;
        this.m_isPlayBeingChanged = false;
        this.m_isSummoned = false;
        this.m_summonedFromSymbiot = false;
        this.m_alreadyReturnedToSymbiot = false;
        this.m_name = "<undefined>";
        this.m_onFight = false;
        this.m_onSpecialMovement = null;
        this.m_ownerId = -1L;
        this.m_position.set(-32000, -32000, (short)(-1));
        if (this.m_runningEffectManager != null) {
            this.m_runningEffectManager.destroyAll();
            this.m_runningEffectManager = null;
        }
        this.m_stateAffinities.clear();
        this.m_spellCostModification.clear();
        this.m_sex = -1;
        this.m_spellCastHistory.reset();
        this.m_type = -1;
        this.m_worldProperties = null;
        if (this.m_carrier != null) {
            this.m_carrier.forceUncarry();
        }
        this.m_carrier = null;
        if (this.m_currentCarryTarget != null) {
            this.m_currentCarryTarget.onUncarryEvent();
        }
        this.m_currentCarryTarget = null;
        this.unloadFightData();
        this.m_inPool = true;
        this.m_citizenComportment.reset();
        this.m_isAbandonning = false;
        this.m_isFleeing = false;
        this.m_hasBeenRaisedByTrigger = false;
        this.m_updateEffectDelegate = null;
        this.m_beginDelegateCount = 0;
        this.m_aggressiveList = null;
        this.m_forcedGfxId = 0;
    }
    
    @Override
    public void release() {
        this.onCheckIn();
    }
    
    protected abstract Breed defaultBreed();
    
    protected abstract byte defaultCharacterType();
    
    public boolean isInPool() {
        return this.m_inPool;
    }
    
    public String getCheckInStackTrace() {
        return this.m_checkInStackTrace;
    }
    
    @Override
    public void onJoinFight(final BasicFight basicFight) {
        this.m_finalHealResist = 0.0f;
        this.m_onFight = true;
        this.setDead(this.m_isFleeing = false);
        this.m_spellCastHistory.reset();
        this.notifyJoinFight();
    }
    
    @Override
    public void onLeaveFight() {
        this.m_finalHealResist = 0.0f;
        this.m_onFight = false;
        this.m_isKO = false;
        this.m_isFleeing = false;
        this.m_currentCarryTarget = null;
        this.m_carrier = null;
        if (this.isFighterNotLocalToCurrentFight()) {
            this.m_spellCastHistory.reset();
            this.reloadCharacter(this.m_ownContext);
        }
        this.notifyLeaveFight();
    }
    
    private boolean isFighterNotLocalToCurrentFight() {
        return !this.isFighterLocalToCurrentFight();
    }
    
    protected boolean isFighterLocalToCurrentFight() {
        final BasicFight<?> currentFight = (BasicFight<?>)this.getCurrentFight();
        return currentFight == null || currentFight.isLocalToFight(this);
    }
    
    protected final void reloadCharacter(final EffectContext context) {
        this.reloadCharacter(context, null);
    }
    
    protected final void reloadCharacter(final EffectContext context, final List<StateRunningEffect> statesToTransmigrate) {
        this.reloadCharacter(context, statesToTransmigrate, true);
    }
    
    protected void reloadCharacter(final EffectContext context, List<StateRunningEffect> statesToTransmigrate, final boolean clearManager) {
        try {
            if (statesToTransmigrate == null) {
                statesToTransmigrate = this.getStatesToTransmigrate(context);
            }
            final int hpValue = this.getCharacteristicValue(FighterCharacteristicType.HP);
            final PropertyManager<FightPropertyType> fightProperties = this.m_fightProperties;
            if (fightProperties != null) {
                fightProperties.reset();
            }
            if (clearManager) {
                this.getRunningEffectManager().setDoNotNotifyEffectUnapplication(true);
                try {
                    this.getRunningEffectManager().clear();
                }
                catch (Exception e) {
                    BasicCharacterInfo.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
                this.getRunningEffectManager().setDoNotNotifyEffectUnapplication(false);
            }
            this.onSpecialFighterEvent(new EffectClearedEvent());
            if (!this.m_isSummoned) {
                this.initialiseCharacteristicsToBaseValue();
            }
            this.reloadBuffs(context);
            this.transmigrateStates(statesToTransmigrate);
            this.setHpValue(hpValue);
        }
        catch (Exception e2) {
            BasicCharacterInfo.m_logger.error((Object)("Exception levee lors du rechargment d'un perso " + this), (Throwable)e2);
        }
    }
    
    public void setHpValue(final int hpValue) {
        this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(hpValue);
    }
    
    public abstract void returnToOriginalController();
    
    protected static <C extends BasicCharacterInfo> void returnToOriginalControllerUsingFight(final BasicFight<C> fight, final C fighter) {
        if (fight == null) {
            return;
        }
        fight.returnToOriginalController(fighter);
    }
    
    public void setCurrentController(final long controlledId, final long newControllerId) {
        if (this.getCurrentFight() == null) {
            return;
        }
        this.getCurrentFight().setCurrentController(controlledId, newControllerId);
    }
    
    @Override
    public void setObstacleId(final byte id) {
        this.m_obstacleId = id;
    }
    
    @Override
    public byte getObstacleId() {
        return this.m_obstacleId;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return true;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return true;
    }
    
    @Override
    public boolean isBlockingSight() {
        return !this.isActiveProperty(FightPropertyType.INVISIBLE_SUPERIOR) && (this.isActiveProperty(FightPropertyType.INVISIBLE_REVEALED) || !this.isActiveProperty(FightPropertyType.INVISIBLE)) && !this.isActiveProperty(FightPropertyType.DONT_BLOCK_LOS);
    }
    
    public short getPreviousWorldId() {
        return 0;
    }
    
    @Override
    public Point3 getPosition() {
        return (this.m_position != null) ? new Point3(this.m_position) : null;
    }
    
    public Point3 getLeaderPosition() {
        final long leader = HeroesLeaderManager.INSTANCE.getLeader(this.getOwnerId());
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(leader);
        return (hero == null) ? null : new Point3(hero.m_position);
    }
    
    @Override
    public EffectUser getResistanceTarget() {
        if (this.m_resistanceTarget != null) {
            return this.m_resistanceTarget;
        }
        return this;
    }
    
    public void setResistanceTarget(final EffectUser resistanceTarget) {
        this.m_resistanceTarget = resistanceTarget;
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
        assert alt >= -512 && alt <= 511 : "Altitude of the position is out of bounds : " + alt;
        final FightMap map = this.getFightMap();
        if (map != null && !this.isCarried() && map.checkObstacleId(this.m_obstacleId) && !this.m_wasCarriedOnLastCell) {
            map.moveObstacle(this, x, y);
        }
        this.m_position.set(x, y, alt);
        if (this.m_id != -1L && !this.checkPositionValidity()) {
            BasicCharacterInfo.m_logger.error((Object)("Position invalide pour un BasicCharacterInfo : " + this.m_position + " worldId=" + this.m_instanceId));
        }
        if (this.m_currentCarryTarget != null) {
            this.m_currentCarryTarget.setPosition(x, y, alt);
            this.m_currentCarryTarget.onPositionChanged();
        }
    }
    
    @Override
    public void setPosition(final Point3 pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    private FightMap getFightMap() {
        final BasicFight fight = this.getCurrentFight();
        if (fight == null) {
            return null;
        }
        return fight.getFightMap();
    }
    
    protected boolean checkPositionValidity() {
        return true;
    }
    
    @Override
    public void teleport(final int x, final int y, final short z) {
        throw new UnsupportedOperationException("Not implemented - this method must be overloaded");
    }
    
    @Nullable
    public ProtectorBase getProtector() {
        return null;
    }
    
    public void updateElementMastery(final Elements element) {
        if (element == null) {
            return;
        }
        if (!element.hasMasteryCharacteristic()) {
            return;
        }
        final SpellInventory<? extends AbstractSpellLevel> spellInventory = this.getSpellInventory();
        if (spellInventory == null) {
            return;
        }
        if (this.m_characteristics == null) {
            return;
        }
        if (this.getBreed() == null || !(this.getBreed() instanceof AvatarBreed)) {
            return;
        }
        ((AvatarBreed)this.getBreed()).getMasteryCharacsCalculator().computeAndApply(element, this.m_characteristics, (Iterable<? extends AbstractSpellLevel>)spellInventory);
    }
    
    @Override
    public boolean hasCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics != null && this.m_characteristics.contains(charac);
    }
    
    @Override
    public FighterCharacteristic getCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics.getCharacteristic(charac);
    }
    
    @Override
    public int getCharacteristicValue(final CharacteristicType charac) throws UnsupportedOperationException {
        return (this.m_characteristics == null) ? 0 : this.m_characteristics.getCharacteristicValue(charac);
    }
    
    @Override
    public int getCharacteristicMax(final CharacteristicType charac) {
        return this.m_characteristics.getCharacteristicMaxValue(charac);
    }
    
    @Override
    public int getDodgeValue() {
        return this.getCharacteristicValue(FighterCharacteristicType.DODGE);
    }
    
    @Override
    public int getTackleValue() {
        return this.getCharacteristicValue(FighterCharacteristicType.TACKLE);
    }
    
    @Override
    public boolean canTackle() {
        return !this.isActiveProperty(FightPropertyType.CANT_TACKLE);
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        final int delta = direction.m_index - this.m_direction.m_index;
        this.m_direction = direction;
        if (this.m_currentCarryTarget != null) {
            this.m_currentCarryTarget.setDirection(this.m_currentCarryTarget.getDirection().getNextDirection8(delta));
        }
    }
    
    @Override
    public void setSpecialMovementDirection(final Direction8 direction) {
        this.m_onSpecialMovement = direction;
    }
    
    @Override
    public Direction8 getMovementDirection() {
        return (this.m_onSpecialMovement == null) ? this.m_direction : this.m_onSpecialMovement;
    }
    
    @Override
    public byte getHeight() {
        if (this.m_breed != null) {
            return this.m_breed.getHeight();
        }
        return 6;
    }
    
    @Override
    public boolean canBeTargeted() {
        return !this.isActiveProperty(FightPropertyType.CANNOT_BE_EFFECT_TARGET);
    }
    
    @Override
    public byte getPhysicalRadius() {
        if (this.m_breed != null) {
            return this.m_breed.getPhysicalRadius();
        }
        return 0;
    }
    
    @Override
    public short getJumpCapacity() {
        return 4;
    }
    
    @Override
    public boolean isActiveProperty(final PropertyType property) {
        switch (property.getPropertyTypeId()) {
            case 1: {
                if (this.m_fightProperties != null) {
                    return this.m_fightProperties.isActiveProperty((FightPropertyType)property);
                }
                break;
            }
            case 0: {
                if (this.m_worldProperties != null) {
                    return this.m_worldProperties.isActiveProperty((WorldPropertyType)property);
                }
                break;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasProperty(final PropertyType property) {
        return this.isActiveProperty(property);
    }
    
    public boolean isInvisible() {
        return this.isActiveProperty(FightPropertyType.INVISIBLE) || this.isActiveProperty(FightPropertyType.INVISIBLE_SUPERIOR);
    }
    
    @Override
    public byte getPropertyValue(final PropertyType property) {
        switch (property.getPropertyTypeId()) {
            case 1: {
                if (this.m_fightProperties != null) {
                    return this.m_fightProperties.getPropertyValue((FightPropertyType)property);
                }
                break;
            }
            case 0: {
                if (this.m_worldProperties != null) {
                    return this.m_worldProperties.getPropertyValue((WorldPropertyType)property);
                }
                break;
            }
        }
        return 0;
    }
    
    @Override
    public void setPropertyValue(final PropertyType property, final byte value) {
        switch (property.getPropertyTypeId()) {
            case 1: {
                if (this.m_fightProperties != null) {
                    this.m_fightProperties.add((FightPropertyType)property);
                }
                if (this.m_fightProperties != null) {
                    this.m_fightProperties.setPropertyValue((FightPropertyType)property, value);
                    break;
                }
                break;
            }
            case 0: {
                if (this.m_worldProperties == null) {
                    this.m_worldProperties = (PropertyManager<WorldPropertyType>)PropertyManager.newInstance((byte)0, this);
                }
                if (this.m_worldProperties != null) {
                    this.m_worldProperties.setPropertyValue((WorldPropertyType)property, value);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void addProperty(final PropertyType property) {
        switch (property.getPropertyTypeId()) {
            case 1: {
                if (this.m_fightProperties != null) {
                    this.m_fightProperties.add((FightPropertyType)property);
                    break;
                }
                break;
            }
            case 0: {
                if (this.m_worldProperties == null) {
                    this.m_worldProperties = (PropertyManager<WorldPropertyType>)PropertyManager.newInstance((byte)0, this);
                }
                if (this.m_worldProperties != null) {
                    this.m_worldProperties.add((WorldPropertyType)property);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void substractProperty(final PropertyType property) {
        if (property == null) {
            return;
        }
        switch (property.getPropertyTypeId()) {
            case 1: {
                if (this.m_fightProperties != null) {
                    this.m_fightProperties.substract((FightPropertyType)property);
                    break;
                }
                break;
            }
            case 0: {
                if (this.m_worldProperties == null) {
                    break;
                }
                this.m_worldProperties.substract((WorldPropertyType)property);
                if (this.m_worldProperties.isEmpty()) {
                    this.m_worldProperties = null;
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void removeProperty(final PropertyType property) {
        switch (property.getPropertyTypeId()) {
            case 1: {
                if (this.getRunningEffectManager() != null) {
                    this.getRunningEffectManager().removeLinkedToProperty((FightPropertyType)property);
                }
                if (this.m_fightProperties != null) {
                    this.m_fightProperties.remove((FightPropertyType)property);
                    break;
                }
                break;
            }
            case 0: {
                if (this.m_worldProperties == null) {
                    break;
                }
                this.m_worldProperties.remove((WorldPropertyType)property);
                if (this.m_worldProperties.isEmpty()) {
                    this.m_worldProperties = null;
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onPropertyUpdated(final PropertyType prop) {
    }
    
    @Override
    public boolean trigger(final BitSet triggers, final RunningEffect triggerer, final byte options) {
        return this.getRunningEffectManager() != null && this.getRunningEffectManager().trigger(triggers, triggerer, options);
    }
    
    @Override
    public boolean isValidForEffectExecution() {
        return true;
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
    public boolean mustGoOffPlay() {
        final BasicFight<?> fight = (BasicFight<?>)this.getCurrentFight();
        return (fight == null || fight.getModel().isAllowNoFightersInPlayAtEnd() || fight.getInPlayFightersCount() >= 2) && this.isOnFight() && this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).isZero() && !this.m_isDead && !this.m_isKO;
    }
    
    @Override
    public boolean mustGoBackInPlay() {
        return this.isOnFight() && this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).value() > 0 && !this.m_isDead && this.m_isKO && !this.m_isAbandonning;
    }
    
    @Override
    public boolean mustGoOutOfPlay() {
        return this.isOnFight() && (this.getCharacteristic((CharacteristicType)FighterCharacteristicType.KO_TIME_BEFORE_DEATH).isZero() || (this.getCurrentFight() != null && this.getCurrentFight().getFighterInPlayInTeamCountingForFightEnd(this.getTeamId()).isEmpty())) && this.m_isKO && this.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).value() <= 0;
    }
    
    @Override
    public boolean isOffPlay() {
        return this.isOnFight() && this.getCurrentFight() != null && this.getCurrentFight().isOffPlay(this);
    }
    
    @Override
    public boolean isOutOfPlay() {
        return this.isOnFight() && this.getCurrentFight() != null && this.getCurrentFight().isOutOfPlay(this);
    }
    
    @Override
    public boolean isInPlay() {
        return this.isOnFight() && this.getCurrentFight() != null && this.getCurrentFight().isInPlay(this);
    }
    
    @Override
    public void onGoesOffPlay() {
        this.m_isKO = true;
    }
    
    @Override
    public void onBackInPlay() {
        this.m_isKO = false;
    }
    
    public void setAbandonning(final boolean abandonning) {
        this.m_isAbandonning = abandonning;
    }
    
    public boolean isAbandonning() {
        return this.m_isAbandonning;
    }
    
    @Override
    public void onGoesOutOfPlay() {
        if (this.uncarryAndRemoveCastedEffects()) {
            return;
        }
        if (!this.isActiveProperty(FightPropertyType.NO_DEATH) && !this.m_isFleeing) {
            this.setDead(true);
        }
    }
    
    private boolean uncarryAndRemoveCastedEffects() {
        if (this.m_carrier != null) {
            this.m_carrier.forceUncarry();
        }
        final BasicFight<?> currentFight = (BasicFight<?>)this.getCurrentFight();
        if (currentFight == null) {
            return true;
        }
        final Collection<? extends BasicCharacterInfo> fighters = (Collection<? extends BasicCharacterInfo>)currentFight.getFighters();
        for (final BasicCharacterInfo fighter : fighters) {
            this.getRunningEffectManager().removeLinkedToCaster(fighter);
        }
        return false;
    }
    
    @Override
    public boolean canChangePlayStatus() {
        return !this.m_isPlayBeingChanged;
    }
    
    @Override
    public void setUnderChange(final boolean bool) {
        this.m_isPlayBeingChanged = bool;
    }
    
    @Override
    public byte[] serializeFighterDatas() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeFighterDatas();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeFighterDataForReconnection() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeFighterDataForReconnection();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializePublicCharacteristics() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializePublicCharacteristics();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeAllCharacteristics() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeAllCharacteristics();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeFighterDatas(final byte[] serializedFighter) {
        this.unloadFightData();
        this.loadFightData();
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            serializer.fromBuild(serializedFighter);
        }
        else {
            BasicCharacterInfo.m_logger.error((Object)"Impossible de d\u00e9s\u00e9rialioser les donn\u00e9es binaire du fighter : pas de s\u00e9rialiseur d\u00e9fini.");
        }
    }
    
    @Override
    public byte[] serializeEffectUser() {
        return this.getRunningEffectManager().serializeInFight(false);
    }
    
    public byte[] serializeEffectUser(final boolean forIa) {
        return this.getRunningEffectManager().serializeInFight(forIa);
    }
    
    @Override
    public void unserializeEffectUser(final byte[] serializedEffectUserDatas) {
        this.getRunningEffectManager().unserializeInFight(serializedEffectUserDatas, this.getEffectContext());
    }
    
    @Override
    public byte getTeamId() {
        if (this.getCurrentFight() == null) {
            return -1;
        }
        return this.getCurrentFight().getTeamId(this);
    }
    
    @Override
    public void setTeamId(final byte teamId) {
        if (this.getCurrentFight() == null) {
            return;
        }
        this.getCurrentFight().setTeamId(this, teamId);
    }
    
    @Override
    public EffectContext<WakfuEffect> getAppropriateContext() {
        EffectContext<WakfuEffect> context = this.getEffectContext();
        if (context == null || context.getContextType() == 0) {
            context = this.m_ownContext;
        }
        return context;
    }
    
    public WakfuEffectContextForUniqueUser getHavenWorldEffectContext() {
        return this.m_havenWorldEffectContext;
    }
    
    public boolean castSpellEffects(final int spellReferenceId, final Point3 targetCell) {
        return true;
    }
    
    public void fromBuild(final byte[] serializedCharacter) {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            serializer.fromBuild(serializedCharacter);
        }
        else {
            BasicCharacterInfo.m_logger.error((Object)"Impossible de d\u00e9coder les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        }
    }
    
    @Override
    public short getWorldCellAltitude() {
        return this.m_position.getZ();
    }
    
    public void setAltitude(final short z) {
        this.m_position.setZ(z);
        if (this.m_currentCarryTarget != null) {
            this.m_currentCarryTarget.setPosition(this.m_position.getX(), this.m_position.getY(), z);
        }
    }
    
    public AbstractBagContainer getBags() {
        return null;
    }
    
    public String getControllerName() {
        return this.getName();
    }
    
    public final EffectContext<WakfuEffect> getEffectContext() {
        return (this.m_onFight && this.getCurrentFight() != null) ? this.getFightEffectContext() : this.getInstanceEffectContext();
    }
    
    public ItemEquipment getEquipmentInventory() {
        return null;
    }
    
    @Nullable
    public ShortcutInventory getShortcutInventory(final ShortCutBarType shortcutType, final byte shortcurBarNumber) {
        return null;
    }
    
    public EffectContext<WakfuEffect> getFightEffectContext() {
        if (this.getCurrentFight() != null) {
            return (EffectContext<WakfuEffect>)this.getCurrentFight().getContext();
        }
        return null;
    }
    
    public String getGameServer() {
        return null;
    }
    
    @Override
    public short getLevel() {
        return 0;
    }
    
    public abstract float getWakfuGaugeValue();
    
    @Override
    public String getName() {
        return (this.m_name != null) ? this.m_name : "UNDEFINED";
    }
    
    @Override
    public final Point3 getPositionConst() {
        return this.m_position;
    }
    
    public SkillInventory<? extends AbstractSkill> getSkillInventory() {
        return null;
    }
    
    public SpellInventory<? extends AbstractSpellLevel> getSpellInventory() {
        return null;
    }
    
    public Iterable<? extends AbstractSpellLevel> getPermanentSpellInventory() {
        return null;
    }
    
    public SpellLevelCastHistory getSpellLevelCastHistory() {
        return this.m_spellCastHistory;
    }
    
    @Override
    public int getSummoningsCount() {
        return this.getSummoningsCount(AvatarBreed.NONE.getBreedId());
    }
    
    @Override
    public int getSummoningsCount(final int breedId) {
        int summonings = 0;
        if (this.getCurrentFight() != null) {
            for (final BasicFighter basicFighter : this.getCurrentFight().getFighters()) {
                if (basicFighter instanceof BasicCharacterInfo) {
                    final BasicCharacterInfo basicCharacterInfo = (BasicCharacterInfo)basicFighter;
                    if (!basicCharacterInfo.m_isSummoned || basicCharacterInfo.m_isDead || basicCharacterInfo.getOriginalController() != this || basicCharacterInfo == this || basicCharacterInfo.hasProperty(FightPropertyType.BYPASS_SUMMONS_COUNT) || (breedId != AvatarBreed.NONE.getBreedId() && basicCharacterInfo.getBreedId() != breedId)) {
                        continue;
                    }
                    ++summonings;
                }
            }
        }
        else {
            for (final BasicCharacterInfo fighter : this.getControlled()) {
                if (fighter.m_isSummoned && !fighter.m_isDead && (breedId == AvatarBreed.NONE.getBreedId() || (fighter.getBreedId() == breedId && !fighter.hasProperty(FightPropertyType.BYPASS_SUMMONS_COUNT)))) {
                    ++summonings;
                }
            }
        }
        return summonings;
    }
    
    public int getSummoningLeadershipScore() {
        int score = 0;
        if (this.getCurrentFight() != null) {
            for (final BasicFighter basicFighter : this.getCurrentFight().getFighters()) {
                if (basicFighter instanceof BasicCharacterInfo) {
                    final BasicCharacterInfo basicCharacterInfo = (BasicCharacterInfo)basicFighter;
                    if (!basicCharacterInfo.m_isSummoned || basicCharacterInfo.m_isDead || basicCharacterInfo.getOriginalController() != this || basicCharacterInfo.hasProperty(FightPropertyType.BYPASS_SUMMONS_COUNT)) {
                        continue;
                    }
                    score += ((AbstractMonsterBreed)basicCharacterInfo.getBreed()).getLevelMax();
                }
            }
        }
        else {
            for (final BasicCharacterInfo fighter : this.getControlled()) {
                if (fighter.m_isSummoned && !fighter.m_isDead && !fighter.hasProperty(FightPropertyType.BYPASS_SUMMONS_COUNT)) {
                    score += ((AbstractMonsterBreed)fighter.getBreed()).getLevelMax();
                }
            }
        }
        return score;
    }
    
    public int getSymbiotCurrentCreatureLeadership() {
        final AbstractSymbiot symbiot = this.getSymbiot();
        if (symbiot == null) {
            return 0;
        }
        final BasicInvocationCharacteristics currentCreature = symbiot.getCurrentCreatureParameters();
        if (currentCreature == null) {
            return 0;
        }
        return currentCreature.getBreedMaxLevel();
    }
    
    public EffectContext<WakfuEffect> getInstanceEffectContext() {
        return null;
    }
    
    @Override
    public int getWorldCellX() {
        return this.m_position.getX();
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_position.getY();
    }
    
    @Override
    public float getWorldX() {
        return this.getWorldCellX();
    }
    
    @Override
    public float getWorldY() {
        return this.getWorldCellY();
    }
    
    @Override
    public float getAltitude() {
        return this.getWorldCellAltitude();
    }
    
    protected void initializeSerializer() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            new CharacterInfoPartId(serializer.getIdPart());
            new CharacterInfoPartIdentity(serializer.getIdentityPart());
            new CharacterInfoPartName(serializer.getNamePart());
            new CharacterInfoPartBreed(serializer.getBreedPart());
            new CharacterInfoPartSex(serializer.getSexPart());
            new CharacterInfoPartPosition(serializer.getPositionPart());
            new CharacterInfoPartFight(serializer.getFightPart());
            new CharacterInfoPartFightCharacteristics(serializer.getFightCharacteristicsPart());
            new CharacterInfoPartAllCharacteristics(serializer.getAllCharacteristicsPart());
            new CharacterInfoPartWorldProperties(serializer.getWorldPropertiesPart());
            new CharacterInfoPartFightProperties(serializer.getFightPropertiesPart());
            new CharacterInfoPartControlledByAI(serializer.getControlledByAIPart());
        }
        else {
            BasicCharacterInfo.m_logger.error((Object)"Pas de s\u00e9rialiseur \u00e0 initialiser : le personnage ne sera pas s\u00e9rialisable !");
        }
    }
    
    public boolean isControlledByAI() {
        return this.m_isControlledByAI;
    }
    
    @Override
    public boolean isSummoned() {
        return this.m_isSummoned;
    }
    
    @Override
    public boolean isSummonedFromSymbiot() {
        return this.m_summonedFromSymbiot;
    }
    
    public boolean isNotEcosystemNpc() {
        return this.m_isSummoned || this.isActiveProperty(WorldPropertyType.IS_ARCADE_NPC);
    }
    
    public void loadFightData() {
        if (this.m_fightProperties == null) {
            this.m_fightProperties = (PropertyManager<FightPropertyType>)PropertyManager.newInstance((byte)1, this);
        }
    }
    
    public void registerCharacterInfoPropertyEventsHandler(final CharacterInfoPropertyEventsHandler handler) {
        if (!this.m_characterInfoEventsHandler.contains(handler)) {
            this.m_characterInfoEventsHandler.add(handler);
        }
    }
    
    public void reloadItemEffects() {
        this.m_itemEffectsApplier.reloadItemEffects();
    }
    
    public void setApPmPwToMax() {
        this.setCharacToMaxValue(this.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP));
        this.setCharacToMaxValue(this.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP));
        this.setCharacToMaxValue(this.getCharacteristic((CharacteristicType)FighterCharacteristicType.WP));
    }
    
    private void setCharacToMaxValue(final AbstractCharacteristic characteristic) {
        if (characteristic != null) {
            characteristic.toMax();
        }
    }
    
    protected void reloadAptitudeEffects(final EffectContext context) {
        this.unloadAptitudeEffects();
        final AptitudeInventory aptitudeInventory = this.getAptitudeInventory();
        if (aptitudeInventory == null) {
            return;
        }
        for (final Aptitude aptitude : aptitudeInventory) {
            if (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.NEW_APTITUDE_ENABLED) && aptitude.getReferenceAptitude().getType() == AptitudeType.COMMON) {
                continue;
            }
            for (final WakfuEffect effect : aptitude) {
                if (effect.getEffectType() == 0) {
                    effect.execute(aptitude, this, context, RunningEffectConstants.getInstance(), this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this, null, false);
                }
            }
        }
    }
    
    public void reloadNewAptitudeEffects(final EffectContext context) {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.NEW_APTITUDE_ENABLED)) {
            return;
        }
        final AptitudeBonusInventory aptitudeBonusInventory = this.getAptitudeBonusInventory();
        if (aptitudeBonusInventory == null) {
            return;
        }
        this.getRunningEffectManager().removeLinkedToContainerType(36, true, false);
        aptitudeBonusInventory.forEachBonus(new TIntShortProcedure() {
            @Override
            public boolean execute(final int bonusId, final short level) {
                final AptitudeBonusModel aptitudeBonusModel = AptitudeBonusModelManager.INSTANCE.get(bonusId);
                if (aptitudeBonusModel == null) {
                    return true;
                }
                final int effectId = aptitudeBonusModel.getEffectId();
                final WakfuEffect effect = AptitudeBonusEffectManager.INSTANCE.getEffect(effectId);
                if (effect == null) {
                    return true;
                }
                final EffectContainer container = new AptitudeBonusEffectContainer(aptitudeBonusModel, level);
                final BasicCharacterInfo caster = BasicCharacterInfo.this;
                effect.execute(container, caster, context, RunningEffectConstants.getInstance(), caster.getWorldCellX(), caster.getWorldCellY(), caster.getWorldCellAltitude(), caster, null, false);
                return true;
            }
        });
    }
    
    public void unloadAptitudeEffects() {
        this.getRunningEffectManager().removeLinkedToContainerType(17);
    }
    
    public void unloadProtectorBuffs() {
        this.getRunningEffectManager().removeLinkedToContainerType(19);
    }
    
    public Collection<ZoneBuffInstance> getActiveZoneBuffs() {
        return null;
    }
    
    public final String dumpZoneEffects() {
        String result = "";
        for (final RunningEffect effect : this.getRunningEffectManager().getEffectsWithContainerType(18)) {
            result = result + effect.getClass().getSimpleName() + " zoneBuffId=" + effect.getEffectContainer().getEffectContainerId() + '\n';
        }
        return result;
    }
    
    public void reloadZoneEffects() {
        this.getRunningEffectManager().removeLinkedToContainerType(18);
        final Collection<ZoneBuffInstance> zoneBuffs = this.getActiveZoneBuffs();
        if (zoneBuffs != null) {
            for (final ZoneBuffInstance buffInstance : zoneBuffs) {
                for (final WakfuEffect effect : buffInstance.getBuff()) {
                    if (effect.getEffectType() == 0) {
                        effect.execute(buffInstance.getBuff(), this, this.m_ownContext, RunningEffectConstants.getInstance(), this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this, null, false);
                    }
                }
            }
        }
    }
    
    public void onCitizenScoreChanged(final int nationId, final CitizenComportment comportment, final CitizenRank oldRank, final int newPoints, final int deltaScore) {
        if (comportment.getNationId() != nationId) {
            return;
        }
        final CitizenRank newRank = CitizenRankManager.getInstance().getRankFromCitizenScore(newPoints);
        if (newRank == oldRank) {
            return;
        }
        if (this.isOnFight()) {
            return;
        }
        if (oldRank.hasRule(CitizenRankRule.CAN_RECEIVE_MDC_BONUS) && !newRank.hasRule(CitizenRankRule.CAN_RECEIVE_MDC_BONUS)) {
            this.unloadProtectorBuffs();
        }
        if (newRank.hasRule(CitizenRankRule.CAN_RECEIVE_MDC_BONUS) && !oldRank.hasRule(CitizenRankRule.CAN_RECEIVE_MDC_BONUS)) {
            this.reloadProtectorBuffs();
        }
    }
    
    public void reloadProtectorBuffs() {
        this.getRunningEffectManager().removeLinkedToContainerType(19);
        if (this.m_citizenComportment == null) {
            return;
        }
        if (!this.hasCitizenRankRule(CitizenRankRule.CAN_RECEIVE_MDC_BONUS)) {
            return;
        }
        final Nation nation = this.m_citizenComportment.getNation();
        final IntArray buffs = nation.getAllProtectorsBuffs();
        if (buffs == null) {
            BasicCharacterInfo.m_logger.info((Object)"buffs nuls impossible de recharger les buff");
            return;
        }
        for (int i = 0, size = buffs.size(); i < size; ++i) {
            final int buffId = buffs.getQuick(i);
            final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(buffId);
            if (buff != null) {
                for (final WakfuEffect effect : buff) {
                    if (effect.getEffectType() == 0) {
                        effect.execute(buff, this, this.m_ownContext, RunningEffectConstants.getInstance(), this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this, null, false);
                    }
                }
            }
            else {
                BasicCharacterInfo.m_logger.error((Object)("Impossible d'appliquer ce buff (ID=" + buffId + "), il est introuvable."));
            }
        }
    }
    
    public void reloadHavenWorldBuffs() {
    }
    
    public void reloadGuildBuffs() {
    }
    
    public void reloadSubscriptionState() {
    }
    
    public void reloadAntiAddictionBuffs() {
    }
    
    private boolean hasCitizenRankRule(final CitizenRankRule rule) {
        final Nation nation = this.m_citizenComportment.getNation();
        final int score = this.m_citizenComportment.getCitizenScoreForNation(nation.getNationId());
        return CitizenRankManager.getInstance().getRankFromCitizenScore(score).hasRule(rule);
    }
    
    public AptitudeInventory getAptitudeInventory() {
        return null;
    }
    
    public AptitudeBonusInventory getAptitudeBonusInventory() {
        return null;
    }
    
    public final byte levelUpAptitude(final short aptitudeId, final boolean consumePoints) {
        final AptitudeInventory aptitudeInventory = this.getAptitudeInventory();
        if (aptitudeInventory == null) {
            BasicCharacterInfo.m_logger.error((Object)("Impossible de leveler les aptitudes d'un " + this.getClass().getName() + " qui n'en poss\u00e8de pas"));
            return 1;
        }
        final Aptitude aptitude = aptitudeInventory.getWithUniqueId(aptitudeId);
        if (aptitude == null) {
            BasicCharacterInfo.m_logger.error((Object)("Le personnage " + this + " ne poss\u00e8de pas l'aptitude " + aptitudeId));
            return 1;
        }
        if (!aptitude.getReferenceAptitude().hasBreed(this.getBreed())) {
            BasicCharacterInfo.m_logger.error((Object)("Mauvaise breed pour augmenter l'aptitude " + aptitudeId + " : attendu=" + aptitude.getReferenceAptitude().toString() + ", trouv\u00e9=" + this.getBreed()));
            return 1;
        }
        final short aptitudeLevel = aptitude.getLevel();
        if (aptitudeLevel < 0 || aptitudeLevel > aptitude.getReferenceAptitude().getMaxLevel()) {
            BasicCharacterInfo.m_logger.error((Object)("Niveau invalide pour l'aptitude " + aptitude + " du personnage " + this + " : " + aptitudeLevel));
            return 1;
        }
        if (aptitudeLevel == aptitude.getReferenceAptitude().getMaxLevel()) {
            return 3;
        }
        final int linkedSpellId = aptitude.getReferenceAptitude().getLinkedSpellId();
        final ArrayList<? extends AbstractSpellLevel> spells = this.getSpellInventory().getAllWithReferenceId(linkedSpellId);
        if (linkedSpellId != 0 && spells.isEmpty()) {
            return 1;
        }
        final int unlockLevel = aptitude.getReferenceAptitude().getLevelUnlock((short)(aptitudeLevel + 1));
        if (unlockLevel > this.getLevel()) {
            BasicCharacterInfo.m_logger.error((Object)(this + " n'a pas le niveau pour augmenter l'aptitude " + aptitudeId + " jusqu'au niveau " + (aptitude.getLevel() + 1) + " : requis=" + unlockLevel + ", actuellement=" + this.getLevel()));
            return 4;
        }
        if (consumePoints) {
            final AptitudeType type = aptitude.getReferenceAptitude().getType();
            final int requiredPoints = aptitude.getPointsForLevel((short)(aptitudeLevel + 1), this);
            final int availablePoints = aptitudeInventory.getAvailablePoints(type);
            if (availablePoints < requiredPoints) {
                BasicCharacterInfo.m_logger.error((Object)("Pas assez de points \u00e0 " + this + " pour augmenter l'aptitude " + aptitudeId + " jusqu'au niveau " + (aptitude.getLevel() + 1) + " : requis=" + requiredPoints + ", actuellement=" + availablePoints));
                return 2;
            }
            aptitudeInventory.setAvailablePoints(type, availablePoints - requiredPoints);
        }
        aptitude.setLevel((short)(aptitudeLevel + 1));
        this.reloadAptitudeEffects(this.m_ownContext);
        this.reloadPassiveSpells();
        return 0;
    }
    
    public void reset() {
        this.m_id = -1L;
        this.m_ownerId = -1L;
        this.m_name = "";
        this.setPosition(0, 0, (short)510);
        this.m_instanceId = -1;
        this.m_characterInfoEventsHandler.clear();
        this.m_sex = 0;
        this.m_direction = Direction8.NORTH_EAST;
    }
    
    public byte[] serialize() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serialize();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForCharacterList() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForCharacterList();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForCharacterPositionInformation() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForCharacterPositionInformation();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForCreationCharacterInformation() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForCharacterCreationInformation();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForLocalCharacterInformation() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForLocalCharacterInformation();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForAccountInformationUpdate() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForAccountInformationUpdate();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForAntiAddiction() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForAntiAddictionUpdate();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForAptitudeBonusInventory() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForAptitudeBonusInventory();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForRemoteAccountInformationUpdate() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForRemoteAccountInformationUpdate();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForRemotePetUpdate() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForRemotePetUpdate();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForDiscoveredItemsUpdate() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForDiscoveredItemsUpdate();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForHeroLoad() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForHeroLoad();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForPropertiesUpdate(final boolean withFightPart) {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForPropertiesUpdate(withFightPart);
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForRemoteCharacterInformation() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            String name = null;
            if (this.isStaff()) {
                name = this.getName();
                this.setName("[STAFF] " + name);
            }
            final byte[] res = serializer.serializeForRemoteCharacterInformation();
            if (this.isStaff()) {
                this.setName(name);
            }
            return res;
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForRemoteUpdateCharacterInformation() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            String name = null;
            if (this.isStaff()) {
                name = this.getName();
                this.setName("[STAFF] " + name);
            }
            final byte[] res = serializer.serializeForRemoteUpdateCharacterInformation();
            if (this.isStaff()) {
                this.setName(name);
            }
            return res;
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForGlobalServerExchange() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForWorldToGlobalExchange();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForGlobalToGameServerExchange() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForGlobalToGameServerExchange();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForNationPvp() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForNationPvp();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeForGlobalToLocalClient() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForGlobalToLocalClient();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializeInventories() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeInventories();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public byte[] serializePersonalEffects() {
        final CharacterSerializer serializer = this.getSerializer();
        if (serializer != null) {
            return serializer.serializeForPersonalEffects();
        }
        BasicCharacterInfo.m_logger.error((Object)"Impossible de s\u00e9rialiser les donn\u00e9es binaire du personnage : pas de s\u00e9rialiseur d\u00e9fini.");
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    public void setAIOrder(final int orderId, final Object target) {
    }
    
    public void cancelAIOrder(final int orderId, final Object target) {
    }
    
    public void setControlledByAI(final boolean controlledByAI) {
        this.m_isControlledByAI = controlledByAI;
    }
    
    public void setGameServer(final String gameServer) {
    }
    
    public void setForcedGfxId(final int forcedGfxId) {
        this.m_forcedGfxId = forcedGfxId;
    }
    
    public int getGfxId() {
        return this.m_forcedGfxId;
    }
    
    public void setGfxId(final int gfxId) {
    }
    
    public void setLevel(final short level) {
        throw new UnsupportedOperationException("Only monsters can set level");
    }
    
    public void setScripedOrder(final int orderId, final int param, final Object target) {
    }
    
    public void setSummoned(final boolean isSummoned) {
        this.m_isSummoned = isSummoned;
    }
    
    public boolean applyItemOnEquipEffect(final Item item) {
        return this.m_itemEffectsApplier.applyItemOnEquipEffect(item);
    }
    
    public void unapplyItemOnEquipEffect(final Item item) {
        this.m_itemEffectsApplier.unapplyItemOnEquipEffect(item);
    }
    
    public void unapplyItemOnEquipEffect(final Item item, final AbstractItemSet set) {
        this.m_itemEffectsApplier.unapplyItemOnEquipEffect(item, set);
    }
    
    public void unloadFightData() {
        this.m_fightProperties = null;
        this.m_isPlayBeingChanged = false;
        this.clearAggressiveList();
    }
    
    public void unregisterAllCharacterInfoPropertyEventsHandlers() {
        this.m_characterInfoEventsHandler.clear();
    }
    
    protected void updateCharacBoundWithLevel() {
        for (final CharacBoundByLevel characBoundByLevel : CharacBoundByLevelTable.getInstance().getCharacBoundsByLevel()) {
            final byte characId = characBoundByLevel.getCharacId();
            final int bound = characBoundByLevel.getBound(this.getLevel());
            final FighterCharacteristicType characType = FighterCharacteristicType.getCharacteristicTypeFromId(characId);
            if (characType != null && this.hasCharacteristic(characType)) {
                this.getCharacteristic((CharacteristicType)characType).setUpperBound(bound);
            }
        }
    }
    
    public void useItem(final byte equipmentPosition, final Point3 targetCell, final boolean withUseCost, final AbstractSpellLevel associatedWithThisSpellLevel) {
    }
    
    @Override
    public Breed getBreed() {
        return this.m_breed;
    }
    
    @Override
    public short getBreedId() {
        if (this.m_breed != null) {
            return this.m_breed.getBreedId();
        }
        return -1;
    }
    
    public CharacteristicManager<FighterCharacteristic> getCharacteristics() {
        return this.m_characteristics;
    }
    
    @Override
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    @Nullable
    public PropertyManager<FightPropertyType> getFightProperties() {
        return this.m_fightProperties;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public byte getEffectUserType() {
        return 20;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    @Nullable
    protected Point3 getDimensionalBagPosition() {
        return null;
    }
    
    protected short getDimensionalBagInstanceId() {
        return -1;
    }
    
    protected void setDimensionalBagPosition(final Point3 pos) {
    }
    
    protected void setDimensionalBagInstanceId(final short instanceId) {
    }
    
    @Override
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    @Override
    public PartLocalisator getPartLocalisator() {
        if (this.isActiveProperty(FightPropertyType.DO_NOT_USE_LOCALISATION)) {
            return null;
        }
        this.m_partLocalisator.update(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this.m_direction);
        return this.m_partLocalisator;
    }
    
    @Override
    public TimedRunningEffectManager getRunningEffectManager() {
        this.createRunningEffectManagerIfNecessary();
        return this.m_runningEffectManager;
    }
    
    public boolean hasRunnintEffectManager() {
        return this.m_runningEffectManager != null;
    }
    
    public void createRunningEffectManagerIfNecessary() {
        if (this.m_runningEffectManager == null) {
            this.m_runningEffectManager = new TimedRunningEffectManager(this);
        }
    }
    
    @Override
    public byte getSex() {
        return this.m_sex;
    }
    
    @Override
    public byte getType() {
        return this.m_type;
    }
    
    @Nullable
    public PropertyManager<WorldPropertyType> getWorldProperties() {
        return this.m_worldProperties;
    }
    
    public void setDead(final boolean dead) {
        if (this.m_isDead != dead) {
            this.m_isDead = dead;
            this.onDeadChange();
        }
    }
    
    public boolean isDead() {
        return this.m_isDead;
    }
    
    protected void onDeadChange() {
    }
    
    public boolean isInGame() {
        return this.m_inGame;
    }
    
    public void setBreed(final Breed breed) {
        this.m_breed = breed;
    }
    
    public BonusPointCharacteristics getBonusPointCharacteristics() {
        return null;
    }
    
    @Override
    public void setId(final long id) {
        this.m_id = id;
    }
    
    public void setInGame(final boolean inGame) {
        this.m_inGame = inGame;
    }
    
    public void setInstanceId(final short instanceId) {
        this.m_instanceId = instanceId;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setOnFight(final boolean onFight) {
        this.m_onFight = onFight;
    }
    
    public void setOwnerId(final long ownerId) {
        this.m_ownerId = ownerId;
    }
    
    public void setSex(final byte sex) {
        this.m_sex = sex;
    }
    
    public void setType(final byte type) {
        this.m_type = type;
    }
    
    public void linkTo(final BasicCharacterInfo linkedCharacter) {
        if (linkedCharacter == null) {
            return;
        }
        this.m_linkedCharacter = linkedCharacter;
    }
    
    public void unlink() {
        this.m_linkedCharacter = null;
    }
    
    public BasicCharacterInfo getLinkedCharacter() {
        return this.m_linkedCharacter;
    }
    
    @Override
    public CarryTarget getCurrentCarryTarget() {
        return this.m_currentCarryTarget;
    }
    
    @Override
    public Carrier getCarrier() {
        return this.m_carrier;
    }
    
    @Override
    public boolean isCarrying() {
        return this.m_currentCarryTarget != null;
    }
    
    @Override
    public boolean isCarried() {
        return this.m_carrier != null;
    }
    
    @Override
    public boolean canJumpFromCarrier() {
        return !this.isActiveProperty(FightPropertyType.CANT_JUMP_FROM_CARRIER);
    }
    
    @Override
    public boolean canBeCarriedBy(final Carrier carrier) {
        return !this.isCarrying() && !this.isCarried();
    }
    
    @Override
    public boolean canCarry(final CarryTarget target) {
        return target != null && !this.isCarrying() && !this.isCarried() && target != this && target.canBeCarriedBy(this) && (target.getWorldCellAltitude() <= this.getWorldCellAltitude() + this.getHeight() / 2 || target.getWorldCellAltitude() >= this.getWorldCellAltitude() - this.getHeight() / 2);
    }
    
    @Override
    public boolean carry(final CarryTarget target) {
        if (!this.canCarry(target)) {
            return false;
        }
        if (target == null) {
            return false;
        }
        final Point3 position = this.getPosition();
        (this.m_currentCarryTarget = target).onCarryEvent(this);
        target.setPosition(position.getX(), position.getY(), position.getZ());
        target.onPositionChanged();
        return true;
    }
    
    @Override
    public void onCarryEvent(final Carrier carrier) {
        this.m_carrier = carrier;
        final FightMap map = this.getFightMap();
        if (map != null) {
            map.modifyObstacle(this, false);
        }
    }
    
    @Override
    public void onUncarryEvent() {
        this.m_carrier = null;
        final FightMap map = this.getFightMap();
        if (map != null) {
            map.modifyObstacle(this, true);
        }
    }
    
    @Override
    public boolean uncarryTo(final Point3 pos) {
        if (this.m_currentCarryTarget != null) {
            if (!this.m_position.equals(pos)) {
                final Direction8 direction = new Vector3i(pos.getX() - this.m_position.getX(), pos.getY() - this.m_position.getY(), 0).toDirection4();
                this.setDirection(direction);
            }
            this.m_currentCarryTarget.setPosition(pos.getX(), pos.getY(), pos.getZ());
            this.m_currentCarryTarget.onUncarryEvent();
            this.m_currentCarryTarget = null;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean uncarryTo_effect(final Point3 pos) {
        return this.uncarryTo(pos);
    }
    
    @Override
    public void forceUncarry() {
        if (this.m_currentCarryTarget != null) {
            final Point3 pos = this.getPosition();
            this.uncarryTo(pos);
        }
    }
    
    public boolean addAvailableTitle(final int titleId) {
        return false;
    }
    
    public void reloadPassiveSpells() {
        this.getRunningEffectManager().removeLinkedToContainerType(25);
        if (this.getSpellInventory() == null) {
            return;
        }
        if (this.isOnFight() && this.getCurrentFight() != null && !this.getCurrentFight().getModel().isWithPassiveSpells()) {
            return;
        }
        for (final AbstractSpellLevel spellLevel : this.getSpellInventory()) {
            final AbstractSpell spell = spellLevel.getSpell();
            if (spellLevel.getLevel() <= 0 && !spell.isPassiveEnabledFromStart()) {
                continue;
            }
            if (!spell.isPassive()) {
                continue;
            }
            final WakfuEffectContainer effectContainer = this.getSpellInventory().getFirstWithReferenceId(spell.getId());
            for (final WakfuEffect e : effectContainer) {
                e.execute(spellLevel, this, this.getAppropriateContext(), RunningEffectConstants.getInstance(), this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this, null, false);
            }
        }
    }
    
    public final void reloadBuffs(final EffectContext context) {
        this.reloadAptitudeEffects(context);
        this.reloadNewAptitudeEffects(context);
        this.reloadPassiveSpells();
        this.reloadZoneEffects();
        this.reloadProtectorBuffs();
        this.reloadHavenWorldBuffs();
        this.reloadGuildBuffs();
        this.reloadItemEffects();
        this.reloadAntiAddictionBuffs();
        this.reloadSubscriptionState();
    }
    
    protected final void initializeRunningEffectManagerAndLoadEffects() {
        this.getRunningEffectManager().initialize(this, this.getEffectContext());
        this.reloadBuffs(this.m_ownContext);
    }
    
    protected final List<StateRunningEffect> getStatesToTransmigrate(final EffectContext context) {
        final List<StateRunningEffect> effectList = this.getRunningEffectManager().getRunningState();
        final Iterator<StateRunningEffect> runningEffectIterator = effectList.iterator();
        final List<StateRunningEffect> transmigratedRunningEffect = new ArrayList<StateRunningEffect>();
        while (runningEffectIterator.hasNext()) {
            final StateRunningEffect runningEffect = runningEffectIterator.next();
            if (runningEffect.getState().isTransmigrable()) {
                final StateRunningEffect effect = runningEffect.transmigrate(context);
                transmigratedRunningEffect.add(effect);
            }
        }
        return transmigratedRunningEffect;
    }
    
    protected void transmigrateStates(final List<StateRunningEffect> transmigratedRunningEffect) {
        for (final StateRunningEffect aTransmigratedRunningEffect : transmigratedRunningEffect) {
            RunningEffect.resetLimitedApplyCount();
            final StateRunningEffect runningEffect = aTransmigratedRunningEffect;
            runningEffect.setNotified(true);
            runningEffect.setCaster(this);
            runningEffect.setTarget(this);
            runningEffect.enableValueComputation();
            runningEffect.applyOnTargets(this);
            runningEffect.release();
        }
    }
    
    @Deprecated
    public void applyState(final short stateId, final short level, final int containerType, final WakfuEffect genericEffect) {
        this.applyState(stateId, level, containerType, genericEffect, false);
    }
    
    @Deprecated
    public void applyState(final short stateId, final short level, final int containerType, final WakfuEffect genericEffect, final boolean notifyForced) {
        final EffectContext<WakfuEffect> context = (this.getCurrentFight() != null) ? this.getCurrentFight().getContext() : this.getInstanceEffectContext();
        final WakfuEffectContainerBuilder containerBuilder = new WakfuEffectContainerBuilder();
        RunningEffect.resetLimitedApplyCount();
        final ApplyState state = ApplyState.checkout(context, this, stateId, level, false);
        state.setCaster(this);
        state.setNotifyForced(notifyForced);
        containerBuilder.setContainerType(containerType).setContainerId(state.getId());
        ((RunningEffect<FX, WakfuEffectContainer>)state).setEffectContainer(containerBuilder.build());
        ((RunningEffect<WakfuEffect, EC>)state).setGenericEffect(genericEffect);
        state.execute(null, false);
    }
    
    public void applyStateEffects(final short stateId) {
        this.applyStateEffects(stateId, (short)0, this.getInstanceEffectContext());
    }
    
    public void applyStateEffects(final short stateId, final short level) {
        this.applyStateEffects(stateId, level, this.getInstanceEffectContext());
    }
    
    public void applyStateEffects(final short stateId, final short level, final EffectContext context) {
        final State state = StateManager.getInstance().getState(stateId);
        final State container = state.instanceAnother(level);
        final ArrayList<WakfuEffect> effects = state.getEffectsForLevelAsList(level);
        for (int i = 0, n = effects.size(); i < n; ++i) {
            final WakfuEffect wakfuEffect = effects.get(i);
            wakfuEffect.execute(container, this, context, RunningEffectConstants.getInstance(), this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this, null, false);
        }
    }
    
    public void unapplyStateEffects(final short stateId) {
        this.getRunningEffectManager().removeStatesFromId(stateId);
    }
    
    @NotNull
    public abstract SkillCharacteristics getSkillCharacteristics();
    
    public long getTotalLeadershipNeededForCreature(final BasicInvocationCharacteristics creature) {
        return this.getSummoningLeadershipScore() + creature.getBreedMaxLevel();
    }
    
    public long getTotalLeadershipNeededForCurrentCreature() {
        return this.getSummoningLeadershipScore() + this.getSymbiotCurrentCreatureLeadership();
    }
    
    public void resetCustomHpRegen() {
    }
    
    @Override
    public boolean hasState(final long stateId, final long stateLevel) {
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId() && ((StateRunningEffect)runningEffect).getState().getStateBaseId() == stateId) {
                return stateLevel == ((StateRunningEffect)runningEffect).getState().getLevel();
            }
        }
        return false;
    }
    
    @Override
    public boolean hasStateFromLevel(final long stateId, final long level) {
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId() && ((StateRunningEffect)runningEffect).getState().getStateBaseId() == stateId) {
                return level <= ((StateRunningEffect)runningEffect).getState().getLevel();
            }
        }
        return false;
    }
    
    @Override
    public boolean hasStateFromUser(final long stateId, final long stateLevel, final CriterionUser casterUser) {
        if (this.getRunningEffectManager() == null) {
            return false;
        }
        if (casterUser == null) {
            return false;
        }
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                final State state = ((StateRunningEffect)runningEffect).getState();
                if (state.getStateBaseId() != stateId) {
                    continue;
                }
                return state.getLevel() == stateLevel && runningEffect.getCaster() != null && runningEffect.getCaster().getId() == casterUser.getId();
            }
        }
        return false;
    }
    
    @Override
    public boolean hasStateFromUser(final long stateId, final CriterionUser casterUser) {
        if (this.getRunningEffectManager() == null) {
            return false;
        }
        if (casterUser == null) {
            return false;
        }
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                final State state = ((StateRunningEffect)runningEffect).getState();
                if (state.getStateBaseId() != stateId) {
                    continue;
                }
                return runningEffect.getCaster() != null && runningEffect.getCaster().getId() == casterUser.getId();
            }
        }
        return false;
    }
    
    @Override
    public boolean hasState(final long stateId) {
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId() && ((StateRunningEffect)runningEffect).getState().getStateBaseId() == stateId) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getStateLevel(final long stateId) {
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.RUNNING_STATE.getId() && ((StateRunningEffect)runningEffect).getState().getStateBaseId() == stateId) {
                return ((StateRunningEffect)runningEffect).getState().getLevel();
            }
        }
        return -1;
    }
    
    public boolean hasEffectWithActionId(final int actionId) {
        return this.getEffectWithActionId(actionId) != null;
    }
    
    public boolean hasEffectWithReferenceId(final int actionId) {
        return this.getEffectWithReferenceId(actionId) != null;
    }
    
    public RunningEffect getEffectWithActionId(final int actionId) {
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getId() == actionId) {
                return runningEffect;
            }
        }
        return null;
    }
    
    public RunningEffect getEffectWithReferenceId(final int effectId) {
        for (final RunningEffect runningEffect : this.getRunningEffectManager()) {
            if (runningEffect.getEffectId() == effectId) {
                return runningEffect;
            }
        }
        return null;
    }
    
    public void addStateAffinityUpdateListener(final StateAffinitiesUpdateListener listener) {
        this.m_stateAffinities.addUpdateListener(listener);
    }
    
    public void removeStateAffinityUpdateListener(final StateAffinitiesUpdateListener listener) {
        this.m_stateAffinities.removeUpdateListener(listener);
    }
    
    public void addStateResistance(final short stateId, final int value) {
        this.m_stateAffinities.addStateResistance(stateId, value);
    }
    
    public int getStateResistance(final short stateId) {
        return this.m_stateAffinities.getStateResistance(stateId);
    }
    
    public void addStateApplicationBonus(final short stateId, final int value) {
        this.m_stateAffinities.addApplicationBonus(stateId, value);
    }
    
    public int getStateApplicationBonus(final short stateId) {
        return this.m_stateAffinities.getApplicationBonus(stateId);
    }
    
    public boolean isFleeing() {
        return this.m_isFleeing;
    }
    
    public void setFleeing(final boolean fleeing) {
        this.m_isFleeing = fleeing;
    }
    
    public void createSpellInventoryFromRaw(final RawSpellLevelInventory rawSpellLevelInventory) {
    }
    
    public byte getControllerTeamId() {
        return (this.getController() == null) ? this.getTeamId() : this.getController().getTeamId();
    }
    
    public void setHasBeenRaisedByTrigger(final boolean hasBeenRaised) {
        this.m_hasBeenRaisedByTrigger = hasBeenRaised;
    }
    
    public boolean hasBeenRaisedByTrigger() {
        return this.m_hasBeenRaisedByTrigger;
    }
    
    public void onGobgobChangedLevel() {
        final int wp = this.getCharacteristicValue(FighterCharacteristicType.WP);
        final int oldWpMax = this.getCharacteristicMax(FighterCharacteristicType.WP);
        final int ap = this.getCharacteristicValue(FighterCharacteristicType.AP);
        final int oldApMax = this.getCharacteristicMax(FighterCharacteristicType.AP);
        final int mp = this.getCharacteristicValue(FighterCharacteristicType.MP);
        final int oldMpMax = this.getCharacteristicMax(FighterCharacteristicType.MP);
        this.initialiseCharacteristicsToBaseValue();
        final int newWpMax = this.getCharacteristicMax(FighterCharacteristicType.WP);
        final int maxWpDiff = newWpMax - oldWpMax;
        final int newApMax = this.getCharacteristicMax(FighterCharacteristicType.AP);
        final int maxApDiff = newApMax - oldApMax;
        final int newMpMax = this.getCharacteristicMax(FighterCharacteristicType.MP);
        final int maxMpDiff = newMpMax - oldMpMax;
        this.getCharacteristic((CharacteristicType)FighterCharacteristicType.WP).set(wp + maxWpDiff);
        this.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).set(ap + maxApDiff);
        this.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).set(mp + maxMpDiff);
    }
    
    public void setSummonedFromSymbiot(final boolean summonedFromSymbiot) {
        this.m_summonedFromSymbiot = summonedFromSymbiot;
    }
    
    public void setAlreadyReturnedToSymbiot(final boolean alreadyReturnedToSymbiot) {
        this.m_alreadyReturnedToSymbiot = alreadyReturnedToSymbiot;
    }
    
    protected void putBackInSymbiot(final BasicCharacterInfo summoner) {
        final AbstractSymbiot symb = summoner.getSymbiot();
        if (this.m_alreadyReturnedToSymbiot) {
            return;
        }
        symb.setCreatureAvailable(this.getId());
    }
    
    public void onHeal(final int value) {
        if (this.m_type != 0 && this.m_type != 5) {
            return;
        }
        final BasicFight currentFight = this.getCurrentFight();
        if (!this.isOnFight() || (currentFight != null && currentFight.getStatus() != AbstractFight.FightStatus.ACTION)) {
            return;
        }
        final int max = this.getCharacteristicMax(FighterCharacteristicType.HP);
        this.m_finalHealResist += value / max / 3.0f * 100.0f;
    }
    
    public float getFinalHealResist() {
        return this.m_finalHealResist;
    }
    
    public int getReducedHealValue(final int value) {
        return value - ValueRounder.randomRound(value * this.m_finalHealResist / 100.0f);
    }
    
    protected void reloadFightProperties(final Breed breed) {
        if (breed.getBaseFightProperties().length <= 0) {
            return;
        }
        final PropertyManager<FightPropertyType> properties = this.getFightProperties();
        if (properties == null) {
            BasicCharacterInfo.m_logger.error((Object)"manager de propri\u00e9t\u00e9 de combat null au chargement des donn\u00e9es de combat");
            return;
        }
        for (final int propertyId : breed.getBaseFightProperties()) {
            final FightPropertyType prop = FightPropertyType.getPropertyFromId(propertyId);
            if (prop != null) {
                properties.add(prop);
            }
            else {
                BasicCharacterInfo.m_logger.error((Object)("id d'une propri\u00e9t\u00e9 de base incorrect :" + propertyId));
            }
        }
    }
    
    public long getClientId() {
        throw new UnsupportedOperationException();
    }
    
    public void addToIgnoreResistancesTargets(final EffectUser caster) {
    }
    
    public void removeFromIgnoreResistancesTargets(final EffectUser caster) {
    }
    
    public void spellApCostModification(final int value, final int propertyId) {
        final SpellPropertyType propertyType = SpellPropertyType.getPropertyFromId(propertyId);
        if (propertyType == null) {
            BasicCharacterInfo.m_logger.error((Object)("Propri\u00e9t\u00e9 inconnue " + propertyId));
            return;
        }
        this.m_spellCostModification.apCostModification(value, propertyType);
    }
    
    @NotNull
    public SpellCostModification getSpellCostModification() {
        return this.m_spellCostModification;
    }
    
    public boolean addSpellToTemporaryInventory(final int spellid, final short spellLevel) {
        return false;
    }
    
    public void createTemporaryInventory() {
    }
    
    public void removeSpellFromTemporaryInventory(final int spellId) {
    }
    
    public void changePlayerSpellsByMonsterOnes(final int breedId, final MonsterSpellsLevel level) {
        this.initTemporarySpellInventory(breedId, level);
    }
    
    protected void initTemporarySpellInventory(final int breedId, final MonsterSpellsLevel level) {
    }
    
    public void resetPlayerSpellsByMonsterOnes(final int breedId) {
        this.resetTemporarySpellInventory();
    }
    
    public void resetTemporarySpellInventory() {
    }
    
    public boolean hasTemporarySpellInventory() {
        return false;
    }
    
    public void beginUpdateItemEffects() {
        if (this.m_beginDelegateCount == 0) {
            assert this.m_updateEffectDelegate == null;
            this.m_updateEffectDelegate = new UpdateItemsDelegate();
        }
        ++this.m_beginDelegateCount;
        if (this.m_beginDelegateCount > 100) {
            BasicCharacterInfo.m_logger.warn((Object)"Il doit y avoir un pobl\u00e8me....", (Throwable)new Exception());
        }
    }
    
    public void executeAfterAllUpdate(final String uniqueKey, final Runnable runnable) {
        if (this.m_updateEffectDelegate == null) {
            runnable.run();
        }
        else {
            this.m_updateEffectDelegate.runAtEnd(uniqueKey, runnable);
        }
    }
    
    public void endUpdateItemEffects() {
        --this.m_beginDelegateCount;
        if (this.m_beginDelegateCount == 0) {
            this.m_updateEffectDelegate.execute();
            this.m_updateEffectDelegate = null;
        }
    }
    
    protected void onFightInfoUpdated() {
    }
    
    public void playEmote(final int emoteId, final boolean displayChatFeedback) {
        this.playEmote(emoteId, null, displayChatFeedback);
    }
    
    public void playEmote(final int emoteId, @Nullable final HashMap<String, Object> vars, final boolean displayChatFeedback) {
    }
    
    @Override
    public long getAggroUserId() {
        return this.m_id;
    }
    
    @Nullable
    @Override
    public TLongShortHashMap getAggressiveList() {
        return this.m_aggressiveList;
    }
    
    public BasicCharacterInfo getKiller() {
        return null;
    }
    
    @Override
    public void addAggro(final AggroUser aggroUser, final short amount) {
        if (this.m_aggressiveList == null) {
            this.m_aggressiveList = new TLongShortHashMap();
        }
        short aggroAmount = this.m_aggressiveList.get(aggroUser.getAggroUserId());
        aggroAmount += amount;
        this.m_aggressiveList.put(aggroUser.getAggroUserId(), aggroAmount);
    }
    
    @Override
    public void substractAggro(final AggroUser aggroUser, final short amount) {
        if (this.m_aggressiveList != null) {
            short aggroAmount = this.m_aggressiveList.get(aggroUser.getAggroUserId());
            aggroAmount -= amount;
            this.m_aggressiveList.put(aggroUser.getAggroUserId(), aggroAmount);
        }
    }
    
    @Override
    public void setAggro(final AggroUser aggroUser, final short amount) {
        if (this.m_aggressiveList == null) {
            this.m_aggressiveList = new TLongShortHashMap();
        }
        this.m_aggressiveList.put(aggroUser.getAggroUserId(), amount);
    }
    
    @Override
    public void removeAggroUser(final AggroUser aggroUser) {
        if (this.m_aggressiveList != null) {
            this.m_aggressiveList.remove(aggroUser.getAggroUserId());
        }
    }
    
    @Override
    public void clearAggressiveList() {
        this.m_aggressiveList = null;
    }
    
    @Override
    public String aggroToString() {
        if (this.getCurrentFight() != null) {
            String s = this.getName();
            if (s.length() == 0) {
                s = "id(" + this.m_id + ')' + " breed(" + ((this.getBreed() != null) ? this.getBreed().getBreedId() : "unknown") + ')';
            }
            String list = "AggroList for " + s + ":\n";
            if (this.m_aggressiveList != null) {
                final TLongShortIterator longShortIterator = this.m_aggressiveList.iterator();
                while (longShortIterator.hasNext()) {
                    longShortIterator.advance();
                    final long fighterId = longShortIterator.key();
                    final BasicFight fight = this.getCurrentFight();
                    final BasicCharacterInfo fighter = fight.getFighterFromId(fighterId);
                    String name = null;
                    if (fighter != null) {
                        name = fighter.getName();
                        if (name != null && name.isEmpty()) {
                            name = "id(" + fighter.m_id + ')' + " breed(" + ((fighter.getBreed() != null) ? fighter.getBreed().getBreedId() : "unknown") + ')';
                        }
                    }
                    list = list + name + " : " + longShortIterator.value() + "\n";
                }
            }
            return list;
        }
        return this.getName() + " not in Fight";
    }
    
    public void onEffectUnApplication(final RunningEffect effect) {
    }
    
    public int getMaxWalkDistance() {
        return (this.getCurrentFight() != null) ? this.m_breed.getMaxFightWalkDistance() : this.m_breed.getMaxWalkDistance();
    }
    
    public int getWalkTimeBetweenCells() {
        return this.m_breed.getWalkTimeBetweenCells().getTimeBetweenCells();
    }
    
    public int getRunTimeBetweenCells() {
        return this.m_breed.getRunTimeBetweenCells().getTimeBetweenCells();
    }
    
    public BasicOccupation getCurrentOccupation() {
        return null;
    }
    
    public void setCurrentOccupation(final BasicOccupation currentOccupation) {
    }
    
    public boolean cancelCurrentOccupation(final boolean fromServer, final boolean sendMessage) {
        return true;
    }
    
    public boolean finishCurrentOccupation() {
        return true;
    }
    
    public void raiseOutOfCombat() {
    }
    
    public abstract void initialize();
    
    public abstract int getCurrentTerritoryNationId();
    
    public abstract int getCurrentTerritoryId();
    
    public abstract void onControllerEvent(final int p0, final Object p1);
    
    protected static <C extends BasicCharacterInfo> C getControllerFromFightOrSelf(final BasicFight<C> fight, final C self) {
        if (fight == null) {
            return self;
        }
        final C controllerFromFight = fight.getController(self);
        return (controllerFromFight == null) ? self : controllerFromFight;
    }
    
    protected static <C extends BasicCharacterInfo> C getOriginalControllerFromFightOrSelf(final BasicFight<C> fight, final C self) {
        if (fight == null) {
            return self;
        }
        final C controllerFromFight = fight.getOriginalController(self);
        return (controllerFromFight == null) ? self : controllerFromFight;
    }
    
    public abstract BasicCharacterInfo getOriginalController();
    
    @Override
    public long getOriginalControllerId() {
        final BasicCharacterInfo originalController = this.getOriginalController();
        if (originalController == null) {
            return 0L;
        }
        return originalController.m_id;
    }
    
    public AbstractSymbiot getSymbiot() {
        return null;
    }
    
    public void setCustomHpRegen(final int hpRegen) {
    }
    
    public void setWasCarriedOnLastCell(final boolean wasCarriedOnLastCell) {
        this.m_wasCarriedOnLastCell = wasCarriedOnLastCell;
    }
    
    public void escapeFight() {
    }
    
    public void excludeFromFight() {
        final BasicFight currentFight = this.getCurrentFight();
        if (currentFight == null) {
            return;
        }
        currentFight.putOffPlayInProtagonists(this);
        currentFight.onFighterRemovedFromInPlay(this);
        currentFight.shelveFighterFromTimeline(this);
        currentFight.putOutOfPlayInProtagonists(this);
        currentFight.returnToOriginalController(this);
        this.uncarryAndRemoveCastedEffects();
        currentFight.removeFromTimeline(this);
        currentFight.removeFromObstacles(this);
        currentFight.setHasBeenExcluded(this);
    }
    
    public boolean hasAGroup(final GroupType groupType) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public WakfuEffectContextForUniqueUser getOwnContext() {
        return this.m_ownContext;
    }
    
    public int getChrageAtLastTurnStart() {
        return 0;
    }
    
    @Override
    public void onPositionChanged() {
    }
    
    @Override
    public boolean is(final CriterionUserType type) {
        return CriterionUserType.CHARACTER.is(type);
    }
    
    public FighterCharacteristicManager getDoubleCharacteristics() {
        return null;
    }
    
    @Override
    public double getPlantationBonus(final ResourceType resourceType) {
        int bonus = 0;
        if (this.hasCharacteristic(FighterCharacteristicType.OCCUPATION_GREEN_THUMBS)) {
            bonus += this.getCharacteristicValue(FighterCharacteristicType.OCCUPATION_GREEN_THUMBS);
        }
        bonus += this.getSkillCharacteristics().getEcosystemCharacteristicEfficiency(EcosystemActionType.PLANTATION, resourceType);
        return bonus / 100.0;
    }
    
    @Override
    public String toString() {
        return "BasicCharacterInfo{m_id=" + this.m_id + ", m_ownerId=" + this.m_ownerId + ", m_name='" + this.m_name + '\'' + '}';
    }
    
    static {
        PUBLIC_TYPES = new FighterCharacteristicType[] { FighterCharacteristicType.HP, FighterCharacteristicType.AP, FighterCharacteristicType.MP, FighterCharacteristicType.WP, FighterCharacteristicType.INIT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.MP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, FighterCharacteristicType.MP_DEBUFF_RES, FighterCharacteristicType.RES_IN_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.TACKLE, FighterCharacteristicType.DODGE };
        PRIVATE_TYPES = new FighterCharacteristicType[] { FighterCharacteristicType.WISDOM, FighterCharacteristicType.PROSPECTION, FighterCharacteristicType.RANGE, FighterCharacteristicType.FEROCITY, FighterCharacteristicType.FUMBLE_RATE, FighterCharacteristicType.HEAL_IN_PERCENT, FighterCharacteristicType.MECHANISM_MASTERY, FighterCharacteristicType.MECHANICS, FighterCharacteristicType.LEADERSHIP, FighterCharacteristicType.SUMMONING_MASTERY, FighterCharacteristicType.BACKSTAB_BONUS, FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_REBOUND, FighterCharacteristicType.DMG_ABSORB, FighterCharacteristicType.EQUIPMENT_KNOWLEDGE, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.MP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, FighterCharacteristicType.MP_DEBUFF_RES, FighterCharacteristicType.STRENGTH, FighterCharacteristicType.AGILITY, FighterCharacteristicType.INTELLIGENCE, FighterCharacteristicType.LUCK };
        FOR_FIGHT_TYPES = new FighterCharacteristicType[] { FighterCharacteristicType.HP, FighterCharacteristicType.AP, FighterCharacteristicType.MP, FighterCharacteristicType.WP, FighterCharacteristicType.INIT, FighterCharacteristicType.RES_IN_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.MP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, FighterCharacteristicType.MP_DEBUFF_RES, FighterCharacteristicType.TACKLE, FighterCharacteristicType.DODGE, FighterCharacteristicType.BACKSTAB_BONUS, FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_REBOUND, FighterCharacteristicType.DMG_ABSORB, FighterCharacteristicType.FEROCITY, FighterCharacteristicType.FUMBLE_RATE, FighterCharacteristicType.HEAL_IN_PERCENT, FighterCharacteristicType.MECHANISM_MASTERY, FighterCharacteristicType.SUMMONING_MASTERY, FighterCharacteristicType.FECA_GLYPH_CHARGE_BONUS, FighterCharacteristicType.ARMOR_PLATE_BONUS, FighterCharacteristicType.ARMOR_PLATE, FighterCharacteristicType.BOMB_COOLDOWN, FighterCharacteristicType.STATE_APPLICATION_BONUS, FighterCharacteristicType.STATE_RESISTANCE_BONUS, FighterCharacteristicType.PERCEPTION, FighterCharacteristicType.VIRTUAL_HP, FighterCharacteristicType.STEAMER_MICROBOT_MAX_DISTANCE };
        m_logger = Logger.getLogger((Class)BasicCharacterInfo.class);
    }
    
    public enum MonsterSpellsLevel
    {
        MONSTER_LEVEL, 
        PLAYER_LEVEL;
    }
    
    private static class UpdateItemsDelegate
    {
        private final LightWeightMap<String, Runnable> m_runnables;
        
        private UpdateItemsDelegate() {
            super();
            this.m_runnables = new LightWeightMap<String, Runnable>();
        }
        
        public void runAtEnd(final String uniqueKey, final Runnable runnable) {
            if (!this.m_runnables.contains(uniqueKey)) {
                this.m_runnables.put(uniqueKey, runnable);
            }
        }
        
        void execute() {
            for (int i = 0, size = this.m_runnables.size(); i < size; ++i) {
                this.m_runnables.getQuickValue(i).run();
            }
        }
    }
    
    private final class CharacterInfoPartId extends CharacterInfoPart
    {
        private final CharacterSerializedId m_part;
        
        private CharacterInfoPartId(final CharacterSerializedId part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.id = BasicCharacterInfo.this.getId();
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.setId(this.m_part.id);
            for (final CharacterInfoPropertyEventsHandler handler : BasicCharacterInfo.this.m_characterInfoEventsHandler) {
                handler.onIdentityChanged(BasicCharacterInfo.this);
            }
        }
    }
    
    private final class CharacterInfoPartIdentity extends CharacterInfoPart
    {
        private final CharacterSerializedIdentity m_part;
        
        private CharacterInfoPartIdentity(final CharacterSerializedIdentity part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.ownerId = BasicCharacterInfo.this.getOwnerId();
            this.m_part.type = BasicCharacterInfo.this.getType();
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.setOwnerId(this.m_part.ownerId);
            BasicCharacterInfo.this.setType(this.m_part.type);
            for (final CharacterInfoPropertyEventsHandler handler : BasicCharacterInfo.this.m_characterInfoEventsHandler) {
                handler.onIdentityChanged(BasicCharacterInfo.this);
            }
        }
    }
    
    private final class CharacterInfoPartName extends CharacterInfoPart
    {
        private final CharacterSerializedName m_part;
        
        public CharacterInfoPartName(final CharacterSerializedName serializedPart) {
            super();
            this.m_part = serializedPart;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.name = BasicCharacterInfo.this.getName();
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.setName(this.m_part.name);
        }
    }
    
    private final class CharacterInfoPartBreed extends CharacterInfoPart
    {
        private final CharacterSerializedBreed m_part;
        
        public CharacterInfoPartBreed(final CharacterSerializedBreed part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            if (BasicCharacterInfo.this.m_breed == AvatarBreed.NONE) {
                BasicCharacterInfo.m_logger.warn((Object)"Serialisation d'une breed NONE ! probablement pas normal");
            }
            this.m_part.breedId = BasicCharacterInfo.this.m_breed.getBreedId();
        }
        
        @Override
        public void onDataChanged() {
            final short breedId = this.m_part.breedId;
            if (BasicCharacterInfo.this.m_breed == null || breedId != BasicCharacterInfo.this.m_breed.getBreedId()) {
                final Breed breed = BasicCharacterInfo.this.getBreedManager().getBreedFromId(breedId);
                assert breed != null : "breed == null. Invalid BreedId : " + breedId + " ?";
                BasicCharacterInfo.this.setBreed(breed);
                if (BasicCharacterInfo.this.m_name == null || BasicCharacterInfo.this.m_name.length() == 0) {
                    BasicCharacterInfo.this.setName("");
                }
                for (final CharacterInfoPropertyEventsHandler handler : BasicCharacterInfo.this.m_characterInfoEventsHandler) {
                    handler.onBreedChanged(BasicCharacterInfo.this);
                }
            }
        }
    }
    
    private final class CharacterInfoPartSex extends CharacterInfoPart
    {
        private final CharacterSerializedSex m_part;
        
        public CharacterInfoPartSex(final CharacterSerializedSex part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.sex = BasicCharacterInfo.this.m_sex;
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.m_sex = this.m_part.sex;
        }
    }
    
    private final class CharacterInfoPartPosition extends CharacterInfoPart
    {
        private final CharacterSerializedPosition m_part;
        
        private CharacterInfoPartPosition(final CharacterSerializedPosition part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            final Point3 position = BasicCharacterInfo.this.getPosition();
            this.m_part.x = position.getX();
            this.m_part.y = position.getY();
            this.m_part.z = position.getZ();
            this.m_part.direction = (byte)BasicCharacterInfo.this.getDirection().m_index;
            this.m_part.instanceId = BasicCharacterInfo.this.getInstanceId();
            final Point3 dbPos = BasicCharacterInfo.this.getDimensionalBagPosition();
            if (dbPos == null) {
                return;
            }
            this.m_part.dimBagPosition = new CharacterSerializedPosition.DimBagPosition();
            this.m_part.dimBagPosition.x = dbPos.getX();
            this.m_part.dimBagPosition.y = dbPos.getY();
            this.m_part.dimBagPosition.z = dbPos.getZ();
            this.m_part.dimBagPosition.instanceId = BasicCharacterInfo.this.getDimensionalBagInstanceId();
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.setPosition(this.m_part.x, this.m_part.y, this.m_part.z);
            BasicCharacterInfo.this.setDirection(Direction8.getDirectionFromIndex(this.m_part.direction));
            BasicCharacterInfo.this.setInstanceId(this.m_part.instanceId);
            if (this.m_part.dimBagPosition != null) {
                BasicCharacterInfo.this.setDimensionalBagPosition(new Point3(this.m_part.dimBagPosition.x, this.m_part.dimBagPosition.y, this.m_part.dimBagPosition.z));
                BasicCharacterInfo.this.setDimensionalBagInstanceId(this.m_part.dimBagPosition.instanceId);
            }
            for (final CharacterInfoPropertyEventsHandler handler : BasicCharacterInfo.this.m_characterInfoEventsHandler) {
                handler.onPositionChanged(BasicCharacterInfo.this);
                handler.onDirectionChanged(BasicCharacterInfo.this);
            }
        }
    }
    
    private final class CharacterInfoPartFight extends CharacterInfoPart
    {
        private final CharacterSerializedFight m_part;
        
        private CharacterInfoPartFight(final CharacterSerializedFight part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            final BasicFight fight = BasicCharacterInfo.this.getCurrentFight();
            this.m_part.currentFightId = ((fight != null) ? fight.getId() : -1);
            this.m_part.isKo = BasicCharacterInfo.this.m_isKO;
            this.m_part.isDead = BasicCharacterInfo.this.m_isDead;
            this.m_part.isSummoned = BasicCharacterInfo.this.m_isSummoned;
            this.m_part.isFleeing = BasicCharacterInfo.this.m_isFleeing;
            this.m_part.obstacleId = BasicCharacterInfo.this.m_obstacleId;
            if (BasicCharacterInfo.this.m_summonCharacteristics != null) {
                this.m_part.SUMMONDATA = new CharacterSerializedFight.SUMMONDATA();
                BasicCharacterInfo.this.m_summonCharacteristics.toRaw(this.m_part.SUMMONDATA.summon);
            }
        }
        
        @Override
        public void onDataChanged() {
            final int fightId = this.m_part.currentFightId;
            BasicCharacterInfo.this.setFight(fightId);
            if (fightId > 0) {
                BasicCharacterInfo.this.setOnFight(true);
            }
            BasicCharacterInfo.this.setDead(this.m_part.isDead);
            BasicCharacterInfo.this.m_isKO = this.m_part.isKo;
            BasicCharacterInfo.this.m_isSummoned = this.m_part.isSummoned;
            BasicCharacterInfo.this.m_isFleeing = this.m_part.isFleeing;
            BasicCharacterInfo.this.m_obstacleId = this.m_part.obstacleId;
            if (this.m_part.SUMMONDATA != null) {
                if (this.m_part.SUMMONDATA.summon.DOUBLEINVOC != null) {
                    DoubleInvocationCharacteristics invocationCharacteristics;
                    if (this.m_part.SUMMONDATA.summon.DOUBLEINVOC.doubledata.doubleType == 1) {
                        invocationCharacteristics = DoubleInvocationCharacteristics.getDefaultInstance();
                    }
                    else if (this.m_part.SUMMONDATA.summon.DOUBLEINVOC.doubledata.doubleType == 2) {
                        invocationCharacteristics = BellaphoneDoubleCharacteristics.getDefaultInstance();
                    }
                    else if (this.m_part.SUMMONDATA.summon.DOUBLEINVOC.doubledata.doubleType == 3) {
                        invocationCharacteristics = IceStatueDoubleCharacteristics.getDefaultInstance();
                    }
                    else {
                        BasicCharacterInfo.m_logger.error((Object)("Type d'invoc de double inconnue " + this.m_part.SUMMONDATA.summon.DOUBLEINVOC.doubledata.doubleType));
                        invocationCharacteristics = DoubleInvocationCharacteristics.getDefaultInstance();
                    }
                    if (BasicCharacterInfo.this.getSpellInventory() != null) {
                        BasicCharacterInfo.this.m_summonCharacteristics = invocationCharacteristics.newInstance((short)22, (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)BasicCharacterInfo.this.getSpellInventory().getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)BasicCharacterInfo.this.getSpellInventory().getContentChecker(), false, false, false);
                    }
                    else {
                        BasicCharacterInfo.this.m_summonCharacteristics = invocationCharacteristics.newInstance();
                    }
                }
                else if (this.m_part.SUMMONDATA.summon.IMAGEINVOC != null) {
                    BasicCharacterInfo.this.m_summonCharacteristics = ImageCharacteristics.getDefaultInstance().newInstance();
                }
                else {
                    BasicCharacterInfo.this.m_summonCharacteristics = new BasicInvocationCharacteristics();
                }
                BasicCharacterInfo.this.m_summonCharacteristics.fromRaw(this.m_part.SUMMONDATA.summon);
            }
            BasicCharacterInfo.this.onFightInfoUpdated();
        }
    }
    
    private final class CharacterInfoPartFightCharacteristics extends CharacterInfoPart
    {
        private final CharacterSerializedCharacteristics m_part;
        
        private CharacterInfoPartFightCharacteristics(final CharacterSerializedCharacteristics part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            BasicCharacterInfo.this.getCharacteristics().toRaw(this.m_part.characteristics, (CharacteristicType[])BasicCharacterInfo.FOR_FIGHT_TYPES);
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.getCharacteristics().fromRaw(this.m_part.characteristics);
        }
    }
    
    private final class CharacterInfoPartAllCharacteristics extends CharacterInfoPart
    {
        private final CharacterSerializedCharacteristics m_part;
        
        private CharacterInfoPartAllCharacteristics(final CharacterSerializedCharacteristics part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            BasicCharacterInfo.this.getCharacteristics().toRaw(this.m_part.characteristics, (CharacteristicType[])FighterCharacteristicType.values());
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.getCharacteristics().fromRaw(this.m_part.characteristics);
        }
    }
    
    private final class CharacterInfoPartWorldProperties extends CharacterInfoPart
    {
        private final CharacterSerializedProperties m_part;
        
        private CharacterInfoPartWorldProperties(final CharacterSerializedProperties part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            if (BasicCharacterInfo.this.m_worldProperties != null) {
                this.m_part.properties = new CharacterSerializedProperties.Properties();
                BasicCharacterInfo.this.m_worldProperties.toRaw(this.m_part.properties.properties);
            }
            else {
                this.m_part.properties = null;
            }
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.properties != null) {
                if (BasicCharacterInfo.this.m_worldProperties == null) {
                    BasicCharacterInfo.this.m_worldProperties = (PropertyManager<WorldPropertyType>)PropertyManager.newInstance((byte)0, BasicCharacterInfo.this);
                }
                BasicCharacterInfo.this.m_worldProperties.fromRaw(this.m_part.properties.properties);
            }
            else if (BasicCharacterInfo.this.m_worldProperties != null) {
                BasicCharacterInfo.this.m_worldProperties = null;
            }
        }
    }
    
    private final class CharacterInfoPartFightProperties extends CharacterInfoPart
    {
        private final CharacterSerializedProperties m_part;
        
        private CharacterInfoPartFightProperties(final CharacterSerializedProperties part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            if (BasicCharacterInfo.this.m_fightProperties != null) {
                this.m_part.properties = new CharacterSerializedProperties.Properties();
                BasicCharacterInfo.this.m_fightProperties.toRaw(this.m_part.properties.properties);
            }
            else {
                this.m_part.properties = null;
            }
        }
        
        @Override
        public void onDataChanged() {
            if (this.m_part.properties != null) {
                if (BasicCharacterInfo.this.m_fightProperties == null) {
                    BasicCharacterInfo.this.m_fightProperties = (PropertyManager<FightPropertyType>)PropertyManager.newInstance((byte)1, BasicCharacterInfo.this);
                }
                BasicCharacterInfo.this.m_fightProperties.fromRaw(this.m_part.properties.properties);
            }
            else if (BasicCharacterInfo.this.m_fightProperties != null) {
                BasicCharacterInfo.this.m_fightProperties = null;
            }
        }
    }
    
    private final class CharacterInfoPartControlledByAI extends CharacterInfoPart
    {
        private final CharacterSerializedControlledByAI m_part;
        
        private CharacterInfoPartControlledByAI(final CharacterSerializedControlledByAI part) {
            super();
            this.m_part = part;
            this.m_part.getBinarPart().setDataSource(this);
        }
        
        @Override
        public void updateToSerializedPart() {
            this.m_part.controlledByAI = BasicCharacterInfo.this.m_isControlledByAI;
        }
        
        @Override
        public void onDataChanged() {
            BasicCharacterInfo.this.m_isControlledByAI = this.m_part.controlledByAI;
        }
    }
}
