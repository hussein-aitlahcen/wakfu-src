package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmSpriteDefinitionIndexed extends SpriteDefinition
{
    private static final Logger m_logger;
    private final AnmTransformDataTable m_table;
    private int[] m_framePos;
    private AnmFrameData m_frameData;
    private short[] m_spriteInfo;
    private short[] m_actionInfo;
    protected int m_currentSprite;
    
    public AnmSpriteDefinitionIndexed(final AnmTransformDataTable table) {
        super();
        this.m_currentSprite = -1;
        this.m_table = table;
    }
    
    @Override
    public final int getFrameCount() {
        return this.m_framePos.length / ((this.m_actionInfo == null) ? 2 : 3);
    }
    
    @Override
    public int getShapeCount() {
        int count = 0;
        for (int i = 0; i < this.m_spriteInfo.length; i += this.m_spriteInfo[i], ++i) {
            count += this.m_spriteInfo[i];
        }
        return count;
    }
    
    @Override
    public int getRealShapeCount() {
        return 0;
    }
    
    @Override
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        int count = bitStream.readShort() & 0xFFFF;
        this.m_framePos = bitStream.readInts(count);
        count = (bitStream.readShort() & 0xFFFF);
        this.m_spriteInfo = bitStream.readShorts(count);
        count = (bitStream.readShort() & 0xFFFF);
        this.m_actionInfo = (short[])((count == 0) ? null : bitStream.readShorts(count));
        this.m_frameData = AnmFrameData.create(bitStream);
        for (int i = 0; i < this.m_spriteInfo.length; i += this.m_spriteInfo[i], ++i) {
            if (this.m_spriteInfo[i] > this.m_maxSpriteCount) {
                this.m_maxSpriteCount = this.m_spriteInfo[i];
            }
        }
    }
    
    @Override
    public void pushActions(final int frameIndex, final ArrayList<AnmAction> actions) {
        if (this.m_actionInfo == null) {
            return;
        }
        int start = this.m_framePos[frameIndex * 3 + 2];
        final short actionCount = this.m_actionInfo[start];
        if (actionCount == 0) {
            return;
        }
        final AnmAction[] tblActions = this.m_table.m_actions;
        for (int end = ++start + actionCount, i = start; i < end; ++i) {
            final AnmAction action = tblActions[this.m_actionInfo[i]];
            action.setFrameIndex(frameIndex);
            actions.add(action);
        }
    }
    
    @Override
    public short[] getShapesIds(final int frameIndex) {
        final int cur = this.m_framePos[frameIndex * 2 + 1];
        final short[] ids = new short[this.m_spriteInfo[cur]];
        final int offset = cur + 1;
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = this.m_spriteInfo[offset + i];
        }
        return ids;
    }
    
    @Override
    public final boolean hasOnlyOneSprite() {
        return false;
    }
    
    @Override
    public final short firstSpriteId() {
        throw new UnsupportedOperationException("devrait etre du type AnmSpriteDefinitionSingle");
    }
    
    @Override
    public final void nextSprite() {
        ++this.m_currentSprite;
    }
    
    @Override
    public final int beginProcessFrame(final int index) {
        final int i = index * ((this.m_actionInfo == null) ? 2 : 3);
        this.m_frameData.begin(this.m_framePos[i]);
        this.m_currentSprite = this.m_framePos[i + 1];
        return this.m_spriteInfo[this.m_currentSprite];
    }
    
    @Override
    public final short process(final AnmTransform parentTransform, final AnmTransform result) {
        final int type = this.m_frameData.read();
        AnmFrameProcessor.getFromType(type).readAndProcess(this.m_frameData, this.m_table, parentTransform, result);
        return this.m_spriteInfo[this.m_currentSprite];
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmSpriteDefinitionIndexed.class);
    }
}
