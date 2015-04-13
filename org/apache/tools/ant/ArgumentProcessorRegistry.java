package org.apache.tools.ant;

import org.apache.tools.ant.util.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class ArgumentProcessorRegistry
{
    private static final String DEBUG_ARGUMENT_PROCESSOR_REPOSITORY = "ant.argument-processor-repo.debug";
    private static final boolean DEBUG;
    private static final String SERVICE_ID = "META-INF/services/org.apache.tools.ant.ArgumentProcessor";
    private static ArgumentProcessorRegistry instance;
    private List<ArgumentProcessor> processors;
    
    public static ArgumentProcessorRegistry getInstance() {
        return ArgumentProcessorRegistry.instance;
    }
    
    private ArgumentProcessorRegistry() {
        super();
        this.processors = new ArrayList<ArgumentProcessor>();
        this.collectArgumentProcessors();
    }
    
    public List<ArgumentProcessor> getProcessors() {
        return this.processors;
    }
    
    private void collectArgumentProcessors() {
        try {
            final ClassLoader classLoader = LoaderUtils.getContextClassLoader();
            if (classLoader != null) {
                final Enumeration<URL> resources = classLoader.getResources("META-INF/services/org.apache.tools.ant.ArgumentProcessor");
                while (resources.hasMoreElements()) {
                    final URL resource = resources.nextElement();
                    final ArgumentProcessor processor = this.getProcessorByService(resource.openStream());
                    this.registerArgumentProcessor(processor);
                }
            }
            final InputStream systemResource = ClassLoader.getSystemResourceAsStream("META-INF/services/org.apache.tools.ant.ArgumentProcessor");
            if (systemResource != null) {
                final ArgumentProcessor processor2 = this.getProcessorByService(systemResource);
                this.registerArgumentProcessor(processor2);
            }
        }
        catch (Exception e) {
            System.err.println("Unable to load ArgumentProcessor from service META-INF/services/org.apache.tools.ant.ArgumentProcessor (" + e.getClass().getName() + ": " + e.getMessage() + ")");
            if (ArgumentProcessorRegistry.DEBUG) {
                e.printStackTrace(System.err);
            }
        }
    }
    
    public void registerArgumentProcessor(final String helperClassName) throws BuildException {
        this.registerArgumentProcessor(this.getProcessor(helperClassName));
    }
    
    public void registerArgumentProcessor(final Class<? extends ArgumentProcessor> helperClass) throws BuildException {
        this.registerArgumentProcessor(this.getProcessor(helperClass));
    }
    
    private ArgumentProcessor getProcessor(final String helperClassName) {
        try {
            final Class<? extends ArgumentProcessor> cl = (Class<? extends ArgumentProcessor>)Class.forName(helperClassName);
            return this.getProcessor(cl);
        }
        catch (ClassNotFoundException e) {
            throw new BuildException("Argument processor class " + helperClassName + " was not found", e);
        }
    }
    
    private ArgumentProcessor getProcessor(final Class<? extends ArgumentProcessor> processorClass) {
        ArgumentProcessor processor;
        try {
            processor = (ArgumentProcessor)processorClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception e) {
            throw new BuildException("The argument processor class" + processorClass.getClass().getName() + " could not be instanciated with a default constructor", e);
        }
        return processor;
    }
    
    public void registerArgumentProcessor(final ArgumentProcessor processor) {
        if (processor == null) {
            return;
        }
        this.processors.add(processor);
        if (ArgumentProcessorRegistry.DEBUG) {
            System.out.println("Argument processor " + processor.getClass().getName() + " registered.");
        }
    }
    
    private ArgumentProcessor getProcessorByService(final InputStream is) throws IOException {
        InputStreamReader isr = null;
        try {
            try {
                isr = new InputStreamReader(is, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                isr = new InputStreamReader(is);
            }
            final BufferedReader rd = new BufferedReader(isr);
            final String processorClassName = rd.readLine();
            if (processorClassName != null && !"".equals(processorClassName)) {
                return this.getProcessor(processorClassName);
            }
        }
        finally {
            try {
                isr.close();
            }
            catch (IOException ex) {}
        }
        return null;
    }
    
    static {
        DEBUG = "true".equals(System.getProperty("ant.argument-processor-repo.debug"));
        ArgumentProcessorRegistry.instance = new ArgumentProcessorRegistry();
    }
}
