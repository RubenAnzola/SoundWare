package modelo.artistas;

import excepciones.artista.AlbumYaExisteException;
import excepciones.artista.ArtistaNoVerificadoException;
import modelo.contenido.Cancion;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Artista {
    private String id;
    private String nombreArtistico;
    private String nombreReal;
    private String paisOrigen;
    private ArrayList<Cancion> discografia;
    private ArrayList<Album> albumes;
    private int oyentesMensuales;
    private boolean verificado;
    private String biografia;

    // Constructores
    public Artista(String nombreArtistico, String nombreReal, String paisOrigen) {
        this.id = UUID.randomUUID().toString();
        this.nombreArtistico = nombreArtistico;
        this.nombreReal = nombreReal;
        this.paisOrigen = paisOrigen;
        this.discografia = new ArrayList<>();
        this.albumes = new ArrayList<>();
        this.oyentesMensuales = 0;
        this.verificado = false;
        this.biografia = "";
    }

    public Artista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado, String biografia) {
        this(nombreArtistico, nombreReal, paisOrigen);
        this.verificado = verificado;
        this.biografia = biografia != null ? biografia : "";
    }

    // Métodos
    public void publicarCancion(Cancion cancion) {
        if (cancion != null && !discografia.contains(cancion)) {
            discografia.add(cancion);
        }
    }

    public Album crearAlbum(String titulo, Date fecha) throws ArtistaNoVerificadoException, AlbumYaExisteException {
        if (!verificado) {
            throw new ArtistaNoVerificadoException("El artista debe estar verificado para crear álbumes");
        }

        // Verificar duplicados
        for (Album album : albumes) {
            if (album.getTitulo().equalsIgnoreCase(titulo)) {
                throw new AlbumYaExisteException("Ya existe un álbum con el título '" + titulo + "'");
            }
        }

        Album album = new Album(titulo, this, fecha);
        albumes.add(album);
        return album;
    }

    public void agregarAlbum(Album album) {
        if (album != null && !albumes.contains(album)) {
            albumes.add(album);
        }
    }

    public boolean eliminarAlbum(String idAlbum) {
        return albumes.removeIf(album -> album.getId().equals(idAlbum));
    }

    public ArrayList<Cancion> obtenerTopCanciones(int cantidad) {
        ArrayList<Cancion> topCanciones = new ArrayList<>(discografia);
        topCanciones.sort((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()));

        if (cantidad < topCanciones.size()) {
            return new ArrayList<>(topCanciones.subList(0, cantidad));
        }
        return topCanciones;
    }

    public double calcularPromedioReproducciones() {
        if (discografia.isEmpty()) {
            return 0.0;
        }

        int totalReproducciones = getTotalReproducciones();
        return (double) totalReproducciones / discografia.size();
    }

    public boolean esVerificado() {
        return verificado;
    }

    public int getTotalReproducciones() {
        int total = 0;
        for (Cancion cancion : discografia) {
            total += cancion.getReproducciones();
        }
        return total;
    }

    public void verificar() {
        this.verificado = true;
    }

    public void incrementarOyentes() {
        this.oyentesMensuales++;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombreArtistico() {
        return nombreArtistico;
    }

    public void setNombreArtistico(String nombreArtistico) {
        this.nombreArtistico = nombreArtistico;
    }

    public String getNombreReal() {
        return nombreReal;
    }

    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public ArrayList<Cancion> getDiscografia() {
        return new ArrayList<>(discografia); // Copia defensiva
    }

    public ArrayList<Album> getAlbumes() {
        return new ArrayList<>(albumes); // Copia defensiva
    }

    public int getOyentesMensuales() {
        return oyentesMensuales;
    }

    public void setOyentesMensuales(int oyentesMensuales) {
        this.oyentesMensuales = oyentesMensuales;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    // Overrides
    @Override
    public String toString() {
        return String.format("Artista{id='%s', nombreArtistico='%s', nombreReal='%s', paisOrigen='%s', canciones=%d, albumes=%d, oyentesMensuales=%d, verificado=%s}",
                id,
                nombreArtistico,
                nombreReal,
                paisOrigen,
                discografia.size(),
                albumes.size(),
                oyentesMensuales,
                verificado ? "Sí" : "No");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Artista artista = (Artista) obj;
        return this.id.equals(artista.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}

