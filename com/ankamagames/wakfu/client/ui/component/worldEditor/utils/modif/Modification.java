package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public abstract class Modification<T extends ModificationItem>
{
    private final ArrayList<ConstructionError> m_conflicts;
    protected final T m_item;
    
    protected Modification(final T item) {
        super();
        this.m_conflicts = new ArrayList<ConstructionError>();
        this.m_item = item;
    }
    
    public boolean hasConflict() {
        return !this.m_conflicts.isEmpty();
    }
    
    public ArrayList<ConstructionError> getConflicts() {
        return this.m_conflicts;
    }
    
    public void addErrors(final ArrayList<ConstructionError> conflicts) {
        this.m_conflicts.addAll(conflicts);
    }
    
    public T getItem() {
        return this.m_item;
    }
    
    public abstract Type getType();
    
    public abstract void apply(final HavenWorldTopology p0);
    
    public abstract void onSuccess(final WorldEditor p0);
    
    public abstract void unapply(final WorldEditor p0);
    
    public final void onError(final WorldEditor editor) {
        this.unapply(editor);
    }
    
    protected void revertModif(final WorldEditor editor) {
        this.unapply(editor);
    }
    
    public void revert(final WorldEditor editor) {
        this.revertModif(editor);
        for (final ConstructionError error : this.getConflicts()) {
            if (error.getType() == ConstructionError.Type.Conflict) {
                editor.unmarkAsError(error.getItem());
            }
        }
    }
    
    public Point2i getLocation() {
        return this.m_item.getCell();
    }
    
    public HavenWorldCatalogEntry getCatalogEntry() {
        return this.m_item.getCatalogEntry();
    }
    
    public enum Type
    {
        ADD, 
        REMOVE, 
        MOVE, 
        SELECT, 
        ERROR, 
        INIT;
        
        public Type getReverseModifType() {
            if (this == Type.ADD) {
                return Type.REMOVE;
            }
            if (this == Type.REMOVE) {
                return Type.ADD;
            }
            return this;
        }
    }
}
