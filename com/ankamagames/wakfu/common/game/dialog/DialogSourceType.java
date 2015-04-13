package com.ankamagames.wakfu.common.game.dialog;

import com.ankamagames.baseImpl.common.clientAndServer.game.dialog.*;
import org.jetbrains.annotations.*;

public enum DialogSourceType implements AbstractDialogSourceType
{
    UNKNOWN(0), 
    PROTECTOR(1), 
    NPC(2), 
    DIALOG_MACHINE(3);
    
    private final byte m_id;
    
    private DialogSourceType(final int id) {
        this.m_id = (byte)id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getIllustrationId(final long id) {
        return this.name().toLowerCase() + id;
    }
    
    @NotNull
    public static DialogSourceType getFromId(final byte id) {
        final DialogSourceType[] v = values();
        for (int i = 0; i < v.length; ++i) {
            final DialogSourceType type = v[i];
            if (type.m_id == id) {
                return type;
            }
        }
        return DialogSourceType.UNKNOWN;
    }
}
