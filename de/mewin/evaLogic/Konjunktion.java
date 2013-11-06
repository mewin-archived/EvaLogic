package de.mewin.evaLogic;

import de.mewin.evaLogic.util.LogicChars;
import java.util.Arrays;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class Konjunktion extends Aussage
{
    private Aussage[] aussagen;
    public Konjunktion(Aussage ... aussagen)
    {
        this.aussagen = aussagen;
    }

    @Override
    public boolean getResult()
    {
        if (baum()) return true;
        for (Aussage a : aussagen)
        {
            if (!a.getResult())
            {
                return false;
            }
        }
        
        return true;
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
                str += LogicChars.AND + aussagen[i].toString();
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

    @Override
    public boolean equals(Object obj)
    {
        return obj != null
                && obj instanceof Konjunktion
                && obj.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Arrays.deepHashCode(this.aussagen);
        return hash;
    }
    
    

    public Aussage[] getAussagen()
    {
        return aussagen;
    }
}
