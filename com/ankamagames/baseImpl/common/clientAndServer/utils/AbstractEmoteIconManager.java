package com.ankamagames.baseImpl.common.clientAndServer.utils;

public abstract class AbstractEmoteIconManager
{
    public boolean checkEmotesInSentence(final String sentence, final long sourceId) {
        if (sentence == null) {
            return false;
        }
        final String text = sentence.trim();
        if (text.length() == 0) {
            return false;
        }
        for (final SmileyEnum smiley : SmileyEnum.values()) {
            if (smiley.equalsText(text)) {
                return this.displayEmoteIcon(smiley.ordinal(), sourceId);
            }
        }
        return false;
    }
    
    protected abstract boolean displayEmoteIcon(final int p0, final long p1);
}
