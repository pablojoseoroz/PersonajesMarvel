
# Personaje Marvel

Se solicita la creación de la estructura de una app que muestre un listado de los personajes Marvel y permite ver el detalle de cada uno de ellos de manera individual.




## API Marvel
Implementadas la siguientes llamadas y sus correspondientes respuestas

#### Obtener todos los personajes mediante paginación

```http
  GET /v1/public/characters
```

#### Obtener el detalle de un personaje

```http
  GET /v1/public/characters/{id}
```

## Screenshots
<p float="left">
  <img src="https://github.com/pablojoseoroz/PersonajesMarvel/blob/32092a91811b2ea920ed963c15a2ecc0dd78fcbf/captures/splash.png" width="33%" />
  <img src="https://github.com/pablojoseoroz/PersonajesMarvel/blob/32092a91811b2ea920ed963c15a2ecc0dd78fcbf/captures/home.png" width="33%" /> 
  <img src="https://github.com/pablojoseoroz/PersonajesMarvel/blob/32092a91811b2ea920ed963c15a2ecc0dd78fcbf/captures/detail.png" width="33%" />
</p>


## Documentation

La aplicación está escrita en lenguaje Kotlin y utiliza la arquitectura MVVM.

Se ha dividido la aplicación en módulos:
- marvelapi: módulo encargado de las conexiones con la API de marvel. Utiliza para ello OkHttp + Retrofit. Se ha optado por un interceptor para añadir las queries correspondientes a la identificación del usuario.
- navigation: módulo customizado de navegación para que mantenga el fragment anterior en la pila y no resuma al volver atrás.
- uibase: módulo base para la interfaz. Contiene clases base para facilitar la implementación de las pantallas y no tener que reescribir código.

Existen dos ViewModel encargados de la lógica y los casos de uso, uno para comunicarse con la API de Marvel [MarvelViewModel](https://github.com/pablojoseoroz/PersonajesMarvel/blob/ecab42fd324d75507b0d06751a1e56d6ba71ec25/marvelapi/src/main/java/com/pablojoseoroz/marvelapi/MarvelViewModel.kt) y el otro para encargarse de los favoritos [FavoriteViewModel](https://github.com/pablojoseoroz/PersonajesMarvel/blob/ecab42fd324d75507b0d06751a1e56d6ba71ec25/app/src/main/java/com/pablojoseoroz/marvel/ui/detail/FavoriteViewModel.kt).

La aplicación contiene una única [Activity](https://github.com/pablojoseoroz/PersonajesMarvel/blob/6da38ae46fcda3e4cb880842e49e647a0e0cddf1/app/src/main/java/com/pablojoseoroz/marvel/ui/MainActivity.kt) la cual tiene asociada un NavHostFragment. A su vez ésta contiene la única Toolbar que es controlada por NavigationUI.

Se han utilizado las siguientes librerías:
- [Timber](https://github.com/JakeWharton/timber): para facilitar los logs
- [Glide](https://github.com/bumptech/glide): para facilitar el mostrado de imágenes; está customizado para que use el cliente OkHttp y diferentes configuraciones.
- [EventBus](https://github.com/greenrobot/EventBus): para enviar eventos entre pantallas; en este caso se ha utilizado para facilitar la sincronización de favoritos.
- [Room](https://developer.android.com/training/data-storage/room): para crear la base de datos local donde guardar los favoritos
- [Palette](https://developer.android.com/training/material/palette-colors): para obtener los colores principales de una imagen y colorear con ellos otras vistas dinámicamente
- [Retrofit](https://github.com/square/retrofit): para facilitar la implementación de la API de Marvel
- [OkHttp](https://github.com/square/okhttp): para realizar las llamadas web
- [OkHttpProfiler](https://github.com/itkacher/OkHttpProfiler): para ver las llamadas hechas en la API en tiempo de desarrollo
- [Navigation](https://developer.android.com/guide/navigation): para la navegación entre pantallas




## Demo

El funcionamiento básico de la app es el siguiente:
- Durante 1 seg se muestra la pantalla de carga indicando el copyright del contenido
- Después navega al listado de personajes Marvel, donde se puede modificar meediante un botón en la Toolbar el modo en que se visualizan, de forma linear o en forma de grid de 3 columnas, dependinedo el modo se utiliza una vista u otra.
- Se ha optado por una paginación simple en donde se detecta al scrollear la lista cuanto falta para llegar al final, en cuyo caso manda cargar otros tanto personajes.
- Para cada personaje se muestra su foto, su nombre y la cantidad de cómics, series,  historias y eventos en los que ha participado.
- Se utiliza Palette para extraer de la imagen su color principal y aplicarlo al borde de la card del item, así como a los chips donde se muestra las cantidades.
- Se cargan a su vez los personajes favoritos que estén alojados en la base de datos de Room, y en caso de que un personaje sea favorito se muestra un corazón en su item.
- Al pinchar en un personaje, se navega a su detalle, mostrando su foto en grande, nombre, descripción y un tablayout en el cual se listan los nombres de los cómics, series, historias y eventos en los que ha participado.
- En la pantalla de detalle, existen las acciones de hacer o deshacer un personaje como favorito, lo cual enviará un evento al listado para que se actualice utilizado Payload.
- En caso de compartirse un favorito, se descarga su imagen utilizando Glide y se lanza el Intent correspondiente para compartir dicha imagen.

[Vídeo](https://j.gifs.com/EqWkDm.gif)

## Authors

- [@pablojoseoroz](https://www.linkedin.com/in/pablo-j-oroz-2402424a/)
