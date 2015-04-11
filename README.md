# ElggConnect - Easy desktop access to your Elgg-Network

![ElggConnect Icon](doc/images/screenshot.png)


ElggConnect is a desktop application for accessing your Elgg Network. You get informed of new messages and notifications or
easily visit your favorite Elgg-Network-Pages with custom shortcuts.


## Requirements

- A favorite text editor or IDE
- Java SE Development Kit 8 (JDK) 1.8 or later 
- Elgg Network with activated Elgg-Plugin [elgg_connect](https://github.com/ElggConnect/elgg_connect.git)

## Getting started


#### Copy or clone the Repo
```
git clone https://github.com/ElggConnect/ElggConnect.git
```
#### Create configuration-file

* **Copy** *doc/elgconnect.properties.example* into */src/main/resources/config/*
* **Rename** the file to *elgconnect.properties*
* **Customize** elgconnect.properties

```
# Required
baseurl=http://example.tld/

# Standard values for application window; change if desired
appname=ElggConnect
subline=Easy access to your Network
image=/images/png/logo.png

```

## Run ElggConnect


```
Unix        ./gradlew run
Windows     gradlew.bat run

```

## Deploy as a native package

```
Unix        ./gradlew jfxDeploy
Windows     gradlew.bat jfxDeploy

```




