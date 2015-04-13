package com.ankamagames.wakfu.common.game.mount;

import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public enum MountType implements ExportableEnum
{
    NONE(0, MovementSpeed.MOUNT_SPEED1, "Pas une monture", 0), 
    DRAGOTURKEY_30(1, MovementSpeed.MOUNT_SPEED1, "Dragodinde 25%", 1), 
    DRAGOTURKEY_60(2, MovementSpeed.MOUNT_SPEED2, "Dragodinde 50%", 1), 
    DRAGOTURKEY_100(3, MovementSpeed.MOUNT_SPEED3, "Dragodinde 75%", 1);
    
    private final byte m_id;
    private final MovementSpeed m_movementSpeed;
    private final String m_mountDescription;
    private final int m_mountType;
    private final String m_animPlayerStatic;
    private final String m_animPlayerRun;
    private final String m_animPlayerRunEnd;
    private final String m_animPlayerJump;
    
    private MountType(final int id, final MovementSpeed movementSpeed, final String mountDescription, final int mountType) {
        super(name, ordinal);
        this.m_mountType = mountType;
        this.m_id = MathHelper.ensureByte(id);
        this.m_movementSpeed = movementSpeed;
        this.m_mountDescription = mountDescription;
        this.m_animPlayerStatic = "AnimStatique-Mnt" + this.m_mountType;
        String speedDesc = null;
        switch (this.m_movementSpeed) {
            case MOUNT_SPEED1: {
                speedDesc = "1";
                break;
            }
            case MOUNT_SPEED2: {
                speedDesc = "2";
                break;
            }
            default: {
                speedDesc = "3";
                break;
            }
        }
        this.m_animPlayerRun = "AnimCourse-Speed" + speedDesc + "-Mnt" + this.m_mountType;
        this.m_animPlayerRunEnd = "AnimCourse-Fin-Mnt" + this.m_mountType;
        this.m_animPlayerJump = "AnimRelique-Saut-Mnt" + this.m_mountType;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public MovementSpeed getMovementSpeed() {
        return this.m_movementSpeed;
    }
    
    public String getAnimPlayerStatic() {
        return this.m_animPlayerStatic;
    }
    
    public String getAnimPlayerRun() {
        return this.m_animPlayerRun;
    }
    
    public String getAnimPlayerRunEnd() {
        return this.m_animPlayerRunEnd;
    }
    
    public String getAnimPlayerJump() {
        return this.m_animPlayerJump;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_mountDescription;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    @Nullable
    public static MountType getFromId(final byte id) {
        for (final MountType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return MountType.NONE;
    }
}
