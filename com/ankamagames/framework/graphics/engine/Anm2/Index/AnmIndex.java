package com.ankamagames.framework.graphics.engine.Anm2.Index;

import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.util.*;
import gnu.trove.*;

public final class AnmIndex
{
    public static final int UNKNOW_HEIGHT = -1;
    private static final int HAS_SCALE = 1;
    private static final int HAS_EXTENSION = 2;
    private static final int HAS_HIDDING_PART = 4;
    private static final int HAS_RENDER_RADIUS = 8;
    private static final int OVERRIDE_FLIP = 16;
    private static final int PERFECT_HIT_TEST = 32;
    private static final int CAN_HIDE_PART = 64;
    private static final int EXTENDED = 128;
    private byte m_flags;
    private float m_scale;
    private float m_renderRadius;
    private String[] m_fileNames;
    private AnmAnimationFileRecord[] m_animationFileRecords;
    private final THashMap<String, AnmAnimationFileRecord> m_animationFileRecordsByName;
    private HiddingPart[] m_partHiddenByItem;
    private CanHidePart[] m_canHidePartItem;
    private AnmIndexExtend m_extend;
    
    public AnmIndex() {
        super();
        this.m_animationFileRecordsByName = new THashMap<String, AnmAnimationFileRecord>();
        this.clear();
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_flags = bitStream.readByte();
        if (this.hasScale()) {
            this.m_scale = bitStream.readFloat();
        }
        if (this.hasRenderRadius()) {
            this.m_renderRadius = bitStream.readFloat();
        }
        if (this.hasExtension()) {
            final int numFiles = bitStream.readShort() & 0xFFFF;
            this.m_fileNames = new String[numFiles];
            for (int i = 0; i < numFiles; ++i) {
                this.m_fileNames[i] = bitStream.readString();
            }
        }
        if (this.hasHiddingPart()) {
            final int numPart = bitStream.readByte() & 0xFF;
            this.m_partHiddenByItem = new HiddingPart[numPart];
            for (int i = 0; i < numPart; ++i) {
                final int crcKey = bitStream.readInt();
                final int crcToHide = bitStream.readInt();
                this.m_partHiddenByItem[i] = new HiddingPart(crcKey, crcToHide);
            }
        }
        if (this.canHidePart()) {
            final int numPart = bitStream.readByte() & 0xFF;
            this.m_canHidePartItem = new CanHidePart[numPart];
            for (int i = 0; i < numPart; ++i) {
                final String itemName = bitStream.readString();
                final int crcKey2 = bitStream.readInt();
                this.m_canHidePartItem[i] = new CanHidePart(itemName, crcKey2);
            }
        }
        if (this.isExtended()) {
            this.m_extend = new AnmIndexExtend(bitStream);
        }
        final int numAnimationFileRecords = bitStream.readShort() & 0xFFFF;
        this.m_animationFileRecords = new AnmAnimationFileRecord[numAnimationFileRecords];
        this.m_animationFileRecordsByName.ensureCapacity(numAnimationFileRecords);
        for (int i = 0; i < this.m_animationFileRecords.length; ++i) {
            final AnmAnimationFileRecord fileRecord = new AnmAnimationFileRecord();
            (this.m_animationFileRecords[i] = fileRecord).load(bitStream);
            this.m_animationFileRecordsByName.put(fileRecord.m_name, fileRecord);
        }
    }
    
    public void save(final OutputBitStream bitStream) throws IOException {
        bitStream.writeByte(this.m_flags);
        if (this.hasScale()) {
            bitStream.writeFloat(this.m_scale);
        }
        if (this.hasRenderRadius()) {
            bitStream.writeFloat(this.m_renderRadius);
        }
        if (this.hasExtension()) {
            bitStream.writeShort((short)this.m_fileNames.length);
            for (int i = 0; i < this.m_fileNames.length; ++i) {
                bitStream.writeString(this.m_fileNames[i]);
            }
        }
        if (this.hasHiddingPart()) {
            bitStream.writeByte((byte)this.m_partHiddenByItem.length);
            for (int i = 0; i < this.m_partHiddenByItem.length; ++i) {
                final HiddingPart part = this.m_partHiddenByItem[i];
                bitStream.writeInt(part.crcKey);
                bitStream.writeInt(part.crcToHide);
            }
        }
        if (this.canHidePart()) {
            bitStream.writeByte((byte)this.m_canHidePartItem.length);
            for (int i = 0; i < this.m_canHidePartItem.length; ++i) {
                final CanHidePart part2 = this.m_canHidePartItem[i];
                bitStream.writeString(part2.itemName);
                bitStream.writeInt(part2.crcKey);
            }
        }
        if (this.isExtended()) {
            this.m_extend.save(bitStream);
        }
        if (this.m_animationFileRecords == null) {
            bitStream.writeShort((short)0);
        }
        else {
            bitStream.writeShort((short)this.m_animationFileRecords.length);
            for (int i = 0; i < this.m_animationFileRecords.length; ++i) {
                this.m_animationFileRecords[i].save(bitStream);
            }
        }
    }
    
