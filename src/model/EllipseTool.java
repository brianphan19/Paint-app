/*
 * TCSS 305 - Assignment 6
 */
package model;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import javax.swing.ImageIcon;
import view.DrawingPanel;

/**
 * Tool to draw ellipses.
 * <p>
 * This tool allows the user to draw ellipses on the drawing panel
 * by clicking and dragging the mouse. The ellipse is created using 
 * two diagonal points determined by the mouse press and drag events.
 * </p>
 * 
 * @author Quang
 * @version Nov 22, 2024
 */
public class EllipseTool extends AbstractPaintTool {
    /** Out of Canvas number for null-ing the object. */
    private static final Point NULL_POINT = new Point(-50, -50);
    /** The starting point for the ellipse, set when the mouse is pressed. */
    private Point myStartingPoint;
    

    /**
     * Constructs an ellipse tool associated with the given drawing panel.
     *
     * @param thePanel the drawing panel this tool interacts with
     */
    public EllipseTool(final DrawingPanel thePanel) {
        super("Ellipse", new ImageIcon("files/ellipse_bw.gif"), thePanel);
    }

    /**
     * Sets the starting point of the ellipse and initializes the current shape.
     * Called when the mouse is pressed.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void press(final MouseEvent theEvent) {
        myStartingPoint = theEvent.getPoint();
        myCurrentShape = new Ellipse2D.Double(myStartingPoint.x, myStartingPoint.y, 0, 0);
    }

    /**
     * Updates the dimensions of the ellipse as the mouse is dragged.
     * Called during a mouse drag to dynamically adjust the shape of the ellipse
     * based on the current mouse position.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void drag(final MouseEvent theEvent) {
        final Ellipse2D ellipse = (Ellipse2D) myCurrentShape;
        ellipse.setFrameFromDiagonal(myStartingPoint, theEvent.getPoint());
        myDrawingPanel.repaint();
    }

    /**
     * Finalizes the ellipse and adds it to the drawing panel's shape list.
     * 
     * Called when the mouse is released to save the drawn ellipse and reset the current shape.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void release(final MouseEvent theEvent) {
        myDrawingPanel.addShape(myCurrentShape);
        myCurrentShape = new Ellipse2D.Double(NULL_POINT.x, NULL_POINT.y, 0, 0);
    }
}
