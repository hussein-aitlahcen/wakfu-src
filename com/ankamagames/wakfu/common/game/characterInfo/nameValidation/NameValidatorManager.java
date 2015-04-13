package com.ankamagames.wakfu.common.game.characterInfo.nameValidation;

import java.util.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class NameValidatorManager
{
    public static final NameValidatorManager INSTANCE;
    private final ArrayList<NameValidator> m_validators;
    private final ArrayList<NameValidator> m_guildValidators;
    
    public NameValidatorManager() {
        super();
        this.m_validators = new ArrayList<NameValidator>();
        this.m_guildValidators = new ArrayList<NameValidator>();
    }
    
    public void addValidator(final NameValidator nameValidator) {
        this.m_validators.add(nameValidator);
    }
    
    public void addGuildValidator(final NameValidator validator) {
        this.m_guildValidators.add(validator);
    }
    
    public NameCheckerResult validateName(final String name) {
        if (name == null) {
            return NameChecker.createResult(NameChecker.NameResult.ERROR_NAME_TOO_SHORT);
        }
        for (int i = 0, size = this.m_validators.size(); i < size; ++i) {
            final NameCheckerResult nameResult = this.m_validators.get(i).checkValidity(name);
            if (nameResult.getResult() != NameChecker.NameResult.ERROR_BAD_CHAR || i == size - 1) {
                return nameResult;
            }
        }
        return NameChecker.createResult(NameChecker.NameResult.ERROR_BAD_CHAR);
    }
    
    public NameCheckerResult validateGuildName(final String name) {
        if (name == null) {
            return NameChecker.createResult(NameChecker.NameResult.ERROR_NAME_TOO_SHORT);
        }
        for (int i = 0, size = this.m_guildValidators.size(); i < size; ++i) {
            final NameCheckerResult nameResult = this.m_guildValidators.get(i).checkValidity(name);
            if (nameResult.getResult() != NameChecker.NameResult.ERROR_BAD_CHAR || i == size - 1) {
                return nameResult;
            }
        }
        return NameChecker.createResult(NameChecker.NameResult.ERROR_BAD_CHAR);
    }
    
    static {
        INSTANCE = new NameValidatorManager();
    }
}
