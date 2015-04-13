package com.ankamagames.wakfu.client.core.game.characterInfo.name;

import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class FileRandomNameGenerator implements RandomNameGenerator
{
    private final ArrayList<String> m_names;
    
    public FileRandomNameGenerator(final String filename) {
        super();
        this.m_names = new ArrayList<String>();
        try {
            this.init(filename);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
    
    private void init(final String fileName) throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(ContentFileHelper.openFile(fileName), "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                this.m_names.add(line);
            }
        }
        reader.close();
    }
    
    @Override
    public String getRandomName() {
        return this.m_names.get(MathHelper.random(this.m_names.size()));
    }
    
    @Override
    public void clean() {
        this.m_names.clear();
    }
}
