package org.apache.tools.ant.util;

import org.apache.tools.ant.*;

public class ProxySetup
{
    private Project owner;
    public static final String USE_SYSTEM_PROXIES = "java.net.useSystemProxies";
    public static final String HTTP_PROXY_HOST = "http.proxyHost";
    public static final String HTTP_PROXY_PORT = "http.proxyPort";
    public static final String HTTPS_PROXY_HOST = "https.proxyHost";
    public static final String HTTPS_PROXY_PORT = "https.proxyPort";
    public static final String FTP_PROXY_HOST = "ftp.proxyHost";
    public static final String FTP_PROXY_PORT = "ftp.proxyPort";
    public static final String HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts";
    public static final String HTTPS_NON_PROXY_HOSTS = "https.nonProxyHosts";
    public static final String FTP_NON_PROXY_HOSTS = "ftp.nonProxyHosts";
    public static final String HTTP_PROXY_USERNAME = "http.proxyUser";
    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
    public static final String SOCKS_PROXY_HOST = "socksProxyHost";
    public static final String SOCKS_PROXY_PORT = "socksProxyPort";
    public static final String SOCKS_PROXY_USERNAME = "java.net.socks.username";
    public static final String SOCKS_PROXY_PASSWORD = "java.net.socks.password";
    
    public ProxySetup(final Project owner) {
        super();
        this.owner = owner;
    }
    
    public static String getSystemProxySetting() {
        try {
            return System.getProperty("java.net.useSystemProxies");
        }
        catch (SecurityException e) {
            return null;
        }
    }
    
    public void enableProxies() {
        if (getSystemProxySetting() == null) {
            String proxies = this.owner.getProperty("java.net.useSystemProxies");
            if (proxies == null || Project.toBoolean(proxies)) {
                proxies = "true";
            }
            final String message = "setting java.net.useSystemProxies to " + proxies;
            try {
                this.owner.log(message, 4);
                System.setProperty("java.net.useSystemProxies", proxies);
            }
            catch (SecurityException e) {
                this.owner.log("Security Exception when " + message);
            }
        }
    }
}
