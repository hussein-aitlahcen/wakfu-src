package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.groundType.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class GroundTypeLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new GroundBinaryData(), new LoadProcedure<GroundBinaryData>() {
            @Override
            public void load(final GroundBinaryData bs) {
                final GroundType groundType = new GroundType(bs.getId());
                groundType.setFertilityOnResources(bs.getResourceFertility());
                groundType.setFertilityOnResourceTypes(bs.getResourceTypeFertility());
                GroundManager.getInstance().addGroundType(groundType);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.ground");
    }
}
