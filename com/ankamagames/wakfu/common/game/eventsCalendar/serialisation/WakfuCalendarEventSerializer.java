package com.ankamagames.wakfu.common.game.eventsCalendar.serialisation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import gnu.trove.*;

public class WakfuCalendarEventSerializer extends BinarSerial implements Poolable
{
    protected static Logger m_logger;
    WakfuCalendarEvent m_event;
    private static WakfuCalendarEventProvider eventProvider;
    private boolean m_isCheckedOut;
    private static final ObjectPool POOL;
    public BinarSerialPart TITLE;
    public BinarSerialPart ID;
    public final BinarSerialPart START_DATE;
    public final BinarSerialPart END_DATE;
    public final BinarSerialPart REGISTERED;
    public final BinarSerialPart PARTICIPANT;
    public final BinarSerialPart DESCRIPTION;
    
    public static void setEventProvider(final WakfuCalendarEventProvider provider) {
        WakfuCalendarEventSerializer.eventProvider = provider;
    }
    
    public static WakfuCalendarEventSerializer checkOut() {
        WakfuCalendarEventSerializer res;
        try {
            res = (WakfuCalendarEventSerializer)WakfuCalendarEventSerializer.POOL.borrowObject();
            res.m_isCheckedOut = true;
        }
        catch (Exception e) {
            res = new WakfuCalendarEventSerializer();
            WakfuCalendarEventSerializer.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type WakfuCalendarEventSerializable : " + e.getMessage()));
        }
        return res;
    }
    
    @Override
    public void onCheckOut() {
        this.m_event = null;
    }
    
    @Override
    public void onCheckIn() {
        this.m_event = null;
    }
    
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                WakfuCalendarEventSerializer.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                WakfuCalendarEventSerializer.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    public WakfuCalendarEventSerializer() {
        super();
        this.m_isCheckedOut = false;
        this.TITLE = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final String title = WakfuCalendarEventSerializer.this.m_event.getTitle();
                final byte[] serialized_title = StringUtils.toUTF8(title);
                if (serialized_title.length > 32) {
                    return;
                }
                buffer.put((byte)serialized_title.length);
                buffer.put(serialized_title);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                if (WakfuCalendarEventSerializer.this.m_event == null) {
                    WakfuCalendarEventSerializer.this.m_event = WakfuCalendarEventSerializer.eventProvider.getNewEventInstance();
                }
                final int title_size = buffer.get();
                final byte[] serialized_title = new byte[title_size];
                buffer.get(serialized_title);
                WakfuCalendarEventSerializer.this.m_event.setTitle(StringUtils.fromUTF8(serialized_title));
            }
            
