package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedCraft extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<RawCraft> rawCrafts;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedCraft() {
        super();
        this.rawCrafts = new ArrayList<RawCraft>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedCraft.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedCraft");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedCraft", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedCraft.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCraft");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCraft", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedCraft.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.rawCrafts.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.rawCrafts.size());
        for (int i = 0; i < this.rawCrafts.size(); ++i) {
            final RawCraft rawCrafts_element = this.rawCrafts.get(i);
            final boolean rawCrafts_element_ok = rawCrafts_element.serialize(buffer);
            if (!rawCrafts_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int rawCrafts_size = buffer.getShort() & 0xFFFF;
        this.rawCrafts.clear();
        this.rawCrafts.ensureCapacity(rawCrafts_size);
        for (int i = 0; i < rawCrafts_size; ++i) {
            final RawCraft rawCrafts_element = new RawCraft();
            final boolean rawCrafts_element_ok = rawCrafts_element.unserialize(buffer);
            if (!rawCrafts_element_ok) {
                return false;
            }
            this.rawCrafts.add(rawCrafts_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.rawCrafts.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedCraftConverter converter = new CharacterSerializedCraftConverter();
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
        size += 2;
        for (int i = 0; i < this.rawCrafts.size(); ++i) {
            final RawCraft rawCrafts_element = this.rawCrafts.get(i);
            size += rawCrafts_element.serializedSize();
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
        repr.append(prefix).append("rawCrafts=");
        if (this.rawCrafts.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.rawCrafts.size()).append(" elements)...\n");
            for (int i = 0; i < this.rawCrafts.size(); ++i) {
                final RawCraft rawCrafts_element = this.rawCrafts.get(i);
                rawCrafts_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class RawCraft implements VersionableObject
    {
        public int refCraftId;
        public long xp;
        public final CraftCounter craftCounter;
        public final ArrayList<RawLearnedRecipe> rawLearnedRecipes;
        
        public RawCraft() {
            super();
            this.refCraftId = 0;
            this.xp = 0L;
            this.craftCounter = new CraftCounter();
            this.rawLearnedRecipes = new ArrayList<RawLearnedRecipe>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.refCraftId);
            buffer.putLong(this.xp);
            final boolean craftCounter_ok = this.craftCounter.serialize(buffer);
            if (!craftCounter_ok) {
                return false;
            }
            if (this.rawLearnedRecipes.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.rawLearnedRecipes.size());
            for (int i = 0; i < this.rawLearnedRecipes.size(); ++i) {
                final RawLearnedRecipe rawLearnedRecipes_element = this.rawLearnedRecipes.get(i);
                final boolean rawLearnedRecipes_element_ok = rawLearnedRecipes_element.serialize(buffer);
                if (!rawLearnedRecipes_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.refCraftId = buffer.getInt();
            this.xp = buffer.getLong();
            final boolean craftCounter_ok = this.craftCounter.unserialize(buffer);
            if (!craftCounter_ok) {
                return false;
            }
            final int rawLearnedRecipes_size = buffer.getShort() & 0xFFFF;
            this.rawLearnedRecipes.clear();
            this.rawLearnedRecipes.ensureCapacity(rawLearnedRecipes_size);
            for (int i = 0; i < rawLearnedRecipes_size; ++i) {
                final RawLearnedRecipe rawLearnedRecipes_element = new RawLearnedRecipe();
                final boolean rawLearnedRecipes_element_ok = rawLearnedRecipes_element.unserialize(buffer);
                if (!rawLearnedRecipes_element_ok) {
                    return false;
                }
                this.rawLearnedRecipes.add(rawLearnedRecipes_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.refCraftId = 0;
            this.xp = 0L;
            this.craftCounter.clear();
            this.rawLearnedRecipes.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final RawCraftConverter converter = new RawCraftConverter();
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
            size += 8;
            size += this.craftCounter.serializedSize();
            size += 2;
            for (int i = 0; i < this.rawLearnedRecipes.size(); ++i) {
                final RawLearnedRecipe rawLearnedRecipes_element = this.rawLearnedRecipes.get(i);
                size += rawLearnedRecipes_element.serializedSize();
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
            repr.append(prefix).append("refCraftId=").append(this.refCraftId).append('\n');
            repr.append(prefix).append("xp=").append(this.xp).append('\n');
            repr.append(prefix).append("craftCounter=...\n");
            this.craftCounter.internalToString(repr, prefix + "  ");
            repr.append(prefix).append("rawLearnedRecipes=");
            if (this.rawLearnedRecipes.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.rawLearnedRecipes.size()).append(" elements)...\n");
                for (int i = 0; i < this.rawLearnedRecipes.size(); ++i) {
                    final RawLearnedRecipe rawLearnedRecipes_element = this.rawLearnedRecipes.get(i);
                    rawLearnedRecipes_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class RawLearnedRecipe implements VersionableObject
        {
            public int recipeId;
            public static final int SERIALIZED_SIZE = 4;
            
            public RawLearnedRecipe() {
                super();
                this.recipeId = 0;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.putInt(this.recipeId);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.recipeId = buffer.getInt();
                return true;
            }
            
            @Override
            public void clear() {
                this.recipeId = 0;
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
                repr.append(prefix).append("recipeId=").append(this.recipeId).append('\n');
            }
        }
        
        private final class RawCraftConverter
        {
            private int refCraftId;
            private long xp;
            private final CraftCounter craftCounter;
            private final ArrayList<RawLearnedRecipe> rawLearnedRecipes;
            
            private RawCraftConverter() {
                super();
                this.refCraftId = 0;
                this.xp = 0L;
                this.craftCounter = new CraftCounter();
                this.rawLearnedRecipes = new ArrayList<RawLearnedRecipe>(0);
            }
            
            public void pushResult() {
                RawCraft.this.refCraftId = this.refCraftId;
                RawCraft.this.xp = this.xp;
                RawCraft.this.craftCounter.plantationCounter = this.craftCounter.plantationCounter;
                RawCraft.this.craftCounter.nonDestructiveCollectCounter = this.craftCounter.nonDestructiveCollectCounter;
                RawCraft.this.craftCounter.destructiveCollectCounter = this.craftCounter.destructiveCollectCounter;
                RawCraft.this.craftCounter.recipeCounter = this.craftCounter.recipeCounter;
                RawCraft.this.rawLearnedRecipes.clear();
                RawCraft.this.rawLearnedRecipes.ensureCapacity(this.rawLearnedRecipes.size());
                RawCraft.this.rawLearnedRecipes.addAll(this.rawLearnedRecipes);
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version >= 1) {
                    return false;
                }
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class CharacterSerializedCraftConverter
    {
        private final ArrayList<RawCraft> rawCrafts;
        
        private CharacterSerializedCraftConverter() {
            super();
            this.rawCrafts = new ArrayList<RawCraft>(0);
        }
        
        public void pushResult() {
            CharacterSerializedCraft.this.rawCrafts.clear();
            CharacterSerializedCraft.this.rawCrafts.ensureCapacity(this.rawCrafts.size());
            CharacterSerializedCraft.this.rawCrafts.addAll(this.rawCrafts);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 1) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v1();
                return true;
            }
            return false;
        }
    }
}
