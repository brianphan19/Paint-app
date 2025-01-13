/*
 * TCSS 305 - Assignment 6
 */
package model;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

/**
 * Interface for paint tools.
 * 
 * This interface defines the required methods for implementing various paint tools
 * that interact with a drawing panel. Implementing classes must provide functionality
 * for responding to mouse events, drawing shapes, and integrating tools into the user 
 * interface.
 * 
 * @version Nov 22, 2024
 * @author Quang
 */
public interface PaintTool {

    /**
     * Called when the user presses the mouse.
     * 
     * This method initializes the process of creating a shape or interacting
     * with the canvas when the mouse button is pressed.
     * 
     * @param theEvent the mouse event triggering this action
     */
    void press(MouseEvent theEvent);

    /**
     * Called when the user drags the mouse.
     * <p>
     * This method updates the shape being drawn or manipulated
     * based on the current mouse position as the user drags.
     * </p>
     * 
     * @param theEvent the mouse event triggering this action
     */
    void drag(MouseEvent theEvent);

    /**
     * Called when the user releases the mouse.
     * <p>
     * This method finalizes the shape or interaction that was started
     * with the mouse press and updates the drawing panel.
     * </p>
     * 
     * @param theEvent the mouse event triggering this action
     */
    void release(MouseEvent theEvent);

    /**
     * Draws the shape for this tool.
     * 
     * This method is responsible for rendering the current state of the shape
     * being created or manipulated by the tool onto the canvas.
     * 
     * @param g2 the graphics context used for drawing
     */
    void draw(Graphics2D theG2);

    /**
     * Returns the menu item for this tool.
     * 
     * The menu item allows users to select this tool from a menu interface.
     * 
     * @param theGroup the button group to synchronize tools
     * @return the menu item associated with this tool
     */
    JMenuItem getMenuItem(ButtonGroup theGroup);

    /**
     * Returns the toggle button for this tool.
     * 
     * The toggle button allows users to select this tool from a toolbar interface.
     * 
     * @param group the button group to synchronize tools
     * @return the toggle button associated with this tool
     */
    JToggleButton getToggleButton(ButtonGroup theGroup);

    String getName();
}
