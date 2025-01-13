/*
 * TCSS 305 - Assignment 
 */
package view;




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.EllipseTool;
import model.LineTool;
import model.PaintTool;
import model.PencilTool;
import model.RectangleTool;




/**
 * Paint application GUI.
 * This class serves as the main entry point for the paint application,
 * managing the window, menu bar, toolbar, and interactions with the 
 * drawing panel. Users can select tools, adjust stroke thickness, choose
 *  colors, and clear the canvas via the GUI.
 * 
 * @version Nov 22, 2024
 * @author Quang
 */
public final class PaintGUI {

    /** Default size for the application window. */
    private static final  Dimension MY_SCREEN_SIZE = 
            Toolkit.getDefaultToolkit().getScreenSize();
    
    /** Default size for the application window. */
    private static final  Dimension MY_GUI_SIZE = 
            new Dimension(MY_SCREEN_SIZE.width / 3, MY_SCREEN_SIZE.height / 3);

    /** Title of the application window. */
    private static final String MY_FRAME_TITLE = "TCSS 305 Paint";
    
    /** Path to the application icon. */
    private static final String MY_ICON_PATH = "files/w_small.png";

    /** Path to the color wheel icon for the About dialog. */
    private static final String MY_COLOR_WHEEL_ICON_PATH = 
            "files/Gradient_color_wheel.png";
    
    /** initial thickness. */
    private static final int MY_INITIAL_THICKNESS = 5;

    /** Main application frame. */
    private final JFrame myFrame;

    /** Drawing panel for creating shapes. */
    private final DrawingPanel myDrawingPanel;

    /** Slider for adjusting stroke thickness. */
    private final JSlider myThicknessSlider;

    /** Button group for tool selection. */
    private final ButtonGroup myToolGroup;

    /** List of available paint tools. */
    private final List<PaintTool> myTools;

    /**
     * Initializes the GUI.
     */
    public PaintGUI() {
        myFrame = new JFrame(MY_FRAME_TITLE);
        myDrawingPanel = new DrawingPanel();
        myThicknessSlider = createThicknessSlider();
        myToolGroup = new ButtonGroup();
        myTools = new ArrayList<>();

        setupGUI();
    }

    /**
     * Configures and displays the GUI components.
     */
    private void setupGUI() {
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a composite layout panel
        final JPanel compositeLayout = new JPanel(new BorderLayout());
        
        myFrame.setContentPane(compositeLayout);
        myFrame.setSize(MY_GUI_SIZE);
        myFrame.setLocationRelativeTo(null);
        myFrame.setResizable(true);
        myFrame.setVisible(true);
        
        // Add components to the composite layout
        setWindowIcon();
        initializeTools();

        compositeLayout.setLayout(new BorderLayout());
        compositeLayout.add(myDrawingPanel, BorderLayout.CENTER);
        compositeLayout.add(createToolBar(), BorderLayout.SOUTH);

        // Set up the menu bar
        myFrame.setJMenuBar(createMenuBar());
        
        // Set Metal look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException 
                | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates the menu bar for the application.
     * 
     * @return the menu bar
     */
    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu optionsMenu = new JMenu("Options");
        optionsMenu.add(createThicknessMenu());
        optionsMenu.addSeparator();
        optionsMenu.add(createColorMenuItem());
        optionsMenu.addSeparator();
        optionsMenu.add(createBackgroundMenuItem());

        optionsMenu.add(createClearMenuItem());

        menuBar.add(optionsMenu);

        final JMenu toolsMenu = new JMenu("Tools");
        for (final PaintTool tool : myTools) {
            toolsMenu.add(tool.getMenuItem(myToolGroup));
        }
        menuBar.add(toolsMenu);

        final JMenu helpMenu = new JMenu("Help");
        helpMenu.add(createAboutMenuItem());
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * Sets the window icon for the application.
     */
    private void setWindowIcon() {
        try {
            final Image icon = ImageIO.read(new File(MY_ICON_PATH));
            myFrame.setIconImage(icon);
        } catch (final IOException e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }
    }

    /**
     * Creates a toolbar for tool selection.
     * 
     * @return the toolbar
     */
    private JToolBar createToolBar() {
        final JToolBar toolBar = new JToolBar();
        
        toolBar.add(createUndoButton());
        toolBar.addSeparator();
        
        for (final PaintTool tool : myTools) {
            toolBar.add(tool.getToggleButton(myToolGroup));
        }
        return toolBar;
    }

    /**
     * Creates a slider for adjusting stroke thickness.
     * 
     * @return the thickness slider
     */
    private JSlider createThicknessSlider() {
        final JSlider slider = new JSlider(0, 25, 5);
        slider.setMajorTickSpacing(MY_INITIAL_THICKNESS);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e 
                -> myDrawingPanel.setThickness(slider.getValue()));
        return slider;
    }

