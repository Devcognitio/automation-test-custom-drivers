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

###### Trabajar con una sola ventana:

Para interactuar con una sola ventana se debe crear el driver a través de alguno de
los siguientes métodos getWindowsDriver(Capabilities capabilities) o getWindowsDriver(). En caso de cambiar a otra 
plataforma sea móvil o web se debe utilizar el método getWindowsDriver() para retomar el control de la ventana.  

###### Trabajar con una múltiples ventanas:
Para interactuar con múltiples ventanas se debe crear el primer driver a través 
de alguno de los siguientes métodos getWindowsDriver(Capabilities capabilities) o getWindowsDriver(), este primer driver 
tendrá el control de la primera ventana. Para crear un driver que interactué con una nueva ventana (ya sea de la misma 
aplicación o de otra) se utilizará uno de los siguientes métodos changeToNewWindow(String idApp, String key) o 
changeToNewWindow(String idApp, String key, Capabilities capabilities). En caso de haber cambiado de plataforma sea móvil, 
web o para cambiar entre ventanas ya abiertas se debe utilizar el método getWindowByKey(String key), donde key corresponde 
al key utilizado en el método changeToNewWindow, para el primer driver que se creo utilizando otro método se debe utilizar 
la constante MAIN_WINDOW. Ejemplo:
```
// Creamos el primer driver tomando el key (Capabilitie app) del archivo de propiedades
WebDriver driver = EnhancedWindowsDriver.getWindowsDriver();
// trabajamos con la primera ventana
...
driver = EnhancedWindowsDriver.changeToNewWindow("C:\\Windows\\System32\\notepad.exe", "NOTE");
// trabajamos con la segunda ventana
...
EnhancedWindowsDriver.changeToNewWindow("Microsoft.WindowsCalculator_8wekyb3d8bbwe!App", "CALCULATOR");
// trabajamos con la tercera ventana
...
// Volvemos a la Segunda ventanas
driver = EnhancedWindowsDriver.getWindowByKey("NOTE");
// Trabajamos con la segunda ventana
...
// Volvemos a la tercera ventana
driver = EnhancedWindowsDriver.getWindowByKey("CALCULATOR");
// trabajamos con la tercera ventana
...
// Volvemos a la primera ventana
driver = EnhancedWindowsDriver.getWindowByKey(EnhancedWindowsDriver.MAIN_WINDOW);
// trabajamos con la primera ventana
```

## Recomendaciones
Si se usa un web driver y el navegador es ie, se recomienda agregar el siguiente capability:
    ignoreZoomSetting : true

