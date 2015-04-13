package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.personalSpace.data.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public final class HavenBagModelViewLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenBagModelViewBinaryData(), new LoadProcedure<HavenBagModelViewBinaryData>() {
            @Override
            public void load(final HavenBagModelViewBinaryData data) throws Exception {
                final int viewId = data.getId();
                boolean restrictionMarket = data.isRestrictionMarket();
                if (viewId == 408) {
                    restrictionMarket = false;
                }
                final DimensionalBagModelView view = new DimensionalBagModelView(viewId, data.isRestrictionWorld(), restrictionMarket, (short)data.getBackgroundMapId(), data.isInnate());
                DimensionalBagModelViewManager.INSTANCE.addView(view);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.ground");
    }
}
