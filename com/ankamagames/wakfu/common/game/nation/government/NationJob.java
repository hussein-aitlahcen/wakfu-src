package com.ankamagames.wakfu.common.game.nation.government;

import gnu.trove.*;
import java.util.*;

public enum NationJob
{
    GUARD(1, NationRank.MARSHAL), 
    SOLDIER(2, NationRank.GENERAL);
    
    private static final TByteObjectHashMap<NationJob> HELPER;
    private final byte m_id;
    private long m_mask;
    private final NationRank m_parentRank;
    
    private NationJob(final int id, final NationRank parentRank) {
        this.m_id = (byte)id;
        this.m_mask = 1 << this.m_id;
        this.m_parentRank = parentRank;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public long getMask() {
        return this.m_mask;
    }
    
    public NationRank getParentRank() {
        return this.m_parentRank;
    }
    
    public static long toLong(final EnumSet<NationJob> jobs) {
        long mask = 0L;
        final Iterator<NationJob> it = jobs.iterator();
        while (it.hasNext()) {
            mask |= it.next().m_mask;
        }
        return mask;
    }
    
    public static EnumSet<NationJob> fromLong(final long longJobs) {
        final EnumSet<NationJob> result = EnumSet.noneOf(NationJob.class);
        final NationJob[] jobs = values();
        for (int i = 0; i < jobs.length; ++i) {
            final NationJob job = jobs[i];
            if ((longJobs & job.m_mask) == job.m_mask) {
                result.add(job);
            }
        }
        return result;
    }
    
    public static NationJob fromId(final byte id) {
        return NationJob.HELPER.get(id);
    }
    
    static {
        HELPER = new TByteObjectHashMap<NationJob>();
        final NationJob[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final NationJob value = values[i];
            NationJob.HELPER.put(value.m_id, value);
        }
    }
}
