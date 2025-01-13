/*
 * TCSS 305 - Assignment 6
 */
package model;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;
import view.DrawingPanel;

/**
 * Tool to draw freeform lines (pencil tool).
 * 
 * This tool allows the user to draw freeform lines by following the 
 * mouse movement as it is dragged. A {@link Path2D} object is used 
 * to represent the freeform shape.
 * 
 * @author Quang
 * @version Nov 22, 2024
 */
public class PencilTool extends AbstractPaintTool {
    /** Out of Canvas number for null-ing the object. */
    private static final Point NULL_POINT = new Point(-50, -50);

    /**
     * Constructs a PencilTool associated with the given drawing panel.
     *
     * @param thePanel the drawing panel this tool interacts with
     */
    public PencilTool(final DrawingPanel thePanel) {
        super("Pencil", new ImageIcon("files/pencil_bw.gif"), thePanel);
    }

    /**
     * Starts a new freeform line at the mouse press location.
     * 
     * Initializes a {@link Path2D} object to track the freeform line.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void press(final MouseEvent theEvent) {
        myCurrentShape = new Path2D.Double();
        ((Path2D) myCurrentShape).moveTo(theEvent.getX(), theEvent.getY());
    }

    /**
     * Updates the freeform line as the mouse is dragged.
     * 
     * Adds a line segment to the {@link Path2D} object to reflect the current mouse position.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void drag(final MouseEvent theEvent) {
        ((Path2D) myCurrentShape).lineTo(theEvent.getX(), theEvent.getY());
        myDrawingPanel.repaint();
    }

    /**
     * Finalizes the freeform line and adds it to the drawing panel.
     * 
     * Saves the completed {@link Path2D} object and resets the current shape.
     *
     * @param theEvent the mouse event triggering this action
     */
    @Override
    public void release(final MouseEvent theEvent) {
        myDrawingPanel.addShape(myCurrentShape);
        myCurrentShape = new Path2D.Double();
        ((Path2D) myCurrentShape).moveTo(NULL_POINT.x, NULL_POINT.y);
    }
}
