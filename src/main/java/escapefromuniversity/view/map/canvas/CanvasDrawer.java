package escapefromuniversity.view.map.canvas;

import escapefromuniversity.model.map.Rectangle;

/**
 * An interface which draws a canvas.
 */
public interface CanvasDrawer {

    public void clear();

    public double getWidth();

    public double getHeight();

    public double getScreenRatio();

    /**
     * Draws a tile.
     *
     * @param filename the filename
     * @param imagePos the position in the file of the rectangle to draw
     * @param drawPos  the position in the map of the rectangle to draw
     */
    void drawImage(String filename, Rectangle imagePos, Rectangle drawPos);
}