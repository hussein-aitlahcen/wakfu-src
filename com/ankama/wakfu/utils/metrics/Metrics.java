package com.ankama.wakfu.utils.metrics;

public enum Metrics
{
    NETWORK_INBOUND_MESSAGE_SIZE("network.message.in.%d.size"), 
    NETWORK_OUTBOUND_MESSAGE_SIZE("network.message.out.%d.size"), 
    AI_COMPUTING_TIME("ai.decision.compute.%d.time"), 
    INSTANCE_COUNT("instance.%d.count"), 
    INSTANCE_LIFE("instance.%d.life");
    
    public final String m_format;
    
    private Metrics(final String format) {
        this.m_format = format;
    }
}
