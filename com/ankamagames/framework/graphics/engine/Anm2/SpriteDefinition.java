package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.*;
import java.io.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public abstract class SpriteDefinition
{
    private static final Logger m_logger;
    public static final int LOOP = 128;
    public static final int HAS_NAME = 64;
    public static final int COLOR_MASK = 63;
    static final byte SINGLE = 1;
    static final byte SINGLE_NO_ACTION = 2;
    static final byte SINGLE_FRAME = 3;
    static final byte FRAMES = 4;
    short m_id;
    String m_name;
    int m_nameCRC;
    int m_baseNameCRC;
    byte m_flags;
    private boolean m_isAnimationNode;
    int m_maxSpriteCount;
    
    public SpriteDefinition() {
        super();
        this.m_maxSpriteCount = -1;
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public final int getNameCRC() {
        return this.m_nameCRC;
    }
    
    public final boolean isLoop() {
        return (this.m_flags & 0x80) != 0x0;
    }
    
    public final int getColorIndex() {
        return this.m_flags & 0x3F;
    }
    
    public final boolean isAnimationNode() {
        return this.m_isAnimationNode;
    }
    
    protected final void setIsAnimationNode() {
        this.m_isAnimationNode = true;
    }
    
    @Override
    public final String toString() {
        return new StringBuilder(this.m_id).append(" [").append(this.m_name).append("]").toString();
    }
    
    public abstract int getFrameCount();
    
    public abstract int getShapeCount();
    
    public abstract int getRealShapeCount();
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_id = bitStream.readShort();
        this.m_flags = bitStream.readByte();
        if ((this.m_flags & 0x40) != 0x0) {
            this.m_name = bitStream.readString();
            this.m_nameCRC = Engine.getPartName(this.m_name);
            final int index = this.m_name.indexOf(95) + 1;
            this.m_baseNameCRC = Engine.getPartName(this.m_name.substring(index));
        }
        else {
            final boolean b = false;
            this.m_baseNameCRC = (b ? 1 : 0);
            this.m_nameCRC = (b ? 1 : 0);
        }
        bitStream.readInt();
        bitStream.readInt();
    }
    
    public abstract void pushActions(final int p0, final ArrayList<AnmAction> p1);
    
    public abstract short[] getShapesIds(final int p0);
    
    public abstract boolean hasOnlyOneSprite();
    
    public abstract short firstSpriteId();
    
    public abstract int beginProcessFrame(final int p0);
    
    public abstract void nextSprite();
    
    public abstract short process(final AnmTransform p0, final AnmTransform p1);
    
    public static SpriteDefinition createFrom(final AnmTransformDataTable table, final ExtendedDataInputStream bitStream, final boolean optimized) throws IOException {
        if (table == null) {
            final SpriteDefinition def = new AnmSpriteDefinition(optimized);
            return def;
        }
        final byte type = bitStream.readByte();
        switch (type) {
            case 1: {
                return new AnmSpriteDefinitionSingle(table);
            }
            case 2: {
                return new AnmSpriteDefinitionSingleNoAction(table);
            }
            case 3: {
                return new AnmSpriteDefinitionSingleFrame(table);
            }
            case 4: {
                return new AnmSpriteDefinitionIndexed(table);
            }
            default: {
                SpriteDefinition.m_logger.error((Object)("type incoonu " + type));
                return null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpriteDefinition.class);
    }
}
