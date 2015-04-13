package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.time.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public abstract class AbstractFight<F extends BasicCharacterInfo> extends WakfuTurnBasedFight<F> implements TimeEventHandler
{
    private static final NullEffectExecutionListener NULL_FIGHT_EFFECT_EXECUTION_LISTENER;
    public static final boolean WAKFU_FIGHT_DEBUG = false;
    protected EffectExecutionListener m_effectExecutionListener;
    public static final byte FIGHTER_KO = 0;
    public static final byte FIGHTER_RAISE = 1;
    public static final byte FIGHTER_DEFEAT = 2;
    public static final byte FIGHTER_FLEE = 3;
    protected FightStatus m_status;
    private final TLongObjectHashMap<EffectUser> m_additionnalTargets;
    private final SpellCastValidator m_spellCastValidator;
    private final ItemCastValidator m_itemCastValidator;
    private final ItemAndSpellCastValidator m_itemAndSpellCastValidator;
    protected byte m_readyCount;
    protected AbstractBattlegroundBorderEffectArea m_battlegroundBorderEffectArea;
    
    private boolean areOpponent(final BasicCharacterInfo f1, final BasicCharacterInfo f2) {
        return this.getTeamId(f1) != this.getTeamId(f2);
    }
    
    public boolean isEnding() {
        return this.m_status == FightStatus.DESTRUCTION;
    }
    
    public EffectExecutionListener getEffectExecutionListener() {
        return this.m_effectExecutionListener;
    }
    
    public void changeSpellCastTargetCell(final Point3 targetCell) {
    }
    
    public Point3 getSpellTargetedCell() {
        return null;
    }
    
    public SpellCaster getSpellCaster() {
        return null;
    }
    
    public int dropEnutrofBlessing(final Looter looter) {
        return 0;
    }
    
    public int dropEnutrofPurse(final Dropper dropper, final Looter looter, final boolean addToEndDrop, final boolean improvedDrop) {
        return 0;
    }
    
    public void onBlockAchieved(final EffectUser target) {
    }
    
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
        if (!(area instanceof CarryTarget)) {
            return;
        }
        final CarryTarget carryTarget = (CarryTarget)area;
        final boolean carried = carryTarget.isCarried();
        if (!carried) {
            return;
        }
        if (carryTarget.getCarrier() != null) {
            carryTarget.getCarrier().forceUncarry();
        }
    }
    
    public MonsterSpawner getMonsterSpawner() {
        return null;
    }
    
    public void addBonusLoot(final Looter looter, final int refId, final short qty) {
    }
    
    @Override
    public boolean hasAdditionalTarget(final long id) {
        return this.m_additionnalTargets.contains(id);
    }
    
    public void removeAdditionalTarget(final long id) {
        this.m_additionnalTargets.remove(id);
    }
    
    @Override
    public EffectUser getAdditionalTargetWithId(final long id) {
        return this.m_additionnalTargets.get(id);
    }
    
    @Override
    public Iterator<EffectUser> getAdditionalTargets() {
        final EffectUser[] res = new EffectUser[this.m_additionnalTargets.size()];
        this.m_additionnalTargets.getValues(res);
        return new ArrayIterator<EffectUser>(res, false);
    }
    
    public void putAdditionnalTarget(final EffectUser target) {
        this.m_additionnalTargets.put(target.getId(), target);
    }
    
    public int getAdditionnalTargetCount() {
        return this.m_additionnalTargets.size();
    }
    
    public boolean isNeedPlacementStep() {
        return this.m_model.needPlacementStep();
    }
    
    public boolean isMoveFightersOnEnterFight() {
        return this.m_model.moveFightersOnEnterFight();
    }
    
    protected AbstractFight(final int id, final FightModel model, final FightMap fightMap) {
        super(id, model, fightMap);
        this.m_effectExecutionListener = AbstractFight.NULL_FIGHT_EFFECT_EXECUTION_LISTENER;
        this.m_additionnalTargets = new TLongObjectHashMap<EffectUser>();
        this.m_spellCastValidator = new SpellCastValidator(this);
        this.m_itemCastValidator = new ItemCastValidator(this);
        this.m_itemAndSpellCastValidator = new ItemAndSpellCastValidator(this);
        this.m_status = FightStatus.CREATION;
    }
    
    public void setFighterDirection(final F fighter, final Direction8 dir) {
        fighter.setDirection(dir);
    }
    
    @Override
    public AbstractTimeline getTimeline() {
        return this.m_timeline;
    }
    
    @Override
    public FightStatus getStatus() {
        return this.m_status;
    }
    
    public void setStatus(final FightStatus status) {
        this.m_status = status;
    }
    
    public void start() {
        this.getTimeline().start();
        this.onFightStarted();
    }
    
    private void createTimeBuff() {
        final TIntObjectHashMap<List<WakfuEffect>> teamEffects = new TIntObjectHashMap<List<WakfuEffect>>();
        final TIntHashSet playerTeams = this.computePlayerTeamEffects(teamEffects);
        this.computeNonPlayerTeamEffects(teamEffects, playerTeams);
        this.fillTimeScoreGaugeTeamEffects(teamEffects);
    }
    
    private void fillTimeScoreGaugeTeamEffects(final TIntObjectHashMap<List<WakfuEffect>> teamEffects) {
        teamEffects.forEachEntry(new TIntObjectProcedure<List<WakfuEffect>>() {
            @Override
            public boolean execute(final int teamId, final List<WakfuEffect> effects) {
                AbstractFight.this.getTimeline().getTimeScoreGauges().setTeamEffects(teamId, effects);
                return true;
            }
        });
    }
    
    private void computeNonPlayerTeamEffects(final TIntObjectHashMap<List<WakfuEffect>> teamEffects, final TIntHashSet playerTeams) {
        final Collection<F> fighters = this.getNonPlayers();
        for (final F fighter : fighters) {
            final byte teamId = fighter.getTeamId();
            if (playerTeams.contains(teamId)) {
                continue;
            }
            final Breed breed = fighter.getBreed();
            if (!(breed instanceof AbstractMonsterBreed)) {
                continue;
            }
            final WakfuEffect effect = TimelineBuffListManager.INSTANCE.getEffectByBaseId(((AbstractMonsterBreed)breed).getTimelineBuffId());
            if (effect == null) {
                continue;
            }
            List<WakfuEffect> monsterTeamEffects = teamEffects.get(teamId);
            if (monsterTeamEffects == null) {
                monsterTeamEffects = new ArrayList<WakfuEffect>();
                teamEffects.put(teamId, monsterTeamEffects);
            }
            monsterTeamEffects.add(effect);
        }
    }
    
    private TIntHashSet computePlayerTeamEffects(final TIntObjectHashMap<List<WakfuEffect>> teamEffects) {
        final TIntHashSet playerTeams = new TIntHashSet();
        final Collection<F> players = this.getPlayers();
        for (final F player : players) {
            final byte teamId = player.getTeamId();
            final List<WakfuEffect> playerEffects = teamEffects.get(teamId);
            if (playerEffects != null) {
                continue;
            }
            teamEffects.put(teamId, new ArrayList<WakfuEffect>(TimelineBuffListManager.INSTANCE.getPlayerEffects()));
            playerTeams.add(teamId);
        }
        return playerTeams;
    }
    
    public boolean startPlacement(final int remainingTime) {
        this.m_status = FightStatus.PLACEMENT;
        this.onPlacementStart(remainingTime);
        return true;
    }
    
    public boolean startAction() {
        this.m_status = FightStatus.ACTION;
        this.createTimeBuff();
        this.onActionStart();
        this.getTimeline().updateDynamicOrder();
        this.getTimeline().startAction();
        return true;
    }
    
    @Override
    public boolean fighterAbandonFight(final long controllerId) {
        final F fighter = this.getFighterFromId(controllerId);
        if (fighter != null) {
            fighter.returnToOriginalController();
        }
        return super.fighterAbandonFight(controllerId);
    }
    
    @Override
    public boolean putFighterOffPlay(final F f) {
        final boolean rv = super.putFighterOffPlay(f);
        if (rv) {
            this.shelveFighterFromTimeline(f);
        }
        return rv;
    }
    
    @Override
    public void putFighterBackInPlay(final F f) {
        if (!f.isActiveProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)) {
            this.getTimeline().unShelveFighter(f.getId());
        }
        super.putFighterBackInPlay(f);
    }
    
    @Override
    public boolean putFighterOutOfPlay(final F f) {
        final boolean rv = super.putFighterOutOfPlay(f);
        if (rv) {
            this.removeFromTimeline(f);
        }
        return rv;
    }
    
    public CastValidity getItemAndSpellCastValidity(final BasicCharacterInfo fighter, final Item item, final AbstractSpellLevel spelllevel, final Point3 targetCell) {
        return this.m_itemAndSpellCastValidator.getItemAndSpellCastValidity(fighter, item, spelllevel, targetCell);
    }
    
    public CastValidity getItemCastValidity(final BasicCharacterInfo fighter, final Item item, final Point3 targetCell, final boolean withUseCost) {
        return this.m_itemCastValidator.getItemCastValidity(fighter, item, targetCell, withUseCost);
    }
    
    public CastValidity getSpellCastValidity(final BasicCharacterInfo fighter, final AbstractSpellLevel spelllevel, final Point3 targetCell, final boolean checkUseCost) {
        return this.m_spellCastValidator.getSpellCastValidity(fighter, spelllevel, targetCell, checkUseCost);
    }
    
    public CastValidity getSpellCastValidityWithoutGates(final BasicCharacterInfo fighter, final AbstractSpellLevel spelllevel, final Point3 targetCell, final boolean checkUseCost) {
        this.m_spellCastValidator.disableGates();
        CastValidity res;
        try {
            res = this.m_spellCastValidator.getSpellCastValidity(fighter, spelllevel, targetCell, checkUseCost);
        }
        catch (Exception e) {
            AbstractFight.m_logger.error((Object)"Exception levee", (Throwable)e);
            return CastValidity.CANNOT_EVALUATE;
        }
        finally {
            this.m_spellCastValidator.enableGates();
        }
        return res;
    }
    
    public abstract AbstractEffectManager<WakfuEffect> getEffectManager();
    
    @Override
    public void onFightCreatedAndInitialized() {
        super.onFightCreatedAndInitialized();
        this.m_context = new WakfuFightEffectContext(this, this.getEffectManager());
    }
    
    @Override
    public void onFighterTeleported(final F f) {
        final Collection<BasicEffectArea> areasToRemove = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : this.m_effectAreaManager.getActiveEffectAreas()) {
            if (area.getType() == EffectAreaType.GLYPH.getTypeId() && area.getOwner() == f && !((AbstractGlyphEffectArea)area).checkCasterPosition()) {
                areasToRemove.add(area);
            }
        }
        for (final BasicEffectArea area : areasToRemove) {
            this.m_effectAreaManager.removeEffectArea(area);
        }
    }
    
    public void onFightStarted() {
        this.getTimeline().sortInitially();
        this.m_fightMap.blockFightingGroundInTopology(true, true);
    }
    
    protected abstract void onPlacementStart(final int p0);
    
    public abstract void onPlacementEnd();
    
    public void onActionStart() {
        this.getTimeline().sortInitially();
        this.m_effectAreaManager.addEffectArea(this.m_battlegroundBorderEffectArea);
    }
    
    @Override
    public void endFight() {
        this.m_status = FightStatus.DESTRUCTION;
        super.endFight();
    }
    
    @Override
    public void onTableTurnBegin() {
        super.onTableTurnBegin();
        this.updateItems();
    }
    
    @Override
    public void onFighterJoinFight(final F f) {
        f.loadFightData();
        switch (this.m_status) {
            case PLACEMENT:
            case CREATION: {
                if (!f.isActiveProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)) {
                    this.getTimeline().addFighter(f.getId(), false);
                }
                if (this.m_status == FightStatus.PLACEMENT) {
                    this.getTimeline().sortInitially();
                    break;
                }
                break;
            }
            case ACTION: {
                if (f.isActiveProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)) {
                    break;
                }
                this.getTimeline().addFighter(f.getId(), true);
                break;
            }
        }
        super.onFighterJoinFight(f);
    }
    
    @Override
    public void onFighterRemovedFromInPlay(final F f) {
        final TimedRunningEffectManager rem = f.getRunningEffectManager();
        for (final F fighter : this.getInPlayFighters()) {
            if (fighter.getRunningEffectManager() != null) {
                fighter.getRunningEffectManager().removeLinkedToCaster(f, true, true);
            }
            if (rem != null) {
                rem.removeLinkedToCaster(fighter, true, true);
            }
        }
        if (f.isCarrying()) {
            f.forceUncarry();
        }
        if (this.getEffectAreaManager() != null) {
            this.getEffectAreaManager().removeEffectAreaOwnedByEffectUserIfNecessary(f);
        }
    }
    
    public void onFighterStartTurn(@NotNull final F fighter) {
        if (fighter.isActiveProperty(FightPropertyType.GROGGY_3)) {
            fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).toMin();
        }
        if (fighter.isActiveProperty(FightPropertyType.CRIPPLED_3)) {
            fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).toMin();
        }
        fighter.getSpellLevelCastHistory().onNewTurn();
        super.onFighterStartTurn(fighter);
    }
    
    public void onFighterEndTurn(@NotNull final F fighter) {
        if (fighter.isActiveProperty(FightPropertyType.GROGGY_3)) {
            fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).toMin();
        }
        else {
            fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).toMax();
        }
        fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).toMax();
        if (fighter.isActiveProperty(FightPropertyType.CRIPPLED_3)) {
            fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).toMin();
        }
        super.onFighterEndTurn(fighter);
    }
    
    @Override
    public void onFightEnded() {
        this.m_fightMap.blockFightingGroundInTopology(false, true);
    }
    
    @Override
    public void handlePlacementEndEvent(final PlacementEndEvent event) {
        this.onPlacementEnd();
    }
    
    @Override
    public void handleTableTurnStartEvent(final TableTurnStartEvent event) {
        this.onTableTurnBegin();
    }
    
    @Override
    public void handleTableTurnEndEvent(final TableTurnEndEvent event) {
        this.onTableTurnEnd();
    }
    
    @Override
    public void handleFighterTurnStartEvent(final FighterTurnStartEvent event) {
        this.onFighterStartTurn(event.getFighterId());
    }
    
    @Override
    public void handleFighterTurnEndEvent(final FighterTurnEndEvent event) {
        this.onFighterEndTurn(event.getFighterId());
    }
    
    @Override
    public void handleBeforeFighterTurnEndEvent(final long fighterId) {
    }
    
    @Override
    public void handleRunningEffectActivationEvent(final RunningEffectActivationEvent event) {
        final RunningEffect re = event.getRunningEffect();
        event.setRunningEffectToNull();
        if (re != null) {
            re.askForExecution();
        }
    }
    
    @Override
    public void handleRunningEffectDeactivationEvent(final RunningEffectDeactivationEvent event) {
        final RunningEffect runningEffect = event.getRunningEffect();
        if (runningEffect != null) {
            runningEffect.askForUnapplication();
        }
    }
    
    @Override
    public void handleEffectAreaActivationEvent(final EffectAreaActivationEvent event) {
        event.getArea().setActive(this.getFighterFromId(event.getApplicantId()));
    }
    
    public abstract Iterator<? extends DroppedItem> getItems();
    
    public abstract void removeItem(final long p0);
    
    public void updateItems() {
        final Iterator<? extends DroppedItem> it = this.getItems();
        final Collection<Long> toRemove = new ArrayList<Long>();
        while (it.hasNext()) {
            final DroppedItem item = (DroppedItem)it.next();
            if (item.getPhase() == -1) {
                if (item.getRemainingTicksInPhase() <= 0L) {
                    toRemove.add(item.getId());
                }
                else {
                    item.setRemainingTicksInPhase(item.getRemainingTicksInPhase() - 1L);
                }
            }
            else if (item.getRemainingTicksInPhase() <= 0L) {
                item.nextPhase();
                item.setRemainingTicksInPhase((item.getPhase() == -1) ? 3L : ((long)ItemDropParameters.ITEM_PHASES_TURN[item.getPhase()]));
            }
            else {
                item.setRemainingTicksInPhase(item.getRemainingTicksInPhase() - 1L);
            }
        }
        for (final Long itemId : toRemove) {
            this.removeItem(itemId);
        }
    }
    
    public short getMonsterCount(final short breedId, final byte teamId) {
        Collection<F> inPlayFighters;
        if (teamId != -1) {
            inPlayFighters = this.m_protagonists.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.or(ProtagonistFilter.ofType((byte)1), ProtagonistFilter.ofType((byte)2)), ProtagonistFilter.inTeam(teamId));
        }
        else {
            inPlayFighters = this.m_protagonists.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.or(ProtagonistFilter.ofType((byte)1), ProtagonistFilter.ofType((byte)2)));
        }
        short count = 0;
        if (breedId == -1) {
            return (short)inPlayFighters.size();
        }
        for (final F inPlayFighter : inPlayFighters) {
            if (inPlayFighter.getBreedId() == breedId) {
                ++count;
            }
        }
        return count;
    }
    
    public AbstractBattlegroundBorderEffectArea getBattlegroundBorderEffectArea() {
        return this.m_battlegroundBorderEffectArea;
    }
    
    public void setBattlegroundBorderEffectArea(final AbstractBattlegroundBorderEffectArea battlegroundBorderEffectArea) {
        this.m_battlegroundBorderEffectArea = battlegroundBorderEffectArea;
    }
    
    public abstract void revealInvisibleCharacterForAll(final F p0);
    
    public void tryToPickUpLoot(final int x, final int y, final BasicCharacterInfo looter, final boolean forceSuccess) {
    }
    
    public void updateAggroListForAll(final WakfuEffectContainer effectContainer, final BasicCharacterInfo caster) {
        if (effectContainer != null) {
            this.updateAggroListForAll(effectContainer.getAggroWeight(), caster);
        }
    }
    
    public void updateAggroListForAll(final short aggroWeight, final BasicCharacterInfo caster) {
        if (caster == null) {
            return;
        }
        final byte teamId = caster.getTeamId();
        for (final F f1 : this.m_protagonists.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.not(ProtagonistFilter.inTeam(teamId)))) {
            f1.addAggro(caster, aggroWeight);
        }
    }
    
    public void updateAggroFromEffect(final WakfuRunningEffect effect) {
        if (((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect().getEffectType() != 2) {
            return;
        }
        if (((RunningEffect<FX, WakfuEffectContainer>)effect).getEffectContainer() != null) {
            final short aggroWeight = ((RunningEffect<FX, WakfuEffectContainer>)effect).getEffectContainer().getAggroWeight();
            final short allyEfficacity = ((RunningEffect<FX, WakfuEffectContainer>)effect).getEffectContainer().getAllyEfficacity();
            final short foeEfficacity = ((RunningEffect<FX, WakfuEffectContainer>)effect).getEffectContainer().getFoeEfficacity();
            EffectUser caster = effect.getCaster();
            if (((RunningEffect<FX, WakfuEffectContainer>)effect).getEffectContainer().getContainerType() == 3) {
                caster = ((RunningEffect<FX, BasicEffectArea>)effect).getEffectContainer().getOwner();
            }
            final EffectUser target = effect.getTarget();
            if (caster != null && target != null && caster instanceof BasicCharacterInfo && target instanceof BasicCharacterInfo) {
                final BasicCharacterInfo cfighter = (BasicCharacterInfo)caster;
                boolean treason = false;
                if (this.areOpponent(cfighter, (BasicCharacterInfo)target)) {
                    if (foeEfficacity == 0) {
                        treason = true;
                    }
                }
                else if (allyEfficacity == 0) {
                    treason = true;
                }
                final AggroUser tuser = (AggroUser)target;
                final AggroUser cuser = (AggroUser)caster;
                if (!treason) {
                    if (tuser != cuser) {
                        tuser.addAggro(cuser, aggroWeight);
                    }
                }
                else {
                    for (final F f : this.getInPlayFighters()) {
                        if (this.areOpponent(cfighter, f)) {
                            f.substractAggro(cuser, (short)(aggroWeight * 10));
                        }
                    }
                }
            }
        }
    }
    
    protected abstract void tryPutAdditionalTarget(final FightObstacle p0);
    
    @Override
    protected void destroyEffectAreas() {
        if (this.m_battlegroundBorderEffectArea != null) {
            this.m_effectAreaManager.removeEffectArea(this.m_battlegroundBorderEffectArea);
            this.m_effectAreaManager.removeFromAreaList(this.m_battlegroundBorderEffectArea);
            this.m_battlegroundBorderEffectArea.release();
            this.m_battlegroundBorderEffectArea = null;
        }
        super.destroyEffectAreas();
    }
    
    static {
        NULL_FIGHT_EFFECT_EXECUTION_LISTENER = new NullEffectExecutionListener();
    }
    
    public enum FightStatus
    {
        NONE((byte)0), 
        PLACEMENT((byte)1), 
        ACTION((byte)2), 
        CREATION((byte)3), 
        DESTRUCTION((byte)4);
        
        private final byte m_id;
        
        private FightStatus(final byte id) {
            this.m_id = id;
        }
        
        public byte getId() {
            return this.m_id;
        }
        
        public static FightStatus getStatusFromId(final byte id) {
            for (final FightStatus fightStatus : values()) {
                if (fightStatus.getId() == id) {
                    return fightStatus;
                }
            }
            return FightStatus.NONE;
        }
    }
}
