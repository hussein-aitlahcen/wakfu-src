package com.ankamagames.xulor2.component.bubble;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.regex.*;
import com.ankamagames.xulor2.component.bubble.bubbleStyleTransformers.*;

public enum WakfuBubbleModeType
{
    NORMAL((WakfuBubbleStyleTransformer)new WakfuBubbleStyleTransformersLibrary.StandartBubbleStyleTransformer(), 9, 3), 
    THINKING((WakfuBubbleStyleTransformer)new WakfuBubbleStyleTransformersLibrary.ThinkingBubbleStyleTransformer(), 9, 3), 
    SCREAM((WakfuBubbleStyleTransformer)new WakfuBubbleStyleTransformersLibrary.ScreamingBubbleStyleTransformer(), 15, 3), 
    WHISPER((WakfuBubbleStyleTransformer)new WakfuBubbleStyleTransformersLibrary.ThinkingBubbleStyleTransformer(), 2, 0);
    
    private static final String EXCLAMATION_SHORT_EXPRESSION = "(!!|\uff01\uff01)";
    private static final Pattern EXCLAMATION_SMALL_EXPRESSION_TAG;
    private static final Pattern EXCLAMATION_EXPRESSION_TAG;
    private static final Pattern THINKING_EXPRESSION_TAG;
    private static final Pattern WHISPERING_EXPRESSION_TAG;
    private final WakfuBubbleStyleTransformer m_wakfuBubbleStyleTransformer;
    private final int m_farDistance;
    private final int m_closeDistance;
    
    private WakfuBubbleModeType(final WakfuBubbleStyleTransformer wakfuBubbleStyleTransformer, final int farDistance, final int closeDistance) {
        this.m_wakfuBubbleStyleTransformer = wakfuBubbleStyleTransformer;
        this.m_farDistance = farDistance;
        this.m_closeDistance = closeDistance;
    }
    
    public WakfuBubbleStyleTransformer getWakfuBubbleStyleTransformer() {
        return this.m_wakfuBubbleStyleTransformer;
    }
    
    public int getFarDistance() {
        return this.m_farDistance;
    }
    
    public int getCloseDistance() {
        return this.m_closeDistance;
    }
    
    public static final ObjectPair<WakfuBubbleModeType, String> getWakfuBubbleType(String message) {
        final ObjectPair<WakfuBubbleModeType, String> returnValue = new ObjectPair<WakfuBubbleModeType, String>();
        WakfuBubbleModeType wakfuBubbleModeType = WakfuBubbleModeType.NORMAL;
        final Matcher smallExclamationMatcher = WakfuBubbleModeType.EXCLAMATION_SMALL_EXPRESSION_TAG.matcher(message);
        final Matcher exclamationMatcher = WakfuBubbleModeType.EXCLAMATION_EXPRESSION_TAG.matcher(message);
        final Matcher thinkingMatcher = WakfuBubbleModeType.THINKING_EXPRESSION_TAG.matcher(message);
        final Matcher whisperingMatcher = WakfuBubbleModeType.WHISPERING_EXPRESSION_TAG.matcher(message);
        if (exclamationMatcher.matches()) {
            smallExclamationMatcher.find();
            if (smallExclamationMatcher.start() == 0 && smallExclamationMatcher.end() < message.length()) {
                message = message.replaceFirst("(!!|\uff01\uff01)", "");
            }
            wakfuBubbleModeType = WakfuBubbleModeType.SCREAM;
        }
        else if (thinkingMatcher.matches()) {
            message = message.replaceFirst("\\*\\*", "");
            wakfuBubbleModeType = WakfuBubbleModeType.THINKING;
        }
        else if (whisperingMatcher.matches()) {
            message = message.replaceFirst("--", "");
            wakfuBubbleModeType = WakfuBubbleModeType.WHISPER;
        }
        returnValue.setFirst(wakfuBubbleModeType);
        returnValue.setSecond(message);
        return returnValue;
    }
    
    static {
        EXCLAMATION_SMALL_EXPRESSION_TAG = Pattern.compile("(!!|\uff01\uff01)", 32);
        EXCLAMATION_EXPRESSION_TAG = Pattern.compile("(.*(!!|\uff01\uff01).*)", 32);
        THINKING_EXPRESSION_TAG = Pattern.compile("\\*\\*.*", 32);
        WHISPERING_EXPRESSION_TAG = Pattern.compile("--.*", 32);
    }
}
