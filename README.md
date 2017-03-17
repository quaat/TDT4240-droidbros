Server: (skal ordne databasen)

npm install
node server.js


Klient:
lag fil "local.properties" i "../client/game" foldern med innhold: 

# Location of YOUR android SDK (Eksempel på min)
sdk.dir=C:/Users/Leppis/AppData/Local/Android/sdk


run android:
>>> gradlew android:installDebug android:run


de andre gir feilmelding, dno why