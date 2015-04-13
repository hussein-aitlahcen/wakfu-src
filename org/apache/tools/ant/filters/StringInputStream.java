package org.apache.tools.ant.filters;

import org.apache.tools.ant.util.*;
import java.io.*;

public class StringInputStream extends ReaderInputStream
{
    public StringInputStream(final String source) {
        super(new StringReader(source));
    }
    
    public StringInputStream(final String source, final String encoding) {
        super(new StringReader(source), encoding);
    }
}
