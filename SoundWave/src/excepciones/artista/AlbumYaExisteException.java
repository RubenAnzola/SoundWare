package excepciones.artista;

public class AlbumYaExisteException extends Exception {

    public AlbumYaExisteException(){}
    public AlbumYaExisteException(String message) {
        super(message);
    }
}
