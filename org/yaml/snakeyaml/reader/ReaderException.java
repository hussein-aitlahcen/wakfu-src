package org.yaml.snakeyaml.reader;

import org.yaml.snakeyaml.error.*;

public class ReaderException extends YAMLException
{
    private static final long serialVersionUID = 8710781187529689083L;
    private final String name;
    private final char character;
    private final int position;
    
    public ReaderException(final String name, final int position, final char character, final String message) {
        super(message);
        this.name = name;
        this.character = character;
        this.position = position;
    }
    
    public String getName() {
        return this.name;
    }
    
    public char getCharacter() {
        return this.character;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public String toString() {
        return "unacceptable character '" + this.character + "' (0x" + Integer.toHexString(this.character).toUpperCase() + ") " + this.getMessage() + "\nin \"" + this.name + "\", position " + this.position;
    }
}
