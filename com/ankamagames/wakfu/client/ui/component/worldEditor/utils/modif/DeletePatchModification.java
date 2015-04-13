package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;

public class DeletePatchModification extends Modification<PatchItem>
{
    private static final Logger m_logger;
    
    public DeletePatchModification(final PatchItem item) {
        super(item);
    }
    
    @Override
    public Type getType() {
        return Type.REMOVE;
    }
    
    @Override
    public void apply(final HavenWorldTopology world) {
    }
    
    @Override
    public void unapply(final WorldEditor worldEditor) {
    }
    
    @Override
    public void onSuccess(final WorldEditor editor) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)DeletePatchModification.class);
    }
}
