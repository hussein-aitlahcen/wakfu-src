package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class TutorialBinaryData implements BinaryData
{
    protected int m_id;
    protected Event[] m_eventIds;
    
    public int getId() {
        return this.m_id;
    }
    
    public Event[] getEventIds() {
        return this.m_eventIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_eventIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        final int eventIdCount = buffer.getInt();
        this.m_eventIds = new Event[eventIdCount];
        for (int iEventId = 0; iEventId < eventIdCount; ++iEventId) {
            (this.m_eventIds[iEventId] = new Event()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.TUTORIAL.getId();
    }
    
    public static class Event
    {
        protected int m_eventId;
        
        public int getEventId() {
            return this.m_eventId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_eventId = buffer.getInt();
        }
    }
}
