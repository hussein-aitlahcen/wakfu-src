package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.dataTest.*;
import com.ankamagames.wakfu.client.core.*;

public final class AptitudeCategoryModelLoader implements ContentInitializer
{
    public static final AptitudeCategoryModelLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.loadFromBinaryData();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private void loadFromBinaryData() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AptitudeBonusCategoryBinaryData(), new LoadProcedure<AptitudeBonusCategoryBinaryData>() {
            @Override
            public void load(final AptitudeBonusCategoryBinaryData bs) {
                final int categoryId = bs.getCategoryId();
                final int[] levels = bs.getLevels();
                final int[] bonusIds = bs.getBonusIds();
                final AptitudeCategoryModel categoryModel = new AptitudeCategoryModel(categoryId, new TIntArrayList(levels));
                for (int i = 0; i < bonusIds.length; ++i) {
                    final int bonusId = bonusIds[i];
                    final AptitudeBonusModel aptitudeBonusModel = AptitudeBonusModelManager.INSTANCE.get(bonusId);
                    categoryModel.addBonus(aptitudeBonusModel);
                }
                AptitudeCategoryModelManager.INSTANCE.addCategory(categoryModel);
            }
        });
    }
    
    private void testCategory() {
        AptitudeCategoryModelManager.INSTANCE.addCategory(AptitudeCategoryTest.FORCE_CAT);
        AptitudeCategoryModelManager.INSTANCE.addCategory(AptitudeCategoryTest.INTEL_CAT);
        AptitudeCategoryModelManager.INSTANCE.addCategory(AptitudeCategoryTest.AGI_CAT);
        AptitudeCategoryModelManager.INSTANCE.addCategory(AptitudeCategoryTest.CHANCE_CAT);
        AptitudeCategoryModelManager.INSTANCE.addCategory(AptitudeCategoryTest.CAPITAL_CAT);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.aptitudes");
    }
    
    static {
        INSTANCE = new AptitudeCategoryModelLoader();
    }
}
