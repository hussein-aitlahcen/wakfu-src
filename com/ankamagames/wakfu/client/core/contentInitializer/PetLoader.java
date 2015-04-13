package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.mount.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;

public class PetLoader implements ContentInitializer
{
    private static final Logger m_logger;
    public static final PetLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new PetBinaryData(), new LoadProcedure<PetBinaryData>() {
            @Override
            public void load(final PetBinaryData bs) {
                PetDefinitionManager.INSTANCE.add(PetLoader.createPetDefinition(bs), bs.getItemRefId());
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    public static PetDefinition createPetDefinition(final PetBinaryData bs) {
        final PetDefinition def = new PetDefinition(bs.getId(), Integer.toString(bs.getGfxId()), MountType.getFromId(bs.getMountType()), bs.getItemColorRefId(), bs.getItemReskinRefId(), bs.getHealth(), bs.getMinMealInterval(), bs.getMaxMealInterval(), bs.getXpByMeal(), bs.getXpPerLevel(), bs.getLevelMax());
        for (final PetBinaryData.HealthPenalty penalty : bs.getHealthPenalties()) {
            def.addHealthPenalty(HealthPenaltyType.getById(penalty.getPenaltyType()), penalty.getValue());
        }
        for (final PetBinaryData.HealthItem health : bs.getHealthItems()) {
            def.addHealItem(health.getItemId(), health.getValue());
        }
        for (final PetBinaryData.MealItem meal : bs.getMealItems()) {
            def.addMeal(meal.getItemId(), meal.isVisible());
        }
        for (final PetBinaryData.SleepItem sleep : bs.getSleepItems()) {
            def.addSleepItem(sleep.getItemId(), GameInterval.fromSeconds(sleep.getDuration()));
        }
        final int[] equipmentItems = bs.getEquipmentItems();
        if (equipmentItems != null) {
            for (final int equipment : equipmentItems) {
                def.addEquipment(equipment);
            }
        }
        for (final PetBinaryData.ColorItem color : bs.getColorItems()) {
            def.addColor(color.getItemId(), color.getPartId(), color.getColorABGR());
        }
        for (final PetBinaryData.ReskinItem reskin : bs.getReskinItems()) {
            def.addReskinItem(reskin.getItemId(), String.valueOf(reskin.getGfxId()));
        }
        return def;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.item");
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetLoader.class);
        INSTANCE = new PetLoader();
    }
}
