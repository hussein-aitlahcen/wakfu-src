package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawPet implements VersionableObject
{
    public int definitionId;
    public String name;
    public int colorItemRefId;
    public int equippedRefItemId;
    public int health;
    public int xp;
    public byte fightCounter;
    public long fightCounterStartDate;
    public long lastMealDate;
    public long lastHungryDate;
    public int sleepRefItemId;
    public long sleepDate;
    
    public RawPet() {
        super();
        this.definitionId = 0;
        this.name = null;
        this.colorItemRefId = 0;
        this.equippedRefItemId = 0;
        this.health = 0;
        this.xp = 0;
        this.fightCounter = 0;
        this.fightCounterStartDate = 0L;
        this.lastMealDate = 0L;
        this.lastHungryDate = 0L;
        this.sleepRefItemId = 0;
        this.sleepDate = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.definitionId);
        if (this.name != null) {
            final byte[] serialized_name = StringUtils.toUTF8(this.name);
            if (serialized_name.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_name.length);
            buffer.put(serialized_name);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putInt(this.colorItemRefId);
        buffer.putInt(this.equippedRefItemId);
        buffer.putInt(this.health);
        buffer.putInt(this.xp);
        buffer.put(this.fightCounter);
        buffer.putLong(this.fightCounterStartDate);
        buffer.putLong(this.lastMealDate);
        buffer.putLong(this.lastHungryDate);
        buffer.putInt(this.sleepRefItemId);
        buffer.putLong(this.sleepDate);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.definitionId = buffer.getInt();
        final int name_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_name = new byte[name_size];
        buffer.get(serialized_name);
        this.name = StringUtils.fromUTF8(serialized_name);
        this.colorItemRefId = buffer.getInt();
        this.equippedRefItemId = buffer.getInt();
        this.health = buffer.getInt();
        this.xp = buffer.getInt();
        this.fightCounter = buffer.get();
        this.fightCounterStartDate = buffer.getLong();
        this.lastMealDate = buffer.getLong();
        this.lastHungryDate = buffer.getLong();
        this.sleepRefItemId = buffer.getInt();
        this.sleepDate = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.definitionId = 0;
        this.name = null;
        this.colorItemRefId = 0;
        this.equippedRefItemId = 0;
        this.health = 0;
        this.xp = 0;
        this.fightCounter = 0;
        this.fightCounterStartDate = 0L;
        this.lastMealDate = 0L;
        this.lastHungryDate = 0L;
        this.sleepRefItemId = 0;
        this.sleepDate = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10035007) {
            return this.unserialize(buffer);
        }
        final RawPetConverter converter = new RawPetConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 4;
        size += 2;
        size += ((this.name != null) ? StringUtils.toUTF8(this.name).length : 0);
        size += 4;
        size += 4;
        size += 4;
        size += 4;
        ++size;
        size += 8;
        size += 8;
        size += 8;
        size += 4;
        size += 8;
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("definitionId=").append(this.definitionId).append('\n');
        repr.append(prefix).append("name=").append(this.name).append('\n');
        repr.append(prefix).append("colorItemRefId=").append(this.colorItemRefId).append('\n');
        repr.append(prefix).append("equippedRefItemId=").append(this.equippedRefItemId).append('\n');
        repr.append(prefix).append("health=").append(this.health).append('\n');
        repr.append(prefix).append("xp=").append(this.xp).append('\n');
        repr.append(prefix).append("fightCounter=").append(this.fightCounter).append('\n');
        repr.append(prefix).append("fightCounterStartDate=").append(this.fightCounterStartDate).append('\n');
        repr.append(prefix).append("lastMealDate=").append(this.lastMealDate).append('\n');
        repr.append(prefix).append("lastHungryDate=").append(this.lastHungryDate).append('\n');
        repr.append(prefix).append("sleepRefItemId=").append(this.sleepRefItemId).append('\n');
        repr.append(prefix).append("sleepDate=").append(this.sleepDate).append('\n');
    }
    
    private final class RawPetConverter
    {
        private int definitionId;
        private String name;
        private int colorItemRefId;
        private int equippedRefItemId;
        private int health;
        private int xp;
        private byte fightCounter;
        private long fightCounterStartDate;
        private int lastMeal;
        private long lastMealDate;
        private long lastHungryDate;
        private int sleepRefItemId;
        private long sleepDate;
        private int reskinItemRefId;
        
        private RawPetConverter() {
            super();
            this.definitionId = 0;
            this.name = null;
            this.colorItemRefId = 0;
            this.equippedRefItemId = 0;
            this.health = 0;
            this.xp = 0;
            this.fightCounter = 0;
            this.fightCounterStartDate = 0L;
            this.lastMeal = 0;
            this.lastMealDate = 0L;
            this.lastHungryDate = 0L;
            this.sleepRefItemId = 0;
            this.sleepDate = 0L;
            this.reskinItemRefId = 0;
        }
        
        public void pushResult() {
            RawPet.this.definitionId = this.definitionId;
            RawPet.this.name = this.name;
            RawPet.this.colorItemRefId = this.colorItemRefId;
            RawPet.this.equippedRefItemId = this.equippedRefItemId;
            RawPet.this.health = this.health;
            RawPet.this.xp = this.xp;
            RawPet.this.fightCounter = this.fightCounter;
            RawPet.this.fightCounterStartDate = this.fightCounterStartDate;
            RawPet.this.lastMealDate = this.lastMealDate;
            RawPet.this.lastHungryDate = this.lastHungryDate;
            RawPet.this.sleepRefItemId = this.sleepRefItemId;
            RawPet.this.sleepDate = this.sleepDate;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.definitionId = buffer.getInt();
            final int name_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_name = new byte[name_size];
            buffer.get(serialized_name);
            this.name = StringUtils.fromUTF8(serialized_name);
            this.colorItemRefId = buffer.getInt();
            this.equippedRefItemId = buffer.getInt();
            this.health = buffer.getInt();
            this.xp = buffer.getInt();
            this.fightCounter = buffer.get();
            this.fightCounterStartDate = buffer.getLong();
            this.lastMeal = buffer.getInt();
            this.lastMealDate = buffer.getLong();
            this.lastHungryDate = buffer.getLong();
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.definitionId = buffer.getInt();
            final int name_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_name = new byte[name_size];
            buffer.get(serialized_name);
            this.name = StringUtils.fromUTF8(serialized_name);
            this.colorItemRefId = buffer.getInt();
            this.equippedRefItemId = buffer.getInt();
            this.health = buffer.getInt();
            this.xp = buffer.getInt();
            this.fightCounter = buffer.get();
            this.fightCounterStartDate = buffer.getLong();
            this.lastMealDate = buffer.getLong();
            this.lastHungryDate = buffer.getLong();
            this.sleepRefItemId = buffer.getInt();
            this.sleepDate = buffer.getLong();
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.definitionId = buffer.getInt();
            final int name_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_name = new byte[name_size];
            buffer.get(serialized_name);
            this.name = StringUtils.fromUTF8(serialized_name);
            this.colorItemRefId = buffer.getInt();
            this.equippedRefItemId = buffer.getInt();
            this.health = buffer.getInt();
            this.xp = buffer.getInt();
            this.fightCounter = buffer.get();
            this.fightCounterStartDate = buffer.getLong();
            this.lastMealDate = buffer.getLong();
            this.lastHungryDate = buffer.getLong();
            this.sleepRefItemId = buffer.getInt();
            this.sleepDate = buffer.getLong();
            this.reskinItemRefId = buffer.getInt();
            return true;
        }
        
        public void convert_v313_to_v315() {
        }
        
        public void convert_v315_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035007() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 313) {
                return false;
            }
            if (version < 315) {
                final boolean ok = this.unserialize_v313(buffer);
                if (ok) {
                    this.convert_v313_to_v315();
                    this.convert_v315_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
            else if (version < 10035004) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10035007) {
                    return false;
                }
                final boolean ok = this.unserialize_v10035004(buffer);
                if (ok) {
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
        }
    }
}