    public float[] getHighlightColor() {
        return (this.m_extend == null) ? AnmIndexExtend.DEFAULT_HIGHLIGHT_COLOR : this.m_extend.getHighlightColor();
    }
    
    public int getAnimHeight(final String animName) {
        if (this.m_extend == null) {
            return -1;
        }
        return this.m_extend.getHeight(animName);
    }
    
    public AnmAnimationFileRecord getAnimationFileRecord(final String animName) {
        return this.m_animationFileRecordsByName.get(animName);
    }
    
    public String getFileName(final int fileIndex) {
        return this.m_fileNames[fileIndex];
    }
    
    public AnmAnimationFileRecord[] getAnimationFileRecords() {
        return this.m_animationFileRecords;
    }
    
    public String[] getFileNames() {
        return this.m_fileNames;
    }
    
    public HiddingPart[] getPartsHiddenByItem() {
        return this.m_partHiddenByItem;
    }
    
    public float getScale() {
        return this.m_scale;
    }
    
    public float getRenderRadius() {
        return this.m_renderRadius;
    }
    
    private boolean hasScale() {
        return (this.m_flags & 0x1) != 0x0;
    }
    
    private boolean hasExtension() {
        return (this.m_flags & 0x2) != 0x0;
    }
    
    public boolean hasRenderRadius() {
        return (this.m_flags & 0x8) != 0x0;
    }
    
    public boolean hasHiddingPart() {
        return (this.m_flags & 0x4) != 0x0;
    }
    
    public boolean useFlip() {
        return (this.m_flags & 0x10) == 0x0;
    }
    
    public boolean usePerfectHitTest() {
        return (this.m_flags & 0x20) == 0x20;
    }
    
    public boolean canHidePart() {
        return (this.m_flags & 0x40) != 0x0;
    }
    
    private boolean isExtended() {
        return (this.m_flags & 0x80) != 0x0;
    }
    
    public void clear() {
        this.m_flags = 0;
        this.m_scale = 1.0f;
        this.m_renderRadius = 1.0f;
        this.m_fileNames = null;
        this.m_animationFileRecords = null;
        this.m_animationFileRecordsByName.clear();
        this.m_partHiddenByItem = null;
        this.m_canHidePartItem = null;
        this.m_extend = null;
    }
    
    public void setFileNames(final ArrayList<String> extensions) {
        if (extensions == null || extensions.isEmpty()) {
            return;
        }
        this.m_flags |= 0x2;
        this.m_fileNames = extensions.toArray(new String[extensions.size()]);
    }
    
    public void setHiddingPart(final ArrayList<HiddingPart> hiddingPart) {
        if (hiddingPart == null || hiddingPart.isEmpty()) {
            return;
        }
        this.m_flags |= 0x4;
        hiddingPart.toArray(this.m_partHiddenByItem = new HiddingPart[hiddingPart.size()]);
    }
    
    public void setCanHidePart(final ArrayList<CanHidePart> canHidePart) {
        if (canHidePart == null || canHidePart.isEmpty()) {
            return;
        }
        this.m_flags |= 0x40;
        canHidePart.toArray(this.m_canHidePartItem = new CanHidePart[canHidePart.size()]);
    }
    
    public void setScale(final float scale) {
        if (scale != 1.0f) {
            this.m_flags |= 0x1;
            this.m_scale = scale;
        }
    }
    
    public void setRenderRadius(final float renderRadius) {
        if (renderRadius != 1.0f) {
            this.m_flags |= 0x8;
            this.m_renderRadius = renderRadius;
        }
    }
    
    public void setOverrideFlip(final boolean flip) {
        if (flip) {
            this.m_flags |= 0x10;
        }
        else {
            this.m_flags &= 0xFFFFFFEF;
        }
    }
    
    public void setAnimationFileRecord(final AnmAnimationFileRecord[] fileRecords) {
        this.m_animationFileRecords = fileRecords;
    }
    
    public void setUsePerfectHitTest() {
        this.m_flags |= 0x20;
    }
    
    public CanHidePart[] getCanHideParts() {
        return this.m_canHidePartItem;
    }
    
    public void setAnimationHeights(final TObjectByteHashMap<String> animationHeight) {
        if (animationHeight.isEmpty()) {
            return;
        }
        this.m_flags |= (byte)128;
        (this.m_extend = new AnmIndexExtend()).setAnimationHeights(animationHeight);
    }
    
    public void setHighlightColor(final float[] highlightColor) {
        if (highlightColor == null) {
            return;
        }
        this.m_flags |= (byte)128;
        (this.m_extend = new AnmIndexExtend()).setHighlightColor(highlightColor);
    }
}
