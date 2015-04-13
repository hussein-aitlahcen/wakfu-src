package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public final class CitizenRankLoader implements ContentInitializer
{
    private static CitizenRankLoader INSTANCE;
    
    public static CitizenRankLoader getInstance() {
        return CitizenRankLoader.INSTANCE;
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new CitizenRankBinaryData(), new LoadProcedure<CitizenRankBinaryData>() {
            @Override
            public void load(final CitizenRankBinaryData data) {
                CitizenRankManager.getInstance().addRank(data.getId(), data.getCap(), data.getPdcLossFactor(), data.getTranslationKey(), data.getColor(), data.getCitizenRankRules());
            }
        });
        CitizenRankManager.getInstance().onDataLoaded();
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.citizen.rank");
    }
    
    static {
        CitizenRankLoader.INSTANCE = new CitizenRankLoader();
    }
}
