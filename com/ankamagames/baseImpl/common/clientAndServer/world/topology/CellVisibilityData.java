package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

public class CellVisibilityData extends CellData
{
    @Override
    public CellData createMerged(final CellData c) {
        final CellVisibilityData result = new CellVisibilityData();
        CellData.merge(this, c, result);
        return result;
    }
}
