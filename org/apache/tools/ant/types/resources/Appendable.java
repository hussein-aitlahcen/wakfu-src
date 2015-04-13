package org.apache.tools.ant.types.resources;

import java.io.*;

public interface Appendable
{
    OutputStream getAppendOutputStream() throws IOException;
}
