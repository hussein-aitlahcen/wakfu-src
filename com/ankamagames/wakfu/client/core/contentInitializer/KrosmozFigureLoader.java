package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.krosmoz.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class KrosmozFigureLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new KrosmozFigureBinaryData(), new LoadProcedure<KrosmozFigureBinaryData>() {
            @Override
            public void load(final KrosmozFigureBinaryData data) {
                KrosmozFigureDataManager.INSTANCE.addFigureData(data.getId(), data.getYear(), data.getAddon(), data.getSeason());
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.krosmozFigure");
    }
}
