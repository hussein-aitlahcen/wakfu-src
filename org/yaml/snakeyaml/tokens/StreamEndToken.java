package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.*;

public final class StreamEndToken extends Token
{
    public StreamEndToken(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    public ID getTokenId() {
        return ID.StreamEnd;
    }
}
