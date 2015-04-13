package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class AddWorldPositionMarkerItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private long m_longPosition;
    private short m_instanceId;
    
    public AddWorldPositionMarkerItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        try {
            final PositionValue pos = CriteriaCompiler.compilePosition(params[0]);
            this.m_longPosition = pos.getLongValue(null, null, null, null);
        }
        catch (Exception e) {
            AddWorldPositionMarkerItemAction.m_logger.error((Object)("Impossible de parser la position : " + params[0]));
        }
        this.m_instanceId = Short.parseShort(params[1]);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            AddWorldPositionMarkerItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        final Point3 pos = PositionValue.fromLong(this.m_longPosition);
        MapManager.getInstance().removeUniqueCompass();
        MapManager.getInstance().addCompassPointAndPositionMarker(pos.getX(), pos.getY(), pos.getZ(), this.m_instanceId, item, true);
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.ADD_WORLD_POSITION_MARKER;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddWorldPositionMarkerItemAction.class);
    }
}
