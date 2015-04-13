package com.ankamagames.xulor2.util.xmlToJava;

public class ThemeCompileData
{
    private String m_package;
    private String m_themeInitLoaderName;
    private String m_styleProviderName;
    private String m_outputDirectory;
    
    public ThemeCompileData(final String aPackage, final String themeInitLoaderName, final String styleProviderName, final String outputDirectory) {
        super();
        this.m_package = aPackage;
        this.m_themeInitLoaderName = themeInitLoaderName;
        this.m_styleProviderName = styleProviderName;
        this.m_outputDirectory = outputDirectory;
    }
    
    public String getPackage() {
        return this.m_package;
    }
    
    public String getThemeInitLoaderName() {
        return this.m_themeInitLoaderName;
    }
    
    public String getStyleProviderName() {
        return this.m_styleProviderName;
    }
    
    public String getOutputDirectory() {
        return this.m_outputDirectory;
    }
}
