package com.ankamagames.wakfu.client.core.utils;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import gnu.trove.*;

public class ClientCensorInitializer implements CensorInitializer
{
    @Override
    public void init(final WordsModerator wordsModerator) {
        final TShortObjectHashMap<ArrayList<String>> wordsContaining = new TShortObjectHashMap<ArrayList<String>>();
        final TShortObjectHashMap<ArrayList<String>> wordsEquals = new TShortObjectHashMap<ArrayList<String>>();
        final TShortObjectHashMap<ArrayList<String>> namesEquals = new TShortObjectHashMap<ArrayList<String>>();
        final TShortObjectHashMap<ArrayList<String>> namesContaining = new TShortObjectHashMap<ArrayList<String>>();
        final TShortObjectHashMap<ArrayList<String>> bannedWordsContaining = new TShortObjectHashMap<ArrayList<String>>();
        final TShortObjectHashMap<ArrayList<String>> bannedWordsEquals = new TShortObjectHashMap<ArrayList<String>>();
        final TIntObjectHashMap<CensoredWordEntry> censoredWordEntries = wordsModerator.getCensoredWordEntries();
        final ArrayList<CensoredLanguageEnum> languages = wordsModerator.getLanguages();
        final TIntObjectIterator<CensoredWordEntry> it = censoredWordEntries.iterator();
        while (it.hasNext()) {
            it.advance();
            final CensoredWordEntry entry = it.value();
            for (final CensoredLanguageEnum lang : languages) {
                if (entry.hasLanguage(lang)) {
                    addEntry(lang, entry, wordsEquals, wordsContaining, namesEquals, namesContaining, bannedWordsEquals, bannedWordsContaining);
                }
            }
        }
        wordsModerator.setBannedExactWords(bannedWordsEquals);
        wordsModerator.setBannedContainingWords(bannedWordsContaining);
        wordsModerator.setChatForbiddenExactWords(wordsEquals);
        wordsModerator.setChatForbiddenWordsContaining(wordsContaining);
        wordsModerator.setNameExactWords(namesEquals);
        wordsModerator.setNameContainingWords(namesContaining);
    }
    
    private static void addEntry(final CensoredLanguageEnum lang, final CensoredWordEntry censoredWord, final TShortObjectHashMap<ArrayList<String>> wordsEquals, final TShortObjectHashMap<ArrayList<String>> wordsContaining, final TShortObjectHashMap<ArrayList<String>> namesEquals, final TShortObjectHashMap<ArrayList<String>> namesContaining, final TShortObjectHashMap<ArrayList<String>> bannedWordsEquals, final TShortObjectHashMap<ArrayList<String>> bannedWordsContaining) {
        final short langId = lang.getId();
        switch (censoredWord.getCensorType()) {
            case BANNISHED: {
                if (censoredWord.isDeepSearch()) {
                    if (!bannedWordsContaining.containsKey(langId)) {
                        bannedWordsContaining.put(langId, new ArrayList<String>());
                    }
                    final ArrayList<String> words = bannedWordsContaining.get(langId);
                    words.add(censoredWord.getText());
                    break;
                }
                if (!bannedWordsEquals.containsKey(langId)) {
                    bannedWordsEquals.put(langId, new ArrayList<String>());
                }
                final ArrayList<String> words = bannedWordsEquals.get(langId);
                words.add(censoredWord.getText());
                break;
            }
            case COPYRIGHTED: {
                if (censoredWord.isDeepSearch()) {
                    if (!namesContaining.containsKey(langId)) {
                        namesContaining.put(langId, new ArrayList<String>());
                    }
                    final ArrayList<String> words = namesContaining.get(langId);
                    words.add(censoredWord.getText());
                    break;
                }
                if (!namesEquals.containsKey(langId)) {
                    namesEquals.put(langId, new ArrayList<String>());
                }
                final ArrayList<String> words = namesEquals.get(langId);
                words.add(censoredWord.getText());
                break;
            }
            case OBSCENITY: {
                if (censoredWord.isDeepSearch()) {
                    if (!wordsContaining.containsKey(langId)) {
                        wordsContaining.put(langId, new ArrayList<String>());
                    }
                    final ArrayList<String> words = wordsContaining.get(langId);
                    words.add(censoredWord.getText());
                    break;
                }
                if (!wordsEquals.containsKey(langId)) {
                    wordsEquals.put(langId, new ArrayList<String>());
                }
                final ArrayList<String> words = wordsEquals.get(langId);
                words.add(censoredWord.getText());
                break;
            }
        }
    }
}
