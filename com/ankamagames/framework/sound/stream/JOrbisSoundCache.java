package com.ankamagames.framework.sound.stream;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class JOrbisSoundCache extends LRUCache<String, JOrbisSound>
{
    public static final JOrbisSoundCache INSTANCE;
    
    private JOrbisSoundCache() {
        super(50);
    }
    
    public long getMemorySize() {
        long totalSize = 0L;
        for (final JOrbisSound sound : this.m_cache.values()) {
            totalSize += sound.getMemorySize();
        }
        return totalSize;
    }
    
    static {
        INSTANCE = new JOrbisSoundCache();
    }
}
