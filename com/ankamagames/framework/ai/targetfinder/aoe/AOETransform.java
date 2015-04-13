package com.ankamagames.framework.ai.targetfinder.aoe;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class AOETransform
{
    public abstract int[] apply(final int... p0);
    
    public abstract int[] applyInvert(final int... p0);
    
    static AOETransform aoeTransform(final int cellCenterX, final int cellCenterY, final Direction8 directionOfArea, final boolean invariantByRotation) {
        if (invariantByRotation || directionOfArea == Direction8.NONE || directionOfArea == Direction8.SOUTH_EAST) {
            return new TranslationAOETransform(cellCenterX, cellCenterY);
        }
        return new IsometryAOETransform(cellCenterX, cellCenterY, directionOfArea);
    }
    
    static class TranslationAOETransform extends AOETransform
    {
        private int[] m_translation;
        
        TranslationAOETransform(final int cellCenterX, final int cellCenterY) {
            super();
            this.m_translation = new int[] { cellCenterX, cellCenterY };
        }
        
        @Override
        public int[] apply(final int... point) {
            return new int[] { point[0] + this.m_translation[0], point[1] + this.m_translation[1] };
        }
        
        @Override
        public int[] applyInvert(final int... point) {
            return new int[] { point[0] - this.m_translation[0], point[1] - this.m_translation[1] };
        }
    }
    
    static class IsometryAOETransform extends AOETransform
    {
        private final int[][] m_transformMatrix;
        
        IsometryAOETransform(final int cellCenterX, final int cellCenterY, Direction8 rotationDirection) {
            super();
            if (rotationDirection == Direction8.NONE) {
                rotationDirection = Direction8.SOUTH_EAST;
            }
            final int directionX = rotationDirection.m_x;
            final int directionY = rotationDirection.m_y;
            this.m_transformMatrix = new int[][] { { directionX, -directionY, cellCenterX }, { directionY, directionX, cellCenterY } };
        }
        
        @Override
        public int[] apply(final int... point) {
            return new int[] { this.m_transformMatrix[0][0] * point[0] + this.m_transformMatrix[0][1] * point[1] + this.m_transformMatrix[0][2], this.m_transformMatrix[1][0] * point[0] + this.m_transformMatrix[1][1] * point[1] + this.m_transformMatrix[1][2] };
        }
        
        @Override
        public int[] applyInvert(final int... point) {
            final int tx = point[0] - this.m_transformMatrix[0][2];
            final int ty = point[1] - this.m_transformMatrix[1][2];
            return new int[] { this.m_transformMatrix[0][0] * tx + this.m_transformMatrix[1][0] * ty, this.m_transformMatrix[0][1] * tx + this.m_transformMatrix[1][1] * ty };
        }
    }
}
