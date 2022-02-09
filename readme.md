
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

![Splash](https://github.com/pablojoseoroz/PersonajesMarvel/blob/32092a91811b2ea920ed963c15a2ecc0dd78fcbf/captures/splash.png) | ![List](https://github.com/pablojoseoroz/PersonajesMarvel/blob/32092a91811b2ea920ed963c15a2ecc0dd78fcbf/captures/home.png) | ![Detail](https://github.com/pablojoseoroz/PersonajesMarvel/blob/32092a91811b2ea920ed963c15a2ecc0dd78fcbf/captures/detail.png)


## Documentation

Se ha dividido la aplicación en modulos:
- app: contiene la aplicación de Marvel
- marvelapi: módulo encargado de las conexiones con la API de marvel
- navigation: módulo customizado de navegación
- uibase: módulo base para la interfaz

Se han utilizado las siguientes librerías:
- Timber: para facilitar los logs
- Glide: para facilitar el mostrado de imágenes
- EventBus: para enviar eventos entre pantallas
- Room: para crear la base de datos local donde guardar los favoritos
- Palette: para obtener los colores principales de una imagen y colorear con ellos otras vistas dinámicamente
- Retrofit: para facilitar la implementación de la API de Marvel
- OkHttp: para realizar las llamadas web
- OkHttpProfiler: para ver las llamadas hechas en la API en tiempo de desarrollo
- Navigation: para la navegación entre pantallas



## Demo

El funcionamiento básico de la app es el siguiente:
- Durante 1 seg se muestra la pantalla de carga indicando el copyright del contenido
- Después navega al listado de personajes Marvel, donde se puede modificar meediante un botón en la Toolbar el modo en que se visualizan, de forma linear o en forma de grid de 3 columnas, dependinedo el modo se utiliza una vista u otra.
- Para cada personaje se muestra su foto, su nombre y la cantidad de cómics, series,  historias y eventos en los que ha participado.
- Se utiliza Palette para extraer de la imagen su color principal y aplicarlo al borde de la card del item, así como a los chips donde se muestra las cantidades.
- Se cargan a su vez los personajes favoritos que estén alojados en la base de datos de Room, y en caso de que un personaje sea favorito se muestra un corazón en su item.
- Al pinchar en un personaje, se navega a su detalle, mostrando su foto en grande, nombre, descripción y un tablayout en el cual se listan los nombres de los cómics, series, historias y eventos en los que ha participado.
- En la pantalla de detalle, existen las acciones de hacer o deshacer un personaje como favorito, lo cual enviará un evento al listado para que se actualice utilizado Payload.
- En caso de compartirse un favorito, se descarga su imagen utilizando Glide y se lanza el Intent correspondiente para compartir dicha imagen.


## Authors

- [@pablojoseoroz](https://www.linkedin.com/in/pablo-j-oroz-2402424a/)
