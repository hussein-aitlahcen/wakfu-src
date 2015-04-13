package com.ankamagames.wakfu.common.game.nation.government;

import com.ankamagames.wakfu.common.game.nation.government.data.*;
import com.ankamagames.wakfu.common.rawData.*;

public class EmptyGovernmentData extends GovernmentData
{
    public static final EmptyGovernmentData INSTANCE;
    
    @Override
    public String getSpeech() {
        return "";
    }
    
    @Override
    public void toRaw(final RawNationGovernmentData raw) {
    }
    
    @Override
    public void fromRaw(final RawNationGovernmentData raw) {
    }
    
    static {
        INSTANCE = new EmptyGovernmentData();
    }
}
