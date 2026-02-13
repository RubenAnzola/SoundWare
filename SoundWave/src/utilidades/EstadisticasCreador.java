package utilidades;

import java.util.HashMap;
import modelo.artistas.Creador;
import modelo.contenido.Podcast;

public class EstadisticasCreador {

    private Creador creador;
    private int totalEpisodios;
    private int totalReproducciones;
    private double promedioReproducciones;
    private int totalSuscriptores;
    private int totalLikes;
    private int duracionTotalSegundos;
    private Podcast episodioMasPopular;
    private HashMap<Integer, Integer> episodiosPorTemporada;


    //contructor
    public EstadisticasCreador(Creador creador) {
        this.creador = creador;
        this.episodiosPorTemporada = new HashMap<>();
        calcularEstadisticas();
    }

    private void calcularEstadisticas() {
        // Obtener suscriptores del creador (no se suman por episodio)
        this.totalSuscriptores = creador.getSuscriptores();

        for (Podcast episodio : creador.getEpisodios()) {
            totalEpisodios++;
            totalReproducciones += episodio.getReproducciones();
            totalLikes += episodio.getLikes();
            duracionTotalSegundos += episodio.getDuracionSegundos();

            if (episodioMasPopular == null || episodio.getReproducciones() > episodioMasPopular.getReproducciones()) {
                episodioMasPopular = episodio;
            }

            int temporada = episodio.getTemporada();
            episodiosPorTemporada.put(temporada, episodiosPorTemporada.getOrDefault(temporada, 0) + 1);
        }

        if (totalEpisodios > 0) {
            promedioReproducciones = (double) totalReproducciones / totalEpisodios;
        }
    }

    public String formatearDuracion(int segundos) {
        int horas = segundos / 3600;
        int minutos = (segundos % 3600) / 60;
        int segs = segundos % 60;

        if (horas > 0) {
            return String.format("%d:%02d:%02d", horas, minutos, segs);
        } else {
            return String.format("%d:%02d", minutos, segs);
        }
    }
        //Metodos
    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();

        reporte.append("Estadísticas del Creador: ").append(creador.getNombre()).append("\n");
        reporte.append("Total de Episodios: ").append(totalEpisodios).append("\n");
        reporte.append("Total de Reproducciones: ").append(totalReproducciones).append("\n");
        reporte.append("Promedio de Reproducciones por Episodio: ")
                .append(String.format("%.2f", promedioReproducciones)).append("\n");
        reporte.append("Total de Suscriptores: ").append(totalSuscriptores).append("\n");
        reporte.append("Total de Likes: ").append(totalLikes).append("\n");
        reporte.append("Duración Total: ").append(formatearDuracion(duracionTotalSegundos)).append("\n");

        if (episodioMasPopular != null) {
            reporte.append("Episodio Más Popular: ").append(episodioMasPopular.getTitulo())
                    .append(" (").append(episodioMasPopular.getReproducciones())
                    .append(" reproducciones)").append("\n");
        }

        reporte.append("Episodios por Temporada:\n");
        for (Integer temporada : episodiosPorTemporada.keySet()) {
            reporte.append("  Temporada ").append(temporada).append(": ")
                    .append(episodiosPorTemporada.get(temporada)).append(" episodios\n");
        }

        return reporte.toString();
    }

    public double calcularEngagement() {
        if (totalReproducciones == 0) {
            return 0.0;
        }
        return (double) totalLikes / totalReproducciones * 100;
    }

    public double estimarCrecimientoMensual() {
        if (totalEpisodios == 0 || totalSuscriptores == 0) {
            return 0.0;
        }

        double engagement = calcularEngagement();
        double factorCrecimiento = (promedioReproducciones / totalSuscriptores) * (engagement / 100);

        return factorCrecimiento * 100;  // ← Faltaba esta línea
    }

    // Getters
    public Creador getCreador() {
        return creador;
    }

    public int getTotalEpisodios() {
        return totalEpisodios;
    }

    public int getTotalReproducciones() {
        return totalReproducciones;
    }

    public double getPromedioReproducciones() {
        return promedioReproducciones;
    }

    public int getTotalSuscriptores() {
        return totalSuscriptores;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public int getDuracionTotalSegundos() {
        return duracionTotalSegundos;
    }

    public Podcast getEpisodioMasPopular() {
        return episodioMasPopular;
    }


    public HashMap<Integer, Integer> getEpisodiosPorTemporada() {
        return new HashMap<>(episodiosPorTemporada);  // Copia defensiva
    }

    @Override
    public String toString() {
        return "EstadisticasCreador{" +
                "creador=" + creador +
                ", totalEpisodios=" + totalEpisodios +
                ", totalReproducciones=" + totalReproducciones +
                ", promedioReproducciones=" + promedioReproducciones +
                ", totalSuscriptores=" + totalSuscriptores +
                ", totalLikes=" + totalLikes +
                ", duracionTotalSegundos=" + duracionTotalSegundos +
                ", episodioMasPopular=" + episodioMasPopular +
                ", episodiosPorTemporada=" + episodiosPorTemporada +
                '}';
         }
    }