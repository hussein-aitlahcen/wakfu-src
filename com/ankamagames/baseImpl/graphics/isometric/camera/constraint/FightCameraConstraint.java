package com.ankamagames.baseImpl.graphics.isometric.camera.constraint;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class FightCameraConstraint implements CameraConstraint
{
    private static final Logger m_logger;
    private final AleaIsoCamera m_camera;
    private final FightMap m_fightMap;
    private final Rect m_fightMapBoundingRect;
    
    public FightCameraConstraint(final FightMap fightMap, final AleaIsoCamera camera) {
        super();
        this.m_fightMap = fightMap;
        this.m_camera = camera;
        this.m_fightMapBoundingRect = computFightBoundingRect(this.m_fightMap);
    }
    
    @Override
    public void clampMouseMove(final float centerScreenX, final float centerScreenY, final Point2 delta) {
        final Rect rect = WorldCameraConstraint.computeRect(this.m_camera, centerScreenX, centerScreenY, delta);
        if (rect == null) {
            return;
        }
        final float BORDER = 40.0f;
        final float left = this.m_fightMapBoundingRect.m_xMin - 40.0f;
        final float right = this.m_fightMapBoundingRect.m_xMax + 40.0f;
        final float bottom = this.m_fightMapBoundingRect.m_yMin - 40.0f;
        final float top = this.m_fightMapBoundingRect.m_yMax + 40.0f;
        if (this.m_fightMapBoundingRect.width() > rect.width() + 40.0f) {
            if (rect.m_xMin < left) {
                delta.m_x -= rect.m_xMin - left;
            }
            if (rect.m_xMax > right) {
                delta.m_x -= rect.m_xMax - right;
            }
        }
        else {
            final float fightCenterX = (float)this.m_fightMapBoundingRect.getCenterX();
            delta.m_x = fightCenterX - centerScreenX;
            delta.m_x = 0.0f;
        }
        if (this.m_fightMapBoundingRect.height() > rect.height() + 40.0f) {
            if (rect.m_yMin < bottom) {
                delta.m_y -= rect.m_yMin - bottom;
            }
            if (rect.m_yMax > top) {
                delta.m_y -= rect.m_yMax - top;
            }
        }
        else {
            final float fightCenterY = (float)this.m_fightMapBoundingRect.getCenterY();
            delta.m_y = fightCenterY - centerScreenY;
            delta.m_y = 0.0f;
        }
    }
    
    private boolean fightMapInsideRect(final Rect rect) {
        return this.m_fightMapBoundingRect.m_xMin >= rect.m_xMin && this.m_fightMapBoundingRect.m_xMax <= rect.m_xMax && this.m_fightMapBoundingRect.m_yMin >= rect.m_yMin && this.m_fightMapBoundingRect.m_yMax <= rect.m_yMax;
    }
    
    private static Rect computFightBoundingRect(final FightMap fightMap) {
        final Rect rect = new Rect(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
        final ArrayList<DisplayedScreenElement> elements = new ArrayList<DisplayedScreenElement>(16);
        final short[] cells = fightMap.getCells();
        final int minX = fightMap.getMinX();
        final int maxX = minX + fightMap.getWidth();
        final int minY = fightMap.getMinY();
        final int maxY = minY + fightMap.getHeight();
        int index = 0;
        for (int cellY = minY; cellY < maxY; ++cellY) {
            for (int cellX = minX; cellX < maxX; ++cellX) {
                if (fightMap.isInsideOrBorder(cellX, cellY)) {
                    final short data = cells[index++];
                    if (data != -1) {
                        elements.clear();
                        DisplayedScreenWorld.getInstance().getElements(cellX, cellY, elements, ElementFilter.VISIBLE_ONLY);
                        for (final DisplayedScreenElement e : elements) {
                            final Entity entity = e.getEntitySprite();
                            if (entity instanceof EntitySprite) {
                                final EntitySprite s = (EntitySprite)entity;
                                if (s.getLeft() < rect.m_xMin) {
                                    rect.m_xMin = MathHelper.fastFloor(s.getLeft());
                                }
                                if (s.getRight() > rect.m_xMax) {
                                    rect.m_xMax = MathHelper.fastCeil(s.getRight());
                                }
                                if (s.getBottom() < rect.m_yMin) {
                                    rect.m_yMin = MathHelper.fastFloor(s.getBottom());
                                }
                                if (s.getTop() <= rect.m_yMax) {
                                    continue;
                                }
                                rect.m_yMax = MathHelper.fastCeil(s.getTop());
                            }
                        }
                    }
                }
            }
        }
        return rect;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightCameraConstraint.class);
    }
}
