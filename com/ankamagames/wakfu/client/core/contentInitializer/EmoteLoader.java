package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class EmoteLoader implements ContentInitializer
{
    private static final Logger m_logger;
    private static final short INVALID_EMOTE_TYPE_ID = 0;
    private static final short EMOTE_TYPE_ID = 1;
    private static final short EMOTE_WITH_TARGET_TYPE_ID = 2;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new EmoteBinaryData(), new LoadProcedure<EmoteBinaryData>() {
            @Override
            public void load(final EmoteBinaryData data) {
                switch (data.getType()) {
                    case 0: {
                        EmoteLoader.m_logger.error((Object)("Emote de type invalide id=" + data.getId()));
                        break;
                    }
                    case 1: {
                        ReferenceEmoteManager.INSTANCE.addEmote(data);
                        break;
                    }
                    case 2: {
                        ReferenceEmoteManager.INSTANCE.addEmoteWithTarget(data);
                        break;
                    }
                }
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.emotes");
    }
    
    static {
        m_logger = Logger.getLogger((Class)EmoteLoader.class);
    }
}
