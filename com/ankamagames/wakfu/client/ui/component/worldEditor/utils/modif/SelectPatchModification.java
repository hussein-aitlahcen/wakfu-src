package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class SelectPatchModification extends Modification<PatchItem>
{
    private static final Logger m_logger;
    
    public SelectPatchModification(final PatchItem item) {
        super(item);
    }
    
    @Override
    public Type getType() {
        return Type.SELECT;
    }
    
    @Override
    public void apply(final HavenWorldTopology world) {
    }
    
    @Override
    public void unapply(final WorldEditor worldEditor) {
        worldEditor.highlightEntity(null);
    }
    
    @Override
    public void onSuccess(final WorldEditor editor) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)SelectPatchModification.class);
    }
}
