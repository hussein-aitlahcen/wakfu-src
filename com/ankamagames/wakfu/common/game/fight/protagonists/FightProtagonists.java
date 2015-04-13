package com.ankamagames.wakfu.common.game.fight.protagonists;

import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;
import java.util.*;
import java.io.*;

public class FightProtagonists<F extends BasicCharacterInfo>
{
    private final TLongObjectHashMap<FighterState> m_fightersStates;
    private final TLongObjectHashMap<F> m_fightersById;
    private static final Logger m_logger;
    
    public FightProtagonists() {
        super();
        this.m_fightersStates = new TLongObjectHashMap<FighterState>();
        this.m_fightersById = new TLongObjectHashMap<F>();
    }
    
    private FighterState getState(final BasicCharacterInfo fighter) {
        if (fighter == null) {
            return new NullFighterState(null);
        }
        final FighterState state = this.m_fightersStates.get(fighter.getId());
        return (state == null) ? new NullFighterState(fighter) : state;
    }
    
    public TLongObjectHashMap<FighterState> getFightersStates() {
        return this.m_fightersStates.clone();
    }
    
    public void addFighter(final F fighter, final byte teamId, final boolean liveOnlyThisFight, final F controller) {
        if (fighter == null) {
            FightProtagonists.m_logger.error((Object)("[FIGHT] fighter cannot be null - " + ExceptionFormatter.currentStackTrace()));
            return;
        }
        if (!this.controlErrorHelper(controller, "controller")) {
            return;
        }
        if (this.containsFighter(fighter)) {
            FightProtagonists.m_logger.error((Object)String.format("[FIGHT] On ajoute le fighter %d d\u00e9j\u00e0 pr\u00e9sent -> r\u00e9initialisation de son \u00e9tat.", fighter.getId()));
        }
        this.m_fightersStates.put(fighter.getId(), this.initializeState(teamId, liveOnlyThisFight, controller));
        this.m_fightersById.put(fighter.getId(), fighter);
    }
    
    public void addFighterState(final long fighterId, final FighterState fighterState) {
        this.m_fightersStates.put(fighterId, fighterState);
    }
    
    private FighterState initializeState(final byte teamId, final boolean localToFight, final F controller) {
        return new FighterState(teamId, !this.teamHasLeader(teamId), localToFight, controller.getId());
    }
    
    public boolean putInPlay(final F fighter) {
        return this.getState(fighter).setPlayState(FighterPlayState.IN_PLAY);
    }
    
    public boolean putOffPlay(final F fighter) {
        return this.getState(fighter).setPlayState(FighterPlayState.OFF_PLAY);
    }
    
    public boolean putOutOfPlay(final F fighter) {
        return this.getState(fighter).setPlayState(FighterPlayState.OUT_OF_PLAY);
    }
    
    public void removeFighter(final F fighter) {
        if (!this.containsFighter(fighter)) {
            FightProtagonists.m_logger.error((Object)String.format("[FIGHT] removeFighter sur un fighter absent %d - %s", fighter.getId(), ExceptionFormatter.currentStackTrace(10)));
            return;
        }
        final FighterState state = this.m_fightersStates.remove(fighter.getId());
        this.m_fightersById.remove(fighter.getId());
        if (state.isTeamLeader()) {
            final Collection<F> fs = this.getFighters(ProtagonistFilter.inOriginalTeam(state.getTeamId()));
            if (!fs.isEmpty()) {
                this.getState(fs.iterator().next()).setTeamLeader(true);
            }
        }
    }
    
    public boolean isInPlay(final BasicCharacterInfo fighter) {
        return this.getState(fighter).isInPlay();
    }
    
    public FighterPlayState getPlayState(final BasicCharacterInfo fighter) {
        return this.getState(fighter).getPlayState();
    }
    
    public boolean isOffPlay(final BasicCharacterInfo fighter) {
        return this.getState(fighter).isOffPlay();
    }
    
    public boolean isOutOfPlay(final BasicCharacterInfo fighter) {
        return this.getState(fighter).isOutOfPlay();
    }
    
    public F getFighterById(final long fighterId) {
        return this.m_fightersById.get(fighterId);
    }
    
    public boolean containsFighter(final F fighter) {
        return fighter != null && this.m_fightersStates.contains(fighter.getId());
    }
    
    public boolean isLocalFighter(final BasicCharacterInfo fighter) {
        return this.getState(fighter).isLocalToFight();
    }
    
    public void setCurrentController(final F controlled, final F controller) {
        if (!this.controlErrorHelper(controller, "controller")) {
            return;
        }
        if (!this.controlErrorHelper(controlled, "controlled")) {
            return;
        }
        final FighterState state = this.getState(controlled);
        final long controllerId = controller.getId();
        state.setCurrentControllerId(controllerId, controller.getTeamId());
    }
    
