package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ParameterizedFightCreationRequestMessage extends OutputOnlyProxyMessage
{
    private long m_targetId;
    private int m_x;
    private int m_y;
    private short m_z;
    private boolean m_teleportFighters;
    private int m_fightType;
    private boolean m_withBorders;
    private byte m_battleGroundType;
    private Point3 m_battlegroundCenter;
    private int[] m_bgParams;
    private Point3 m_attackerPos;
    private Point3 m_defenderPos;
    
    public ParameterizedFightCreationRequestMessage() {
        super();
        this.m_teleportFighters = false;
        this.m_fightType = 2;
        this.m_withBorders = true;
        this.m_battleGroundType = 1;
        this.m_battlegroundCenter = null;
        this.m_bgParams = null;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray array = new ByteArray();
        array.putLong(this.m_targetId);
        array.putInt(this.m_x);
        array.putInt(this.m_y);
        array.putShort(this.m_z);
        array.put((byte)(this.m_teleportFighters ? 1 : 0));
        array.putInt(this.m_fightType);
        array.put((byte)(this.m_withBorders ? 1 : 0));
        array.put(this.m_battleGroundType);
        if (this.m_battlegroundCenter == null) {
            array.put((byte)0);
        }
        else {
            array.put((byte)1);
            array.putInt(this.m_battlegroundCenter.getX());
            array.putInt(this.m_battlegroundCenter.getY());
            array.putShort(this.m_battlegroundCenter.getZ());
        }
        if (this.m_bgParams == null) {
            array.put((byte)0);
        }
        else {
            array.put((byte)this.m_bgParams.length);
            for (byte i = 0; i < this.m_bgParams.length; ++i) {
                array.putInt(this.m_bgParams[i]);
            }
        }
        if (this.m_attackerPos == null) {
            array.put((byte)0);
        }
        else {
            array.put((byte)1);
            array.putInt(this.m_attackerPos.getX());
            array.putInt(this.m_attackerPos.getY());
            array.putShort(this.m_attackerPos.getZ());
        }
        if (this.m_defenderPos == null) {
            array.put((byte)0);
        }
        else {
            array.put((byte)1);
            array.putInt(this.m_defenderPos.getX());
            array.putInt(this.m_defenderPos.getY());
            array.putShort(this.m_defenderPos.getZ());
        }
        return this.addClientHeader((byte)3, array.toArray());
    }
    
    @Override
    public int getId() {
        return 8007;
    }
    
    public void setTargetId(final long targetId) {
        this.m_targetId = targetId;
    }
    
    public void setTargetPosition(final int x, final int y, final short z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public void setBattlegroundCenter(final Point3 battlegroundCenter) {
        this.m_battlegroundCenter = battlegroundCenter;
    }
    
    public void setBattleGroundType(final byte battleGroundType) {
        this.m_battleGroundType = battleGroundType;
    }
    
    public void setBgParams(final int[] bgParams) {
        this.m_bgParams = bgParams;
    }
    
    public void setFightType(final int fightType) {
        this.m_fightType = fightType;
    }
    
    public void setTeleportFighters(final boolean teleportFighters) {
        this.m_teleportFighters = teleportFighters;
    }
    
    public void setWithBorders(final boolean withBorders) {
        this.m_withBorders = withBorders;
    }
    
    public Point3 getAttackerPos() {
        return this.m_attackerPos;
    }
    
    public void setAttackerPos(final Point3 attackerPos) {
        this.m_attackerPos = attackerPos;
    }
    
    public Point3 getDefenderPos() {
        return this.m_defenderPos;
    }
    
    public void setDefenderPos(final Point3 defenderPos) {
        this.m_defenderPos = defenderPos;
    }
}
