package com.ankamagames.wakfu.common.game.nation.actionRequest.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.nio.*;

public class NationMemberAddRequest extends NationActionRequest
{
    private static final Logger m_logger;
    public static final NationActionRequestFactory FACTORY;
    private long m_characterId;
    
    public NationMemberAddRequest() {
        super(NationActionRequestType.MEMBER_ADD_REQUEST);
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    @Override
    public void execute() {
        final Nation nation = this.getConcernedNation();
        if (nation == null) {
            NationMemberAddRequest.m_logger.error((Object)("Impossible d'ex\u00e9cuter l'action " + this + " : la nation " + this.m_nationId + " n'existe pas"));
            return;
        }
        nation.requestAddCitizen(this.m_characterId);
    }
    
    @Override
    public boolean authorizedFromClient(final Citizen citizen) {
        return false;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_characterId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.m_characterId = buffer.getLong();
        return true;
    }
    
    @Override
    public int serializedSize() {
        return 8;
    }
    
    @Override
    public void clear() {
        this.m_nationId = -1;
        this.m_characterId = -1L;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationMemberAddRequest.class);
        FACTORY = new NationActionRequestFactory() {
            @Override
            public NationActionRequest createNew() {
                return new NationMemberAddRequest();
            }
        };
    }
}
