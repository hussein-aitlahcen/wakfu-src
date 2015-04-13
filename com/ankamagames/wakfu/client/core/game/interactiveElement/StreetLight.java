package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class StreetLight extends WakfuClientMapInteractiveElement implements ChaosInteractiveElement
{
    private static final Logger m_logger;
    private IEStreetLightParameter m_info;
    private int m_lightId;
    private int m_particleSystemId;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        StreetLight.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.runScript(action);
        this.sendActionMessage(action);
        return true;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        final boolean extinction = this.m_state == 1 && this.m_info.getExtinctionVisualId() >= 0;
        final boolean ignition = this.m_state == 0 && this.m_info.getIgnitionVisualId() >= 0;
        return (extinction || ignition) ? InteractiveElementAction.ACTIVATE : InteractiveElementAction.NONE;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final ActionVisual visual = this.getVisual();
        if (visual == null) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final MRUActionWithCost action = MRUActions.STREET_LIGHT_ACTION.getMRUAction();
        action.setGfxId(visual.getMruGfx());
        final boolean checkItem = this.checkItem();
        action.setTextKey("desc.mru." + visual.getMruLabelKey());
        if (this.getVisual().needItem()) {
            action.setCostText(LootChest.getCostText(this.getVisual().getAssociatedItems()[0], 1, 0));
        }
        action.setCanPay(true);
        action.setEnabled(visual.isEnabled() && checkItem);
        return new AbstractMRUAction[] { action };
    }
    
    private boolean checkItem() {
        return this.getVisual().isValidFor(WakfuGameEntity.getInstance().getLocalPlayer());
    }
    
    private ActionVisual getVisual() {
        ActionVisual visual = null;
        switch (this.m_state) {
            case 1: {
                visual = ActionVisualManager.getInstance().get(this.m_info.getExtinctionVisualId());
                break;
            }
            case 0: {
                visual = ActionVisualManager.getInstance().get(this.m_info.getIgnitionVisualId());
                break;
            }
            default: {
                throw new IllegalStateException("State " + this.m_state + " inconnu pour un Lampadaire");
            }
        }
        return visual;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_info = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        this.m_lightId = 0;
        this.m_particleSystemId = 0;
        assert this.m_info == null;
    }
    
    @Override
    protected void onStateChanged() {
        super.onStateChanged();
        switch (this.m_state) {
            case 1: {
                this.turnOn();
                break;
            }
            case 0: {
                this.turnOff();
                break;
            }
        }
    }
    
    private void turnOff() {
        IsoSceneLightManager.INSTANCE.shutdownLight(this.m_lightId, 1000);
        final IsoParticleSystem particleSystem = IsoParticleSystemManager.getInstance().getParticleSystem(this.m_particleSystemId);
        if (particleSystem != null) {
            particleSystem.stopAndKill(500);
        }
        this.m_lightId = -1;
        this.m_particleSystemId = -1;
    }
    
    private void turnOn() {
        this.turnOff();
        try {
            final LightSource lightSource = LightSourceManagerDelegate.INSTANCE.createLightSource();
            this.m_lightId = lightSource.getId();
            final Color c = new Color(this.m_info.getColor());
            lightSource.setBaseColor(0.0f, 0.0f, 0.0f);
            lightSource.setSaturation(0.0f, 0.0f, 0.0f);
            lightSource.saturationFadeTo(c.getRed(), c.getGreen(), c.getBlue(), 1000);
            lightSource.setPosition(new Vector3(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude()));
            lightSource.setRange(this.m_info.getRange());
            lightSource.setEnabled(true);
            lightSource.setNightOnly(this.m_info.isNightOnly());
            IsoSceneLightManager.INSTANCE.addLight(lightSource);
            final int apsId = this.m_info.getApsId();
            if (apsId > 0) {
                final IsoParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getCellParticleSystem(apsId);
                if (particleSystem != null) {
                    particleSystem.setPosition(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude() + this.getHeight());
                    IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
                    this.m_particleSystemId = particleSystem.getId();
                }
                else {
                    StreetLight.m_logger.error((Object)("Particule non trouv\u00e9e " + apsId));
                }
            }
        }
        catch (Exception ex) {
            StreetLight.m_logger.error((Object)"", (Throwable)ex);
        }
    }
    
    @Override
    public boolean isUseSpecificAnimTransition() {
        return true;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        StreetLight.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            StreetLight.m_logger.error((Object)("[LD] Le lampadaire " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        final IEStreetLightParameter param = (IEStreetLightParameter)IEParametersManager.INSTANCE.getParam(IETypes.STREET_LIGHT, Integer.valueOf(params[0]));
        if (param == null) {
            StreetLight.m_logger.error((Object)("[LD] Le lampadaire " + this.m_id + " \u00e0 un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_info = param;
        this.setOverHeadable(false);
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return this.m_info;
    }
    
    @Override
    public void onDeSpawn() {
        IsoSceneLightManager.INSTANCE.removeLight(this.m_lightId);
        IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_particleSystemId);
        super.onDeSpawn();
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    static {
        m_logger = Logger.getLogger((Class)StreetLight.class);
    }
    
    public static class StreetLightFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            StreetLight light;
            try {
                light = (StreetLight)StreetLightFactory.m_pool.borrowObject();
                light.setPool(StreetLightFactory.m_pool);
            }
            catch (Exception e) {
                StreetLight.m_logger.error((Object)"Erreur lors de l'extraction d'un StreetLight du pool", (Throwable)e);
                light = new StreetLight();
            }
            return light;
        }
        
        static {
            StreetLightFactory.m_pool = new MonitoredPool(new ObjectFactory<StreetLight>() {
                @Override
                public StreetLight makeObject() {
                    return new StreetLight();
                }
            });
        }
    }
}
