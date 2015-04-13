package com.ankamagames.wakfu.common.game.characterInfo.nameValidation;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class AsianNameValidator implements NameValidator
{
    private static final char MIDDLE_DOT = '·';
    
    private static boolean isAuthorized(final char c) {
        final boolean chineseChar = c >= '\u4e00' && c <= '\u9fa5';
        final boolean hiragana = c >= '\u3041' && c <= '\u3096';
        return chineseChar || hiragana || c == '·';
    }
    
    @Override
    public NameCheckerResult checkValidity(@NotNull final String name) {
        final char[] chars = name.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            if (!isAuthorized(chars[i])) {
                return NameChecker.createbadCharacterError(NameChecker.NameResult.ERROR_BAD_CHAR, chars[i]);
            }
        }
        if (name.length() < 2) {
            return NameChecker.createResult(NameChecker.NameResult.ERROR_NAME_TOO_SHORT);
        }
        if (name.length() > 10) {
            return NameChecker.createResult(NameChecker.NameResult.ERROR_NAME_TOO_LONG);
        }
        if (getCharacterCount(name, '·') > 1) {
            return NameChecker.createbadCharacterError(NameChecker.NameResult.ERROR_TOO_MANY_SPECIAL, '·');
        }
        if (name.length() > 1 && (name.charAt(0) == '·' || name.charAt(name.length() - 1) == '·')) {
            return NameChecker.createbadCharacterError(NameChecker.NameResult.ERROR_INVALID_DASH_POSITION, '·');
        }
        return NameChecker.createResult(NameChecker.NameResult.OK);
    }
    
    private static int getCharacterCount(final String string, final char character) {
        int count = 0;
        for (int i = 0, size = string.length(); i < size; ++i) {
            if (string.charAt(i) == character) {
                ++count;
            }
        }
        return count;
    }
}
