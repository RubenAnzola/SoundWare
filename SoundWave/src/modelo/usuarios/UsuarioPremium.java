package modelo.usuarios;

import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import excepciones.usuario.AnuncioRequeridoExeption;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;

import java.util.ArrayList;

public class UsuarioPremium extends Usuario {

    //Constante
    private static final int MAX_DESCARGAS_DEFAULT = 100;

    //Atributos propios
    private boolean descargasOffline;
    private int maxDescargas;
    private ArrayList<Contenido> descargados;
    private String calidadAudio;

    //Constructores
    public UsuarioPremium(String nombre, String email, String password)
            throws EmailInvalidoException, PasswordDebilException {
        super(nombre, email, password, TipoSuscripcion.PREMIUM);
        this.descargasOffline = true;
        this.maxDescargas = MAX_DESCARGAS_DEFAULT;
        this.descargados = new ArrayList<>();
        this.calidadAudio = "Alta";
    }

    public UsuarioPremium(String nombre, String email, String password, TipoSuscripcion suscripcion)
            throws EmailInvalidoException, PasswordDebilException {
        super(nombre, email, password, suscripcion);
        this.descargasOffline = true;
        this.maxDescargas = MAX_DESCARGAS_DEFAULT;
        this.descargados = new ArrayList<>();
        this.calidadAudio = "Alta";
    }

    //Implementación del método abstracto
    @Override
    public void reproducir(Contenido contenido)
            throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoExeption {
        if(contenido == null){
            throw new ContenidoNoDisponibleException("El contenido no está disponible");
        }
        // Reproducir sin anuncios ni límite diario (ventaja Premium)
        agregarAlHistorial(contenido);
        contenido.aumentarReproducciones();
    }

    //Métodos propios
    public void descargar(Contenido contenido)
            throws LimiteDescargasException, ContenidoYaDescargadoException {
        // Verificar si ya está descargado
        if (this.descargados.contains(contenido)) {
            throw new ContenidoYaDescargadoException("El contenido ya está descargado");
        }

        // Verificar si hay espacio disponible
        if (this.descargados.size() >= this.maxDescargas) {
            throw new LimiteDescargasException("Se alcanzó el límite de descargas (" + this.maxDescargas + ")");
        }

        // Registrar la descarga
        this.descargados.add(contenido);
    }

    public boolean eliminarDescarga(Contenido contenido) {
        // Elimina una descarga existente y retorna true si existía
        return this.descargados.remove(contenido);
    }

    public boolean verificarEspacioDescarga() {
        // Indica si quedan huecos para más descargas
        return this.descargados.size() < this.maxDescargas;
    }

    public int getDescargasRestantes() {
        // Cuántas descargas quedan disponibles
        return this.maxDescargas - this.descargados.size();
    }

    public void cambiarCalidadAudio(String calidad) {
        // Cambia calidad si es válida
        if (calidad != null && !calidad.trim().isEmpty()) {
            this.calidadAudio = calidad;
        }
    }

    public void limpiarDescargas() {
        // Vacía la lista de descargas
        this.descargados.clear();
    }

    //Getters y Setters
    public boolean isDescargasOffline() {
        return this.descargasOffline;
    }

    public void setDescargasOffline(boolean descargasOffline) {
        this.descargasOffline = descargasOffline;
    }

    public int getMaxDescargas() {
        return this.maxDescargas;
    }

    public ArrayList<Contenido> getDescargados() {
        return new ArrayList<>(this.descargados);
    }

    public int getNumDescargados() {
        // Retorna la cantidad de contenidos descargados
        return this.descargados.size();
    }

    public String getCalidadAudio() {
        return this.calidadAudio;
    }

    public void setCalidadAudio(String calidadAudio) {
        this.calidadAudio = calidadAudio;
    }

    //Override toString
    @Override
    public String toString() {
        return "UsuarioPremium{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", suscripcion=" + getSuscripcion() +
                ", descargasOffline=" + descargasOffline +
                ", maxDescargas=" + maxDescargas +
                ", numDescargados=" + descargados.size() +
                ", calidadAudio='" + calidadAudio + '\'' +
                '}';
    }
}
