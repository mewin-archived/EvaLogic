package de.mewin.evaLogic;

import de.mewin.evaLogic.util.LogicChars;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public abstract class Aussage
{
    public static Aussage TRUE = new Aussage() { public boolean getResult() {return true; } public String toString() {return Character.toString(LogicChars.TRUE);}};
    public static Aussage FALSE = new Aussage() { public boolean getResult() {return false; }public String toString() {return Character.toString(LogicChars.FALSE);}};
    public abstract boolean getResult();
    
    protected boolean baum()
    {
        return false;
    }
}
