package modelo.usuarios;

import enums.TipoSuscripcion;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.usuario.AnuncioRequeridoExeption;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.LimiteDiarioAlcanzadoException;
import excepciones.usuario.PasswordDebilException;

import java.sql.Date;
import java.util.ArrayList;

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
        if (!email.contains("@")) {
            throw new EmailInvalidoException("Falta @");
        }
        if (password.length() < 8) {
            throw new PasswordDebilException("La contraseña debe ser mayor a 8 caracteres");
        }
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.suscripcion = suscripcion;
    }

    public abstract void reproducir(Contenido contenido)
            throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoExeption;

    private Playlist crearPlaylist(String nombrePlaylist) {
        //crear la playList
        Playlist nuevaPlaylist = new Playlist(nombrePlaylist);
        //guardar la nueva playlist en mis playlist
        this.misPlaylist.add(nuevaPlaylist);
        //retorno la nueva playlist
        return nuevaPlaylist;
    }

    public void seguirPlaylist(Playlist playlist){
        //verifico si la playList es publica
        if (playlist.isPublica()) {
        //si es publica la agrego
            this.playlistsSeguidas.add(playlist);
        }
    }

    public void dejarDeSeguirPlaylist(Playlist playlist) {
        //accion para dejar de seguir una playlist
        this.playlistsSeguidas.remove(playlist);
    }

    public void darLike(Contenido contenido) {
        if (!this.contenidosLiked.contains(contenido)) {
            this.contenidosLiked.add(contenido);
        }
    }

    public void quitarLike(Contenido contenido){
        this.contenidosLiked.remove(contenido);
    }

    boolean validarEmail() throws EmailInvalidoException {
        //Valido que el email contenga @
        if (!this.email.contains("@")) {
            throw new EmailInvalidoException("Email invalido: debe contener @");
        }
        return true;
    }

    boolean validarPassword() throws PasswordDebilException {
        //Valido que la contraseña tenga al menos 8 caracteres
        if (this.password.length() < 8) {
            throw new PasswordDebilException("La contraseña debe tener al menos 8 caracteres");
        }
        return true;
    }

    public void agregarAlHistorial(Contenido contenido){
        //Valido si el contenido ya existe en el historial
        if(!this.historial.contains(contenido)){
            //Si el historial está lleno (límite de 100), elimino el más antiguo
            if(this.historial.size() >= 100){
                this.historial.remove(0); //Elimino el primer elemento (más antiguo)
            }
            //Agrego el nuevo contenido al historial
            this.historial.add(contenido);
        }
    }

    public void limpiarHistorial(){
        this.historial.clear();
    }

    public boolean esPremium(){
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