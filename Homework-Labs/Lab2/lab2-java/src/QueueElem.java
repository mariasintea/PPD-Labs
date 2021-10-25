public class QueueElem {
    private int line, column, newValue;

    public QueueElem(int line, int column, int newValue) {
        this.line = line;
        this.column = column;
        this.newValue = newValue;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }
}
