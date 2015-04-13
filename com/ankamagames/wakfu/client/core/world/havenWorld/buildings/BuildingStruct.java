package com.ankamagames.wakfu.client.core.world.havenWorld.buildings;

import com.ankamagames.wakfu.common.game.world.havenWorld.building.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class BuildingStruct extends AbstractBuildingStruct<EditorGroupMap>
{
    private static int UID;
    private int m_layer;
    
    public BuildingStruct(final long buildingUid, final int buildingDefinitionId, final int itemId, final short cellX, final short cellY) {
        super(EditorGroupMapLibrary.INSTANCE, buildingUid, buildingDefinitionId, itemId, cellX, cellY);
    }
    
    public BuildingStruct(final Building b) {
        super(EditorGroupMapLibrary.INSTANCE, b);
    }
    
    public BuildingStruct(final short buildingDefinitionId, final int cellX, final int cellY) {
        this(--BuildingStruct.UID, buildingDefinitionId, 0, (short)cellX, (short)cellY);
    }
    
    public int getLayer() {
        return this.m_layer;
    }
    
    public void setLayer(final int layer) {
        this.m_layer = layer;
    }
    
    public static AbstractBuildingStruct fromRaw(final ByteBuffer buffer) {
        final long buildingUid = buffer.getLong();
        final short defId = buffer.getShort();
        final int itemId = buffer.getInt();
        final short x = buffer.getShort();
        final short y = buffer.getShort();
        return new BuildingStruct(buildingUid, defId, itemId, x, y);
    }
    
    static {
        BuildingStruct.UID = 0;
    }
    
    public static class Factory implements BuildingStructFactory
    {
        @Override
        public AbstractBuildingStruct createBuildingStruct(final Building b) {
            return new BuildingStruct(b);
        }
    }
}
