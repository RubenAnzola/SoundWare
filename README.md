# SoundWave — Documentación técnica

---

## Índice

- [1. Paquete `interfaces`](#1-paquete-interfaces)
- [2. Paquete `enums`](#2-paquete-enums)
- [3. Paquete `excepciones`](#3-paquete-excepciones)
- [4. Paquete `modelo.contenido`](#4-paquete-modelocontenido)
- [5. Paquete `modelo.artistas`](#5-paquete-modeloartistas)
- [6. Paquete `modelo.plataforma`](#6-paquete-modeloplataforma)
- [7. Paquete `modelo.usuarios`](#7-paquete-modelousuarios)
- [8. Paquete `utilidades`](#8-paquete-utilidades)
- [9. Clase `Main` (escenarios de validación)](#9-clase-main-escenarios-de-validación)

---

## 1. Paquete `interfaces`

### 1.1. `Reproducible`

**Propósito:** contrato para contenido que se puede reproducir (control básico de reproducción).

**Métodos:**
- `void play()`
  - Inicia la reproducción del contenido.
- `void pause()`
  - Pausa la reproducción del contenido.
- `void stop()`
  - Detiene completamente la reproducción del contenido.
- `int getDuracion()`
  - Devuelve la duración del contenido en segundos.

---

### 1.2. `Descargable`

**Propósito:** contrato para contenido descargable para uso offline.

**Métodos:**
- `boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException`
  - Descarga el contenido. Debe fallar si supera límites o ya está descargado.
- `boolean eliminarDescarga()`
  - Elimina la descarga existente.
- `int espacioRequerido()`
  - Devuelve el espacio (aproximado) requerido para almacenar la descarga (en MB).

---

### 1.3. `Recomendador`

**Propósito:** contrato del sistema de recomendaciones.

**Métodos:**
- `ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException`
  - Genera recomendaciones personalizadas para el usuario.
- `ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException`
  - Obtiene contenido similar a un contenido dado.

---

## 2. Paquete `enums`

### 2.1. `GeneroMusical`

- POP("Pop", "Música popular contemporánea"),
- ROCK("Rock", "Rock clásico y moderno"),
- HIPHOP("Hip Hop", "Hip hop y rap"),
- JAZZ("Jazz", "Jazz clásico y contemporáneo"),
- ELECTRONICA("Electrónica", "Música electrónica y EDM"),
- REGGAETON("Reggaetón", "Reggaetón y música urbana latina"),
- INDIE("Indie", "Música independiente"),
- CLASICA("Clásica", "Música clásica"),
- COUNTRY("Country", "Música country"),
- METAL("Metal", "Heavy metal y subgéneros"),
- RNB("R&B", "Rhythm and Blues"),
- SOUL("Soul", "Música soul"),
- BLUES("Blues", "Blues clásico y contemporáneo"),
- TRAP("Trap", "Trap y música urbana");

**Atributos (privados):**
- `String nombre`
- `String descripcion`

**Constructor (package-private):**
- `GeneroMusical(String nombre, String descripcion)`

**Métodos:**
- `String getNombre()`
  - Devuelve el nombre legible del género.
- `String getDescripcion()`
  - Devuelve una descripción del género.
- `String toString()` *(override)*
  - Devuelve `nombre`.

---

### 2.2. `CategoriaPodcast`

- TECNOLOGIA("Tecnología", "Podcasts sobre tecnología e innovación"),
- DEPORTES("Deportes", "Podcasts deportivos"),
- COMEDIA("Comedia", "Podcasts de humor y entretenimiento"),
- TRUE_CRIME("True Crime", "Podcasts de crímenes reales"),
- EDUCACION("Educación", "Podcasts educativos"),
- NEGOCIOS("Negocios", "Podcasts de negocios y emprendimiento"),
- SALUD("Salud", "Podcasts de salud y bienestar"),
- ENTRETENIMIENTO("Entretenimiento", "Podcasts de entretenimiento general"),
- HISTORIA("Historia", "Podcasts históricos"),
- CIENCIA("Ciencia", "Podcasts científicos"),
- POLITICA("Política", "Podcasts de política y actualidad"),
- CULTURA("Cultura", "Podcasts culturales");

**Atributos (privados):**
- `String nombre`
- `String descripcion`

**Constructor (package-private):**
- `CategoriaPodcast(String nombre, String descripcion)`

**Métodos:**
- `String getNombre()`
  - Devuelve el nombre legible de la categoría.
- `String getDescripcion()`
  - Devuelve descripción de la categoría.
- `String toString()` *(override)*
  - Devuelve `nombre`.

---

### 2.3. `TipoSuscripcion`

- GRATUITO(0.0, false, 50, false),
- PREMIUM(9.99, true, -1, true),
- FAMILIAR(14.99, true, -1, true),
- ESTUDIANTE(4.99, true, -1, true);

**Atributos (privados):**
- `double precioMensual`
- `boolean sinAnuncios`
- `int limiteReproducciones`
- `boolean descargasOffline`

**Constructor (package-private):**
- `TipoSuscripcion(double precioMensual, boolean sinAnuncios, int limiteReproducciones, boolean descargasOffline)`

**Métodos:**
- `double getPrecioMensual()`
  - Devuelve el precio mensual.
- `boolean isSinAnuncios()`
  - Indica si el plan no incluye anuncios.
- `int getLimiteReproducciones()`
  - Devuelve el límite de reproducciones (o `-1` si es ilimitado).
- `boolean isDescargasOffline()`
  - Indica si permite descargas offline.
- `boolean tieneReproduccionesIlimitadas()`
  - Devuelve `true` si `limiteReproducciones == -1`.
- `String toString()` *(override)*
  - Devuelve representación con nombre y precio.

---

### 2.4. `CriterioOrden`

- FECHA_AGREGADO("Fecha de agregado", "Ordena por fecha en que se agregó"),
- POPULARIDAD("Popularidad", "Ordena por número de reproducciones"),
- DURACION("Duración", "Ordena por duración del contenido"),
- ALFABETICO("Alfabético", "Ordena alfabéticamente por título"),
- ARTISTA("Artista", "Ordena por nombre del artista"),
- ALEATORIO("Aleatorio", "Orden aleatorio");


**Atributos (privados):**
- `String nombre`
- `String descripcion`

**Constructor (package-private):**
- `CriterioOrden(String nombre, String descripcion)`

**Métodos:**
- `String getNombre()`
  - Devuelve el nombre legible del criterio.
- `String getDescripcion()`
  - Devuelve descripción del criterio.
- `String toString()` *(override)*
  - Devuelve `nombre`.

---

### 2.5. `TipoAnuncio`

- AUDIO(15, 0.05),
- BANNER(0, 0.02),
- VIDEO(30, 0.10);

**Atributos (privados):**
- `int duracionSegundos`
- `double costoPorImpresion`

**Constructor (package-private):**
- `TipoAnuncio(int duracionSegundos, double costoPorImpresion)`

**Métodos:**
- `int getDuracionSegundos()`
  - Devuelve duración estándar del anuncio.
- `double getCostoPorImpresion()`
  - Devuelve coste por impresión.
- `String toString()` *(override)*
  - Devuelve nombre y duración.

---

### 2.6. `AlgoritmoRecomendacion`

- COLABORATIVO("Basado en usuarios similares"),
- CONTENIDO("Basado en características del contenido"),
- HIBRIDO("Combinación de ambos");

**Atributos (privados):**
- `String descripcion`

**Constructor (package-private):**
- `AlgoritmoRecomendacion(String descripcion)`

**Métodos:**
- `String getDescripcion()`
  - Devuelve descripción del algoritmo.
- `String toString()` *(override)*
  - Devuelve nombre y descripción.

---

## 3. Paquete `excepciones`

> **Nota:** En este proyecto, todas las excepciones son *checked* y siguen el mismo patrón: **dos constructores** (vacío y con mensaje). No tienen atributos adicionales.

### 3.1. `excepciones.usuario`

- `EmailInvalidoException extends Exception`
  - `EmailInvalidoException()`
  - `EmailInvalidoException(String mensaje)`

- `PasswordDebilException extends Exception`
  - `PasswordDebilException()`
  - `PasswordDebilException(String mensaje)`

- `LimiteDiarioAlcanzadoException extends Exception`
  - `LimiteDiarioAlcanzadoException()`
  - `LimiteDiarioAlcanzadoException(String mensaje)`

- `AnuncioRequeridoException extends Exception`
  - `AnuncioRequeridoException()`
  - `AnuncioRequeridoException(String mensaje)`

---

### 3.2. `excepciones.descarga`

- `LimiteDescargasException extends Exception`
  - `LimiteDescargasException()`
  - `LimiteDescargasException(String mensaje)`

- `ContenidoYaDescargadoException extends Exception`
  - `ContenidoYaDescargadoException()`
  - `ContenidoYaDescargadoException(String mensaje)`

---

### 3.3. `excepciones.contenido`

- `ContenidoNoDisponibleException extends Exception`
  - `ContenidoNoDisponibleException()`
  - `ContenidoNoDisponibleException(String mensaje)`

- `DuracionInvalidaException extends Exception`
  - `DuracionInvalidaException()`
  - `DuracionInvalidaException(String mensaje)`

- `LetraNoDisponibleException extends Exception`
  - `LetraNoDisponibleException()`
  - `LetraNoDisponibleException(String mensaje)`

- `ArchivoAudioNoEncontradoException extends Exception`
  - `ArchivoAudioNoEncontradoException()`
  - `ArchivoAudioNoEncontradoException(String mensaje)`

- `EpisodioNoEncontradoException extends Exception`
  - `EpisodioNoEncontradoException()`
  - `EpisodioNoEncontradoException(String mensaje)`

- `TranscripcionNoDisponibleException extends Exception`
  - `TranscripcionNoDisponibleException()`
  - `TranscripcionNoDisponibleException(String mensaje)`

---

### 3.4. `excepciones.playlist`

- `PlaylistLlenaException extends Exception`
  - `PlaylistLlenaException()`
  - `PlaylistLlenaException(String mensaje)`

- `ContenidoDuplicadoException extends Exception`
  - `ContenidoDuplicadoException()`
  - `ContenidoDuplicadoException(String mensaje)`

- `PlaylistVaciaException extends Exception`
  - `PlaylistVaciaException()`
  - `PlaylistVaciaException(String mensaje)`

- `CancionNoEncontradaException extends Exception`
  - `CancionNoEncontradaException()`
  - `CancionNoEncontradaException(String mensaje)`

---

### 3.5. `excepciones.artista`

- `ArtistaNoVerificadoException extends Exception`
  - `ArtistaNoVerificadoException()`
  - `ArtistaNoVerificadoException(String mensaje)`

- `AlbumYaExisteException extends Exception`
  - `AlbumYaExisteException()`
  - `AlbumYaExisteException(String mensaje)`

- `AlbumCompletoException extends Exception`
  - `AlbumCompletoException()`
  - `AlbumCompletoException(String mensaje)`

- `LimiteEpisodiosException extends Exception`
  - `LimiteEpisodiosException()`
  - `LimiteEpisodiosException(String mensaje)`

---

### 3.6. `excepciones.plataforma`

- `UsuarioYaExisteException extends Exception`
  - `UsuarioYaExisteException()`
  - `UsuarioYaExisteException(String mensaje)`

- `ContenidoNoEncontradoException extends Exception`
  - `ContenidoNoEncontradoException()`
  - `ContenidoNoEncontradoException(String mensaje)`

- `ArtistaNoEncontradoException extends Exception`
  - `ArtistaNoEncontradoException()`
  - `ArtistaNoEncontradoException(String mensaje)`

---

### 3.7. `excepciones.recomendacion`

- `RecomendacionException extends Exception`
  - `RecomendacionException()`
  - `RecomendacionException(String mensaje)`

- `HistorialVacioException extends RecomendacionException`
  - `HistorialVacioException()`
  - `HistorialVacioException(String mensaje)`

- `ModeloNoEntrenadoException extends RecomendacionException`
  - `ModeloNoEntrenadoException()`
  - `ModeloNoEntrenadoException(String mensaje)`

---

## 4. Paquete `modelo.contenido`

### 4.1. `Contenido` *(abstract class)*

**Propósito:** base común para cualquier contenido de la plataforma.

**Atributos (protected):**
- `String id` — Identificador único del contenido.
- `String titulo` — Título del contenido.
- `int reproducciones` — Contador de reproducciones.
- `int likes` — Contador de “me gusta”.
- `int duracionSegundos` — Duración (en segundos).
- `ArrayList<String> tags` — Etiquetas asociadas al contenido.
- `boolean disponible` — Indica si el contenido está disponible.
- `Date fechaPublicacion` — Fecha de publicación.

**Constructores:**
- `Contenido(String titulo, int duracionSegundos) throws DuracionInvalidaException`
  - Inicializa el contenido con identificador único, título, duración y valores por defecto.
  - Debe validar que `duracionSegundos > 0`.

**Métodos abstractos:**
- `void reproducir() throws ContenidoNoDisponibleException`
  - Define la lógica específica de reproducción para un tipo concreto de contenido.
  - Debe fallar si `disponible == false`.

**Métodos concretos:**
- `void aumentarReproducciones()`
  - Incrementa el contador de reproducciones.
- `void agregarLike()`
  - Incrementa el contador de likes.
- `boolean esPopular()`
  - Devuelve `true` si `reproducciones > 100000`.
- `void validarDuracion() throws DuracionInvalidaException`
  - Verifica que la duración sea positiva.
- `void agregarTag(String tag)`
  - Añade un tag válido, evitando duplicados.
- `boolean tieneTag(String tag)`
  - Indica si el contenido contiene ese tag.
- `void marcarNoDisponible()`
  - Marca el contenido como no disponible.
- `void marcarDisponible()`
  - Marca el contenido como disponible.
- `String getDuracionFormateada()`
  - Devuelve la duración formateada en `M:SS`.

**Getters/Setters:**
- `String getId()`
- `String getTitulo()`
- `void setTitulo(String titulo)`
- `int getReproducciones()`
- `void setReproducciones(int reproducciones)`
- `int getLikes()`
- `int getDuracionSegundos()`
- `ArrayList<String> getTags()` *(copia defensiva)*
- `boolean isDisponible()`
- `Date getFechaPublicacion()`
- `void setFechaPublicacion(Date fechaPublicacion)`

**Overrides:**
- `String toString()` — Representación en texto del contenido.
- `boolean equals(Object obj)` — Compara por `id`.
- `int hashCode()` — Basado en `id`.

---

### 4.2. `Cancion` *(class)*

**Herencia / Interfaces:**
- `extends Contenido`
- `implements Reproducible, Descargable`

**Atributos (private):**
- `String letra` — Letra de la canción.
- `Artista artista` — Artista asociado.
- `Album album` — Álbum asociado.
- `GeneroMusical genero` — Género musical.
- `String audioURL` — URL del archivo de audio.
- `boolean explicit` — Indica si es explícita.
- `String ISRC` — Código ISRC.
- `boolean reproduciendo` — Estado interno.
- `boolean pausado` — Estado interno.
- `boolean descargado` — Estado interno.

**Constructores:**
- `Cancion(String titulo, int duracionSegundos, Artista artista, GeneroMusical genero) throws DuracionInvalidaException`
  - Crea una canción básica con URL por defecto e ISRC generado.
- `Cancion(String titulo, int duracionSegundos, Artista artista, GeneroMusical genero, String letra, boolean explicit) throws DuracionInvalidaException`
  - Crea una canción incluyendo letra y flag de explícito.

**Métodos privados:**
- `String generarISRC()`
  - Genera un identificador ISRC.

**Overrides (Contenido):**
- `void reproducir() throws ContenidoNoDisponibleException`
  - Debe comprobar disponibilidad, ejecutar reproducción y aumentar reproducciones.

**Implementación interfaz `Reproducible`:**
- `void play()`
  - Debe marcar estados de reproducción y mostrar información.
- `void pause()`
  - Debe pausar solo si está reproduciendo.
- `void stop()`
  - Debe detener y resetear estados.
- `int getDuracion()`
  - Devuelve `duracionSegundos`.

**Implementación interfaz `Descargable`:**
- `boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException`
  - Debe marcar como descargado o lanzar excepción si ya lo está.
- `boolean eliminarDescarga()`
  - Debe eliminar el estado descargado si existía.
- `int espacioRequerido()`
  - Devuelve una aproximación del espacio necesario.

**Métodos propios:**
- `String obtenerLetra() throws LetraNoDisponibleException`
  - Devuelve la letra o lanza excepción si no existe.
- `boolean esExplicit()`
  - Indica si es explícita.
- `void cambiarGenero(GeneroMusical nuevoGenero)`
  - Cambia el género.
- `void validarAudioURL() throws ArchivoAudioNoEncontradoException`
  - Valida que exista una URL de audio.

**Getters/Setters:**
- `String getLetra()` / `void setLetra(String letra)`
- `Artista getArtista()` / `void setArtista(Artista artista)`
- `Album getAlbum()` / `void setAlbum(Album album)`
- `GeneroMusical getGenero()` / `void setGenero(GeneroMusical genero)`
- `String getAudioURL()` / `void setAudioURL(String audioURL)`
- `boolean isExplicit()` / `void setExplicit(boolean explicit)`
- `String getISRC()`
- `boolean isReproduciendo()`
- `boolean isPausado()`
- `boolean isDescargado()` / `void setDescargado(boolean descargado)`

**Overrides:**
- `String toString()` — Representación legible (incluye duración y artista).

---

### 4.3. `Podcast` *(class)*

**Herencia / Interfaces:**
- `extends Contenido`
- `implements Reproducible, Descargable`

**Atributos (private):**
- `Creador creador`
- `int numeroEpisodio`
- `int temporada`
- `String descripcion`
- `CategoriaPodcast categoria`
- `ArrayList<String> invitados`
- `String transcripcion`
- `boolean reproduciendo`
- `boolean pausado`
- `boolean descargado`

**Constructores:**
- `Podcast(String titulo, int duracionSegundos, Creador creador, int numeroEpisodio, int temporada, CategoriaPodcast categoria) throws DuracionInvalidaException`
- `Podcast(String titulo, int duracionSegundos, Creador creador, int numeroEpisodio, int temporada, CategoriaPodcast categoria, String descripcion) throws DuracionInvalidaException`

**Overrides (Contenido):**
- `void reproducir() throws ContenidoNoDisponibleException`
  - Debe comprobar disponibilidad, reproducir y aumentar reproducciones.

**Implementación interfaz `Reproducible`:**
- `void play()`
- `void pause()`
- `void stop()`
- `int getDuracion()`

**Implementación interfaz `Descargable`:**
- `boolean descargar() throws LimiteDescargasException, ContenidoYaDescargadoException`
- `boolean eliminarDescarga()`
- `int espacioRequerido()`

**Métodos propios:**
- `String obtenerDescripcion()`
  - Devuelve una descripción legible.
- `void agregarInvitado(String nombre)`
  - Añade un invitado evitando duplicados.
- `boolean esTemporadaNueva()`
  - Indica si se considera temporada “nueva”.
- `String obtenerTranscripcion() throws TranscripcionNoDisponibleException`
  - Devuelve transcripción o lanza excepción.
- `void validarEpisodio() throws EpisodioNoEncontradoException`
  - Valida que episodio/temporada sean válidos.

**Getters/Setters:**
- `Creador getCreador()` / `void setCreador(Creador creador)`
- `int getNumeroEpisodio()` / `void setNumeroEpisodio(int numeroEpisodio)`
- `int getTemporada()` / `void setTemporada(int temporada)`
- `String getDescripcion()` / `void setDescripcion(String descripcion)`
- `CategoriaPodcast getCategoria()` / `void setCategoria(CategoriaPodcast categoria)`
- `ArrayList<String> getInvitados()` *(copia defensiva)*
- `String getTranscripcion()` / `void setTranscripcion(String transcripcion)`
- `boolean isReproduciendo()`
- `boolean isPausado()`
- `boolean isDescargado()` / `void setDescargado(boolean descargado)`

**Overrides:**
- `String toString()`

---

## 5. Paquete `modelo.artistas`

### 5.1. `Artista`

**Atributos (private):**
- `String id`
- `String nombreArtistico`
- `String nombreReal`
- `String paisOrigen`
- `ArrayList<Cancion> discografia`
- `ArrayList<Album> albumes`
- `int oyentesMensuales`
- `boolean verificado`
- `String biografia`

**Constructores:**
- `Artista(String nombreArtistico, String nombreReal, String paisOrigen)`
- `Artista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado, String biografia)`

**Métodos:**
- `void publicarCancion(Cancion cancion)`
  - Añade una canción a la discografía del artista.
- `Album crearAlbum(String titulo, Date fecha) throws ArtistaNoVerificadoException, AlbumYaExisteException`
  - Crea un álbum si el artista está verificado y no hay duplicados.
- `ArrayList<Cancion> obtenerTopCanciones(int cantidad)`
  - Devuelve las canciones más reproducidas hasta un máximo.
- `double calcularPromedioReproducciones()`
  - Devuelve promedio de reproducciones de su discografía.
- `boolean esVerificado()`
  - Indica si está verificado.
- `int getTotalReproducciones()`
  - Suma reproducciones de toda la discografía.
- `void verificar()`
  - Marca al artista como verificado.
- `void incrementarOyentes()`
  - Incrementa oyentes mensuales.

**Getters/Setters:**
- `String getId()`
- `String getNombreArtistico()` / `void setNombreArtistico(String nombreArtistico)`
- `String getNombreReal()` / `void setNombreReal(String nombreReal)`
- `String getPaisOrigen()` / `void setPaisOrigen(String paisOrigen)`
- `ArrayList<Cancion> getDiscografia()` *(copia defensiva)*
- `ArrayList<Album> getAlbumes()` *(copia defensiva)*
- `int getOyentesMensuales()` / `void setOyentesMensuales(int oyentesMensuales)`
- `boolean isVerificado()` / `void setVerificado(boolean verificado)`
- `String getBiografia()` / `void setBiografia(String biografia)`

**Overrides:**
- `String toString()`
- `boolean equals(Object obj)`
- `int hashCode()`

---

### 5.2. `Album`

**Relación:** Composición con `Cancion` (el álbum crea y gestiona sus canciones).

**Atributos (private):**
- `String id`
- `String titulo`
- `Artista artista`
- `Date fechaLanzamiento`
- `ArrayList<Cancion> canciones`
- `String portadaURL`
- `String discografica`
- `String tipoAlbum`

**Constantes (private static final):**
- `int MAX_CANCIONES = 20`

**Constructores:**
- `Album(String titulo, Artista artista, Date fechaLanzamiento)`
- `Album(String titulo, Artista artista, Date fechaLanzamiento, String discografica, String tipoAlbum)`

**Métodos (composición / creación):**
- `Cancion crearCancion(String titulo, int duracionSegundos, GeneroMusical genero) throws AlbumCompletoException, DuracionInvalidaException`
  - Crea una canción y la agrega al álbum.
- `Cancion crearCancion(String titulo, int duracionSegundos, GeneroMusical genero, String letra, boolean explicit) throws AlbumCompletoException, DuracionInvalidaException`
  - Crea canción y establece letra/explicit.

**Métodos (gestión):**
- `void eliminarCancion(int posicion) throws CancionNoEncontradaException`
  - Elimina canción por posición (1-based).
- `void eliminarCancion(Cancion cancion) throws CancionNoEncontradaException`
  - Elimina canción por referencia.
- `int getDuracionTotal()`
  - Suma total de duración.
- `String getDuracionTotalFormateada()`
  - Duración total formateada.
- `int getNumCanciones()`
  - Número de canciones.
- `void ordenarPorPopularidad()`
  - Ordena por reproducciones.
- `Cancion getCancion(int posicion) throws CancionNoEncontradaException`
  - Obtiene canción por posición (1-based).
- `int getTotalReproducciones()`
  - Suma de reproducciones.

**Getters/Setters:**
- `String getId()`
- `String getTitulo()` / `void setTitulo(String titulo)`
- `Artista getArtista()` / `void setArtista(Artista artista)`
- `Date getFechaLanzamiento()` / `void setFechaLanzamiento(Date fechaLanzamiento)`
- `ArrayList<Cancion> getCanciones()` *(copia defensiva)*
- `String getPortadaURL()` / `void setPortadaURL(String portadaURL)`
- `String getDiscografica()` / `void setDiscografica(String discografica)`
- `String getTipoAlbum()` / `void setTipoAlbum(String tipoAlbum)`
- `int getMaxCanciones()`

**Overrides:**
- `String toString()`
- `boolean equals(Object obj)`
- `int hashCode()`

---

### 5.3. `Creador`

**Atributos (private):**
- `String id`
- `String nombreCanal`
- `String nombre`
- `ArrayList<Podcast> episodios`
- `int suscriptores`
- `String descripcion`
- `HashMap<String, String> redesSociales`
- `ArrayList<CategoriaPodcast> categoriasPrincipales`

**Constantes (private static final):**
- `int MAX_EPISODIOS = 500`

**Constructores:**
- `Creador(String nombreCanal, String nombre)`
- `Creador(String nombreCanal, String nombre, String descripcion)`

**Métodos:**
- `void publicarPodcast(Podcast episodio) throws LimiteEpisodiosException`
  - Publica un episodio y lo registra dentro del creador.
- `EstadisticasCreador obtenerEstadisticas()`
  - Genera estadísticas del creador.
- `void agregarRedSocial(String red, String usuario)`
  - Añade entrada a `redesSociales`.
- `double calcularPromedioReproducciones()`
  - Promedio de reproducciones de episodios.
- `void eliminarEpisodio(String idEpisodio) throws EpisodioNoEncontradoException`
  - Elimina un episodio por ID.
- `int getTotalReproducciones()`
  - Total reproducciones del canal.
- `void incrementarSuscriptores()`
  - Incrementa suscriptores.
- `ArrayList<Podcast> obtenerTopEpisodios(int cantidad)`
  - Devuelve episodios más populares.
- `int getUltimaTemporada()`
  - Devuelve el número de temporada máximo.

**Getters/Setters:**
- `String getId()`
- `String getNombreCanal()` / `void setNombreCanal(String nombreCanal)`
- `String getNombre()` / `void setNombre(String nombre)`
- `ArrayList<Podcast> getEpisodios()` *(copia defensiva)*
- `int getSuscriptores()` / `void setSuscriptores(int suscriptores)`
- `String getDescripcion()` / `void setDescripcion(String descripcion)`
- `HashMap<String, String> getRedesSociales()` *(copia defensiva)*
- `ArrayList<CategoriaPodcast> getCategoriasPrincipales()` *(copia defensiva)*
- `int getNumEpisodios()`

**Overrides:**
- `String toString()`
- `boolean equals(Object obj)`
- `int hashCode()`

---

## 6. Paquete `modelo.plataforma`

### 6.1. `Anuncio`

**Atributos (private):**
- `String id`
- `String empresa`
- `int duracionSegundos`
- `String audioURL`
- `TipoAnuncio tipo`
- `int impresiones`
- `double presupuesto`
- `boolean activo`

**Constructores:**
- `Anuncio(String empresa, TipoAnuncio tipo, double presupuesto)`
- `Anuncio(String empresa, TipoAnuncio tipo, double presupuesto, String audioURL)`

**Métodos:**
- `void reproducir()`
  - Reproduce el anuncio si está activo.
- `void registrarImpresion()`
  - Incrementa impresiones y desactiva si agota presupuesto.
- `double calcularCostoPorImpresion()`
  - Devuelve coste por impresión del tipo.
- `double calcularCostoTotal()`
  - Devuelve coste acumulado.
- `int calcularImpresionesRestantes()`
  - Estima impresiones restantes según presupuesto.
- `void desactivar()`
  - Marca anuncio inactivo.
- `void activar()`
  - Marca anuncio activo.
- `boolean puedeMostrarse()`
  - Indica si está activo y con presupuesto.

**Getters/Setters:**
- `String getId()`
- `String getEmpresa()` / `void setEmpresa(String empresa)`
- `int getDuracionSegundos()` / `void setDuracionSegundos(int duracionSegundos)`
- `String getAudioURL()` / `void setAudioURL(String audioURL)`
- `TipoAnuncio getTipo()` / `void setTipo(TipoAnuncio tipo)`
- `int getImpresiones()`
- `double getPresupuesto()` / `void setPresupuesto(double presupuesto)`
- `boolean isActivo()` / `void setActivo(boolean activo)`

**Overrides:**
- `String toString()`
- `boolean equals(Object obj)`
- `int hashCode()`

---

### 6.2. `Playlist`

**Relación:** Agregación con `Contenido`.

**Atributos (private):**
- `String id`
- `String nombre`
- `Usuario creador`
- `ArrayList<Contenido> contenidos`
- `boolean esPublica`
- `int seguidores`
- `String descripcion`
- `String portadaURL`
- `Date fechaCreacion`
- `int maxContenidos`

**Constantes (private static final):**
- `int MAX_CONTENIDOS_DEFAULT = 500`

**Constructores:**
- `Playlist(String nombre, Usuario creador)`
- `Playlist(String nombre, Usuario creador, boolean esPublica, String descripcion)`

**Métodos:**
- `void agregarContenido(Contenido contenido) throws PlaylistLlenaException, ContenidoDuplicadoException`
  - Añade contenido validando límite y duplicados.
- `boolean eliminarContenido(String idContenido)`
  - Elimina contenido por ID.
- `boolean eliminarContenido(Contenido contenido)`
  - Elimina contenido por referencia.
- `void ordenarPor(CriterioOrden criterio) throws PlaylistVaciaException`
  - Ordena la playlist según criterio.
- `int getDuracionTotal()`
  - Suma duración de los contenidos.
- `String getDuracionTotalFormateada()`
  - Devuelve duración total legible.
- `void shuffle()`
  - Mezcla aleatoriamente el orden.
- `ArrayList<Contenido> buscarContenido(String termino)`
  - Busca contenidos por coincidencia en título.
- `void hacerPublica()`
  - Cambia la visibilidad a pública.
- `void hacerPrivada()`
  - Cambia la visibilidad a privada.
- `void incrementarSeguidores()`
  - Incrementa seguidores.
- `void decrementarSeguidores()`
  - Decrementa seguidores (sin bajar de 0).
- `int getNumContenidos()`
  - Devuelve número de contenidos.
- `boolean estaVacia()`
  - Indica si no tiene contenidos.
- `Contenido getContenido(int posicion)`
  - Devuelve el contenido en índice (0-based) o `null`.

**Getters/Setters:**
- `String getId()`
- `String getNombre()` / `void setNombre(String nombre)`
- `Usuario getCreador()`
- `ArrayList<Contenido> getContenidos()` *(copia defensiva)*
- `boolean isEsPublica()` / `void setEsPublica(boolean esPublica)`
- `int getSeguidores()` / `void setSeguidores(int seguidores)`
- `String getDescripcion()` / `void setDescripcion(String descripcion)`
- `String getPortadaURL()` / `void setPortadaURL(String portadaURL)`
- `Date getFechaCreacion()`
- `int getMaxContenidos()`

**Overrides:**
- `String toString()`
- `boolean equals(Object obj)`
- `int hashCode()`

---

### 6.3. `Plataforma` *(Singleton)*

**Propósito:** clase principal del sistema. **Única responsable** de gestionar colecciones del modelo.

**Atributos (private):**
- `static Plataforma instancia`
- `String nombre`
- `HashMap<String, Usuario> usuarios`
- `HashMap<String, Usuario> usuariosPorEmail`
- `ArrayList<Contenido> catalogo`
- `ArrayList<Playlist> playlistsPublicas`
- `HashMap<String, Artista> artistas`
- `HashMap<String, Creador> creadores`
- `ArrayList<Album> albumes`
- `ArrayList<Anuncio> anuncios`
- `RecomendadorIA recomendador`
- `int totalAnunciosReproducidos`

**Constructor (private):**
- `Plataforma(String nombre)`
  - Inicializa colecciones y anuncios, y configura el recomendador.

**Métodos Singleton:**
- `static synchronized Plataforma getInstancia(String nombre)`
  - Devuelve/crea instancia única.
- `static synchronized Plataforma getInstancia()`
  - Devuelve instancia con nombre por defecto.
- `static synchronized void reiniciarInstancia()`
  - Reinicia la instancia (útil para pruebas).

**Gestión de usuarios:**
- `UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password, TipoSuscripcion tipo) throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException`
  - Registra usuario premium garantizando email único.
- `UsuarioPremium registrarUsuarioPremium(String nombre, String email, String password) throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException`
  - Registra premium por defecto.
- `UsuarioGratuito registrarUsuarioGratuito(String nombre, String email, String password) throws UsuarioYaExisteException, EmailInvalidoException, PasswordDebilException`
  - Registra usuario gratuito.
- `ArrayList<UsuarioPremium> getUsuariosPremium()`
  - Devuelve todos los premium.
- `ArrayList<UsuarioGratuito> getUsuariosGratuitos()`
  - Devuelve todos los gratuitos.
- `ArrayList<Usuario> getTodosLosUsuarios()`
  - Devuelve todos los usuarios.
- `Usuario buscarUsuarioPorEmail(String email)`
  - Devuelve usuario asociado al email.

**Gestión de artistas:**
- `Artista registrarArtista(String nombreArtistico, String nombreReal, String paisOrigen, boolean verificado)`
  - Crea y registra artista.
- `void registrarArtista(Artista artista)`
  - Registra artista existente.
- `ArrayList<Artista> getArtistasVerificados()`
- `ArrayList<Artista> getArtistasNoVerificados()`
- `Artista buscarArtista(String nombre) throws ArtistaNoEncontradoException`

**Gestión de álbumes:**
- `Album crearAlbum(Artista artista, String titulo, Date fecha) throws ArtistaNoVerificadoException, AlbumYaExisteException`
- `ArrayList<Album> getAlbumes()`

**Gestión de canciones:**
- `Cancion crearCancion(String titulo, int duracion, Artista artista, GeneroMusical genero) throws DuracionInvalidaException`
  - Crea canción independiente.
- `Cancion crearCancionEnAlbum(String titulo, int duracion, Artista artista, GeneroMusical genero, Album album) throws DuracionInvalidaException, AlbumCompletoException`
  - Delegación al álbum (composición).
- `void agregarContenidoCatalogo(Contenido contenido)`
- `ArrayList<Cancion> getCanciones()`

**Gestión de creadores/podcasts:**
- `Creador registrarCreador(String nombreCanal, String nombre, String descripcion)`
- `void registrarCreador(Creador creador)`
- `Podcast crearPodcast(String titulo, int duracion, Creador creador, int numEpisodio, int temporada, CategoriaPodcast categoria) throws DuracionInvalidaException, LimiteEpisodiosException`
- `ArrayList<Podcast> getPodcasts()`
- `ArrayList<Creador> getTodosLosCreadores()`

**Gestión de playlists públicas:**
- `Playlist crearPlaylistPublica(String nombre, Usuario creador)`
- `ArrayList<Playlist> getPlaylistsPublicas()`

**Búsquedas:**
- `ArrayList<Contenido> buscarContenido(String termino) throws ContenidoNoEncontradoException`
- `ArrayList<Cancion> buscarPorGenero(GeneroMusical genero) throws ContenidoNoEncontradoException`
- `ArrayList<Podcast> buscarPorCategoria(CategoriaPodcast categoria) throws ContenidoNoEncontradoException`
- `ArrayList<Contenido> obtenerTopContenidos(int cantidad)`

**Anuncios:**
- `Anuncio obtenerAnuncioAleatorio()`
  - Devuelve anuncio activo aleatorio.
- `void incrementarAnunciosReproducidos()`

**Estadísticas:**
- `String obtenerEstadisticasGenerales()`
  - Genera reporte de estadísticas globales.

**Getters básicos:**
- `String getNombre()`
- `ArrayList<Contenido> getCatalogo()` *(copia defensiva)*
- `HashMap<String, Artista> getArtistas()` *(copia defensiva)*
- `HashMap<String, Creador> getCreadores()` *(copia defensiva)*
- `ArrayList<Anuncio> getAnuncios()` *(copia defensiva)*
- `RecomendadorIA getRecomendador()`
- `int getTotalUsuarios()`
- `int getTotalContenido()`
- `int getTotalAnunciosReproducidos()`

**Overrides:**
- `String toString()`

---

## 7. Paquete `modelo.usuarios`

### 7.1. `Usuario` *(abstract class)*

**Atributos (protected):**
- `String id`
- `String nombre`
- `String email`
- `String password`
- `TipoSuscripcion suscripcion`
- `ArrayList<Playlist> misPlaylists`
- `ArrayList<Contenido> historial`
- `Date fechaRegistro`
- `ArrayList<Playlist> playlistsSeguidas`
- `ArrayList<Contenido> contenidosLiked`

**Constructor:**
- `Usuario(String nombre, String email, String password, TipoSuscripcion suscripcion) throws EmailInvalidoException, PasswordDebilException`
  - Inicializa el usuario validando email y password.

**Método abstracto:**
- `void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException`
  - Reproduce contenido según tipo de usuario.

**Métodos concretos:**
- `Playlist crearPlaylist(String nombrePlaylist)`
  - Crea y registra una playlist privada del usuario.
- `void seguirPlaylist(Playlist playlist)`
  - Sigue playlist pública.
- `void dejarDeSeguirPlaylist(Playlist playlist)`
  - Deja de seguir playlist.
- `void darLike(Contenido contenido)`
  - Da like y registra el contenido en favoritos.
- `void quitarLike(Contenido contenido)`
  - Quita el like.
- `boolean validarEmail() throws EmailInvalidoException`
  - Valida el email del usuario.
- `boolean validarPassword() throws PasswordDebilException`
  - Valida el password del usuario.
- `void agregarAlHistorial(Contenido contenido)`
  - Añade contenido al historial (con límite de tamaño).
- `void limpiarHistorial()`
  - Limpia el historial.
- `boolean esPremium()`
  - Indica si no es gratuito.

**Getters/Setters:**
- `String getId()`
- `String getNombre()` / `void setNombre(String nombre)`
- `String getEmail()` / `void setEmail(String email) throws EmailInvalidoException`
- `String getPassword()` / `void setPassword(String password) throws PasswordDebilException`
- `TipoSuscripcion getSuscripcion()` / `void setSuscripcion(TipoSuscripcion suscripcion)`
- `ArrayList<Playlist> getMisPlaylists()` *(copia defensiva)*
- `ArrayList<Contenido> getHistorial()` *(copia defensiva)*
- `Date getFechaRegistro()`
- `ArrayList<Playlist> getPlaylistsSeguidas()` *(copia defensiva)*
- `ArrayList<Contenido> getContenidosLiked()` *(copia defensiva)*

**Overrides:**
- `String toString()`
- `boolean equals(Object obj)`
- `int hashCode()`

---

### 7.2. `UsuarioPremium`

**Herencia:** `extends Usuario`

**Atributos (private):**
- `boolean descargasOffline`
- `int maxDescargas`
- `ArrayList<Contenido> descargados`
- `String calidadAudio`

**Constantes (private static final):**
- `int MAX_DESCARGAS_DEFAULT = 100`

**Constructores:**
- `UsuarioPremium(String nombre, String email, String password) throws EmailInvalidoException, PasswordDebilException`
- `UsuarioPremium(String nombre, String email, String password, TipoSuscripcion suscripcion) throws EmailInvalidoException, PasswordDebilException`

**Overrides:**
- `void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException`
  - Reproduce sin anuncios ni límite diario.

**Métodos propios:**
- `void descargar(Contenido contenido) throws LimiteDescargasException, ContenidoYaDescargadoException`
  - Registra descarga en lista local del usuario.
- `boolean eliminarDescarga(Contenido contenido)`
  - Elimina una descarga existente.
- `boolean verificarEspacioDescarga()`
  - Indica si quedan huecos.
- `int getDescargasRestantes()`
  - Cuántas descargas quedan.
- `void cambiarCalidadAudio(String calidad)`
  - Cambia calidad si es válida.
- `void limpiarDescargas()`
  - Vacía la lista.

**Getters/Setters:**
- `boolean isDescargasOffline()` / `void setDescargasOffline(boolean descargasOffline)`
- `int getMaxDescargas()`
- `ArrayList<Contenido> getDescargados()` *(copia defensiva)*
- `int getNumDescargados()`
- `String getCalidadAudio()` / `void setCalidadAudio(String calidadAudio)`

**Overrides:**
- `String toString()`

---

### 7.3. `UsuarioGratuito`

**Herencia:** `extends Usuario`

**Atributos (private):**
- `int anunciosEscuchados`
- `Date ultimoAnuncio`
- `int reproduccionesHoy`
- `int limiteReproducciones`
- `int cancionesSinAnuncio`
- `Date fechaUltimaReproduccion`

**Constantes (private static final):**
- `int LIMITE_DIARIO = 50`
- `int CANCIONES_ENTRE_ANUNCIOS = 3`

**Constructor:**
- `UsuarioGratuito(String nombre, String email, String password) throws EmailInvalidoException, PasswordDebilException`

**Overrides:**
- `void reproducir(Contenido contenido) throws ContenidoNoDisponibleException, LimiteDiarioAlcanzadoException, AnuncioRequeridoException`
  - Aplica límite diario y lógica de anuncios.

**Métodos propios:**
- `void verAnuncio()`
  - Muestra un anuncio genérico aleatorio.
- `void verAnuncio(Anuncio anuncio)`
  - Reproduce un anuncio específico o genérico si es null.
- `boolean puedeReproducir()`
  - Indica si quedan reproducciones.
- `boolean debeVerAnuncio()`
  - Indica si toca ver anuncio.
- `void reiniciarContadorDiario()`
  - Reinicia el contador diario.
- `int getReproduccionesRestantes()`
  - Cuántas reproducciones quedan.
- `int getCancionesHastaAnuncio()`
  - Cuántas canciones faltan.

**Getters/Setters:**
- `int getAnunciosEscuchados()`
- `Date getUltimoAnuncio()`
- `int getReproduccionesHoy()` / `void setReproduccionesHoy(int reproduccionesHoy)`
- `int getLimiteReproducciones()`
- `int getCancionesSinAnuncio()` / `void setCancionesSinAnuncio(int cancionesSinAnuncio)`

**Overrides:**
- `String toString()`

---

## 8. Paquete `utilidades`

### 8.1. `RecomendadorIA` *(implements Recomendador)*

**Atributos (private):**
- `HashMap<String, ArrayList<String>> matrizPreferencias`
- `HashMap<String, ArrayList<Contenido>> historialCompleto`
- `AlgoritmoRecomendacion algoritmo`
- `double umbralSimilitud`
- `boolean modeloEntrenado`
- `ArrayList<Contenido> catalogoReferencia`

**Constantes (private static final):**
- `double UMBRAL_DEFAULT = 0.6`

**Constructores:**
- `RecomendadorIA()`
- `RecomendadorIA(AlgoritmoRecomendacion algoritmo)`

**Implementación de `Recomendador` (overrides):**
- `ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException`
  - Genera recomendaciones (requiere modelo entrenado y usuario con historial).
- `ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException`
  - Devuelve similares según coincidencia de género/categoría.

**Métodos propios:**
- `void entrenarModelo(ArrayList<Usuario> usuarios)`
  - Construye el modelo a partir de usuarios.
- `void entrenarModelo(ArrayList<Usuario> usuarios, ArrayList<Contenido> catalogo)`
  - Construye el modelo y fija catálogo de referencia.
- `double calcularSimilitud(Usuario u1, Usuario u2)`
  - Calcula similitud entre usuarios.
- `void actualizarPreferencias(Usuario usuario)`
  - Actualiza preferencias del usuario según historial.
- `HashMap<String, Integer> obtenerGenerosPopulares()`
  - Cuenta preferencias globales.

**Método privado:**
- `double calcularSimilitudContenido(Contenido contenido, ArrayList<String> preferencias)`
  - Calcula similitud de un contenido frente a preferencias.

**Getters/Setters:**
- `AlgoritmoRecomendacion getAlgoritmo()` / `void setAlgoritmo(AlgoritmoRecomendacion algoritmo)`
- `double getUmbralSimilitud()` / `void setUmbralSimilitud(double umbralSimilitud)`
- `boolean isModeloEntrenado()`
- `HashMap<String, ArrayList<String>> getMatrizPreferencias()` *(copia defensiva)*
- `void setCatalogoReferencia(ArrayList<Contenido> catalogo)`

---

### 8.2. `EstadisticasCreador`

**Atributos (private):**
- `Creador creador`
- `int totalEpisodios`
- `int totalReproducciones`
- `double promedioReproducciones`
- `int totalSuscriptores`
- `int totalLikes`
- `int duracionTotalSegundos`
- `Podcast episodioMasPopular`
- `HashMap<Integer, Integer> episodiosPorTemporada`

**Constructor:**
- `EstadisticasCreador(Creador creador)`
  - Inicializa y calcula estadísticas.

**Métodos privados:**
- `void calcularEstadisticas()`
  - Calcula métricas internas.
- `String formatearDuracion(int segundos)`
  - Convierte duración a texto.

**Métodos públicos:**
- `String generarReporte()`
  - Genera un informe de estadísticas.
- `double calcularEngagement()`
  - Engagement como porcentaje.
- `int estimarCrecimientoMensual()`
  - Estimación de crecimiento basada en métricas.

**Getters:**
- `Creador getCreador()`
- `int getTotalEpisodios()`
- `int getTotalReproducciones()`
- `double getPromedioReproducciones()`
- `int getTotalSuscriptores()`
- `int getTotalLikes()`
- `int getDuracionTotalSegundos()`
- `Podcast getEpisodioMasPopular()`
- `HashMap<Integer, Integer> getEpisodiosPorTemporada()` *(copia defensiva)*

**Overrides:**
- `String toString()`

---
