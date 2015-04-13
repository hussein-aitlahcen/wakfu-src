package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.dataProvider.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.handler.*;
import com.ankamagames.wakfu.common.game.fight.time.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.wakfu.common.game.fight.microbotCombination.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.game.fight.bombCombination.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import java.util.*;

public abstract class BasicFight<F extends BasicCharacterInfo> extends BinarSerial implements EffectUserInformationProvider, BasicFightInfo<F>, TargetInformationProvider<EffectUser>, EffectAreaActionListener
{
    private static final String LOGGER_NAME = "mainLog.fightLog";
    protected static final Logger m_logger;
    public static final int EVENT_JOIN_FIGHT = 300;
    public static final int EVENT_LEAVE_FIGHT = 301;
    public static final int EVENT_WON_FIGHT = 305;
    public static final int EVENT_LOSE_FIGHT = 306;
    public static final int EVENT_FLEE_FIGHT = 307;
    public static final int EVENT_ABANDON_FIGHT = 308;
    private ArrayList<FightControllerListener> m_controllerListeners;
    private Set<F> m_fighterBeingRemovedFromProtagonist;
    private boolean m_createdAndInitialized;
    protected final FightProtagonists<F> m_protagonists;
    private final TLongArrayList m_excluded;
    protected AbstractTimeline m_timeline;
    protected final FightMap m_fightMap;
    protected EffectContext m_context;
    protected BasicEffectAreaManager m_effectAreaManager;
    @Nullable
    private MicrobotManager m_microbotManager;
    private final int m_id;
    @NotNull
    protected final FightModel m_model;
    protected boolean m_isEnding;
    protected final BombCombinationComputer m_bombSpecialEffectComputer;
    private byte m_winningTeamId;
    private final BinarSerialPart TIMELINE;
    
    @Override
    public abstract long getNextFreeEffectUserId(final byte p0);
    
    protected BasicFight(final int id, final FightModel model, final FightMap fightMap) {
        super();
        this.m_controllerListeners = new ArrayList<FightControllerListener>();
        this.m_fighterBeingRemovedFromProtagonist = new HashSet<F>();
        this.m_protagonists = new FightProtagonists<F>();
        this.m_excluded = new TLongArrayList();
        this.m_microbotManager = null;
        this.m_isEnding = false;
        this.m_bombSpecialEffectComputer = new BombCombinationComputerImpl();
        this.TIMELINE = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final byte[] serializedTimeline = BasicFight.this.getTimeline().serializeTimeline();
                buffer.putShort((short)serializedTimeline.length);
                buffer.put(serializedTimeline);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final byte[] serializedTimeline = new byte[buffer.getShort()];
                buffer.get(serializedTimeline);
                BasicFight.this.readTimelineFromBuild(serializedTimeline);
            }
            
