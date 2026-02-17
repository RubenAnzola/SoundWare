package modelo.artistas;

import enums.CategoriaPodcast;
import excepciones.artista.LimiteEpisodiosException;
import excepciones.contenido.EpisodioNoEncontradoException;
import modelo.contenido.Podcast;
import utilidades.EstadisticasCreador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Creador {
    private static final int MAX_EPISODIOS = 500;

    private String id;
    private String nombreCanal;
    private String nombre;
    private ArrayList<Podcast> episodios;
    private int suscriptores;
    private String descripcion;
    private HashMap<String, String> redesSociales;
    private ArrayList<CategoriaPodcast> categoriasPrincipales;

    // Constructores
    public Creador(String nombreCanal, String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombreCanal = nombreCanal;
        this.nombre = nombre;
        this.episodios = new ArrayList<>();
        this.suscriptores = 0;
        this.descripcion = "";
        this.redesSociales = new HashMap<>();
        this.categoriasPrincipales = new ArrayList<>();
    }

    public Creador(String nombreCanal, String nombre, String descripcion) {
        this(nombreCanal, nombre);
        this.descripcion = descripcion != null ? descripcion : "";
    }

    // Métodos
    public void publicarPodcast(Podcast episodio) throws LimiteEpisodiosException {
        if (episodios.size() >= MAX_EPISODIOS) {
            throw new LimiteEpisodiosException("Se alcanzó el límite máximo de " + MAX_EPISODIOS + " episodios");
        }
        episodios.add(episodio);
    }

    public EstadisticasCreador obtenerEstadisticas() {
        return new EstadisticasCreador(this);
    }

    public void incrementarSuscriptores() {
        this.suscriptores++;
    }

    public void agregarRedSocial(String plataforma, String url) {
        // Checo que me hayan pasado datos válidos
        if (plataforma != null && url != null) {
            // Guardo la plataforma en minúsculas para que sea más fácil buscar después
            // Por ejemplo: "Twitter", "TWITTER" y "twitter" se guardan como "twitter"
            redesSociales.put(plataforma.toLowerCase(), url);
        }
    }

    public void agregarCategoriaPrincipal(CategoriaPodcast categoria) {
        if (categoria != null && !categoriasPrincipales.contains(categoria)) {
            categoriasPrincipales.add(categoria);
        }
    }

    public int getUltimaTemporada() {
        int maxTemporada = 0;
        for (Podcast episodio : episodios) {
            if (episodio.getTemporada() > maxTemporada) {
                maxTemporada = episodio.getTemporada();
            }
        }
        return maxTemporada;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Podcast> getEpisodios() {
        return new ArrayList<>(episodios); // Copia defensiva
    }

    public int getSuscriptores() {
        return suscriptores;
    }

    public void setSuscriptores(int suscriptores) {
        this.suscriptores = suscriptores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public HashMap<String, String> getRedesSociales() {
        return new HashMap<>(redesSociales); // Copia defensiva
    }

    public ArrayList<CategoriaPodcast> getCategoriasPrincipales() {
        return new ArrayList<>(categoriasPrincipales); // Copia defensiva
    }

    @Override
    public String toString() {
        return "Creador{" +
                "id='" + id + '\'' +
                ", nombreCanal='" + nombreCanal + '\'' +
                ", nombre='" + nombre + '\'' +
                ", episodios=" + episodios.size() +
                ", suscriptores=" + suscriptores +
                '}';
    }

    public int getNumEpisodios() {
        return episodios.size();
    }

    public void eliminarEpisodio(String idEliminar) throws EpisodioNoEncontradoException {
        // Intento eliminar el episodio que tenga ese ID
        boolean eliminado = episodios.removeIf(episodio -> episodio.getId().equals(idEliminar));
        // Si no se pudo eliminar es porque no existía
        if (!eliminado) {
            throw new EpisodioNoEncontradoException("No se encontró el episodio con ID: " + idEliminar);
        }
    }

    public double calcularPromedioReproducciones() {
        if (episodios.isEmpty()) {
            return 0.0;
        }
        int total = getTotalReproducciones();
        return (double) total / episodios.size();
    }

    public int getTotalReproducciones() {
        int total = 0;
        for (Podcast episodio : episodios) {
            total += episodio.getReproducciones();
        }
        return total;
    }

    public ArrayList<Podcast> obtenerTopEpisodios(int cantidad) {
        ArrayList<Podcast> topEpisodios = new ArrayList<>(episodios);
        topEpisodios.sort((e1, e2) -> Integer.compare(e2.getReproducciones(), e1.getReproducciones()));

        if (cantidad < topEpisodios.size()) {
            return new ArrayList<>(topEpisodios.subList(0, cantidad));
        }
        return topEpisodios;
    }
}

