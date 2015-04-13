package com.ankamagames.wakfu.common.game.characterInfo;

import java.util.regex.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.characterInfo.nameValidation.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;

public class NameChecker
{
    private static final Pattern SPACES_PATTERN;
    private static final Pattern DASHES_PATTERN;
    private static final Pattern QUOTES_PATTERN;
    private static final Pattern DASH_SPLIT_PATTERN;
    protected static final Logger m_logger;
    
    public static String doNameCorrection(final String name) {
        String result = StringUtils.capitalizeWords(name);
        result = NameChecker.SPACES_PATTERN.matcher(result).replaceAll(" ");
        result = NameChecker.DASHES_PATTERN.matcher(result).replaceAll("-");
        result = NameChecker.QUOTES_PATTERN.matcher(result).replaceAll("'");
        return result;
    }
    
    public static NameCheckerResult nameValidity(final String name) {
        final NameCheckerResult basicCheck = NameValidatorManager.INSTANCE.validateName(name);
        if (basicCheck.getResult() != NameResult.OK) {
            return basicCheck;
        }
        if (!WordsModerator.getInstance().validateName(name)) {
            return createResult(NameResult.ERROR_FORBIDDEN_NAME);
        }
        return createResult(NameResult.OK);
    }
    
    public static NameCheckerResult guildNameValidity(final String name) {
        final NameCheckerResult basicCheck = NameValidatorManager.INSTANCE.validateGuildName(name);
        if (basicCheck.getResult() != NameResult.OK) {
            return basicCheck;
        }
        if (!WordsModerator.getInstance().validateName(name)) {
            return createResult(NameResult.ERROR_FORBIDDEN_NAME);
        }
        return createResult(NameResult.OK);
    }
    
    public static NameCheckerResult createResult(final NameResult result) {
        return new NameCheckerResult(result);
    }
    
    public static NameCheckerResult createbadCharacterError(final NameResult result, final char character) {
        return new BadCharacterNameCheckerError(result, character);
    }
    
    static {
        SPACES_PATTERN = Pattern.compile("([ ]+)");
        DASHES_PATTERN = Pattern.compile("([-]+)");
        QUOTES_PATTERN = Pattern.compile("([']+)");
        DASH_SPLIT_PATTERN = Pattern.compile("[ \\-']");
        m_logger = Logger.getLogger((Class)NameChecker.class);
    }
    
    public enum NameResult
    {
        OK, 
        ERROR_NAME_TOO_SHORT, 
        ERROR_NAME_TOO_LONG, 
        ERROR_BAD_CHAR, 
        ERROR_TOO_MANY_CONSECUTIVE_CONSONANT, 
        ERROR_TOO_MANY_CONSECUTIVE_VOWEL, 
        ERROR_TOO_MANY_CONSECUTIVE_IDENTICAL, 
        ERROR_TOO_FEW_VOWEL_IN_PART, 
        ERROR_TOO_FEW_CONSONANT_IN_PART, 
        ERROR_TOO_MANY_SPECIAL_IN_PART, 
        ERROR_PART_TOO_LONG, 
        ERROR_TOO_MANY_SPECIAL, 
        ERROR_FORBIDDEN_NAME, 
        ERROR_INVALID_DASH_POSITION, 
        ERROR_NAME_WITH_BAD_CASE;
    }
}
