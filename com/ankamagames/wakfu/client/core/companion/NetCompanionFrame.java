package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.common.game.companion.freeCompanion.*;

public final class NetCompanionFrame extends MessageRunnerFrame
{
    public static final NetCompanionFrame INSTANCE;
    
    public NetCompanionFrame() {
        super(new MessageRunner[] { new CompanionAddedMessageRunner(), new CompanionUpdateNameMessageRunner(), new CompanionListMessageRunner(), new AddItemToCompanionEquipmentResultMessageRunner(), new RemoveItemFromCompanionEquipmentResultMessageRunner(), new CompanionUpdateXpMessageRunner(), new FreeCompanionBreedIdMessageRunner(), new CompanionUpdateUnlockedMessageRunner(), new AddItemToCompanionEquipmentErrorMessageRunner(), new CompanionListUpdateMessageRunner(), new HeroAddedMessageRunner() });
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        FreeCompanionManager.INSTANCE.addListener(FreeCompanionUpdateClientListener.INSTANCE);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        FreeCompanionManager.INSTANCE.removeListener(FreeCompanionUpdateClientListener.INSTANCE);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new NetCompanionFrame();
    }
}
