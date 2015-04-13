package com.ankamagames.framework.kernel.core.common.serialization;

public interface BinarSerialDataSource
{
    void updateToSerializedPart();
    
    void onDataChanged();
}
