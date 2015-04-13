package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class NationColorLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new NationColorsBinaryData(), new LoadProcedure<NationColorsBinaryData>() {
            @Override
            public void load(final NationColorsBinaryData data) {
                NationDisplayer.getInstance().putNationColor(data.getId(), data.getColor());
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.nationColor");
    }
}
