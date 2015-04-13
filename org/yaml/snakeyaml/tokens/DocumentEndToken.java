package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.*;

public final class DocumentEndToken extends Token
{
    public DocumentEndToken(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    public ID getTokenId() {
        return ID.DocumentEnd;
    }
}
