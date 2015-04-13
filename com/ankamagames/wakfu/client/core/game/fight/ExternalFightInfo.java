package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.dataProvider.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.graphics.isometric.lights.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.framework.graphics.engine.material.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class ExternalFightInfo implements FightInfo, InteractiveElementSelectionChangeListener, EffectExecutionListener, EffectAreaActionListener, EffectUserInformationProvider, TargetInformationProvider<EffectUser>
{
    protected static final Logger m_logger;
    private int m_id;
    private AbstractFight.FightStatus m_status;
    private final EffectContext m_context;
    private final List<CharacterInfo> m_members;
    private final List<CharacterInfo> m_membersBoundToThis;
    private final ExternalEffectAreaManager m_areaManager;
    private final TLongByteHashMap m_teamAssignments;
    private int m_refCount;
    private ArrayList<ObjectPair<Short, Short>> m_partition;
    private InOutCellLightModifier m_cellLightModifier;
    private FightMap m_fightMap;
    private CharacterInfo m_attackerCreator;
    private CharacterInfo m_defenderCreator;
    private long m_attackerCreatorId;
    private long m_defenderCreatorId;
    @NotNull
    private final FightModel m_fightModel;
    private MessageBoxControler m_currentNotification;
    
    public void addFighterToTeamById(final byte teamId, final long fighterId) {
        final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(fighterId);
        if (fighter == null) {
            this.m_teamAssignments.put(fighterId, teamId);
            return;
        }
        this.addFighter(fighter, teamId);
    }
    
    public void addFighter(final CharacterInfo fighter, final byte teamId) {
        if (!this.m_members.contains(fighter)) {
            this.m_members.add(fighter);
            this.m_teamAssignments.put(fighter.getId(), teamId);
            fighter.onJoinExternalFight();
            fighter.loadFightData();
            FightVisibilityManager.getInstance().onFighterJoinFight(fighter, this.m_id);
            if (fighter.isNotEcosystemNpc()) {
                this.m_membersBoundToThis.add(fighter);
            }
            fighter.setCurrentExternalFightInfo(this);
            fighter.getActor().addSelectionChangedListener(this);
        }
        else {
            ExternalFightInfo.m_logger.error((Object)("Ajout multiple du fighter " + fighter + " au combat externe ID=" + this.getId()));
        }
    }
    
    public void spawnFighter(final CharacterInfo character) {
        if (!this.m_teamAssignments.containsKey(character.getId())) {
            ExternalFightInfo.m_logger.error((Object)String.format("[FIGHT_REFACTOR]Tentative de spawn du fighter %d sans connaitre sa team", character.getId()));
            return;
        }
        final byte teamId = this.m_teamAssignments.get(character.getId());
        this.addFighter(character, teamId);
        boolean changed = false;
        if (this.m_defenderCreator == null && this.m_defenderCreatorId == character.getId()) {
            this.m_defenderCreator = character;
            changed = true;
        }
        if (this.m_attackerCreator == null && this.m_attackerCreatorId == character.getId()) {
            this.m_attackerCreator = character;
            changed = true;
        }
        if (changed && this.m_attackerCreator != null && this.m_defenderCreator != null) {
            FightVisibilityManager.getInstance().onExternalFightCreation(this);
        }
    }
    
    @NotNull
    @Override
    public FightModel getModel() {
        return this.m_fightModel;
    }
    
    public ExternalFightInfo(@NotNull final FightModel model) {
        super();
        this.m_id = -1;
        this.m_members = new ArrayList<CharacterInfo>();
        this.m_membersBoundToThis = new ArrayList<CharacterInfo>();
        this.m_teamAssignments = new TLongByteHashMap();
        this.m_refCount = 0;
        this.m_partition = new ArrayList<ObjectPair<Short, Short>>();
        this.m_cellLightModifier = new ExternalFightCellLightModifier();
        this.m_fightModel = model;
        this.m_context = new WakfuExternalFightEffectContext(this);
        this.m_areaManager = new ExternalEffectAreaManager(this, this.m_context);
    }
    
    @Override
    public byte getInitiatingTeamId() {
        return this.m_teamAssignments.get(this.m_attackerCreatorId);
    }
    
    protected static Material getHighLightMaterial(final byte teamId) {
        final Material highLightColor = Material.Factory.newPooledInstance();
        switch (teamId) {
            case 0: {
                highLightColor.setSpecularColor(0.5f, 0.0f, 0.0f);
                break;
            }
            case 1: {
                highLightColor.setSpecularColor(0.0f, 0.0f, 0.5f);
                break;
            }
            default: {
                if (teamId > 7) {
                    highLightColor.setSpecularColor(1.0f, 1.0f, 1.0f);
                    ExternalFightInfo.m_logger.error((Object)("team id : " + teamId + " > 7"));
                    break;
                }
                highLightColor.setSpecularColor(teamId, teamId, teamId);
                break;
            }
        }
        return highLightColor;
    }
    
    @Override
    public AbstractFight.FightStatus getStatus() {
        return this.m_status;
    }
    
    public void setStatus(final AbstractFight.FightStatus status) {
        this.m_status = status;
        if (status == AbstractFight.FightStatus.ACTION) {
            this.closeCurrentNotification();
        }
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    public void setId(final int id) {
        this.m_id = id;
    }
    
    @Override
    public Collection<CharacterInfo> getFighters() {
        return Collections.unmodifiableCollection((Collection<? extends CharacterInfo>)this.m_members);
    }
    
    @Override
    public Collection<CharacterInfo> getFightersInTeam(final byte teamId) {
        final Collection<CharacterInfo> fighters = new ArrayList<CharacterInfo>();
        for (final CharacterInfo fighter : this.m_members) {
            if (this.m_teamAssignments.get(fighter.getId()) == teamId) {
                fighters.add(fighter);
            }
        }
        return fighters;
    }
    
    @Override
    public Collection<CharacterInfo> getFightersPresentInTimelineInPlayInTeam(final byte teamId) {
        final Collection<CharacterInfo> fighters = new ArrayList<CharacterInfo>();
        for (final CharacterInfo fighter : this.m_members) {
            if (this.m_teamAssignments.get(fighter.getId()) == teamId && fighter.isInPlay() && !fighter.isActiveProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)) {
                fighters.add(fighter);
            }
        }
        return fighters;
    }
    
    @Override
    public Collection<CharacterInfo> getFightersNotInTeam(final byte teamId) {
        final Collection<CharacterInfo> fighters = new ArrayList<CharacterInfo>();
        for (final CharacterInfo fighter : this.m_members) {
            if (this.m_teamAssignments.get(fighter.getId()) != teamId) {
                fighters.add(fighter);
            }
        }
        return fighters;
    }
    
    @Override
    public CharacterInfo getFighterFromId(final long characterId) {
        for (final CharacterInfo member : this.m_members) {
            if (member.getId() == characterId) {
                return member;
            }
        }
        return null;
    }
    
    public void removeFighter(final CharacterInfo member) {
        if (!this.m_members.remove(member)) {
            return;
        }
        FightVisibilityManager.getInstance().onFighterLeaveFight(member, this.m_id);
        this.cleanExternalFightSpecificFighterDisplay(member);
        member.getRunningEffectManager().clear();
        member.setObstacleId((byte)(-1));
    }
    
    @Override
    public JoinFightResult canJoinTeam(final CharacterInfo f, final byte teamId) {
        if (f.isOnFight()) {
            return JoinFightResult.ALREADY_IN_FIGHT;
        }
        if (f.isDead()) {
            return JoinFightResult.PLAYER_IS_DEAD;
        }
        if (teamId < 0 || teamId >= this.m_fightModel.getMaxTeam()) {
            return JoinFightResult.TEAM_DOESNT_EXIST;
        }
        if (!JoinTeamHelper.canJoinMonsterTeam(this, f, teamId)) {
            return JoinFightResult.CANT_JOIN_MONSTER_TEAM;
        }
        if (!JoinTeamHelper.canJoinOpposingPartyTeam(this, f, teamId)) {
            return JoinFightResult.CANT_JOIN_OPPOSING_PARTY_TEAM;
        }
        final JoinFightResult protectorFightError = JoinTeamHelper.canJoinProtectorFight(this, f, teamId);
        if (protectorFightError != JoinFightResult.OK) {
            return protectorFightError;
        }
        final JoinFightResult pvpResult = NationPvpHelper.testPlayerCanJoinPvpFightOf(this, f, teamId);
        if (pvpResult != JoinFightResult.OK) {
            return pvpResult;
        }
        if (teamId == 0 && this.m_fightModel.isNoFighterLimitForRedTeam()) {
            return JoinFightResult.OK;
        }
        final Collection<CharacterInfo> team = this.getFightersInTeam(teamId);
        if (team.size() >= this.m_fightModel.getMaxFighterByTeam()) {
            return JoinFightResult.TEAM_IS_FULL;
        }
        return JoinFightResult.OK;
    }
    
    @Override
    public void selectionChanged(final AnimatedInteractiveElement object, final boolean selected) {
        final Iterator<CharacterInfo> it = this.getFighters().iterator();
        if (selected) {
            if (CameraMouseMove.isForceActivate()) {
                while (it.hasNext()) {
                    final CharacterInfo fighter = it.next();
                    final Material material = getHighLightMaterial(fighter.getTeamId());
                    fighter.getActor().colorize(material);
                    material.removeReference();
                }
            }
        }
        else {
            while (it.hasNext()) {
                final Actor fighterActor = it.next().getActor();
                fighterActor.resetColor();
            }
        }
    }
    
    public boolean removeSelectionChangedListenerForAll() {
        for (final CharacterInfo characterInfo : this.getFighters()) {
            this.removeSelectionChangedListenerForSpecific(characterInfo);
        }
        return true;
    }
    
    private boolean removeSelectionChangedListenerForSpecific(final CharacterInfo fighter) {
        final Actor fighterActor = fighter.getActor();
        fighterActor.removeSelectionChangedListener(this);
        return true;
    }
    
    public void cleanExternalFightAllFighterDisplay() {
        for (final CharacterInfo characterInfo : this.getFighters()) {
            this.cleanExternalFightSpecificFighterDisplay(characterInfo);
        }
    }
    
    public void cleanExternalFightSpecificFighterDisplay(final CharacterInfo fighter) {
        this.removeSelectionChangedListenerForSpecific(fighter);
        fighter.getActor().resetAlpha();
    }
    
    @Override
    public void onEffectApplication(final RunningEffect effect) {
        if (!effect.hasDuration()) {
            return;
        }
        if (effect.getTarget() != null) {
            if (effect.getTarget() instanceof CharacterInfo) {
                ((CharacterInfo)effect.getTarget()).getActor().applyOnApplicationHMIAction((WakfuRunningEffect)effect, false);
            }
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnApplicationHMIAction((WakfuRunningEffect)effect, true);
        }
    }
    
    @Override
    public void onEffectDirectExecution(final RunningEffect effect) {
        if (effect.getTarget() != null) {
            if (effect.getTarget() instanceof CharacterInfo) {
                ((CharacterInfo)effect.getTarget()).getActor().applyOnExecutionHMIAction((WakfuRunningEffect)effect, false);
            }
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnExecutionHMIAction((WakfuRunningEffect)effect, true);
        }
    }
    
    @Override
    public void onEffectTriggeredExecution(final RunningEffect effect) {
        this.onEffectDirectExecution(effect);
    }
    
    @Override
    public void onEffectUnApplication(final RunningEffect effect) {
        if (!effect.hasDuration()) {
            return;
        }
        if (effect.getTarget() != null) {
            if (effect.getTarget() instanceof CharacterInfo) {
                ((CharacterInfo)effect.getTarget()).getActor().applyOnUnapplicationHMIAction((WakfuRunningEffect)effect, false);
            }
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnUnapplicationHMIAction((WakfuRunningEffect)effect, true);
        }
    }
    
    @Override
    public void onEffectAreaAdded(final BasicEffectArea area) {
        FightVisibilityManager.getInstance().onBasicEffectAreaAdded(this, area);
    }
    
    @Override
    public void onEffectAreaApplication(final BasicEffectArea area, final Target applicant) {
    }
    
    @Override
    public void onEffectAreaExecuted(final BasicEffectArea area) {
    }
    
    @Override
    public void onEffectAreaPreExecution(final BasicEffectArea area, final Target triggerer) {
    }
    
    @Override
    public void onEffectAreaPositionChanged(final BasicEffectArea area) {
        FightVisibilityManager.getInstance().onBasicEffectAreaTeleported(this, area);
    }
    
    @Override
    public void onEffectAreaRemoved(final BasicEffectArea area) {
        FightVisibilityManager.getInstance().onBasicEffectAreaRemoved(this, area);
    }
    
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
        if (area == null) {
            return;
        }
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
    public void onEffectAreaUnapplication(final BasicEffectArea area, final Target unapplicant) {
    }
    
    @Override
    public EffectContext getContext() {
        return this.m_context;
    }
    
    @Override
    public BasicEffectAreaManager getEffectAreaManager() {
        return this.m_areaManager;
    }
    
    @Override
    public EffectUser getEffectUserFromId(final long effectUserId) {
        EffectUser user = null;
        if (this.m_areaManager != null) {
            user = this.m_areaManager.getEffectAreaWithId(effectUserId);
        }
        if (user != null) {
            return user;
        }
        for (final CharacterInfo info : this.m_members) {
            if (info.getId() == effectUserId) {
                return info;
            }
        }
        return null;
    }
    
    public Iterator<EffectUser> getEffectUsers() {
        return new MergedIterator<EffectUser>(this.m_members.iterator(), this.m_areaManager.getEffectAreaList().iterator());
    }
    
    public int getEffectUsersCount() {
        return this.m_members.size() + this.m_areaManager.getActiveEffectAreaCount();
    }
    
    @Override
    public long getNextFreeEffectUserId(final byte effectUserType) {
        throw new UnsupportedOperationException("On ne doit pas cr\u00e9er de nouvelles ID ici");
    }
    
    public void onFightDestroyed() {
        while (!this.m_members.isEmpty()) {
            this.reallyRemoveCharacter(this.m_members.get(0));
        }
        this.closeCurrentNotification();
        if (this.m_cellLightModifier != null) {
            IsoSceneLightManager.INSTANCE.removeLightingModifier((LitSceneModifier)this.m_cellLightModifier);
            this.m_cellLightModifier = null;
        }
    }
    
    private void reallyRemoveCharacter(final CharacterInfo character) {
        try {
            this.removeFighter(character);
        }
        catch (Exception e) {
            ExternalFightInfo.m_logger.error((Object)("Erreur au retrait d'un personnage depuis une ExternalFightInfo " + ExceptionFormatter.toString(e, 10)));
        }
        finally {
            character.setCurrentExternalFightInfo(null);
        }
    }
    
    @Override
    public Iterator<EffectUser> getAllPossibleTargets() {
        return this.getEffectUsers();
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final Point3 pos) {
        return this.getPossibleTargetsAtPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final int x, final int y, final int z) {
        final ArrayList<EffectUser> list = new ArrayList<EffectUser>();
        final Iterator<EffectUser> it = this.getAllPossibleTargets();
        while (it.hasNext()) {
            final EffectUser eu = it.next();
            if (DistanceUtils.getIntersectionDistance(eu, x, y) == 0) {
                list.add(eu);
            }
        }
        return list;
    }
    
    @Override
    public void endFight() {
        this.cleanExternalFightAllFighterDisplay();
        final Iterable<CharacterInfo> fighters = new ArrayList<CharacterInfo>(this.getFighters());
        for (final CharacterInfo fighter : fighters) {
            try {
                this.removeAndReloadFighter(fighter);
            }
            catch (Exception e) {
                ExternalFightInfo.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        for (final BasicEffectArea area : this.getEffectAreaManager().getActiveEffectAreas()) {
            this.onEffectAreaRemoved(area);
        }
        if (this.m_cellLightModifier != null) {
            IsoSceneLightManager.INSTANCE.removeLightingModifier((LitSceneModifier)this.m_cellLightModifier);
        }
        for (final CharacterInfo characterInfo : this.m_membersBoundToThis) {
            CharacterInfoManager.getInstance().removeCharacter(characterInfo);
        }
        this.m_teamAssignments.clear();
        this.m_members.clear();
        this.m_membersBoundToThis.clear();
        FightVisibilityManager.getInstance();
        FightVisibilityManager.onFightEnd(this);
    }
    
    public void removeAndReloadFighter(final CharacterInfo characterInfo) {
        this.removeFighter(characterInfo);
        characterInfo.setCurrentExternalFightInfo(null);
        characterInfo.reloadCharacterAfterExternalFightEnded();
        characterInfo.onLeaveExternalFight();
        FightVisibilityManager.getInstance().onFighterLeaveFight(characterInfo, this.getId());
    }
    
    public void setPartition(final ArrayList<ObjectPair<Short, Short>> partition) {
        this.m_partition = partition;
    }
    
    public ArrayList<ObjectPair<Short, Short>> getPartitions() {
        return this.m_partition;
    }
    
    public void addReference() {
        ++this.m_refCount;
    }
    
    public void removeReference() {
        --this.m_refCount;
        if (this.m_refCount == 0) {
            final FightInfo fightById = FightManager.getInstance().getFightById(this.getId());
            if (fightById instanceof ExternalFightInfo) {
                this.endFight();
                FightManager.getInstance().destroyFight(this);
            }
        }
    }
    
    @Override
    public InOutCellLightModifier getCellLightModifier() {
        return this.m_cellLightModifier;
    }
    
    public void setFightMap(final FightMap fightMap) {
        this.m_fightMap = fightMap;
    }
    
    @Override
    public FightMap getFightMap() {
        return this.m_fightMap;
    }
    
    @Override
    public byte getTeamId(final long fighterId) {
        if (this.m_teamAssignments.contains(fighterId)) {
            return this.m_teamAssignments.get(fighterId);
        }
        return -1;
    }
    
    public void setCurrentNotification(final MessageBoxControler currentNotification) {
        this.closeCurrentNotification();
        this.m_currentNotification = currentNotification;
    }
    
    private void closeCurrentNotification() {
        if (this.m_currentNotification != null) {
            this.m_currentNotification.messageBoxClosed(16, null);
        }
        this.m_currentNotification = null;
    }
    
    public void notificationExpired(final MessageBoxControler notification) {
        if (notification == this.m_currentNotification) {
            this.m_currentNotification = null;
        }
    }
    
    public void setAttackerCreatorId(final long id) {
        this.m_attackerCreatorId = id;
    }
    
    public void setDefenderCreatorId(final long id) {
        this.m_defenderCreatorId = id;
    }
    
    public void setAttackerCreator(final CharacterInfo attackerCreator) {
        this.m_attackerCreator = attackerCreator;
    }
    
    public void setDefenderCreator(final CharacterInfo defenderCreator) {
        this.m_defenderCreator = defenderCreator;
    }
    
    public CharacterInfo getAttackerCreator() {
        return this.m_attackerCreator;
    }
    
    public CharacterInfo getDefenderCreator() {
        return this.m_defenderCreator;
    }
    
    @Override
    public boolean shouldDisplayAreaForLocalPlayer(final BasicEffectArea area) {
        return area.getType() != EffectAreaType.TRAP.getTypeId() && area.getType() != EffectAreaType.ENUTROF_DEPOSIT.getTypeId() && area.getType() != EffectAreaType.HOUR.getTypeId();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExternalFightInfo.class);
    }
}
