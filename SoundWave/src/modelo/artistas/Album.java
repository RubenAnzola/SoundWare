package modelo.artistas;

import enums.GeneroMusical;
import excepciones.artista.AlbumCompletoException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.playlist.CancionNoEncontradaException;
import modelo.contenido.Cancion;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Album {
    private static final int MAX_CANCIONES = 20;

    private String id;
    private String titulo;
    private Artista artista;
    private Date fechaLanzamiento;
    private ArrayList<Cancion> canciones;
    private String portadaURL;
    private String discografica;
    private String tipoAlbum;

    // Constructores
    public Album(String titulo, Artista artista, Date fechaLanzamiento) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.artista = artista;
        this.fechaLanzamiento = fechaLanzamiento;
        this.canciones = new ArrayList<>();
        this.portadaURL = "";
        this.discografica = "";
        this.tipoAlbum = "Estudio";
    }

    public Album(String titulo, Artista artista, Date fechaLanzamiento, String discografica, String tipoAlbum) {
        this(titulo, artista, fechaLanzamiento);
        this.discografica = discografica != null ? discografica : "";
        this.tipoAlbum = tipoAlbum != null ? tipoAlbum : "Estudio";
    }

    // Métodos de composición / creación
    public Cancion crearCancion(String titulo, int duracionSegundos, GeneroMusical genero)
            throws AlbumCompletoException, DuracionInvalidaException {
        if (canciones.size() >= MAX_CANCIONES) {
            throw new AlbumCompletoException("El álbum ha alcanzado el límite máximo de " + MAX_CANCIONES + " canciones");
        }

        Cancion cancion = new Cancion(titulo, duracionSegundos, artista, genero);
        cancion.setAlbum(this);
        canciones.add(cancion);
        return cancion;
    }

    public Cancion crearCancion(String titulo, int duracionSegundos, GeneroMusical genero, String letra, boolean explicit)
            throws AlbumCompletoException, DuracionInvalidaException {
        if (canciones.size() >= MAX_CANCIONES) {
            throw new AlbumCompletoException("El álbum ha alcanzado el límite máximo de " + MAX_CANCIONES + " canciones");
        }

        Cancion cancion = new Cancion(titulo, duracionSegundos, artista, genero, letra, explicit);
        cancion.setAlbum(this);
        canciones.add(cancion);
        return cancion;
    }

    // Métodos de gestión
    public void eliminarCancion(int posicion) throws CancionNoEncontradaException {
        if (posicion < 1 || posicion > canciones.size()) {
            throw new CancionNoEncontradaException("Posición inválida: " + posicion);
        }
        canciones.remove(posicion - 1);
    }

    public void eliminarCancion(Cancion cancion) throws CancionNoEncontradaException {
        if (!canciones.remove(cancion)) {
            throw new CancionNoEncontradaException("La canción no pertenece a este álbum");
        }
    }

    public int getDuracionTotal() {
        int duracionTotal = 0;
        for (Cancion cancion : canciones) {
            duracionTotal += cancion.getDuracionSegundos();
        }
        return duracionTotal;
    }

    public String getDuracionTotalFormateada() {
        int duracionTotal = getDuracionTotal();
        int horas = duracionTotal / 3600;
        int minutos = (duracionTotal % 3600) / 60;
        int segundos = duracionTotal % 60;

        if (horas > 0) {
            return String.format("%d:%02d:%02d", horas, minutos, segundos);
        } else {
            return String.format("%d:%02d", minutos, segundos);
        }
    }

    public int getNumCanciones() {
        return canciones.size();
    }

    public void ordenarPorPopularidad() {
        canciones.sort((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()));
    }

    public Cancion getCancion(int posicion) throws CancionNoEncontradaException {
        if (posicion < 1 || posicion > canciones.size()) {
            throw new CancionNoEncontradaException("Posición inválida: " + posicion);
        }
        return canciones.get(posicion - 1);
    }

    public int getTotalReproducciones() {
        int total = 0;
        for (Cancion cancion : canciones) {
            total += cancion.getReproducciones();
        }
        return total;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public Date getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(Date fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public ArrayList<Cancion> getCanciones() {
        return new ArrayList<>(canciones); // Copia defensiva
    }

    public String getPortadaURL() {
        return portadaURL;
    }

    public void setPortadaURL(String portadaURL) {
        this.portadaURL = portadaURL;
    }

    public String getDiscografica() {
        return discografica;
    }

    public void setDiscografica(String discografica) {
        this.discografica = discografica;
    }

    public String getTipoAlbum() {
        return tipoAlbum;
    }

    public void setTipoAlbum(String tipoAlbum) {
        this.tipoAlbum = tipoAlbum;
    }

    public int getMaxCanciones() {
        return MAX_CANCIONES;
    }

    // Overrides
    @Override
    public String toString() {
        return String.format("Album{id='%s', titulo='%s', artista='%s', canciones=%d, duracion=%s, tipo='%s'}",
                id,
                titulo,
                artista != null ? artista.getNombreArtistico() : "Desconocido",
                canciones.size(),
                getDuracionTotalFormateada(),
                tipoAlbum);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Album album = (Album) obj;
        return this.id.equals(album.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
