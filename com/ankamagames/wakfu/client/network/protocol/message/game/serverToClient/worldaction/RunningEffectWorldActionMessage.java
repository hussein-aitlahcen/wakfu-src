package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.worldaction;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.world.action.*;

public class RunningEffectWorldActionMessage extends AbstractWorldActionMessage
{
    private int m_runningEffectId;
    byte[] m_serializedRunningEffect;
    private boolean m_triggered;
    private boolean m_mustBeExecutedNow;
    private final ArrayList<ObjectTriplet<Integer, Integer, byte[]>> m_serializedTargetsWithCorrespondingAction;
    
    public RunningEffectWorldActionMessage() {
        super();
        this.m_mustBeExecutedNow = false;
        this.m_serializedTargetsWithCorrespondingAction = new ArrayList<ObjectTriplet<Integer, Integer, byte[]>>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeWorldActionHeader(bb);
        this.m_mustBeExecutedNow = (bb.get() == 1);
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
        return 6320;
    }
    
    @Override
    public int getActionId() {
        return this.m_runningEffectId;
    }
    
    @Override
    public WorldActionType getWorldActionType() {
        return WorldActionType.EFFECT_EXECUTION;
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
    
    public boolean isMustBeExecutedNow() {
        return this.m_mustBeExecutedNow;
    }
    
    public boolean isTriggered() {
        return this.m_triggered;
    }
}
