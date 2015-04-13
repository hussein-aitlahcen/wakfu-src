package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public final class MonsterFamilyRelationShipLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        final MonsterFamilyManager familyManager = MonsterFamilyManager.getInstance();
        BinaryDocumentManager.getInstance().foreach(new MonsterTypeRelashionshipBinaryData(), new LoadProcedure<MonsterTypeRelashionshipBinaryData>() {
            @Override
            public void load(final MonsterTypeRelashionshipBinaryData data) {
                final int firstFamilyId = data.getFamilyFrom();
                final int secondFamilyId = data.getFamilyTo();
                final MonsterFamily firstFamily = familyManager.getMonsterFamily(firstFamilyId);
                firstFamily.addFriendlyFamily(secondFamilyId);
                final MonsterFamily secondFamily = familyManager.getMonsterFamily(secondFamilyId);
                secondFamily.addFriendlyFamily(firstFamilyId);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.monsterFamilyRelationShip");
    }
}
