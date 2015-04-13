package com.ankamagames.wakfu.common.game.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.procedure.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;

public class HavenWorldSerializer
{
    private static final Logger m_logger;
    
    public static byte[] serialize(final HavenWorld world) {
        final ByteArray bb = new ByteArray();
        bb.putShort(world.getWorldInstanceId());
        GuildInfo.encode(world.getGuildInfo(), bb);
        bb.putInt(world.getResources());
        final SerializeBuildings procedure = new SerializeBuildings();
        world.forEachBuilding(procedure);
        bb.putInt(procedure.getCounter());
        bb.put(procedure.getData());
        final SerializePartitions procedure2 = new SerializePartitions();
        world.forEachPartition(procedure2);
        bb.putInt(procedure2.getCounter());
        bb.put(procedure2.getData());
        return bb.toArray();
    }
    
    public static HavenWorld unserialize(final ByteBuffer bb) {
        final short worldInstanceId = bb.getShort();
        final GuildInfo guildInfo = GuildInfo.decode(bb);
        final int resources = bb.getInt();
        final HavenWorldDefinition definition = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(worldInstanceId);
        final HavenWorldModel havenWorld = (HavenWorldModel)HavenWorldFactory.create(definition, guildInfo, resources);
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            havenWorld.addBuilding(unserializeBuilding(bb));
        }
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            havenWorld.addPartition(unSerializePartition(bb));
        }
        return havenWorld;
    }
    
    public static byte[] serializePartition(final Partition partition) {
        final ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putShort(partition.getX());
        bb.putShort(partition.getY());
        bb.putShort(partition.getTopLeftPatch());
        bb.putShort(partition.getTopRightPatch());
        bb.putShort(partition.getBottomLeftPatch());
        bb.putShort(partition.getBottomRightPatch());
        return bb.array();
    }
    
    public static byte[] serializeBuildingWithElements(final Building building) {
        final ByteArray bb = new ByteArray();
        bb.put(serializeBuildingNoElements(building));
        final SerializeBuildingElements elementSerializer = new SerializeBuildingElements();
        building.forEachElement(elementSerializer);
        bb.putInt(elementSerializer.getNumber());
        bb.put(elementSerializer.getData());
        return bb.toArray();
    }
    
    public static byte[] serializeBuildingNoElements(final Building building) {
        final ByteBuffer bb = ByteBuffer.allocate(26);
        bb.putShort(building.getDefinition().getId());
        bb.putLong(building.getUid());
        bb.putInt(building.getEquippedItemId());
        bb.putLong(building.getCreationDate());
        bb.putShort(building.getX());
        bb.putShort(building.getY());
        return bb.array();
    }
    
    public static byte[] serializeElement(final BuildingElement element) {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(element.getUid());
        bb.putLong(element.getElementId());
        return bb.array();
    }
    
    public static Partition unSerializePartition(final ByteBuffer bb) {
        final short x = bb.getShort();
        final short y = bb.getShort();
        final short topLeftPatch = bb.getShort();
        final short topRightPatch = bb.getShort();
        final short bottomLeftPatch = bb.getShort();
        final short bottomRightPatch = bb.getShort();
        return new PartitionModel(x, y, topLeftPatch, topRightPatch, bottomLeftPatch, bottomRightPatch);
    }
    
    public static Building unserializeBuilding(final ByteBuffer bb) {
        final BuildingModel building = (BuildingModel)unSerializeBuildingNoElements(bb);
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            final BuildingElementModel element = unSerializeElement(bb);
            building.addElement(element);
        }
        return building;
    }
    
    public static Building unSerializeBuildingNoElements(final ByteBuffer bb) {
        final short defId = bb.getShort();
        final long uId = bb.getLong();
        final int itemId = bb.getInt();
        final long creationDate = bb.getLong();
        final short x = bb.getShort();
        final short y = bb.getShort();
        final AbstractBuildingDefinition def = HavenWorldDefinitionManager.INSTANCE.getBuilding(defId);
        return new BuildingModel(def, uId, itemId, creationDate, x, y);
    }
    
    private static BuildingElementModel unSerializeElement(final ByteBuffer bb) {
        final long elementUid = bb.getLong();
        final long elementIEId = bb.getLong();
        return new BuildingElementModel(elementUid, elementIEId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldSerializer.class);
    }
    
    private static class SerializeBuildingElements implements TObjectProcedure<BuildingElement>
    {
        private final ByteArray m_bb;
        private int m_number;
        
        SerializeBuildingElements() {
            super();
            this.m_bb = new ByteArray();
        }
        
        @Override
        public boolean execute(final BuildingElement object) {
            ++this.m_number;
            this.m_bb.put(HavenWorldSerializer.serializeElement(object));
            return true;
        }
        
        public ByteArray getData() {
            return this.m_bb;
        }
        
        public int getNumber() {
            return this.m_number;
        }
        
        @Override
        public String toString() {
            return "SerializeBuildingElements{m_number=" + this.m_number + '}';
        }
    }
}
