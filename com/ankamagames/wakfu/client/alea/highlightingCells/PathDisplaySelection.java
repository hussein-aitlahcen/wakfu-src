package com.ankamagames.wakfu.client.alea.highlightingCells;

import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.ai.pathfinder.*;

public class PathDisplaySelection extends ElementSelection
{
    private static final String LAYER_NAME = "pathDisplayer";
    private static final PathDisplaySelection m_instance;
    
    public static PathDisplaySelection getInstance() {
        return PathDisplaySelection.m_instance;
    }
    
    private PathDisplaySelection() {
        super("pathDisplayer", WakfuClientConstants.PATH_COLOR);
    }
    
    public void setPath(final PathFindResult path) {
        this.clear();
        for (int numCells = path.getPathLength(), i = 0; i < numCells; ++i) {
            final int[] step = path.getPathStep(i);
            this.add(step[0], step[1], (short)step[2]);
        }
    }
    
    static {
        m_instance = new PathDisplaySelection();
    }
}
