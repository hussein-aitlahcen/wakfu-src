package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.basicDungeon.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class DungeonLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.execute();
        clientInstance.fireContentInitializerDone(this);
    }
    
    public void execute() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new DungeonBinaryData(), new LoadProcedure<DungeonBinaryData>() {
            @Override
            public void load(final DungeonBinaryData bs) {
                final int id = bs.getId();
                final short minLevel = bs.getMinLevel();
                final short instanceId = bs.getInstanceId();
                final DungeonDefinition def = new DungeonDefinition(id, minLevel, instanceId, bs.getTps());
                DungeonManager.INSTANCE.addDungeon(def);
            }
        });
    }
    
    @Override
    public String getName() {
        return "";
    }
}
