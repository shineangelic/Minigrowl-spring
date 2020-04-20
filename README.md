# Minigrow back-end services

Minigrow APIs are based on three kind of objects: `sensors`, `actuators` and `commands`. While the first two reflect real hardware devices with their own readings, the command is an abstraction used to drive such devices.

The spring-boot server exposes REST API to exchange such devices between harware boards and clients with JSON representation and keeps a history. Since this is a personal home project, no additional security nor login features are provided. The spring server may be used optionally, as the [Minigrowl-ESP](https://shineangelic.github.io/Minigrowl-ESP-LoRa32-OLED/) already implements some basic logic, but you'll need this running in order to archive logs and serve clients like [Minigrowl-react](https://shineangelic.github.io/Minigrowl-react/).

*MongoDB* is being used for logging, it stores a new sensor log when detects value changes. Such collection is then used to aggregate data ready to be plotted

![architecture diagram](/docs/diagram.png)

# Client APIs
```
   /api/minigrowl/v1/commands
   /api/minigrowl/v1/sensors
   /api/minigrowl/v1/actuators
   /api/minigrowl/v1/commands/queue/add
   /sensors/{id}/hourChart
```

# ESP32 APIs
```
   /api/esp/v1/actuators/id/{id}
   /api/esp/v1/sensors/id/{id}
   /api/esp/v1/commands
   /api/minigrowl/v1/commands/queue/add
```

# Websockets

Two topic websocket are made available in order to asyncronously update front-end views. a message is sent upon changes detection, containing only data relevant to the change event.

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
        "id": 17,
        "typ": "HUMIDITY",
        "val": "25.57002",
        "uinit": "PERCENT",
        "timeStamp": "2020-04-11T13:22:34.205+0000",
        "err": false
    },
    {
        "id": 21,
        "typ": "BAROMETER",
        "val": "1012.763",
        "uinit": "MILLIBAR",
        "timeStamp": "2020-04-11T13:22:20.353+0000",
        "err": false
    },
    {
        "id": 22,
        "typ": "TEMPERATURE",
        "val": "26.12",
        "uinit": "CELSIUS",
        "timeStamp": "2020-04-11T13:22:48.155+0000",
        "err": false
    },
    {
        "id": 33,
        "typ": "LIGHT",
        "val": "673",
        "uinit": "LUMEN",
        "timeStamp": "2020-04-11T13:23:02.023+0000",
        "err": false
    }
]
```

## Actuators example (w/ supported commands)

An actuator is a real device used inside a typical growroom: `MainLights`, `Humidifier` or different kind of `Fan`. The fields are similar to sensors ones, apart from a list `cmds` of supported commands and the current Actuator's `mode` 

```
[
    {
        "id": 2,
        "typ": "FAN",
        "uinit": "O",
        "timeStamp": "2020-04-19T19:40:03.037+0000",
        "val": "0",
        "mode": -2,
        "err": false,
        "cmds": [
            {
                "name": "Turn ON",
                "val": "1",
                "tgt": 2
            },
            {
                "name": "Turn OFF",
                "val": "0",
                "tgt": 2
            },
            {
                "name": "AUTO mode",
                "val": "-2",
                "tgt": 2
            },
            {
                "name": "Manual mode",
                "val": "-1",
                "tgt": 2
            }
        ]
    },
    {
        "id": 12,
        "typ": "LIGHT",
        "uinit": "O",
        "timeStamp": "2020-04-19T19:39:53.424+0000",
        "val": "1",
        "mode": -2,
        "err": false,
        "cmds": [
            {
                "name": "AUTO mode",
                "val": "-2",
                "tgt": 12
            },
            {
                "name": "Manual mode",
                "val": "-1",
                "tgt": 12
            },
            {
                "name": "Switch ON",
                "val": "1",
                "tgt": 12
            },
            {
                "name": "Switch OFF",
                "val": "0",
                "tgt": 12
            }
        ]
    },
    {
        "id": 13,
        "typ": "OUTTAKE",
        "uinit": null,
        "timeStamp": "2020-04-19T19:40:07.770+0000",
        "val": "0",
        "mode": -2,
        "err": false,
        "cmds": [
            {
                "name": "AUTO mode",
                "val": "-2",
                "tgt": 13
            },
            {
                "name": "Manual mode",
                "val": "-1",
                "tgt": 13
            },
            {
                "name": "Turn ON",
                "val": "1",
                "tgt": 13
            },
            {
                "name": "Turn OFF",
                "val": "0",
                "tgt": 13
            }
        ]
    }
]
```
## Send Command example
PUT on /api/minigrowl/v1/commands/queue/add, with payload like the following (it must be a supported command seen above)
```
{
        "name": "Turn intake Fan OFF",
        "targetActuator": 2,
        "val": "0"
}
```



