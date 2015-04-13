package com.ankamagames.framework.fileFormat.io.binaryStorage.handler;

import com.ankamagames.framework.fileFormat.io.binaryStorage.*;

public interface BinaryStorageHandler
{
    void onInit(AbstractBinaryStorage p0, String p1);
    
    void onSave(AbstractBinaryStorage p0, BinaryStorable p1);
    
    void onDelete(AbstractBinaryStorage p0, BinaryStorable p1);
    
    void onShutdown(AbstractBinaryStorage p0);
    
    void onLoad(AbstractBinaryStorage p0, BinaryStorable p1);
}
