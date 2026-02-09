package enums;

public enum CriterioOrden {
    FECHA_AGREGADO("Fecha de agregado", "Orden por fecha en que se agrego"),
    POPULARIDAD("Popularidad", "Ordena por numero de reproducciones"),
    DURACION("Duracion", "Ordena por duracion del contenido"),
    ALFABETICO("Alfabético", "ordena alfabeticamente por titulo"),
    ARTISTA("Artista", "Ordena por orden del artista"),
    ALEATORIO("aleatorio", "Orden Alatorio");

    //Atributos:
    private String nombre;
    private String descripcion;

    //Constructor:
    CriterioOrden(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    //GetterSetter:
    public String getNombre(){return this.nombre;}
    public String getDescripcion(){return this.descripcion;}

    //Metodo toString
    @Override
    public String toString(){
        return this.nombre;
    }

}
