package de.mewin.evaLogic.util;

import de.mewin.evaLogic.Aequivalenz;
import de.mewin.evaLogic.Aussage;
import de.mewin.evaLogic.Disjunktion;
import de.mewin.evaLogic.Implikation;
import de.mewin.evaLogic.Konjunktion;
import de.mewin.evaLogic.Negation;
import de.mewin.util.data.Chain;
import java.util.HashSet;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class Normalizer
{
    public static Aussage nnf(Aussage a)
    {
        if (a instanceof Negation)
        {
            Aussage bas = ((Negation)a).getAussage();
            if (bas instanceof Negation)
            {
                return nnf(((Negation)bas).getAussage());
            }
            else if (bas instanceof Disjunktion)
            {
                Disjunktion dis = (Disjunktion) bas;
                Aussage[] aussagen = new Aussage[dis.getAussagen().length];
                for (int i = 0; i < aussagen.length; i++)
                {
                    aussagen[i] = nnf(new Negation(dis.getAussagen()[i]));
                }
                return new Konjunktion(aussagen);
            }
            else if (bas instanceof Konjunktion)
            {
                Konjunktion kon = (Konjunktion) bas;
                Aussage[] aussagen = new Aussage[kon.getAussagen().length];
                for (int i = 0; i < aussagen.length; i++)
                {
                    aussagen[i] = nnf(new Negation(kon.getAussagen()[i]));
                }
                return new Disjunktion(aussagen);
            }
            else if (bas instanceof Implikation)
            {
                Implikation im = (Implikation) bas;
                return nnf(new Negation(new Disjunktion(new Negation(im.getAussage1()), im.getAussage2())));
            }
            else if (bas instanceof Aequivalenz)
            {
                Aequivalenz ae = (Aequivalenz) bas;
                return nnf(new Negation(new Disjunktion(
                        new Konjunktion(ae.getAussage1(), ae.getAussage2()), 
                        new Konjunktion(
                            new Negation(ae.getAussage1()), 
                            new Negation(ae.getAussage2())))));
            }
            else
            {
                return a;
            }
        }
        else if (a instanceof Konjunktion)
        {
            Konjunktion kon = (Konjunktion) a;
            Aussage[] aussagen = new Aussage[kon.getAussagen().length];
            for (int i = 0; i < aussagen.length; i++)
            {
                aussagen[i] = nnf(kon.getAussagen()[i]);
            }
            return new Konjunktion(aussagen);
        }
        else if (a instanceof Disjunktion)
        {
            Disjunktion dis = (Disjunktion) a;
            Aussage[] aussagen = new Aussage[dis.getAussagen().length];
            for (int i = 0; i < aussagen.length; i++)
            {
                aussagen[i] = nnf(dis.getAussagen()[i]);
            }
            return new Disjunktion(aussagen);
        }
        else if (a instanceof Implikation)
        {
            Implikation im = (Implikation) a;
            return new Disjunktion(getNeg(im.getAussage1()), nnf(im.getAussage2()));
        }
        else if (a instanceof Aequivalenz)
        {
            Aequivalenz ae = (Aequivalenz) a;
            return new Aequivalenz(nnf(ae.getAussage1()), nnf(ae.getAussage2()));
        }
        else
        {
            return a;
        }
    }
    
    /**
     * wandelt eine Aussage in konjunktive Normalenform um
     * @param a die Aussage in NNF
     * @return die Aussage in KNF
     */
    public static Aussage knf(Aussage a)
    {
        if (a instanceof Konjunktion)
        {
            try
            {
                HashSet<Aussage> aussagen = shortenKons((Konjunktion) a);
                return new Konjunktion(aussagen.toArray(new Aussage[0]));
            }
            catch(SetTo t)
            {
                return t.to;
            }
        }
        else if (a instanceof Disjunktion)
        {
            Disjunktion dis = (Disjunktion) a;
            Chain<Aussage> aussagen = new Chain<>();
            Konjunktion kon = null;
            for (Aussage aussage : dis.getAussagen())
            {
                if (aussage instanceof Konjunktion
                        && kon == null)
                {
                    kon = (Konjunktion) aussage;
                }
                else
                {
                    aussagen.append(aussage);
                }
            }
            if (kon != null)
            {
                return knf(distribution(knf(new Disjunktion(aussagen.toArray(Aussage.class))), kon));
            }
            else if (aussagen.getLength() == 1)
            {
                return knf(aussagen.iterator().next());
            }
            else
            {
                return a;
            }
        }
        else if (a instanceof Implikation)
        {
            Implikation im = (Implikation) a;
            return knf(new Disjunktion(getNeg(im.getAussage1()), im.getAussage2()));
        }
        else if (a instanceof Aequivalenz)
        {
            Aequivalenz ae = (Aequivalenz) a;
            return knf(new Disjunktion(
                        new Konjunktion(knf(ae.getAussage1()), knf(ae.getAussage2())), 
                        new Konjunktion(
                            getNeg(knf(ae.getAussage1())), 
                            getNeg(knf(ae.getAussage2())))));
        }
        else
        {
            return a;
        }
    }
    
    private static Aussage getNeg(Aussage a)
    {
        if (a instanceof Negation)
        {
            return ((Negation)a).getAussage();
        }
        else if (a instanceof Konjunktion
                || a instanceof Disjunktion)
        {
            return nnf(new Negation(a));
        }
        else
        {
            return new Negation(a);
        }
    }
    
    //Av(B&C) <=> (AvB)&(AvC)
    public static Konjunktion distribution(Aussage a, Konjunktion kon)
    {
        Aussage[] diss = new Aussage[kon.getAussagen().length];
        for (int i = 0; i < diss.length; i++)
        {
            diss[i] = wrapDis(new Disjunktion(knf(a), kon.getAussagen()[i]));
        }
        return new Konjunktion(diss);
    }
    
    public static Aussage wrapDis(Disjunktion dis)
    {
        try
        {
            return new Disjunktion(doWrapDis(dis).toArray(new Aussage[0]));
        }
        catch(SetTo to)
        {
            return to.to;
        }
    }
    
    private static HashSet<Aussage> doWrapDis(Disjunktion dis) throws SetTo
    {
        HashSet<Aussage> aussagen = new HashSet<>();
        for (Aussage a : dis.getAussagen())
        {
            if (a instanceof Disjunktion)
            {
                try
                {
                    aussagen.addAll(doWrapDis((Disjunktion)a));
                }
                catch(SetTo to)
                {
                    if (to.to == Aussage.TRUE)
                    {
                        throw to;
                    }
                    else if (to.to != Aussage.FALSE)
                    {
                        aussagen.add(to.to);
                    }
                }
            }
            else if (a instanceof Implikation)
            {
                Implikation im = (Implikation) a;
                aussagen.add(getNeg(im.getAussage1()));
                aussagen.add(im.getAussage2());
            }
            else if (a == Aussage.TRUE)
            {
                throw new SetTo(Aussage.TRUE);
            }
            else if (a != Aussage.FALSE)
            {
                if (aussagen.contains(getNeg(a)))
                {
                    throw new SetTo(Aussage.TRUE);
                }
                else if (!aussagen.contains(a))
                {
                    aussagen.add(a);
                }
            }
        }
        if (aussagen.size()> 1)
        {
            return aussagen;
        }
        else if (aussagen.size() < 1)
        {
            throw new SetTo(Aussage.FALSE);
        }
        else 
        {
            throw new SetTo(aussagen.iterator().next());
        }
    }
    
    private static HashSet<Aussage> shortenKons(Konjunktion kon) throws SetTo
    {
            HashSet<Aussage> aussagen = new HashSet<>();
            for (Aussage aussage : kon.getAussagen())
            {
                if (aussage instanceof Konjunktion)
                {
                    aussagen.addAll(shortenKons((Konjunktion)aussage));
                }
                else if (aussage == Aussage.FALSE)
                {
                    throw new SetTo(Aussage.FALSE);
                }
                else if (aussage != Aussage.TRUE)
                {
                    if (aussagen.contains(getNeg(aussage)))
                    {
                        throw new SetTo(Aussage.FALSE);
                    }
                    else if (!aussagen.contains(aussage))
                    {
                        aussagen.add(knf(aussage));
                    }
                }
            }
            if (aussagen.size() > 1)
            {
                return aussagen;
            }
            else if (aussagen.size() < 1)
            {
                throw new SetTo(Aussage.TRUE);
            }
            else
            {
                throw new SetTo(aussagen.iterator().next());
            }
    }
    
    private static class SetTo extends Throwable
    {
        private Aussage to;

        private SetTo(Aussage to)
        {
            this.to = to;
        }
    }
}
