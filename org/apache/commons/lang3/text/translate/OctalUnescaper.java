package org.apache.commons.lang3.text.translate;

import java.io.*;

public class OctalUnescaper extends CharSequenceTranslator
{
    private static int OCTAL_MAX;
    
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        if (input.charAt(index) == '\\' && index < input.length() - 1 && Character.isDigit(input.charAt(index + 1))) {
            final int start = index + 1;
            int end = index + 2;
            while (end < input.length() && Character.isDigit(input.charAt(end))) {
                ++end;
                if (Integer.parseInt(input.subSequence(start, end).toString(), 10) > OctalUnescaper.OCTAL_MAX) {
                    --end;
                    break;
                }
            }
            out.write(Integer.parseInt(input.subSequence(start, end).toString(), 8));
            return 1 + end - start;
        }
        return 0;
    }
    
    static {
        OctalUnescaper.OCTAL_MAX = 377;
    }
}
