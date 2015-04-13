package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.lock.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class LockLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.execute();
        clientInstance.fireContentInitializerDone(this);
    }
    
    public void execute() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new LockBinaryData(), new LoadProcedure<LockBinaryData>() {
            @Override
            public void load(final LockBinaryData bs) {
                final int id = bs.getId();
                final int lockedItemId = bs.getLockedItemId();
                final GameInterval periodDuration = GameInterval.fromSeconds(bs.getPeriodDuration());
                final GameDate unlockDate = GameDate.fromJavaDate(bs.getUnlockDate());
                final LockDefinition lock = new Lock(id, lockedItemId, bs.getLockValue(), unlockDate, periodDuration, GameDate.getNullDate(), bs.isAvailableForCitizensOnly());
                LockManager.INSTANCE.addLock(lock);
            }
        });
    }
    
    @Override
    public String getName() {
        return "";
    }
}
