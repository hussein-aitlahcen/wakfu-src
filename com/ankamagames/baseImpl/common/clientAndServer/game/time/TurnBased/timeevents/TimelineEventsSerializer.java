package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import java.nio.*;
import gnu.trove.*;
import java.util.*;

public final class TimelineEventsSerializer
{
    private static final Logger m_logger;
    
    public static byte[] serialize(final TimelineEvents timelineEvents) {
        final ByteArray ba = new ByteArray();
        serializeInactiveFighters(timelineEvents, ba);
        serializeEvents(timelineEvents, ba);
        return ba.toArray();
    }
    
    public static void unserialize(final byte[] data, final TimelineUnmarshallingContext context, final TimelineEvents res) {
        final ByteBuffer bb = ByteBuffer.wrap(data);
        unserializeInactiveFighters(res, bb);
        unserializeEvents(res, bb, context);
    }
    
    private static void serializeEvents(final TimelineEvents timelineEvents, final ByteArray ba) {
        serializeAtStartEvents(timelineEvents, ba);
        serializeAtEndEvents(timelineEvents, ba);
    }
    
    private static void unserializeEvents(final TimelineEvents res, final ByteBuffer bb, final TimelineUnmarshallingContext context) {
        unserializeAtStartEvents(res, bb, context);
        unserializeAtEndEvents(res, bb, context);
    }
    
    private static void unserializeAtStartEvents(final TimelineEvents res, final ByteBuffer bb, final TimelineUnmarshallingContext context) {
        unserializeEvents(res, bb, res.getAtStartDelayableTimeEvents(), context);
    }
    
    private static void unserializeAtEndEvents(final TimelineEvents res, final ByteBuffer bb, final TimelineUnmarshallingContext context) {
        unserializeEvents(res, bb, res.getAtEndDelayableTimeEvents(), context);
    }
    
    private static void serializeAtStartEvents(final TimelineEvents timelineEvents, final ByteArray ba) {
        serializeEvents(ba, timelineEvents.getAtStartDelayableTimeEvents());
    }
    
    private static void serializeAtEndEvents(final TimelineEvents timelineEvents, final ByteArray ba) {
        serializeEvents(ba, timelineEvents.getAtEndDelayableTimeEvents());
    }
    
    private static void serializeEvents(final ByteArray ba, final TShortObjectHashMap<List<DelayableTimeEvent>> events) {
        ba.putInt(events.size());
        events.forEachEntry(new EventsListSerializer(ba));
    }
    
    private static void unserializeEvents(final TimelineEvents res, final ByteBuffer bb, final TShortObjectHashMap<List<DelayableTimeEvent>> eventsMap, final TimelineUnmarshallingContext context) {
        final int eventsSize = bb.getInt();
        final EventsListUnserializer unserializer = new EventsListUnserializer(context);
        unserializer.unserializeEventsList(res, bb, eventsSize, eventsMap);
    }
    
    private static void serializeInactiveFighters(final TimelineEvents timelineEvents, final ByteArray ba) {
        final TLongHashSet inactiveFighters = timelineEvents.getInactiveFighters();
        ba.put((byte)inactiveFighters.size());
        inactiveFighters.forEach(new TLongProcedure() {
            @Override
            public boolean execute(final long value) {
                ba.putLong(value);
                return true;
            }
        });
    }
    
    private static void unserializeInactiveFighters(final TimelineEvents timelineEvents, final ByteBuffer bb) {
        final byte size = bb.get();
        for (int i = 0; i < size; ++i) {
            timelineEvents.fighterIsShelved(bb.getLong());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimelineEventsSerializer.class);
    }
    
    private static class EventsListSerializer implements TShortObjectProcedure<List<DelayableTimeEvent>>
    {
        private final ByteArray m_ba;
        
        private EventsListSerializer(final ByteArray ba) {
            super();
            this.m_ba = ba;
        }
        
        @Override
        public boolean execute(final short a, final List<DelayableTimeEvent> b) {
            this.serializeEventsList(a, b);
            return true;
        }
        
        private void serializeEventsList(final short a, final List<DelayableTimeEvent> b) {
            this.m_ba.putShort(a);
            this.m_ba.putInt(b.size());
            for (final DelayableTimeEvent delayableTimeEvent : b) {
                final int size = delayableTimeEvent.serializedSize();
                final ByteBuffer bb = ByteBuffer.allocate(size);
                delayableTimeEvent.serialize(bb);
                this.m_ba.putInt(size);
                this.m_ba.put(bb.array());
            }
        }
    }
    
    private static final class EventsListUnserializer
    {
        private final TimelineUnmarshallingContext m_context;
        
        private EventsListUnserializer(final TimelineUnmarshallingContext context) {
            super();
            this.m_context = context;
        }
        
        void unserializeEventsList(final TimelineEvents timelineEvents, final ByteBuffer bb, final int size, final TShortObjectHashMap<List<DelayableTimeEvent>> events) {
            for (int i = 0; i < size; ++i) {
                final short turn = bb.getShort();
                for (int eventsSize = bb.getInt(), j = 0; j < eventsSize; ++j) {
                    final int bufferSize = bb.getInt();
                    final byte[] data = new byte[bufferSize];
                    bb.get(data);
                    try {
                        final DelayableTimeEvent timeEvent = DelayableTimeEvent.deserialize(this.m_context, ByteBuffer.wrap(data));
                        timelineEvents.addToEvents(timeEvent, turn, events);
                    }
                    catch (Exception e) {
                        TimelineEventsSerializer.m_logger.error((Object)"Exception levee", (Throwable)e);
                    }
                }
            }
        }
    }
}
