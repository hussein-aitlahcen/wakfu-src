package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedDiscoveredItemsInventory extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<Zaap> zaaps;
    public final ArrayList<Drago> dragos;
    public final ArrayList<Boat> boats;
    public final ArrayList<Cannon> cannon;
    public final ArrayList<Phoenix> phoenix;
    public int selectedPhoenix;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedDiscoveredItemsInventory() {
        super();
        this.zaaps = new ArrayList<Zaap>(0);
        this.dragos = new ArrayList<Drago>(0);
        this.boats = new ArrayList<Boat>(0);
        this.cannon = new ArrayList<Cannon>(0);
        this.phoenix = new ArrayList<Phoenix>(0);
        this.selectedPhoenix = -1;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedDiscoveredItemsInventory.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedDiscoveredItemsInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedDiscoveredItemsInventory", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedDiscoveredItemsInventory.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDiscoveredItemsInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDiscoveredItemsInventory", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedDiscoveredItemsInventory.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.zaaps.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.zaaps.size());
        for (int i = 0; i < this.zaaps.size(); ++i) {
            final Zaap zaaps_element = this.zaaps.get(i);
            final boolean zaaps_element_ok = zaaps_element.serialize(buffer);
            if (!zaaps_element_ok) {
                return false;
            }
        }
        if (this.dragos.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.dragos.size());
        for (int i = 0; i < this.dragos.size(); ++i) {
            final Drago dragos_element = this.dragos.get(i);
            final boolean dragos_element_ok = dragos_element.serialize(buffer);
            if (!dragos_element_ok) {
                return false;
            }
        }
        if (this.boats.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.boats.size());
        for (int i = 0; i < this.boats.size(); ++i) {
            final Boat boats_element = this.boats.get(i);
            final boolean boats_element_ok = boats_element.serialize(buffer);
            if (!boats_element_ok) {
                return false;
            }
        }
        if (this.cannon.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.cannon.size());
        for (int i = 0; i < this.cannon.size(); ++i) {
            final Cannon cannon_element = this.cannon.get(i);
            final boolean cannon_element_ok = cannon_element.serialize(buffer);
            if (!cannon_element_ok) {
                return false;
            }
        }
        if (this.phoenix.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.phoenix.size());
        for (int i = 0; i < this.phoenix.size(); ++i) {
            final Phoenix phoenix_element = this.phoenix.get(i);
            final boolean phoenix_element_ok = phoenix_element.serialize(buffer);
            if (!phoenix_element_ok) {
                return false;
            }
        }
        buffer.putInt(this.selectedPhoenix);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int zaaps_size = buffer.getShort() & 0xFFFF;
        this.zaaps.clear();
        this.zaaps.ensureCapacity(zaaps_size);
        for (int i = 0; i < zaaps_size; ++i) {
            final Zaap zaaps_element = new Zaap();
            final boolean zaaps_element_ok = zaaps_element.unserialize(buffer);
            if (!zaaps_element_ok) {
                return false;
            }
            this.zaaps.add(zaaps_element);
        }
        final int dragos_size = buffer.getShort() & 0xFFFF;
        this.dragos.clear();
        this.dragos.ensureCapacity(dragos_size);
        for (int j = 0; j < dragos_size; ++j) {
            final Drago dragos_element = new Drago();
            final boolean dragos_element_ok = dragos_element.unserialize(buffer);
            if (!dragos_element_ok) {
                return false;
            }
            this.dragos.add(dragos_element);
        }
        final int boats_size = buffer.getShort() & 0xFFFF;
        this.boats.clear();
        this.boats.ensureCapacity(boats_size);
        for (int k = 0; k < boats_size; ++k) {
            final Boat boats_element = new Boat();
            final boolean boats_element_ok = boats_element.unserialize(buffer);
            if (!boats_element_ok) {
                return false;
            }
            this.boats.add(boats_element);
        }
        final int cannon_size = buffer.getShort() & 0xFFFF;
        this.cannon.clear();
        this.cannon.ensureCapacity(cannon_size);
        for (int l = 0; l < cannon_size; ++l) {
            final Cannon cannon_element = new Cannon();
            final boolean cannon_element_ok = cannon_element.unserialize(buffer);
            if (!cannon_element_ok) {
                return false;
            }
            this.cannon.add(cannon_element);
        }
        final int phoenix_size = buffer.getShort() & 0xFFFF;
        this.phoenix.clear();
        this.phoenix.ensureCapacity(phoenix_size);
        for (int m = 0; m < phoenix_size; ++m) {
            final Phoenix phoenix_element = new Phoenix();
            final boolean phoenix_element_ok = phoenix_element.unserialize(buffer);
            if (!phoenix_element_ok) {
                return false;
            }
            this.phoenix.add(phoenix_element);
        }
        this.selectedPhoenix = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.zaaps.clear();
        this.dragos.clear();
        this.boats.clear();
        this.cannon.clear();
        this.phoenix.clear();
        this.selectedPhoenix = -1;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.zaaps.size(); ++i) {
            final Zaap zaaps_element = this.zaaps.get(i);
            size += zaaps_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.dragos.size(); ++i) {
            final Drago dragos_element = this.dragos.get(i);
            size += dragos_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.boats.size(); ++i) {
            final Boat boats_element = this.boats.get(i);
            size += boats_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.cannon.size(); ++i) {
            final Cannon cannon_element = this.cannon.get(i);
            size += cannon_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.phoenix.size(); ++i) {
            final Phoenix phoenix_element = this.phoenix.get(i);
            size += phoenix_element.serializedSize();
        }
        size += 4;
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("zaaps=");
        if (this.zaaps.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.zaaps.size()).append(" elements)...\n");
            for (int i = 0; i < this.zaaps.size(); ++i) {
                final Zaap zaaps_element = this.zaaps.get(i);
                zaaps_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("dragos=");
        if (this.dragos.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.dragos.size()).append(" elements)...\n");
            for (int i = 0; i < this.dragos.size(); ++i) {
                final Drago dragos_element = this.dragos.get(i);
                dragos_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("boats=");
        if (this.boats.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.boats.size()).append(" elements)...\n");
            for (int i = 0; i < this.boats.size(); ++i) {
                final Boat boats_element = this.boats.get(i);
                boats_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("cannon=");
        if (this.cannon.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.cannon.size()).append(" elements)...\n");
            for (int i = 0; i < this.cannon.size(); ++i) {
                final Cannon cannon_element = this.cannon.get(i);
                cannon_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("phoenix=");
        if (this.phoenix.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.phoenix.size()).append(" elements)...\n");
            for (int i = 0; i < this.phoenix.size(); ++i) {
                final Phoenix phoenix_element = this.phoenix.get(i);
                phoenix_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("selectedPhoenix=").append(this.selectedPhoenix).append('\n');
    }
    
    public static class Zaap implements VersionableObject
    {
        public int zaapId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Zaap() {
            super();
            this.zaapId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.zaapId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.zaapId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.zaapId = 0;
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
            repr.append(prefix).append("zaapId=").append(this.zaapId).append('\n');
        }
    }
    
    public static class Drago implements VersionableObject
    {
        public int dragoId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Drago() {
            super();
            this.dragoId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.dragoId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.dragoId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.dragoId = 0;
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
            repr.append(prefix).append("dragoId=").append(this.dragoId).append('\n');
        }
    }
    
    public static class Boat implements VersionableObject
    {
        public int boatId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Boat() {
            super();
            this.boatId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.boatId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.boatId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.boatId = 0;
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
            repr.append(prefix).append("boatId=").append(this.boatId).append('\n');
        }
    }
    
    public static class Cannon implements VersionableObject
    {
        public int cannonId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Cannon() {
            super();
            this.cannonId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.cannonId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.cannonId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.cannonId = 0;
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
            repr.append(prefix).append("cannonId=").append(this.cannonId).append('\n');
        }
    }
    
    public static class Phoenix implements VersionableObject
    {
        public int phoenixId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Phoenix() {
            super();
            this.phoenixId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.phoenixId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.phoenixId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.phoenixId = 0;
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
            repr.append(prefix).append("phoenixId=").append(this.phoenixId).append('\n');
        }
    }
}
