package org.apache.commons.lang3.text.translate;

import java.util.*;
import java.io.*;

public class LookupTranslator extends CharSequenceTranslator
{
    private final HashMap<CharSequence, CharSequence> lookupMap;
    private final int shortest;
    private final int longest;
    
    public LookupTranslator(final CharSequence[]... lookup) {
        super();
        this.lookupMap = new HashMap<CharSequence, CharSequence>();
        int _shortest = Integer.MAX_VALUE;
        int _longest = 0;
        if (lookup != null) {
            for (final CharSequence[] seq : lookup) {
                this.lookupMap.put(seq[0], seq[1]);
                final int sz = seq[0].length();
                if (sz < _shortest) {
                    _shortest = sz;
                }
                if (sz > _longest) {
                    _longest = sz;
                }
            }
        }
        this.shortest = _shortest;
        this.longest = _longest;
    }
    
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        int max = this.longest;
        if (index + this.longest > input.length()) {
            max = input.length() - index;
        }
        for (int i = max; i >= this.shortest; --i) {
            final CharSequence subSeq = input.subSequence(index, index + i);
            final CharSequence result = this.lookupMap.get(subSeq);
            if (result != null) {
                out.write(result.toString());
                return i;
            }
        }
        return 0;
    }
}
