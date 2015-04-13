package com.ankamagames.baseImpl.client.proxyclient.base.console;

import org.apache.log4j.*;

public final class StandardConsoleView implements ConsoleView
{
    private static final Logger m_logger;
    
    @Override
    public void log(final String text) {
        StandardConsoleView.m_logger.info((Object)text);
    }
    
    @Override
    public void customStyle(final String text) {
        StandardConsoleView.m_logger.info((Object)text);
    }
    
    @Override
    public void trace(final String text) {
        StandardConsoleView.m_logger.info((Object)text);
    }
    
    @Override
    public void err(final String text) {
        StandardConsoleView.m_logger.error((Object)text);
    }
    
    @Override
    public void customTrace(final String text, final int color) {
        StandardConsoleView.m_logger.info((Object)text);
    }
    
    @Override
    public void setPrompt(final String prompt) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)ConsoleView.class);
    }
}
