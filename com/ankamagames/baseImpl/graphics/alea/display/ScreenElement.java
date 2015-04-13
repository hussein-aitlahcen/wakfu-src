package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class ScreenElement extends MemoryObject
{
    private static final Logger m_logger;
    public static final ObjectFactory Factory;
    byte m_type;
    short m_cellZ;
    int m_cellX;
    int m_cellY;
    int m_top;
    int m_left;
    ElementProperties m_commonData;
    int m_altitudeOrder;
    byte m_height;
    int m_groupKey;
    short m_layerIndex;
    int m_groupId;
    boolean m_occluder;
    long m_hashCode;
    float[] m_colors;
    private static final float[] m_tempColor;
    private static final float DEFAULT_TEINT = 0.5f;
    private static final float DEFAULT_ALPHA = 1.0f;
    public static final int TEINT_MASK = 1;
    public static final int ALPHA_MASK = 2;
    public static final int GRADIENT_MASK = 4;
    private static final int[] Indexes;
    
    private ScreenElement() {
        super();
    }
    
    public ScreenElement(final byte type) {
        super();
        this.m_type = type;
        this.m_colors = newColorsFromType(type);
    }
    
    public final boolean load(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_cellZ = bitStream.readShort();
        this.m_height = bitStream.readByte();
        this.m_altitudeOrder = bitStream.readByte();
        this.m_occluder = bitStream.readBooleanBit();
        this.m_type = (byte)(bitStream.readBooleanBit() ? 1 : 0);
        this.m_type |= (byte)(bitStream.readBooleanBit() ? 2 : 0);
        this.m_type |= (byte)(bitStream.readBooleanBit() ? 4 : 0);
        final int elementId = bitStream.readInt();
        this.m_commonData = ElementPropertiesLibrary.getElement(elementId);
        if (this.m_commonData == null) {
            ScreenElement.m_logger.error((Object)("Element of id " + elementId + " is missing"));
            return false;
        }
        return true;
    }
    
    public final boolean isOccluder() {
        return this.m_occluder;
    }
    
    public final boolean isAnimated() {
        return this.m_commonData.isAnimated();
    }
    
    public final short getCellZ() {
        return this.m_cellZ;
    }
    
    public final int getCellX() {
        return this.m_cellX;
    }
    
    public final int getCellY() {
        return this.m_cellY;
    }
    
    public final byte getHeight() {
        return this.m_height;
    }
    
    public final Point3 getCoordinates() {
        return new Point3(this.m_cellX, this.m_cellY, this.m_cellZ);
    }
    
    public final short getAltitude() {
        return (short)(this.m_cellZ - this.m_height);
    }
    
    public final ElementProperties getCommonProperties() {
        return this.m_commonData;
    }
    
    public int getGroupId() {
        return this.m_groupId;
    }
    
    public final int getGroupKey() {
        return this.m_groupKey;
    }
    
    public final short getLayerIndex() {
        return this.m_layerIndex;
    }
    
    public final boolean isGradient() {
        return (this.m_type & 0x4) == 0x4;
    }
    
    public final void getTeint(final float[] color) {
        assert color != null && color.length >= 3;
        if ((this.m_type & 0x1) != 0x1) {
            final int n = 0;
            final int n2 = 1;
            final int n3 = 2;
            final float n4 = 0.5f;
            color[n3] = n4;
            color[n] = (color[n2] = n4);
            return;
        }
        int index = getIndexTeint(this.m_type);
        color[0] = this.m_colors[index++];
        color[1] = this.m_colors[index++];
        color[2] = this.m_colors[index];
    }
    
    public final void getTeintAlpha(final float[] color) {
        assert color != null && color.length >= 4;
        this.getTeint(color);
        color[3] = this.getAlpha();
    }
    
    public final float getAlpha() {
        return ((this.m_type & 0x2) == 0x2) ? this.m_colors[getIndexAlpha(this.m_type)] : 1.0f;
    }
    
    public final void getTeint2(final float[] color) {
        assert (this.m_type & 0x4) == 0x4 : "impossible sur un objet non d\u00e9grad\u00e9. tester avec isGradient()";
        assert color != null && color.length >= 3;
        if ((this.m_type & 0x1) != 0x1) {
            final int n = 0;
            final int n2 = 1;
            final int n3 = 2;
            final float n4 = 0.5f;
            color[n3] = n4;
            color[n] = (color[n2] = n4);
            return;
        }
        int index = getIndexTeintGradient(this.m_type);
        color[0] = this.m_colors[index++];
        color[1] = this.m_colors[index++];
        color[2] = this.m_colors[index];
    }
    
    public final void getTeintAlpha2(final float[] color) {
        assert (this.m_type & 0x4) == 0x4 : "impossible sur un objet non d\u00e9grad\u00e9. tester avec isGradient()";
        assert color != null && color.length >= 4;
        this.getTeint2(color);
        color[3] = this.getAlpha();
    }
    
    public final float getAlpha2() {
        assert (this.m_type & 0x4) == 0x4 : "impossible sur un objet non d\u00e9grad\u00e9. tester avec isGradient()";
        return ((this.m_type & 0x2) == 0x2) ? this.m_colors[getIndexAlphaGradient(this.m_type)] : 1.0f;
    }
    
    public void computeColor(final float[] color) {
        assert color != null;
        assert color.length >= 4;
        this.getTeint(ScreenElement.m_tempColor);
        final int n = 0;
        color[n] *= ScreenElement.m_tempColor[0];
        final int n2 = 1;
        color[n2] *= ScreenElement.m_tempColor[1];
        final int n3 = 2;
        color[n3] *= ScreenElement.m_tempColor[2];
        final int n4 = 3;
        color[n4] *= this.getAlpha();
    }
    
    public final void save(final OutputBitStream bitStream) throws IOException {
        assert this.m_commonData != null;
        bitStream.writeShort(this.m_cellZ);
        bitStream.writeByte(this.m_height);
        bitStream.writeByte((byte)this.m_altitudeOrder);
        bitStream.writeBooleanBit(this.m_occluder);
        bitStream.writeBooleanBit((this.m_type & 0x1) == 0x1);
        bitStream.writeBooleanBit((this.m_type & 0x2) == 0x2);
        bitStream.writeBooleanBit((this.m_type & 0x4) == 0x4);
        bitStream.writeInt(this.m_commonData.getId());
    }
    
    public final void setWorldCoord(final int cellX, final int cellY, final short cellZ) {
        this.m_cellX = cellX;
        this.m_cellY = cellY;
        this.m_cellZ = cellZ;
    }
    
    public final void setScreenCoord(final int left, final int top) {
        this.m_left = left;
        this.m_top = top;
    }
    
    public void setOccluder(final boolean occluder) {
        this.m_occluder = occluder;
    }
    
    public final void setHeight(final int height) {
        this.m_height = (byte)height;
    }
    
    public final void setProperties(final ElementProperties elementProperties) {
        this.m_commonData = elementProperties;
    }
    
    public final void setAltitudeOrder(final int altitudeOrder) {
        this.m_altitudeOrder = altitudeOrder;
    }
    
    public int getAltitudeOrder() {
        return this.m_altitudeOrder;
    }
    
    public final void setLayerIndex(final short layerIndex) {
        this.m_layerIndex = layerIndex;
    }
    
    public void setGroupId(final int groupId) {
        this.m_groupId = groupId;
    }
    
    public final void setGroupKey(final int groupKey) {
        this.m_groupKey = groupKey;
    }
    
    public final void setTeint(final float r, final float g, final float b) {
        if ((this.m_type & 0x1) != 0x1) {
            return;
        }
        int index = getIndexTeint(this.m_type);
        this.m_colors[index++] = r;
        this.m_colors[index++] = g;
        this.m_colors[index] = b;
    }
    
    public final void setTeint(final float r, final float g, final float b, final float a) {
        if ((this.m_type & 0x1) == 0x1) {
            int index = getIndexTeint(this.m_type);
            this.m_colors[index++] = r;
            this.m_colors[index++] = g;
            this.m_colors[index] = b;
        }
        if ((this.m_type & 0x2) == 0x2) {
            this.m_colors[getIndexAlpha(this.m_type)] = a;
        }
    }
    
    public final void setAlpha(final float a) {
        if ((this.m_type & 0x2) == 0x2) {
            this.m_colors[getIndexAlpha(this.m_type)] = a;
        }
    }
    
    public final void setTeint2(final float r, final float g, final float b) {
        if ((this.m_type & 0x4) != 0x4) {
            return;
        }
        if ((this.m_type & 0x1) != 0x1) {
            return;
        }
        int index = getIndexTeint(this.m_type);
        this.m_colors[index++] = r;
        this.m_colors[index++] = g;
        this.m_colors[index] = b;
    }
    
    public final void setTeint2(final float r, final float g, final float b, final float a) {
        if ((this.m_type & 0x4) != 0x4) {
            return;
        }
        if ((this.m_type & 0x1) == 0x1) {
            int index = getIndexTeint(this.m_type);
            this.m_colors[index++] = r;
            this.m_colors[index++] = g;
            this.m_colors[index] = b;
        }
        if ((this.m_type & 0x2) == 0x2) {
            this.m_colors[getIndexAlpha(this.m_type)] = a;
        }
    }
    
    public final void setAlpha2(final float a) {
        if ((this.m_type & 0x4) != 0x4) {
            return;
        }
        if ((this.m_type & 0x2) == 0x2) {
            this.m_colors[getIndexAlpha(this.m_type)] = a;
        }
    }
    
    public void setCoordinates(final int cellX, final int cellY, final short cellZ) {
        this.m_cellX = cellX;
        this.m_cellY = cellY;
        this.m_cellZ = cellZ;
    }
    
    @Override
    protected void checkout() {
        this.m_hashCode = 0L;
        this.m_type = 0;
        this.m_cellZ = 0;
        final boolean b = false;
        this.m_cellY = (b ? 1 : 0);
        this.m_cellX = (b ? 1 : 0);
        final boolean b2 = false;
        this.m_left = (b2 ? 1 : 0);
        this.m_top = (b2 ? 1 : 0);
        this.m_altitudeOrder = 0;
        this.m_height = 0;
        this.m_groupKey = 0;
        this.m_layerIndex = 0;
        this.m_groupId = 0;
        this.m_occluder = false;
        this.m_hashCode = 0L;
    }
    
    @Override
    protected void checkin() {
        this.m_colors = null;
    }
    
    public static float getTeintValue(final byte value) {
        return value / 255.0f + 0.5f;
    }
    
    static void readColors(final float[] colors, final int type, final ExtendedDataInputStream bitStream) throws IOException {
        int i = 0;
        if ((type & 0x1) == 0x1) {
            assert i == getIndexTeint(type);
            colors[i++] = getTeintValue(bitStream.readByte());
            colors[i++] = getTeintValue(bitStream.readByte());
            colors[i++] = getTeintValue(bitStream.readByte());
        }
        if ((type & 0x2) == 0x2) {
            assert i == getIndexAlpha(type);
            colors[i++] = bitStream.readByte() / 255.0f + 0.5f;
        }
        if ((type & 0x4) == 0x4) {
            if ((type & 0x1) == 0x1) {
                assert i == getIndexTeintGradient(type);
                colors[i++] = bitStream.readByte() / 255.0f + 0.5f;
                colors[i++] = bitStream.readByte() / 255.0f + 0.5f;
                colors[i++] = bitStream.readByte() / 255.0f + 0.5f;
            }
            if ((type & 0x2) == 0x2) {
                assert i == getIndexAlphaGradient(type);
                colors[i++] = bitStream.readByte() / 255.0f + 0.5f;
            }
        }
        assert i == colors.length;
    }
    
    static float[] newColorsFromType(final int type) {
        int size = 0;
        size += (((type & 0x1) == 0x1) ? 3 : 0);
        size += (((type & 0x2) == 0x2) ? 1 : 0);
        size *= (((type & 0x4) == 0x4) ? 2 : 1);
        return new float[size];
    }
    
    static int getIndexTeint(final int type) {
        return ScreenElement.Indexes[type & 0x0];
    }
    
    static int getIndexAlpha(final int type) {
        return ScreenElement.Indexes[type & 0x1];
    }
    
    static int getIndexTeintGradient(final int type) {
        final int mask = type & 0x3;
        assert (mask & 0x1) == 0x1;
        return ScreenElement.Indexes[mask];
    }
    
    static int getIndexAlphaGradient(final int type) {
        final int mask = type & 0x3;
        assert (mask & 0x2) == 0x2;
        return ScreenElement.Indexes[mask + 4];
    }
    
    public void fillRectBounds(final Rect rect) {
        rect.set(this.m_left, this.m_left + this.m_commonData.getImgWidth(), this.m_top - this.m_commonData.getImgHeight(), this.m_top);
    }
    
    public void computeHashCode() {
        this.m_hashCode = LayerOrder.computeZOrder(this.m_cellX, this.m_cellY, this.m_altitudeOrder, 0);
    }
    
    public void computeLocation() {
        this.m_left = ScreenWorld.isoToScreenX(this.m_cellX, this.m_cellY) - this.m_commonData.getOriginX();
        this.m_top = ScreenWorld.isoToScreenY(this.m_cellX, this.m_cellY, this.m_cellZ - this.m_height) + this.m_commonData.getOriginY();
    }
    
    @Override
    public void removeReference() {
        super.removeReference();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScreenElement.class);
        Factory = new ObjectFactory();
        m_tempColor = new float[4];
        Indexes = new int[8];
        final int teintSize = 3;
        final int alphaSize = 1;
        ScreenElement.Indexes[0] = 0;
        for (int i = 1; i < 2; ++i) {
            ScreenElement.Indexes[i] = 3 + ScreenElement.Indexes[i - 1];
        }
        for (int i = 2; i < 4; ++i) {
            ScreenElement.Indexes[i] = 1 + ScreenElement.Indexes[i - 2];
        }
        for (int i = 4; i < ScreenElement.Indexes.length; ++i) {
            ScreenElement.Indexes[i] = ScreenElement.Indexes[i - 4];
            if ((i & 0x1) == 0x1) {
                final int[] indexes = ScreenElement.Indexes;
                final int n = i;
                indexes[n] += 3;
            }
        }
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<ScreenElement>
    {
        public ObjectFactory() {
            super(ScreenElement.class);
        }
        
        @Override
        public ScreenElement create() {
            return new ScreenElement(null);
        }
    }
}
