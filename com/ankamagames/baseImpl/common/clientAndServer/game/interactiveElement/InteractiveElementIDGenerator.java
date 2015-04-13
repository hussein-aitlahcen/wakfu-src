package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

public final class InteractiveElementIDGenerator
{
    private static final long STATIC_ID_BASE_VALUE = 4611686018427387903L;
    private static long m_initialDynamicId;
    private static long m_baseDynamicId;
    
    public static void setBaseDynamicId(final long baseDynamicId) {
        if (baseDynamicId >= 0L) {
            throw new IndexOutOfBoundsException("L'id de base pour la g\u00e9n\u00e9ration dynamique doit etre n\u00e9gatif.");
        }
        InteractiveElementIDGenerator.m_baseDynamicId = baseDynamicId;
    }
    
    public static long idToStaticId(final long id) {
        if (id < 0L) {
            return id;
        }
        if (id >= 4611686018427387903L) {
            throw new IndexOutOfBoundsException("L'ID sp\u00e9cifi\u00e9 \u00e9crase une plage d'ID existante id=" + id + " >= " + 4611686018427387903L);
        }
        return 4611686018427387903L + id;
    }
    
    public static long baseId(final long statOrDynId) {
        if (statOrDynId > 4611686018427387903L) {
            return statOrDynId - 4611686018427387903L;
        }
        return statOrDynId;
    }
    
    public static boolean isStatic(final long id) {
        return id >= 4611686018427387903L;
    }
    
    public static boolean isDynamic(final long id) {
        return id < 0L;
    }
    
    public static long nextDynamicId() {
        return InteractiveElementIDGenerator.m_baseDynamicId - InteractiveElementIDGenerator.m_initialDynamicId--;
    }
    
    static {
        InteractiveElementIDGenerator.m_initialDynamicId = -1L;
        InteractiveElementIDGenerator.m_baseDynamicId = 0L;
    }
}
