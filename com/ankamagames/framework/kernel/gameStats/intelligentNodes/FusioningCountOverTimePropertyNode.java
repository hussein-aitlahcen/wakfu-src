package com.ankamagames.framework.kernel.gameStats.intelligentNodes;

import com.ankamagames.framework.kernel.gameStats.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;

public class FusioningCountOverTimePropertyNode extends ContainerNode
{
    private final Map<String, ServerData> m_values;
    private LinkedPropertyNode m_overLastHour;
    private LinkedPropertyNode m_overLastTenMinutes;
    
    public FusioningCountOverTimePropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        if (mergeMode == MergeMode.REPLACE) {
            this.m_values = null;
        }
        else {
            this.m_values = new HashMap<String, ServerData>(5);
        }
        this.m_overLastHour = this.addChild(new LinkedPropertyNode("overLastHour", this, mergeMode));
        this.m_overLastTenMinutes = this.addChild(new LinkedPropertyNode("overLastTenMinutes", this, mergeMode));
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_values.clear();
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        throw new UnsupportedOperationException("Un noeud fusionnant ne peut pas \u00eatre s\u00e9rialis\u00e9");
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        long overLastHour = buffer.getLong();
        long overLastTenMinutes = buffer.getLong();
        if (this.m_mergeMode != MergeMode.REPLACE) {
            final ServerData newData = new ServerData();
            newData.overLastHour = overLastHour;
            newData.overLastTenMinutes = overLastTenMinutes;
            this.m_values.put(source, newData);
            overLastHour = 0L;
            overLastTenMinutes = 0L;
            for (final ServerData data : this.m_values.values()) {
                overLastHour += data.overLastHour;
                overLastTenMinutes += data.overLastTenMinutes;
            }
            if (this.m_mergeMode == MergeMode.AVERAGE) {
                overLastHour /= this.m_values.size();
                overLastTenMinutes /= this.m_values.size();
            }
        }
        this.m_overLastHour.setValue(overLastHour);
        this.m_overLastTenMinutes.setValue(overLastTenMinutes);
    }
    
    private static class ServerData
    {
        public long overLastHour;
        public long overLastTenMinutes;
    }
}
