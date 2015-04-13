package com.ankamagames.wakfu.client.alea.highlightingCells;

import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.wakfu.client.*;

public class DebugDisplayZone extends ElementSelection
{
    private static final DebugDisplayZone m_instance;
    private static final String LAYER_NAME = "debugDisplayer";
    
    public static DebugDisplayZone getInstance() {
        return DebugDisplayZone.m_instance;
    }
    
    private DebugDisplayZone() {
        super("debugDisplayer", WakfuClientConstants.DEBUG_PATH_COLOR);
    }
    
    public void setInfos(final int nbCoordinates, final int[][] tabCoordinates) {
        this.clear();
        for (final int[] step : tabCoordinates) {
            this.add(step[0], step[1], (short)step[2]);
        }
    }
    
    static {
        m_instance = new DebugDisplayZone();
    }
}
