package enums;

public enum TipoSuscripcion {
    GRATUITO(0.0, false, 50, false),
    PREMIUM(9.99, true, -1, true),
    FAMILIAR(14.99, true, -1, true),
    ESTUDIANTE(4.99, true, -1, true);

    //Atributos
    private double precioMensual;
    private boolean sinAnuncios;
    private int limiteReproducciones;
    private boolean descargasOffline;

    //Constructor
    TipoSuscripcion(double precioMensual, boolean sinAnuncios, int limiteReproducciones, boolean descargasOffline) {
        this.precioMensual = precioMensual;
        this.sinAnuncios = sinAnuncios;
        this.limiteReproducciones = limiteReproducciones;
        this.descargasOffline = descargasOffline;
    }

    //GetterSetter
    public double getPrecioMensual() {
        return precioMensual;
    }

    public boolean isSinAnuncios() {
        return this.sinAnuncios;
    }

    public int getLimiteReproducciones(){
        return this.limiteReproducciones;
    }

    public boolean isDescargasOffline(){
        return this.descargasOffline;
    }

    public boolean tieneReproduccionesIlimitadas() {
        if (this.limiteReproducciones == -1) {
            return true;
        } else {
            return false;
        }
    }

    //Metodo toString:
    @Override
    public String toString(){
        return this.name() + "tiene un valor mensual de " + this.precioMensual + "€";
    }
}
