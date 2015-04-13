package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmSpriteDefinitionSingleFrame extends SpriteDefinition
{
    private static final short[] EMPTY;
    private static final Logger m_logger;
    private final AnmTransformDataTable m_table;
    private AnmFrameData m_frameData;
    private short[] m_actionInfo;
    private short[] m_spriteIds;
    protected int m_currentSprite;
    
    public AnmSpriteDefinitionSingleFrame(final AnmTransformDataTable table) {
        super();
        this.m_currentSprite = -1;
        this.m_table = table;
    }
    
    @Override
    public final int getFrameCount() {
        return 1;
    }
    
    @Override
    public final int getShapeCount() {
        return this.m_spriteIds.length;
    }
    
    @Override
    public final int getRealShapeCount() {
        return this.m_spriteIds.length;
    }
    
    @Override
    public final void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        final int spriteCount = bitStream.readShort() & 0xFFFF;
        this.m_spriteIds = ((spriteCount == 0) ? AnmSpriteDefinitionSingleFrame.EMPTY : bitStream.readShorts(spriteCount));
        final int actionCount = bitStream.readShort() & 0xFFFF;
        this.m_actionInfo = (short[])((actionCount == 0) ? null : bitStream.readShorts(actionCount));
        this.m_frameData = AnmFrameData.create(bitStream);
        if (spriteCount == 0 && actionCount != 0) {
            this.setIsAnimationNode();
        }
        this.m_maxSpriteCount = spriteCount;
    }
    
    @Override
    public final void pushActions(final int frameIndex, final ArrayList<AnmAction> actions) {
        assert frameIndex == 0;
        if (this.m_actionInfo == null) {
            return;
        }
        final AnmAction[] tblActions = this.m_table.m_actions;
        for (int i = 0; i < this.m_actionInfo.length; ++i) {
            final AnmAction action = tblActions[this.m_actionInfo[i]];
            action.setFrameIndex(frameIndex);
            actions.add(action);
        }
    }
    
    @Override
    public final short[] getShapesIds(final int frameIndex) {
        assert frameIndex == 0;
        return this.m_spriteIds;
    }
    
    @Override
    public final boolean hasOnlyOneSprite() {
        assert this.m_spriteIds.length != 1;
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
        this.m_frameData.begin(0);
        this.m_currentSprite = -1;
        return this.m_spriteIds.length;
    }
    
    @Override
    public final short process(final AnmTransform parentTransform, final AnmTransform result) {
        final int type = this.m_frameData.read();
        AnmFrameProcessor.getFromType(type).readAndProcess(this.m_frameData, this.m_table, parentTransform, result);
        return this.m_spriteIds[this.m_currentSprite];
    }
    
    static {
        EMPTY = new short[0];
        m_logger = Logger.getLogger((Class)AnmSpriteDefinitionSingleFrame.class);
    }
}
