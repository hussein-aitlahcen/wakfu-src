package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class DeleteBuildingModification extends Modification<BuildingItem>
{
    private static final Logger m_logger;
    
    public DeleteBuildingModification(final BuildingItem item) {
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
        worldEditor.unmarkAsRemoved(this.m_item);
    }
    
    @Override
    public void onSuccess(final WorldEditor editor) {
        editor.remove(this.m_item);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DeleteBuildingModification.class);
    }
}
