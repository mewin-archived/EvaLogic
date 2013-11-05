package de.mewin.evaLogic;

import java.util.Objects;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class ElementarAussage extends Aussage
{
    public String name;
    private boolean wert;

    public ElementarAussage(String name, boolean wert)
    {
        this.name = name;
        this.wert = wert;
        if (this.name.equalsIgnoreCase("baum"))
        {
            this.wert = true;
        }
    }
    
    public boolean getValue()
    {
        return this.wert;
    }
    
    public void setValue(boolean val)
    {
        if (this.name.equalsIgnoreCase("baum")) return;
        this.wert = val;
    }

    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof ElementarAussage)
                && obj != null
                && obj.hashCode() == this.hashCode();
    }

    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + (this.wert ? 1 : 0);
        return hash;
    }

    @Override
    public boolean getResult()
    {
        return wert;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    protected boolean baum()
    {
        return this.name.equalsIgnoreCase("baum");
    }
}
