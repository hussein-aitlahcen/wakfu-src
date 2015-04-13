package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.game.worldPositionManager.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class WorldPositionMarkerManager extends AbstractWorldPositionMarkerManager implements TargetPositionListener<PathMobile>
{
    private static WorldPositionMarkerManager m_instance;
    
    private WorldPositionMarkerManager() {
        super();
        this.init();
    }
    
    public static WorldPositionMarkerManager getInstance() {
        return WorldPositionMarkerManager.m_instance;
    }
    
    private void init() {
        this.setValuePointManager(CompassWidget.INSTANCE);
    }
    
    @Override
    protected ValuePoint createPoint(final int type, final ValuePointManager manager, final Object value, final int arrowApsId, final int startX, final int startY, final int startZ, final boolean forceZ) {
        final ValuePoint point = new ValuePoint(manager, value, arrowApsId, startX, startY, startZ, forceZ);
        point.setIconUrl(WakfuConfiguration.getInstance().getIconUrl("compassTypePath", "defaultIconPath", type));
        return point;
    }
    
    @Override
    public void setPoint(final int type, final long id, final int x, final int y, final int z, final Object value, final ValuePointDeleteListener deleteListener, final boolean forceZ) {
        CompassWidget.INSTANCE.setTarget(WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        super.setPoint(type, id, x, y, z, value, deleteListener, forceZ);
        final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacter(id);
        if (characterInfo != null) {
            this.setScreenTarget(0, characterInfo.getId(), characterInfo.getActor());
        }
    }
    
    public void onCharacterSpawn(final PathMobile target) {
        if (target != null) {
            this.setScreenTarget(0, target.getId(), target);
        }
    }
    
    public void onCharacterDespawn(final PathMobile target) {
        if (target != null) {
            this.setScreenTarget(0, target.getId(), null);
        }
    }
    
    @Override
    public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
        this.updatePosition(0, target.getId(), worldX, worldY, altitude);
    }
    
    static {
        WorldPositionMarkerManager.m_instance = new WorldPositionMarkerManager();
    }
}
