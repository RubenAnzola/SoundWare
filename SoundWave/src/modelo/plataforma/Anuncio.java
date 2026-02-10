package modelo.plataforma;

import enums.TipoAnuncio;
import java.util.UUID;

public class Anuncio {

    // Atributos
    private String id;
    private String empresa;
    private int duracionSegundos;
    private String audioURL;
    private TipoAnuncio tipo;
    private int impresiones;
    private double presupuesto;
    private boolean activo;

    // Constructores
    public Anuncio(String empresa, TipoAnuncio tipo, double presupuesto) {
        this.id = UUID.randomUUID().toString();
        this.empresa = empresa;
        this.tipo = tipo;
        this.presupuesto = presupuesto;
        this.duracionSegundos = tipo.getDuracionSegundos();
        this.impresiones = 0;
        this.activo = true;
        this.audioURL = null;
    }

    public Anuncio(String empresa, TipoAnuncio tipo, double presupuesto, String audioURL) {
        this.id = UUID.randomUUID().toString();
        this.empresa = empresa;
        this.tipo = tipo;
        this.presupuesto = presupuesto;
        this.audioURL = audioURL;
        this.duracionSegundos = tipo.getDuracionSegundos();
        this.impresiones = 0;
        this.activo = true;
    }

    // Métodos
    public void reproducir() {
        if (this.activo) {
            System.out.println("Reproduciendo anuncio de " + this.empresa);
            registrarImpresion();
        }
    }

    public void registrarImpresion() {
        this.impresiones++;
        double costoTotal = calcularCostoTotal();

        // Desactivar si se agota el presupuesto
        if (costoTotal >= this.presupuesto) {
            desactivar();
        }
    }

    public double calcularCostoPorImpresion() {
        return this.tipo.getCostoPorImpresion();
    }

    public double calcularCostoTotal() {
        return this.impresiones * calcularCostoPorImpresion();
    }

    public int calcularImpresionesRestantes() {
        double costoTotal = calcularCostoTotal();
        double presupuestoRestante = this.presupuesto - costoTotal;

        if (presupuestoRestante <= 0) {
            return 0;
        }

        return (int) (presupuestoRestante / calcularCostoPorImpresion());
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }

    public boolean puedeMostrarse() {
        return this.activo && calcularImpresionesRestantes() > 0;
    }

    // Getters y Setters
    public String getId() {
        return this.id;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getDuracionSegundos() {
        return this.duracionSegundos;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public String getAudioURL() {
        return this.audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public TipoAnuncio getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoAnuncio tipo) {
        this.tipo = tipo;
    }

    public int getImpresiones() {
        return this.impresiones;
    }

    public double getPresupuesto() {
        return this.presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Overrides
    @Override
    public String toString() {
        return "Anuncio{" +
                "id='" + id + '\'' +
                ", empresa='" + empresa + '\'' +
                ", duracionSegundos=" + duracionSegundos +
                ", tipo=" + tipo +
                ", impresiones=" + impresiones +
                ", presupuesto=" + presupuesto +
                ", activo=" + activo +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Anuncio anuncio = (Anuncio) obj;
        return id.equals(anuncio.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
