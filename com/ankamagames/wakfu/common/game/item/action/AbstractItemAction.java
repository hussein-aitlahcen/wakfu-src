package com.ankamagames.wakfu.common.game.item.action;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public abstract class AbstractItemAction implements SerializableObject
{
    private static final int ABSOLUTION_SCROLL_ID = 15188;
    private final int m_id;
    protected boolean m_mustConsumeItem;
    protected boolean m_clientOnly;
    protected boolean m_stopMovement;
    protected SimpleCriterion m_criterion;
    private boolean m_canUseDuringOccupation;
    private boolean m_hasScript;
    
    protected AbstractItemAction(final int id) {
        super();
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean isHasScript() {
        return this.m_hasScript;
    }
    
    public void setHasScript(final boolean hasScript) {
        this.m_hasScript = hasScript;
    }
    
    public void setMustConsumeItem(final boolean mustConsumeItem) {
        this.m_mustConsumeItem = mustConsumeItem;
    }
    
    public boolean isMustConsumeItem() {
        return this.m_mustConsumeItem;
    }
    
    public void setClientOnly(final boolean clientOnly) {
        this.m_clientOnly = clientOnly;
    }
    
    public boolean isClientOnly() {
        return this.m_clientOnly;
    }
    
    public void setCriterion(final SimpleCriterion criterion) {
        this.m_criterion = criterion;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public boolean needsConfirm(final int refId) {
        return refId == 15188;
    }
    
    public boolean isStopMovement() {
        return this.m_stopMovement;
    }
    
    public void setStopMovement(final boolean stopMovement) {
        this.m_stopMovement = stopMovement;
    }
    
    public boolean canUseDuringOccupation() {
        return this.m_canUseDuringOccupation;
    }
    
    public void setCanUseDuringOccupation(final boolean canUseDuringOccupation) {
        this.m_canUseDuringOccupation = canUseDuringOccupation;
    }
    
    public abstract ItemActionConstants getType();
}
