package modelo.usuarios;

import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;
import modelo.contenido.Contenido;
import modelo.plataforma.Playlist;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public abstract class Usuario {

    //Atributos:
    protected String id;
    protected String nombre;
    protected String email;
    protected String password;
    protected TipoSuscripcion suscripcion;
    protected ArrayList<Playlist> misPlaylist;
    protected ArrayList<Contenido> historial;
    protected Date fechaRegistro;
    protected ArrayList<Playlist> playlistsSeguidas;
    protected ArrayList<Contenido> contenidosLiked;

    //Contructores
    Usuario(String nombre, String email, String password, TipoSuscripcion suscripcion)
            throws EmailInvalidoException, PasswordDebilException {
        // Primero validamos el email para asegurarnos que tiene el formato correcto
        if (email == null || email.trim().isEmpty()) {
            throw new EmailInvalidoException("El email no puede estar vacío");
        }
        // Checamos que tenga el arroba
        if (!email.contains("@")) {
            throw new EmailInvalidoException("Falta @");
        }
        // El arroba no puede estar al inicio ni al final
        if (email.endsWith("@") || email.startsWith("@")) {
            throw new EmailInvalidoException("Formato de email inválido");
        }

        // Ahora validamos que la contraseña sea segura (mínimo 8 caracteres)
        if (password == null || password.length() < 8) {
            throw new PasswordDebilException("La contraseña debe tener al menos 8 caracteres");
        }

        // Si todo está bien, inicializo los datos del usuario
        this.id = UUID.randomUUID().toString(); // Genero un ID único
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.suscripcion = suscripcion;

        // Creo las listas vacías para las colecciones del usuario
        this.misPlaylist = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.playlistsSeguidas = new ArrayList<>();
        this.contenidosLiked = new ArrayList<>();
        this.fechaRegistro = new Date(); // Guardo cuando se registró
    }

    // Método abstracto que cada tipo de usuario implementa a su manera
    public abstract void reproducir(Contenido contenido)
            throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException;

    public Playlist crearPlaylist(String nombrePlaylist) {
        // Creo una nueva playlist con el nombre que me pasan y este usuario como creador
        Playlist nuevaPlaylist = new Playlist(nombrePlaylist, this);
        // La agrego a mi lista de playlists
        this.misPlaylist.add(nuevaPlaylist);
        // Devuelvo la playlist para que se pueda usar
        return nuevaPlaylist;
    }

    public void seguirPlaylist(Playlist playlist){
        // Solo puedo seguir playlists públicas y que no esté siguiendo ya
        if (playlist.isEsPublica() && !this.playlistsSeguidas.contains(playlist)) {
            // La agrego a mis playlists seguidas
            this.playlistsSeguidas.add(playlist);
            // Sumo un seguidor a la playlist
            playlist.incrementarSeguidores();
        }
    }

    public void dejarDeSeguirPlaylist(Playlist playlist) {
        // Intento quitar la playlist de mi lista de seguidas
        if(this.playlistsSeguidas.remove(playlist)){
            // Si se pudo quitar, resto un seguidor a la playlist
            playlist.decrementarSeguidores();
        }
    }

    public void darLike(Contenido contenido) {
        // Solo agrego el like si no lo tengo ya (evito duplicados)
        if (!this.contenidosLiked.contains(contenido)) {
            this.contenidosLiked.add(contenido);
        }
    }

    public void quitarLike(Contenido contenido){
        // Simplemente quito el contenido de mis likes
        this.contenidosLiked.remove(contenido);
    }

    boolean validarEmail() throws EmailInvalidoException {
        // Checo que el email tenga arroba
        if (!this.email.contains("@")) {
            throw new EmailInvalidoException("Email invalido: debe contener @");
        }
        return true;
    }

    boolean validarPassword() throws PasswordDebilException {
        // La contraseña tiene que tener mínimo 8 caracteres
        if (this.password.length() < 8) {
            throw new PasswordDebilException("La contraseña debe tener al menos 8 caracteres");
        }
        return true;
    }

    public void agregarAlHistorial(Contenido contenido){
        // Primero reviso si ya está en el historial para no duplicar
        if(!this.historial.contains(contenido)){
            // Si ya tengo 100 elementos (el límite), quito el más viejo
            if(this.historial.size() >= 100){
                this.historial.remove(0); // El primero es el más antiguo
            }
            // Ahora sí agrego el nuevo contenido al final
            this.historial.add(contenido);
        }
    }

    public void limpiarHistorial(){
        // Borro todo el historial de una vez
        this.historial.clear();
    }

    public boolean esPremium(){
        // Compruebo si mi suscripción es Premium
        return this.suscripcion == TipoSuscripcion.PREMIUM;
    }

    //Getters y Setters
    public String getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public  String getEmail() {
    return this.email;
}

    public String getPassword(){ return this.password;}

    public TipoSuscripcion getSuscripcion(){
        return this.suscripcion;
    }

    public ArrayList<Playlist> getMisPlaylists(){
        return new ArrayList<>(this.misPlaylist);
    }

    public ArrayList<Contenido> getHistorial(){
        return new ArrayList<>(this.historial);
    }

    public Date getFechaRegistro(){
        return this.fechaRegistro;
    }

    public ArrayList<Playlist> getPlaylistsSeguidas(){
        return new ArrayList<>(this.playlistsSeguidas);
    }

    public ArrayList<Contenido> getContenidosLiked(){
        return new ArrayList<>(this.contenidosLiked);
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setEmail(String email) throws EmailInvalidoException {
        if (!email.contains("@")) {
            throw new EmailInvalidoException("Email invalido: debe contener @");
        }
        this.email = email;
    }

    public void setPassword(String password) throws PasswordDebilException {
        if (password.length() < 8) {
            throw new PasswordDebilException("La contraseña debe tener al menos 8 caracteres");
        }
        this.password = password;
    }

    public void setSuscripcion(TipoSuscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    //metodos toString, equals y hashCode
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", suscripcion=" + suscripcion +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}