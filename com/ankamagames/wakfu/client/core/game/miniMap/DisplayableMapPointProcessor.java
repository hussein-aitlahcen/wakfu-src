package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.landMarks.agtEnum.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.common.game.respawn.*;
import com.ankamagames.wakfu.common.game.travel.character.*;

public abstract class DisplayableMapPointProcessor
{
    public static final DragoDisplayableMapPointProcessor DRAGO_MAP_PROCESSOR;
    public static final DisplayableMapPointProcessor DEFAULT;
    
    public abstract void process(final DisplayableMapPoint p0);
    
    private static boolean isVisible(final DisplayableMapPoint point) {
        if (point.getValue() instanceof InteractiveElementDef) {
            final InteractiveElementDef ie = (InteractiveElementDef)point.getValue();
            switch (ie.m_type) {
                case 48: {
                    final TravelInfo info = TravelInfoManager.INSTANCE.getInfo(TravelType.BOAT, ie.m_id);
                    return info.getLandmarkTravelType() != null;
                }
            }
        }
        else if (point.getValue() instanceof LandMarkDef) {
            final LandMarkDef def = (LandMarkDef)point.getValue();
            if (def.m_achievementGoalId != -1) {
                final ClientAchievementsContext context = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext();
                return !context.hasObjective(def.m_achievementGoalId) || context.isObjectiveCompleted(def.m_achievementGoalId);
            }
            if (def.m_type == LandMarkEnum.CRAFT.getType()) {
                return def.m_subType <= 0 || WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler().contains(def.m_subType);
            }
        }
        return true;
    }
    
    static void setPointIcon(final DisplayableMapPoint point, final TravelType type) {
        switch (type) {
            case BOAT: {
                point.setIcon(DisplayableMapPointIconFactory.BOAT_ICON);
                break;
            }
            case CANNON: {
                point.setIcon(DisplayableMapPointIconFactory.CANNON_ICON);
                break;
            }
            case DRAGO: {
                point.setIcon(DisplayableMapPointIconFactory.DRAGO_ICON);
                break;
            }
            case ZAAP: {
                point.setIcon(DisplayableMapPointIconFactory.ZAAP_ICON);
                break;
            }
        }
    }
    
    static {
        DRAGO_MAP_PROCESSOR = new DragoDisplayableMapPointProcessor();
        DEFAULT = new DisplayableMapPointProcessor() {
            @Override
            public void process(final DisplayableMapPoint point) {
                if (!isVisible(point)) {
                    point.setVisible(false);
                    return;
                }
                boolean useGrayScale = false;
                if (point.getValue() instanceof InteractiveElementDef) {
                    final InteractiveElementDef ie = (InteractiveElementDef)point.getValue();
                    switch (ie.m_type) {
                        case 16: {
                            final RespawnPointHandler respawnPointHandler = WakfuGameEntity.getInstance().getLocalPlayer().getRespawnPointHandler();
                            useGrayScale = !respawnPointHandler.isSelected((int)ie.m_id);
                            break;
                        }
                        case 47: {
                            final TravelInfo info = TravelInfoManager.INSTANCE.getInfo(TravelType.DRAGO, ie.m_id);
                            DisplayableMapPointProcessor.setPointIcon(point, info.getLandmarkTravelType());
                            final TravelHandler travelHandler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
                            useGrayScale = !travelHandler.isDragoDiscovered((int)ie.m_id);
                            break;
                        }
                        case 26: {
                            final TravelHandler travelHandler2 = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
                            final TravelInfo info2 = TravelInfoManager.INSTANCE.getInfo(TravelType.ZAAP, ie.m_id);
                            useGrayScale = !travelHandler2.isZaapDiscovered((int)ie.m_id);
                            DisplayableMapPointProcessor.setPointIcon(point, info2.getLandmarkTravelType());
                            break;
                        }
                        case 49: {
                            final TravelHandler travelHandler2 = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
                            final TravelInfo info2 = TravelInfoManager.INSTANCE.getInfo(TravelType.CANNON, ie.m_id);
                            useGrayScale = !travelHandler2.isCannonDiscovered((int)ie.m_id);
                            DisplayableMapPointProcessor.setPointIcon(point, info2.getLandmarkTravelType());
                            break;
                        }
                        case 48: {
                            final TravelInfo info = TravelInfoManager.INSTANCE.getInfo(TravelType.BOAT, ie.m_id);
                            DisplayableMapPointProcessor.setPointIcon(point, info.getLandmarkTravelType());
                            break;
                        }
                    }
                }
                point.setVisible(true);
                point.setHighlightOnOver(false);
                point.setUseGrayScale(point.isUseGrayScale() || useGrayScale);
            }
        };
    }
    
    public static class DragoDisplayableMapPointProcessor extends DisplayableMapPointProcessor
    {
        private long m_currentDragoId;
        
        public void setCurrentDragoId(final long currentdragoId) {
            this.m_currentDragoId = currentdragoId;
        }
        
        @Override
        public void process(final DisplayableMapPoint point) {
            if (!isVisible(point)) {
                point.setVisible(false);
                return;
            }
            if (point.getValue() instanceof InteractiveElementDef) {
                final InteractiveElementDef ie = (InteractiveElementDef)point.getValue();
                if (ie.m_type == 47) {
                    if (ie.m_id == this.m_currentDragoId) {
                        point.setVisible(false);
                        return;
                    }
                    point.setUseGrayScale(true);
                    final TravelInfo info = TravelInfoManager.INSTANCE.getInfo(TravelType.DRAGO, ie.m_id);
                    DisplayableMapPointProcessor.setPointIcon(point, info.getLandmarkTravelType());
                    final TravelHandler travelHandler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
                    if (travelHandler.canUseDrago((int)ie.m_id)) {
                        point.setVisible(true);
                        point.setHighlightOnOver(true);
                        point.setUseGrayScale(false);
                        return;
                    }
                }
            }
            point.setVisible(true);
            point.setHighlightOnOver(false);
            point.setUseGrayScale(true);
        }
    }
}
