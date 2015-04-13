package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CraftCounter extends CharacterSerializedPart implements VersionableObject
{
    public PlantationCounter plantationCounter;
    public NonDestructiveCollectCounter nonDestructiveCollectCounter;
    public DestructiveCollectCounter destructiveCollectCounter;
    public RecipeCounter recipeCounter;
    private final BinarSerialPart m_binarPart;
    
    public CraftCounter() {
        super();
        this.plantationCounter = null;
        this.nonDestructiveCollectCounter = null;
        this.destructiveCollectCounter = null;
        this.recipeCounter = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CraftCounter.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CraftCounter");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CraftCounter", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CraftCounter.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CraftCounter");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CraftCounter", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CraftCounter.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.plantationCounter != null) {
            buffer.put((byte)1);
            final boolean plantationCounter_ok = this.plantationCounter.serialize(buffer);
            if (!plantationCounter_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.nonDestructiveCollectCounter != null) {
            buffer.put((byte)1);
            final boolean nonDestructiveCollectCounter_ok = this.nonDestructiveCollectCounter.serialize(buffer);
            if (!nonDestructiveCollectCounter_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.destructiveCollectCounter != null) {
            buffer.put((byte)1);
            final boolean destructiveCollectCounter_ok = this.destructiveCollectCounter.serialize(buffer);
            if (!destructiveCollectCounter_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.recipeCounter != null) {
            buffer.put((byte)1);
            final boolean recipeCounter_ok = this.recipeCounter.serialize(buffer);
            if (!recipeCounter_ok) {
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
        final boolean plantationCounter_present = buffer.get() == 1;
        if (plantationCounter_present) {
            this.plantationCounter = new PlantationCounter();
            final boolean plantationCounter_ok = this.plantationCounter.unserialize(buffer);
            if (!plantationCounter_ok) {
                return false;
            }
        }
        else {
            this.plantationCounter = null;
        }
        final boolean nonDestructiveCollectCounter_present = buffer.get() == 1;
        if (nonDestructiveCollectCounter_present) {
            this.nonDestructiveCollectCounter = new NonDestructiveCollectCounter();
            final boolean nonDestructiveCollectCounter_ok = this.nonDestructiveCollectCounter.unserialize(buffer);
            if (!nonDestructiveCollectCounter_ok) {
                return false;
            }
        }
        else {
            this.nonDestructiveCollectCounter = null;
        }
        final boolean destructiveCollectCounter_present = buffer.get() == 1;
        if (destructiveCollectCounter_present) {
            this.destructiveCollectCounter = new DestructiveCollectCounter();
            final boolean destructiveCollectCounter_ok = this.destructiveCollectCounter.unserialize(buffer);
            if (!destructiveCollectCounter_ok) {
                return false;
            }
        }
        else {
            this.destructiveCollectCounter = null;
        }
        final boolean recipeCounter_present = buffer.get() == 1;
        if (recipeCounter_present) {
            this.recipeCounter = new RecipeCounter();
            final boolean recipeCounter_ok = this.recipeCounter.unserialize(buffer);
            if (!recipeCounter_ok) {
                return false;
            }
        }
        else {
            this.recipeCounter = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.plantationCounter = null;
        this.nonDestructiveCollectCounter = null;
        this.destructiveCollectCounter = null;
        this.recipeCounter = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CraftCounterConverter converter = new CraftCounterConverter();
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
        if (this.plantationCounter != null) {
            size += this.plantationCounter.serializedSize();
        }
        ++size;
        if (this.nonDestructiveCollectCounter != null) {
            size += this.nonDestructiveCollectCounter.serializedSize();
        }
        ++size;
        if (this.destructiveCollectCounter != null) {
            size += this.destructiveCollectCounter.serializedSize();
        }
        ++size;
        if (this.recipeCounter != null) {
            size += this.recipeCounter.serializedSize();
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
        repr.append(prefix).append("plantationCounter=");
        if (this.plantationCounter == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.plantationCounter.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("nonDestructiveCollectCounter=");
        if (this.nonDestructiveCollectCounter == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.nonDestructiveCollectCounter.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("destructiveCollectCounter=");
        if (this.destructiveCollectCounter == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.destructiveCollectCounter.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("recipeCounter=");
        if (this.recipeCounter == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.recipeCounter.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class PlantationCounter implements VersionableObject
    {
        public int counter;
        public static final int SERIALIZED_SIZE = 4;
        
        public PlantationCounter() {
            super();
            this.counter = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.counter);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.counter = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.counter = 0;
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
            repr.append(prefix).append("counter=").append(this.counter).append('\n');
        }
    }
    
    public static class NonDestructiveCollectCounter implements VersionableObject
    {
        public int counter;
        public static final int SERIALIZED_SIZE = 4;
        
        public NonDestructiveCollectCounter() {
            super();
            this.counter = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.counter);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.counter = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.counter = 0;
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
            repr.append(prefix).append("counter=").append(this.counter).append('\n');
        }
    }
    
    public static class DestructiveCollectCounter implements VersionableObject
    {
        public int counter;
        public static final int SERIALIZED_SIZE = 4;
        
        public DestructiveCollectCounter() {
            super();
            this.counter = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.counter);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.counter = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.counter = 0;
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
            repr.append(prefix).append("counter=").append(this.counter).append('\n');
        }
    }
    
    public static class RecipeCounter implements VersionableObject
    {
        public int counter;
        public static final int SERIALIZED_SIZE = 4;
        
        public RecipeCounter() {
            super();
            this.counter = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.counter);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.counter = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.counter = 0;
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
            repr.append(prefix).append("counter=").append(this.counter).append('\n');
        }
    }
    
    private final class CraftCounterConverter
    {
        private PlantationCounter plantationCounter;
        private NonDestructiveCollectCounter nonDestructiveCollectCounter;
        private DestructiveCollectCounter destructiveCollectCounter;
        private RecipeCounter recipeCounter;
        
        private CraftCounterConverter() {
            super();
            this.plantationCounter = null;
            this.nonDestructiveCollectCounter = null;
            this.destructiveCollectCounter = null;
            this.recipeCounter = null;
        }
        
        public void pushResult() {
            CraftCounter.this.plantationCounter = this.plantationCounter;
            CraftCounter.this.nonDestructiveCollectCounter = this.nonDestructiveCollectCounter;
            CraftCounter.this.destructiveCollectCounter = this.destructiveCollectCounter;
            CraftCounter.this.recipeCounter = this.recipeCounter;
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
