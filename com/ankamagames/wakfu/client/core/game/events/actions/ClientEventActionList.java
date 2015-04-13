package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public class ClientEventActionList implements Dropable
{
    private final int m_id;
    private final short m_dropWeight;
    private final SimpleCriterion m_dropCriterion;
    private final ArrayList<ClientEventAction> m_actions;
    
    public ClientEventActionList(final int id, final short dropWeight, final SimpleCriterion dropCriterion) {
        super();
        this.m_id = id;
        this.m_dropWeight = dropWeight;
        this.m_dropCriterion = dropCriterion;
        this.m_actions = new ArrayList<ClientEventAction>();
    }
    
    public void addAction(final ClientEventAction action) {
        this.m_actions.add(action);
    }
    
    public ArrayList<ClientEventAction> getActions() {
        return this.m_actions;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public SimpleCriterion getCriterion() {
        return this.m_dropCriterion;
    }
    
    @Override
    public short getDropWeight() {
        return this.m_dropWeight;
    }
}
