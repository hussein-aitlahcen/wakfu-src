package com.ankamagames.wakfu.common.game.havenWorld.definition;

import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;

public class BuildingDecoDefinition extends AbstractBuildingDefinition
{
    public BuildingDecoDefinition(final short id, final short catalogEntryId, final int kamasCost, final int editorGroupId, final int resourcesCost) {
        super(id, catalogEntryId, kamasCost, (byte)0, 0, editorGroupId, resourcesCost, true);
    }
    
    @Override
    public int getEditorGroupId(final int itemId) {
        return this.getEditorGroupId();
    }
    
    @Override
    public boolean acceptItem(final int itemId) {
        return false;
    }
    
    @Override
    public int[] getEquipableItems() {
        return PrimitiveArrays.EMPTY_INT_ARRAY;
    }
    
    @Override
    public boolean isDecoOnly() {
        return true;
    }
    
    @Override
    public boolean hasEffect() {
        return false;
    }
    
    @Override
    public boolean forEachElement(final TObjectProcedure<BuildingIEDefinition> procedure) {
        return true;
    }
}
