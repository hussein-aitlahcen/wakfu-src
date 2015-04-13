package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class RunningEffectActionMessage extends AbstractFightActionMessage
{
    private int m_runningEffectId;
    byte[] m_serializedRunningEffect;
    private boolean m_triggered;
    private final ArrayList<ObjectTriplet<Integer, Integer, byte[]>> m_serializedTargetsWithCorrespondingAction;
    
    public RunningEffectActionMessage() {
        super();
        this.m_serializedTargetsWithCorrespondingAction = new ArrayList<ObjectTriplet<Integer, Integer, byte[]>>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_triggered = (bb.get() == 1);
        this.m_runningEffectId = bb.getInt();
        bb.get(this.m_serializedRunningEffect = new byte[bb.getShort()]);
        while (bb.hasRemaining()) {
            final byte[] serializedTarget = new byte[bb.getShort()];
            bb.get(serializedTarget);
            this.m_serializedTargetsWithCorrespondingAction.add(new ObjectTriplet<Integer, Integer, byte[]>(bb.getInt(), bb.getInt(), serializedTarget));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8120;
    }
    
    @Override
    public int getActionId() {
        return this.m_runningEffectId;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.EFFECT_EXECUTION;
    }
    
    public int getRunningEffectId() {
        return this.m_runningEffectId;
    }
    
    public ArrayList<ObjectTriplet<Integer, Integer, byte[]>> getSerializedTargetsWithCorrespondingAction() {
        return this.m_serializedTargetsWithCorrespondingAction;
    }
    
    public byte[] getSerializedRunningEffect() {
        return this.m_serializedRunningEffect;
    }
    
    public boolean isTriggered() {
        return this.m_triggered;
    }
}
