package com.ankamagames.wakfu.common.game.spell;

import org.apache.log4j.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;

public class StateManager
{
    protected static final Logger m_logger;
    protected final TIntObjectHashMap<State> m_states;
    private static final StateManager m_instance;
    private StateLoader m_stateLoader;
    
    protected StateManager() {
        super();
        this.m_states = new TIntObjectHashMap<State>();
    }
    
    public static StateManager getInstance() {
        return StateManager.m_instance;
    }
    
    public void setStateLoader(final StateLoader stateLoader) {
        this.m_stateLoader = stateLoader;
    }
    
    public void addState(final State state) {
        if (state != null) {
            this.m_states.put(state.getStateBaseId(), state);
        }
    }
    
    @Nullable
    public State getState(final int stateId) {
        if (this.m_states.containsKey(stateId)) {
            return this.m_states.get(stateId);
        }
        final State state = this.loadState(stateId);
        if (state == null) {
            return null;
        }
        this.m_states.put(state.getStateBaseId(), state);
        return state;
    }
    
    public void reloadState(final int stateId) {
        if (this.m_stateLoader == null) {
            return;
        }
        final State state = this.m_stateLoader.loadState(stateId);
        if (state == null) {
            return;
        }
        this.m_states.put(state.getStateBaseId(), state);
    }
    
    @Nullable
    private State loadState(final int stateId) {
        if (this.m_stateLoader == null) {
            return null;
        }
        return this.m_stateLoader.loadState(stateId);
    }
    
    public State getBasicStateFromUniqueId(final int uniqueId) {
        final short basicId = State.getBasicIdFromUniqueId(uniqueId);
        return this.getState(basicId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)StateManager.class);
        m_instance = new StateManager();
    }
}
