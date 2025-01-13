/*
 * TCSS 305 - Assignment 6
 */
package view;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import model.PaintTool;

/**
 * A panel that serves as the canvas for the paint application.
 *
 * This panel listens for mouse events and enables the user to draw 
 * shapes using the currently selected {@link PaintTool}. It supports
 * color selection, stroke thickness adjustment, and clearing of 
 * drawn shapes.
 * 
 * @author Quang
 * @version Nov 22, 2024
 */
public class DrawingPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Initial color for drawing. */
    private static final Color MY_INITIAL_COLOR = new Color(51, 0, 111);
    
    /** Drawing panel width. */
    private static final int MY_PANEL_WIDTH = 800;
    
    /** Drawing panel height. */
    private static final int MY_PANEL_HEIGHT = 600;
    
    /** initial thickness. */
    private static final int MY_INITIAL_THICKNESS = 5;

    /** List of shapes drawn on the panel. */
    private final List<ColoredShape> myShapes;

    /** The currently selected drawing tool. */
    private PaintTool myCurrentTool;

    /** The current color used for drawing. */
    private Color myCurrentColor;

    /** The current stroke thickness. */
    private int myThickness;

    /** Listeners to notify when drawing state changes. */
    private final List<DrawingListener> myListeners;
    

    /**
     * Constructs a new drawing panel with default settings.
     */
    public DrawingPanel() {
        super();
        myShapes = new ArrayList<>();
        myListeners = new ArrayList<>();
        myCurrentColor = MY_INITIAL_COLOR;
        myThickness = MY_INITIAL_THICKNESS;
        
        initializeBackgroundColor();
        initializeWindow();
        initializeCursor();

        setupMouseListeners();
    }
    
    /**
     * Initialize cursor. 
     * 
     */
    private void initializeCursor() {
        super.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }
    
    /**
     * Initialize window. 
     * 
     */
    private void initializeWindow() {
        super.setPreferredSize(new Dimension(MY_PANEL_WIDTH, MY_PANEL_HEIGHT));
    }
    
    /**
     * Initialize background color. 
     * 
     */
    private void initializeBackgroundColor() {
        super.setBackground(Color.WHITE);
    }    
        

    /**
     * Sets the current drawing tool.
     * 
     * @param theTool the selected tool
     */
    public void setCurrentTool(final PaintTool theTool) {
        myCurrentTool = theTool;
    }

    /**
     * Sets the current drawing color.
     * 
     * @param theColor the new drawing color
     */
    public void setColor(final Color theColor) {
        myCurrentColor = theColor;
    }

    /**
     * Gets the current drawing color.
     * 
     * @return the current color
     */
    public Color getColor() {
        return myCurrentColor;
    }

    /**
     * Sets the current stroke thickness.
     * 
     * @param theThickness the new thickness
     */
    public void setThickness(final int theThickness) {
        myThickness = Math.max(1, theThickness); // Ensure minimum thickness of 1
    }

    /**
     * Adds a shape to the list of drawn shapes.
     * 
     * @param theShape the completed shape
     */
    public void addShape(final Shape theShape) {
        if (theShape != null) {
            myShapes.add(new ColoredShape(theShape, myCurrentColor, 
                    myThickness));
            notifyDrawingListeners(true); // Enable "Clear" button
        }
    }

    /**
     * Clears all shapes from the panel.
     */
    public void clear() {
        myShapes.clear();
        repaint();
        notifyDrawingListeners(false); // Disable "Clear" button
    }
    
    public void undo() {
        if (!myShapes.isEmpty()) {
            myShapes.remove(myShapes.size() - 1);
            repaint();
            notifyDrawingListeners(!myShapes.isEmpty());
        }
    }


    /**
     * Adds a listener to observe changes in the drawing state. Listeners
     * will be notified when the drawing state changes, such as when shapes 
     * are added or cleared. This can be used to update UI elements like buttons.
     * 
     * @param theListener the listener to add
     */
    public void addDrawingListener(final DrawingListener theListener) {
        myListeners.add(theListener);
    }

    /**
     * Paints the shapes onto the panel.
     * 
     * @param theGraphic the graphics context
     */
    @Override
    protected void paintComponent(final Graphics theGraphic) {
        super.paintComponent(theGraphic);
        final Graphics2D g2d = (Graphics2D) theGraphic;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw all saved shapes
        for (final ColoredShape cs : myShapes) {
            g2d.setColor(cs.getColor());
            g2d.setStroke(new BasicStroke(cs.getThickness()));
            g2d.draw(cs.getShape());
        }

        // Let the current tool draw its "in-progress" shape
        if (myCurrentTool != null) {
            g2d.setColor(myCurrentColor);
            g2d.setStroke(new BasicStroke(myThickness));
            myCurrentTool.draw(g2d);
        }
    }

    /**
     * Adds mouse listeners to handle drawing events.
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent theEvent) {
                if (myCurrentTool != null) {
                    myCurrentTool.press(theEvent);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent theEvent) {
                if (myCurrentTool != null) {
                    myCurrentTool.release(theEvent);
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(final MouseEvent theEvent) {
                if (myCurrentTool != null) {
                    myCurrentTool.drag(theEvent);
                }
                repaint();
            }
        });
    }

    /**
     * Notifies listeners when the drawing state changes.
     * 
     * @param theEnable true if there are shapes to clear
     */
    private void notifyDrawingListeners(final boolean theEnable) {
        for (final DrawingListener listener : myListeners) {
            listener.drawingChanged(theEnable);
        }
    }

    /**
     * Encapsulates a shape with its color and thickness.
     */
    private static class ColoredShape {
        /** the current drawing shape. */
        private final Shape myShape;
        /** the current drawing color. */
        private final Color myColor;
        /** the current drawing thickness. */
        private final int myThickness;

        ColoredShape(final Shape theShape, final Color theColor, final int theThickness) {
            myShape = theShape;
            myColor = theColor;
            myThickness = theThickness;
        }

        public Shape getShape() {
            return myShape;
        }

        public Color getColor() {
            return myColor;
        }

        public int getThickness() {
            return myThickness;
        }
    }

    /**
     * Interface for listening to drawing changes.
     */
    public interface DrawingListener {
        /**
         * Called when the drawing state changes.
         * 
         * @param enabled true if the clear button should be enabled
         */
        void drawingChanged(boolean theEnabled);
    }
    
    
}
