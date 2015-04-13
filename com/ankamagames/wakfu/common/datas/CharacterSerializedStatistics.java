package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedStatistics extends CharacterSerializedPart implements VersionableObject
{
    public int earnedKamas;
    public int lostKamas;
    public short fightCounter;
    public short deathCounter;
    public final ArrayList<AchievementDate> achievementDates;
    public final ArrayList<KillCounter> killCounters;
    public final ArrayList<CollectCounter> collectCounters;
    public final ArrayList<PlantCounter> plantCounters;
    public final ArrayList<CraftCounter> craftCounters;
    public final ArrayList<DropCounter> dropCounters;
    public final ArrayList<MarketCounter> marketBuyCount;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedStatistics() {
        super();
        this.earnedKamas = 0;
        this.lostKamas = 0;
        this.fightCounter = 0;
        this.deathCounter = 0;
        this.achievementDates = new ArrayList<AchievementDate>(0);
        this.killCounters = new ArrayList<KillCounter>(0);
        this.collectCounters = new ArrayList<CollectCounter>(0);
        this.plantCounters = new ArrayList<PlantCounter>(0);
        this.craftCounters = new ArrayList<CraftCounter>(0);
        this.dropCounters = new ArrayList<DropCounter>(0);
        this.marketBuyCount = new ArrayList<MarketCounter>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedStatistics.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedStatistics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedStatistics", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedStatistics.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedStatistics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedStatistics", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedStatistics.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.earnedKamas);
        buffer.putInt(this.lostKamas);
        buffer.putShort(this.fightCounter);
        buffer.putShort(this.deathCounter);
        if (this.achievementDates.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.achievementDates.size());
        for (int i = 0; i < this.achievementDates.size(); ++i) {
            final AchievementDate achievementDates_element = this.achievementDates.get(i);
            final boolean achievementDates_element_ok = achievementDates_element.serialize(buffer);
            if (!achievementDates_element_ok) {
                return false;
            }
        }
        if (this.killCounters.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.killCounters.size());
        for (int i = 0; i < this.killCounters.size(); ++i) {
            final KillCounter killCounters_element = this.killCounters.get(i);
            final boolean killCounters_element_ok = killCounters_element.serialize(buffer);
            if (!killCounters_element_ok) {
                return false;
            }
        }
        if (this.collectCounters.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.collectCounters.size());
        for (int i = 0; i < this.collectCounters.size(); ++i) {
            final CollectCounter collectCounters_element = this.collectCounters.get(i);
            final boolean collectCounters_element_ok = collectCounters_element.serialize(buffer);
            if (!collectCounters_element_ok) {
                return false;
            }
        }
        if (this.plantCounters.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.plantCounters.size());
        for (int i = 0; i < this.plantCounters.size(); ++i) {
            final PlantCounter plantCounters_element = this.plantCounters.get(i);
            final boolean plantCounters_element_ok = plantCounters_element.serialize(buffer);
            if (!plantCounters_element_ok) {
                return false;
            }
        }
        if (this.craftCounters.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.craftCounters.size());
        for (int i = 0; i < this.craftCounters.size(); ++i) {
            final CraftCounter craftCounters_element = this.craftCounters.get(i);
            final boolean craftCounters_element_ok = craftCounters_element.serialize(buffer);
            if (!craftCounters_element_ok) {
                return false;
            }
        }
        if (this.dropCounters.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.dropCounters.size());
        for (int i = 0; i < this.dropCounters.size(); ++i) {
            final DropCounter dropCounters_element = this.dropCounters.get(i);
            final boolean dropCounters_element_ok = dropCounters_element.serialize(buffer);
            if (!dropCounters_element_ok) {
                return false;
            }
        }
        if (this.marketBuyCount.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.marketBuyCount.size());
        for (int i = 0; i < this.marketBuyCount.size(); ++i) {
            final MarketCounter marketBuyCount_element = this.marketBuyCount.get(i);
            final boolean marketBuyCount_element_ok = marketBuyCount_element.serialize(buffer);
            if (!marketBuyCount_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.earnedKamas = buffer.getInt();
        this.lostKamas = buffer.getInt();
        this.fightCounter = buffer.getShort();
        this.deathCounter = buffer.getShort();
        final int achievementDates_size = buffer.getShort() & 0xFFFF;
        this.achievementDates.clear();
        this.achievementDates.ensureCapacity(achievementDates_size);
        for (int i = 0; i < achievementDates_size; ++i) {
            final AchievementDate achievementDates_element = new AchievementDate();
            final boolean achievementDates_element_ok = achievementDates_element.unserialize(buffer);
            if (!achievementDates_element_ok) {
                return false;
            }
            this.achievementDates.add(achievementDates_element);
        }
        final int killCounters_size = buffer.getShort() & 0xFFFF;
        this.killCounters.clear();
        this.killCounters.ensureCapacity(killCounters_size);
        for (int j = 0; j < killCounters_size; ++j) {
            final KillCounter killCounters_element = new KillCounter();
            final boolean killCounters_element_ok = killCounters_element.unserialize(buffer);
            if (!killCounters_element_ok) {
                return false;
            }
            this.killCounters.add(killCounters_element);
        }
        final int collectCounters_size = buffer.getShort() & 0xFFFF;
        this.collectCounters.clear();
        this.collectCounters.ensureCapacity(collectCounters_size);
        for (int k = 0; k < collectCounters_size; ++k) {
            final CollectCounter collectCounters_element = new CollectCounter();
            final boolean collectCounters_element_ok = collectCounters_element.unserialize(buffer);
            if (!collectCounters_element_ok) {
                return false;
            }
            this.collectCounters.add(collectCounters_element);
        }
        final int plantCounters_size = buffer.getShort() & 0xFFFF;
        this.plantCounters.clear();
        this.plantCounters.ensureCapacity(plantCounters_size);
        for (int l = 0; l < plantCounters_size; ++l) {
            final PlantCounter plantCounters_element = new PlantCounter();
            final boolean plantCounters_element_ok = plantCounters_element.unserialize(buffer);
            if (!plantCounters_element_ok) {
                return false;
            }
            this.plantCounters.add(plantCounters_element);
        }
        final int craftCounters_size = buffer.getShort() & 0xFFFF;
        this.craftCounters.clear();
        this.craftCounters.ensureCapacity(craftCounters_size);
        for (int m = 0; m < craftCounters_size; ++m) {
            final CraftCounter craftCounters_element = new CraftCounter();
            final boolean craftCounters_element_ok = craftCounters_element.unserialize(buffer);
            if (!craftCounters_element_ok) {
                return false;
            }
            this.craftCounters.add(craftCounters_element);
        }
        final int dropCounters_size = buffer.getShort() & 0xFFFF;
        this.dropCounters.clear();
        this.dropCounters.ensureCapacity(dropCounters_size);
        for (int i2 = 0; i2 < dropCounters_size; ++i2) {
            final DropCounter dropCounters_element = new DropCounter();
            final boolean dropCounters_element_ok = dropCounters_element.unserialize(buffer);
            if (!dropCounters_element_ok) {
                return false;
            }
            this.dropCounters.add(dropCounters_element);
        }
        final int marketBuyCount_size = buffer.getShort() & 0xFFFF;
        this.marketBuyCount.clear();
        this.marketBuyCount.ensureCapacity(marketBuyCount_size);
        for (int i3 = 0; i3 < marketBuyCount_size; ++i3) {
            final MarketCounter marketBuyCount_element = new MarketCounter();
            final boolean marketBuyCount_element_ok = marketBuyCount_element.unserialize(buffer);
            if (!marketBuyCount_element_ok) {
                return false;
            }
            this.marketBuyCount.add(marketBuyCount_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.earnedKamas = 0;
        this.lostKamas = 0;
        this.fightCounter = 0;
        this.deathCounter = 0;
        this.achievementDates.clear();
        this.killCounters.clear();
        this.collectCounters.clear();
        this.plantCounters.clear();
        this.craftCounters.clear();
        this.dropCounters.clear();
        this.marketBuyCount.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 4;
        size += 4;
        size += 2;
        size += 2;
        size += 2;
        for (int i = 0; i < this.achievementDates.size(); ++i) {
            final AchievementDate achievementDates_element = this.achievementDates.get(i);
            size += achievementDates_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.killCounters.size(); ++i) {
            final KillCounter killCounters_element = this.killCounters.get(i);
            size += killCounters_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.collectCounters.size(); ++i) {
            final CollectCounter collectCounters_element = this.collectCounters.get(i);
            size += collectCounters_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.plantCounters.size(); ++i) {
            final PlantCounter plantCounters_element = this.plantCounters.get(i);
            size += plantCounters_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.craftCounters.size(); ++i) {
            final CraftCounter craftCounters_element = this.craftCounters.get(i);
            size += craftCounters_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.dropCounters.size(); ++i) {
            final DropCounter dropCounters_element = this.dropCounters.get(i);
            size += dropCounters_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.marketBuyCount.size(); ++i) {
            final MarketCounter marketBuyCount_element = this.marketBuyCount.get(i);
            size += marketBuyCount_element.serializedSize();
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
        repr.append(prefix).append("earnedKamas=").append(this.earnedKamas).append('\n');
        repr.append(prefix).append("lostKamas=").append(this.lostKamas).append('\n');
        repr.append(prefix).append("fightCounter=").append(this.fightCounter).append('\n');
        repr.append(prefix).append("deathCounter=").append(this.deathCounter).append('\n');
        repr.append(prefix).append("achievementDates=");
        if (this.achievementDates.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.achievementDates.size()).append(" elements)...\n");
            for (int i = 0; i < this.achievementDates.size(); ++i) {
                final AchievementDate achievementDates_element = this.achievementDates.get(i);
                achievementDates_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("killCounters=");
        if (this.killCounters.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.killCounters.size()).append(" elements)...\n");
            for (int i = 0; i < this.killCounters.size(); ++i) {
                final KillCounter killCounters_element = this.killCounters.get(i);
                killCounters_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("collectCounters=");
        if (this.collectCounters.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.collectCounters.size()).append(" elements)...\n");
            for (int i = 0; i < this.collectCounters.size(); ++i) {
                final CollectCounter collectCounters_element = this.collectCounters.get(i);
                collectCounters_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("plantCounters=");
        if (this.plantCounters.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.plantCounters.size()).append(" elements)...\n");
            for (int i = 0; i < this.plantCounters.size(); ++i) {
                final PlantCounter plantCounters_element = this.plantCounters.get(i);
                plantCounters_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("craftCounters=");
        if (this.craftCounters.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.craftCounters.size()).append(" elements)...\n");
            for (int i = 0; i < this.craftCounters.size(); ++i) {
                final CraftCounter craftCounters_element = this.craftCounters.get(i);
                craftCounters_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("dropCounters=");
        if (this.dropCounters.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.dropCounters.size()).append(" elements)...\n");
            for (int i = 0; i < this.dropCounters.size(); ++i) {
                final DropCounter dropCounters_element = this.dropCounters.get(i);
                dropCounters_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("marketBuyCount=");
        if (this.marketBuyCount.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.marketBuyCount.size()).append(" elements)...\n");
            for (int i = 0; i < this.marketBuyCount.size(); ++i) {
                final MarketCounter marketBuyCount_element = this.marketBuyCount.get(i);
                marketBuyCount_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class AchievementDate implements VersionableObject
    {
        public int achievementId;
        public long date;
        public static final int SERIALIZED_SIZE = 12;
        
        public AchievementDate() {
            super();
            this.achievementId = 0;
            this.date = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.achievementId);
            buffer.putLong(this.date);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.achievementId = buffer.getInt();
            this.date = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.achievementId = 0;
            this.date = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 12;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("achievementId=").append(this.achievementId).append('\n');
            repr.append(prefix).append("date=").append(this.date).append('\n');
        }
    }
    
    public static class KillCounter implements VersionableObject
    {
        public short breedId;
        public short count;
        public static final int SERIALIZED_SIZE = 4;
        
        public KillCounter() {
            super();
            this.breedId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.breedId);
            buffer.putShort(this.count);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.breedId = buffer.getShort();
            this.count = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.breedId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 4;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("breedId=").append(this.breedId).append('\n');
            repr.append(prefix).append("count=").append(this.count).append('\n');
        }
    }
    
    public static class CollectCounter implements VersionableObject
    {
        public short referenceId;
        public short count;
        public static final int SERIALIZED_SIZE = 4;
        
        public CollectCounter() {
            super();
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.referenceId);
            buffer.putShort(this.count);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.referenceId = buffer.getShort();
            this.count = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 4;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
            repr.append(prefix).append("count=").append(this.count).append('\n');
        }
    }
    
    public static class PlantCounter implements VersionableObject
    {
        public short referenceId;
        public short count;
        public static final int SERIALIZED_SIZE = 4;
        
        public PlantCounter() {
            super();
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.referenceId);
            buffer.putShort(this.count);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.referenceId = buffer.getShort();
            this.count = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 4;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
            repr.append(prefix).append("count=").append(this.count).append('\n');
        }
    }
    
    public static class CraftCounter implements VersionableObject
    {
        public int referenceId;
        public short count;
        public static final int SERIALIZED_SIZE = 6;
        
        public CraftCounter() {
            super();
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.referenceId);
            buffer.putShort(this.count);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.referenceId = buffer.getInt();
            this.count = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 6;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
            repr.append(prefix).append("count=").append(this.count).append('\n');
        }
    }
    
    public static class DropCounter implements VersionableObject
    {
        public int referenceId;
        public short count;
        public static final int SERIALIZED_SIZE = 6;
        
        public DropCounter() {
            super();
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.referenceId);
            buffer.putShort(this.count);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.referenceId = buffer.getInt();
            this.count = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.referenceId = 0;
            this.count = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 6;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
            repr.append(prefix).append("count=").append(this.count).append('\n');
        }
    }
    
    public static class MarketCounter implements VersionableObject
    {
        public int itemId;
        public int count;
        public int price;
        public static final int SERIALIZED_SIZE = 12;
        
        public MarketCounter() {
            super();
            this.itemId = 0;
            this.count = 0;
            this.price = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.itemId);
            buffer.putInt(this.count);
            buffer.putInt(this.price);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.itemId = buffer.getInt();
            this.count = buffer.getInt();
            this.price = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.itemId = 0;
            this.count = 0;
            this.price = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 12;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("itemId=").append(this.itemId).append('\n');
            repr.append(prefix).append("count=").append(this.count).append('\n');
            repr.append(prefix).append("price=").append(this.price).append('\n');
        }
    }
}
