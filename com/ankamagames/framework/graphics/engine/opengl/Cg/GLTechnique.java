package com.ankamagames.framework.graphics.engine.opengl.Cg;

import com.ankamagames.framework.graphics.engine.fx.*;
import com.sun.opengl.cg.*;

public class GLTechnique extends Technique
{
    public GLTechnique(final CGtechnique cgTechnique) {
        super(CgGL.cgGetTechniqueName(cgTechnique));
        this.m_passes = new Pass[countPass(cgTechnique)];
        int index = 0;
        for (CGpass cgPass = CgGL.cgGetFirstPass(cgTechnique); cgPass != null; cgPass = CgGL.cgGetNextPass(cgPass)) {
            this.m_passes[index++] = new GLPass(cgPass);
        }
    }
    
    private static int countPass(final CGtechnique cgTechnique) {
        int count = 0;
        for (CGpass cgPass = CgGL.cgGetFirstPass(cgTechnique); cgPass != null; cgPass = CgGL.cgGetNextPass(cgPass)) {
            ++count;
        }
        return count;
    }
}
