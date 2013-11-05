package de.mewin.evaLogic;

import de.mewin.evaLogic.util.LogicChars;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class Disjunktion extends Aussage
{
    private Aussage[] aussagen;
    
    public Disjunktion(Aussage ... aussagen)
    {
        this.aussagen = aussagen;
    }

    @Override
    public boolean getResult()
    {
        for (Aussage a : aussagen)
        {
            if (a.getResult())
            {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String toString()
    {
        if (aussagen.length < 1)
        {
            return Aussage.FALSE.toString();
        }
        else
        {
            String str = "(" + aussagen[0].toString();
            for (int i = 1; i < aussagen.length; i++)
            {
                str += LogicChars.OR + aussagen[i].toString();
            }
            return str + ")";
        }
    }

    @Override
    protected boolean baum()
    {
        for (Aussage a : aussagen)
        {
            if (a.baum())
            {
                return true;
            }
        }
        return false;
    }
}
