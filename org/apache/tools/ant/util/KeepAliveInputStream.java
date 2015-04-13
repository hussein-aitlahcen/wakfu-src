package org.apache.tools.ant.util;

import java.io.*;

public class KeepAliveInputStream extends FilterInputStream
{
    public KeepAliveInputStream(final InputStream in) {
        super(in);
    }
    
    public void close() throws IOException {
    }
    
    public static InputStream wrapSystemIn() {
        return new KeepAliveInputStream(System.in);
    }
}
