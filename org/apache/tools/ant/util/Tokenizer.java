package org.apache.tools.ant.util;

import java.io.*;

public interface Tokenizer
{
    String getToken(Reader p0) throws IOException;
    
    String getPostToken();
}
