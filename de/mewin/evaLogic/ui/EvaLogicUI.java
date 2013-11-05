/*
 * Copyright (c) by mewin<mewin001@hotmail.de>
 * All rights reserved.
 */
package de.mewin.evaLogic.ui;

import de.mewin.evaLogic.Aussage;
import de.mewin.evaLogic.parser.LogicParser;
import de.mewin.evaLogic.util.LogicChars;
import de.mewin.evaLogic.util.TableCreator;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class EvaLogicUI
{
    private JFrame frame;
    private JTable table;
    private JTextField input;
    private JScrollPane scroll;
    private DefaultTableModel model;
    
    public EvaLogicUI()
    {
        this.frame = new JFrame("EvaLogic");
        init();
    }
    
    private void init()
    {
        input = new JTextField();
        
        input.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke)
            {
            }

            @Override
            public void keyPressed(KeyEvent ke){}

            @Override
            public void keyReleased(KeyEvent ke)
            {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    createTable();
                }
            }
        });
        table = new JTable();
        model = new DefaultTableModel();
        table.setModel(model);
        scroll = new JScrollPane(table);
        
        frame.setLayout(new BorderLayout());
        frame.add(input, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void createTable()
    {
        LogicParser parser = new LogicParser();
        String text = input.getText().trim();
        text = text.replace("<->", LogicChars.EQUALITY + "");
        text = text.replace("->", LogicChars.IMPLICATION + "");
        text = text.replace("v", LogicChars.OR + "");
        text = text.replace("-T", LogicChars.FALSE + "");
        text = text.replace("-", LogicChars.NOT + "");
        text = text.replace("T", LogicChars.TRUE + "");
        text = text.replace("&", LogicChars.AND + "");
        if (text.length() < 1)
        {
            return;
        }
        if (text.charAt(0) != '(')
        {
            text = "(" + text + ")";
        }
        input.setText(text);
        parser.read(text.toCharArray());
        parser.parse();
        TableCreator creator = new TableCreator(parser.getAussage(), parser.getEles());
        //creator.setTeilaussagen(parser.getTeilAussagen());
        creator.create();
        
        for (int i = model.getRowCount() - 1; i >= 0; i--)
        {
            model.removeRow(i);
        }
        
        String[][] arr = creator.toStringArray();
        model.setColumnCount(arr[0].length);
        for (String[] row : arr)
        {
            model.addRow(row);
        }
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        EvaLogicUI ui = new EvaLogicUI();
        ui.show();
    }
}
