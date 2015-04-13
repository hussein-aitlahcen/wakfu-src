package com.ankamagames.wakfu.common.rawData;

import java.nio.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public class RawInventoryItem implements VersionableObject
{
    public long uniqueId;
    public int refId;
    public short quantity;
    public Timestamp timestamp;
    public Pet pet;
    public Xp xp;
    public Gems gems;
    public RentInfo rentInfo;
    public CompanionInfo companionInfo;
    public Bind bind;
    public Elements elements;
    public MergedItems mergedItems;
    
    public RawInventoryItem() {
        super();
        this.uniqueId = 0L;
        this.refId = 0;
        this.quantity = 0;
        this.timestamp = null;
        this.pet = null;
        this.xp = null;
        this.gems = null;
        this.rentInfo = null;
        this.companionInfo = null;
        this.bind = null;
        this.elements = null;
        this.mergedItems = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.uniqueId);
        buffer.putInt(this.refId);
        buffer.putShort(this.quantity);
        if (this.timestamp != null) {
            buffer.put((byte)1);
            final boolean timestamp_ok = this.timestamp.serialize(buffer);
            if (!timestamp_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.pet != null) {
            buffer.put((byte)1);
            final boolean pet_ok = this.pet.serialize(buffer);
            if (!pet_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.xp != null) {
            buffer.put((byte)1);
            final boolean xp_ok = this.xp.serialize(buffer);
            if (!xp_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.gems != null) {
            buffer.put((byte)1);
            final boolean gems_ok = this.gems.serialize(buffer);
            if (!gems_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.rentInfo != null) {
            buffer.put((byte)1);
            final boolean rentInfo_ok = this.rentInfo.serialize(buffer);
            if (!rentInfo_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.companionInfo != null) {
            buffer.put((byte)1);
            final boolean companionInfo_ok = this.companionInfo.serialize(buffer);
            if (!companionInfo_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.bind != null) {
            buffer.put((byte)1);
            final boolean bind_ok = this.bind.serialize(buffer);
            if (!bind_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.elements != null) {
            buffer.put((byte)1);
            final boolean elements_ok = this.elements.serialize(buffer);
            if (!elements_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.mergedItems != null) {
            buffer.put((byte)1);
            final boolean mergedItems_ok = this.mergedItems.serialize(buffer);
            if (!mergedItems_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.uniqueId = buffer.getLong();
        this.refId = buffer.getInt();
        this.quantity = buffer.getShort();
        final boolean timestamp_present = buffer.get() == 1;
        if (timestamp_present) {
            this.timestamp = new Timestamp();
            final boolean timestamp_ok = this.timestamp.unserialize(buffer);
            if (!timestamp_ok) {
                return false;
            }
        }
        else {
            this.timestamp = null;
        }
        final boolean pet_present = buffer.get() == 1;
        if (pet_present) {
            this.pet = new Pet();
            final boolean pet_ok = this.pet.unserialize(buffer);
            if (!pet_ok) {
                return false;
            }
        }
        else {
            this.pet = null;
        }
        final boolean xp_present = buffer.get() == 1;
        if (xp_present) {
            this.xp = new Xp();
            final boolean xp_ok = this.xp.unserialize(buffer);
            if (!xp_ok) {
                return false;
            }
        }
        else {
            this.xp = null;
        }
        final boolean gems_present = buffer.get() == 1;
        if (gems_present) {
            this.gems = new Gems();
            final boolean gems_ok = this.gems.unserialize(buffer);
            if (!gems_ok) {
                return false;
            }
        }
        else {
            this.gems = null;
        }
        final boolean rentInfo_present = buffer.get() == 1;
        if (rentInfo_present) {
            this.rentInfo = new RentInfo();
            final boolean rentInfo_ok = this.rentInfo.unserialize(buffer);
            if (!rentInfo_ok) {
                return false;
            }
        }
        else {
            this.rentInfo = null;
        }
        final boolean companionInfo_present = buffer.get() == 1;
        if (companionInfo_present) {
            this.companionInfo = new CompanionInfo();
            final boolean companionInfo_ok = this.companionInfo.unserialize(buffer);
            if (!companionInfo_ok) {
                return false;
            }
        }
        else {
            this.companionInfo = null;
        }
        final boolean bind_present = buffer.get() == 1;
        if (bind_present) {
            this.bind = new Bind();
            final boolean bind_ok = this.bind.unserialize(buffer);
            if (!bind_ok) {
                return false;
            }
        }
        else {
            this.bind = null;
        }
        final boolean elements_present = buffer.get() == 1;
        if (elements_present) {
            this.elements = new Elements();
            final boolean elements_ok = this.elements.unserialize(buffer);
            if (!elements_ok) {
                return false;
            }
        }
        else {
            this.elements = null;
        }
        final boolean mergedItems_present = buffer.get() == 1;
        if (mergedItems_present) {
            this.mergedItems = new MergedItems();
            final boolean mergedItems_ok = this.mergedItems.unserialize(buffer);
            if (!mergedItems_ok) {
                return false;
            }
        }
        else {
            this.mergedItems = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.uniqueId = 0L;
        this.refId = 0;
        this.quantity = 0;
        this.timestamp = null;
        this.pet = null;
        this.xp = null;
        this.gems = null;
        this.rentInfo = null;
        this.companionInfo = null;
        this.bind = null;
        this.elements = null;
        this.mergedItems = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawInventoryItemConverter converter = new RawInventoryItemConverter();
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
        size += 8;
        size += 4;
        size += 2;
        ++size;
        if (this.timestamp != null) {
            size += this.timestamp.serializedSize();
        }
        ++size;
        if (this.pet != null) {
            size += this.pet.serializedSize();
        }
        ++size;
        if (this.xp != null) {
            size += this.xp.serializedSize();
        }
        ++size;
        if (this.gems != null) {
            size += this.gems.serializedSize();
        }
        ++size;
        if (this.rentInfo != null) {
            size += this.rentInfo.serializedSize();
        }
        ++size;
        if (this.companionInfo != null) {
            size += this.companionInfo.serializedSize();
        }
        ++size;
        if (this.bind != null) {
            size += this.bind.serializedSize();
        }
        ++size;
        if (this.elements != null) {
            size += this.elements.serializedSize();
        }
        ++size;
        if (this.mergedItems != null) {
            size += this.mergedItems.serializedSize();
        }
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("uniqueId=").append(this.uniqueId).append('\n');
        repr.append(prefix).append("refId=").append(this.refId).append('\n');
        repr.append(prefix).append("quantity=").append(this.quantity).append('\n');
        repr.append(prefix).append("timestamp=");
        if (this.timestamp == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.timestamp.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("pet=");
        if (this.pet == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.pet.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("xp=");
        if (this.xp == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.xp.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("gems=");
        if (this.gems == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.gems.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("rentInfo=");
        if (this.rentInfo == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.rentInfo.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("companionInfo=");
        if (this.companionInfo == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.companionInfo.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("bind=");
        if (this.bind == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.bind.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("elements=");
        if (this.elements == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.elements.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("mergedItems=");
        if (this.mergedItems == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.mergedItems.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Timestamp implements VersionableObject
    {
        public long timestampValue;
        public static final int SERIALIZED_SIZE = 8;
        
        public Timestamp() {
            super();
            this.timestampValue = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putLong(this.timestampValue);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.timestampValue = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.timestampValue = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 8;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("timestampValue=").append(this.timestampValue).append('\n');
        }
    }
    
    public static class Pet implements VersionableObject
    {
        public final RawPet rawPet;
        
        public Pet() {
            super();
            this.rawPet = new RawPet();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawPet_ok = this.rawPet.serialize(buffer);
            return rawPet_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawPet_ok = this.rawPet.unserialize(buffer);
            return rawPet_ok;
        }
        
        @Override
        public void clear() {
            this.rawPet.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10035007) {
                return this.unserialize(buffer);
            }
            final PetConverter converter = new PetConverter();
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
            size += this.rawPet.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawPet=...\n");
            this.rawPet.internalToString(repr, prefix + "  ");
        }
        
        private final class PetConverter
        {
            private final RawPet rawPet;
            
            private PetConverter() {
                super();
                this.rawPet = new RawPet();
            }
            
            public void pushResult() {
                Pet.this.rawPet.definitionId = this.rawPet.definitionId;
                Pet.this.rawPet.name = this.rawPet.name;
                Pet.this.rawPet.colorItemRefId = this.rawPet.colorItemRefId;
                Pet.this.rawPet.equippedRefItemId = this.rawPet.equippedRefItemId;
                Pet.this.rawPet.health = this.rawPet.health;
                Pet.this.rawPet.xp = this.rawPet.xp;
                Pet.this.rawPet.fightCounter = this.rawPet.fightCounter;
                Pet.this.rawPet.fightCounterStartDate = this.rawPet.fightCounterStartDate;
                Pet.this.rawPet.lastMealDate = this.rawPet.lastMealDate;
                Pet.this.rawPet.lastHungryDate = this.rawPet.lastHungryDate;
                Pet.this.rawPet.sleepRefItemId = this.rawPet.sleepRefItemId;
                Pet.this.rawPet.sleepDate = this.rawPet.sleepDate;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final boolean rawPet_ok = this.rawPet.unserializeVersion(buffer, 313);
                return rawPet_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final boolean rawPet_ok = this.rawPet.unserializeVersion(buffer, 315);
                return rawPet_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean rawPet_ok = this.rawPet.unserializeVersion(buffer, 10035004);
                return rawPet_ok;
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
    
    public static class Xp implements VersionableObject
    {
        public final RawItemXp rawXp;
        
        public Xp() {
            super();
            this.rawXp = new RawItemXp();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawXp_ok = this.rawXp.serialize(buffer);
            return rawXp_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawXp_ok = this.rawXp.unserialize(buffer);
            return rawXp_ok;
        }
        
        @Override
        public void clear() {
            this.rawXp.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.rawXp.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawXp=...\n");
            this.rawXp.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class GemsOld implements VersionableObject
    {
        public static final int SERIALIZED_SIZE = 0;
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            return true;
        }
        
        @Override
        public void clear() {
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10023) {
                return this.unserialize(buffer);
            }
            final GemsOldConverter converter = new GemsOldConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 0;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
        }
        
        private final class GemsOldConverter
        {
            private final RawGems rawGems;
            
            private GemsOldConverter() {
                super();
                this.rawGems = new RawGems();
            }
            
            public void pushResult() {
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final boolean rawGems_ok = this.rawGems.unserializeVersion(buffer, 10003);
                return rawGems_ok;
            }
            
            public void convert_v1_to_v10003() {
            }
            
            public void convert_v10003_to_v10023() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version < 10003) {
                    final boolean ok = this.unserialize_v1(buffer);
                    if (ok) {
                        this.convert_v1_to_v10003();
                        this.convert_v10003_to_v10023();
                        return true;
                    }
                    return false;
                }
                else {
                    if (version >= 10023) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v10003(buffer);
                    if (ok) {
                        this.convert_v10003_to_v10023();
                        return true;
                    }
                    return false;
                }
            }
        }
    }
    
    public static class Gems implements VersionableObject
    {
        public final RawGems rawGems;
        
        public Gems() {
            super();
            this.rawGems = new RawGems();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawGems_ok = this.rawGems.serialize(buffer);
            return rawGems_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawGems_ok = this.rawGems.unserialize(buffer);
            return rawGems_ok;
        }
        
        @Override
        public void clear() {
            this.rawGems.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10023) {
                return this.unserialize(buffer);
            }
            final GemsConverter converter = new GemsConverter();
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
            size += this.rawGems.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawGems=...\n");
            this.rawGems.internalToString(repr, prefix + "  ");
        }
        
        private final class GemsConverter
        {
            private final RawGems rawGems;
            
            private GemsConverter() {
                super();
                this.rawGems = new RawGems();
            }
            
            public void pushResult() {
                Gems.this.rawGems.gems.clear();
                Gems.this.rawGems.gems.ensureCapacity(this.rawGems.gems.size());
                Gems.this.rawGems.gems.addAll(this.rawGems.gems);
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v1_to_v10023() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10023) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10023();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class RentInfo implements VersionableObject
    {
        public final RawRentInfo rawRentInfo;
        
        public RentInfo() {
            super();
            this.rawRentInfo = new RawRentInfo();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawRentInfo_ok = this.rawRentInfo.serialize(buffer);
            return rawRentInfo_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawRentInfo_ok = this.rawRentInfo.unserialize(buffer);
            return rawRentInfo_ok;
        }
        
        @Override
        public void clear() {
            this.rawRentInfo.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10028000) {
                return this.unserialize(buffer);
            }
            final RentInfoConverter converter = new RentInfoConverter();
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
            size += this.rawRentInfo.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawRentInfo=...\n");
            this.rawRentInfo.internalToString(repr, prefix + "  ");
        }
        
        private final class RentInfoConverter
        {
            private final RawRentInfo rawRentInfo;
            
            private RentInfoConverter() {
                super();
                this.rawRentInfo = new RawRentInfo();
            }
            
            public void pushResult() {
                RentInfo.this.rawRentInfo.type = this.rawRentInfo.type;
                RentInfo.this.rawRentInfo.duration = this.rawRentInfo.duration;
                RentInfo.this.rawRentInfo.count = this.rawRentInfo.count;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v1_to_v10028000() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10028000) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10028000();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class CompanionInfo implements VersionableObject
    {
        public final RawCompanionInfo rawCompanionInfo;
        
        public CompanionInfo() {
            super();
            this.rawCompanionInfo = new RawCompanionInfo();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawCompanionInfo_ok = this.rawCompanionInfo.serialize(buffer);
            return rawCompanionInfo_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawCompanionInfo_ok = this.rawCompanionInfo.unserialize(buffer);
            return rawCompanionInfo_ok;
        }
        
        @Override
        public void clear() {
            this.rawCompanionInfo.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10029000) {
                return this.unserialize(buffer);
            }
            final CompanionInfoConverter converter = new CompanionInfoConverter();
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
            size += this.rawCompanionInfo.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawCompanionInfo=...\n");
            this.rawCompanionInfo.internalToString(repr, prefix + "  ");
        }
        
        private final class CompanionInfoConverter
        {
            private final RawCompanionInfo rawCompanionInfo;
            
            private CompanionInfoConverter() {
                super();
                this.rawCompanionInfo = new RawCompanionInfo();
            }
            
            public void pushResult() {
                CompanionInfo.this.rawCompanionInfo.xp = this.rawCompanionInfo.xp;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v1_to_v10029000() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10029000) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10029000();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class Bind implements VersionableObject
    {
        public final RawItemBind rawItemBind;
        
        public Bind() {
            super();
            this.rawItemBind = new RawItemBind();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawItemBind_ok = this.rawItemBind.serialize(buffer);
            return rawItemBind_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawItemBind_ok = this.rawItemBind.unserialize(buffer);
            return rawItemBind_ok;
        }
        
        @Override
        public void clear() {
            this.rawItemBind.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10032003) {
                return this.unserialize(buffer);
            }
            final BindConverter converter = new BindConverter();
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
            size += this.rawItemBind.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawItemBind=...\n");
            this.rawItemBind.internalToString(repr, prefix + "  ");
        }
        
        private final class BindConverter
        {
            private final RawItemBind rawItemBind;
            
            private BindConverter() {
                super();
                this.rawItemBind = new RawItemBind();
            }
            
            public void pushResult() {
                Bind.this.rawItemBind.type = this.rawItemBind.type;
                Bind.this.rawItemBind.data = this.rawItemBind.data;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v1_to_v10032003() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10032003) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10032003();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class Elements implements VersionableObject
    {
        public final RawItemElements rawItemElements;
        
        public Elements() {
            super();
            this.rawItemElements = new RawItemElements();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawItemElements_ok = this.rawItemElements.serialize(buffer);
            return rawItemElements_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawItemElements_ok = this.rawItemElements.unserialize(buffer);
            return rawItemElements_ok;
        }
        
        @Override
        public void clear() {
            this.rawItemElements.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10036004) {
                return this.unserialize(buffer);
            }
            final ElementsConverter converter = new ElementsConverter();
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
            size += this.rawItemElements.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawItemElements=...\n");
            this.rawItemElements.internalToString(repr, prefix + "  ");
        }
        
        private final class ElementsConverter
        {
            private final RawItemElements rawItemElements;
            
            private ElementsConverter() {
                super();
                this.rawItemElements = new RawItemElements();
            }
            
            public void pushResult() {
                Elements.this.rawItemElements.damageElements = this.rawItemElements.damageElements;
                Elements.this.rawItemElements.resistanceElements = this.rawItemElements.resistanceElements;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v1_to_v10036004() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10036004) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10036004();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class MergedItems implements VersionableObject
    {
        public final RawMergedItems rawMergedItems;
        
        public MergedItems() {
            super();
            this.rawMergedItems = new RawMergedItems();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawMergedItems_ok = this.rawMergedItems.serialize(buffer);
            return rawMergedItems_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawMergedItems_ok = this.rawMergedItems.unserialize(buffer);
            return rawMergedItems_ok;
        }
        
        @Override
        public void clear() {
            this.rawMergedItems.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037001) {
                return this.unserialize(buffer);
            }
            final MergedItemsConverter converter = new MergedItemsConverter();
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
            size += this.rawMergedItems.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawMergedItems=...\n");
            this.rawMergedItems.internalToString(repr, prefix + "  ");
        }
        
        private final class MergedItemsConverter
        {
            private final RawMergedItems rawMergedItems;
            
            private MergedItemsConverter() {
                super();
                this.rawMergedItems = new RawMergedItems();
            }
            
            public void pushResult() {
                MergedItems.this.rawMergedItems.version = this.rawMergedItems.version;
                MergedItems.this.rawMergedItems.items = this.rawMergedItems.items;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v1_to_v10037001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10037001) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10037001();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class RawInventoryItemConverter
    {
        private long uniqueId;
        private int refId;
        private short quantity;
        private Timestamp timestamp;
        private Pet pet;
        private Xp xp;
        private GemsOld gemsOld;
        private Gems gems;
        private RentInfo rentInfo;
        private CompanionInfo companionInfo;
        private Bind bind;
        private Elements elements;
        private MergedItems mergedItems;
        
        private RawInventoryItemConverter() {
            super();
            this.uniqueId = 0L;
            this.refId = 0;
            this.quantity = 0;
            this.timestamp = null;
            this.pet = null;
            this.xp = null;
            this.gemsOld = null;
            this.gems = null;
            this.rentInfo = null;
            this.companionInfo = null;
            this.bind = null;
            this.elements = null;
            this.mergedItems = null;
        }
        
        public void pushResult() {
            RawInventoryItem.this.uniqueId = this.uniqueId;
            RawInventoryItem.this.refId = this.refId;
            RawInventoryItem.this.quantity = this.quantity;
            RawInventoryItem.this.timestamp = this.timestamp;
            RawInventoryItem.this.pet = this.pet;
            RawInventoryItem.this.xp = this.xp;
            RawInventoryItem.this.gems = this.gems;
            RawInventoryItem.this.rentInfo = this.rentInfo;
            RawInventoryItem.this.companionInfo = this.companionInfo;
            RawInventoryItem.this.bind = this.bind;
            RawInventoryItem.this.elements = this.elements;
            RawInventoryItem.this.mergedItems = this.mergedItems;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 1);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 313);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 313);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 314);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 314);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 314);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 315);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 315);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 315);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10003);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10003);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10003);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gemsOld_present = buffer.get() == 1;
            if (gemsOld_present) {
                this.gemsOld = new GemsOld();
                final boolean gemsOld_ok = this.gemsOld.unserializeVersion(buffer, 10003);
                if (!gemsOld_ok) {
                    return false;
                }
            }
            else {
                this.gemsOld = null;
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10023);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10023);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10023);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gems_present = buffer.get() == 1;
            if (gems_present) {
                this.gems = new Gems();
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10023);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10028000);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10028000);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10028000);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gems_present = buffer.get() == 1;
            if (gems_present) {
                this.gems = new Gems();
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10028000);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            final boolean rentInfo_present = buffer.get() == 1;
            if (rentInfo_present) {
                this.rentInfo = new RentInfo();
                final boolean rentInfo_ok = this.rentInfo.unserializeVersion(buffer, 10028000);
                if (!rentInfo_ok) {
                    return false;
                }
            }
            else {
                this.rentInfo = null;
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10029000);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10029000);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10029000);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gems_present = buffer.get() == 1;
            if (gems_present) {
                this.gems = new Gems();
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10029000);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            final boolean rentInfo_present = buffer.get() == 1;
            if (rentInfo_present) {
                this.rentInfo = new RentInfo();
                final boolean rentInfo_ok = this.rentInfo.unserializeVersion(buffer, 10029000);
                if (!rentInfo_ok) {
                    return false;
                }
            }
            else {
                this.rentInfo = null;
            }
            final boolean companionInfo_present = buffer.get() == 1;
            if (companionInfo_present) {
                this.companionInfo = new CompanionInfo();
                final boolean companionInfo_ok = this.companionInfo.unserializeVersion(buffer, 10029000);
                if (!companionInfo_ok) {
                    return false;
                }
            }
            else {
                this.companionInfo = null;
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10032003);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10032003);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10032003);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gems_present = buffer.get() == 1;
            if (gems_present) {
                this.gems = new Gems();
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10032003);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            final boolean rentInfo_present = buffer.get() == 1;
            if (rentInfo_present) {
                this.rentInfo = new RentInfo();
                final boolean rentInfo_ok = this.rentInfo.unserializeVersion(buffer, 10032003);
                if (!rentInfo_ok) {
                    return false;
                }
            }
            else {
                this.rentInfo = null;
            }
            final boolean companionInfo_present = buffer.get() == 1;
            if (companionInfo_present) {
                this.companionInfo = new CompanionInfo();
                final boolean companionInfo_ok = this.companionInfo.unserializeVersion(buffer, 10032003);
                if (!companionInfo_ok) {
                    return false;
                }
            }
            else {
                this.companionInfo = null;
            }
            final boolean bind_present = buffer.get() == 1;
            if (bind_present) {
                this.bind = new Bind();
                final boolean bind_ok = this.bind.unserializeVersion(buffer, 10032003);
                if (!bind_ok) {
                    return false;
                }
            }
            else {
                this.bind = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10035004);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10035004);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10035004);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gems_present = buffer.get() == 1;
            if (gems_present) {
                this.gems = new Gems();
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10035004);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            final boolean rentInfo_present = buffer.get() == 1;
            if (rentInfo_present) {
                this.rentInfo = new RentInfo();
                final boolean rentInfo_ok = this.rentInfo.unserializeVersion(buffer, 10035004);
                if (!rentInfo_ok) {
                    return false;
                }
            }
            else {
                this.rentInfo = null;
            }
            final boolean companionInfo_present = buffer.get() == 1;
            if (companionInfo_present) {
                this.companionInfo = new CompanionInfo();
                final boolean companionInfo_ok = this.companionInfo.unserializeVersion(buffer, 10035004);
                if (!companionInfo_ok) {
                    return false;
                }
            }
            else {
                this.companionInfo = null;
            }
            final boolean bind_present = buffer.get() == 1;
            if (bind_present) {
                this.bind = new Bind();
                final boolean bind_ok = this.bind.unserializeVersion(buffer, 10035004);
                if (!bind_ok) {
                    return false;
                }
            }
            else {
                this.bind = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10035007);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10035007);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10035007);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gems_present = buffer.get() == 1;
            if (gems_present) {
                this.gems = new Gems();
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10035007);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            final boolean rentInfo_present = buffer.get() == 1;
            if (rentInfo_present) {
                this.rentInfo = new RentInfo();
                final boolean rentInfo_ok = this.rentInfo.unserializeVersion(buffer, 10035007);
                if (!rentInfo_ok) {
                    return false;
                }
            }
            else {
                this.rentInfo = null;
            }
            final boolean companionInfo_present = buffer.get() == 1;
            if (companionInfo_present) {
                this.companionInfo = new CompanionInfo();
                final boolean companionInfo_ok = this.companionInfo.unserializeVersion(buffer, 10035007);
                if (!companionInfo_ok) {
                    return false;
                }
            }
            else {
                this.companionInfo = null;
            }
            final boolean bind_present = buffer.get() == 1;
            if (bind_present) {
                this.bind = new Bind();
                final boolean bind_ok = this.bind.unserializeVersion(buffer, 10035007);
                if (!bind_ok) {
                    return false;
                }
            }
            else {
                this.bind = null;
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            final boolean timestamp_present = buffer.get() == 1;
            if (timestamp_present) {
                this.timestamp = new Timestamp();
                final boolean timestamp_ok = this.timestamp.unserializeVersion(buffer, 10036004);
                if (!timestamp_ok) {
                    return false;
                }
            }
            else {
                this.timestamp = null;
            }
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10036004);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            final boolean xp_present = buffer.get() == 1;
            if (xp_present) {
                this.xp = new Xp();
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10036004);
                if (!xp_ok) {
                    return false;
                }
            }
            else {
                this.xp = null;
            }
            final boolean gems_present = buffer.get() == 1;
            if (gems_present) {
                this.gems = new Gems();
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10036004);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            final boolean rentInfo_present = buffer.get() == 1;
            if (rentInfo_present) {
                this.rentInfo = new RentInfo();
                final boolean rentInfo_ok = this.rentInfo.unserializeVersion(buffer, 10036004);
                if (!rentInfo_ok) {
                    return false;
                }
            }
            else {
                this.rentInfo = null;
            }
            final boolean companionInfo_present = buffer.get() == 1;
            if (companionInfo_present) {
                this.companionInfo = new CompanionInfo();
                final boolean companionInfo_ok = this.companionInfo.unserializeVersion(buffer, 10036004);
                if (!companionInfo_ok) {
                    return false;
                }
            }
            else {
                this.companionInfo = null;
            }
            final boolean bind_present = buffer.get() == 1;
            if (bind_present) {
                this.bind = new Bind();
                final boolean bind_ok = this.bind.unserializeVersion(buffer, 10036004);
                if (!bind_ok) {
                    return false;
                }
            }
            else {
                this.bind = null;
            }
            final boolean elements_present = buffer.get() == 1;
            if (elements_present) {
                this.elements = new Elements();
                final boolean elements_ok = this.elements.unserializeVersion(buffer, 10036004);
                if (!elements_ok) {
                    return false;
                }
            }
            else {
                this.elements = null;
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v313() {
        }
        
        public void convert_v313_to_v314() {
        }
        
        public void convert_v314_to_v315() {
        }
        
        public void convert_v315_to_v10003() {
        }
        
        public void convert_v10003_to_v10023() {
        }
        
        public void convert_v10023_to_v10028000() {
        }
        
        public void convert_v10028000_to_v10029000() {
        }
        
        public void convert_v10029000_to_v10032003() {
        }
        
        public void convert_v10032003_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035007() {
        }
        
        public void convert_v10035007_to_v10036004() {
        }
        
        public void convert_v10036004_to_v10037001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 313) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 314) {
                final boolean ok = this.unserialize_v313(buffer);
                if (ok) {
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 315) {
                final boolean ok = this.unserialize_v314(buffer);
                if (ok) {
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10003) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10023) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10028000) {
                final boolean ok = this.unserialize_v10023(buffer);
                if (ok) {
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10029000) {
                final boolean ok = this.unserialize_v10028000(buffer);
                if (ok) {
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032003) {
                final boolean ok = this.unserialize_v10029000(buffer);
                if (ok) {
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035004) {
                final boolean ok = this.unserialize_v10032003(buffer);
                if (ok) {
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035007) {
                final boolean ok = this.unserialize_v10035004(buffer);
                if (ok) {
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10036004) {
                final boolean ok = this.unserialize_v10035007(buffer);
                if (ok) {
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10037001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10036004(buffer);
                if (ok) {
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
        }
    }
}
