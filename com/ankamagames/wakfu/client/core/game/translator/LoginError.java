package com.ankamagames.wakfu.client.core.game.translator;

public enum LoginError
{
    ERROR_INTERNAL((byte)7, "error.connection.unknown"), 
    ERROR_BANNED((byte)5, "error.connection.banned"), 
    ERROR_OTP_AUTH_FAILED((byte)22, "error.connection.OTPAuthFailed"), 
    SECURITYCARD((byte)26, "error.connection.unknown"), 
    ERROR_INVALID_LOGIN((byte)2, "error.connection.invalidLogin"), 
    ERROR_INVALID_PARTNER((byte)12, "error.connection.invalidPartner"), 
    ERROR_ACCOUNT_INVALID_EMAIL((byte)20, "error.connection.invalidEmail"), 
    ERROR_CLOSED_BETA((byte)127, "error.connection.closedBeta"), 
    ERROR_FORBIDDEN_COMMUNITY((byte)25, "error.connection.forbiddenCommunity"), 
    ERROR_EXTERNAL_ACCOUNT_LINKED((byte)23, "error.connection.external.accountLinked"), 
    ERROR_EXTERNAL_NO_ACCOUNT((byte)24, "error.connection.external.noAccount");
    
    public final byte m_errorCode;
    public final String m_key;
    
    private LoginError(final byte errorCode, final String key) {
        this.m_errorCode = errorCode;
        this.m_key = key;
    }
    
    public static LoginError fromError(final byte errorCode) {
        final LoginError[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final LoginError value = values[i];
            if (value.m_errorCode == errorCode) {
                return value;
            }
        }
        return LoginError.ERROR_INTERNAL;
    }
}
