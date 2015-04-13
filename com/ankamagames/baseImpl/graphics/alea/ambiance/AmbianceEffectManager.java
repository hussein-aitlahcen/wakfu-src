package com.ankamagames.baseImpl.graphics.alea.ambiance;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.nio.*;

public class AmbianceEffectManager
{
    private static final AmbianceEffectManager m_instance;
    private ByteBuffer m_buffer;
    private final TIntIntHashMap m_index;
    private EffectBlockFactory m_factory;
    private final TIntObjectHashMap<Effect> m_effects;
    
    public static AmbianceEffectManager getInstance() {
        return AmbianceEffectManager.m_instance;
    }
    
    private AmbianceEffectManager() {
        super();
        this.m_index = new TIntIntHashMap();
        this.m_effects = new TIntObjectHashMap<Effect>();
    }
    
    public void init(final String filename, @NotNull final EffectBlockFactory effectBlockFactory) throws IOException {
        this.m_factory = effectBlockFactory;
        this.setFileName(filename);
    }
    
    private void setFileName(final String filename) throws IOException {
        this.m_buffer = this.openFile(filename);
        this.createIndex();
    }
    
    private void createIndex() {
        this.m_buffer.position(0);
        this.m_index.clear();
        this.m_effects.clear();
        for (int count = this.m_buffer.getInt(), i = 0; i < count; ++i) {
            final int effectId = this.m_buffer.getInt();
            final int location = this.m_buffer.getInt();
            this.m_index.put(effectId, location);
        }
    }
    
    public final Effect getEffect(final int effectId) {
        try {
            this.loadEffects(effectId);
        }
        catch (Exception ex) {}
        return this.m_effects.get(effectId);
    }
    
    public final void loadEffects(final int... effectIds) throws Exception {
        this.m_effects.clear();
        for (final int effectId : effectIds) {
            this.m_effects.put(effectId, this.readEffect(this.m_buffer, effectId));
        }
    }
    
    public final void loadAllEffects() throws Exception {
        this.loadEffects(this.m_index.keys());
    }
    
    private Effect readEffect(final ByteBuffer buffer, final int effectId) throws Exception {
        final int position = this.m_index.get(effectId);
        if (position > 0) {
            buffer.position(position);
            final Effect effect = new Effect(effectId);
            effect.unserialize(buffer, this.m_factory);
            return effect;
        }
        return null;
    }
    
    public void readEffectsFrom(final EffectBlockFactory factory, final ByteBuffer buffer) throws Exception {
        this.m_factory = factory;
        this.m_buffer = buffer;
        this.createIndex();
        this.loadEffects(this.m_index.keys());
    }
    
    private ByteBuffer openFile(final String fileName) throws IOException {
        final byte[] bytes = ContentFileHelper.readFile(fileName);
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }
    
    public final int getIndexSize() {
        return this.m_index.size();
    }
    
    public final int getEffectCount() {
        return this.m_effects.size();
    }
    
    public final void resetEffect(final int effectId, final ByteBuffer effectData, final EffectBlockFactory factory) throws Exception {
        final Effect effect = this.m_effects.get(effectId);
        if (effect != null) {
            effect.unserialize(effectData, factory);
        }
    }
    
    static {
        m_instance = new AmbianceEffectManager();
    }
}
