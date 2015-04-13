package com.ankamagames.wakfu.client.core.webBrowser;

public abstract class JavascriptBrowserEventHandler implements BrowserEventHandler
{
    protected final SWFBrowser m_browser;
    protected final String m_url;
    private final String m_jsObjectName;
    
    protected JavascriptBrowserEventHandler(final SWFBrowser browser, final String url, final String jsObjectName) {
        super();
        this.m_browser = browser;
        this.m_url = url;
        this.m_jsObjectName = jsObjectName;
    }
    
    @Override
    public String getUrl() {
        return this.m_url;
    }
    
    @Override
    public boolean invokeFunction(final String functionName, final Object[] params) {
        final StringBuilder sb = new StringBuilder();
        sb.append("try{").append(this.m_jsObjectName).append('.');
        sb.append(functionName);
        sb.append('(');
        for (int i = 0, size = params.length; i < size; ++i) {
            if (i != 0) {
                sb.append(", ");
            }
            if (params[i] instanceof String) {
                final String param = (String)params[i];
                sb.append('\'').append(param).append('\'');
            }
            else {
                if (!(params[i] instanceof Number) && !(params[i] instanceof Boolean)) {
                    throw new IllegalArgumentException("Un des param n'est pas du bon type : " + params[i]);
                }
                sb.append(String.valueOf(params[i]));
            }
        }
        sb.append(");} catch (err) {alert(err.message);};");
        return this.m_browser.getBrowser().execute(sb.toString());
    }
}
