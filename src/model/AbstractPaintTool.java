/*
 * TCSS 305 - Assignment 6
 */package model;

import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import view.DrawingPanel;


/**
 * Abstract base class for paint tools.
 * 
 * This class provides a common foundation for implementing various paint tools
 * that can be used within a drawing application. It includes methods for
 * creating menu items and toggle buttons to integrate tools into the UI, and 
 * for rendering the current shape being drawn.
 * 
 * @author Quang
 * @version Nov 22, 2024
 * 
 */

public abstract class AbstractPaintTool implements PaintTool {
    
    /** The name of the tool. */
    protected final String myName;
    
    /** The icon representing the tool. */
    protected final Icon myIcon;
    
    /** Reference to the drawing panel this tool interacts with. */
    protected final DrawingPanel myDrawingPanel;
    
    /** The current shape being drawn by this tool. */
    protected Shape myCurrentShape;

    /**
     * Constructs a new paint tool with the specified name, icon, and drawing panel.
     *
     * @param theName the name of the tool
     * @param theIcon the icon representing the tool
     * @param thePanel the drawing panel this tool interacts with
     */
    public AbstractPaintTool(final String theName, final Icon theIcon, 
            final DrawingPanel thePanel) {
        this.myName = theName;
        this.myIcon = theIcon;
        this.myDrawingPanel = thePanel;
    }

    /**
     * Creates a menu item for this tool and adds it to the provided button group.
     * When the menu item is selected, this tool is set as the current tool in the 
     * drawing panel.
     *
     * @param theGroup the button group to which the menu item belongs
     * @return the created menu item
     */
    @Override
    public JMenuItem getMenuItem(final ButtonGroup theGroup) {
        final JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(myName, myIcon);
        menuItem.addActionListener(e -> myDrawingPanel.setCurrentTool(this));
        theGroup.add(menuItem);
        
        return menuItem;
    }

    /**
     * Creates a toggle button for this tool and adds it to the provided button group.
     * When the toggle button is selected, this tool is set as the current tool in the 
     * drawing panel.
     *
     * @param theGroup the button group to which the toggle button belongs
     * @return the created toggle button
     */
    @Override
    public JToggleButton getToggleButton(final ButtonGroup theGroup) {
        final JToggleButton button = new JToggleButton(myName, myIcon);
        button.addActionListener(e -> myDrawingPanel.setCurrentTool(this));
        theGroup.add(button);
        return button;
    }

    /**
     * Draws the current shape on the provided {@code Graphics2D} context.
     * 
     * This method should be called during the rendering process to display the shape
     * being drawn by the tool. If no shape is currently being drawn, this method does nothing.
     *
     * @param theG2 the graphics context to use for drawing
     */
    @Override
    public void draw(final Graphics2D theG2) {
        if (myCurrentShape != null) {
            theG2.draw(myCurrentShape);
        }
    }
    
    @Override
    public String getName() {
        return this.myName;
    }
}
