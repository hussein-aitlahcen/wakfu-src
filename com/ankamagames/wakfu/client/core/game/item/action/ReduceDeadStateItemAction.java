package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class ReduceDeadStateItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    
    public ReduceDeadStateItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            ReduceDeadStateItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        boolean hasDeadState = false;
        final List<StateRunningEffect> states = character.getRunningEffectManager().getRunningState();
        for (int i = 0; i < states.size(); ++i) {
            final StateRunningEffect effect = states.get(i);
            if (effect.getState() == null || effect.getState().isStateForDeath()) {
                hasDeadState = true;
                break;
            }
        }
        if (!hasDeadState) {
            ReduceDeadStateItemAction.m_logger.warn((Object)"Tentative d'utilisation d'un item de reduction de DEAD_STATE sans en avoir");
            return false;
        }
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.REDUCE_DEAD_STATE;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReduceDeadStateItemAction.class);
    }
}
