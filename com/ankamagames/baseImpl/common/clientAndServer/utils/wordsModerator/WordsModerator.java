package com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.regex.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class WordsModerator
{
    public static boolean CENSOR_ACTIVATED;
    private static final Logger m_logger;
    private static final WordsModerator INSTANCE;
    private final ArrayList<CensoredLanguageEnum> m_languages;
    private final TIntObjectHashMap<CensoredWordEntry> m_censoredWordEntries;
    private CensorInitializer m_censorInitializer;
    private static final String SYSTEM_TEXT_PATTERN = "(<\\D[^/]+>)|(<.*/\\D>)";
    private final ArrayList<Pattern> m_namesPattern;
    private final ArrayList<Pattern> m_chatPattern;
    private final ArrayList<Pattern> m_bannedPattern;
    private final ArrayList<Pattern> m_chatExactWordPattern;
    private final ArrayList<Pattern> m_chatWordContainingPattern;
    private boolean m_forceFiltered;
    
    public WordsModerator() {
        super();
        this.m_languages = new ArrayList<CensoredLanguageEnum>();
        this.m_censoredWordEntries = new TIntObjectHashMap<CensoredWordEntry>();
        this.m_namesPattern = new ArrayList<Pattern>();
        this.m_chatPattern = new ArrayList<Pattern>();
        this.m_bannedPattern = new ArrayList<Pattern>();
        this.m_chatExactWordPattern = new ArrayList<Pattern>();
        this.m_chatWordContainingPattern = new ArrayList<Pattern>();
        this.m_forceFiltered = false;
    }
    
    public static WordsModerator getInstance() {
        return WordsModerator.INSTANCE;
    }
    
    public void setForceFiltered(final boolean forceFiltered) {
        this.m_forceFiltered = forceFiltered;
    }
    
    public void addCensoredWordEntry(final CensoredWordEntry censoredWordEntry) {
        this.m_censoredWordEntries.put(censoredWordEntry.getId(), censoredWordEntry);
    }
    
    public void setLanguages(final List<CensoredLanguageEnum> languages) {
        this.m_languages.clear();
        this.m_languages.addAll(languages);
        this.init();
    }
    
    public void setLanguages(final CensoredLanguageEnum... languages) {
        this.setLanguages(Arrays.asList(languages));
    }
    
    public void init(final CensorInitializer censorInitializer) {
        this.m_bannedPattern.clear();
        this.m_chatPattern.clear();
        this.m_chatExactWordPattern.clear();
        this.m_chatWordContainingPattern.clear();
        this.m_namesPattern.clear();
        censorInitializer.init(this);
        if (this.m_censorInitializer == null) {
            this.m_censorInitializer = censorInitializer;
        }
    }
    
    public void init() {
        if (this.m_censorInitializer != null) {
            this.init(this.m_censorInitializer);
        }
    }
    
    public void setBannedExactWords(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        this.m_bannedPattern.addAll(WordModeratorHelper.buildCompleteExactPatterns(forbiddenWords));
    }
    
    public void setBannedContainingWords(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        this.m_bannedPattern.addAll(WordModeratorHelper.buildCompleteContainingPatterns(forbiddenWords));
    }
    
    public void setNameExactWords(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        this.m_namesPattern.addAll(WordModeratorHelper.buildCompleteExactPatterns(forbiddenWords));
    }
    
    public void setNameContainingWords(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        this.m_namesPattern.addAll(WordModeratorHelper.buildCompleteContainingPatterns(forbiddenWords));
    }
    
    public void setChatForbiddenExactWords(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        final ArrayList<Pattern> patterns = WordModeratorHelper.buildCompleteExactPatterns(forbiddenWords);
        this.m_chatExactWordPattern.addAll(patterns);
        this.m_chatPattern.addAll(patterns);
    }
    
    public void setChatForbiddenWordsContaining(final TShortObjectHashMap<ArrayList<String>> forbiddenWords) {
        final ArrayList<Pattern> patterns = WordModeratorHelper.buildCompleteContainingPatterns(forbiddenWords);
        this.m_chatWordContainingPattern.addAll(patterns);
        this.m_chatPattern.addAll(patterns);
    }
    
    public boolean validateName(final String name) {
        if (!WordsModerator.CENSOR_ACTIVATED) {
            return true;
        }
        if (name == null || name.length() == 0) {
            return true;
        }
        final String sentence = StringUtils.cleanSentence(name);
        return !this.matchesWords(sentence, this.m_namesPattern) && !this.matchesWords(sentence, this.m_chatPattern) && !this.matchesWords(sentence, this.m_bannedPattern);
    }
    
    private boolean matchesWords(final String sentence, final ArrayList<Pattern> patterns) {
        return this.getMatchingMatcher(sentence, patterns) != null;
    }
    
    private Matcher getMatchingMatcher(final String sentence, final ArrayList<Pattern> patterns) {
        for (int i = 0, size = patterns.size(); i < size; ++i) {
            final Pattern pattern = patterns.get(i);
            final Matcher matcher = pattern.matcher(sentence);
            if (matcher.find()) {
                matcher.reset();
                return matcher;
            }
        }
        return null;
    }
    
    public String makeValidSentence(final String sentence, final boolean filtered) {
        if (sentence == null || sentence.trim().isEmpty()) {
            return "";
        }
        if (!WordsModerator.CENSOR_ACTIVATED) {
            return sentence;
        }
        if (this.matchesWords(sentence, this.m_bannedPattern)) {
            return "";
        }
        if (!filtered && !this.m_forceFiltered) {
            return sentence;
        }
        final ArrayList<ObjectPair<Integer, String>> systemTextsByIndex = new ArrayList<ObjectPair<Integer, String>>();
        final Pattern pattern = Pattern.compile("(<\\D[^/]+>)|(<.*/\\D>)");
        String cleanedText = "";
        final Matcher matcher = pattern.matcher(sentence);
        int lastEnd = 0;
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            final String systemText = sentence.substring(start, end);
            cleanedText += sentence.substring(lastEnd, start);
            systemTextsByIndex.add(new ObjectPair<Integer, String>(start, systemText));
            lastEnd = end;
        }
        cleanedText += sentence.substring(lastEnd);
        if (cleanedText.isEmpty()) {
            cleanedText = sentence;
        }
        String moderatedSentence = null;
        if (!this.m_chatPattern.isEmpty()) {
            moderatedSentence = WordModeratorHelper.moderateWithBroadPattern(cleanedText, this.m_chatPattern);
        }
        else if (!this.m_chatExactWordPattern.isEmpty() && !this.m_chatWordContainingPattern.isEmpty()) {
            final String moderatedString = WordModeratorHelper.moderateWithPrecisePattern(cleanedText, this.m_chatExactWordPattern);
            moderatedSentence = WordModeratorHelper.moderateWithPrecisePattern(moderatedString, this.m_chatWordContainingPattern);
        }
        if (moderatedSentence == null) {
            WordsModerator.m_logger.error((Object)("[TRANSLATION] No censor pattern matched for languages " + this.m_languages));
            return sentence;
        }
        final StringBuilder finalText = new StringBuilder();
        if (!systemTextsByIndex.isEmpty()) {
            int lastEnd2 = 0;
            for (final ObjectPair<Integer, String> systemTexts : systemTextsByIndex) {
                final int start2 = systemTexts.getFirst();
                final String text = systemTexts.getSecond();
                final int end2 = start2 + text.length();
                finalText.append(sentence.substring(lastEnd2, start2 + 1)).append(text);
                lastEnd2 = end2;
            }
            finalText.append(sentence.substring(lastEnd2 - 1));
        }
        else {
            finalText.append(moderatedSentence);
        }
        return finalText.toString();
    }
    
    public ArrayList<CensoredLanguageEnum> getLanguages() {
        return this.m_languages;
    }
    
    public TIntObjectHashMap<CensoredWordEntry> getCensoredWordEntries() {
        return this.m_censoredWordEntries;
    }
    
    static {
        WordsModerator.CENSOR_ACTIVATED = true;
        m_logger = Logger.getLogger((Class)WordsModerator.class);
        INSTANCE = new WordsModerator();
    }
}
