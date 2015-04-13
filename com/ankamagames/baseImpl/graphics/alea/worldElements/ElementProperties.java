package com.ankamagames.baseImpl.graphics.alea.worldElements;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.sun.opengl.util.texture.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class ElementProperties
{
    private static final Logger m_logger;
    private static final short FLIP_MASK = 16;
    private static final short MOVE_TOP_MASK = 32;
    private static final short BEFORE_MOBILE_MASK = 64;
    private static final short WALKABLE_MASK = 128;
    private static final short SLOPE_MASK = 15;
    public static final byte EXPORT_MASK = 1;
    public static final byte EXPORT_AGT_MASK = 2;
    private final int m_id;
    private final short m_originX;
    private final short m_originY;
    private final short m_imgWidth;
    private final short m_imgHeight;
    private final int m_gfxId;
    private final byte m_visualHeight;
    private final byte m_visibilityMask;
    private final byte m_exportMask;
    private final byte m_shader;
    protected final AnimData m_animData;
    private final TextureCoords m_textureCoords;
    private byte m_groundSoundType;
    private final byte m_propertiesFlag;
    
    public ElementProperties(final int id, final short originX, final short originY, final short imgWidth, final short imgHeight, final int gfxId, final boolean flip, final byte slope, final byte visualHeight, final boolean moveTop, final boolean beforeMobile, final boolean walkable, final byte visibilityMask, final byte exportMask, final byte shaderId, final AnimData animData, final byte groundSoundType) {
        super();
        this.m_id = id;
        this.m_originX = originX;
        this.m_originY = originY;
        this.m_imgWidth = imgWidth;
        this.m_imgHeight = imgHeight;
        this.m_gfxId = gfxId;
        this.m_visualHeight = visualHeight;
        assert slope <= 15;
        byte propertiesFlag = (byte)(slope | (flip ? 16 : 0));
        propertiesFlag |= (byte)(moveTop ? 32 : 0);
        propertiesFlag |= (byte)(beforeMobile ? 64 : 0);
        propertiesFlag |= (byte)(walkable ? 128 : 0);
        this.m_propertiesFlag = propertiesFlag;
        this.m_visibilityMask = visibilityMask;
        this.m_exportMask = exportMask;
        this.m_shader = shaderId;
        this.m_animData = animData;
        this.m_textureCoords = ((this.m_animData == null) ? computeTextureCoords(this.m_imgWidth, this.m_imgHeight, this.isFlip()) : null);
        this.m_groundSoundType = groundSoundType;
    }
    
    private static TextureCoords computeTextureCoords(final int imgWidth, final int imgHeight, final boolean flip) {
        final float width = MathHelper.nearestGreatestPowOfTwo(imgWidth);
        final float height = MathHelper.nearestGreatestPowOfTwo(imgHeight) - 0.5f;
        final float right = imgWidth / width;
        final float bottom = imgHeight / height;
        if (flip) {
            return new TextureCoords(right, bottom, 0.0f, 0.0f);
        }
        return new TextureCoords(0.0f, bottom, right, 0.0f);
    }
    
    ElementProperties(final ByteBuffer buffer) {
        super();
        this.m_id = buffer.getInt();
        this.m_originX = buffer.getShort();
        this.m_originY = buffer.getShort();
        this.m_imgWidth = buffer.getShort();
        this.m_imgHeight = buffer.getShort();
        this.m_gfxId = buffer.getInt();
        this.m_propertiesFlag = buffer.get();
        this.m_visualHeight = buffer.get();
        this.m_visibilityMask = buffer.get();
        this.m_exportMask = buffer.get();
        this.m_shader = buffer.get();
        final boolean flipped = this.isFlip();
        this.m_animData = AnimData.Use.fromBuffer(buffer, flipped);
        this.m_textureCoords = ((this.m_animData == null) ? computeTextureCoords(this.m_imgWidth, this.m_imgHeight, flipped) : null);
        this.m_groundSoundType = buffer.get();
    }
    
    public void save(final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_id);
        ostream.writeShort(this.m_originX);
        ostream.writeShort(this.m_originY);
        ostream.writeShort(this.m_imgWidth);
        ostream.writeShort(this.m_imgHeight);
        ostream.writeInt(this.m_gfxId);
        ostream.writeByte(this.m_propertiesFlag);
        ostream.writeByte(this.m_visualHeight);
        ostream.writeByte(this.m_visibilityMask);
        ostream.writeByte(this.m_exportMask);
        ostream.writeByte(this.m_shader);
        if (this.m_animData == null) {
            ostream.writeByte((byte)0);
        }
        else {
            this.m_animData.write(ostream);
        }
        ostream.writeByte(this.m_groundSoundType);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getOriginX() {
        return this.m_originX;
    }
    
    public int getOriginY() {
        return this.m_originY;
    }
    
    public int getImgWidth() {
        return this.m_imgWidth;
    }
    
    public int getImgHeight() {
        return this.m_imgHeight;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public boolean isFlip() {
        return (this.m_propertiesFlag & 0x10) == 0x10;
    }
    
    public byte getSlope() {
        return (byte)(this.m_propertiesFlag & 0xF);
    }
    
    public int getVisualHeight() {
        return this.m_visualHeight;
    }
    
    public boolean isMoveTop() {
        return (this.m_propertiesFlag & 0x20) == 0x20;
    }
    
    public byte getGroundSoundType() {
        return this.m_groundSoundType;
    }
    
    public boolean isBeforeMobile() {
        return (this.m_propertiesFlag & 0x40) == 0x40;
    }
    
    public boolean isWalkable() {
        return (this.m_propertiesFlag & 0x80) == 0x80;
    }
    
    public byte getVisibilityMask() {
        return this.m_visibilityMask;
    }
    
    public byte getExportMask() {
        return this.m_exportMask;
    }
    
    public byte getShaderId() {
        return this.m_shader;
    }
    
    public boolean isAnimated() {
        return this.m_animData != null;
    }
    
    public TextureCoords getTextureCoordinates(final short animationTime) {
        if (this.m_textureCoords == null) {
            return this.m_animData.getTextureCoordinates(animationTime);
        }
        assert this.m_animData == null;
        return this.m_textureCoords;
    }
    
    public static ElementProperties createDefault(final int id, final boolean empty, final boolean walkable) {
        return new ElementProperties(id, (short)0, (short)0, (short)0, (short)0, 0, false, (byte)0, (byte)0, empty, false, walkable, (byte)0, (byte)0, (byte)0, null, (byte)0);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ElementProperties.class);
    }
}
