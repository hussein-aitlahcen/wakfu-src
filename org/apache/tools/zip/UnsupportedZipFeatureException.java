package org.apache.tools.zip;

import java.util.zip.*;

public class UnsupportedZipFeatureException extends ZipException
{
    private final Feature reason;
    private final ZipEntry entry;
    private static final long serialVersionUID = 4430521921766595597L;
    
    public UnsupportedZipFeatureException(final Feature reason, final ZipEntry entry) {
        super("unsupported feature " + reason + " used in entry " + entry.getName());
        this.reason = reason;
        this.entry = entry;
    }
    
    public Feature getFeature() {
        return this.reason;
    }
    
    public ZipEntry getEntry() {
        return this.entry;
    }
    
    public static class Feature
    {
        public static final Feature ENCRYPTION;
        public static final Feature METHOD;
        public static final Feature DATA_DESCRIPTOR;
        private final String name;
        
        private Feature(final String name) {
            super();
            this.name = name;
        }
        
        public String toString() {
            return this.name;
        }
        
        static {
            ENCRYPTION = new Feature("encryption");
            METHOD = new Feature("compression method");
            DATA_DESCRIPTOR = new Feature("data descriptor");
        }
    }
}
