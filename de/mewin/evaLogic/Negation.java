package de.mewin.evaLogic;

import de.mewin.evaLogic.util.LogicChars;

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

    @Override
    protected boolean baum()
    {
        return aussage.baum();
    }
}
