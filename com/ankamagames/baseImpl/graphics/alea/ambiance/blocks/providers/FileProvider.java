package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class FileProvider extends EffectProvider<String>
{
    private FileProvider(final ProviderModel model) {
        super(model);
    }
    
    public final String getValue() {
        return this.m_inputs[0].stringValue();
    }
    
    @Override
    public final float floatValue() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final int intValue() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final String stringValue() {
        return this.getValue();
    }
    
    public static class Model extends ProviderModel<FileProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[] { new ModelVar.MString("value") });
        }
        
        @Override
        public final FileProvider createInstance() {
            return new FileProvider(this, null);
        }
    }
}
