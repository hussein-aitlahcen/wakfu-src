package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmSpriteDefinitionSingleNoAction extends SpriteDefinition
{
    private final AnmTransformDataTable m_table;
    private AnmFrameData m_frameData;
    private short m_spriteId;
    
    public AnmSpriteDefinitionSingleNoAction(final AnmTransformDataTable table) {
        super();
        this.m_table = table;
    }
    
    @Override
    public final int getFrameCount() {
        return 1;
    }
    
    @Override
    public final int getShapeCount() {
        return 1;
    }
    
    @Override
    public final int getRealShapeCount() {
        return 1;
    }
    
    @Override
    public final void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        this.m_spriteId = bitStream.readShort();
        this.m_frameData = AnmFrameData.create(bitStream);
        this.m_maxSpriteCount = 1;
    }
    
    @Override
    public final void pushActions(final int frameIndex, final ArrayList<AnmAction> actions) {
        assert frameIndex == 0;
    }
    
    @Override
    public final short[] getShapesIds(final int frameIndex) {
        return new short[] { this.m_spriteId };
    }
    
    @Override
    public final boolean hasOnlyOneSprite() {
        return true;
    }
    
    @Override
    public final short firstSpriteId() {
        return this.m_spriteId;
    }
    
    @Override
    public final void nextSprite() {
    }
    
    @Override
    public final int beginProcessFrame(final int index) {
        this.m_frameData.begin(0);
        return 1;
    }
    
    @Override
    public final short process(final AnmTransform parentTransform, final AnmTransform result) {
        final int type = this.m_frameData.read();
        AnmFrameProcessor.getFromType(type).readAndProcess(this.m_frameData, this.m_table, parentTransform, result);
        return this.m_spriteId;
    }
}
