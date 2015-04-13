package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.maths.*;

public enum InteractiveElementAction implements ExportableEnum
{
    ACTIVATE(0), 
    TURN_ON(1), 
    TURN_OFF(2), 
    PUSH(3), 
    PULL(4), 
    WALKON(5), 
    SITON(6), 
    STANDUP(7), 
    NONE(8), 
    WALKIN(9), 
    WALKOUT(10), 
    ENTER(11), 
    START_BROWSING(12), 
    STOP_BROWSING(13), 
    START_MANAGE(14), 
    STOP_MANAGE(15), 
    CONFIRM_COLLECT(16), 
    OPEN(17), 
    CLOSE(18), 
    LOCK(19), 
    UNLOCK(20), 
    DISCONNECT_AND_ACTIVATE(21), 
    CHALLENGE_ACTIVATE(22), 
    REPACK(23), 
    REGISTER(24), 
    VOTE(25), 
    RECYCLE(26), 
    MOVE(27), 
    READ(28), 
    ROTATE(29), 
    ASK_INFORMATIONS(30), 
    ACTIVATE2(31), 
    ACTIVATE3(32), 
    ACTIVATE4(33), 
    ACTIVATE5(34), 
    ACTIVATE6(35), 
    ACTIVATE7(36), 
    ACTIVATE8(37), 
    CHAOS_ACTIVATE(38);
    
    private final short m_id;
    public static final InteractiveElementAction[] GENERIC_ACTIONS;
    public static final InteractiveElementAction[] EMPTY_ACTIONS;
    
    private InteractiveElementAction(final int id) {
        this.m_id = MathHelper.ensureShort(id);
    }
    
    public static InteractiveElementAction valueOf(final short actionId) {
        for (final InteractiveElementAction value : values()) {
            if (value.m_id == actionId) {
                return value;
            }
        }
        return null;
    }
    
    public short getActionId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    static {
        GENERIC_ACTIONS = new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE, InteractiveElementAction.ACTIVATE2, InteractiveElementAction.ACTIVATE3, InteractiveElementAction.ACTIVATE4, InteractiveElementAction.ACTIVATE5, InteractiveElementAction.ACTIVATE6, InteractiveElementAction.ACTIVATE7, InteractiveElementAction.ACTIVATE8 };
        EMPTY_ACTIONS = new InteractiveElementAction[0];
    }
}
