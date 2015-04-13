package com.ankamagames.wakfu.common.game.nation.actionRequest.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.nio.*;

public class NationProtectorBuffsAddRequest extends NationActionRequest
{
    private static final Logger m_logger;
    public static final NationActionRequestFactory FACTORY;
    private int m_protectorId;
    private int[] m_buffs;
    
    public NationProtectorBuffsAddRequest() {
        super(NationActionRequestType.PROTECTOR_BUFFS_ADD);
    }
    
    public void setProtectorId(final int protectorId) {
        this.m_protectorId = protectorId;
    }
    
    public void setBuffs(final int[] buffs) {
        this.m_buffs = buffs;
    }
    
    @Override
    public void execute() {
        final Nation nation = this.getConcernedNation();
        if (nation == null) {
            NationProtectorBuffsAddRequest.m_logger.error((Object)("Impossible d'ex\u00e9cuter l'action " + this + " : la nation " + this.m_nationId + " n'existe pas"));
            return;
        }
        nation.requestAddProtectorBuffs(this.m_protectorId, this.m_buffs);
    }
    
    @Override
    public boolean authorizedFromClient(final Citizen citizen) {
        return false;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.m_protectorId);
        if (this.m_buffs != null) {
            for (int buffsCount = this.m_buffs.length, i = 0; i < buffsCount; ++i) {
                buffer.putInt(this.m_buffs[i]);
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.m_protectorId = buffer.getInt();
        final int remainingBytes = buffer.remaining();
        if (remainingBytes % 4 != 0) {
            NationProtectorBuffsAddRequest.m_logger.error((Object)"Impossible de d\u00e9s\u00e9rialiser une nationActionRequest. Nombre d'octets restants non valides pour former des entiers... pas multiple de 4.");
            return false;
        }
        final int buffsCount = remainingBytes / 4;
        this.m_buffs = new int[buffsCount];
        for (int i = 0; i < buffsCount; ++i) {
            this.m_buffs[i] = buffer.getInt();
        }
        return true;
    }
    
    @Override
    public int serializedSize() {
        if (this.m_buffs == null) {
            return 4;
        }
        return 4 + 4 * this.m_buffs.length;
    }
    
    @Override
    public void clear() {
        this.m_buffs = null;
        this.m_nationId = -1;
        this.m_protectorId = -1;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationProtectorBuffsAddRequest.class);
        FACTORY = new NationActionRequestFactory() {
            @Override
            public NationActionRequest createNew() {
                return new NationProtectorBuffsAddRequest();
            }
        };
    }
}
