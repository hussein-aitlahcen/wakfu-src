package com.ankamagames.wakfu.common.game.characterInfo.nameValidation;

import com.ankamagames.framework.kernel.core.translator.*;

public class NameValidatorHelper
{
    public static void initializeWithAll() {
        NameValidatorManager.INSTANCE.addValidator(createLatinNameValidator());
        NameValidatorManager.INSTANCE.addGuildValidator(createLatinGuildNameValidator());
        final NameValidator asianValidator = new AsianNameValidator();
        NameValidatorManager.INSTANCE.addValidator(asianValidator);
        NameValidatorManager.INSTANCE.addGuildValidator(asianValidator);
    }
    
    public static void initialize(final Language lang) {
        switch (lang) {
            case THA:
            case CH:
            case JP:
            case TW:
            case KOR:
            case VIE: {
                NameValidatorManager.INSTANCE.addValidator(createLatinNameValidator());
                NameValidatorManager.INSTANCE.addGuildValidator(createLatinGuildNameValidator());
                final NameValidator asianValidator = new AsianNameValidator();
                NameValidatorManager.INSTANCE.addValidator(asianValidator);
                NameValidatorManager.INSTANCE.addGuildValidator(asianValidator);
                break;
            }
            default: {
                NameValidatorManager.INSTANCE.addValidator(createLatinNameValidator());
                NameValidatorManager.INSTANCE.addGuildValidator(createLatinGuildNameValidator());
                break;
            }
        }
    }
    
    public static NameValidator createLatinNameValidator() {
        return new LatinNameValidator(3, 25, 4, 3, 2, 3, 1, 1, 2, 15);
    }
    
    public static NameValidator createLatinGuildNameValidator() {
        return new LatinNameValidator(5, 30, 8, 4, 8, 6, 1, 0, 2, 20);
    }
}
