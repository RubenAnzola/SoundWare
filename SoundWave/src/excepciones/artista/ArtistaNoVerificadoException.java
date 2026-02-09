package excepciones.artista;

public class ArtistaNoVerificadoException extends Exception {

    public ArtistaNoVerificadoException(){}
    public ArtistaNoVerificadoException(String message) {
        super(message);
    }
}
