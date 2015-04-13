package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class RunningEffectDefinition extends ConstantDefinition<WakfuRunningEffect>
{
    public static final int NO_SCRIPT_ID = -1;
    public static final int ACTION_COST_ID = 1000;
    public static final int HP_LOSS_SCRIPT_ID = 1001;
    public static final int HP_GAIN_SCRIPT_ID = 1002;
    public static final int HP_LEECH_SCRIPT_ID = 1003;
    public static final int MP_LOSS_SCRIPT_ID = 1004;
    public static final int MP_GAIN_SCRIPT_ID = 1005;
    public static final int MP_LEECH_SCRIPT_ID = 1006;
    public static final int AP_LOSS_SCRIPT_ID = 1007;
    public static final int AP_GAIN_SCRIPT_ID = 1008;
    public static final int AP_LEECH_SCRIPT_ID = 1009;
    public static final int SLIDE_MOBILE_SCRIPT_ID = 1010;
    public static final int RG_GAIN_SCRIPT_ID = 1011;
    public static final int RG_LOSS_SCRIPT_ID = 1012;
    public static final int HP_GAIN_WITH_HURT_SCRIPT_ID = 1013;
    public static final int ARMOR_SCRIPT_ID = 1014;
    public static final int DISPLAY_EXECUTION_STATUS_SCRIPT_ID = 1015;
    public static final int ITEM_LOOT = 1016;
    public static final int CHRAGE_LOSS = 1017;
    public static final int CONSUME_CHRAGE_FUNCTION_RANGE = 1018;
    public static final int SLIDE_CASTER_SCRIPT_ID = 1029;
    public static final int AREA_HP_LOSS = 1031;
    public static final int WP_GAIN = 1032;
    public static final int MOVEMENT_EFFECT_SCRIPT = 1033;
    public static final int WP_LOSS = 1035;
    public static final int CHANGE_DIRECTION_SCRIPT = 1039;
    public static final int TACKLE_COST = 1041;
    private String m_adminDescription;
    private int m_scriptId;
    
    public RunningEffectDefinition(final int id, final WakfuRunningEffect object, final Constants<WakfuRunningEffect> constants, final int scriptId, final String decription, final RunningEffectStatus status) {
        super(id, object, constants);
        object.setId(id);
        object.setRunningEffectStatus(status);
        object.initialiseParent();
        this.m_adminDescription = decription;
        this.m_scriptId = scriptId;
    }
    
    @Override
    public String getAdminDescription() {
        return this.m_adminDescription;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    public boolean checkParamsCount(final int count) {
        return this.getObject().getParametersListSet().mapValueCount(count);
    }
}
