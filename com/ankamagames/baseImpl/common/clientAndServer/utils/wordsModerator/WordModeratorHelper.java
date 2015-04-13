package com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator;

import java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.regex.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.io.*;
import gnu.trove.*;

public class WordModeratorHelper
{
    public static final String EXACT_WORDS_PATTERN_START = "(([^\\p{L}]|\\A)(";
    public static final String EXACT_WORDS_PATTERN_END = ")([^\\p{L}]|\\z))";
    public static final String CONTAINING_CHINESE_WORDS_PATTERN_START = "((";
    public static final String CONTAINING_CHINESE_WORDS_PATTERN_END = "))";
    public static final String CONTAINING_WORDS_PATTERN_START = "([\\p{L}]*((";
    public static final String CONTAINING_WORDS_PATTERN_END = ")+)[\\p{L}]*)";
    public static final int MAX_CHARS_BY_PATTERN = 10000;
    private static final char[] OBSCENITY;
    
    private static ArrayList<Pattern> buildCompletePattern(final List<String> list, final String patternStart, final String patternEnd, final int flags) {
        final ArrayList<Pattern> patterns = new ArrayList<Pattern>();
        int currentIndex = 0;
        while (currentIndex < list.size()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(patternStart);
            boolean first = true;
            while (currentIndex < list.size()) {
                if (first) {
                    first = false;
                }
                else {
                    sb.append('|');
                }
                sb.append(Pattern.quote(list.get(currentIndex)));
                if (sb.length() > 10000) {
                    break;
                }
                ++currentIndex;
            }
            sb.append(patternEnd);
            patterns.add(Pattern.compile(sb.toString(), flags));
        }
        return patterns;
    }
    
    public static ArrayList<Pattern> buildCompleteExactPatterns(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        final ArrayList<Pattern> allPatterns = new ArrayList<Pattern>();
        final TShortObjectIterator<ArrayList<String>> it = forbiddenWords.iterator();
        while (it.hasNext()) {
            it.advance();
            if (CensoredLanguageEnum.getById(it.key()).isSpaceLess()) {
                allPatterns.addAll(buildCompletePattern(it.value(), "(([^\\p{L}]|\\A)(", ")([^\\p{L}]|\\z))", 2));
            }
            else {
                allPatterns.addAll(buildCompletePattern(it.value(), "(([^\\p{L}]|\\A)(", ")([^\\p{L}]|\\z))", 2));
            }
        }
        return allPatterns;
    }
    
    public static ArrayList<Pattern> buildCompleteContainingPatterns(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        final ArrayList<Pattern> allPatterns = new ArrayList<Pattern>();
        final TShortObjectIterator<ArrayList<String>> it = forbiddenWords.iterator();
        while (it.hasNext()) {
            it.advance();
            if (CensoredLanguageEnum.getById(it.key()).isSpaceLess()) {
                allPatterns.addAll(buildCompletePattern(it.value(), "((", "))", 2));
            }
            else {
                allPatterns.addAll(buildCompletePattern(it.value(), "([\\p{L}]*((", ")+)[\\p{L}]*)", 2));
            }
        }
        return allPatterns;
    }
    
    public static String moderateWithBroadPattern(String sentence, final ArrayList<Pattern> patterns) {
        for (int i = 0; i < patterns.size(); ++i) {
            final Pattern pattern = patterns.get(i);
            sentence = moderateWithBroadPattern(sentence, pattern);
        }
        return sentence;
    }
    
    public static String moderateWithPrecisePattern(String sentence, final ArrayList<Pattern> patterns) {
        for (int i = 0; i < patterns.size(); ++i) {
            final Pattern pattern = patterns.get(i);
            sentence = moderateWithPrecisePattern(sentence, pattern);
        }
        return sentence;
    }
    
    private static String moderateWithBroadPattern(final String sentence, final Pattern currentPattern) {
        final StringBuilder sb = new StringBuilder();
        final String trimmedSentence = sentence.trim();
        final String cleanedSentence = StringUtils.cleanSentence(trimmedSentence);
        final Matcher matcher = currentPattern.matcher(cleanedSentence);
        int currentIndex = 0;
        while (matcher.find()) {
            sb.append(trimmedSentence, currentIndex, matcher.start());
            currentIndex = matcher.end();
            final int numChars = matcher.end() - matcher.start();
            appendObscenity(sb, numChars);
        }
        sb.append(trimmedSentence, currentIndex, trimmedSentence.length());
        return sb.toString();
    }
    
    private static String moderateWithPrecisePattern(final String sentence, final Pattern currentPattern) {
        final StringBuilder sb = new StringBuilder();
        final String trimmedSentence = sentence.trim();
        final String cleanedSentence = StringUtils.cleanSentence(trimmedSentence);
        final Matcher matcher = currentPattern.matcher(cleanedSentence);
        int currentChar = 0;
        while (matcher.find()) {
            sb.append(trimmedSentence, currentChar, matcher.start(2));
            currentChar = matcher.end(2);
            final int numChars = matcher.end(2) - matcher.start(2);
            appendObscenity(sb, numChars);
        }
        sb.append(trimmedSentence, currentChar, trimmedSentence.length());
        return sb.toString();
    }
    
    public static void appendObscenity(final StringBuilder sb, final int numChars) {
        for (int i = 0; i < numChars; ++i) {
            sb.append(WordModeratorHelper.OBSCENITY[MathHelper.random(WordModeratorHelper.OBSCENITY.length)]);
        }
    }
    
    static {
        OBSCENITY = new char[] { '&', '~', '#', '@', '£', '¤', 'µ', '%', '!' };
    }
}
