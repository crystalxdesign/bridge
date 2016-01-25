public class Double extends Call {
    private int value;

    public Double() {
        this.value = Rules.DOUBLE;
    }

    public Double(int value) {
        this.value = value;
    }

    public Double(String doubleStr) {
        if (!Call.isDouble(doubleStr)) {
            throw new MalformedCallException();
        }
    }

    public String toString() {
        if (this.value == Rules.DOUBLE) {
            return "X";
        }
        else if (this.value == Rules.REDOUBLE) {
            return "XX";
        }
        else {
            return "";
        }
    }
}