    public void partialRemoveController(final F controller) {
        if (!this.controlErrorHelper(controller, "controller")) {
            return;
        }
        this.m_fightersStates.forEachValue(new TObjectProcedure<FighterState>() {
            @Override
            public boolean execute(final FighterState fighterState) {
                if (fighterState.isCurrentlyControlledBy(controller.getId())) {
                    fighterState.setCurrentControllerId(null, (byte)(-1));
                }
                return true;
            }
        });
    }
    
    public void removeControlledFighter(final F controlled) {
        if (!this.controlErrorHelper(controlled, "controlled")) {
            return;
        }
        this.getState(controlled).setCurrentControllerId(null, (byte)(-1));
    }
    
    public void returnToOriginalController(final F controlled) {
        if (!this.controlErrorHelper(controlled, "controlled")) {
            return;
        }
        final FighterState state = this.getState(controlled);
        state.setCurrentControllerId(state.getOriginalControllerId(), state.getOriginalTeamId());
    }
    
    public Collection<F> getFightersControlledBy(final F controller) {
        if (!this.controlErrorHelper(controller, "controller")) {
            return (Collection<F>)Collections.emptySet();
        }
        return this.getFighters(ProtagonistFilter.controlledBy(controller));
    }
    
    public Collection<F> getFightersOriginallyControlledBy(final F controller) {
        if (!this.controlErrorHelper(controller, "controller")) {
            return (Collection<F>)Collections.emptySet();
        }
        return this.getFighters(ProtagonistFilter.originallyControlledBy(controller));
    }
    
    public F getController(final F controlled) {
        if (!this.controlErrorHelper(controlled, "controlled")) {
            return null;
        }
        return this.getFighterByNullableId(this.getState(controlled).getCurrentControllerId());
    }
    
    public F getOriginalController(final F controlled) {
        if (!this.controlErrorHelper(controlled, "controlled")) {
            return null;
        }
        return this.getFighterById(this.getState(controlled).getOriginalControllerId());
    }
    
    private F getFighterByNullableId(final Long fighterId) {
        return (F)((fighterId == null) ? null : this.getFighterById(fighterId));
    }
    
    private boolean controlErrorHelper(final F parameterValue, final String parameterName) {
        if (parameterValue == null) {
            FightProtagonists.m_logger.error((Object)String.format("[FIGHT_CONTROLLERS] %s cannot be null - %s", parameterName, ExceptionFormatter.currentStackTrace(1, 10)));
            return false;
        }
        return true;
    }
    
    public Collection<F> controllersInOriginalTeam(final byte teamId) {
        return this.getFighters(ProtagonistFilter.inOriginalTeam(teamId), ProtagonistFilter.controller());
    }
    
    private boolean teamHasLeader(final byte teamId) {
        return !this.getFighters(ProtagonistFilter.inOriginalTeam(teamId), ProtagonistFilter.leaderOfTeam()).isEmpty();
    }
    
    public byte getTeamId(final BasicCharacterInfo character) {
        return this.getState(character).getTeamId();
    }
    
    public byte getOriginalTeamId(final BasicCharacterInfo character) {
        return this.getState(character).getOriginalTeamId();
    }
    
    public void setTeamId(final BasicCharacterInfo characterInfo, final byte teamId) {
        this.getState(characterInfo).setTeamId(teamId);
    }
    
    public boolean isTeamLeader(final BasicCharacterInfo fighter) {
        return this.getState(fighter).isTeamLeader();
    }
    
    public F getSingleFighter(final ProtagonistFilter... specs) {
        final Collection<F> value = this.getFighters(specs);
        if (value.isEmpty()) {
            return null;
        }
        if (value.size() > 1) {
            final StringBuilder builder = new StringBuilder("On a plusieurs fighters diff\u00e9rents alors qu'on en attendait un seul ");
            for (final ProtagonistFilter spec : specs) {
                builder.append(spec).append(", ");
            }
            if (specs.length > 0) {
                builder.setLength(builder.length() - 2);
            }
            FightProtagonists.m_logger.error((Object)builder.toString());
        }
        return value.iterator().next();
    }
    
    public Collection<F> getFighters(final ProtagonistFilter... specs) {
        final Collection<F> value = new ArrayList<F>();
        this.m_fightersById.forEachValue(new TObjectProcedure<F>() {
            @Override
            public boolean execute(final F f) {
                if (FightProtagonists.this.acceptFigther(f, specs)) {
                    value.add(f);
                }
                return true;
            }
        });
        return value;
    }
    
    private boolean acceptFigther(final F fighter, final ProtagonistFilter... specs) {
        for (final ProtagonistFilter spec : specs) {
            if (!spec.matches(fighter, this.getState(fighter))) {
                return false;
            }
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightProtagonists.class);
    }
}
