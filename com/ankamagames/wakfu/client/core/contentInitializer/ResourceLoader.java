package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.game.craft.collect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;

public class ResourceLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ResourceBinaryData(), new LoadProcedure<ResourceBinaryData>() {
            @Override
            public void load(final ResourceBinaryData data) {
                final ReferenceResource resource = ResourceLoader.loadFromBinaryForm(data);
                ReferenceResourceManager.getInstance().addReferenceResource(resource);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.resource");
    }
    
    public static ReferenceResource loadFromBinaryForm(final ResourceBinaryData bs) {
        final int resourceId = bs.getId();
        final TIntObjectHashMap<int[]> gfxIds = bs.getGfxIds();
        final int resourceType = bs.getType();
        final int iconGfxId = bs.getIconGfxId();
        final boolean isBlocking = bs.isBlocking();
        final short height = bs.getHeight();
        final int[] resourcePropertyIds = bs.getProperties();
        final short rainMin = bs.getIdealRainMin();
        final short rainMax = bs.getIdealRainMax();
        final short tempMin = bs.getIdealTemperatureMin();
        final short tempMax = bs.getIdealTemperatureMax();
        final boolean useBigChallengeAps = bs.isUseBigChallengeAps();
        ReferenceResource resource;
        if (bs.isMonsterEmbryo()) {
            resource = new MonsterReferenceResource(resourceId, (short)resourceType, tempMin, tempMax, rainMin, rainMax, isBlocking, height, gfxIds, iconGfxId, useBigChallengeAps);
            ((MonsterReferenceResource)resource).addMonsterFamilies(bs.getMonsterFamilies());
            ((MonsterReferenceResource)resource).setBirthEvolutionStep(bs.getMonsterStepHatching());
        }
        else {
            resource = new ReferenceResource(resourceId, (short)resourceType, tempMin, tempMax, rainMin, rainMax, isBlocking, height, gfxIds, iconGfxId, useBigChallengeAps);
        }
        for (int i = 0; i < resourcePropertyIds.length; ++i) {
            final int propertyId = resourcePropertyIds[i];
            final ResourcesProperty prop = ResourcesProperty.getProperty(propertyId);
            if (prop == null) {
                ResourceLoader.m_logger.error((Object)("Impossible de trouver la propri\u00e9t\u00e9 " + propertyId + " d\u00e9finie pour la ressource " + resourceId));
            }
            else {
                resource.addProperty(prop);
            }
        }
        final ResourceEvolutionStep step = new ResourceEvolutionStep((byte)0, ResourceSizeCategory.TINY, null, 0.0f);
        resource.addEvolveStep(step);
        resource.ensureEvolutionStepCapacity(bs.getSteps().length);
        for (final ResourceBinaryData.ResourceStep steps : bs.getSteps()) {
            loadStep(resource, steps);
        }
        return resource;
    }
    
    private static void loadStep(final ReferenceResource resource, final ResourceBinaryData.ResourceStep bsStep) {
        final int stepIndex = bsStep.getIndex();
        final byte stepidx = (byte)bsStep.getIndex();
        final int sizeIdx = bsStep.getSizeCategoryId();
        final ResourceSizeCategory[] sizeCategories = ResourceSizeCategory.values();
        float[] lightIntensity = null;
        float lightRange = 0.0f;
        final int intColor = bsStep.getLightColor();
        if (intColor != 0) {
            lightIntensity = new Color(intColor).getFloatRGBA();
            lightRange = bsStep.getLightRadius();
        }
        final ResourceEvolutionStep evolutionStep = new ResourceEvolutionStep(stepidx, sizeCategories[sizeIdx], lightIntensity, lightRange);
        resource.addEvolveStep(evolutionStep);
        evolutionStep.ensureCollectCapacity(bsStep.getActions().length);
        for (final ResourceBinaryData.ResourceStepAction action : bsStep.getActions()) {
            final CollectAction collect = loadCollect(resource.getId(), stepIndex, action);
            evolutionStep.addCollectAction(collect);
        }
    }
    
    @Nullable
    private static CollectAction loadCollect(final int resourceId, final int stepIndex, final ResourceBinaryData.ResourceStepAction action) {
        final int collectId = action.getId();
        final int resourceNextIndex = action.getResourceNextIndex();
        final int craftId = action.getSkillId();
        final int levelMin = action.getSkillLevelRequired();
        final int visualId = action.getSkillVisualFeedbackId();
        final int nbPlayerMin = action.getSkillSimultaneousPlayer();
        final int collectedItemId = action.getCollectItemId();
        final int collectDuration = action.getDuration();
        final byte order = (byte)action.getMruOrder();
        final String criterionAsString = action.getCriteria();
        final int consumableId = action.getConsumableId();
        final int consumableGfxId = action.getGfxId();
        final int[] lootItems = action.getLootItems();
        final boolean displayInCrfatDialog = action.isDisplayInCraftDialog();
        final ClientConsumableInfo consumableInfo = new ClientConsumableInfo(consumableId, consumableGfxId);
        SimpleCriterion criterion;
        try {
            criterion = CriteriaCompiler.compileBoolean(criterionAsString);
        }
        catch (Exception e) {
            ResourceLoader.m_logger.error((Object)("Erreur de compilation du crit\u00e8rre sur l'action de collecte de ressource " + action.getId() + " de la ressource " + resourceId));
            return null;
        }
        return new CollectAction(collectId, stepIndex, craftId, levelMin, nbPlayerMin, collectDuration, collectedItemId, visualId, criterion, resourceNextIndex, order, consumableInfo, displayInCrfatDialog, lootItems);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ResourceLoader.class);
    }
}
