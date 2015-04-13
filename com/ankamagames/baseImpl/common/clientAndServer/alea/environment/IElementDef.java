package com.ankamagames.baseImpl.common.clientAndServer.alea.environment;

import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public interface IElementDef
{
    void load(ExtendedDataInputStream p0) throws IOException;
    
    void save(OutputBitStream p0) throws IOException;
}
