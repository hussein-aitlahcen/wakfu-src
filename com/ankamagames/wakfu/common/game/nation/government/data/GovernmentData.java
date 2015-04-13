package com.ankamagames.wakfu.common.game.nation.government.data;

import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.rawData.*;

public abstract class GovernmentData
{
    public static GovernmentData createNew(final NationRank rank) {
        switch (rank) {
            case GOVERNOR: {
                return new GovernorData();
            }
            case MARSHAL: {
                return new MarshalData();
            }
            case GENERAL: {
                return new GeneralData();
            }
            default: {
                return EmptyGovernmentData.INSTANCE;
            }
        }
    }
    
    public abstract String getSpeech();
    
    public abstract void toRaw(final RawNationGovernmentData p0);
    
    public abstract void fromRaw(final RawNationGovernmentData p0);
}
