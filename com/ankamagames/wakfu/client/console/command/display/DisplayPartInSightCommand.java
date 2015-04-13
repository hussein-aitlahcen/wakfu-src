package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;

public final class DisplayPartInSightCommand implements Command
{
    private static final Logger m_logger;
    private static final String FRONT_LAYER = "DEBUG_FRONT_LAYER";
    private static final String BACK_LAYER = "DEBUG_BACK_LAYER";
    private static final String SIDE_LAYER = "DEBUG_SIDE_LAYER";
    private static final float[] DEBUG_FRONT_LAYER_COLOR;
    private static final float[] DEBUG_BACK_LAYER_COLOR;
    private static final float[] DEBUG_SIDE_LAYER_COLOR;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final PartLocalisator partLocalisator = localPlayer.getPartLocalisator();
        this.createHighLightLayer("DEBUG_FRONT_LAYER", DisplayPartInSightCommand.DEBUG_FRONT_LAYER_COLOR);
        this.createHighLightLayer("DEBUG_BACK_LAYER", DisplayPartInSightCommand.DEBUG_BACK_LAYER_COLOR);
        this.createHighLightLayer("DEBUG_SIDE_LAYER", DisplayPartInSightCommand.DEBUG_SIDE_LAYER_COLOR);
        final int x = localPlayer.getWorldCellX();
        final int y = localPlayer.getWorldCellY();
        for (int i = -10; i < 10; ++i) {
            for (int j = -10; j < 10; ++j) {
                final Part partInSight = partLocalisator.getMainPartInSightFromPosition(x + i, y + j, localPlayer.getWorldCellAltitude());
                final int partId = partInSight.getPartId();
                final long hash = HighLightManager.getHandle(x + i, y + j, localPlayer.getWorldCellAltitude());
                switch (partId) {
                    case 0: {
                        HighLightManager.getInstance().add(hash, "DEBUG_FRONT_LAYER");
                        break;
                    }
                    case 2: {
                        HighLightManager.getInstance().add(hash, "DEBUG_BACK_LAYER");
                        break;
                    }
                    case 1:
                    case 3: {
                        HighLightManager.getInstance().add(hash, "DEBUG_SIDE_LAYER");
                        break;
                    }
                }
            }
        }
    }
    
    private void createHighLightLayer(final String layerName, final float[] layerColor) {
        HighLightLayer highLightPathLayer = HighLightManager.getInstance().getLayer(layerName);
        if (highLightPathLayer == null) {
            try {
                highLightPathLayer = HighLightManager.getInstance().createLayer(layerName);
                highLightPathLayer.setColor(layerColor);
            }
            catch (Exception e) {
                DisplayPartInSightCommand.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        HighLightManager.getInstance().clearLayer(layerName);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DisplayPartInSightCommand.class);
        DEBUG_FRONT_LAYER_COLOR = new float[] { 1.0f, 0.0f, 0.0f, 0.5f };
        DEBUG_BACK_LAYER_COLOR = new float[] { 0.0f, 1.0f, 0.0f, 0.5f };
        DEBUG_SIDE_LAYER_COLOR = new float[] { 0.0f, 0.0f, 1.0f, 0.5f };
    }
}
