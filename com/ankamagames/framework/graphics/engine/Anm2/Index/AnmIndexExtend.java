package com.ankamagames.framework.graphics.engine.Anm2.Index;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;
import gnu.trove.*;

public class AnmIndexExtend
{
    public static final float[] DEFAULT_HIGHLIGHT_COLOR;
    private static final int ANIM_HEIGHT = 1;
    private static final int HAS_HIGHLIGHT_BOOST = 2;
    private int m_flags;
    private final TIntByteHashMap m_heights;
    private float[] m_highLightColor;
    
    AnmIndexExtend(final ExtendedDataInputStream bitStream) {
        super();
        this.m_heights = new TIntByteHashMap(1, 1.0f);
        this.m_highLightColor = AnmIndexExtend.DEFAULT_HIGHLIGHT_COLOR;
        this.m_flags = bitStream.readInt();
        if (this.hasAnimHeight()) {
            final int count = bitStream.readShort() & 0xFFFF;
            this.m_heights.ensureCapacity(count);
            for (int i = 0; i < count; ++i) {
                this.m_heights.put(bitStream.readInt(), (byte)(bitStream.readByte() + 1));
            }
        }
        if (this.hasHighlightBoost()) {
            this.m_highLightColor = bitStream.readFloats(3);
        }
    }
    
    AnmIndexExtend() {
        super();
        this.m_heights = new TIntByteHashMap(1, 1.0f);
        this.m_highLightColor = AnmIndexExtend.DEFAULT_HIGHLIGHT_COLOR;
    }
    
    void save(final OutputBitStream bitStream) throws IOException {
        bitStream.writeInt(this.m_flags);
        if (this.hasAnimHeight()) {
            bitStream.writeShort((short)this.m_heights.size());
            final TIntByteIterator it = this.m_heights.iterator();
            while (it.hasNext()) {
                it.advance();
                bitStream.writeInt(it.key());
                bitStream.writeByte(it.value());
            }
        }
        if (this.hasHighlightBoost()) {
            bitStream.writeFloat(this.m_highLightColor[0]);
            bitStream.writeFloat(this.m_highLightColor[1]);
            bitStream.writeFloat(this.m_highLightColor[2]);
        }
    }
    
    private boolean hasAnimHeight() {
        return (this.m_flags & 0x1) != 0x0;
    }
    
    private boolean hasHighlightBoost() {
        return (this.m_flags & 0x2) == 0x2;
    }
    
    int getHeight(final String animName) {
        return this.m_heights.get(animName.hashCode()) - 1;
    }
    
    public void setAnimationHeights(final TObjectByteHashMap<String> animationHeight) {
        this.m_flags |= 0x1;
        animationHeight.forEachEntry(new TObjectByteProcedure<String>() {
            @Override
            public boolean execute(final String animName, final byte height) {
                AnmIndexExtend.this.m_heights.put(animName.hashCode(), height);
                return true;
            }
        });
    }
    
    public float[] getHighlightColor() {
        return this.m_highLightColor;
    }
    
    public void setHighlightColor(final float[] color) {
        assert this.m_highLightColor == AnmIndexExtend.DEFAULT_HIGHLIGHT_COLOR;
        if (!Arrays.equals(color, AnmIndexExtend.DEFAULT_HIGHLIGHT_COLOR)) {
            this.m_flags |= 0x2;
            this.m_highLightColor = color;
        }
    }
    
    static {
        DEFAULT_HIGHLIGHT_COLOR = new float[] { 0.1f, 0.1f, 0.1f };
    }
}
