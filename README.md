# automation-tests-custom-drivers

Este proyecto está constuído con el fin de brindar un punto centralizado para el uso de drivers perzonalizados para
diferentes plataformas (web, mobile y de escritorio). Es un proyecto cuyo propósito principal será ser usado como un jar
dentro de los proyectos de automatización, bastará con eso, para que un usuario tenga la posibilidad de usar uno o
varios drivers al mismo tiempo según las necesidades que tenga.

## Drivers soportados
En la versión 1.0, esta librería soporta los siguientes drivers:

* AppiumDriver
* WebDriver (chrome, firefox, ie, edge)
* WindowsDriver
* AppCenterDrivers (Android iOS)

## Requerimientos
Para usar los drivers, es requerido tener un archivo de propiedades con los capabilities de los mismos ubicados dentro
de la carpeta resources del módulo test del proyecto. Los archivos deben estar nombrados de la siguiente manera:

* WEB: 'web.properties'
* MOBILE, APPCENTER: 'mobile.properties'
* WINDOWS: 'windows.properties'

Este proyecto no abstrae el uso del driver, sólo su control. Por tanto, es necesario que en el proyecto donde sea
importada esta librería, se agregue el capability con el path al driver o el ejecutable del driver. Tenga en cuenta que
cuando se está usando un driver appium o alguna de sus extensiones como windows, se requiere tener previo a la ejecución
de los tests un servidor de appium o windows corriendo.

Cuando se requiere agregar capabilities propias de Serenity, se agregan al archivo serenity.properties en la raiz del
proyecto.

## Consideraciones adicionales para el uso del windows driver


## Recomendaciones
Si se usa un web driver y el navegador es ie, se recomienda agregar el siguiente capability:
    ignoreZoomSetting : true

