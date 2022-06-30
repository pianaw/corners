package ai;

class Point {

    protected byte currentX; // column
    protected byte currentY; //row

    protected Point targetPoint;

    public Point(byte x, byte y) {
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "currentX=" + currentX +
                ", currentY=" + currentY +
                ", targetPoint=" + targetPoint +
                '}';
    }
}
