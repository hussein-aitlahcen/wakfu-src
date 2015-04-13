package com.ankamagames.wakfu.client.core.game.dungeon;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.basicDungeon.*;
import com.ankamagames.wakfu.common.game.lock.*;

public class DungeonView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String LEVEL_RANGE = "levelRange";
    public static final String LOCATION = "location";
    public static final String IS_LOCKED = "isLocked";
    public static final String LOCK_DESC = "lockDesc";
    private final int m_id;
    private final int m_lockId;
    
    public DungeonView(final int id) {
        super();
        this.m_id = id;
        this.m_lockId = LockManager.INSTANCE.getLockByLockedItemId(id);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("levelRange")) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(this.getMinLevel());
            return WakfuTranslator.getInstance().getString("levelShort.custom", sb.finishAndToString());
        }
        if (fieldName.equals("location")) {
            return this.getInstanceDesc();
        }
        if (fieldName.equals("isLocked")) {
            return this.isLocked();
        }
        if (!fieldName.equals("lockDesc")) {
            return null;
        }
        final String lockText = LockHelper.getLockText(WakfuGameEntity.getInstance().getLocalPlayer(), this.m_lockId);
        if (lockText.length() == 0) {
            return null;
        }
        final TextWidgetFormater sb2 = new TextWidgetFormater();
        sb2.b().append(lockText)._b();
        return sb2.finishAndToString();
    }
    
    public int getId() {
        return this.m_id;
    }
    
    private String getName() {
        return WakfuTranslator.getInstance().getString(137, this.m_id, new Object[0]);
    }
    
    public short getMinLevel() {
        final DungeonDefinition dungeon = DungeonManager.INSTANCE.getDungeon(this.m_id);
        return (short)((dungeon != null) ? dungeon.getMinLevel() : 0);
    }
    
    public short getInstanceId() {
        final DungeonDefinition dungeon = DungeonManager.INSTANCE.getDungeon(this.m_id);
        return (short)((dungeon != null) ? dungeon.getInstanceId() : 0);
    }
    
    private String getInstanceDesc() {
        final DungeonDefinition dungeon = DungeonManager.INSTANCE.getDungeon(this.m_id);
        if (dungeon == null) {
            return "";
        }
        return WakfuTranslator.getInstance().getString(77, dungeon.getInstanceId(), new Object[0]);
    }
    
    public boolean isLevelValid() {
        final short level = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
        return level >= this.getMinLevel();
    }
    
    public boolean isLocked() {
        return this.getContext().isLocked(this.m_lockId);
    }
    
    private LockContext getContext() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getLockContext();
    }
}
