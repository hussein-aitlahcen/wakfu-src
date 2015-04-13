package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.tutorial.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.*;

public class TutorialLoader implements ContentInitializer
{
    protected static final Logger m_logger;
    public static final TutorialLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.loadFromStorage();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private boolean loadFromStorage() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new TutorialBinaryData(), new LoadProcedure<TutorialBinaryData>() {
            @Override
            public void load(final TutorialBinaryData data) {
                final int tutorialId = data.getId();
                final TutorialBinaryData.Event[] eventIds = data.getEventIds();
                final TIntArrayList ids = new TIntArrayList();
                for (final TutorialBinaryData.Event e : eventIds) {
                    ids.add(e.getEventId());
                }
                TutorialManager.INSTANCE.addTutorialPage(tutorialId, ids.toNativeArray());
            }
        });
        ClientGameEventManager.INSTANCE.initialize();
        return true;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.scriptAction");
    }
    
    static {
        m_logger = Logger.getLogger((Class)TutorialLoader.class);
        INSTANCE = new TutorialLoader();
    }
}
