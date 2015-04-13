package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class CompanionItemLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.execute();
        clientInstance.fireContentInitializerDone(this);
    }
    
    public void execute() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new CompanionItemBinaryData(), new LoadProcedure<CompanionItemBinaryData>() {
            @Override
            public void load(final CompanionItemBinaryData bs) {
                final int id = bs.getId();
                CompanionItemManager.INSTANCE.addEntry(id);
            }
        });
    }
    
    @Override
    public String getName() {
        return "";
    }
}
