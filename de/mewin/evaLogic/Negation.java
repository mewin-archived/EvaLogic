package de.mewin.evaLogic;

import de.mewin.evaLogic.util.LogicChars;
import java.util.Objects;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class Negation extends Aussage
{
    private Aussage aussage;
    
    public Negation(Aussage aussage)
    {
        this.aussage = aussage;
    }
    
    @Override
    public boolean getResult()
    {
        return baum() || !this.aussage.getResult();
    }

    @Override
    public String toString()
    {
        return LogicChars.NOT + this.aussage.toString();
    }

    public Aussage getAussage()
    {
        return aussage;
    }

    @Override
    protected boolean baum()
    {
        return aussage.baum();
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj != null
                && obj instanceof Negation
                && obj.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.aussage);
        return hash;
    }
}
