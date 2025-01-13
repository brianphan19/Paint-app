/*
 * TCSS 305 - Assignment 6
 */
package model;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import view.DrawingPanel;

/**
 * Tool to draw rectangles.
 * 
 * This tool allows the user to draw rectangles on the drawing panel
 *  by clicking and dragging the mouse. The rectangle is defined by two 
 *  diagonal points determined by the mouse press and drag events.
 * 
 * @author Quang
 * @version Nov 22, 2024
 */
public class RectangleTool extends AbstractPaintTool {
    /** Out of Canvas number for null-ing the object. */
    private static final int NULL_NUMBER = -50;
    /** The starting point for the rectangle, set when the mouse is pressed. */
    private Point myStartingPoint;
    

    /**
     * Constructs a RectangleTool associated with the given drawing panel.
     *
     * @param thePanel the drawing panel this tool interacts with
     */
    public RectangleTool(final DrawingPanel thePanel) {
        super("Rectangle", new ImageIcon("files/rectangle_bw.gif"), thePanel);
    }

    /**
     * Sets the starting point of the rectangle and initializes the current shape.
     * 
     * Called when the mouse is pressed.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void press(final MouseEvent theEvent) {
        myStartingPoint = theEvent.getPoint();
        myCurrentShape = new Rectangle2D.Double(myStartingPoint.x, 
                myStartingPoint.y, 0, 0);
    }

    /**
     * Updates the dimensions of the rectangle as the mouse is dragged.
     * 
     * Called during a mouse drag to dynamically adjust the size and 
     * position of the rectangle based on the current mouse position.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void drag(final MouseEvent theEvent) {
        final Rectangle2D rect = (Rectangle2D) myCurrentShape;
        rect.setFrameFromDiagonal(myStartingPoint, theEvent.getPoint());
        myDrawingPanel.repaint();
    }

    /**
     * Finalizes the rectangle and adds it to the drawing panel's shape list.
     * 
     * Called when the mouse is released to save the drawn rectangle 
     * and reset the current shape.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void release(final MouseEvent theEvent) {
        myDrawingPanel.addShape(myCurrentShape);
        myCurrentShape = new Rectangle2D.Double(NULL_NUMBER, NULL_NUMBER, 0, 0);
    }
}