            @Override
            public int expectedSize() {
                final byte[] title = StringUtils.toUTF8(WakfuCalendarEventSerializer.this.m_event.getTitle());
                final int size = 1 + title.length;
                return size;
            }
        };
        this.ID = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(WakfuCalendarEventSerializer.this.m_event.getUid());
                buffer.putLong(WakfuCalendarEventSerializer.this.m_event.getOwnerId());
                buffer.put(WakfuCalendarEventSerializer.this.m_event.getType());
                buffer.put(WakfuCalendarEventSerializer.this.m_event.getSubType());
                buffer.put((byte)(WakfuCalendarEventSerializer.this.m_event.areTextsUserDefined() ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                if (WakfuCalendarEventSerializer.this.m_event == null) {
                    WakfuCalendarEventSerializer.this.m_event = WakfuCalendarEventSerializer.eventProvider.getNewEventInstance();
                }
                WakfuCalendarEventSerializer.this.m_event.setUid(buffer.getLong());
                WakfuCalendarEventSerializer.this.m_event.setOwnerId(buffer.getLong());
                WakfuCalendarEventSerializer.this.m_event.setType(buffer.get());
                WakfuCalendarEventSerializer.this.m_event.setSubType(buffer.get());
                WakfuCalendarEventSerializer.this.m_event.setTextsUserDefined(buffer.get() == 1);
            }
            
            @Override
            public int expectedSize() {
                return 19;
            }
        };
        this.START_DATE = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final GameDateConst startDate = WakfuCalendarEventSerializer.this.m_event.getStartDate();
                if (startDate != null) {
                    buffer.putLong(startDate.toLong());
                }
                else {
                    buffer.putLong(0L);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                if (WakfuCalendarEventSerializer.this.m_event == null) {
                    WakfuCalendarEventSerializer.this.m_event = WakfuCalendarEventSerializer.eventProvider.getNewEventInstance();
                }
                final long dateInLong = buffer.getLong();
                WakfuCalendarEventSerializer.this.m_event.setStartDate(GameDate.fromLong(dateInLong));
            }
            
            @Override
            public int expectedSize() {
                return 8;
            }
        };
        this.END_DATE = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final GameDateConst endDate = WakfuCalendarEventSerializer.this.m_event.getEndDate();
                if (endDate != null) {
                    buffer.putLong(endDate.toLong());
                }
                else {
                    buffer.putLong(0L);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                if (WakfuCalendarEventSerializer.this.m_event == null) {
                    WakfuCalendarEventSerializer.this.m_event = WakfuCalendarEventSerializer.eventProvider.getNewEventInstance();
                }
                final long dateInLong = buffer.getLong();
                WakfuCalendarEventSerializer.this.m_event.setEndDate(GameDate.fromLong(dateInLong));
            }
            
            @Override
            public int expectedSize() {
                return 8;
            }
        };
        this.REGISTERED = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final TLongObjectHashMap<String> registered = WakfuCalendarEventSerializer.this.m_event.getRegistered();
                buffer.put(WakfuCalendarEventSerializer.this.m_event.getMaxRegistrations());
                buffer.put((byte)registered.size());
                final TLongObjectIterator<String> iterator = registered.iterator();
                while (iterator.hasNext()) {
                    iterator.advance();
                    buffer.putLong(iterator.key());
                    final byte[] nameData = StringUtils.toUTF8(iterator.value());
                    if (nameData.length > 127) {
                        return;
                    }
                    buffer.put((byte)nameData.length);
                    buffer.put(nameData);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                WakfuCalendarEventSerializer.this.m_event.setMaxRegistrations(buffer.get());
                final byte nbRegistrations = buffer.get();
                for (int i = 0; i < nbRegistrations; ++i) {
                    final long id = buffer.getLong();
                    final byte[] nameData = new byte[buffer.get()];
                    buffer.get(nameData);
                    WakfuCalendarEventSerializer.this.m_event.addRegistered(id, StringUtils.fromUTF8(nameData));
                }
            }
            
            @Override
            public int expectedSize() {
                int size = 2;
                final TLongObjectHashMap<String> registered = WakfuCalendarEventSerializer.this.m_event.getRegistered();
                final TLongObjectIterator<String> iterator = registered.iterator();
                while (iterator.hasNext()) {
                    iterator.advance();
                    final byte[] nameData = StringUtils.toUTF8(iterator.value());
                    if (nameData.length > 127) {
                        return size;
                    }
                    size += 9 + nameData.length;
                }
                return size;
            }
        };
        this.PARTICIPANT = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final TLongObjectHashMap<String> participants = WakfuCalendarEventSerializer.this.m_event.getParticipants();
                buffer.put(WakfuCalendarEventSerializer.this.m_event.getMaxParticipants());
                buffer.put((byte)participants.size());
                final TLongObjectIterator<String> iterator = participants.iterator();
                while (iterator.hasNext()) {
                    iterator.advance();
                    buffer.putLong(iterator.key());
                    final byte[] nameData = StringUtils.toUTF8(iterator.value());
                    if (nameData.length > 127) {
                        return;
                    }
                    buffer.put((byte)nameData.length);
                    buffer.put(nameData);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                WakfuCalendarEventSerializer.this.m_event.setMaxParticipants(buffer.get());
                final byte nbRegistrations = buffer.get();
                for (int i = 0; i < nbRegistrations; ++i) {
                    final long id = buffer.getLong();
                    final byte[] nameData = new byte[buffer.get()];
                    buffer.get(nameData);
                    WakfuCalendarEventSerializer.this.m_event.addParticipant(id, StringUtils.fromUTF8(nameData));
                }
            }
            
            @Override
            public int expectedSize() {
                int size = 2;
                final TLongObjectHashMap<String> registered = WakfuCalendarEventSerializer.this.m_event.getParticipants();
                final TLongObjectIterator<String> iterator = registered.iterator();
                while (iterator.hasNext()) {
                    iterator.advance();
                    final byte[] nameData = StringUtils.toUTF8(iterator.value());
                    if (nameData.length > 127) {
                        return size;
                    }
                    size += 9 + nameData.length;
                }
                return size;
            }
        };
        this.DESCRIPTION = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final String description = WakfuCalendarEventSerializer.this.m_event.getDescription();
                final byte[] serialized_description = StringUtils.toUTF8(description);
                buffer.put((byte)serialized_description.length);
                buffer.put(serialized_description);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                if (WakfuCalendarEventSerializer.this.m_event == null) {
                    WakfuCalendarEventSerializer.this.m_event = WakfuCalendarEventSerializer.eventProvider.getNewEventInstance();
                }
                short desc_size = buffer.get();
                if (desc_size < 0) {
                    desc_size += 256;
                }
                final byte[] serialized_desc = new byte[desc_size];
                buffer.get(serialized_desc);
                WakfuCalendarEventSerializer.this.m_event.setDesc(StringUtils.fromUTF8(serialized_desc));
            }
            
            @Override
            public int expectedSize() {
                int size = 0;
                final byte[] desc = StringUtils.toUTF8(WakfuCalendarEventSerializer.this.m_event.getDescription());
                size += 1 + desc.length;
                return size;
            }
        };
    }
    
    public void setEventForSerialisation(final WakfuCalendarEvent event) {
        this.m_event = event;
    }
    
    public void setNewEventInstance() {
        this.m_event = WakfuCalendarEventSerializer.eventProvider.getNewEventInstance();
    }
    
    public void freeEvent() {
        WakfuCalendarEventSerializer.eventProvider.freeEventInstance(this.m_event);
    }
    
    public WakfuCalendarEvent getEvent() {
        return this.m_event;
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.ID, this.TITLE, this.DESCRIPTION, this.START_DATE, this.END_DATE, this.PARTICIPANT, this.REGISTERED };
    }
    
    public void serialize(final ByteBuffer buf) {
        final BinarSerialPart[] parts = this.partsEnumeration();
        for (int i = 0; i < parts.length; ++i) {
            final BinarSerialPart part = parts[i];
            part.serialize(buf);
        }
    }
    
    public void unserialize(final ByteBuffer buf) {
        final BinarSerialPart[] parts = this.partsEnumeration();
        for (int i = 0; i < parts.length; ++i) {
            final BinarSerialPart part = parts[i];
            part.unserialize(buf, Version.SERIALIZATION_VERSION);
        }
    }
    
    public void serializeDescription(final ByteBuffer buf) {
        this.DESCRIPTION.serialize(buf);
    }
    
    public void unserializeDescription(final ByteBuffer buf) {
        this.DESCRIPTION.unserialize(buf, Version.SERIALIZATION_VERSION);
    }
    
    public void serializeForIdentification(final ByteBuffer buf) {
        this.ID.serialize(buf);
        this.TITLE.serialize(buf);
    }
    
    public void unserializeForIdentification(final ByteBuffer buf) {
        this.ID.unserialize(buf, Version.SERIALIZATION_VERSION);
        this.TITLE.unserialize(buf, Version.SERIALIZATION_VERSION);
    }
    
    public int expectedSize() {
        final BinarSerialPart[] parts = this.partsEnumeration();
        int size = 0;
        for (int i = 0; i < parts.length; ++i) {
            final BinarSerialPart part = parts[i];
            size += part.expectedSize();
        }
        return size;
    }
    
    static {
        WakfuCalendarEventSerializer.m_logger = Logger.getLogger((Class)WakfuCalendarEventSerializer.class);
        POOL = new MonitoredPool(new ObjectFactory<WakfuCalendarEventSerializer>() {
            @Override
            public WakfuCalendarEventSerializer makeObject() {
                return new WakfuCalendarEventSerializer();
            }
        });
    }
}
