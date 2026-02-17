package modelo.usuarios;

import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Anuncio;

import java.util.Date;

public class UsuarioGratuito extends Usuario {

    // Constantes
    private static final int LIMITE_DIARIO = 50;
    private static final int CANCIONES_ENTRE_ANUNCIOS = 3;

    // Atributos
    private int anunciosEscuchados;
    private Date ultimoAnuncio;
    private int reproduccionesHoy;
    private int limiteReproducciones;
    private int cancionesSinAnuncio;
    private Date fechaUltimaReproduccion;

    // Constructor
    public UsuarioGratuito(String nombre, String email, String password)
            throws EmailInvalidoException, PasswordDebilException {
        super(nombre, email, password, TipoSuscripcion.GRATUITO);
        this.anunciosEscuchados = 0;
        this.reproduccionesHoy = 0;
        this.limiteReproducciones = LIMITE_DIARIO;
        this.cancionesSinAnuncio = 0;
        this.fechaUltimaReproduccion = new Date(System.currentTimeMillis());
    }

    @Override
    public void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException {
        // Primero me aseguro que me pasaron un contenido válido
        if(contenido == null){
            throw new ContenidoNoDisponibleException("El contenido no está disponible");
        }

        // Checo que el contenido esté disponible para reproducirse
        if(!contenido.isDisponible()){
            throw new ContenidoNoDisponibleException("El contenido '" + contenido.getTitulo() + "' no está disponible actualmente");
        }

        // Si cambió el día, reinicio mis contadores para el nuevo día
        if(nuevoDia()){
            reiniciarContadorDiario();
        }

        // Reviso si ya alcancé mi límite diario (50 reproducciones)
        if (!puedeReproducir()) {
            throw new LimiteDiarioAlcanzadoException("Has alcanzado el límite diario de reproducciones gratuitas. Considera suscribirte a Premium para disfrutar sin límites.");
        }

        // Si ya escuché 3 canciones sin anuncio, me toca ver uno
        if (debeVerAnuncio()) {
            throw new AnuncioRequeridoException("Es hora de escuchar un anuncio. Por favor, disfruta de un anuncio para continuar escuchando tu música.");
        }

        // Si pasé todas las validaciones, puedo reproducir el contenido
        super.agregarAlHistorial(contenido);
        contenido.aumentarReproducciones();

        // Actualizo mis contadores después de reproducir
        this.reproduccionesHoy++; // Una reproducción más hoy
        this.cancionesSinAnuncio++; // Una canción más sin ver anuncio
        this.fechaUltimaReproduccion = new Date(System.currentTimeMillis()); // Guardo cuándo reproduje
    }

    private boolean nuevoDia() {
        // Checo si es un día diferente desde la última vez que reproduje algo
        if (this.fechaUltimaReproduccion == null) {
            return true; // Si nunca he reproducido, es nuevo día
        }
        Date hoy = new Date(System.currentTimeMillis());
        // Comparar solo la fecha (ignorar hora)
        return !hoy.toString().equals(this.fechaUltimaReproduccion.toString());
    }

    public void verAnuncio() {
        // Incrementa el contador de anuncios
        this.anunciosEscuchados++;
        this.ultimoAnuncio = new Date(System.currentTimeMillis());
        this.cancionesSinAnuncio = 0; // Reinicia el contador de canciones
    }

    public void verAnuncio(Anuncio anuncio) {
        // Si el anuncio es null, usa el genérico
        if (anuncio == null) {
            verAnuncio();
        } else {
            // Reproduce un anuncio específico
            this.anunciosEscuchados++;
            this.ultimoAnuncio = new Date(System.currentTimeMillis());
            this.cancionesSinAnuncio = 0; // Reinicia el contador de canciones
        }
    }

    public boolean puedeReproducir() {
        return this.reproduccionesHoy < LIMITE_DIARIO;
    }

    public boolean debeVerAnuncio() {
        return this.cancionesSinAnuncio >= CANCIONES_ENTRE_ANUNCIOS;
    }

    public void reiniciarContadorDiario() {
        this.reproduccionesHoy = 0;
        this.cancionesSinAnuncio = 0;
    }

    public int getReproduccionesRestantes() {
        return LIMITE_DIARIO - this.reproduccionesHoy;
    }

    public int getCancionesHastaAnuncio() {
        return CANCIONES_ENTRE_ANUNCIOS - this.cancionesSinAnuncio;
    }

    // Getters y Setters
    public int getAnunciosEscuchados() {
        return this.anunciosEscuchados;
    }

    public Date getUltimoAnuncio() {
        return this.ultimoAnuncio;
    }

    public int getReproduccionesHoy() {
        return this.reproduccionesHoy;
    }

    public void setReproduccionesHoy(int reproduccionesHoy) {
        this.reproduccionesHoy = reproduccionesHoy;
    }

    public int getLimiteReproducciones() {
        return this.limiteReproducciones;
    }

    public int getCancionesSinAnuncio() {
        return this.cancionesSinAnuncio;
    }

    public void setCancionesSinAnuncio(int cancionesSinAnuncio) {
        this.cancionesSinAnuncio = cancionesSinAnuncio;
    }

    // Override toString
    @Override
    public String toString() {
        return "UsuarioGratuito{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", suscripcion=" + getSuscripcion() +
                ", reproduccionesHoy=" + reproduccionesHoy +
                ", limiteReproducciones=" + limiteReproducciones +
                ", anunciosEscuchados=" + anunciosEscuchados +
                ", cancionesSinAnuncio=" + cancionesSinAnuncio +
                '}';
    }
}
