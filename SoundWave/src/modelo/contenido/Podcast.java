package modelo.contenido;

import enums.CategoriaPodcast;
import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.contenido.EpisodioNoEncontradoException;
import excepciones.contenido.TranscripcionNoDisponibleException;
import excepciones.descarga.ContenidoYaDescargadoException;
import excepciones.descarga.LimiteDescargasException;
import interfaces.Descargable;
import interfaces.Reproducible;
import modelo.artistas.Creador;

import java.util.ArrayList;

public class Podcast extends Contenido implements Reproducible, Descargable {


    private Creador creador;
    private int numeroEpisodio;
    private int temporada;
    private String descripcion;
    private CategoriaPodcast categoria;
    private ArrayList<String> invitados;
    private String transcripcion;
    private boolean reproduciendo;
    private boolean pausado;
    private boolean descargado;

    public Podcast(String titulo, int duracionSegundos, Creador creador, int numeroEpisodio, int temporada, CategoriaPodcast categoria) throws DuracionInvalidaException {
        super(titulo, duracionSegundos);
        this.creador = creador;
        this.numeroEpisodio = numeroEpisodio;
        this.temporada = temporada;
        this.categoria = categoria;
        this.descripcion = "";
        this.invitados = new ArrayList<>();
        this.transcripcion = null;
        this.reproduciendo = false;
        this.pausado = false;
        this.descargado = false;
    }

    public Podcast(String titulo, int duracionSegundos, Creador creador, int numeroEpisodio, int temporada, CategoriaPodcast categoria, String descripcion) throws DuracionInvalidaException {
        this(titulo, duracionSegundos, creador, numeroEpisodio, temporada, categoria);
        this.descripcion = descripcion;
    }

    @Override
    public void reproducir() throws ContenidoNoDisponibleException {
        if (!isDisponible()) {
            throw new ContenidoNoDisponibleException("El podcast '" + getTitulo() + "' no está disponible actualmente");
        }

        play();
        aumentarReproducciones();
    }


    @Override
    public void play() {
        if (!reproduciendo) {
            reproduciendo = true;
            pausado = false;
            System.out.println("▶️ Reproduciendo podcast: " + getTitulo());
            System.out.println("   Episodio " + numeroEpisodio + " - Temporada " + temporada);
            System.out.println("   Creador: " + creador.getNombreCanal());
            System.out.println("   Duración: " + getDuracionFormateada());
        }
    }

    @Override
    public void pause() {
        if (reproduciendo && !pausado) {
            pausado = true;
            System.out.println("⏸️ Podcast pausado: " + getTitulo());
        }
    }

    @Override
    public void stop() {
        if (reproduciendo || pausado) {
            reproduciendo = false;
            pausado = false;
            System.out.println("⏹️ Podcast detenido: " + getTitulo());
        }
    }

    @Override
    public int getDuracion() {
        return getDuracionSegundos();
    }


    @Override
    public boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException {
        if (descargado) {
            throw new ContenidoYaDescargadoException("El podcast '" + getTitulo() + "' ya está descargado");
        }

        descargado = true;
        System.out.println("⬇️ Descargado: " + getTitulo());
        System.out.println("   Episodio " + numeroEpisodio + " - Temporada " + temporada);
        System.out.println("   Espacio requerido: " + espacioRequerido() + " MB");
        return true;
    }

    @Override
    public boolean eliminarDescarga() {
        if (descargado) {
            descargado = false;
            System.out.println("🗑️ Descarga eliminada: " + getTitulo());
            return true;
        }
        return false;
    }

    @Override
    public int espacioRequerido() {
        // Aproximación: 1 MB por minuto de audio
        int minutos = getDuracionSegundos() / 60;
        return Math.max(minutos, 1);
    }

    //Metodos propios:

    public String obtenerDescripcion() {
        if (descripcion != null && !descripcion.isEmpty()) {
            return descripcion;
        }
        return "Episodio " + numeroEpisodio + " de la temporada " + temporada +
                " - " + categoria.getNombre() + " por " + creador.getNombreCanal();
    }

    public void agregarInvitado(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            String nombreLimpio = nombre.trim();
            if (!invitados.contains(nombreLimpio)) {
                invitados.add(nombreLimpio);
                System.out.println("✅ Invitado agregado: " + nombreLimpio);
            }
        }
    }

    public boolean esTemporadaNueva() {
        // Considera "nueva" si es de las 2 últimas temporadas del creador
        int ultimaTemporada = creador.getUltimaTemporada();
        return temporada >= (ultimaTemporada - 1);
    }

    public String obtenerTranscripcion() throws TranscripcionNoDisponibleException {
        if (transcripcion == null || transcripcion.isEmpty()) {
            throw new TranscripcionNoDisponibleException(
                    "La transcripción del episodio '" + getTitulo() + "' no está disponible"
            );
        }
        return transcripcion;
    }

    public void validarEpisodio() throws excepciones.contenido.EpisodioNoEncontradoException {
        if (numeroEpisodio <= 0) {
            throw new excepciones.contenido.EpisodioNoEncontradoException(
                    "Número de episodio inválido: " + numeroEpisodio
            );
        }
        if (temporada <= 0) {
            throw new excepciones.contenido.EpisodioNoEncontradoException(
                    "Número de temporada inválido: " + temporada
            );
        }
    }

    //Getter y Setter:


    public Creador getCreador() {
        return creador;
    }

    public int getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public int getTemporada() {
        return temporada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public CategoriaPodcast getCategoria() {
        return categoria;
    }

    public ArrayList<String> getInvitados() {
        return invitados;
    }

    public String getTranscripcion() {
        return transcripcion;
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

    public void setCreador(Creador creador) {
        this.creador = creador;
    }

    public void setNumeroEpisodio(int numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCategoria(CategoriaPodcast categoria) {
        this.categoria = categoria;
    }

    public void setTranscripcion(String transcripcion) {
        this.transcripcion = transcripcion;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🎙️ PODCAST\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("Título: ").append(getTitulo()).append("\n");
        sb.append("Episodio: ").append(numeroEpisodio).append(" | Temporada: ").append(temporada).append("\n");
        sb.append("Creador: ").append(creador.getNombreCanal()).append("\n");
        sb.append("Categoría: ").append(categoria.getNombre()).append("\n");
        sb.append("Duración: ").append(getDuracionFormateada()).append("\n");
        sb.append("Reproducciones: ").append(getReproducciones()).append("\n");

        if (!invitados.isEmpty()) {
            sb.append("Invitados: ").append(String.join(", ", invitados)).append("\n");
        }

        if (descripcion != null && !descripcion.isEmpty()) {
            sb.append("Descripción: ").append(descripcion).append("\n");
        }

        sb.append("Estado: ");
        if (reproduciendo) {
            sb.append("▶️ Reproduciendo");
        } else if (pausado) {
            sb.append("⏸️ Pausado");
        } else {
            sb.append("⏹️ Detenido");
        }

        if (descargado) {
            sb.append(" | ⬇️ Descargado (").append(espacioRequerido()).append(" MB)");
        }

        sb.append("\n");
        sb.append("Disponible: ").append(isDisponible() ? "✅ Sí" : "❌ No").append("\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return sb.toString();
    }
}