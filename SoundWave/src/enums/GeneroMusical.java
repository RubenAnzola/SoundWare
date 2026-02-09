package enums;
public enum GeneroMusical {

    POP("Pop", "Musica Contemporanea"),
    ROCK("Rock", "Rock clásico moderno"),
    HIPHOP("Hip Hop", "Hip Hop y Rap"),
    JAZZ("Jazz", "Jaz clásico y contemporáneo"),
    ELECTRONICA("Electrónica", "Música electrónica y EDM"),
    REGGAETON("Reggaetón", "Reggaetón y música urbana latina"),
    INDIE("Indie", "Música independiente"),
    CLASICA("Clásica", "Música clásica"),
    COUNTRY("Country", "Música country"),
    METAL("Metal", "Heavy metal y subgéneros"),
    RNB("R&B", "Rhythm and Blues"),
    SOUL("Soul", "Música soul"),
    BLUES("Blues", "Blues clásico y contemporáneo"),
    TRAP("Trap", "Trap y música urbana");

    //Atributos
    private String nombre;
    private final String descripcion;

    //Constructor
   GeneroMusical(String nombre, String descripcion){
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