package org.apache.tools.ant.types.selectors.modifiedselector;

import java.util.*;
import org.apache.tools.ant.*;
import java.security.*;
import java.io.*;

public class DigestAlgorithm implements Algorithm
{
    private static final int BYTE_MASK = 255;
    private static final int BUFFER_SIZE = 8192;
    private String algorithm;
    private String provider;
    private MessageDigest messageDigest;
    private int readBufferSize;
    
    public DigestAlgorithm() {
        super();
        this.algorithm = "MD5";
        this.provider = null;
        this.messageDigest = null;
        this.readBufferSize = 8192;
    }
    
    public void setAlgorithm(final String algorithm) {
        this.algorithm = ((algorithm != null) ? algorithm.toUpperCase(Locale.ENGLISH) : null);
    }
    
    public void setProvider(final String provider) {
        this.provider = provider;
    }
    
    public void initMessageDigest() {
        if (this.messageDigest != null) {
            return;
        }
        if (this.provider != null && !"".equals(this.provider) && !"null".equals(this.provider)) {
            try {
                this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
                return;
            }
            catch (NoSuchAlgorithmException noalgo) {
                throw new BuildException(noalgo);
            }
            catch (NoSuchProviderException noprovider) {
                throw new BuildException(noprovider);
            }
        }
        try {
            this.messageDigest = MessageDigest.getInstance(this.algorithm);
        }
        catch (NoSuchAlgorithmException noalgo) {
            throw new BuildException(noalgo);
        }
    }
    
    public boolean isValid() {
        return "SHA".equals(this.algorithm) || "MD5".equals(this.algorithm);
    }
    
    public String getValue(final File file) {
        this.initMessageDigest();
        String checksum = null;
        try {
            if (!file.canRead()) {
                return null;
            }
            FileInputStream fis = null;
            final byte[] buf = new byte[this.readBufferSize];
            try {
                this.messageDigest.reset();
                fis = new FileInputStream(file);
                final DigestInputStream dis = new DigestInputStream(fis, this.messageDigest);
                while (dis.read(buf, 0, this.readBufferSize) != -1) {}
                dis.close();
                fis.close();
                fis = null;
                final byte[] fileDigest = this.messageDigest.digest();
                final StringBuffer checksumSb = new StringBuffer();
                for (int i = 0; i < fileDigest.length; ++i) {
                    final String hexStr = Integer.toHexString(0xFF & fileDigest[i]);
                    if (hexStr.length() < 2) {
                        checksumSb.append("0");
                    }
                    checksumSb.append(hexStr);
                }
                checksum = checksumSb.toString();
            }
            catch (Exception e) {
                return null;
            }
        }
        catch (Exception e2) {
            return null;
        }
        return checksum;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("<DigestAlgorithm:");
        buf.append("algorithm=").append(this.algorithm);
        buf.append(";provider=").append(this.provider);
        buf.append(">");
        return buf.toString();
    }
}
