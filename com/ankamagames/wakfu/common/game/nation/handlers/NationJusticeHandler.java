package com.ankamagames.wakfu.common.game.nation.handlers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.data.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public abstract class NationJusticeHandler extends NationHandler<NationJusticeEventHandler>
{
    protected static final Logger m_logger;
    private final List<NationJusticeEventHandler> m_justiceEventHandlers;
    private short m_jailInstanceId;
    private final NationLawsManager m_lawManager;
    private final NationJailPart m_jailPart;
    private final NationLawsPart m_lawsPart;
    private short m_excludedRespawnInstanceId;
    private int m_excludedRespawnX;
    private int m_excludedRespawnY;
    
    protected NationJusticeHandler(final Nation nation) {
        super(nation);
        this.m_justiceEventHandlers = new ArrayList<NationJusticeEventHandler>();
        this.m_lawManager = this.createLawManager();
        this.m_jailPart = new NationJailPart();
        this.m_lawsPart = new NationLawsPart();
    }
    
    @Override
    public void finishInitialization() {
        this.m_lawManager.finishInitialization(this.getNation(), this.m_justiceEventHandlers);
    }
    
    protected NationLawsManager createLawManager() {
        return new NationLawsManager();
    }
    
    @Override
    public void registerEventHandler(final NationJusticeEventHandler handler) {
        this.m_justiceEventHandlers.add(handler);
    }
    
    @Override
    public void unregisterEventHandler(final NationJusticeEventHandler handler) {
        this.m_justiceEventHandlers.remove(handler);
    }
    
    public void onCriminalCandidate(final long citizenId) {
        throw new UnsupportedOperationException("Impossible \u00e0 partir de ce Client/server");
    }
    
    public void onRevalidateCandidate(final long citizenId) {
        throw new UnsupportedOperationException("Impossible \u00e0 partir de ce Client/server");
    }
    
    public NationLawsManager getLawManager() {
        return this.m_lawManager;
    }
    
    public void initialize(final short jailInstanceId, final short excludedRespawnInstanceId, final int excludedRespawnX, final int excludedRespawnY) {
        this.m_jailInstanceId = jailInstanceId;
        this.m_excludedRespawnInstanceId = excludedRespawnInstanceId;
        this.m_excludedRespawnX = excludedRespawnX;
        this.m_excludedRespawnY = excludedRespawnY;
    }
    
    public short getJailInstanceId() {
        return this.m_jailInstanceId;
    }
    
    public short getExcludedRespawnInstanceId() {
        return this.m_excludedRespawnInstanceId;
    }
    
    public int getExcludedRespawnX() {
        return this.m_excludedRespawnX;
    }
    
    public int getExcludedRespawnY() {
        return this.m_excludedRespawnY;
    }
    
    public NationJailPart getJailPart() {
        return this.m_jailPart;
    }
    
    public NationLawsPart getLawsPart() {
        return this.m_lawsPart;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationJusticeHandler.class);
    }
    
    protected class NationJailPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer bb, final int version) {
            NationJusticeHandler.this.m_jailInstanceId = bb.getShort();
            NationJusticeHandler.this.m_excludedRespawnX = bb.getInt();
            NationJusticeHandler.this.m_excludedRespawnY = bb.getInt();
            NationJusticeHandler.this.m_excludedRespawnInstanceId = bb.getShort();
        }
        
        @Override
        public void serialize(final ByteBuffer bb) {
            bb.putShort(NationJusticeHandler.this.m_jailInstanceId);
            bb.putInt(NationJusticeHandler.this.m_excludedRespawnX);
            bb.putInt(NationJusticeHandler.this.m_excludedRespawnY);
            bb.putShort(NationJusticeHandler.this.m_excludedRespawnInstanceId);
        }
        
        @Override
        public int serializedSize() {
            return 12;
        }
    }
    
    private class NationLawsPart extends RawNationPart<RawNationLaw>
    {
        @Override
        protected RawNationLaw createNewRaw() {
            return new RawNationLaw();
        }
        
        @Override
        protected void toRaw(final RawNationLaw raw) {
            NationJusticeHandler.this.m_lawManager.toRaw(raw);
        }
        
        @Override
        protected void fromRaw(final RawNationLaw raw) {
            NationJusticeHandler.this.m_lawManager.fromRaw(raw);
        }
    }
}
