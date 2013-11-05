/*
 * Copyright (c) by mewin<mewin001@hotmail.de>
 * All rights reserved.
 */
package de.mewin.evaLogic.util;

import de.mewin.evaLogic.Aussage;
import de.mewin.evaLogic.ElementarAussage;
import de.mewin.util.data.Chain;
import java.util.Iterator;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class TableCreator
{
    private boolean[][] data;
    private Aussage aus;
    private ElementarAussage[] eles;
    private Chain<Aussage> teilaussagen;
    
    public TableCreator(Aussage aus, ElementarAussage ... eles)
    {
        this.aus = aus;
        this.eles = eles;
    }

    public void setTeilaussagen(Chain<Aussage> teilaussagen)
    {
        this.teilaussagen = teilaussagen;
    }
    
    public void create()
    {
        data = new boolean[(int) Math.pow(2, eles.length)][eles.length + 1 + (teilaussagen == null ? 0 : teilaussagen.getLength())];
        for (int i = 0; i < data.length; i++)
        {
            for (int j = 0; j < eles.length; j++)
            {
                boolean val = (i & 0x1 << j) > 0;
                eles[j].setValue(val);
                data[i][j] = eles[j].getValue();
            }
            if (teilaussagen != null)
            {
                Iterator<Aussage> it = teilaussagen.iterator();
                for (int j = eles.length; it.hasNext(); j++)
                {
                    data[i][j] = it.next().getResult();
                }
            }
            data[i][data[0].length - 1] = aus.getResult();
        }
    }
    
    private String repeat(char chr, int rep)
    {
        String str = "";
        for (int i = 0; i < rep; i++)
        {
            str += chr;
        }
        return str;
    }
    
    public String[][] toStringArray()
    {
        String[][] arr = new String[data.length + 1][data[0].length];
        for (int i = 0; i < eles.length; i++)
        {
            arr[0][i] = "\u03B1(" + eles[i].toString() + ")";
        }
        if (teilaussagen != null)
        {
            Iterator<Aussage> it = teilaussagen.iterator();
            for (int i = eles.length; it.hasNext(); i++)
            {
                arr[0][i] = "[" + it.next().toString() + "]\u2090";
            }
        }
        arr[0][arr[0].length - 1] = "[" + aus.toString() + "]\u2090";
        for (int i = 0; i < data.length; i++)
        {
            for (int j = 0; j < data[i].length; j++)
            {
                arr[i + 1][j] = data[i][j] ? "1" : "0";
            }
        }
        return arr;
    }

    @Override
    public String toString()
    {
        String str = "";
        int[] colWidth = new int[eles.length + 1];
        for (int i = 0; i < eles.length; i++)
        {
            String col = "\u03B1(" + eles[i].toString() + ")";
            colWidth[i] = col.length();
            str += col + "|";
        }
        String col = "[" + aus.toString() + "]\u2090";
        str += col + "\n";
        colWidth[eles.length] = col.length();
        for (int i = 0; i < eles.length; i++)
        {
            str += repeat('-', colWidth[i]) + "+";
        }
        str += repeat('-', colWidth[eles.length]) + "\n";
        for (int i = 0; i < data.length;i++)
        {
            for (int j = 0; j < data[i].length; j++)
            {
                str += repeat(' ', colWidth[j] - 1);
                if (data[i][j])
                {
                    str += "1";
                }
                else
                {
                    str += "0";
                }
                if (j < data[i].length - 1)
                {
                    str += "|";
                }
            }
            str += "\n";
        }
        
        return str;
    }
}
