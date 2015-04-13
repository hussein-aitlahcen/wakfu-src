package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.almanach.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class AlmanachDateLoader implements ContentInitializer
{
    public static final AlmanachDateLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.execute();
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.almanach.date");
    }
    
    public void execute() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AlmanachDateBinaryData(), new LoadProcedure<AlmanachDateBinaryData>() {
            @Override
            public void load(final AlmanachDateBinaryData bs) {
                final int almanachEntryId = bs.getAlmanachEntryId();
                final Date date = bs.getDate();
                AlmanachEntryManager.INSTANCE.addRegularDate(date, almanachEntryId);
            }
        });
    }
    
    static {
        INSTANCE = new AlmanachDateLoader();
    }
}
