package com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors;

import java.util.regex.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;

public abstract class CommandPattern
{
    private static final String CMD_PATTERN_SUFFIX = "(?:\\s+.*|$)";
    private static final String ARGS_PATTERN_POST_PREFIX = "\\s+";
    private String m_name;
    private Pattern m_cmdPattern;
    private Pattern m_argsPattern;
    private byte m_level;
    private boolean m_autoCompletion;
    
    public CommandPattern(final String cmdRegex, final String argsRegex, final boolean allowNoArg) {
        super();
        this.m_level = -128;
        this.m_autoCompletion = false;
        this.m_name = "";
        String cmdFullRegex = "";
        if (cmdRegex != null && cmdRegex.length() != 0 && !cmdRegex.endsWith("(?:\\s+.*|$)")) {
            cmdFullRegex = cmdRegex + "(?:\\s+.*|$)";
        }
        this.m_cmdPattern = Pattern.compile(cmdFullRegex);
        String argFullRegex = "(" + cmdRegex + "){1}";
        if (argsRegex != null && argsRegex.length() != 0 && !argsRegex.startsWith(argFullRegex + "\\s+")) {
            argFullRegex = argFullRegex + "\\s+" + argsRegex;
        }
        if (allowNoArg) {
            argFullRegex = argFullRegex + "|(" + cmdRegex + ")";
        }
        this.m_argsPattern = Pattern.compile(argFullRegex);
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public byte getLevel() {
        return this.m_level;
    }
    
    public void setLevel(final byte level) {
        this.m_level = level;
    }
    
    public Pattern getCmdPattern() {
        return this.m_cmdPattern;
    }
    
    public Pattern getArgsPattern() {
        return this.m_argsPattern;
    }
    
    public boolean isAutoCompletion() {
        return this.m_autoCompletion;
    }
    
    public void setAutoCompletion(final boolean autoCompletion) {
        this.m_autoCompletion = autoCompletion;
    }
    
    public abstract Command createInstance();
}
