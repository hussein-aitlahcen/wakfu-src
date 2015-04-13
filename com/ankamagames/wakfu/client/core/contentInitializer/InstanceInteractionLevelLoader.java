package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.account.subscription.instanceRight.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public final class InstanceInteractionLevelLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new InstanceInteractionLevelBinaryData(), new LoadProcedure<InstanceInteractionLevelBinaryData>() {
            @Override
            public void load(final InstanceInteractionLevelBinaryData data) {
                InstanceInteractionLevelManager.INSTANCE.add(data.getWorldId(), data.getSubscriptionLevel(), InstanceInteractionLevel.getFromId(data.getInteractionLevel()));
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.instanceInteractionLevel");
    }
}
