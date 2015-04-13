package com.ankamagames.wakfu.common.game.nation.actionRequest.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.nio.*;

public class NationMemberManageNationJobRequest extends NationActionRequest
{
    public static byte ADD;
    public static byte REMOVE;
    private static final Logger m_logger;
    public static final NationActionRequestFactory FACTORY;
    private byte m_operation;
    private long m_characterId;
    private byte m_nationJobId;
    
    public NationMemberManageNationJobRequest() {
        super(NationActionRequestType.MEMBER_ADD_NATION_JOB);
    }
    
    public void setOperation(final byte operation) {
        this.m_operation = operation;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setNationJob(final NationJob job) {
        this.m_nationJobId = job.getId();
    }
    
    @Override
    public void execute() {
        final Nation nation = this.getConcernedNation();
        if (nation == null) {
            NationMemberManageNationJobRequest.m_logger.error((Object)("Impossible d'ex\u00e9cuter l'action " + this + " : la nation " + this.m_nationId + " n'existe pas"));
            return;
        }
        if (this.m_operation == NationMemberManageNationJobRequest.ADD) {
            nation.requestAddNationJob(this.m_characterId, NationJob.fromId(this.m_nationJobId));
        }
        else if (this.m_operation == NationMemberManageNationJobRequest.REMOVE) {
            nation.requestRemoveNationJob(this.m_characterId, NationJob.fromId(this.m_nationJobId));
        }
    }
    
    @Override
    public boolean authorizedFromClient(final Citizen citizen) {
        return false;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.m_operation);
        buffer.putLong(this.m_characterId);
        buffer.put(this.m_nationJobId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.m_operation = buffer.get();
        this.m_characterId = buffer.getLong();
        this.m_nationJobId = buffer.get();
        return true;
    }
    
    @Override
    public int serializedSize() {
        return 10;
    }
    
    @Override
    public void clear() {
        this.m_nationId = -1;
        this.m_operation = -1;
        this.m_characterId = -1L;
        this.m_nationJobId = -1;
    }
    
    static {
        NationMemberManageNationJobRequest.ADD = 1;
        NationMemberManageNationJobRequest.REMOVE = 2;
        m_logger = Logger.getLogger((Class)NationMemberManageNationJobRequest.class);
        FACTORY = new NationActionRequestFactory() {
            @Override
            public NationActionRequest createNew() {
                return new NationMemberManageNationJobRequest();
            }
        };
    }
}
