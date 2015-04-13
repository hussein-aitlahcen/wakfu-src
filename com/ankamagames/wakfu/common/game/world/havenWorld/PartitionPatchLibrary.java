package com.ankamagames.wakfu.common.game.world.havenWorld;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public abstract class PartitionPatchLibrary<P extends PartitionPatch>
{
    private static final Logger m_logger;
    private final TIntObjectHashMap<P> m_patches;
    
    public PartitionPatchLibrary() {
        super();
        this.m_patches = new TIntObjectHashMap<P>();
    }
    
    public void load(final String pathURL) throws IOException {
        final ExtendedDataInputStream stream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(pathURL));
        for (int count = stream.readInt(), i = 0; i < count; ++i) {
            final int id = stream.readInt();
            final P patch = this.createPatch(id);
            patch.read(stream);
            this.m_patches.put(id, patch);
        }
    }
    
    protected abstract P createPatch(final int p0) throws IOException;
    
    public P getPatch(final int id) {
        return this.m_patches.get(id);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartitionPatchLibrary.class);
    }
}
