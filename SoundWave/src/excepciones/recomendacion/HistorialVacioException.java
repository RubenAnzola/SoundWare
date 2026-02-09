package excepciones.recomendacion;

public class HistorialVacioException extends RuntimeException {

    public HistorialVacioException(){}
    public HistorialVacioException(String message) {
        super(message);
    }
}
