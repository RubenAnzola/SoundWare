package excepciones.usuario;

public class AnuncioRequeridoExeption extends Exception {

    public AnuncioRequeridoExeption(){}
    public AnuncioRequeridoExeption(String message) {
        super(message);
    }
}
