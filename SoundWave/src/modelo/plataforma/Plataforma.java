package modelo.plataforma;

import enums.CategoriaPodcast;
import enums.GeneroMusical;
import enums.TipoAnuncio;
import enums.TipoSuscripcion;
import excepciones.artista.AlbumCompletoException;
import excepciones.artista.AlbumYaExisteException;
import excepciones.artista.ArtistaNoVerificadoException;
import excepciones.artista.LimiteEpisodiosException;
import excepciones.contenido.DuracionInvalidaException;
import excepciones.plataforma.ArtistaNoEncontradoException;
import excepciones.plataforma.ContenidoNoEncontradoException;
import excepciones.plataforma.UsuarioYaExisteException;
import excepciones.usuario.EmailInvalidoException;
import excepciones.usuario.PasswordDebilException;
import modelo.artistas.Album;
import modelo.artistas.Artista;
import modelo.artistas.Creador;
import modelo.contenido.Cancion;
import modelo.contenido.Contenido;
import modelo.contenido.Podcast;
import modelo.usuarios.Usuario;
import modelo.usuarios.UsuarioGratuito;
import modelo.usuarios.UsuarioPremium;
import utilidades.RecomendadorIA;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

public class Plataforma {
    // Singleton
    private static Plataforma instancia;

    // Atributos
    private String nombre;
    private HashMap<String, Usuario> usuarios;
    private HashMap<String, Usuario> usuariosPorEmail;
    private ArrayList<Contenido> catalogo;
    private ArrayList<Playlist> playlistsPublicas;
    private HashMap<String, Artista> artistas;
    private HashMap<String, Creador> creadores;
    private ArrayList<Album> albumes;
    private ArrayList<Anuncio> anuncios;
    private RecomendadorIA recomendador;
    private int totalAnunciosReproducidos;

    // Constructor privado
    private Plataforma(String nombre) {
        this.nombre = nombre;
        this.usuarios = new HashMap<>();
        this.usuariosPorEmail = new HashMap<>();
        this.catalogo = new ArrayList<>();
        this.playlistsPublicas = new ArrayList<>();
        this.artistas = new HashMap<>();
        this.creadores = new HashMap<>();
        this.albumes = new ArrayList<>();
        this.anuncios = new ArrayList<>();
        this.totalAnunciosReproducidos = 0;
        this.recomendador = new RecomendadorIA();
        inicializarAnuncios();
    }

    // Métodos Singleton
    public static synchronized Plataforma getInstancia(String nombre) {
        if (instancia == null) {
            instancia = new Plataforma(nombre);
        }
        return instancia;
    }

    public static synchronized Plataforma getInstancia() {
        return getInstancia("SoundWave");
    }

    public static synchronized void reiniciarInstancia() {
        instancia = null;
    }

