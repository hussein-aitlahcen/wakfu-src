package com.ankamagames.wakfu.client.ui.systemMessage;

public class ImageSystemMessageData extends SystemMessageData
{
    private final int m_gfxId;
    
    public ImageSystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final int gfxId) {
        super(type, message);
        this.m_gfxId = gfxId;
    }
    
    public ImageSystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final int duration, final int gfxId) {
        super(type, message, duration);
        this.m_gfxId = gfxId;
    }
    
    public ImageSystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final int fadeDuration, final int duration, final int gfxId) {
        super(type, message, fadeDuration, duration);
        this.m_gfxId = gfxId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
}
