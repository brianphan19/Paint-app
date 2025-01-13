/*
 * TCSS 305 - Assignment 6
 */
package model;


import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import javax.swing.ImageIcon;
import view.DrawingPanel;

/**
 * Tool to draw lines.
 * 
 * This tool allows the user to draw straight lines on the drawing panel
 *  by clicking and dragging the mouse. The line is defined by a starting
 *   point (mouse press) and an ending point (mouse release).
 * 
 * @author Quang
 * @version Nov 22, 2024
 */
public class LineTool extends AbstractPaintTool {
    /** Out of Canvas number for null-ing the object. */
    private static final Point NULL_POINT = new Point(-50, -50);
    /** The starting point for the line, set when the mouse is pressed. */
    private Point myStartingPoint;

    /**
     * Constructs a line tool associated with the given drawing panel.
     *
     * @param thePanel the drawing panel this tool interacts with
     */
    public LineTool(final DrawingPanel thePanel) {
        super("Line", new ImageIcon("files/line_bw.gif"), thePanel);
    }

    /**
     * Sets the starting point of the line and initializes the current shape.
     * Called when the mouse is pressed.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void press(final MouseEvent theEvent) {
        myStartingPoint = theEvent.getPoint();
        myCurrentShape = new Line2D.Double(myStartingPoint, myStartingPoint);
    }

    /**
     * Updates the endpoint of the line as the mouse is dragged.
     * 
     * Called during a mouse drag to dynamically adjust the length and orientation of the line
     * based on the current mouse position.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void drag(final MouseEvent theEvent) {
        ((Line2D) myCurrentShape).setLine(myStartingPoint, theEvent.getPoint());
        myDrawingPanel.repaint(); 
    }

    /**
     * Finalizes the line and adds it to the drawing panel's shape list.
     * 
     * Called when the mouse is released to save the drawn line and reset the current shape.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void release(final MouseEvent theEvent) {
        myDrawingPanel.addShape(myCurrentShape); 
        myCurrentShape = new Line2D.Double(NULL_POINT, NULL_POINT);
    }
}
