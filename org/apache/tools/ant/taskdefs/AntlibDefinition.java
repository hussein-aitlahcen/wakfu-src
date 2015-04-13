package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;

public class AntlibDefinition extends Task
{
    private String uri;
    private ClassLoader antlibClassLoader;
    
    public AntlibDefinition() {
        super();
        this.uri = "";
    }
    
    public void setURI(String uri) throws BuildException {
        if (uri.equals("antlib:org.apache.tools.ant")) {
            uri = "";
        }
        if (uri.startsWith("ant:")) {
            throw new BuildException("Attempt to use a reserved URI " + uri);
        }
        this.uri = uri;
    }
    
    public String getURI() {
        return this.uri;
    }
    
    public void setAntlibClassLoader(final ClassLoader classLoader) {
        this.antlibClassLoader = classLoader;
    }
    
    public ClassLoader getAntlibClassLoader() {
        return this.antlibClassLoader;
    }
}
