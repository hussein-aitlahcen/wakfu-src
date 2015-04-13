package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.background.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class BackgroundDisplayLoader implements ContentInitializer
{
    public static final BackgroundDisplayLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new BackgroundFeedbackBinaryData(), new LoadProcedure<BackgroundFeedbackBinaryData>() {
            @Override
            public void load(final BackgroundFeedbackBinaryData data) {
                final ArrayList<PageData> pageData = new ArrayList<PageData>();
                for (final BackgroundFeedbackBinaryData.Page p : data.getPages()) {
                    pageData.add(new PageData(p.getId(), p.getOrder(), p.getTemplate(), p.getImageId()));
                }
                final BackgroundDisplayType displayType = BackgroundDisplayType.getById(data.getType());
                if (displayType.isDoublePage() && data.getPages().length % 2 != 0) {
                    pageData.add(new PageData(-1, (short)pageData.size(), (short)0, -1));
                }
                final BackgroundDisplayData backgroundDisplayData = new BackgroundDisplayData(displayType, data.getId(), pageData);
                BackgroundDisplayManager.INSTANCE.addBackgroundDisplay(backgroundDisplayData);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.backgroundDisplays");
    }
    
    static {
        INSTANCE = new BackgroundDisplayLoader();
    }
}
