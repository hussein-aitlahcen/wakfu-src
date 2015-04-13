package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.alea.graphics.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import java.io.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Resource extends AnimatedInteractiveElement implements Poolable, Collectible
{
    private static final Comparator<MRUCollectAction> MRU_COMPARATOR;
    private static final AnimationEndedListener m_animationHandler;
    public static boolean m_canLoadResource;
    private static final ObjectPool m_resourcePool;
    protected ReferenceResource m_referenceResource;
    private String m_animationName;
    private byte m_evolutionStep;
    protected boolean m_autoRespawn;
    protected boolean m_isLoaded;
    private boolean m_canBeDeleted;
    private boolean m_shadow;
    private float m_shadowValue;
    private IsoLightSource m_lightSource;
    
    public static Resource checkOut(final int worldX, final int worldY, final short altitude, final int resourceReferenceId, final byte stepId, final boolean justGrowth, final boolean autoRespawn) {
        Resource resource;
        try {
            resource = (Resource)Resource.m_resourcePool.borrowObject();
        }
        catch (Exception e) {
            Resource.m_logger.error((Object)"Erreur lors de l'extraction d'une resource du pool", (Throwable)e);
            resource = new Resource();
        }
        if (!resource.initialize(worldX, worldY, altitude, resourceReferenceId, stepId, justGrowth, autoRespawn)) {
            return null;
        }
        return resource;
    }
    
    protected boolean initialize(final int worldX, final int worldY, final short altitude, final int resourceReferenceId, final byte stepId, final boolean justGrowth, final boolean autoRespawn) {
        final long myId = MathHelper.getLongFromTwoInt(worldX, worldY);
        this.setId(myId);
        this.m_worldX = worldX;
        this.m_worldY = worldY;
        this.m_altitude = altitude - 1;
        this.m_autoRespawn = autoRespawn;
        this.m_isLoaded = false;
        this.m_referenceResource = ReferenceResourceManager.getInstance().getReferenceResource(resourceReferenceId);
        if (this.m_referenceResource == null) {
            Resource.m_logger.error((Object)("Error reference inexistante pour la ressource d'ID " + resourceReferenceId));
            return false;
        }
        this.setStaticAnimationKey(ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)1, stepId));
        this.setEvolutionStep(stepId, justGrowth);
        this.setDeltaZ(LayerOrder.RESOURCE_SMALL.getDeltaZ());
        MaskableHelper.setUndefined(this);
        return true;
    }
    
    protected Resource() {
        super(0L, 0.0f, 0.0f);
        this.m_evolutionStep = 0;
        this.m_isLoaded = false;
        this.m_canBeDeleted = false;
        this.m_shadowValue = 1.0f;
        this.addSelectionChangedListener(new InteractiveElementSelectionChangeListener<Resource>() {
            @Override
            public void selectionChanged(final Resource element, final boolean selected) {
                if (Resource.this.m_referenceResource == null || !element.isVisible()) {
                    return;
                }
                if (selected) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    if (widget != null && widget != MasterRootContainer.getInstance()) {
                        return;
                    }
                    MobileColorizeHelper.onHover(element);
                    final String text = Resource.this.getOverHeadInfos();
                    if (text.length() == 0) {
                        return;
                    }
                    final ArrayList<String> overIconUrl = new ArrayList<String>();
                    final Protector protector = ProtectorView.getInstance().getProtector();
                    final short resourceType = element.getReferenceResource().getResourceType();
                    if (protector != null) {
                        final ProtectorEcosystemHandler handler = protector.getEcosystemHandler();
                        if (handler.isProtectedResourceFamily(resourceType)) {
                            overIconUrl.add(WakfuConfiguration.getInstance().getIconUrl("ecosystemProtectedIconPath", "defaultIconPath", new Object[0]));
                        }
                    }
                    final UIShowOverHeadInfosMessage msg = new UIShowOverHeadInfosMessage(element, 0);
                    final ResourceType resourceTypeEnum = ResourceType.getByAgtIdOrHWCategory(resourceType);
                    if (resourceTypeEnum != null) {
                        String resourcePopupIconUrl = null;
                        try {
                            resourcePopupIconUrl = String.format(WakfuConfiguration.getInstance().getString("popupIconPath"), "resource" + resourceTypeEnum.getId());
                        }
                        catch (PropertyException e) {
                            Resource.m_logger.error((Object)e.getMessage(), (Throwable)e);
                        }
                        msg.setBreedIconUrl(resourcePopupIconUrl);
                    }
                    msg.addInfo(text, Resource.this.getResourceGrowingRateIconUrl(element), overIconUrl);
                    Worker.getInstance().pushMessage(msg);
                }
                else {
                    UIOverHeadInfosFrame.getInstance().hideOverHead(element);
                    MobileColorizeHelper.onLeave(element);
                }
            }
        });
    }
    
    public String getResourceGrowingRateIconUrl(final Resource resource) {
        if (!resource.canBeCollected()) {
            return null;
        }
        final ResourceType id = ResourceType.getByAgtIdOrHWCategory(resource.getReferenceResource().getResourceType());
        if (id == null) {
            return null;
        }
        if (!id.isClimateDepending()) {
            return null;
        }
        final WeatherInfo weatherInfo = WeatherInfoManager.getInstance().getCurrentWeather().getWeatherInfo();
        if (weatherInfo == null) {
            return null;
        }
        final float temp = weatherInfo.getTemperature();
        final ReferenceResource ref = resource.getReferenceResource();
        final String name = ResourceTemperatureInfluence.getFromTemperature(temp, ref.getExtendedTemperatureMin(), ref.getIdealTemperatureMin(), ref.getIdealTemperatureMax(), ref.getExtendedTemperatureMax()).name();
        return WakfuConfiguration.getInstance().getIconUrl("temperatureInfluenceIconUrl", "defaultIconPath", name);
    }
    
    private void destroyLightSource() {
        if (this.m_lightSource != null) {
            IsoSceneLightManager.INSTANCE.shutdownLight(this.m_lightSource.getId(), 500);
            this.m_lightSource = null;
        }
    }
    
    private void updateLightSource() {
        this.destroyLightSource();
        final ResourceEvolutionStep evolutionStep = this.m_referenceResource.getEvolutionStep(this.m_evolutionStep);
        if (!evolutionStep.hasLight()) {
            return;
        }
        (this.m_lightSource = (IsoLightSource)LightSourceManagerDelegate.INSTANCE.createLightSource()).setTarget(this);
        final float[] intensity = evolutionStep.getLightIntensity();
        this.m_lightSource.setBaseColor(0.0f, 0.0f, 0.0f);
        this.m_lightSource.setSaturation(intensity[0], intensity[1], intensity[2]);
        this.m_lightSource.setAttenuation(0.0f, 0.0f, 0.2f);
        this.m_lightSource.setRange(evolutionStep.getLightRange());
        this.m_lightSource.setNightOnly(false);
        IsoSceneLightManager.INSTANCE.addLight(this.m_lightSource);
    }
    
    public boolean canBeCollected() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ResourceEvolutionStep evolutionStep = this.m_referenceResource.getEvolutionStep(this.m_evolutionStep);
        for (int i = 0, size = evolutionStep.getCollectsCount(); i < size; ++i) {
            final CollectAction collect = evolutionStep.getQuickCollect(i);
            if (collect.getCraftId() == 0) {
                return true;
            }
            if (localPlayer.getCraftHandler().contains(collect.getCraftId())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isBlockingMovement() {
        return this.getEvolutionStep() != 16 && this.m_referenceResource.isBlocking();
    }
    
    @Override
    public void onCheckOut() {
        this.m_evolutionStep = 0;
        this.m_referenceResource = null;
        this.m_autoRespawn = false;
        this.setVisible(this.m_canBeDeleted = false);
        this.addAnimationEndedListener(Resource.m_animationHandler);
        this.setDirection(Direction8.SOUTH_EAST);
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_shadow) {
            ResourceShadowCast.INSTANCE.removeResource(this);
            this.m_shadow = false;
        }
        this.destroyLightSource();
        this.dispose();
        this.m_evolutionStep = 0;
        this.m_referenceResource = null;
        this.m_isLoaded = false;
        UIOverHeadInfosFrame.getInstance().hideOverHead(this);
    }
    
    @Override
    public boolean playAction(final AnmAction action) {
        if (action.getType() == AnmActionTypes.HIT) {
            this.setAnimation(this.m_animationName = ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)4, this.m_evolutionStep));
            return true;
        }
        return false;
    }
    
    @Override
    public void onAnimatedObjectActionFlag(final ArrayList<AnmAction> actions) {
        for (final AnmAction action : actions) {
            if (action.getType() == AnmActionTypes.DELETE) {
                this.m_canBeDeleted = !this.m_autoRespawn;
                if (!this.m_autoRespawn) {
                    continue;
                }
                this.setVisible(false);
            }
            else if (action.getType() == AnmActionTypes.END) {
                this.selectAnimation(this.m_evolutionStep, false);
            }
            else {
                action.run(this);
            }
        }
    }
    
    public boolean canBeDeleted() {
        return this.m_canBeDeleted;
    }
    
    public AbstractMRUAction[] getMRUSkillActions() {
        if (!this.isCraftMRUable()) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        if (WakfuGameEntity.getInstance().getLocalPlayer().isActiveProperty(WorldPropertyType.RESOURCE_COLLECT_DISABLED)) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final SortedList<MRUCollectAction> actions = new SortedList<MRUCollectAction>(Resource.MRU_COMPARATOR);
        final ResourceEvolutionStep evolutionStep = this.m_referenceResource.getEvolutionStep(this.m_evolutionStep);
        for (int i = 0, size = evolutionStep.getCollectsCount(); i < size; ++i) {
            final CollectAction collect = evolutionStep.getQuickCollect(i);
            actions.add(new MRUCollectAction(this, collect));
        }
        return actions.toArray(new AbstractMRUAction[actions.size()]);
    }
    
    private boolean isCraftMRUable() {
        return this.m_referenceResource.canBeCollected(this.m_evolutionStep);
    }
    
    public CollectAction getActionFromActionId(final int actionId) {
        return this.m_referenceResource.getEvolutionStep(this.m_evolutionStep).getCollectAction(actionId);
    }
    
    protected void loadAsynchGFX(final int[] updateTime, final int worldId) {
        final long start = System.nanoTime();
        try {
            final String gfxPath = this.m_referenceResource.getGfx((int)this.m_worldX, (int)this.m_worldY, worldId);
            if (gfxPath != null) {
                this.load(gfxPath, true);
                this.setGfxId(gfxPath);
                this.m_isLoaded = true;
            }
        }
        catch (Exception e) {
            Resource.m_logger.error((Object)"Erreur de cr\u00e9ation de ressource ", (Throwable)e);
        }
        finally {
            final long end = System.nanoTime();
            final int n = 0;
            updateTime[n] -= (int)((end - start) / 1000000L);
        }
    }
    
    public void release() {
        if (Resource.m_resourcePool != null) {
            try {
                Resource.m_resourcePool.returnObject(this);
            }
            catch (Exception e) {
                Resource.m_logger.error((Object)"Erreur lors du retour d'une resource au pool", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    private void selectAnimation(final byte step, final boolean launchTransition) {
        if (step == 16) {
            return;
        }
        this.setVisible(true);
        if (step == 0) {
            UIOverHeadInfosFrame.getInstance().hideOverHead(this);
            this.m_animationName = ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)0, this.m_evolutionStep);
        }
        else if (step < this.m_evolutionStep) {
            if (this.m_evolutionStep == 16) {
                if (launchTransition) {
                    this.m_animationName = ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)1, step);
                }
                else {
                    this.setStaticAnimationKey(this.m_animationName = ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)2, step));
                }
            }
            else {
                this.m_animationName = ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)3, this.m_evolutionStep, step);
                this.setStaticAnimationKey(ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)2, step));
            }
        }
        else if (launchTransition) {
            this.m_animationName = ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)1, step);
        }
        else {
            this.setStaticAnimationKey(this.m_animationName = ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)2, step));
        }
        this.setAnimation(this.m_animationName);
    }
    
    public void setEvolutionStep(final byte step, final boolean launchTransition) {
        this.selectAnimation(step, launchTransition);
        this.m_evolutionStep = step;
        this.updateBlocking();
        this.onResourceStepModification();
    }
    
    public void updateBlocking() {
        final TopologyMapInstance map = TopologyMapManager.getMapFromCell((short)this.getWorldCellX(), (short)this.getWorldCellY());
        if (map != null) {
            map.setCellBlocked(this.getWorldCellX(), this.getWorldCellY(), this.isBlockingMovement());
        }
        else {
            Resource.m_logger.warn((Object)("mise \u00e0 jour de l'\u00e9tat d'une ressource en (" + this.getWorldCellX() + ", " + this.getWorldCellY() + ") alors que la map est inconnue/pas charg\u00e9e\t"));
        }
    }
    
    public boolean update(final IsoWorldScene scene, final int deltaTime, final int[] updateTime) {
        if (!this.m_isLoaded) {
            if (updateTime[0] <= 0) {
                return false;
            }
            this.loadAsynchGFX(updateTime, MapManagerHelper.getWorldId());
        }
        return super.update(scene, deltaTime);
    }
    
    @Override
    public void load(final String fileName, final boolean async) throws IOException {
        super.load(fileName, async);
        this.onResourceStepModification();
    }
    
    private void onResourceStepModification() {
        this.updateLightSource();
        if (this.m_evolutionStep == 0 || this.m_evolutionStep == 16) {
            return;
        }
        final ResourceSizeCategory resourceSizeCategory = this.m_referenceResource.getEvolutionStep(this.m_evolutionStep).getSizeCategory();
        if (resourceSizeCategory == null) {
            Resource.m_logger.warn((Object)("resource sans resourceSizeCategory " + this.getOverHeadInfos() + " m_evolutionStep=" + this.m_evolutionStep));
            return;
        }
        this.setVisualHeight((short)(resourceSizeCategory.getHeight() / 10));
        this.canBeOccluder(resourceSizeCategory.isOccluder());
        if (resourceSizeCategory.isShadowed()) {
            if (!this.m_shadow) {
                ResourceShadowCast.INSTANCE.addResource(this);
                this.m_shadow = true;
            }
        }
        else if (this.m_shadow) {
            ResourceShadowCast.INSTANCE.removeResource(this);
            this.m_shadow = false;
        }
        if (ResourceSizeCategory.underMobile(resourceSizeCategory)) {
            this.setDeltaZ(LayerOrder.RESOURCE_SMALL.getDeltaZ());
        }
        else {
            this.setDeltaZ(LayerOrder.RESOURCE_HIGH.getDeltaZ());
        }
    }
    
    public byte getEvolutionStep() {
        return this.m_evolutionStep;
    }
    
    public boolean isAutoRespawn() {
        return this.m_autoRespawn;
    }
    
    public void setAutoRespawn(final boolean autoRespawn) {
        this.m_autoRespawn = autoRespawn;
    }
    
    public boolean canInteractWith() {
        return this.m_evolutionStep != 0 && this.m_evolutionStep != 16;
    }
    
    @Override
    public short getHeight() {
        if (this.m_referenceResource != null) {
            return this.m_referenceResource.getHeight();
        }
        return super.getHeight();
    }
    
    private String getOverHeadInfos() {
        if (this.getMRUSkillActions().length == 0) {
            return "";
        }
        final StringBuilder text = new StringBuilder();
        final boolean extended = this.displayExtendedInfos();
        if (extended) {
            text.append("[").append(this.getId()).append("] ");
        }
        text.append(this.m_referenceResource.getResourceName());
        if (extended) {
            text.append(" [").append(this.getWorldCellX()).append(", ").append(this.getWorldCellY()).append(", ").append(this.getWorldCellAltitude()).append("]");
            if (this.m_autoRespawn) {
                text.append(" (autoRespawn)");
            }
        }
        return text.toString();
    }
    
    @Override
    public int getIconId() {
        return 0;
    }
    
    @Override
    public Color getOverHeadborderColor() {
        return Color.GREEN;
    }
    
    @Override
    public String getFormatedOverheadText() {
        return this.getOverHeadInfos();
    }
    
    public ResourceSizeCategory getCurrentSizeCategory() {
        final ResourceEvolutionStep step = this.m_referenceResource.getEvolutionStep(this.m_evolutionStep);
        return step.getSizeCategory();
    }
    
    public ReferenceResource getReferenceResource() {
        return this.m_referenceResource;
    }
    
    @Override
    public String getStaticAnimationKey() {
        return ResourceAnimationKeyManager.getInstance().getAnimationKey((byte)2, this.getEvolutionStep());
    }
    
    public void setCurrentShadowValue(final float shadowValue) {
        this.m_shadowValue = shadowValue;
    }
    
    @Override
    public void applyLighting(final float[] colors) {
        final int n = 0;
        colors[n] *= this.m_shadowValue;
        final int n2 = 1;
        colors[n2] *= this.m_shadowValue;
        final int n3 = 2;
        colors[n3] *= this.m_shadowValue;
        super.applyLighting(colors);
    }
    
    @Override
    public boolean isHighlightable() {
        return this.getMRUSkillActions().length > 0 && super.isHighlightable();
    }
    
    @Override
    public Point3 getPosition() {
        return this.m_coordinates;
    }
    
    @Override
    public int getFamilyId() {
        return this.m_referenceResource.getResourceType();
    }
    
    @Override
    public short getInstanceId() {
        throw new UnsupportedOperationException("On ne devrait pas faire appel \u00e0 getInstanceId dans le client");
    }
    
    static {
        MRU_COMPARATOR = new Comparator<MRUCollectAction>() {
            @Override
            public int compare(final MRUCollectAction o1, final MRUCollectAction o2) {
                return o1.getCollectAction().getOrder() - o2.getCollectAction().getOrder();
            }
        };
        m_animationHandler = new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                if (element instanceof Resource && ((Resource)element).getEvolutionStep() == 0 && !((Resource)element).isAutoRespawn()) {
                    ((Resource)element).m_canBeDeleted = true;
                }
            }
        };
        Resource.m_canLoadResource = true;
        m_resourcePool = new MonitoredPool(new ObjectFactory<Resource>() {
            @Override
            public Resource makeObject() {
                return new Resource();
            }
        });
    }
}
