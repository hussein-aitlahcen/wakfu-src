package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class CharacGainPerLevelLoader implements ContentInitializer
{
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.characGainPerLevel");
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new CharacGainPerLevelBinaryData(), new LoadProcedure<CharacGainPerLevelBinaryData>() {
            @Override
            public void load(final CharacGainPerLevelBinaryData data) {
                AvatarBreed.getBreedFromId(data.getBreedId()).loadSecondaryCharacGains(data.getGains());
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
}
