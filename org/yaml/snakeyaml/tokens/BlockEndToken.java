package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.*;

public final class BlockEndToken extends Token
{
    public BlockEndToken(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    public ID getTokenId() {
        return ID.BlockEnd;
    }
}
