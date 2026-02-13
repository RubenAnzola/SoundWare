package modelo.contenido;

import enums.GeneroMusical;
import excepciones.contenido.*;
import excepciones.descarga.*;
import interfaces.Descargable;
import interfaces.Reproducible;
import modelo.artistas.Album;
import modelo.artistas.Artista;

import java.util.UUID;

public class Cancion extends Contenido implements Reproducible, Descargable {
    private String letra;
    private Artista artista;
    private Album album;
    private GeneroMusical genero;
    private String audioURL;
    private boolean explicit;
    private String ISRC;
    private boolean reproduciendo;
    private boolean pausado;
    private boolean descargado;


    // Constructor
    public Cancion(String titulo, int duracionSegundos, Artista artista, GeneroMusical genero) throws DuracionInvalidaException {
        super(titulo, duracionSegundos);
        this.artista = artista;
        this.genero = genero;
        this.letra = "";
        this.explicit = false;
        this.album = null;
        this.audioURL = "https://audio.soundwave.com/default/" + getId();
        this.ISRC = generarISRC();
        this.reproduciendo = false;
        this.pausado = false;
        this.descargado = false;
    }

    public Cancion(String titulo, int duracionSegundos, Artista artista, GeneroMusical genero, String letra, boolean explicit) throws DuracionInvalidaException {
        super(titulo, duracionSegundos);
        this.artista = artista;
        this.genero = genero;
        this.letra = letra != null ? letra : "";
        this.explicit = explicit;
        this.album = null;
        this.audioURL = "https://audio.soundwave.com/default/" + getId();
        this.ISRC = generarISRC();
        this.reproduciendo = false;
        this.pausado = false;
        this.descargado = false;
    }

    private String generarISRC() {
        return "SW" + UUID.randomUUID().toString().substring(0, 10).toUpperCase().replace("-", "");
    }

    @Override
    public void reproducir() throws ContenidoNoDisponibleException {
        if (!isDisponible()) {
            throw new ContenidoNoDisponibleException("La canción '" + getTitulo() + "' no está disponible para reproducción");
        }

        play();
        aumentarReproducciones();
    }

    @Override
    public void play() {
        if (!reproduciendo) {
            reproduciendo = true;
            pausado = false;
            System.out.println("▶ Reproduciendo: " + getTitulo() + " - " + artista.getNombreArtistico());
            System.out.println("   Duración: " + getDuracionFormateada() + " | Género: " + genero.getNombre());
        } else {
            System.out.println("La canción ya está en reproducción");
        }
    }

    @Override
    public void pause() {
        if (reproduciendo && !pausado) {
            pausado = true;
            System.out.println("⏸ Pausado: " + getTitulo());
        } else if (!reproduciendo) {
            System.out.println("No se puede pausar: la canción no está reproduciendo");
        } else {
            System.out.println("La canción ya está pausada");
        }
    }

    @Override
    public void stop() {
        if (reproduciendo || pausado) {
            reproduciendo = false;
            pausado = false;
            System.out.println("⏹ Detenido: " + getTitulo());
        } else {
            System.out.println("La canción no está reproduciendo");
        }
    }

    @Override
    public int getDuracion() {
        return duracionSegundos;
    }

    //Imprementacion interfaz Descargable:

    @Override
    public boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException {
        if (descargado) {
            throw new ContenidoYaDescargadoException("La canción '" + getTitulo() + "' ya está descargada");
        }

        descargado = true;
        System.out.println("⬇ Descargando: " + getTitulo() + " - " + artista.getNombreArtistico());
        System.out.println("   Espacio requerido: " + espacioRequerido() + " MB");
        return true;
    }

    @Override
    public boolean eliminarDescarga() {
        if (descargado) {
            descargado = false;
            System.out.println("🗑 Descarga eliminada: " + getTitulo());
            return true;
        } else {
            System.out.println("La canción no estaba descargada");
            return false;
        }
    }

    @Override
    public int espacioRequerido() {
        // Aproximación: 1 MB por minuto de audio en calidad estándar
        int minutos = (duracionSegundos + 59) / 60; // Redondeo hacia arriba
        return Math.max(minutos, 1); // Mínimo 1 MB
    }


    //Metodos Propios:
    public String obtenerLetra() throws LetraNoDisponibleException {
        if (letra == null || letra.trim().isEmpty()) {
            throw new LetraNoDisponibleException("La letra de '" + getTitulo() + "' no está disponible");
        }
        return letra;
    }

    public boolean esExplicit() {
        return explicit;
    }

    public void cambiarGenero(GeneroMusical nuevoGenero) {
        if (nuevoGenero != null) {
            this.genero = nuevoGenero;
            System.out.println("Género actualizado: " + getTitulo() + " -> " + nuevoGenero.getNombre());
        }
    }

    public void validarAudioURL() throws ArchivoAudioNoEncontradoException {
        if (audioURL == null || audioURL.trim().isEmpty()) {
            throw new ArchivoAudioNoEncontradoException("No se encontró el archivo de audio para '" + getTitulo() + "'");
        }
    }

    //getter and Setter


    public String getLetra() {
        return letra;
    }

    public Artista getArtista() {
        return artista;
    }

    public Album getAlbum() {
        return album;
    }

    public GeneroMusical getGenero() {
        return genero;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public String getISRC() {
        return ISRC;
    }

    public boolean isReproduciendo() {
        return reproduciendo;
    }

    public boolean isPausado() {
        return pausado;
    }

    public boolean isDescargado() {
        return descargado;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setGenero(GeneroMusical genero) {
        this.genero = genero;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("🎵 ").append(getTitulo());

        if (artista != null) {
            sb.append(" - ").append(artista.getNombreArtistico());
        }

        sb.append(" [").append(getDuracionFormateada()).append("]");

        if (explicit) {
            sb.append(" 🅴");
        }

        return sb.toString();
    }

}






