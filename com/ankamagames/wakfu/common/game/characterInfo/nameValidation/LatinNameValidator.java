package com.ankamagames.wakfu.common.game.characterInfo.nameValidation;

import java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class LatinNameValidator implements NameValidator
{
    private static char[] m_voyelles;
    private static char[] m_consonnes;
    private static char[] m_others;
    private int m_minChars;
    private int m_maxChars;
    private int m_maxConsecutiveVowels;
    private int m_maxConsecutiveConsonant;
    private int m_maxConsecutiveIdentical;
    private int m_maxSpecialChars;
    private int m_minVowelsPerPart;
    private int m_minConsonantsPerPart;
    private int m_maxSpecialCharsPerPart;
    private int m_maxParts;
    
    private static boolean isVoyelle(final char c) {
        return Arrays.binarySearch(LatinNameValidator.m_voyelles, c) >= 0;
    }
    
    private static boolean isConsonne(final char c) {
        return Arrays.binarySearch(LatinNameValidator.m_consonnes, c) >= 0;
    }
    
    private static boolean isOther(final char c) {
        return Arrays.binarySearch(LatinNameValidator.m_others, c) >= 0;
    }
    
    private static boolean isAuthorized(final char c) {
        return isConsonne(c) || isVoyelle(c) || isOther(c);
    }
    
    private static boolean isEquals(final Character c, final Character d) {
        return StringUtils.cleanSentence(c.toString()).equals(StringUtils.cleanSentence(d.toString()));
    }
    
    public LatinNameValidator(final int minChars, final int maxChars, final int maxConsecutiveVowels, final int maxConsecutiveConsonant, final int maxConsecutiveIdentical, final int maxSpecialChars, final int minVowelsPerPart, final int minConsonantsPerPart, final int maxSpecialCharsPerPart, final int maxParts) {
        super();
        this.m_minChars = minChars;
        this.m_maxChars = maxChars;
        this.m_maxConsecutiveVowels = maxConsecutiveVowels;
        this.m_maxConsecutiveConsonant = maxConsecutiveConsonant;
        this.m_maxConsecutiveIdentical = maxConsecutiveIdentical;
        this.m_maxSpecialChars = maxSpecialChars;
        this.m_minVowelsPerPart = minVowelsPerPart;
        this.m_minConsonantsPerPart = minConsonantsPerPart;
        this.m_maxSpecialCharsPerPart = maxSpecialCharsPerPart;
        this.m_maxParts = maxParts;
    }
    
    @Override
    public NameCheckerResult checkValidity(@NotNull final String name) {
        final char[] chars = name.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            if (!isAuthorized(chars[i])) {
                return NameChecker.createbadCharacterError(NameChecker.NameResult.ERROR_BAD_CHAR, chars[i]);
            }
        }
        if (name.length() < this.m_minChars) {
            return NameChecker.createResult(NameChecker.NameResult.ERROR_NAME_TOO_SHORT);
        }
        if (name.length() > this.m_maxChars) {
            return NameChecker.createResult(NameChecker.NameResult.ERROR_NAME_TOO_LONG);
        }
        int countV = 0;
        int countC = 0;
        int countI = 1;
        int countS = 0;
        char last = '/';
        for (int j = 0; j < chars.length; ++j) {
            final char c = chars[j];
            if (isVoyelle(c)) {
                ++countV;
                countC = 0;
            }
            else if (isConsonne(c)) {
                ++countC;
                countV = 0;
            }
            else {
                ++countS;
                countC = (countV = 0);
            }
            if (isEquals(last, c)) {
                ++countI;
            }
            else {
                countI = 1;
            }
            last = c;
            if (countV > this.m_maxConsecutiveVowels) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_TOO_MANY_CONSECUTIVE_VOWEL);
            }
            if (countC > this.m_maxConsecutiveConsonant) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_TOO_MANY_CONSECUTIVE_CONSONANT);
            }
            if (countI > this.m_maxConsecutiveIdentical) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_TOO_MANY_CONSECUTIVE_IDENTICAL);
            }
            if (countS > this.m_maxSpecialChars) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_TOO_MANY_SPECIAL);
            }
        }
        final String[] words = name.split("([ -])");
        for (int k = 0; k < words.length; ++k) {
            countC = (countV = (countS = 0));
            final char[] partChars = words[k].toCharArray();
            for (int l = 0; l < partChars.length; ++l) {
                if (isVoyelle(partChars[l])) {
                    ++countV;
                }
                else if (isConsonne(partChars[l])) {
                    ++countC;
                }
                else if (isOther(partChars[l])) {
                    ++countS;
                }
            }
            if (countV < this.m_minVowelsPerPart) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_TOO_FEW_VOWEL_IN_PART);
            }
            if (countC < this.m_minConsonantsPerPart) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_TOO_FEW_CONSONANT_IN_PART);
            }
            if (countS > this.m_maxSpecialCharsPerPart) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_TOO_MANY_SPECIAL_IN_PART);
            }
            if (partChars.length > this.m_maxParts) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_PART_TOO_LONG);
            }
        }
        if (name.length() > 1 && (name.charAt(0) == '-' || name.charAt(name.length() - 1) == '-')) {
            return NameChecker.createResult(NameChecker.NameResult.ERROR_INVALID_DASH_POSITION);
        }
        final String[] arr$;
        final String[] playerNameSeparated = arr$ = name.split("[ \\-']");
        for (final String namePart : arr$) {
            if (namePart.length() < 1 || Character.isLowerCase(namePart.charAt(0)) || !namePart.substring(1).equals(namePart.substring(1).toLowerCase())) {
                return NameChecker.createResult(NameChecker.NameResult.ERROR_NAME_WITH_BAD_CASE);
            }
        }
        return NameChecker.createResult(NameChecker.NameResult.OK);
    }
    
    static {
        LatinNameValidator.m_voyelles = new char[] { 'a', 'e', 'i', 'o', 'u', 'y', '\u00e9', '\u00e8', '\u00eb', '\u00ea', '\u00e2', '\u00e4', '\u00fc', '\u00fb', '\u00f9', '\u00f4', '\u00f6', '\u00ee', '\u00ef' };
        LatinNameValidator.m_consonnes = new char[] { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z' };
        LatinNameValidator.m_others = new char[] { ' ', '-', '\'' };
        char[] build = new char[LatinNameValidator.m_voyelles.length * 2];
        System.arraycopy(LatinNameValidator.m_voyelles, 0, build, 0, LatinNameValidator.m_voyelles.length);
        for (int i = 0; i < LatinNameValidator.m_voyelles.length; ++i) {
            build[LatinNameValidator.m_voyelles.length + i] = Character.toUpperCase(LatinNameValidator.m_voyelles[i]);
        }
        LatinNameValidator.m_voyelles = build;
        build = new char[LatinNameValidator.m_consonnes.length * 2];
        System.arraycopy(LatinNameValidator.m_consonnes, 0, build, 0, LatinNameValidator.m_consonnes.length);
        for (int i = 0; i < LatinNameValidator.m_consonnes.length; ++i) {
            build[LatinNameValidator.m_consonnes.length + i] = Character.toUpperCase(LatinNameValidator.m_consonnes[i]);
        }
        LatinNameValidator.m_consonnes = build;
        Arrays.sort(LatinNameValidator.m_voyelles);
        Arrays.sort(LatinNameValidator.m_consonnes);
        Arrays.sort(LatinNameValidator.m_others);
    }
}
