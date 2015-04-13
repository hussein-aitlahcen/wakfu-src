package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;

public class NationSelectionInfoResult extends InputOnlyProxyMessage
{
    private ArrayList<NationSelectionInfo> m_nationSelectionInfos;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        this.m_nationSelectionInfos = new ArrayList<NationSelectionInfo>();
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            final int nationId = bb.getInt();
            final int totalCash = bb.getInt();
            final float populationPercent = bb.getFloat();
            final byte[] utfName = new byte[bb.getInt()];
            bb.get(utfName);
            final String governorName = StringUtils.fromUTF8(utfName);
            final int protectorsSize = bb.getInt();
            final TIntByteHashMap alignments = new TIntByteHashMap();
            for (int j = 0, nationCount = bb.getInt(); j < nationCount; ++j) {
                alignments.put(bb.getInt(), bb.get());
            }
            this.m_nationSelectionInfos.add(new NationSelectionInfo(nationId, totalCash, populationPercent, governorName, protectorsSize, alignments));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 20300;
    }
    
    public ArrayList<NationSelectionInfo> getNationSelectionInfos() {
        return this.m_nationSelectionInfos;
    }
    
    public static class NationSelectionInfo
    {
        private final int m_nationId;
        private final int m_totalCash;
        private final float m_populationPercent;
        private final String m_governorName;
        private final int m_protectorsSize;
        private final TIntByteHashMap m_alignments;
        
        public NationSelectionInfo(final int nationId, final int totalCash, final float populationPercent, final String governorName, final int protectorsSize, final TIntByteHashMap alignments) {
            super();
            this.m_nationId = nationId;
            this.m_totalCash = totalCash;
            this.m_populationPercent = populationPercent;
            this.m_governorName = governorName;
            this.m_protectorsSize = protectorsSize;
            this.m_alignments = alignments;
        }
        
        public int getNationId() {
            return this.m_nationId;
        }
        
        public int getTotalCash() {
            return this.m_totalCash;
        }
        
        public float getPopulationPercent() {
            return this.m_populationPercent;
        }
        
        public String getGovernorName() {
            return this.m_governorName;
        }
        
        public int getProtectorsSize() {
            return this.m_protectorsSize;
        }
        
        public TIntByteHashMap getAlignments() {
            return this.m_alignments;
        }
    }
}
