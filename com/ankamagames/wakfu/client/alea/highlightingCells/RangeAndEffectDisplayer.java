package com.ankamagames.wakfu.client.alea.highlightingCells;

import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;

public abstract class RangeAndEffectDisplayer
{
    protected final ElementSelection m_range;
    protected final ElementSelection m_rangeWithoutLOS;
    protected final ElementSelection m_rangeWithConstraint;
    private final EffectsExecutionZoneComputer m_effectsZoneComputer;
    final Point3 p;
    
    public RangeAndEffectDisplayer(final String rangeName, final float[] rangeColor, final String zoneEffectName, final float[] zoneEffectColor, final String rangeWithConstraintName, final float[] rangeWithConstraintColor, final String rangeWithoutLOSName, final float[] rangeWithoutLOSColor, final String emptyCellsName, final float[] emptyCellsColor) {
        super();
        this.p = new Point3();
        if (rangeName != null && rangeColor != null) {
            (this.m_range = new ElementSelection(rangeName, rangeColor)).setDisplayPriority(1);
        }
        else {
            this.m_range = null;
        }
        if (rangeWithConstraintName != null && rangeWithConstraintColor != null) {
            (this.m_rangeWithConstraint = new ElementSelection(rangeWithConstraintName, rangeWithConstraintColor)).setDisplayPriority(2);
        }
        else {
            this.m_rangeWithConstraint = null;
        }
        if (rangeWithoutLOSName != null && rangeWithoutLOSColor != null) {
            (this.m_rangeWithoutLOS = new ElementSelection(rangeWithoutLOSName, rangeWithoutLOSColor)).setDisplayPriority(3);
        }
        else {
            this.m_rangeWithoutLOS = null;
        }
        ElementSelection zoneEffect;
        if (zoneEffectName != null && zoneEffectColor != null) {
            zoneEffect = new ElementSelection(zoneEffectName, zoneEffectColor);
            zoneEffect.setDisplayPriority(4);
        }
        else {
            zoneEffect = null;
        }
        ElementSelection emptyCellsNeeded;
        if (emptyCellsName != null && emptyCellsColor != null) {
            emptyCellsNeeded = new ElementSelection(emptyCellsName, emptyCellsColor);
            emptyCellsNeeded.setDisplayPriority(5);
        }
        else {
            emptyCellsNeeded = null;
        }
        this.m_effectsZoneComputer = new EffectsExecutionZoneComputer(zoneEffect, emptyCellsNeeded);
    }
    
    protected void selectRange(final CharacterInfo characterInfo) {
        final Fight fight = characterInfo.getCurrentFight();
        final FightMap fightMap = fight.getFightMap();
        final boolean invisibleBlockingBorder = this.isInvisibleAndBlockingBorder(fight);
        this.clearZoneEffectAndRange();
        final int minX = fightMap.getMinX();
        final int minY = fightMap.getMinY();
        final int width = fightMap.getWidth();
        final int maxX = minX + width;
        final int maxY = minY + fightMap.getHeight();
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                if (!invisibleBlockingBorder || fightMap.isInside(x, y)) {
                    if (fightMap.isInsideOrBorder(x, y)) {
                        final short altitude = fightMap.getCellHeight(x, y);
                        if (altitude != -32768) {
                            this.p.set(x, y, altitude);
                            switch (this.checkValidity(this.p, characterInfo)) {
                                case OK: {
                                    if (this.m_range != null) {
                                        this.m_range.add(x, y, altitude);
                                        break;
                                    }
                                    break;
                                }
                                case INVALID_CRITERION: {
                                    if (this.m_rangeWithConstraint != null) {
                                        this.m_rangeWithConstraint.add(x, y, altitude);
                                        break;
                                    }
                                    break;
                                }
                                case INVALID_LOS: {
                                    if (this.m_rangeWithoutLOS != null) {
                                        this.m_rangeWithoutLOS.add(x, y, altitude);
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isInvisibleAndBlockingBorder(final Fight fight) {
        final AbstractBattlegroundBorderEffectArea area = fight.getBattlegroundBorderEffectArea();
        return area != null && area.isInvisible() && area.isBlockingMovement();
    }
    
    public void selectZoneEffect(final EffectContainer<WakfuEffect> container, final CharacterInfo fighter, final Point3 target) {
        this.m_effectsZoneComputer.clearAndSelectZoneEffect(container, fighter, target);
    }
    
    public void clearAndSelectTargetCell(final Point3 target) {
        this.m_effectsZoneComputer.clearAndSelectTargetCell(target);
    }
    
    protected void setTexture(final String textureFileName, final HighLightTextureApplication iso) {
        this.m_effectsZoneComputer.setTexture(textureFileName, iso);
    }
    
    public void clearZoneEffectAndRange() {
        this.clearZoneEffect();
        if (this.m_range != null) {
            this.m_range.clear();
        }
        if (this.m_rangeWithConstraint != null) {
            this.m_rangeWithConstraint.clear();
        }
        if (this.m_rangeWithoutLOS != null) {
            this.m_rangeWithoutLOS.clear();
        }
    }
    
    public void clearZoneEffect() {
        this.m_effectsZoneComputer.clear();
    }
    
    public boolean rangeContains(final Point3 target) {
        return this.m_range != null && this.m_range.contains(target);
    }
    
    protected abstract RangeValidity checkValidity(final Point3 p0, final CharacterInfo p1);
    
    public enum RangeValidity
    {
        OK, 
        OK_WITH_CONSTRAINTS, 
        INVALID_LOS, 
        INVALID_CRITERION, 
        INVALID;
    }
}
