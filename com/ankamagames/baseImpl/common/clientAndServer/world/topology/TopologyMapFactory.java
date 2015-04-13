package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps.*;

public class TopologyMapFactory
{
    private static final Logger m_logger;
    public static final byte VERSION_MASK = -16;
    public static final byte METHOD_MASK = 15;
    public static final int VERSION_NUMBER_POSITION = 4;
    public static final int METHOD_NUMBER_POSITION = 0;
    public static final byte METHOD_A = 0;
    public static final byte METHOD_B = 1;
    public static final byte METHOD_Bi = 2;
    public static final byte METHOD_C = 3;
    public static final byte METHOD_Ci = 4;
    public static final byte METHOD_Di = 5;
    
    public static TopologyMap createTopologyMap(final byte header) {
        final byte method = (byte)((header & 0xF) >> 0);
        switch (method) {
            case 0: {
                return new TopologyMapA();
            }
            case 1: {
                return new TopologyMapB();
            }
            case 2: {
                return new TopologyMapBi();
            }
            case 3: {
                return new TopologyMapC();
            }
            case 4: {
                return new TopologyMapCi();
            }
            case 5: {
                return new TopologyMapDi();
            }
            default: {
                TopologyMapFactory.m_logger.error((Object)("Type de map inconnu " + method));
                return null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TopologyMapFactory.class);
    }
}
