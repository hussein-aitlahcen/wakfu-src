package com.ankamagames.wakfu.client.alea.graphics.tacticalView;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class TacticalViewManager
{
    private static final Logger m_logger;
    private static final TacticalViewManager m_instance;
    private boolean m_activated;
    private Fight m_fight;
    
    public static TacticalViewManager getInstance() {
        return TacticalViewManager.m_instance;
    }
    
    public boolean isActivated() {
        return this.m_activated;
    }
    
    public void activate(final boolean activate) {
        if (this.m_activated != activate) {
            this.m_activated = activate;
            this.apply();
        }
    }
    
    public void setFight(final Fight fight) {
        if (fight != this.m_fight) {
            this.m_fight = fight;
            this.apply();
        }
    }
    
    private void apply() {
        final AleaWorldSceneWithParallax worldScene = WakfuClientInstance.getInstance().getWorldScene();
        if (this.m_activated && this.m_fight != null) {
            try {
                IsoSceneLightManager.INSTANCE.enableLightning(false);
                worldScene.setEntityFilter(new SceneEntityFilterFight(this.m_fight));
                worldScene.displayParallax(false);
                worldScene.setBackgoundColor(Color.BLACK);
            }
            catch (Exception e) {
                TacticalViewManager.m_logger.error((Object)"", (Throwable)e);
                this.deactivate(worldScene);
            }
        }
        else {
            this.deactivate(worldScene);
        }
    }
    
    private void deactivate(final AleaWorldSceneWithParallax worldScene) {
        IsoSceneLightManager.INSTANCE.enableLightning(true);
        worldScene.setEntityFilter(SceneEntityFilter.DEFAULT);
        worldScene.displayParallax(true);
        this.setCurrentBackgroundColor(worldScene);
    }
    
    private void setCurrentBackgroundColor(final AleaWorldSceneWithParallax worldScene) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final int worldId = localPlayer.getInstanceId();
        final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo((short)worldId);
        if (info != null) {
            worldScene.setBackgoundColor(info.m_backGroundColor);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TacticalViewManager.class);
        m_instance = new TacticalViewManager();
    }
}