            @Override
            public int expectedSize() {
                return 2 + BasicFight.this.getTimeline().serializedSize();
            }
        };
        if (model == null) {
            throw new IllegalArgumentException("On construit un fight avec un mod\u00e8le null");
        }
        this.m_model = model;
        this.m_id = id;
        this.m_fightMap = fightMap;
        this.m_effectAreaManager = EffectAreaManager.checkOut(this, this.getContext());
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.getTimelineBinarSerialPart() };
    }
    
    public BinarSerialPart getTimelineBinarSerialPart() {
        return this.TIMELINE;
    }
    
    public final void readTimelineFromBuild(final byte[] serializedTimeline) {
        this.getTimeline().fromBuild(this.getContext(), serializedTimeline);
    }
    
    public StringBuilder withFightId(final String message) {
        return new StringBuilder().append("[_FL_] fightId=").append(this.getId()).append(' ').append(message);
    }
    
    public StringBuilder withFightId(final String message, final Throwable t) {
        return this.withFightId(message).append(' ').append(ExceptionFormatter.toString(t));
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    public boolean addControllerListener(final FightControllerListener o) {
        return !this.m_controllerListeners.contains(o) && this.m_controllerListeners.add(o);
    }
    
    @Override
    public byte getInitiatingTeamId() {
        throw new UnsupportedOperationException("Impossible de r\u00e9cup\u00e9rer cette info");
    }
    
    @Override
    public FightMap getFightMap() {
        return this.m_fightMap;
    }
    
    public byte getTypeId() {
        return this.m_model.getTypeId();
    }
    
    public TurnBasedTimeline getTimeline() {
        return this.m_timeline;
    }
    
    @Override
    public BasicEffectAreaManager getEffectAreaManager() {
        return this.m_effectAreaManager;
    }
    
    protected abstract void initializeMicrobotManager(final MicrobotManager p0);
    
    protected final MicrobotManager getMicrobotManager() {
        if (this.m_microbotManager == null) {
            (this.m_microbotManager = new MicrobotManagerImpl()).setFightMap(this.getFightMap());
            this.initializeMicrobotManager(this.m_microbotManager);
        }
        return this.m_microbotManager;
    }
    
    @Nullable
    public MicrobotSet getMicrobotSetForFighter(final BasicCharacterInfo fighter) {
        if (this.m_microbotManager == null) {
            return null;
        }
        if (fighter == null) {
            return null;
        }
        return this.m_microbotManager.getMicrobotSet(fighter.getId());
    }
    
    public List<MicrobotSet> getMicrobotSetForTeam(final byte teamId) {
        if (this.m_microbotManager == null) {
            return Collections.emptyList();
        }
        final ArrayList<MicrobotSet> list = new ArrayList<MicrobotSet>();
        for (final F fighter : this.getFightersInTeam(teamId)) {
            final MicrobotSet set = this.m_microbotManager.getMicrobotSet(fighter.getId());
            if (set != null && !set.isEmpty()) {
                list.add(set);
            }
        }
        return list;
    }
    
    public byte getMinTeam() {
        return this.getModel().getMinTeam();
    }
    
    public byte getMaxTeam() {
        return this.getModel().getMaxTeam();
    }
    
    public byte getMaxFighterByTeam() {
        return this.getModel().getMaxFighterByTeam();
    }
    
    public Collection<F> getFightersNotInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.not(ProtagonistFilter.inTeam(teamId)));
    }
    
    public Collection<F> getFightersInPlayNotInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.and(ProtagonistFilter.not(ProtagonistFilter.inTeam(teamId)), ProtagonistFilter.inPlay()));
    }
    
    public Collection<F> getFightersInPlayInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.and(ProtagonistFilter.inTeam(teamId), ProtagonistFilter.inPlay()));
    }
    
    public Collection<F> getFightersInPlayOrOffPlayNotInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.and(ProtagonistFilter.not(ProtagonistFilter.inTeam(teamId)), ProtagonistFilter.or(ProtagonistFilter.offPlay(), ProtagonistFilter.inPlay())));
    }
    
    public Collection<F> getPlayersNotInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.not(ProtagonistFilter.inTeam(teamId)), ProtagonistFilter.ofType((byte)0));
    }
    
    public Collection<F> getPlayersInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.inTeam(teamId), ProtagonistFilter.ofType((byte)0));
    }
    
    @Override
    public Collection<F> getFightersInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.inTeam(teamId));
    }
    
    public Collection<F> getInvisibleFightersNotInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.and(ProtagonistFilter.inPlay(), ProtagonistFilter.invisible(), ProtagonistFilter.not(ProtagonistFilter.inTeam(teamId))));
    }
    
    public Collection<F> getPotentialTacklers(final BasicCharacterInfo mover) {
        return this.m_protagonists.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.not(ProtagonistFilter.controllerInTeam(mover.getControllerTeamId())), ProtagonistFilter.not(ProtagonistFilter.isCarried()), ProtagonistFilter.not(ProtagonistFilter.hasProperty(FightPropertyType.CANT_TACKLE)));
    }
    
    @Override
    public F getFighterFromId(final long fighterId) {
        return this.m_protagonists.getFighterById(fighterId);
    }
    
    public Collection<F> getFightersInPlay() {
        return this.m_protagonists.getFighters(ProtagonistFilter.inPlay());
    }
    
    @Override
    public Collection<F> getFighters() {
        return this.m_protagonists.getFighters(ProtagonistFilter.or(ProtagonistFilter.inPlay(), ProtagonistFilter.offPlay()));
    }
    
    public Collection<F> getFightersControlledBy(final BasicCharacterInfo controller) {
        return this.m_protagonists.getFighters(ProtagonistFilter.originallyControlledBy(controller));
    }
    
    public boolean isFighterCurrentlyInFight(final F f) {
        return f.isOnFight() && (this.m_protagonists.isInPlay(f) || this.m_protagonists.isOffPlay(f));
    }
    
    @Override
    public EffectContext getContext() {
        return this.m_context;
    }
    
    @Override
    public JoinFightResult canJoinTeam(final F fighter, final byte teamId) {
        if (fighter.getCurrentFight() != null && fighter.getCurrentFight().getFighterFromId(fighter.getId()) != null) {
            BasicFight.m_logger.error((Object)"Le personnage est deja ajout\u00e9 au combat !");
            return JoinFightResult.ALREADY_IN_FIGHT;
        }
        if (this.getFightersInTeam(teamId).contains(fighter)) {
            return JoinFightResult.ALREADY_IN_FIGHT;
        }
        if (fighter.isDead()) {
            return JoinFightResult.PLAYER_IS_DEAD;
        }
        if (teamId < 0 || teamId >= this.m_model.getMaxTeam()) {
            return JoinFightResult.TEAM_DOESNT_EXIST;
        }
        if (!JoinTeamHelper.canJoinMonsterTeam(this, fighter, teamId)) {
            return JoinFightResult.CANT_JOIN_MONSTER_TEAM;
        }
        if (!JoinTeamHelper.canJoinOpposingPartyTeam(this, fighter, teamId)) {
            return JoinFightResult.CANT_JOIN_OPPOSING_PARTY_TEAM;
        }
        final JoinFightResult protectorFightError = JoinTeamHelper.canJoinProtectorFight(this, fighter, teamId);
        if (protectorFightError != JoinFightResult.OK) {
            return protectorFightError;
        }
        final JoinFightResult pvpResult = NationPvpHelper.testPlayerCanJoinPvpFightOf(this, fighter, teamId);
        if (pvpResult != JoinFightResult.OK) {
            return pvpResult;
        }
        if (teamId == 0 && this.m_model.isNoFighterLimitForRedTeam()) {
            return JoinFightResult.OK;
        }
        final int count = this.m_protagonists.getFighters(ProtagonistFilter.inTeam(teamId)).size() + 1;
        if (count > this.getMaxFighterByTeam()) {
            return JoinFightResult.TEAM_IS_FULL;
        }
        return JoinFightResult.OK;
    }
    
    public boolean addFightersTeam(final byte teamId, final Collection<F> fighters) {
        return this.addFightersTeam(teamId, fighters, true);
    }
    
    public boolean addFightersTeam(final byte teamId, final Collection<F> fighters, final boolean checkValidity) {
        if (checkValidity && !this.teamRulesAllowToAddFighters(teamId, fighters)) {
            return false;
        }
        for (final F fighter : fighters) {
            this.addFighter(fighter, teamId, false);
            this.onControllerJoinFight(fighter);
            if (this.getStatus() == AbstractFight.FightStatus.DESTRUCTION) {
                return false;
            }
        }
        return true;
    }
    
    private boolean teamRulesAllowToAddFighters(final byte teamId, final Collection<F> fighters) {
        if (teamId == 0 && this.m_model.isNoFighterLimitForRedTeam()) {
            return true;
        }
        final int teamSize = fighters.size();
        return teamId >= 0 && teamId < this.getMaxTeam() && teamSize <= this.getMaxFighterByTeam();
    }
    
    public JoinFightResult addFighterFromControllerToTeam(final F fighter, final byte teamId, final boolean check) {
        JoinFightResult result = JoinFightResult.OK;
        if (!check || (result = this.canJoinTeam(fighter, teamId)) == JoinFightResult.OK) {
            this.addFighter(fighter, teamId, false);
            this.onControllerJoinFight(fighter);
        }
        return result;
    }
    
    public void addFighter(final F fighter, final byte teamId, final boolean liveOnlyThisFight) {
        this.addFighter(fighter, teamId, liveOnlyThisFight, fighter);
    }
    
    public void addFighter(final F fighter, final byte teamId, final boolean liveOnlyThisFight, final F controller) {
        this.m_protagonists.addFighter(fighter, teamId, liveOnlyThisFight, controller);
        this.onFighterJoinFight(fighter);
    }
    
    public boolean fighterAbandonFight(final long controllerId) {
        final F controller = this.m_protagonists.getSingleFighter(ProtagonistFilter.withId(controllerId));
        if (this.m_effectAreaManager != null) {
            this.m_effectAreaManager.removeEffectAreaOwnedByEffectUser(controller);
        }
        if (!this.removeController(controller)) {
            return false;
        }
        if (!this.checkFightEnd()) {
            if (controller.isFleeing()) {
                this.onControllerFleeFight(controller);
            }
            else {
                this.onControllerAbandonFight(controller);
            }
            return true;
        }
        return false;
    }
    
    private boolean removeController(final F controller) {
        if (controller == null) {
            return false;
        }
        for (final F f : this.m_protagonists.getFightersControlledBy(controller)) {
            if (f != null) {
                f.returnToOriginalController();
            }
            else {
                BasicFight.m_logger.error((Object)"On un fighter non pr\u00e9sent en combat sous le contr\u00f4le d'un fighter en combat ");
            }
        }
        for (final F fighter : this.m_protagonists.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.originallyControlledBy(controller))) {
            fighter.setUnderChange(true);
            this.putFighterOffPlay(fighter);
            fighter.setUnderChange(false);
        }
        for (final F fighter : this.m_protagonists.getFighters(ProtagonistFilter.offPlay(), ProtagonistFilter.originallyControlledBy(controller))) {
            fighter.setUnderChange(true);
            this.putFighterOutOfPlay(fighter);
            fighter.setUnderChange(false);
        }
        return true;
    }
    
    @NotNull
    @Override
    public FightModel getModel() {
        return this.m_model;
    }
    
    public Collection<F> getPlayers() {
        return this.m_protagonists.getFighters(ProtagonistFilter.ofType((byte)0));
    }
    
    public Collection<F> getNonPlayers() {
        return this.m_protagonists.getFighters(ProtagonistFilter.ofType((byte)1));
    }
    
    public Collection<F> getCompanions() {
        return this.m_protagonists.getFighters(ProtagonistFilter.ofType((byte)5));
    }
    
    public Collection<F> getControlledCompanions(final BasicCharacterInfo controller) {
        return this.m_protagonists.getFighters(ProtagonistFilter.ofType((byte)5), ProtagonistFilter.controlledBy(controller));
    }
    
    public Collection<F> getInPlayFighters() {
        return this.m_protagonists.getFighters(ProtagonistFilter.inPlay());
    }
    
    public Collection<F> getInPlayOrSimulatingFighters() {
        return this.m_protagonists.getFighters(ProtagonistFilter.or(ProtagonistFilter.inPlay(), ProtagonistFilter.hasProperty(FightPropertyType.SIMULATING)));
    }
    
    public int getInPlayFightersCount() {
        return this.getInPlayFighters().size();
    }
    
    public boolean putFighterOffPlay(final F fighter) {
        if (fighter == null) {
            throw new IllegalArgumentException("appel de putFighterOffPlay avec fighter = null");
        }
        boolean rv = true;
        if (!this.getModel().isAllowNoFightersInPlayAtEnd() && this.getInPlayFightersCount() <= 1) {
            rv = false;
        }
        else if (this.putOffPlayInProtagonists(fighter)) {
            this.onFighterRemovedFromInPlay(fighter);
        }
        else {
            rv = false;
            BasicFight.m_logger.error((Object)this.withFightId("\u00c9chec de la transition IN PLAY -> OFF PLAY pour " + fighter.getId()));
        }
        fighter.onGoesOffPlay();
        return rv;
    }
    
    public boolean putOffPlayInProtagonists(final F fighter) {
        return this.m_protagonists.putOffPlay(fighter);
    }
    
    @Override
    public Collection<F> getFightersPresentInTimelineInPlayInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.inTeam(teamId), ProtagonistFilter.not(ProtagonistFilter.hasProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)));
    }
    
    public Collection<F> getFighters(final ProtagonistFilter... specs) {
        return this.m_protagonists.getFighters(specs);
    }
    
    public Collection<F> getFightersPresentInTimelineInPlayOrSimulatingInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.or(ProtagonistFilter.inPlay(), ProtagonistFilter.hasProperty(FightPropertyType.SIMULATING)), ProtagonistFilter.inTeam(teamId), ProtagonistFilter.not(ProtagonistFilter.hasProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)));
    }
    
    public Collection<F> getFightersWithControllerInTeam(final byte teamId) {
        return this.m_protagonists.getFighters(ProtagonistFilter.controllerInTeam(teamId));
    }
    
    public Collection<F> getFighterInPlayInTeamCountingForFightEnd(final byte teamId) {
        final Collection<F> inPlayFighters = this.m_protagonists.getFighters(ProtagonistFilter.or(ProtagonistFilter.inPlay(), ProtagonistFilter.hasProperty(FightPropertyType.SIMULATING)), ProtagonistFilter.inTeam(teamId));
        final Iterator<F> iterator = inPlayFighters.iterator();
        while (iterator.hasNext()) {
            final F f = iterator.next();
            if (f.hasProperty(FightPropertyType.DONT_COUNT_AS_FIGHTER_ON_FIGHT_END)) {
                iterator.remove();
            }
        }
        return inPlayFighters;
    }
    
    public void putFighterBackInPlay(final F fighter) {
        if (fighter == null) {
            throw new IllegalArgumentException("appel de putFighterBackInPlay avec fighter = null");
        }
        if (this.m_protagonists.putInPlay(fighter)) {
            fighter.returnToOriginalController();
        }
        else {
            BasicFight.m_logger.error((Object)this.withFightId("\u00c9chec de la transition OFF PLAY -> IN PLAY pour " + fighter.getId()));
        }
        fighter.onBackInPlay();
    }
    
    public boolean putFighterOutOfPlay(final F fighter) {
        if (fighter == null) {
            throw new IllegalArgumentException("appel de putFighterOutOfPlay avec fighter = null");
        }
        boolean rv = true;
        try {
            if (!this.putOutOfPlayInProtagonists(fighter)) {
                BasicFight.m_logger.error((Object)this.withFightId("\u00c9chec de la transition OFF PLAY -> OUT OF PLAY pour " + fighter.getId()));
                rv = false;
            }
            else {
                fighter.returnToOriginalController();
            }
            fighter.onGoesOutOfPlay();
            final byte teamId = fighter.getTeamId();
            if (fighter.getOriginalController().getTeamId() == teamId && this.getFightersPresentInTimelineInPlayInTeam(teamId).isEmpty()) {
                this.onTeamLose(teamId);
            }
        }
        catch (IllegalArgumentException e) {
            BasicFight.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        this.onFighterOutOfPlay(fighter);
        return rv;
    }
    
    public boolean putOutOfPlayInProtagonists(final F fighter) {
        return this.m_protagonists.putOutOfPlay(fighter);
    }
    
    private boolean fightShouldContinue() {
        return this.getModel().getFightEndChecker().fightShouldContinue(this);
    }
    
    public boolean checkFightEnd() {
        try {
            if (!this.m_createdAndInitialized) {
                return false;
            }
            if (this.m_isEnding) {
                return false;
            }
            if (this.fightShouldContinue()) {
                return false;
            }
            final byte winningTeam = this.getWinningTeam();
            if (winningTeam != -1) {
                this.onTeamWin(winningTeam);
            }
            this.endFight();
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("CHECK DE FIN DE COMBAT : On termine de force"));
            BasicFight.m_logger.error((Object)this.withFightId("CHECK DE FIN DE COMBAT : on loggue, mais on ne fait rien, sinon on p\u00e8te un combat " + ExceptionFormatter.toString(e, 4)));
        }
        return true;
    }
    
    private byte getWinningTeam() {
        final TByteHashSet remainingTeams = this.getRemainingTeams();
        if (remainingTeams.size() == 1) {
            return remainingTeams.toArray()[0];
        }
        BasicFight.m_logger.error((Object)this.withFightId("On n'a pas une seule equipe vainqueur \u00e0 la fin du combat, on ne d\u00e9clare pas de vainqueur"));
        return -1;
    }
    
    private TByteHashSet getRemainingTeams() {
        final TByteHashSet remainingTeams = new TByteHashSet();
        for (final F f : this.getInPlayFighters()) {
            if (f.getTeamId() == -1) {
                BasicFight.m_logger.error((Object)this.withFightId("[FIGHT_REFACTOR] On a un fighter inPlay avec un teamId \u00e0 -1 " + f + " - " + ExceptionFormatter.currentStackTrace(8)));
            }
            else {
                if (f.hasProperty(FightPropertyType.DONT_COUNT_AS_FIGHTER_ON_FIGHT_END)) {
                    continue;
                }
                remainingTeams.add(f.getTeamId());
            }
        }
        return remainingTeams;
    }
    
    public boolean isCreatedAndInitialized() {
        return this.m_createdAndInitialized;
    }
    
    public void endFight() {
        this.m_isEnding = true;
        try {
            this.getTimeline().stop();
            this.getTimeline().clearTimeEvents();
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Arret de la timeline ", e));
        }
        try {
            for (final F f : this.getInPlayFighters()) {
                this.onFighterRemovedFromInPlay(f);
                this.onFighterOutOfPlay(f);
            }
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Retrait des personnages inplay", e));
        }
        try {
            for (final F f : this.m_protagonists.getFighters(ProtagonistFilter.offPlay())) {
                this.onFighterOutOfPlay(f);
            }
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Retrait des personnages offplay", e));
        }
        try {
            for (final BasicEffectArea area : this.m_effectAreaManager.getEffectAreaList()) {
                this.onEffectAreaRemoved(area);
            }
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Retrait des zones d'effets", e));
        }
        try {
            for (final F f : this.m_protagonists.getFighters(ProtagonistFilter.localToFight())) {
                if (f.getOriginalController() == f) {
                    f.onLeaveFight();
                }
            }
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Notification de fin de combat", e));
        }
        try {
            for (final F f : this.m_protagonists.getFighters(ProtagonistFilter.localToFight())) {
                if (f.getOriginalController() == f) {
                    this.removeControlledFighter(f);
                }
            }
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Retrait des fighters li\u00e9s aux combats de leur controlleur", e));
        }
        for (final F fighter : this.getAllFighters()) {
            try {
                if (this.getOriginalController(fighter) != fighter) {
                    continue;
                }
                this.onControllerRemovedFromFight(fighter);
            }
            catch (Exception e2) {
                BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Retrait des controlleurs", e2));
            }
        }
        try {
            this.onFightEnded();
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Dispatch de l'\u00e9venement de fin de combat", e));
        }
        try {
            this.destroyEffectAreas();
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)this.withFightId("FIN DE COMBAT : Destruction des aires d'effet", e));
        }
        this.destroyFight();
    }
    
    protected void destroyEffectAreas() {
        this.m_effectAreaManager.destroyAll();
    }
    
    public void destroyFight() {
        if (this.m_effectAreaManager != null) {
            this.m_effectAreaManager.release();
            this.m_effectAreaManager = null;
        }
        if (this.m_context != null) {
            this.m_context = null;
        }
    }
    
    public abstract FightManagerBase getFightManager();
    
    protected void onFighterOutOfPlay(final F f) {
        this.removeFromObstacles(f);
    }
    
    public void removeFromObstacles(final F f) {
        this.m_fightMap.removeObstacle(f);
    }
    
    private Iterator<EffectUser> getEffectUsers() {
        final Collection<Iterator<? extends EffectUser>> iterators = this.getEffectUsersIterators();
        return new MergedIterator<EffectUser>(iterators);
    }
    
    private Collection<Iterator<? extends EffectUser>> getEffectUsersIterators() {
        final Collection<Iterator<? extends EffectUser>> iterators = new ArrayList<Iterator<? extends EffectUser>>();
        iterators.add(this.getFighters().iterator());
        if (this.m_effectAreaManager != null) {
            final ArrayList<BasicEffectArea> basicEffectAreas = new ArrayList<BasicEffectArea>(this.m_effectAreaManager.getTargetableEffectArea());
            if (!basicEffectAreas.isEmpty()) {
                iterators.add(basicEffectAreas.iterator());
            }
        }
        return iterators;
    }
    
    @Override
    public EffectUser getEffectUserFromId(final long effectUserId) {
        EffectUser user = this.m_effectAreaManager.getEffectAreaWithId(effectUserId);
        if (user != null) {
            return user;
        }
        user = this.m_protagonists.getFighterById(effectUserId);
        if (user != null) {
            return user;
        }
        return this.getAdditionalTargetWithId(effectUserId);
    }
    
    public int getAllFightersCount() {
        return this.m_protagonists.getFighters(new ProtagonistFilter[0]).size();
    }
    
    public Collection<F> getAllFighters() {
        return this.m_protagonists.getFighters(new ProtagonistFilter[0]);
    }
    
    public Collection<F> getFightersForCreation() {
        if (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.RECO_IN_FIGHT_ENABLED)) {
            return this.m_protagonists.getFighters(ProtagonistFilter.not(ProtagonistFilter.and(ProtagonistFilter.isSummon(), ProtagonistFilter.outOffPlay())));
        }
        return this.getInPlayFighters();
    }
    
    public EffectUser getAdditionalTargetWithId(final long id) {
        return null;
    }
    
    @Nullable
    public Iterator<EffectUser> getAdditionalTargets() {
        return new EmptyIterator<EffectUser>();
    }
    
    public boolean hasAdditionalTarget(final long id) {
        return false;
    }
    
    private Iterator<EffectUser> possibleTargetsIterator() {
        final Collection<Iterator<? extends EffectUser>> targetsIterators = this.getEffectUsersIterators();
        final Iterator<EffectUser> additionalTargets = this.getAdditionalTargets();
        if (additionalTargets != null) {
            targetsIterators.add(additionalTargets);
        }
        return new MergedIterator<EffectUser>(targetsIterators);
    }
    
    @Override
    public Iterator<EffectUser> getAllPossibleTargets() {
        return new PossibleTargetsIterator();
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final Point3 pos) {
        return this.getPossibleTargetsAtPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final int x, final int y, final int z) {
        final Iterator<EffectUser> it = this.getAllPossibleTargets();
        final ArrayList<EffectUser> list = new ArrayList<EffectUser>();
        while (it.hasNext()) {
            final EffectUser eu = it.next();
            if (DistanceUtils.getIntersectionDistance(eu, x, y) == 0) {
                list.add(eu);
            }
        }
        return list;
    }
    
    public BasicCharacterInfo getCharacterInfoAtPosition(final Point3 pos) {
        if (pos == null) {
            return null;
        }
        return this.getCharacterInfoAtPosition(pos.getX(), pos.getY());
    }
    
    public BasicCharacterInfo getCharacterInfoAtPosition(final int posx, final int posy) {
        if (this.m_fightMap == null) {
            return null;
        }
        final FightObstacle obstacle = this.m_fightMap.getObstacle(posx, posy);
        if (obstacle == null) {
            return null;
        }
        if (obstacle instanceof BasicCharacterInfo) {
            return (BasicCharacterInfo)obstacle;
        }
        return null;
    }
    
    public byte getWinningTeamId() {
        return this.m_winningTeamId;
    }
    
    @Override
    public byte getTeamId(final long fighterId) {
        final F fighter = this.m_protagonists.getFighterById(fighterId);
        return this.getTeamId(fighter);
    }
    
    public byte getTeamId(final BasicCharacterInfo fighter) {
        return this.m_protagonists.getTeamId(fighter);
    }
    
    public byte getOriginalTeamId(final BasicCharacterInfo fighter) {
        return this.m_protagonists.getOriginalTeamId(fighter);
    }
    
    public void setTeamId(final BasicCharacterInfo characterInfo, final byte teamId) {
        this.m_protagonists.setTeamId(characterInfo, teamId);
    }
    
    public F getLeader(final byte teamId) {
        return this.m_protagonists.getSingleFighter(ProtagonistFilter.inOriginalTeam(teamId), ProtagonistFilter.leaderOfTeam());
    }
    
    public boolean isLeader(final F fighter) {
        return this.m_protagonists.isTeamLeader(fighter);
    }
    
    public F getController(final F controlled) {
        return this.m_protagonists.getController(controlled);
    }
    
    public F getOriginalController(final F controlled) {
        return this.m_protagonists.getOriginalController(controlled);
    }
    
    public void setCurrentController(final long controlledId, final long newControllerId) {
        final F controlled = this.getFighterFromId(controlledId);
        final F newController = this.getFighterFromId(newControllerId);
        if (controlled == null) {
            BasicFight.m_logger.error((Object)String.format("[FIGHT_CONTROLLERS] Fighter introuvable pour un changement de controlleur : %d", controlledId));
        }
        if (newController == null) {
            BasicFight.m_logger.error((Object)String.format("[FIGHT_CONTROLLERS] Fighter introuvable pour un changement de controlleur : %d", newControllerId));
        }
        this.m_protagonists.setCurrentController(controlled, newController);
    }
    
    public void removeControlledFighter(final F fighter) {
        this.m_protagonists.removeControlledFighter(fighter);
    }
    
    public void returnToOriginalController(final F fighter) {
        this.m_protagonists.returnToOriginalController(fighter);
    }
    
    public FightProtagonists<F> getProtagonists() {
        return this.m_protagonists;
    }
    
    public Collection<BasicEffectArea> getActiveEffectAreas() {
        if (this.m_effectAreaManager != null) {
            return this.m_effectAreaManager.getActiveEffectAreas();
        }
        return (Collection<BasicEffectArea>)Collections.emptyList();
    }
    
    public boolean isLocalToFight(final BasicCharacterInfo characterInfo) {
        return this.m_protagonists.isLocalFighter(characterInfo);
    }
    
    public boolean isInPlay(final BasicCharacterInfo basicCharacterInfo) {
        return this.m_protagonists.isInPlay(basicCharacterInfo);
    }
    
    public boolean isOffPlay(final BasicCharacterInfo fighter) {
        return this.m_protagonists.isOffPlay(fighter);
    }
    
    public boolean isOutOfPlay(final BasicCharacterInfo fighter) {
        return this.m_protagonists.isOutOfPlay(fighter);
    }
    
    public FighterPlayState getPlayState(final BasicCharacterInfo fighter) {
        return this.m_protagonists.getPlayState(fighter);
    }
    
    public void removeFromTimeline(final F f) {
        if (!f.isActiveProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)) {
            this.getTimeline().removeFighter(f.getId());
        }
    }
    
    public void shelveFighterFromTimeline(final F f) {
        if (!f.isActiveProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)) {
            this.getTimeline().shelveFighter(f.getId());
        }
    }
    
    public void setHasBeenExcluded(final BasicCharacterInfo basicCharacterInfo) {
        this.m_excluded.add(basicCharacterInfo.getId());
    }
    
    public boolean hasBeenExcluded(final long fighterId) {
        return this.m_excluded.contains(fighterId);
    }
    
    public void onControllerJoinFight(final F controller) {
        controller.onControllerEvent(300, this);
        for (int i = 0; i < this.m_controllerListeners.size(); ++i) {
            this.m_controllerListeners.get(i).onControllerJoinFight(controller);
        }
    }
    
    public void onControllerWinFight(final F controller) {
        controller.onControllerEvent(305, this);
        for (int i = 0; i < this.m_controllerListeners.size(); ++i) {
            this.m_controllerListeners.get(i).onControllerWinFight(controller);
        }
    }
    
    public void onControllerLoseFight(final F controller) {
        controller.onControllerEvent(306, this);
        for (int i = 0; i < this.m_controllerListeners.size(); ++i) {
            this.m_controllerListeners.get(i).onControllerLoseFight(controller);
        }
    }
    
    public void onControllerAbandonFight(final F controller) {
        controller.onControllerEvent(308, this);
    }
    
    public void onControllerFleeFight(final F controller) {
        controller.onControllerEvent(307, this);
    }
    
    public void onControllerRemovedFromFight(final F controller) {
        if (controller == null) {
            return;
        }
        try {
            this.removeFighterOriginallyControlledBy(controller);
            this.m_protagonists.partialRemoveController(controller);
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        try {
            controller.onControllerEvent(301, this);
        }
        catch (Exception e) {
            BasicFight.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private void removeFighterOriginallyControlledBy(final F controller) {
        if (!this.m_fighterBeingRemovedFromProtagonist.contains(controller)) {
            this.m_fighterBeingRemovedFromProtagonist.add(controller);
            for (final F fighter : this.m_protagonists.getFightersOriginallyControlledBy(controller)) {
                if (fighter != controller) {
                    this.removeFighterOriginallyControlledBy(fighter);
                }
            }
        }
        this.removeFighterFromProtagonists(controller);
    }
    
    protected void removeFighterFromProtagonists(final F fighter) {
        if (!this.m_protagonists.containsFighter(fighter)) {
            return;
        }
        fighter.onLeaveFight();
        this.m_protagonists.removeFighter(fighter);
    }
    
    public void onFighterJoinFight(final F f) {
        this.addFighterToMapObstacles(f);
        f.onJoinFight(this);
        BasicFight.m_logger.info((Object)this.withFightId(f.getControllerName() + " breed = " + f.getBreedId() + " [" + f.getId() + ']' + " isControlledByAI=" + f.isControlledByAI() + " Id d'obstacle " + f.getObstacleId() + " a rejoint le combat"));
    }
    
    public void areaActivationWhenJoiningFight(final BasicCharacterInfo f) {
        if (f == null) {
            return;
        }
        if (f.isActiveProperty(FightPropertyType.DOESNT_TRIGGER_EFFECT_ZONE_WHEN_JOINING_FIGHT)) {
            return;
        }
        final ArrayList<BasicEffectArea> areasIn = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : this.m_effectAreaManager.getActiveEffectAreas()) {
            if (area.contains(f, f.getWorldCellX(), f.getWorldCellY(), f.getWorldCellAltitude())) {
                areasIn.add(area);
            }
        }
        if (!areasIn.isEmpty()) {
            for (final BasicEffectArea area : areasIn) {
                area.triggers(10012, null, f);
                if (this.checkFightEnd()) {
                    return;
                }
            }
        }
    }
    
    protected void addFighterToMapObstacles(final BasicCharacterInfo f) {
        this.m_fightMap.addObstacle(f);
    }
    
    public void onFighterRemovedFromInPlay(final F f) {
        if (this.getEffectAreaManager() != null) {
            this.getEffectAreaManager().removeEffectAreaOwnedByEffectUser(f);
        }
    }
    
    public abstract void onFighterMove(final F p0, final List<int[]> p1, final FightMovementType p2);
    
    public abstract void onFighterTeleported(final F p0);
    
    public void onFightCreatedAndInitialized() {
        this.m_createdAndInitialized = true;
    }
    
    public abstract void onFightEnded();
    
    public void onTeamWin(final byte teamId) {
        for (final F controller : this.m_protagonists.controllersInOriginalTeam(teamId)) {
            this.onControllerWinFight(controller);
        }
        this.m_winningTeamId = teamId;
    }
    
    public void onTeamLose(final byte teamId) {
        for (final F controller : this.m_protagonists.controllersInOriginalTeam(teamId)) {
            try {
                if (controller.isFleeing()) {
                    this.onControllerFleeFight(controller);
                }
                else {
                    this.onControllerLoseFight(controller);
                }
            }
            catch (Exception e) {
                BasicFight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public void onEffectAreaAdded(final BasicEffectArea area) {
        this.m_fightMap.addObstacle(area);
        if (area.getType() == EffectAreaType.BOMB.getTypeId()) {
            ((AbstractBombEffectArea)area).setPositionChangedListener(this.m_bombSpecialEffectComputer);
            this.m_bombSpecialEffectComputer.addBombToCombinationAndNotifyListener((AbstractBombEffectArea)area);
        }
        else if (area.getType() == EffectAreaType.FAKE_FIGHTER.getTypeId()) {
            final AbstractFakeFighterEffectArea fakeFighterEffectArea = (AbstractFakeFighterEffectArea)area;
            switch (fakeFighterEffectArea.getUserDefinedId()) {
                case 4: {
                    this.getMicrobotManager().handleMicrobotAdded(fakeFighterEffectArea);
                    break;
                }
            }
        }
    }
    
    @Override
    public void onEffectAreaRemoved(final BasicEffectArea area) {
        this.m_fightMap.removeObstacle(area);
        if (area.getType() == EffectAreaType.BOMB.getTypeId()) {
            this.m_bombSpecialEffectComputer.removeBombFromCombinationAndNotifyListener((AbstractBombEffectArea)area);
            ((AbstractBombEffectArea)area).setPositionChangedListener(null);
        }
        else if (area.getType() == EffectAreaType.FAKE_FIGHTER.getTypeId()) {
            final AbstractFakeFighterEffectArea fakeFighterEffectArea = (AbstractFakeFighterEffectArea)area;
            switch (fakeFighterEffectArea.getUserDefinedId()) {
                case 4: {
                    this.getMicrobotManager().handleMicrobotRemoved(fakeFighterEffectArea);
                    break;
                }
            }
        }
    }
    
    protected void uncarryAreaIfNecessary(final BasicEffectArea area) {
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
    
    @Override
    public void onEffectAreaExecuted(final BasicEffectArea area) {
        if (area.mustGoOffPlay() && area.canChangePlayStatus()) {
            area.setUnderChange(true);
            area.goOffPlay(null);
            area.setUnderChange(false);
        }
    }
    
    @Override
    public void onEffectAreaPreExecution(final BasicEffectArea area, final Target triggerer) {
    }
    
    @Override
    public void onEffectAreaPositionChanged(final BasicEffectArea area) {
    }
    
    static {
        m_logger = Logger.getLogger("mainLog.fightLog");
    }
    
    public class PossibleTargetsIterator implements Iterator<EffectUser>
    {
        private final Iterator<EffectUser> m_possibleTargetsIterator;
        private boolean m_hasCurrent;
        private EffectUser m_current;
        private boolean m_calledAdvanceSinceLastNext;
        
        public PossibleTargetsIterator() {
            super();
            this.m_hasCurrent = false;
            this.m_possibleTargetsIterator = (Iterator<EffectUser>)BasicFight.this.possibleTargetsIterator();
        }
        
        private boolean checkEffectUser(final EffectUser effectUser) {
            final long id = effectUser.getId();
            final F fighter = BasicFight.this.m_protagonists.getSingleFighter(ProtagonistFilter.withId(id), ProtagonistFilter.or(ProtagonistFilter.inPlay(), ProtagonistFilter.offPlay()));
            return fighter != null || (BasicFight.this.m_effectAreaManager != null && BasicFight.this.m_effectAreaManager.hasPotentialTarget(id)) || BasicFight.this.hasAdditionalTarget(id);
        }
        
        private boolean advance() {
            return this.m_hasCurrent = this.advanceInternal();
        }
        
        private boolean advanceInternal() {
            if (!this.m_possibleTargetsIterator.hasNext()) {
                return false;
            }
            final EffectUser next = this.m_possibleTargetsIterator.next();
            if (this.checkEffectUser(next)) {
                this.m_current = next;
                return true;
            }
            return this.advanceInternal();
        }
        
        private EffectUser current() {
            if (!this.m_hasCurrent) {
                throw new NoSuchElementException();
            }
            return this.m_current;
        }
        
        @Override
        public boolean hasNext() {
            if (this.m_calledAdvanceSinceLastNext) {
                return this.m_hasCurrent;
            }
            this.m_calledAdvanceSinceLastNext = true;
            return this.advance();
        }
        
        @Override
        public EffectUser next() {
            if (!this.m_calledAdvanceSinceLastNext) {
                this.advance();
            }
            this.m_calledAdvanceSinceLastNext = false;
            return this.current();
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
