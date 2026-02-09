package excepciones.recomendacion;

public class ModeloNoEntrenadoException extends Exception {

    public ModeloNoEntrenadoException(){}
    public ModeloNoEntrenadoException(String message) {
        super(message);
    }
}
