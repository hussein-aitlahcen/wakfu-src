package com.ankamagames.baseImpl.graphics.alea.ambiance;

import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.effects.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

class EffectMixer
{
    protected static final Logger m_logger;
    private static final TObjectProcedure<EffectData> CLEAR_PROCEDURE;
    private static final TObjectProcedure<EffectData> RESET_PROCEDURE;
    private final TObjectProcedure<EffectData> procedure;
    private final TIntObjectHashMap<EffectData> m_blocks;
    private final ArrayList<EffectApplyer> m_resultBlocks;
    private final TIntObjectHashMap<ArrayList<EffectBlock>> _countByType;
    
    EffectMixer() {
        super();
        this.procedure = new TObjectProcedure<EffectData>() {
            @Override
            public boolean execute(final EffectData object) {
                try {
                    object.combine();
                    object.updateState();
                    if (object.isActive()) {
                        EffectMixer.this.m_resultBlocks.add(object.m_result);
                    }
                }
                catch (Exception e) {
                    EffectMixer.m_logger.error((Object)"", (Throwable)e);
                }
                object.clear();
                return true;
            }
        };
        this.m_blocks = new TIntObjectHashMap<EffectData>();
        this.m_resultBlocks = new ArrayList<EffectApplyer>();
        this._countByType = new TIntObjectHashMap<ArrayList<EffectBlock>>();
    }
    
    void init(final EffectBlockFactory effectBlockFactory, final AleaWorldScene scene) {
        assert this.m_blocks.size() == 0;
        final AbstractModel[] arr$;
        final AbstractModel[] models = arr$ = effectBlockFactory.getAllModels();
        for (final AbstractModel model : arr$) {
            if (model.getBlockType() == BlockType.Effect) {
                final AbstractEffectModel effectModel = (AbstractEffectModel)model;
                final EffectApplyer effectBlock = effectModel.createApplyer();
                final int typeId = model.getTypeId();
                this.m_blocks.put(typeId, new EffectData(effectBlock));
                effectBlock.init(scene);
                this._countByType.put(typeId, new ArrayList<EffectBlock>());
            }
        }
    }
    
    ArrayList<EffectApplyer> process(final ArrayList<EffectContext> toProcess) {
        this.m_resultBlocks.clear();
        if (toProcess.isEmpty()) {
            return this.m_resultBlocks;
        }
        int lastAmbianceId = -1;
        for (int i = 0, size = toProcess.size(); i < size; ++i) {
            final EffectContext effectContext = toProcess.get(i);
            if (effectContext.isActive()) {
                final float effectStrength = effectContext.getStrength();
                if (lastAmbianceId != effectContext.getFromAmbianceId()) {
                    lastAmbianceId = effectContext.getFromAmbianceId();
                    this.computeStrengths();
                }
                final ArrayList<EffectBlock> effectBlocks = effectContext.m_effect.getEffectBlocks();
                for (int effectBlockSize = effectBlocks.size(), e = 0; e < effectBlockSize; ++e) {
                    final EffectBlock block = effectBlocks.get(e);
                    if (block.isActive()) {
                        this.insertBlock(block, effectStrength);
                    }
                }
            }
        }
        this.computeStrengths();
        this.m_blocks.forEachValue(this.procedure);
        return this.m_resultBlocks;
    }
    
    private void insertBlock(final EffectBlock block, final float coeff) {
        final int typeId = block.getTypeId();
        block.setCoeff(coeff);
        this.m_blocks.get(typeId).add(block);
        this._countByType.get(typeId).add(block);
    }
    
    private void computeStrengths() {
        this._countByType.forEachValue(new TObjectProcedure<ArrayList<EffectBlock>>() {
            @Override
            public boolean execute(final ArrayList<EffectBlock> list) {
                final int size = list.size();
                final float c = 1.0f / size;
                for (int i = 0; i < size; ++i) {
                    list.get(i).multCoeff(c);
                }
                list.clear();
                return true;
            }
        });
    }
    
    public final void clear() {
        this.m_blocks.forEachValue(EffectMixer.CLEAR_PROCEDURE);
    }
    
    public final void reset() {
        this.m_blocks.forEachValue(EffectMixer.RESET_PROCEDURE);
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectMixer.class);
        CLEAR_PROCEDURE = new TObjectProcedure<EffectData>() {
            @Override
            public boolean execute(final EffectData object) {
                object.clear();
                object.updateState();
                return true;
            }
        };
        RESET_PROCEDURE = new TObjectProcedure<EffectData>() {
            @Override
            public boolean execute(final EffectData object) {
                object.updateState();
                return true;
            }
        };
    }
    
    private static class EffectData
    {
        private final ArrayList<EffectBlock> m_currents;
        private final EffectApplyer m_result;
        private boolean m_started;
        
        private EffectData(final EffectApplyer result) {
            super();
            this.m_currents = new ArrayList<EffectBlock>();
            this.m_started = false;
            this.m_result = result;
        }
        
        public final void combine() {
            this.m_result.combine(this.m_currents);
        }
        
        public final boolean isActive() {
            return this.m_currents.size() > 0;
        }
        
        public final void clear() {
            this.m_currents.clear();
        }
        
        public final void updateState() {
            if (this.isActive() == this.m_started) {
                return;
            }
            if (this.m_started) {
                this.m_result.stop();
            }
            else {
                this.m_result.start();
            }
            this.m_started = !this.m_started;
        }
        
        public final void add(final EffectBlock block) {
            this.m_currents.add(block);
        }
    }
}
