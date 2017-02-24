package comp124graphics;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Creates a window frame and canvas panel that is used to draw graphical objects.
 * Created by bjackson on 9/13/2016.
 * @version 0.5
 */
public class CanvasWindow extends JPanel implements GraphicsObserver{

    /**
     * Window frame
     */
    protected JFrame windowFrame;

    /**
     * Holds the objects to be drawn in calls to paintComponent
     */
    private ConcurrentLinkedDeque<GraphicsObject> gObjects;

    //TODO overload this to take just the title. Create getters/setters for width and height
    public CanvasWindow(String title, int windowWidth, int windowHeight){
        setPreferredSize (new Dimension(windowWidth, windowHeight));
        setBackground (Color.white);

        gObjects = new ConcurrentLinkedDeque<GraphicsObject>();

        windowFrame = new JFrame (title);
        windowFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        windowFrame.getContentPane().add(this);
        windowFrame.pack();
        windowFrame.setVisible(true);
    }

    /**
     * Called automatically by java to draw graphical objects on the page
     * @param page
     */
    public void paintComponent (Graphics page) {
        super.paintComponent (page);

        Graphics2D gc = (Graphics2D)page;

        enableAntialiasing(gc);

        // Iterate over all of the graphical objects and draw them.
        Iterator<GraphicsObject> it = gObjects.iterator();
        while (it.hasNext()){
            it.next().draw(gc);
        }
    }

    /**
     * Adds the graphical object to the list of objects drawn on the canvas
     * @param gObject
     */
    public void add(GraphicsObject gObject){
        gObject.addObserver(this);
        gObjects.add(gObject);
        repaint();
    }

    /**
     * Removes the object from being drawn
     * @param gObject
     */
    public void remove(GraphicsObject gObject){
        //TODO: should throw an exception if gObject doesn't exist in this canvaswindow
        gObject.removeObserver(this);
        gObjects.remove(gObject);
        repaint();
    }

    /**
     * Removes all of the objects currently drawn on the canvas
     */
    public void removeAll(){
        Iterator<GraphicsObject> it = gObjects.iterator();
        while(it.hasNext()){
            GraphicsObject obj = it.next();
            obj.removeObserver(this);
            it.remove();
        }
        repaint();
    }

    /**
     * Pauses the program for milliseconds
     * @param milliseconds
     */
    public void pause(long milliseconds){
        try{
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // Empty
        }
    }

    /**
     * Pauses the program for milliseconds
     * @param milliseconds
     */
    public void pause(double milliseconds){
        try {
            int millis = (int) milliseconds;
            int nanos = (int) Math.round((milliseconds - millis) * 1000000);
            Thread.sleep(millis, nanos);
        } catch (InterruptedException ex) {
			/* Empty */
        }
    }

    /**
     * Returns the topmost graphical object underneath position x, y. If no such object exists it returns null.
     * @param x position
     * @param y position
     * @return object at (x,y) or null if it does not exist.
     */
    public GraphicsObject getElementAt(double x, double y){
        Graphics2D gc = (Graphics2D)this.getGraphics();
        Iterator<GraphicsObject> it = gObjects.descendingIterator();
        while(it.hasNext()){
            GraphicsObject obj = it.next();
            if (obj.testHit(x, y, gc)){
                return obj;
            }
        }
        return null;
    }

    /**
     * Enables antialiasing on the drawn shapes.
     * @param gc
     */
    private void enableAntialiasing(Graphics2D gc) {
        gc.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gc.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        gc.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
    }

    /**
     * Implementation of GraphicsObserver method. Notifies Java to repaint the image if any of the objects drawn on the canvas
     * have changed.
     * @param changedObject
     */
    public void graphicChanged(GraphicsObject changedObject){
        repaint();
    }

}


