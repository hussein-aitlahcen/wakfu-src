package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class DirectionFollowerAttributeRW extends AffectorAttributesRW<DirectionFollower>
{
    public static final DirectionFollowerAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 6;
    }
    
    @Override
    public DirectionFollower createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        return new DirectionFollower();
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final DirectionFollower min, final DirectionFollower max) throws IOException {
    }
    
    @Override
    protected boolean equals(final DirectionFollower min, final DirectionFollower max) {
        return true;
    }
    
    static {
        m_instance = new DirectionFollowerAttributeRW();
    }
}
