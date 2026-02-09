package enums;

public enum CategoriaPodcast {
    TECNOLOGIA("Tecnologia", "Podcasts sobre tecnología e innovación"),
    DEPORTES("Deportes", "podcasts deportivos"),
    COMEDIA("Comedia", "Podcasts de humor y entretenimiento"),
    TRUE_CRIME("True Crime", "Podcats de crimenes reales"),
    EDUCACION("Educacion", "Podcats de negocios y emprendimiento"),
    NEGOCIOS("Negocios", "Podcasts de negocios y emprendimiento"),
    SALUD("Salud", "Podcasts de salud y bienestar"),
    ENTRETENIMIENTO("Entretenimiento", "Podcasts de entretenimiento general"),
    HISTORIA("Historia", "Podcasts históricos"),
    CIENCIA("Ciencia", "Podcasts científicos"),
    POLITICA("Política", "Podcasts de política y actualidad"),
    CULTURA("Cultura", "Podcasts culturales");

    //Atributos
    private String nombre;
    private String descripcion;

    //Constructor
    CategoriaPodcast(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    //GetterSetter
    public String getNombre(){return this.nombre;}
    public String getDescripcion(){return this.descripcion;}

    //Método Override
    @Override
    public String toString(){
        return this.nombre;
    }
}
