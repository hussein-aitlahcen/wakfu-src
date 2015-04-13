package org.apache.tools.ant.util;

import org.apache.tools.ant.*;
import java.io.*;

public class FileTokenizer extends ProjectComponent implements Tokenizer
{
    public String getToken(final Reader in) throws IOException {
        return FileUtils.readFully(in);
    }
    
    public String getPostToken() {
        return "";
    }
}
