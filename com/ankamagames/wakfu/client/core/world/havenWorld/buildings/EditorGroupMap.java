package com.ankamagames.wakfu.client.core.world.havenWorld.buildings;

import com.ankamagames.wakfu.common.game.world.havenWorld.building.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.byteKey.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;

public class EditorGroupMap extends AbstractEditorGroupMap
{
    private static final Logger m_logger;
    private GraphicalElement[] m_elements;
    private LightData m_lightData;
    private final ByteObjectLightWeightMap<byte[]> m_layersVisible;
    
    public EditorGroupMap(final int groupId) {
        super(groupId);
        this.m_layersVisible = new ByteObjectLightWeightMap<byte[]>();
    }
    
    @Override
    public void read(final ExtendedDataInputStream stream) throws IOException {
        super.read(stream);
        final int count = stream.readUnsignedShort();
        this.m_elements = new GraphicalElement[count];
        for (int i = 0; i < count; ++i) {
            this.m_elements[i] = GraphicalElement.fromStream(stream);
        }
        (this.m_lightData = new LightData((byte)this.getWidth(), (byte)this.getHeight())).read(stream);
        final int layerCount = stream.readUnsignedByte();
        this.m_layersVisible.ensureCapacity(layerCount);
        for (int j = 0; j < layerCount; ++j) {
            final byte layerId = (byte)stream.readShort();
            final byte[] layerIds = this.readVisibility(stream);
            this.m_layersVisible.put(layerId, layerIds);
        }
    }
    
    private byte[] readVisibility(final ExtendedDataInputStream stream) {
        final int n = stream.readShort();
        final byte[] layerIds = new byte[n];
        for (int j = 0; j < n; ++j) {
            layerIds[j] = (byte)stream.readShort();
        }
        return layerIds;
    }
    
    public LightData getLightData() {
        return new LightData(this.m_lightData);
    }
    
    public GraphicalElement[] getElements() {
        return this.m_elements;
    }
    
    public int getLayerCount() {
        return this.m_layersVisible.size();
    }
    
    public void foreach(final TByteObjectProcedure<byte[]> procedure) {
        for (int i = 0, size = this.m_layersVisible.size(); i < size; ++i) {
            if (!procedure.execute(this.m_layersVisible.getQuickKey(i), this.m_layersVisible.getQuickValue(i))) {
                return;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)EditorGroupMap.class);
    }
}
