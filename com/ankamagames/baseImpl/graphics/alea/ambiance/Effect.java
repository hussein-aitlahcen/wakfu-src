package com.ankamagames.baseImpl.graphics.alea.ambiance;

import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.effects.*;
import java.nio.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public final class Effect
{
    private final int m_id;
    private AbstractBlock[] m_blockDefinitions;
    private ArrayList<EffectBlock> m_effectBlocks;
    
    public Effect(final int id) {
        super();
        this.m_effectBlocks = new ArrayList<EffectBlock>();
        this.m_id = id;
    }
    
    public final int getId() {
        return this.m_id;
    }
    
    public final void unserialize(final ByteBuffer buffer, final EffectBlockFactory factory) throws Exception {
        this.m_effectBlocks.clear();
        final int count = buffer.getShort() & 0xFFFF;
        this.m_blockDefinitions = new AbstractBlock[count];
        for (int i = 0; i < count; ++i) {
            final int typeId = buffer.getShort() & 0xFFFF;
            final AbstractBlock block = factory.createBlock(typeId, buffer);
            this.m_blockDefinitions[i] = block;
            if (block.getBlockType() == BlockType.Effect) {
                this.m_effectBlocks.add((EffectBlock)block);
            }
        }
        this.m_effectBlocks.trimToSize();
        for (int i = 0; i < this.m_blockDefinitions.length; ++i) {
            final AbstractBlock block2 = this.m_blockDefinitions[i];
            final int expectedInputCount = block2.getExpectedInputCount();
            if (expectedInputCount != 0) {
                final EffectBlockParameter[] params = new EffectBlockParameter[expectedInputCount];
                for (int p = 0; p < params.length; ++p) {
                    final int linkedId = buffer.getShort() & 0xFFFF;
                    params[p] = (EffectBlockParameter)this.m_blockDefinitions[linkedId];
                }
                block2.setInputs(params);
            }
        }
        for (int activatorsCount = buffer.getShort() & 0xFFFF, j = 0; j < activatorsCount; ++j) {
            final int effectBlockId = buffer.getShort() & 0xFFFF;
            final int activatorId = buffer.getShort() & 0xFFFF;
            final EffectBlock effectBlock = (EffectBlock)this.m_blockDefinitions[effectBlockId];
            effectBlock.setActivator((EffectBlockParameter)this.m_blockDefinitions[activatorId]);
        }
    }
    
    public final void update(final int deltaTime) {
        for (int i = 0; i < this.m_blockDefinitions.length; ++i) {
            this.m_blockDefinitions[i].update(deltaTime);
        }
        for (int i = 0; i < this.m_effectBlocks.size(); ++i) {
            this.m_effectBlocks.get(i).compute();
        }
    }
    
    ArrayList<EffectBlock> getEffectBlocks() {
        return this.m_effectBlocks;
    }
}
