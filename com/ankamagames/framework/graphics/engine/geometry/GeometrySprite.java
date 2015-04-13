package com.ankamagames.framework.graphics.engine.geometry;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.*;
import java.util.*;

public abstract class GeometrySprite extends GeometryMesh
{
    private static final float[] m_colorizationHelper;
    private static final float[] m_texCoordHelper;
    private static final float[] m_vertexPositions;
    private int m_width;
    private int m_height;
    private float m_top;
    private float m_left;
    private boolean m_uniformColoration;
    
    protected GeometrySprite() {
        super();
        this.m_indexBuffer = IndexBuffer.QUAD_INDICES;
        this.m_meshType = MeshType.TriangleStrip;
        this.allocateColors(4);
    }
    
    public final void setTextureCoordinates(final float top, final float left, final float bottom, final float right) {
        this.setTextureCoordinates(top, left, bottom, right, SpriteRotation.NONE);
    }
    
    public final void setTextureCoordinates(final float top, final float left, final float bottom, final float right, final SpriteRotation rot) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        this.m_vertexBuffer.rewindTexCoordBuffer();
        setTextureCoordinates(this.m_vertexBuffer, top, left, bottom, right, rot);
    }
    
    public static void setTextureCoordinates(final VertexBufferPCT vertexBuffer, final float top, final float left, final float bottom, final float right, final SpriteRotation rot) {
        switch (rot) {
            case NONE: {
                GeometrySprite.m_texCoordHelper[0] = left;
                GeometrySprite.m_texCoordHelper[1] = bottom;
                GeometrySprite.m_texCoordHelper[2] = left;
                GeometrySprite.m_texCoordHelper[3] = top;
                GeometrySprite.m_texCoordHelper[4] = right;
                GeometrySprite.m_texCoordHelper[5] = top;
                GeometrySprite.m_texCoordHelper[6] = right;
                GeometrySprite.m_texCoordHelper[7] = bottom;
                break;
            }
            case QUARTER_CLOCKWISE: {
                GeometrySprite.m_texCoordHelper[0] = right;
                GeometrySprite.m_texCoordHelper[1] = bottom;
                GeometrySprite.m_texCoordHelper[2] = left;
                GeometrySprite.m_texCoordHelper[3] = bottom;
                GeometrySprite.m_texCoordHelper[4] = left;
                GeometrySprite.m_texCoordHelper[5] = top;
                GeometrySprite.m_texCoordHelper[6] = right;
                GeometrySprite.m_texCoordHelper[7] = top;
                break;
            }
            case HALF: {
                GeometrySprite.m_texCoordHelper[0] = right;
                GeometrySprite.m_texCoordHelper[1] = top;
                GeometrySprite.m_texCoordHelper[2] = right;
                GeometrySprite.m_texCoordHelper[3] = bottom;
                GeometrySprite.m_texCoordHelper[4] = left;
                GeometrySprite.m_texCoordHelper[5] = bottom;
                GeometrySprite.m_texCoordHelper[6] = left;
                GeometrySprite.m_texCoordHelper[7] = top;
                break;
            }
            case QUARTER_COUNTER_CLOCKWISE: {
                GeometrySprite.m_texCoordHelper[0] = left;
                GeometrySprite.m_texCoordHelper[1] = top;
                GeometrySprite.m_texCoordHelper[2] = right;
                GeometrySprite.m_texCoordHelper[3] = top;
                GeometrySprite.m_texCoordHelper[4] = right;
                GeometrySprite.m_texCoordHelper[5] = bottom;
                GeometrySprite.m_texCoordHelper[6] = left;
                GeometrySprite.m_texCoordHelper[7] = bottom;
                break;
            }
        }
        vertexBuffer.putTexCoord(GeometrySprite.m_texCoordHelper);
    }
    
    public final void setTopLeft(final float top, final float left) {
        if (this.m_top != top || this.m_left != left) {
            this.m_top = top;
            this.m_left = left;
            this.updatePosition();
        }
    }
    
    public final void setSize(final int width, final int height) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        if (this.m_width != width || this.m_height != height) {
            this.m_width = width;
            this.m_height = height;
            this.updatePosition();
        }
    }
    
    public final void setBounds(final float top, final float left, final int width, final int height) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        if (this.m_top != top || this.m_left != left || this.m_width != width || this.m_height != height) {
            this.m_top = top;
            this.m_left = left;
            this.m_width = width;
            this.m_height = height;
            this.updatePosition();
        }
    }
    
    @Override
    public final void setColor(final float r, final float g, final float b, final float a) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        for (int i = 0; i < 4; ++i) {
            this.m_colors[i * 4] = r;
            this.m_colors[i * 4 + 1] = g;
            this.m_colors[i * 4 + 2] = b;
            this.m_colors[i * 4 + 3] = a;
        }
        this.m_vertexBuffer.rewindColorBuffer();
        setColor(this.m_vertexBuffer, r, g, b, a);
        this.m_uniformColoration = true;
    }
    
    public static void setColor(final VertexBufferPCT vertexBuffer, final float r, final float g, final float b, final float a) {
        final float[] colorizationHelper = GeometrySprite.m_colorizationHelper;
        final int n = 0;
        final float[] colorizationHelper2 = GeometrySprite.m_colorizationHelper;
        final int n2 = 4;
        GeometrySprite.m_colorizationHelper[8] = (GeometrySprite.m_colorizationHelper[12] = r);
        colorizationHelper[n] = (colorizationHelper2[n2] = r);
        final float[] colorizationHelper3 = GeometrySprite.m_colorizationHelper;
        final int n3 = 1;
        final float[] colorizationHelper4 = GeometrySprite.m_colorizationHelper;
        final int n4 = 5;
        GeometrySprite.m_colorizationHelper[9] = (GeometrySprite.m_colorizationHelper[13] = g);
        colorizationHelper3[n3] = (colorizationHelper4[n4] = g);
        final float[] colorizationHelper5 = GeometrySprite.m_colorizationHelper;
        final int n5 = 2;
        final float[] colorizationHelper6 = GeometrySprite.m_colorizationHelper;
        final int n6 = 6;
        GeometrySprite.m_colorizationHelper[10] = (GeometrySprite.m_colorizationHelper[14] = b);
        colorizationHelper5[n5] = (colorizationHelper6[n6] = b);
        final float[] colorizationHelper7 = GeometrySprite.m_colorizationHelper;
        final int n7 = 3;
        final float[] colorizationHelper8 = GeometrySprite.m_colorizationHelper;
        final int n8 = 7;
        GeometrySprite.m_colorizationHelper[11] = (GeometrySprite.m_colorizationHelper[15] = a);
        colorizationHelper7[n7] = (colorizationHelper8[n8] = a);
        vertexBuffer.putColorBuffer(GeometrySprite.m_colorizationHelper);
    }
    
    public final void setColor(final EntitySprite.Position position, final float r, final float g, final float b, final float a) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        final int index = position.ordinal();
        this.m_colors[index * 4] = r;
        this.m_colors[index * 4 + 1] = g;
        this.m_colors[index * 4 + 2] = b;
        this.m_colors[index * 4 + 3] = a;
        this.m_vertexBuffer.setVertexColor(position.ordinal(), r, g, b, a);
        this.m_uniformColoration = false;
    }
    
    @Override
    public final void applyMaterial(final Material material) {
        if (!this.m_uniformColoration) {
            this.applyMaterial(EntitySprite.Position.TopLeft, material);
            this.applyMaterial(EntitySprite.Position.BottomLeft, material);
            this.applyMaterial(EntitySprite.Position.TopRight, material);
            this.applyMaterial(EntitySprite.Position.BottomRight, material);
        }
        else {
            this.applyUniformMaterial(material);
        }
    }
    
    private void applyUniformMaterial(final Material material) {
        final float[] diffuse = material.getDiffuseColor();
        final float[] specular = material.getSpecularColor();
        final float r = this.m_colors[0] * diffuse[0] + specular[0];
        final float g = this.m_colors[1] * diffuse[1] + specular[1];
        final float b = this.m_colors[2] * diffuse[2] + specular[2];
        final float a = this.m_colors[3] * diffuse[3];
        final float[] colorizationHelper = GeometrySprite.m_colorizationHelper;
        final int n = 0;
        final float[] colorizationHelper2 = GeometrySprite.m_colorizationHelper;
        final int n2 = 4;
        final float[] colorizationHelper3 = GeometrySprite.m_colorizationHelper;
        final int n3 = 8;
        final float[] colorizationHelper4 = GeometrySprite.m_colorizationHelper;
        final int n4 = 12;
        final float n5 = r;
        colorizationHelper3[n3] = (colorizationHelper4[n4] = n5);
        colorizationHelper[n] = (colorizationHelper2[n2] = n5);
        final float[] colorizationHelper5 = GeometrySprite.m_colorizationHelper;
        final int n6 = 1;
        final float[] colorizationHelper6 = GeometrySprite.m_colorizationHelper;
        final int n7 = 5;
        final float[] colorizationHelper7 = GeometrySprite.m_colorizationHelper;
        final int n8 = 9;
        final float[] colorizationHelper8 = GeometrySprite.m_colorizationHelper;
        final int n9 = 13;
        final float n10 = g;
        colorizationHelper7[n8] = (colorizationHelper8[n9] = n10);
        colorizationHelper5[n6] = (colorizationHelper6[n7] = n10);
        final float[] colorizationHelper9 = GeometrySprite.m_colorizationHelper;
        final int n11 = 2;
        final float[] colorizationHelper10 = GeometrySprite.m_colorizationHelper;
        final int n12 = 6;
        final float[] colorizationHelper11 = GeometrySprite.m_colorizationHelper;
        final int n13 = 10;
        final float[] colorizationHelper12 = GeometrySprite.m_colorizationHelper;
        final int n14 = 14;
        final float n15 = b;
        colorizationHelper11[n13] = (colorizationHelper12[n14] = n15);
        colorizationHelper9[n11] = (colorizationHelper10[n12] = n15);
        final float[] colorizationHelper13 = GeometrySprite.m_colorizationHelper;
        final int n16 = 3;
        final float[] colorizationHelper14 = GeometrySprite.m_colorizationHelper;
        final int n17 = 7;
        final float[] colorizationHelper15 = GeometrySprite.m_colorizationHelper;
        final int n18 = 11;
        final float[] colorizationHelper16 = GeometrySprite.m_colorizationHelper;
        final int n19 = 15;
        final float n20 = a;
        colorizationHelper15[n18] = (colorizationHelper16[n19] = n20);
        colorizationHelper13[n16] = (colorizationHelper14[n17] = n20);
        this.m_vertexBuffer.setVertexColor(GeometrySprite.m_colorizationHelper);
    }
    
    public void applyMaterial(final EntitySprite.Position position, final Material material) {
        final int colorIndex = position.ordinal();
        final float[] diffuse = material.getDiffuseColor();
        final float[] specular = material.getSpecularColor();
        final float r = this.m_colors[colorIndex * 4] * diffuse[0] + specular[0];
        final float g = this.m_colors[colorIndex * 4 + 1] * diffuse[1] + specular[1];
        final float b = this.m_colors[colorIndex * 4 + 2] * diffuse[2] + specular[2];
        final float a = this.m_colors[colorIndex * 4 + 3] * diffuse[3];
        this.m_vertexBuffer.setVertexColor(colorIndex, r, g, b, a);
    }
    
    public final float getCenterX() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_left + this.m_width * 0.5f;
    }
    
    public final float getCenterY() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_top - this.m_height * 0.5f;
    }
    
    public final float getLeft() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_left;
    }
    
    public final float getRight() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_left + this.m_width;
    }
    
    public final float getTop() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_top;
    }
    
    public final float getBottom() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_top - this.m_height;
    }
    
    public final float getHalfWidth() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_width * 0.5f;
    }
    
    public final float getHalfHeight() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_height * 0.5f;
    }
    
    public final int getWidth() {
        return this.m_width;
    }
    
    public final int getHeight() {
        return this.m_height;
    }
    
    @Override
    public abstract void render(final Renderer p0);
    
    @Override
    protected void checkout() {
        super.checkout();
        final boolean b = false;
        this.m_height = (b ? 1 : 0);
        this.m_width = (b ? 1 : 0);
        final float n = 0.0f;
        this.m_left = n;
        this.m_top = n;
        if (this.m_colors != null) {
            Arrays.fill(this.m_colors, 1.0f);
        }
        this.m_uniformColoration = true;
        assert this.m_vertexBuffer == null;
        this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(4);
    }
    
    @Override
    protected void checkin() {
        this.m_vertexBuffer.removeReference();
        this.m_vertexBuffer = null;
    }
    
    private void updatePosition() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        this.m_vertexBuffer.rewindPositionBuffer();
        setPositions(this.m_vertexBuffer, this.m_top, this.m_left, this.m_width, this.m_height);
    }
    
    public static void setPositions(final VertexBufferPCT vertexBuffer, final float top, final float left, final float width, final float height) {
        final float bottom = top - height;
        final float right = left + width;
        GeometrySprite.m_vertexPositions[0] = left;
        GeometrySprite.m_vertexPositions[1] = bottom;
        GeometrySprite.m_vertexPositions[2] = left;
        GeometrySprite.m_vertexPositions[3] = top;
        GeometrySprite.m_vertexPositions[4] = right;
        GeometrySprite.m_vertexPositions[5] = top;
        GeometrySprite.m_vertexPositions[6] = right;
        GeometrySprite.m_vertexPositions[7] = bottom;
        vertexBuffer.putPositionBuffer(GeometrySprite.m_vertexPositions);
    }
    
    static {
        m_colorizationHelper = new float[16];
        m_texCoordHelper = new float[8];
        m_vertexPositions = new float[8];
    }
    
    public enum SpriteRotation
    {
        NONE(false), 
        QUARTER_CLOCKWISE(true), 
        HALF(false), 
        QUARTER_COUNTER_CLOCKWISE(true);
        
        private final boolean m_affectWidthAndHeight;
        
        private SpriteRotation(final boolean affectWidthAndHeight) {
            this.m_affectWidthAndHeight = affectWidthAndHeight;
        }
        
        public boolean isAffectWidthAndHeight() {
            return this.m_affectWidthAndHeight;
        }
        
        public static SpriteRotation value(final String value) {
            final SpriteRotation[] arr$;
            final SpriteRotation[] values = arr$ = values();
            for (final SpriteRotation a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
        
        public static boolean affectWidthAndHeight(final SpriteRotation rot1, final SpriteRotation rot2) {
            return rot1.ordinal() % 2 != rot2.ordinal() % 2;
        }
    }
}
