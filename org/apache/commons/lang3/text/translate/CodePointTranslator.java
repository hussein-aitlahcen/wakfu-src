package org.apache.commons.lang3.text.translate;

import java.io.*;

public abstract class CodePointTranslator extends CharSequenceTranslator
{
    public final int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        final int codepoint = Character.codePointAt(input, index);
        final boolean consumed = this.translate(codepoint, out);
        if (consumed) {
            return 1;
        }
        return 0;
    }
    
    public abstract boolean translate(final int p0, final Writer p1) throws IOException;
}
