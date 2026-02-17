package modelo.plataforma;

import enums.CriterioOrden;
import excepciones.playlist.ContenidoDuplicadoException;
import excepciones.playlist.PlaylistLlenaException;
import excepciones.playlist.PlaylistVaciaException;
import modelo.contenido.Contenido;
import modelo.usuarios.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class Playlist {
    private static final int MAX_CONTENIDOS_DEFAULT = 500;

    private String id;
    private String nombre;
    private Usuario creador;
    private ArrayList<Contenido> contenidos;
    private boolean esPublica;
    private int seguidores;
    private String descripcion;
    private String portadaURL;
    private Date fechaCreacion;
    private int maxContenidos;

    // Constructores
    public Playlist(String nombre, Usuario creador){
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.creador = creador;
        this.contenidos = new ArrayList<>();
        this.esPublica = false;
        this.seguidores = 0;
        this.fechaCreacion = new Date();
        this.maxContenidos = MAX_CONTENIDOS_DEFAULT;
    }

    public Playlist(String nombre, Usuario creador, boolean esPublica, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.creador = creador;
        this.contenidos = new ArrayList<>();
        this.esPublica = esPublica;
        this.descripcion = descripcion;
        this.seguidores = 0;
        this.fechaCreacion = new Date();
        this.maxContenidos = MAX_CONTENIDOS_DEFAULT;
    }


    // Métodos
    public void agregarContenido(Contenido contenido) throws PlaylistLlenaException, ContenidoDuplicadoException{
        // Primero checo si ya llegué al límite de contenidos
        if (contenidos.size() >= maxContenidos) {
            throw new PlaylistLlenaException("La playlist ha alcanzado su capacidad máxima.");
        }
        // Luego verifico que no esté duplicado
        if (contenidos.contains(contenido)) {
            throw new ContenidoDuplicadoException("El contenido ya existe en la playlist.");
        }
        // Si pasó todas las validaciones, lo agrego
        contenidos.add(contenido);
    }

    public boolean eliminarContenido(String idContenido){
        // Elimino el contenido que tenga ese ID y devuelvo si se pudo eliminar
        return contenidos.removeIf(contenido -> contenido.getId().equals(idContenido));
    }

    public boolean eliminarContenido(Contenido contenido){
        // Intento eliminar el contenido directo
        return contenidos.remove(contenido);
    }

    public void ordenarPor(CriterioOrden criterio) throws PlaylistVaciaException{
        // No puedo ordenar una playlist vacía
        if(contenidos.isEmpty()){
            throw new PlaylistVaciaException("La playlist está vacía.");
        }

        switch(criterio){
            case POPULARIDAD:
                // Ordeno por reproducciones de mayor a menor (los más populares primero)
                contenidos.sort(Comparator.comparingInt(Contenido::getReproducciones).reversed());
                break;
            case DURACION:
                // Ordeno por duración de menor a mayor (los más cortos primero)
                contenidos.sort(Comparator.comparingInt(Contenido::getDuracionSegundos));
                break;
            case ALFABETICO:
                // Ordeno alfabéticamente por el título (A-Z)
                contenidos.sort(Comparator.comparing(Contenido::getTitulo));
                break;
            case ALEATORIO:
                // Mezclo todo al azar (shuffle)
                Collections.shuffle(contenidos);
                break;
            default:
                break;
        }
    }

    public int getDuracionTotal(){
        // Sumo todas las duraciones de los contenidos
        return contenidos.stream()
                .mapToInt(Contenido::getDuracionSegundos)
                .sum();
    }

    public String getDuracionTotalFormateada(){
        int totalSegundos = getDuracionTotal();
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;

        if(horas > 0){
            return String.format("%d:%02d:%02d", horas, minutos, segundos);
        } else {
            return String.format("%d:%02d", minutos, segundos);
        }
    }

    public void shuffle(){
        Collections.shuffle(contenidos);
    }

    public ArrayList<Contenido> buscarContenido(String termino){
        ArrayList<Contenido> resultados = new ArrayList<>();
        for(Contenido contenido : contenidos){
            if(contenido.getTitulo().toLowerCase().contains(termino.toLowerCase())){
                resultados.add(contenido);
            }
        }
        return resultados;
    }

    public void hacerPublica(){
        this.esPublica = true;
    }

    public void hacerPrivada(){
        this.esPublica = false;
    }

    public void incrementarSeguidores(){
        this.seguidores++;
    }

    public void decrementarSeguidores(){
        if(this.seguidores > 0){
            this.seguidores--;
        }
    }

    public int getNumContenidos(){
        return contenidos.size();
    }

    public boolean estaVacia(){
        return contenidos.isEmpty();
    }

    public Contenido getContenido(int posicion){
        if(posicion >= 0 && posicion < contenidos.size()){
            return contenidos.get(posicion);
        }
        return null;
    }

    // Getters y Setters
    public String getId(){
        return this.id;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public Usuario getCreador(){
        return this.creador;
    }

    public ArrayList<Contenido> getContenidos(){
        return new ArrayList<>(this.contenidos);
    }

    public boolean isEsPublica(){
        return this.esPublica;
    }

    public void setEsPublica(boolean esPublica){
        this.esPublica = esPublica;
    }

    public int getSeguidores(){
        return this.seguidores;
    }

    public void setSeguidores(int seguidores){
        this.seguidores = seguidores;
    }

    public String getDescripcion(){
        return this.descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public String getPortadaURL(){
        return this.portadaURL;
    }

    public void setPortadaURL(String portadaURL){
        this.portadaURL = portadaURL;
    }

    public Date getFechaCreacion(){
        return this.fechaCreacion;
    }

    public int getMaxContenidos(){
        return this.maxContenidos;
    }

    // Overrides
    @Override
    public String toString(){
        return "Playlist{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", creador=" + (creador != null ? creador.getNombre() : "Sin creador") +
                ", contenidos=" + contenidos.size() +
                ", esPublica=" + esPublica +
                ", seguidores=" + seguidores +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Playlist playlist = (Playlist) obj;
        return id.equals(playlist.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
