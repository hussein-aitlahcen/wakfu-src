package com.ankamagames.framework.graphics.engine.text;

import gnu.trove.*;
import com.ankamagames.framework.graphics.engine.text.characterPacker.*;

public abstract class TextRenderer
{
    public static boolean FORCE_CUT_ON_MISSING_SPACE;
    private static final char[] PUNCT_ARRAY;
    private static final TIntHashSet PUNCT;
    
    public abstract String getFontName();
    
    public abstract int getFontStyle();
    
    public abstract Font getFont();
    
    public abstract Font createDerivedFont(final int p0, final float p1);
    
    public abstract int getCharacterWidth(final char p0);
    
    public abstract int getVisualCharacterWidth(final char p0);
    
    public abstract int getVisualCharacterHeight(final char p0);
    
    public abstract int getMaxCharacterWidth();
    
    public abstract int getMaxCharacterHeight();
    
    public abstract int getMaxVisibleTextLength(final String p0, final int p1, final int p2);
    
    public abstract int getLineWidth(final String p0);
    
    public abstract int getLineHeight(final String p0);
    
    public abstract int getDescent(final String p0);
    
    public abstract boolean isBlured();
    
    public abstract void setColor(final float p0, final float p1, final float p2, final float p3);
    
    public abstract void beginRendering(final int p0, final int p1);
    
    public abstract void draw(final char[] p0, final float p1, final float p2);
    
    public abstract void draw(final char[] p0, final float p1, final float p2, final float p3);
    
    public abstract void draw(final char[] p0, final float p1, final int p2, final float p3, final float p4);
    
    public abstract void draw(final char[] p0, final float p1, final int p2, final float p3, final float p4, final float p5);
    
    public abstract void endRendering();
    
    public abstract void begin3DRendering();
    
    public abstract void end3DRendering();
    
    public int getMaxVisibleTextLength(final String s, final int width) {
        return this.getMaxVisibleTextLength(s, width, true);
    }
    
    public int getMaxVisibleTextLength(final String s, final int width, final boolean cutIfNeeded) {
        int lastCut = 0;
        int index = 0;
        int count = 0;
        final int length = s.length();
        while (index < length) {
            while (index < length) {
                ++count;
                final char c = s.charAt(index++);
                if (!TextRenderer.FORCE_CUT_ON_MISSING_SPACE) {
                    boolean cutAtSpace = true;
                    if (index < length) {
                        final char c2 = s.charAt(index);
                        if (TextRenderer.PUNCT.contains(c2)) {
                            cutAtSpace = false;
                        }
                    }
                    if ((c == ' ' || c == '\n' || c == '\t') && cutAtSpace) {
                        break;
                    }
                    continue;
                }
            }
            if (count == 0) {
                ++count;
            }
            int maxCount = this.getMaxVisibleTextLength(s, count, width);
            if (maxCount < count) {
                if (lastCut != 0) {
                    return lastCut;
                }
                if (TextRenderer.FORCE_CUT_ON_MISSING_SPACE || cutIfNeeded) {
                    if (maxCount != length) {
                        final int previousMaxCount = maxCount;
                        char nextChar = s.charAt(maxCount);
                        for (CharacterPacker characterPacker = CharacterPacker.getCharacterPacker(); (characterPacker.mustBePacked(nextChar) || TextRenderer.PUNCT.contains(nextChar)) && maxCount > 0; nextChar = s.charAt(--maxCount)) {}
                        if (maxCount == 0 && previousMaxCount != 0) {
                            maxCount = previousMaxCount;
                        }
                    }
                    return maxCount;
                }
                return 0;
            }
            else {
                lastCut = count;
            }
        }
        return length;
    }
    
    static {
        PUNCT_ARRAY = new char[] { '(', ')', '\'', '\"', '.', '?', '!', ':', ';', ',', '#', '$', '%', '&', '*', '+', '-', '/', '<', '=', '>', '@', '[', ']', '^', '`', '{', '|', '}', '~', '\u3002', '\u303c', '\u303d', '\u303e', '\u303f', '\uff01', '\uff02', '\uff05', '\uff07', '\uff08', '\uff09', '\uff0c', '\uff0d', '\uff0e', '\uff0f', '\uff1a', '\uff1b', '\uff1f' };
        PUNCT = new TIntHashSet();
        for (final char c : TextRenderer.PUNCT_ARRAY) {
            TextRenderer.PUNCT.add(c);
        }
    }
}
