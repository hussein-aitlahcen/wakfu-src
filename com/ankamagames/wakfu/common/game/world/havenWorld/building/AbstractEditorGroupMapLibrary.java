package com.ankamagames.wakfu.common.game.world.havenWorld.building;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public abstract class AbstractEditorGroupMapLibrary<B extends AbstractEditorGroupMap>
{
    private static final Logger m_logger;
    private final TIntObjectHashMap<B> m_buildingsByGroupId;
    
    public AbstractEditorGroupMapLibrary() {
        super();
        this.m_buildingsByGroupId = new TIntObjectHashMap<B>();
    }
    
    public void load(final String pathURL) throws IOException {
        final ExtendedDataInputStream stream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(pathURL));
        for (int count = stream.readInt(), i = 0; i < count; ++i) {
            final int groupId = stream.readInt();
            final B building = this.createBuilding(groupId);
            building.read(stream);
            this.m_buildingsByGroupId.put(groupId, building);
        }
    }
    
    protected abstract B createBuilding(final int p0) throws IOException;
    
    public B getEditorGroup(final int groupId) {
        return this.m_buildingsByGroupId.get(groupId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractEditorGroupMapLibrary.class);
    }
}
