package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.game.aptitudenew.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.dataTest.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.*;

public final class AptitudeBonusLoader implements ContentInitializer
{
    public static final AptitudeBonusLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.loadFromBinaryData();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private void loadFromBinaryData() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AptitudeBonusBinaryData(), new LoadProcedure<AptitudeBonusBinaryData>() {
            @Override
            public void load(final AptitudeBonusBinaryData bs) {
                final int bonusId = bs.getBonusId();
                final int max = bs.getMax();
                final int[] effectIds = bs.getEffectIds();
                final int gfxId = bs.getGfxId();
                int effectId = 0;
                if (effectIds.length > 0) {
                    effectId = effectIds[0];
                }
                final AptitudeBonusModel aptitudeBonusModel = new ClientAptitudeBonusModel(bonusId, effectId, max, gfxId);
                AptitudeBonusLoader.addBonusModelToManager(aptitudeBonusModel);
            }
        });
    }
    
    private void testBonuses() {
        addBonusModelToManager(AptitudeBonusTest.DEG_ALL_BOOST);
        addBonusModelToManager(AptitudeBonusTest.DEG_MONO_CLOSE_BOOST);
        addBonusModelToManager(AptitudeBonusTest.DEG_ZONE_CLOSE_BOOST);
        addBonusModelToManager(AptitudeBonusTest.DEG_MONO_RANGE_BOOST);
        addBonusModelToManager(AptitudeBonusTest.DEG_ZONE_RANGE_BOOST);
        addBonusModelToManager(AptitudeBonusTest.HP_MAX_BOOST);
        addBonusModelToManager(AptitudeBonusTest.RES_BOOST);
        addBonusModelToManager(AptitudeBonusTest.HP_REGEN_BY_TURN_BOOST);
        addBonusModelToManager(AptitudeBonusTest.LOCK_BOOST);
        addBonusModelToManager(AptitudeBonusTest.DODGE_BOOST);
        addBonusModelToManager(AptitudeBonusTest.INIT_BOOST);
        addBonusModelToManager(AptitudeBonusTest.LOCK_N_DODGE_BOOST);
        addBonusModelToManager(AptitudeBonusTest.CRITICAL_BOOST);
        addBonusModelToManager(AptitudeBonusTest.BLOCK_BOOST);
        addBonusModelToManager(AptitudeBonusTest.CRITICAL_DAMAGES_BOOST);
        addBonusModelToManager(AptitudeBonusTest.BACKSTAB_BOOST);
        addBonusModelToManager(AptitudeBonusTest.BERZERK_DAMAGES_BOOST);
        addBonusModelToManager(AptitudeBonusTest.HEAL_BOOST);
        addBonusModelToManager(AptitudeBonusTest.NON_CRITICAL_BOOST);
        addBonusModelToManager(AptitudeBonusTest.PA_BOOST);
        addBonusModelToManager(AptitudeBonusTest.PM_BOOST);
        addBonusModelToManager(AptitudeBonusTest.PO_BOOST);
        addBonusModelToManager(AptitudeBonusTest.PW_BOOST);
        addBonusModelToManager(AptitudeBonusTest.CONTROL_BOOST);
        addBonusModelToManager(AptitudeBonusTest.KIT_SKILL_BOOST);
    }
    
    static void addBonusModelToManager(final AptitudeBonusModel bonusModel) {
        final int effectId = bonusModel.getEffectId();
        final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
        if (effect != null) {
            AptitudeBonusEffectManager.INSTANCE.addEffect(effect);
        }
        AptitudeBonusModelManager.INSTANCE.add(bonusModel);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.aptitudes");
    }
    
    static {
        INSTANCE = new AptitudeBonusLoader();
    }
}
