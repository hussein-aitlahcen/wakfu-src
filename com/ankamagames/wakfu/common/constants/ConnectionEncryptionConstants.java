package com.ankamagames.wakfu.common.constants;

import java.security.spec.*;

public interface ConnectionEncryptionConstants
{
    public static final String ALGORITHM = "RSA";
    public static final AlgorithmParameterSpec ALGORITHM_PARAMETERS = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
}
