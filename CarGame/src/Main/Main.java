package Main;

public class Main {
    private int x;
    private int y;
    private int height;
    private int width;
    private String path;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Main(int x, int y, int height, int width, String path) {
        this.setX(x);
        this.setY(y);
        this.setHeight(height);
        this.setWidth(width);
        this.setPath(path);
    }
}