    /**
     * Creates a menu for adjusting stroke thickness.
     * 
     * @return the thickness menu
     */
    private JMenu createThicknessMenu() {
        final JMenu thicknessMenu = new JMenu("Thickness");
        thicknessMenu.add(myThicknessSlider);
        return thicknessMenu;
    }

    /**
     * Creates a menu item for color selection.
     * 
     * @return the color menu item
     */
    private JMenuItem createColorMenuItem() {
        final JMenuItem colorItem = new JMenuItem("Color...");
        colorItem.addActionListener(e -> chooseColor());
        return colorItem;
    }
    
    private JMenuItem createBackgroundMenuItem() {
        final JMenuItem backgroundItem = new JMenuItem("Background...");
        backgroundItem.addActionListener(e -> {
            final Color newBackgroundColor = JColorChooser.showDialog(
                myFrame, "Choose a background color", myDrawingPanel.getBackground());
            if (newBackgroundColor != null) {
                myDrawingPanel.setBackground(newBackgroundColor);
            }
        });
        return backgroundItem;
    }


    /**
     * Creates a menu item for clearing the drawing panel.
     * 
     * @return the clear menu item
     */
    private JMenuItem createClearMenuItem() {
        final JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.setEnabled(false);
        clearItem.addActionListener(e -> {
            myDrawingPanel.clear();
            clearItem.setEnabled(false);
        });
        /*
         * ChatGPT
         * prompt: "i have a "clear" button in a paint app GUI, how do i make it grey 
         * out when there is no drawing in the drawing panel java"
         */
        myDrawingPanel.addDrawingListener(enabled 
                -> clearItem.setEnabled(enabled));
        return clearItem;
    }
    
    private JButton createUndoButton() {
        final JButton undoButton = new JButton("Undo");
        undoButton.setEnabled(false); // Initially disabled
        undoButton.addActionListener(e -> {
            myDrawingPanel.undo();
        });
        myDrawingPanel.addDrawingListener(enabled -> undoButton.setEnabled(enabled));
        return undoButton;
    }


    /**
     * Creates a menu item for the About dialog.
     * 
     * @return the About menu item
     */
    private JMenuItem createAboutMenuItem() {
        final JMenuItem aboutItem = new JMenuItem("About...");
        aboutItem.addActionListener(e -> showAboutDialog());
        return aboutItem;
    }

    /**
     * Opens a color chooser dialog for selecting a new color.
     */
    private void chooseColor() {
        final Color selectedColor = JColorChooser.showDialog(
                myFrame, "Choose a color", myDrawingPanel.getColor());
        if (selectedColor != null) {
            myDrawingPanel.setColor(selectedColor);
        }
    }

    /**
     * Displays an About dialog with information about the application.
     */
    private void showAboutDialog() {
        try {
            final Image originalImage = ImageIO.read(
                    new File(MY_COLOR_WHEEL_ICON_PATH));
            final Image resizedImage = 
                    originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            final Icon colorWheelIcon = new ImageIcon(resizedImage);

            JOptionPane.showMessageDialog(
                myFrame,
                "Quang Phan\nAutumn 2024\nTCSS 305 Paint",
                "About TCSS 305 Paint",
                JOptionPane.INFORMATION_MESSAGE,
                colorWheelIcon
            );
        } catch (final IOException e) {
            System.err.println("Error loading color wheel icon: " + e.getMessage());
        }
    }

    /**
     * Adds tools to the application and sets the initial tool.
     */    private void initializeTools() {
        final PaintTool lineTool = new LineTool(myDrawingPanel);
        myTools.add(lineTool);
        myTools.add(new RectangleTool(myDrawingPanel));
        myTools.add(new EllipseTool(myDrawingPanel));
        myTools.add(new PencilTool(myDrawingPanel));

        // Set the initial tool
        myDrawingPanel.setCurrentTool(lineTool);

        // Select the LineTool button in the UI
        final JToggleButton lineButton = lineTool.getToggleButton(myToolGroup);
        lineButton.setSelected(true);
    }
}
