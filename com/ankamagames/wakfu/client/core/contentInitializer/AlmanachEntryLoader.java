package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.almanach.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class AlmanachEntryLoader implements ContentInitializer
{
    public static final AlmanachEntryLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.execute();
        clientInstance.fireContentInitializerDone(this);
    }
    
    public void execute() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AlmanachEntryBinaryData(), new LoadProcedure<AlmanachEntryBinaryData>() {
            @Override
            public void load(final AlmanachEntryBinaryData bs) {
                final int id = bs.getId();
                final int achievementId = bs.getAchievementId();
                final int scenarioId = bs.getScenarioId();
                final int[] territories = bs.getTerritories();
                AlmanachEntryManager.INSTANCE.addEntry(new AlmanachEntry(id, scenarioId, achievementId, territories));
            }
        });
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.almanach.entry");
    }
    
    static {
        INSTANCE = new AlmanachEntryLoader();
    }
}
