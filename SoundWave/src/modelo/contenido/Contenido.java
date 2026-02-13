package modelo.contenido;

import excepciones.contenido.ContenidoNoDisponibleException;
import excepciones.contenido.DuracionInvalidaException;

import java.util.UUID;
import java.util.Date;
import java.util.ArrayList;


public abstract class Contenido {

    protected String id;
    protected String titulo;
    protected int reproducciones;
    protected int likes;
    protected int duracionSegundos;
    protected ArrayList<String> tags;
    protected boolean disponible;
    protected Date fechaPublicacion;


    public Contenido(String titulo, int duracionSegundos) throws DuracionInvalidaException {
        // Validar duración
        if (duracionSegundos <= 0) {
            throw new DuracionInvalidaException("La duración debe ser mayor a 0 segundos");
        }

        // Asignar valores principales
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.duracionSegundos = duracionSegundos;

        // Valores por defecto
        this.reproducciones = 0;
        this.likes = 0;
        this.tags = new ArrayList<>();
        this.disponible = true;
        this.fechaPublicacion = new Date();
    }

    public void reproducir() throws ContenidoNoDisponibleException {
        if (!disponible) { throw new ContenidoNoDisponibleException("El contenido no está disponible para reproducir"); } aumentarReproducciones();
    }

    public void aumentarReproducciones(){
        this.reproducciones++;
    }

    public void agregarLike(){
        this.likes++;
    }

    public boolean esPopular(){
        return this.reproducciones > 100000;
    }

    public void validarDuracion() throws DuracionInvalidaException {
        if (this.duracionSegundos <= 0) {
            throw new DuracionInvalidaException("La duración debe ser mayor a 0 segundos");
        }
    }

    public void agregarTag(String tag) {
        // Validar que el tag no sea nulo o vacío
        if (tag != null && !tag.trim().isEmpty()) {
            // Evitar duplicados
            if (!this.tags.contains(tag.trim())) {
                this.tags.add(tag.trim());
            }
        }
    }

    public boolean tieneTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return false;
        }
        return this.tags.contains(tag.trim());
    }

    public void marcarNoDisponible() {
        this.disponible = false;
    }

    public void marcarDisponible() {
        this.disponible = true;
    }

    public String getDuracionFormateada() {
        int minutos = this.duracionSegundos / 60;
        int segundos = this.duracionSegundos % 60;
        return String.format("%d:%02d", minutos, segundos);
    }

    //Getter and Setter:

    public String getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getReproducciones() {
        return this.reproducciones;
    }

    public void setReproducciones(int reproducciones) {
        this.reproducciones = reproducciones;
    }

    public int getLikes() {
        return this.likes;
    }

    public int getDuracionSegundos() {
        return this.duracionSegundos;
    }

    public ArrayList<String> getTags() {
        return new ArrayList<>(this.tags);
    }

    public boolean isDisponible() {
        return this.disponible;
    }

    public Date getFechaPublicacion() {
        return this.fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    @Override
    public String toString() {
        return String.format("Contenido{id='%s', titulo='%s', reproducciones=%d, likes=%d, duracion=%s, disponible=%s}",
                this.id,
                this.titulo,
                this.reproducciones,
                this.likes,
                getDuracionFormateada(),
                this.disponible ? "Sí" : "No");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Contenido contenido = (Contenido) obj;
        return this.id.equals(contenido.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }






}
