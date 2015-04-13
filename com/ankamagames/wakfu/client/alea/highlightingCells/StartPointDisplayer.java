package com.ankamagames.wakfu.client.alea.highlightingCells;

import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public class StartPointDisplayer
{
    private final int TEAM_COUNT = 2;
    private final HighLightLayer[] m_teamStartPoints;
    private static final StartPointDisplayer m_instance;
    
    public static StartPointDisplayer getInstance() {
        return StartPointDisplayer.m_instance;
    }
    
    private StartPointDisplayer() {
        super();
        this.m_teamStartPoints = new HighLightLayer[2];
        for (int i = 0; i < 2; ++i) {
            (this.m_teamStartPoints[i] = HighLightManager.getInstance().createLayer(this.getLayerName(i), HighLightTextureApplication.ISO)).setColor(WakfuClientConstants.TEAM_COLOR[i]);
        }
    }
    
    public void display(final Fight fight) {
        for (int i = 0; i < 2; ++i) {
            this.m_teamStartPoints[i].clear();
        }
        final FightMap fightMap = fight.getFightMap();
        final int minX = fightMap.getMinX();
        final int minY = fightMap.getMinY();
        final int width = fightMap.getWidth();
        final int maxX = minX + width;
        final int maxY = minY + fightMap.getHeight();
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                final byte teamId = fightMap.getTeamValidForStartPosition(x, y);
                if (teamId != -1) {
                    final short altitude = fightMap.getCellHeight(x, y);
                    this.m_teamStartPoints[teamId].add(x, y, altitude);
                }
            }
        }
    }
    
    public void clear() {
        for (int i = 0; i < 2; ++i) {
            this.m_teamStartPoints[i].clear();
        }
    }
    
    protected String getLayerName(final int id) {
        return "startPoint" + id;
    }
    
    static {
        m_instance = new StartPointDisplayer();
    }
}
