package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawHavenWorldBuildings implements VersionableObject
{
    public final ArrayList<Building> buildings;
    
    public RawHavenWorldBuildings() {
        super();
        this.buildings = new ArrayList<Building>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.buildings.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.buildings.size());
        for (int i = 0; i < this.buildings.size(); ++i) {
            final Building buildings_element = this.buildings.get(i);
            final boolean buildings_element_ok = buildings_element.serialize(buffer);
            if (!buildings_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int buildings_size = buffer.getShort() & 0xFFFF;
        this.buildings.clear();
        this.buildings.ensureCapacity(buildings_size);
        for (int i = 0; i < buildings_size; ++i) {
            final Building buildings_element = new Building();
            final boolean buildings_element_ok = buildings_element.unserialize(buffer);
            if (!buildings_element_ok) {
                return false;
            }
            this.buildings.add(buildings_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.buildings.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.buildings.size(); ++i) {
            final Building buildings_element = this.buildings.get(i);
            size += buildings_element.serializedSize();
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
        repr.append(prefix).append("buildings=");
        if (this.buildings.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.buildings.size()).append(" elements)...\n");
            for (int i = 0; i < this.buildings.size(); ++i) {
                final Building buildings_element = this.buildings.get(i);
                buildings_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Building implements VersionableObject
    {
        public final RawHavenWorldBuilding building;
        
        public Building() {
            super();
            this.building = new RawHavenWorldBuilding();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean building_ok = this.building.serialize(buffer);
            return building_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean building_ok = this.building.unserialize(buffer);
            return building_ok;
        }
        
        @Override
        public void clear() {
            this.building.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.building.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("building=...\n");
            this.building.internalToString(repr, prefix + "  ");
        }
    }
}
