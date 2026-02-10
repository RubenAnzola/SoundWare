package modelo.plataforma;

import excepciones.playlist.ContenidoDuplicadoException;
import excepciones.playlist.PlaylistLlenaException;
import modelo.usuarios.Usuario;

import java.sql.Date;

public class Playlist {
    private static final int MAX_CONTENIDOS_DEFAULT = 100;

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


    //contructores:
   public Playlist(String nombre, Usuario creador){
       this. nombre = nombre;
       this.creador = creador;
   }

    public Playlist(String nombre, Usuario creador, boolean esPublica, String descripcion) {
        this.nombre = nombre;
        this.creador = creador;
        this.esPublica = esPublica;
        this.descripcion = descripcion;
    }

    public void agregarContenido(Contenido contenido) throws PlaylistLlenaException, ContenidoDuplicadoException{
        if (contenidos.size() >= maxContenidos) {
            throw new PlaylistLlenaException("La playlist ha alcanzado su capacidad máxima.");
        }
        if (contenidos.contains(contenido)) {
            throw new ContenidoDuplicadoException("El contenido ya existe en la playlist.");
        }
        contenidos.add(contenido);
    }



}
