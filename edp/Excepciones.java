package edp;

public class Excepciones{

}

class TypeError extends RuntimeException {
    public TypeError(String message) {
        super(message);
    }
}

class ValueError extends RuntimeException {
    public ValueError(String message) {
        super(message);
    }
}

class KeyError extends RuntimeException {
    public KeyError(String message) {
        super(message);
    }
}

class IndexError extends RuntimeException {
    public IndexError(String message) {
        super(message);
    }
}