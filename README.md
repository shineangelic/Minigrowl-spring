# Minigrow back-end services

Minigrow _APIs_ are based on three kind of objects: `sensors`, `actuators` and `commands`. While the first two reflect real hardware devices with their own readings, the command is an abstraction used to drive such devices. a `Board` contains a list of actuators and one of sensors. The system supports more than one board, each one will be treated and logged separately.

The spring-boot server exposes REST API to exchange such devices between harware boards (ESP) and clients with JSON representation and keeps a history. Two separate api sides are exposed: one to be used by the board and the other from clients. Since this is a personal home project, no additional security nor login features are provided. The spring server may be used optionally, as the [Minigrowl-ESP](https://shineangelic.github.io/Minigrowl-ESP-LoRa32-OLED/) already implements some basic logic, but you'll need this running in order to archive logs and serve clients like [Minigrowl-react](https://shineangelic.github.io/Minigrowl-react/).

*MongoDB* is being used for logging, it stores a new sensor log when detects value changes. Such collection is then used to aggregate data ready to be plotted. It also aggregates actuator stuatuses, so that you can know for how much time a given device was turned on during a certain timespan. Devices flagged with error won't be saved.

![architecture diagram](/docs/diagram.png)

# Client APIs

Minigrowl supports multiple boards, in order to control more chambers/drying rooms. {boardId} refers to a constant wired at board level, that identifies it as an integer.

```
   /api/minigrowl/v2/sensors/{boardId}
   /api/minigrowl/v2/sensors/{id}/hourChart
   /api/minigrowl/v2/sensors/{id}/historyChart
   
   /api/minigrowl/v2/actuators/{boardId}
   /api/minigrowl/v2/actuators/uptime
   /api/minigrowl/v2/commands/{boardId}/queue/add
```

# ESP32 APIs
```
   /api/esp/v1/actuators/id/{id}
   /api/esp/v1/sensors/id/{id}
   /api/esp/v1/commands
```

# Websockets

Two topic websocket are made available in order to asyncronously update front-end views. a message is sent upon changes detection, containing only data relevant to the change event. At front-end level, `actuatorId` and `sensorId` will be used to map actuators and sensors, respectively, so that unrelevant updates are ignored.

```
   /topic/actuators/
   /topic/sensors/
```


## Command example:

A command is used to operate `Actuators`, given that they are in *MANUAL* state. When *AUTOMATIC*, default chamber logic implemented on [ESP32](https://shineangelic.github.io/Minigrowl-ESP-LoRa32-OLED/) will be applied.

```
    {
        "name": "Switch lights ON",
        "val": "1",
        "tgt": 12
    }  
```

To switch mode between *AUTOMATIC* and *MANUAL* mode, relevand commands are included for each device, for example:
```
    {
        "name": "AUTO mode",
        "val": "-2",
        "tgt": 2
    }
```


## Sensors example:
Each `sensor` represented at spring level has a `timeStamp` of last contact received from board, a `uinit` to represent its unit (Celsius, millibar, %, etc.) and an error state at true if some hardware problem occurred. `val` is a float containing last known sensor's value.

```
[
    {
        "sensorId": 28,
        "typ": "HUMIDITY",
        "uinit": "%",
        "timeStamp": "2020-06-01T23:12:41.262+0000",
        "err": false,
        "id": 17,
        "val": "0",
        "bid": {
            "boardId": 2
        }
    },
    {
        "sensorId": 29,
        "typ": "BAROMETER",
        "uinit": "mb",
        "timeStamp": "2020-06-01T23:13:07.244+0000",
        "err": false,
        "id": 21,
        "val": "0",
        "bid": {
            "boardId": 2
        }
    },
    {
        "sensorId": 35,
        "typ": "TEMPERATURE",
        "uinit": "°",
        "timeStamp": "2020-06-01T23:13:33.451+0000",
        "err": false,
        "id": 38,
        "val": "0",
        "bid": {
            "boardId": 2
        }
    }
]
```

## Actuators example (w/ supported commands)

An actuator is a real device used inside a typical growroom, for example: `MainLights`, `Humidifier` or different kind of `Fan`. The fields are similar to sensors ones, apart from a list `cmds` of supported commands and the current Actuator's `mode` ( -1 `AUTO`, -2 `MANUAL`)

```
[
    {
        "actuatorId": 30,
        "typ": "OUTTAKE",
        "uinit": "O",
        "timeStamp": "2020-06-01T23:12:39.712+0000",
        "id": 13,
        "val": "0",
        "mode": -1,
        "err": false,
        "bid": {
            "boardId": 2
        },
        "cmds": [
            {
                "tgt": "13",
                "name": "Manual mode",
                "val": "-1"
            },
            {
                "tgt": "13",
                "name": "AUTO mode",
                "val": "-2"
            },
            {
                "tgt": "13",
                "name": "Turn ON",
                "val": "1"
            },
            {
                "tgt": "13",
                "name": "Turn OFF",
                "val": "0"
            }
        ]
    }
]
```
## Send Command example
PUT on /api/minigrowl/v2/commands/queue/add, with payload like the following (it must be a supported command seen above)
```
{
        "name": "Turn intake Fan OFF",
        "targetActuator": 2,
        "val": "0"
}
```



