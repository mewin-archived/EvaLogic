/*
 * Copyright (c) by mewin<mewin001@hotmail.de>
 * All rights reserved.
 */
package de.mewin.evaLogic.parser;

import de.mewin.evaLogic.Aequivalenz;
import de.mewin.evaLogic.Aussage;
import de.mewin.evaLogic.Disjunktion;
import de.mewin.evaLogic.ElementarAussage;
import de.mewin.evaLogic.Implikation;
import de.mewin.evaLogic.Konjunktion;
import de.mewin.evaLogic.Negation;
import de.mewin.evaLogic.util.LogicChars;
import de.mewin.util.data.Chain;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class LogicParser
{
    private Chain<Character> tokens;
    private char cachedChar = '\u0000';
    private Aussage aussage;
    private HashMap<String, ElementarAussage> eles;
    private Chain<Aussage> teilAussagen;
    
    public LogicParser()
    {
        tokens = new Chain<>();
        eles = new HashMap<>();
        teilAussagen = new Chain<>();
    }
    
    public void parse()
    {
        aussage = parse(tokens.iterator());
    }

    public Aussage getAussage()
    {
        return aussage;
    }
    
    public ElementarAussage[] getEles()
    {
        Chain<ElementarAussage> chain = new Chain(eles.values());
        chain.sort(new Comparator<ElementarAussage>() {

            @Override
            public int compare(ElementarAussage t, ElementarAussage t1)
            {
                return t.name.compareTo(t1.name);
            }
        });
        return chain.toArray(ElementarAussage.class);
    }
    
    private Aussage parse(Iterator<Character> it)
    {
        char chr = cachedChar;
        if (chr == '\u0000')
        {
            if (!it.hasNext())
            {
                return null;
            }
            chr = it.next();
        }
        cachedChar = '\u0000';
        switch (chr)
        {
            case LogicChars.TRUE:
                return Aussage.TRUE;
            case LogicChars.FALSE:
                return Aussage.FALSE;
            case '(':
                Aussage a = parseKlammer(it);
                if (! (a instanceof ElementarAussage)
                        && a != Aussage.FALSE
                        && a != Aussage.TRUE)
                {
                    teilAussagen.append(a);
                }
                return a;
            case LogicChars.NOT:
                return new Negation(parse(it));
            case ')':
            case LogicChars.AND:
            case LogicChars.OR:
            case LogicChars.IMPLICATION:
            case LogicChars.EQUALITY:
                throw new RuntimeException("Unexpected char: " + chr);
            default:
                ElementarAussage ele = parseElem(it, chr);
                if (!eles.containsKey(ele.name))
                {
                    eles.put(ele.name, ele);
                }
                return eles.get(ele.name);
        }
    }
    
    public Chain<Aussage> getTeilAussagen()
    {
        return teilAussagen;
    }
    
    private ElementarAussage parseElem(Iterator<Character> it, char firstChar)
    {
        String name = Character.toString(firstChar);
        char r;
        while (it.hasNext())
        {
            r = it.next();
            if (r == '('
                    || r == ')'
                    || r == LogicChars.NOT
                    || r == LogicChars.EQUALITY
                    || r == LogicChars.IMPLICATION
                    || r == LogicChars.AND
                    || r == LogicChars.OR)
            {
                cachedChar = r;
                return new ElementarAussage(name, true);
            }
            else
            {
                name += r;
            }
        }
        return new ElementarAussage(name, true);
    }
    
    private Aussage parseKlammer(Iterator<Character> it)
    {
        char type = '\u0000';
        Chain<Aussage> aussagen = new Chain<>();
        aussagen.append(parse(it));
        char chr = cachedChar;
        if (chr == '\u0000')
        {
            chr = it.next();
        }
        while (it.hasNext() || chr != '\u0000')
        {
            if (chr == '\u0000')
            {
                chr = it.next();
            }
            cachedChar = '\u0000';
            switch (chr)
            {
                case ')':
                   Iterator<Aussage> it2 = aussagen.iterator();
                   switch (type)
                   {
                       case LogicChars.AND:
                           return new Konjunktion(aussagen.toArray(Aussage.class));
                       case LogicChars.OR:
                           return new Disjunktion(aussagen.toArray(Aussage.class));
                       case LogicChars.IMPLICATION:
                           return new Implikation(it2.next(), it2.next());
                       case LogicChars.EQUALITY:
                           return new Aequivalenz(it2.next(), it2.next());
                       default:
                           return it2.next();
                   }
                case LogicChars.AND:
                    if (type != '\u0000' && type != LogicChars.AND)
                    {
                        throw new RuntimeException("Syntax error: unexpected char " + chr);
                    }
                    type = LogicChars.AND;
                    aussagen.append(parse(it));
                    break;
                case LogicChars.OR:
                    if (type != '\u0000' && type != LogicChars.OR)
                    {
                        throw new RuntimeException("Syntax error: unexpected char " + chr);
                    }
                    type = LogicChars.OR;
                    aussagen.append(parse(it));
                    break;
                case LogicChars.IMPLICATION:
                    if (type != '\u0000' && type != LogicChars.IMPLICATION)
                    {
                        throw new RuntimeException("Syntax error: unexpected char " + chr);
                    }
                    type = LogicChars.IMPLICATION;
                    aussagen.append(parse(it));
                    break;
                case LogicChars.EQUALITY:
                    if (type != '\u0000' && type != LogicChars.EQUALITY)
                    {
                        throw new RuntimeException("Syntax error: unexpected char " + chr);
                    }
                    type = LogicChars.EQUALITY;
                    aussagen.append(parse(it));
                    break;
                default:
                    throw new RuntimeException("Unexpectec char: " + chr);
            }
            chr = cachedChar;
        }
        throw new RuntimeException("Unexpected end of stream!");
    }
    
    public void read(char[] chars)
    {
        for (char chr : chars)
        {
            if (chr == ' '
                    || chr == '\n'
                    || chr == '\r'
                    || chr == '\t')
            {
                continue;
            }
            tokens.append(chr);
        }
    }
    
    public void read(InputStream in) throws IOException
    {
        int i;
        while ((i = in.read()) > -1)
        {
            if (i == ' '
                    || i == '\n'
                    || i == '\r'
                    || i == '\t')
            {
                continue;
            }
            tokens.append((char) i);
        }
    }
}
