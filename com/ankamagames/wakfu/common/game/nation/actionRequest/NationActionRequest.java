package com.ankamagames.wakfu.common.game.nation.actionRequest;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.*;

public abstract class NationActionRequest implements SerializableObject
{
    protected static final Logger m_logger;
    private final NationActionRequestType m_type;
    protected int m_nationId;
    
    protected NationActionRequest(final NationActionRequestType type) {
        super();
        this.m_type = type;
    }
    
    public NationActionRequestType getType() {
        return this.m_type;
    }
    
    public void setNationId(final int nationId) {
        this.m_nationId = nationId;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    protected final Nation getConcernedNation() {
        return NationManager.INSTANCE.getNationById(this.m_nationId);
    }
    
    public abstract void execute();
    
    public abstract boolean authorizedFromClient(final Citizen p0);
    
    static {
        m_logger = Logger.getLogger((Class)NationActionRequest.class);
    }
}
