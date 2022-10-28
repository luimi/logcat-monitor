# Logcat Monitor [![](https://jitpack.io/v/luimi/logcat-monitor.svg)](https://jitpack.io/#luimi/logcat-monitor)

This library helps Android developers to read logcats in real time remotely.

## Instalation

### Server

You can easily run this server localy or in a cloud

**Localy**

```bash
cd server
npm i
node server.js
```

### App

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```gradle
dependencies {
   implementation 'com.github.luimi:logcat-monitor:Tag'
}
```

## Usage

### Android App
Start logcat-monitor service

```kotlin
LogCatMonitor(this).putServer("serverUrl").start()
```

**Doc**

- `LogCatMonitor(context: Context): LogCatMonitor` - Constructor.
- `putServer(serverUrl: String): LogCatMonitor` - Set your server url (Required).
- `putCode(code: String): LogCatMonitor` - Set this code to indentify devices in the server, by default will set androidID.
- `setPing(inverval?: Long)` - Optional interval time to ping until stop, defaul 30sec.
- `setFilterByTag(filters: ArrayList<String>)` - List of tags to filter.
- `setExcludeByTag(excludes: ArrayList<String>)` - List of tags to exclude from logs.
- `start(): void` - Starts the service in background.

**Binding service**

```kotlin
lateinit var bind: MainService.Bind
val serviceConnection = object: ServiceConnection{
    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        bind = p1 as MainService.Bind
        bind.logCallback = {
            val log = Log(it)
            if(log.isLog){
                // In case you want to use logs in your app
            }
        }
        bind.wsCallback = {
            // it = websocket connection status
        }
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        // Disconnected
    }
}

// ---
val logCatMonitor = LogCatMonitor(this).putServer(server).putCode(code)
logCatMonitor.start()
logCatMonitor.intent.also { intent ->
   bindService(intent, serviceConnection, BIND_AUTO_CREATE)
}
```

### Server

Open server url and check you app logcat in realtime

**Doc**

- `Autoscroll` - Toggle autoscroll logs.
- `Filter` - Type filter only for the Tag's you want to see.
- `Device` - Select the device you want to subscribe.
- `Sync` - Syncronice device list.
- `Clear` - Clear's the listed logs.
- `Status` - Show the connection status of the websocket, not the device.