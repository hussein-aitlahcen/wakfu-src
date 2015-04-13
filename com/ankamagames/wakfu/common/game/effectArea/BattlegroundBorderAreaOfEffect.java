package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public class BattlegroundBorderAreaOfEffect extends AreaOfEffect
{
    private final FightMap m_fightMap;
    private final ArrayList<int[]> m_pattern;
    private final ArrayList<int[]> m_cells;
    
    public BattlegroundBorderAreaOfEffect(final FightMap fightMap) {
        super();
        this.m_pattern = new ArrayList<int[]>();
        this.m_cells = new ArrayList<int[]>();
        this.m_fightMap = fightMap;
        final int width = this.m_fightMap.getWidth();
        final int minX = this.m_fightMap.getMinX();
        final int maxX = minX + width;
        final int minY = this.m_fightMap.getMinY();
        final int maxY = minY + this.m_fightMap.getHeight();
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                if (fightMap.isBorder(x, y)) {
                    this.m_pattern.add(new int[] { x - minX, y - minY });
                    this.m_cells.add(new int[] { x, y });
                }
            }
        }
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Un BattlegroundBorderAreaOfEffect ne peut \u00eatre initialis\u00e9 avec un tableau d'entiers venant d'une base de donn\u00e9es, par exemple. Il est cr\u00e9\u00e9 dynamiquement par un combat");
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return null;
    }
    
    @Override
    public boolean isPointInside(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 effectSourceDirection, final int cellQueriedX, final int cellQueriedY, final short cellQueriedAltitude) {
        return this.m_fightMap.isBorder(cellQueriedX, cellQueriedY);
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return false;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return null;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_pattern;
    }
    
    public Iterable<int[]> getCells() {
        return (Iterable<int[]>)Collections.unmodifiableList((List<?>)this.m_cells);
    }
    
    @Override
    public String toShortDescription() {
        return null;
    }
    
    @Override
    public int getVisualRange() {
        return 0;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        return null;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        return AreaOfEffectShape.SPECIAL;
    }
}
