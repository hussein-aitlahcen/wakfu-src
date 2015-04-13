package com.ankamagames.framework.kernel.utils;

public enum OS
{
    UNKNOWN("unknown"), 
    WINDOWS("windows"), 
    MAC("mac"), 
    SUNOS("sunos"), 
    LINUX("linux");
    
    private static OS m_os;
    private final String m_name;
    
    private OS(final String name) {
        this.m_name = name;
    }
    
    public static OS getCurrentOS() {
        return OS.m_os;
    }
    
    public static boolean isMacOs() {
        return OS.m_os == OS.MAC;
    }
    
    public static boolean isWindows() {
        return OS.m_os == OS.WINDOWS;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    static {
        final String lowerOs = System.getProperty("os.name").toLowerCase();
        for (final OS os : values()) {
            if (lowerOs.startsWith(os.getName())) {
                OS.m_os = os;
            }
        }
        if (OS.m_os == null) {
            OS.m_os = OS.UNKNOWN;
        }
    }
}
