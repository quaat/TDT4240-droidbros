## Server
* Server is running at https://fast-crag-60223.herokuapp.com/
* For local:
```
$> cd ../src/server
$> npm install
$> node server.js
```
## Client
* Create file: `"local.properties"` in `"../src/client/game"` foldern med innhold:
    ```
    sdk.dir=C:/Users/Leppis/AppData/Local/Android/sdk
    ```
* Connect android phone to computer and run:
    ```
    $> gradlew android:installDebug android:run
    ```
