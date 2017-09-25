# Ping-Builder-Service

A web service for configuring and building applications.

Built using the [Ktor](https://github.com/Kotlin/ktor) framework.

### Setup

**Intellij**:

1. *Edit configurations*
2. Set *Main class* to `org.jetbrains.ktor.netty.DevelopmentHost`
3. Set *Use classpath of module* to `ping-builder-service_main`
4. Set *Environment variables*:
    - `PORT`: port this server will run on
    - `BUILDER_URL`: Jenkins URL
    - `BUILDER_CRUMB`: Jenkins crumb
    - `BUILDER_USERNAME`: Jenkins username
    - `BUILDER_PASSWORD`: Jenkins user password
    
**Heroku**:

Set the `GRADLE_TASK` Config Variable to build the `.jar` specified in the `Procfile`:

`heroku config:set GRADLE_TASK="shadowJar"`

Add a `.env` file to the root of the project containing your environment variables (see Intellij setup for details):

```
PORT=5001
BUILDER_URL=http://sample.com:8080/
BUILDER_CRUMB=000000000000000000000000
BUILDER_USERNAME=username
BUILDER_PASSWORD=password
```

*NOTE*: if you are deploying to heroku you must either remove `.env` from the `.gitignore` file and commit it or
add your variables to your project's Config Variables in the Heroku platform

### Testing

**Request**:

```json
{
    "titleText": "\"Test\"",
    "titleTextSize": "20F",
    "titleTextColor": "0xFF000000",
    "imageUrl": "\"https://avatars1.githubusercontent.com/u/30177?v=4&s=200\"",
    "imageWidth": "250F",
    "imageHeight": "250F",
    "imageScaleType": "\"CENTER_CROP\"",
    "copyText": "\"Lorem ipsum\"",
    "copyTextSize": "20F",
    "copyTextColor": "0xFF000000",
    "sendButtonText": "\"SEND\"",
    "sendButtonTextColor": "0xFF000000",
    "sendButtonBackgroundColor": "0xFFCCCCCC",
    "phoneInputTextColor": "0xFF000000",
    "phoneInputBackgroundColor": "0xFFFFFFFF",
    "numPadTextColor": "0xFF000000",
    "numPadBackgroundColor": "0xFFFFFFFF",
    "backgroundColor": "0xFFFFFFFF",
    "pin": "\"0000\"",
    "message": "\"Sample SMS message to send\"",
    "urlBase": "\"http://sample.com\"",
    "urlPath": "\"ping\""
}
```

**Response**

```json
{
    "success": true,
    "message": "sample message"
}
```