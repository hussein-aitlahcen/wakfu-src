package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class GradientBackgroundMesh extends PlainBackgroundMesh
{
    public void setColor(final Color color, final GradientBackgroundColorAlign align) {
        switch (align) {
            case NORTH_EAST: {
                this.m_entity.setColor(EntitySprite.Position.TopRight, color);
                break;
            }
            case NORTH_WEST: {
                this.m_entity.setColor(EntitySprite.Position.TopLeft, color);
                break;
            }
            case SOUTH_EAST: {
                this.m_entity.setColor(EntitySprite.Position.BottomRight, color);
                break;
            }
            case SOUTH_WEST: {
                this.m_entity.setColor(EntitySprite.Position.BottomLeft, color);
                break;
            }
            default: {
                assert false : "We should never end here";
                break;
            }
        }
    }
    
    public enum GradientBackgroundColorAlign
    {
        NORTH_WEST, 
        NORTH_EAST, 
        SOUTH_WEST, 
        SOUTH_EAST;
        
        public static GradientBackgroundColorAlign value(final String value) {
            final GradientBackgroundColorAlign[] arr$;
            final GradientBackgroundColorAlign[] values = arr$ = values();
            for (final GradientBackgroundColorAlign a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
    }
}
