package com.ankamagames.wakfu.client.core.world.havenWorld;

import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class ClientPartitionPatch extends PartitionPatch
{
    private static final Logger m_logger;
    private final EnvironmentData m_envData;
    private final LightData m_lightData;
    private GraphicalElement[] m_elements;
    
    public ClientPartitionPatch(final int id) {
        super(id);
        this.m_envData = new EnvironmentData();
        this.m_lightData = new LightData((byte)9, (byte)9);
    }
    
    @Override
    public void read(final ExtendedDataInputStream stream) throws IOException {
        super.read(stream);
        this.m_envData.read(stream);
        this.m_lightData.read(stream);
        this.readGraphical(stream);
    }
    
    private void readGraphical(final ExtendedDataInputStream stream) throws IOException {
        final int count = stream.readShort() & 0xFFFF;
        this.m_elements = new GraphicalElement[count];
        for (int i = 0; i < count; ++i) {
            this.m_elements[i] = GraphicalElement.fromStream(stream);
        }
    }
    
    public EnvironmentData getEnvData() {
        return this.m_envData;
    }
    
    LightData getLightData() {
        return this.m_lightData;
    }
    
    GraphicalElement[] getElements() {
        return this.m_elements;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientPartitionPatch.class);
    }
}
