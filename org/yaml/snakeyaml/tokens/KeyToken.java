package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.*;

public final class KeyToken extends Token
{
    public KeyToken(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    public ID getTokenId() {
        return ID.Key;
    }
}
