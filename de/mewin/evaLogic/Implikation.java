package de.mewin.evaLogic;

import de.mewin.evaLogic.util.LogicChars;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class Implikation extends Aussage
{
    private Aussage a1, a2;
    
    public Implikation(Aussage a1, Aussage a2)
    {
        this.a1 = a1;
        this.a2 = a2;
    }

    @Override
    public boolean getResult()
    {
        return baum() || !a1.getResult() || a2.getResult();
    }

    @Override
    public String toString()
    {
        return "(" + a1.toString() + LogicChars.IMPLICATION + a2.toString() + ")";
    }

    @Override
    protected boolean baum()
    {
        return a1.baum() || a2.baum();
    }

    public Aussage getAussage1()
    {
        return a1;
    }

    public Aussage getAussage2()
    {
        return a2;
    }
}
