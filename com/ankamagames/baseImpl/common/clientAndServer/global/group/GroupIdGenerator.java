package com.ankamagames.baseImpl.common.clientAndServer.global.group;

import org.apache.log4j.*;
import gnu.trove.*;

public class GroupIdGenerator
{
    protected static final Logger m_logger;
    private static long GROUP_ID_MAX_VALUE;
    private static TByteLongHashMap m_currentGroupId;
    
    public static long getNewId(final byte groupType) {
        final long id = GroupIdGenerator.m_currentGroupId.adjustOrPutValue(groupType, 1L, 1L);
        return getId(groupType, id);
    }
    
    public void setCurrentGroupId(final byte groupType, final long currentId) {
        GroupIdGenerator.m_currentGroupId.put(groupType, currentId);
    }
    
    public static byte getTypeFromId(final long groupId) {
        return (byte)(groupId >> 56);
    }
    
    public static long getBaseId(final long currentGroupId) {
        return currentGroupId & 0xFFFFFFFFFFFFFFL;
    }
    
    public static void setCurrentId(final byte groupType, final long id) {
        GroupIdGenerator.m_currentGroupId.put(groupType, id);
    }
    
    public static long getId(final byte groupType, final long baseId) {
        if (baseId > GroupIdGenerator.GROUP_ID_MAX_VALUE) {
            GroupIdGenerator.m_logger.fatal((Object)"[GROUP ID] D\u00e9passement de capacit\u00e9 pour les ID de groupe");
            return 0L;
        }
        final long l = baseId & 0xFFFFFFFFFFFFFFL;
        final long b = groupType & 0xFFL;
        return b << 56 | l;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GroupIdGenerator.class);
        GroupIdGenerator.GROUP_ID_MAX_VALUE = 72057594037927935L;
        GroupIdGenerator.m_currentGroupId = new TByteLongHashMap();
    }
}
