package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;
import java.util.*;

public class RawItemOptionalInfo implements VersionableObject
{
    public Pet pet;
    public Xp xp;
    public Gems gems;
    public Companion companion;
    
    public RawItemOptionalInfo() {
        super();
        this.pet = null;
        this.xp = null;
        this.gems = null;
        this.companion = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
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
        if (this.companion != null) {
            buffer.put((byte)1);
            final boolean companion_ok = this.companion.serialize(buffer);
            if (!companion_ok) {
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
        final boolean companion_present = buffer.get() == 1;
        if (companion_present) {
            this.companion = new Companion();
            final boolean companion_ok = this.companion.unserialize(buffer);
            if (!companion_ok) {
                return false;
            }
        }
        else {
            this.companion = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.pet = null;
        this.xp = null;
        this.gems = null;
        this.companion = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10035007) {
            return this.unserialize(buffer);
        }
        final RawItemOptionalInfoConverter converter = new RawItemOptionalInfoConverter();
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
        if (this.companion != null) {
            size += this.companion.serializedSize();
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
        repr.append(prefix).append("companion=");
        if (this.companion == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.companion.internalToString(repr, prefix + "  ");
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
                return true;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v10024001(final ByteBuffer buffer) {
                final boolean rawPet_ok = this.rawPet.unserializeVersion(buffer, 10024001);
                return rawPet_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean rawPet_ok = this.rawPet.unserializeVersion(buffer, 10035004);
                return rawPet_ok;
            }
            
            public void convert_v313_to_v315() {
            }
            
            public void convert_v315_to_v10024001() {
            }
            
            public void convert_v10024001_to_v10035004() {
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
                        this.convert_v315_to_v10024001();
                        this.convert_v10024001_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        return true;
                    }
                    return false;
                }
                else if (version < 10024001) {
                    final boolean ok = this.unserialize_v315(buffer);
                    if (ok) {
                        this.convert_v315_to_v10024001();
                        this.convert_v10024001_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035004) {
                    final boolean ok = this.unserialize_v10024001(buffer);
                    if (ok) {
                        this.convert_v10024001_to_v10035004();
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
            if (version >= 10024001) {
                return this.unserialize(buffer);
            }
            final XpConverter converter = new XpConverter();
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
        
        private final class XpConverter
        {
            private final RawItemXp rawXp;
            
            private XpConverter() {
                super();
                this.rawXp = new RawItemXp();
            }
            
            public void pushResult() {
                Xp.this.rawXp.definitionId = this.rawXp.definitionId;
                Xp.this.rawXp.xp = this.rawXp.xp;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v314_to_v10024001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 314) {
                    return false;
                }
                if (version >= 10024001) {
                    return false;
                }
                final boolean ok = this.unserialize_v314(buffer);
                if (ok) {
                    this.convert_v314_to_v10024001();
                    return true;
                }
                return false;
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
            if (version >= 10024001) {
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
            
            public void convert_v1_to_v10024001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10024001) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10024001();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class Companion implements VersionableObject
    {
        public final RawCompanionInfo rawCompanion;
        
        public Companion() {
            super();
            this.rawCompanion = new RawCompanionInfo();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean rawCompanion_ok = this.rawCompanion.serialize(buffer);
            return rawCompanion_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean rawCompanion_ok = this.rawCompanion.unserialize(buffer);
            return rawCompanion_ok;
        }
        
        @Override
        public void clear() {
            this.rawCompanion.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10030002) {
                return this.unserialize(buffer);
            }
            final CompanionConverter converter = new CompanionConverter();
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
            size += this.rawCompanion.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("rawCompanion=...\n");
            this.rawCompanion.internalToString(repr, prefix + "  ");
        }
        
        private final class CompanionConverter
        {
            private final RawCompanionInfo rawCompanion;
            
            private CompanionConverter() {
                super();
                this.rawCompanion = new RawCompanionInfo();
            }
            
            public void pushResult() {
                Companion.this.rawCompanion.xp = this.rawCompanion.xp;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v1_to_v10030002() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10030002) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10030002();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class RawItemOptionalInfoConverter
    {
        private Pet pet;
        private Xp xp;
        private Gems gems;
        private Companion companion;
        
        private RawItemOptionalInfoConverter() {
            super();
            this.pet = null;
            this.xp = null;
            this.gems = null;
            this.companion = null;
        }
        
        public void pushResult() {
            RawItemOptionalInfo.this.pet = this.pet;
            RawItemOptionalInfo.this.xp = this.xp;
            RawItemOptionalInfo.this.gems = this.gems;
            RawItemOptionalInfo.this.companion = this.companion;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v10024001(final ByteBuffer buffer) {
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10024001);
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
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10024001);
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
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10024001);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            return true;
        }
        
        private boolean unserialize_v10030002(final ByteBuffer buffer) {
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10030002);
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
                final boolean xp_ok = this.xp.unserializeVersion(buffer, 10030002);
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
                final boolean gems_ok = this.gems.unserializeVersion(buffer, 10030002);
                if (!gems_ok) {
                    return false;
                }
            }
            else {
                this.gems = null;
            }
            final boolean companion_present = buffer.get() == 1;
            if (companion_present) {
                this.companion = new Companion();
                final boolean companion_ok = this.companion.unserializeVersion(buffer, 10030002);
                if (!companion_ok) {
                    return false;
                }
            }
            else {
                this.companion = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
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
            final boolean companion_present = buffer.get() == 1;
            if (companion_present) {
                this.companion = new Companion();
                final boolean companion_ok = this.companion.unserializeVersion(buffer, 10035004);
                if (!companion_ok) {
                    return false;
                }
            }
            else {
                this.companion = null;
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
        
        public void convert_v315_to_v10024001() {
        }
        
        public void convert_v10024001_to_v10030002() {
        }
        
        public void convert_v10030002_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035007() {
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
                    this.convert_v315_to_v10024001();
                    this.convert_v10024001_to_v10030002();
                    this.convert_v10030002_to_v10035004();
                    this.convert_v10035004_to_v10035007();
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
                    this.convert_v315_to_v10024001();
                    this.convert_v10024001_to_v10030002();
                    this.convert_v10030002_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
            else if (version < 314) {
                final boolean ok = this.unserialize_v313(buffer);
                if (ok) {
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10024001();
                    this.convert_v10024001_to_v10030002();
                    this.convert_v10030002_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
            else if (version < 315) {
                final boolean ok = this.unserialize_v314(buffer);
                if (ok) {
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10024001();
                    this.convert_v10024001_to_v10030002();
                    this.convert_v10030002_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
            else if (version < 10024001) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10024001();
                    this.convert_v10024001_to_v10030002();
                    this.convert_v10030002_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
            else if (version < 10030002) {
                final boolean ok = this.unserialize_v10024001(buffer);
                if (ok) {
                    this.convert_v10024001_to_v10030002();
                    this.convert_v10030002_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    return true;
                }
                return false;
            }
            else if (version < 10035004) {
                final boolean ok = this.unserialize_v10030002(buffer);
                if (ok) {
                    this.convert_v10030002_to_v10035004();
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
