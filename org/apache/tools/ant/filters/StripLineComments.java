package org.apache.tools.ant.filters;

import java.util.*;
import java.io.*;
import org.apache.tools.ant.types.*;

public final class StripLineComments extends BaseParamFilterReader implements ChainableReader
{
    private static final String COMMENTS_KEY = "comment";
    private Vector<String> comments;
    private String line;
    
    public StripLineComments() {
        super();
        this.comments = new Vector<String>();
        this.line = null;
    }
    
    public StripLineComments(final Reader in) {
        super(in);
        this.comments = new Vector<String>();
        this.line = null;
    }
    
    public int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        int ch = -1;
        if (this.line != null) {
            ch = this.line.charAt(0);
            if (this.line.length() == 1) {
                this.line = null;
            }
            else {
                this.line = this.line.substring(1);
            }
        }
        else {
            this.line = this.readLine();
            final int commentsSize = this.comments.size();
            while (this.line != null) {
                for (int i = 0; i < commentsSize; ++i) {
                    final String comment = this.comments.elementAt(i);
                    if (this.line.startsWith(comment)) {
                        this.line = null;
                        break;
                    }
                }
                if (this.line != null) {
                    break;
                }
                this.line = this.readLine();
            }
            if (this.line != null) {
                return this.read();
            }
        }
        return ch;
    }
    
    public void addConfiguredComment(final Comment comment) {
        this.comments.addElement(comment.getValue());
    }
    
    private void setComments(final Vector<String> comments) {
        this.comments = comments;
    }
    
    private Vector<String> getComments() {
        return this.comments;
    }
    
    public Reader chain(final Reader rdr) {
        final StripLineComments newFilter = new StripLineComments(rdr);
        newFilter.setComments(this.getComments());
        newFilter.setInitialized(true);
        return newFilter;
    }
    
    private void initialize() {
        final Parameter[] params = this.getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                if ("comment".equals(params[i].getType())) {
                    this.comments.addElement(params[i].getValue());
                }
            }
        }
    }
    
    public static class Comment
    {
        private String value;
        
        public final void setValue(final String comment) {
            if (this.value != null) {
                throw new IllegalStateException("Comment value already set.");
            }
            this.value = comment;
        }
        
        public final String getValue() {
            return this.value;
        }
        
        public void addText(final String comment) {
            this.setValue(comment);
        }
    }
}
