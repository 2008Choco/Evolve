package wtf.choco.evolve.mod;

public class InvalidModException extends RuntimeException {

    private static final long serialVersionUID = -2753501030988330541L;

    public InvalidModException(Throwable cause) {
        super(cause);
    }

    public InvalidModException(String message) {
        super(message);
    }

}
