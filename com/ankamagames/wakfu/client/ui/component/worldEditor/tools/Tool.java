package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.havenWorld.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public abstract class Tool
{
    private static final Logger m_logger;
    protected WorldEditor m_worldEditor;
    protected Modification m_modification;
    private final DisplayOptions m_displayOptions;
    
    protected Tool(final DisplayOptions displayOptions) {
        super();
        this.m_displayOptions = displayOptions;
    }
    
    protected Tool() {
        this(new DisplayOptions(false, false));
    }
    
    public final void setWorldEditor(final WorldEditor worldEditor) {
        (this.m_worldEditor = worldEditor).applyDisplayOptions(this.m_displayOptions);
    }
    
    protected abstract void execute(final int p0, final int p1);
    
    public final boolean tryExecute(final int screenX, final int screenY) {
        this.m_worldEditor.unhighlightPartition();
        if (!this.isEditableZone(screenX, screenY)) {
            return false;
        }
        this.execute(screenX, screenY);
        if (this.m_modification != null) {
            final ArrayList<ConstructionError> errors = (ArrayList<ConstructionError>)this.m_modification.getConflicts();
            for (final ConstructionError error : errors) {
                if (error.getType() == ConstructionError.Type.Conflict) {
                    this.m_worldEditor.markAsError(error.getItem());
                }
            }
        }
        return this.m_modification != null && !this.m_modification.hasConflict();
    }
    
    public void executeAndApply(final int screenX, final int screenY) {
        if (!this.tryExecute(screenX, screenY)) {
            this.clear();
            return;
        }
        this.m_modification.apply(this.m_worldEditor.getWorkingHavenWorld());
        Worker.getInstance().pushMessage(new UIHavenWorldModification(this.m_modification));
        this.m_modification = null;
        this.onApply();
    }
    
    protected void onApply() {
    }
    
    private boolean isEditableZone(final int screenX, final int screenY) {
        final Point2i patchCoord = this.m_worldEditor.getPatchCoordFromMouse(screenX, screenY);
        return this.m_worldEditor.getWorkingHavenWorld().isEditablePatch(patchCoord.getX(), patchCoord.getY());
    }
    
    public void clear() {
        if (this.m_modification != null) {
            this.m_modification.revert(this.m_worldEditor);
            this.m_worldEditor.applyDisplayOptions(this.m_displayOptions);
            this.m_modification = null;
        }
    }
    
    public abstract ItemLayer getWorkingLayer();
    
    static {
        m_logger = Logger.getLogger((Class)Tool.class);
    }
}