    // Inicialización de anuncios
    private void inicializarAnuncios() {
        anuncios.add(new Anuncio("Coca-Cola", TipoAnuncio.AUDIO, 1000.0));
        anuncios.add(new Anuncio("Nike", TipoAnuncio.VIDEO, 2000.0));
        anuncios.add(new Anuncio("Samsung", TipoAnuncio.BANNER, 500.0));
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    public UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password, TipoSuscripcion tipo)
            throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
        if (usuariosPorEmail.containsKey(email)) {
            throw new UsuarioYaExisteException("Ya existe un usuario con el email: " + email);
        }
        UsuarioPremium usuario = new UsuarioPremium(nombre, email, password, tipo);
        usuarios.put(usuario.getId(), usuario);
        usuariosPorEmail.put(email, usuario);
        return usuario;
    }

    public UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password)
            throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
        return registrarUsuarioPremium(nombre, email, password, TipoSuscripcion.PREMIUM);
    }

    public UsuarioGratuito registrarUsuarioGratuito(String nombre, String email, String password)
            throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException {
        if (usuariosPorEmail.containsKey(email)) {
            throw new UsuarioYaExisteException("Ya existe un usuario con el email: " + email);
        }
        UsuarioGratuito usuario = new UsuarioGratuito(nombre, email, password);
        usuarios.put(usuario.getId(), usuario);
        usuariosPorEmail.put(email, usuario);
        return usuario;
    }

    public ArrayList<UsuarioPremium> getUsuariosPremium() {
        return usuarios.values().stream()
                .filter(u -> u instanceof UsuarioPremium)
                .map(u -> (UsuarioPremium) u)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<UsuarioGratuito> getUsuariosGratuitos() {
        return usuarios.values().stream()
                .filter(u -> u instanceof UsuarioGratuito)
                .map(u -> (UsuarioGratuito) u)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Usuario> getTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuariosPorEmail.get(email);
    }

    // ==================== GESTIÓN DE ARTISTAS ====================

    public Artista registrarArtista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado) {
        Artista artista = new Artista(nombreArtistico, nombreReal, paisOrigen, verificado, "");
        artistas.put(artista.getId(), artista);
        return artista;
    }

    public void registrarArtista(Artista artista) {
        artistas.put(artista.getId(), artista);
    }

    public ArrayList<Artista> getArtistasVerificados() {
        return artistas.values().stream()
                .filter(Artista::isVerificado)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Artista> getArtistasNoVerificados() {
        return artistas.values().stream()
                .filter(a -> !a.isVerificado())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Artista buscarArtista(String nombre) throws ArtistaNoEncontradoException {
        for (Artista artista : artistas.values()) {
            if (artista.getNombreArtistico().equalsIgnoreCase(nombre) ||
                artista.getNombreReal().equalsIgnoreCase(nombre)) {
                return artista;
            }
        }
        throw new ArtistaNoEncontradoException("Artista no encontrado: " + nombre);
    }

    // ==================== GESTIÓN DE ÁLBUMES ====================

    public Album crearAlbum(Artista artista, String titulo, Date fecha)
            throws ArtistaNoVerificadoException, AlbumYaExisteException {
        Album album = artista.crearAlbum(titulo, fecha);
        albumes.add(album);
        return album;
    }

    public ArrayList<Album> getAlbumes() {
        return new ArrayList<>(albumes);
    }

    // ==================== GESTIÓN DE CANCIONES ====================

    public Cancion crearCancion(String titulo, int duracion, Artista artista, GeneroMusical genero)
            throws DuracionInvalidaException {
        Cancion cancion = new Cancion(titulo, duracion, artista, genero);
        catalogo.add(cancion);
        artista.publicarCancion(cancion);
        return cancion;
    }

    public Cancion crearCancionEnAlbum(String titulo, int duracion, Artista artista, GeneroMusical genero, Album album)
            throws DuracionInvalidaException, AlbumCompletoException {
        Cancion cancion = album.crearCancion(titulo, duracion, genero);
        catalogo.add(cancion);
        return cancion;
    }

    public void agregarContenidoCatalogo(Contenido contenido) {
        if (!catalogo.contains(contenido)) {
            catalogo.add(contenido);
        }
    }

    public ArrayList<Cancion> getCanciones() {
        return catalogo.stream()
                .filter(c -> c instanceof Cancion)
                .map(c -> (Cancion) c)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // ==================== GESTIÓN DE CREADORES/PODCASTS ====================

    public Creador registrarCreador(String nombreCanal, String nombre, String descripcion) {
        Creador creador = new Creador(nombreCanal, nombre, descripcion);
        creadores.put(creador.getId(), creador);
        return creador;
    }

    public void registrarCreador(Creador creador) {
        creadores.put(creador.getId(), creador);
    }

    public Podcast crearPodcast(String titulo, int duracion, Creador creador, int numEpisodio, int temporada,
                                CategoriaPodcast categoria)
            throws DuracionInvalidaException, LimiteEpisodiosException {
        Podcast podcast = new Podcast(titulo, duracion, creador, numEpisodio, temporada, categoria);
        creador.publicarPodcast(podcast);
        catalogo.add(podcast);
        return podcast;
    }

    public ArrayList<Podcast> getPodcasts() {
        return catalogo.stream()
                .filter(c -> c instanceof Podcast)
                .map(c -> (Podcast) c)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Creador> getTodosLosCreadores() {
        return new ArrayList<>(creadores.values());
    }

    // ==================== GESTIÓN DE PLAYLISTS PÚBLICAS ====================

    public Playlist crearPlaylistPublica(String nombre, Usuario creador) {
        Playlist playlist = new Playlist(nombre, creador, true, "");
        playlistsPublicas.add(playlist);
        return playlist;
    }

    public ArrayList<Playlist> getPlaylistsPublicas() {
        return new ArrayList<>(playlistsPublicas);
    }

    // ==================== BÚSQUEDAS ====================

    public ArrayList<Contenido> buscarContenido(String termino) throws ContenidoNoEncontradoException {
        ArrayList<Contenido> resultados = catalogo.stream()
                .filter(c -> c.getTitulo().toLowerCase().contains(termino.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (resultados.isEmpty()) {
            throw new ContenidoNoEncontradoException("No se encontró contenido con el término: " + termino);
        }
        return resultados;
    }

    public ArrayList<Cancion> buscarPorGenero(GeneroMusical genero) throws ContenidoNoEncontradoException {
        ArrayList<Cancion> resultados = catalogo.stream()
                .filter(c -> c instanceof Cancion)
                .map(c -> (Cancion) c)
                .filter(c -> c.getGenero() == genero)
                .collect(Collectors.toCollection(ArrayList::new));

        if (resultados.isEmpty()) {
            throw new ContenidoNoEncontradoException("No se encontraron canciones del género: " + genero);
        }
        return resultados;
    }

    public ArrayList<Podcast> buscarPorCategoria(CategoriaPodcast categoria) throws ContenidoNoEncontradoException {
        ArrayList<Podcast> resultados = catalogo.stream()
                .filter(c -> c instanceof Podcast)
                .map(c -> (Podcast) c)
                .filter(p -> p.getCategoria() == categoria)
                .collect(Collectors.toCollection(ArrayList::new));

        if (resultados.isEmpty()) {
            throw new ContenidoNoEncontradoException("No se encontraron podcasts de la categoría: " + categoria);
        }
        return resultados;
    }

    public ArrayList<Contenido> obtenerTopContenidos(int cantidad) {
        return catalogo.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()))
                .limit(cantidad)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // ==================== ANUNCIOS ====================

    public Anuncio obtenerAnuncioAleatorio() {
        ArrayList<Anuncio> anunciosActivos = anuncios.stream()
                .filter(Anuncio::puedeMostrarse)
                .collect(Collectors.toCollection(ArrayList::new));

        if (anunciosActivos.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return anunciosActivos.get(random.nextInt(anunciosActivos.size()));
    }

    public void incrementarAnunciosReproducidos() {
        totalAnunciosReproducidos++;
    }

    // ==================== ESTADÍSTICAS ====================

    public String obtenerEstadisticasGenerales() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS GENERALES DE ").append(nombre).append(" ===\n");
        stats.append("Total de usuarios: ").append(usuarios.size()).append("\n");
        stats.append("  - Usuarios Premium: ").append(getUsuariosPremium().size()).append("\n");
        stats.append("  - Usuarios Gratuitos: ").append(getUsuariosGratuitos().size()).append("\n");
        stats.append("Total de contenido: ").append(catalogo.size()).append("\n");
        stats.append("  - Canciones: ").append(getCanciones().size()).append("\n");
        stats.append("  - Podcasts: ").append(getPodcasts().size()).append("\n");
        stats.append("Total de artistas: ").append(artistas.size()).append("\n");
        stats.append("  - Verificados: ").append(getArtistasVerificados().size()).append("\n");
        stats.append("Total de creadores: ").append(creadores.size()).append("\n");
        stats.append("Total de álbumes: ").append(albumes.size()).append("\n");
        stats.append("Playlists públicas: ").append(playlistsPublicas.size()).append("\n");
        stats.append("Anuncios reproducidos: ").append(totalAnunciosReproducidos).append("\n");
        return stats.toString();
    }

    // ==================== GETTERS BÁSICOS ====================

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Contenido> getCatalogo() {
        return new ArrayList<>(catalogo);
    }

    public HashMap<String, Artista> getArtistas() {
        return new HashMap<>(artistas);
    }

    public HashMap<String, Creador> getCreadores() {
        return new HashMap<>(creadores);
    }

    public ArrayList<Anuncio> getAnuncios() {
        return new ArrayList<>(anuncios);
    }

    public RecomendadorIA getRecomendador() {
        return recomendador;
    }

    public int getTotalUsuarios() {
        return usuarios.size();
    }

    public int getTotalContenido() {
        return catalogo.size();
    }

    public int getTotalAnunciosReproducidos() {
        return totalAnunciosReproducidos;
    }

    // ==================== OVERRIDES ====================

    @Override
    public String toString() {
        return "Plataforma{" +
                "nombre='" + nombre + '\'' +
                ", usuarios=" + usuarios.size() +
                ", contenido=" + catalogo.size() +
                ", artistas=" + artistas.size() +
                ", creadores=" + creadores.size() +
                '}';
    }
}
