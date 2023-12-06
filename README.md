<h1 align="center"> Implementation of the Publisher/Subscriber Pattern </h1>

![WEATHER](https://github.com/Lauuaguiar/Lauuaguiar/assets/145450311/d148d92b-20cf-4795-a381-27cf448cacf9)

<p align="left">
   <img src="https://img.shields.io/badge/STATUS-PROJECT%20COMPLETED-green">
</p>

## General Information
- **Subject:** Data Science Application Development
- **Course:** Data Science Engineering Degree
- **School:** School of Computer Engineering
- **University:** Universidad de Las Palmas de Gran Canaria

## Functionality Overview
This project involves two modules: the Prediction Provider, which retrieves weather data and sends it to the Apache ActiveMQ broker's topic; and the Event Store Builder, which stores these events in a structured manner in the 'eventstore' directory, organizing them by date.

## Installation and Usage
1. **Apache ActiveMQ Installation:**
   - Follow the instructions in the [ActiveMQ Getting Started](https://activemq.apache.org/getting-started) tutorial to download and install the broker.
   - By default, the broker accepts connections on port 61616.

2. **Project Configuration:**
   - Clone this repository.

3. **Project Execution:**
   - Open the project in IntelliJ.
   - Adding our API Key to the 'args[0]' variable is necessary. How can this be done? Check out the tutorial below:

[Tutorial Video](https://github.com/Lauuaguiar/Lauuaguiar/assets/145450311/898aeb83-8f7e-414b-8c7e-fa9cee9218fb)

The video demonstrates retrieving the ApiKey and accessing the class that utilizes it.
   - First, execute the Event Store Builder module. It will wait for the 'prediction.Weather' topic to receive messages, which will happen when the PredictionProvider module is executed. When both are executed simultaneously, but in this order, the console will display messages informing about the code's actions.

## Project Structure

- **Prediction Provider:**
  - Retrieves weather data from specific locations at a given frequency.
  - Generates JSON format events from this weather data.
  - The event structure includes information such as the prediction timestamp, the data source, the prediction timestamp, and the location with its coordinates.
  - Additionally, it includes metrics such as temperature in degrees Celsius and wind speed in m/s, adjusted according to the request to the OpenWeatherMap API.
  - Sends these events to the 'prediction.Weather' topic of the broker.

- **Broker (Apache ActiveMQ):**
  - Acts as an intermediary for communication between the Prediction Provider and the Event Store Builder.
  - Allows publication and subscription to specific topics, in this case, 'prediction.Weather'.

- **Event Store Builder:**
  - This module subscribes to the 'prediction.Weather' topic of the broker.
  - Stores consumed events from the broker in an ordered temporal manner.
  - Serializes events in the eventstore following a specific directory structure: eventstore/prediction.Weather/{ss}/{YYYYMMDD}.events.
  - Where 'YYYYMMDD' corresponds to the year, month, and day obtained from the event's timestamp. The '.events' is the file extension where the events associated with a specific day are stored.

## Class Diagram and Design
![EventStoreBuilder](https://github.com/Lauuaguiar/Weather2/assets/145450311/2ea95e5f-d26f-400a-8f0c-4e1d47de3a42)



## Project Dependencies
- **jsoup - Version 1.16.2:**
  - Library for parsing HTML and working with web data.

- **gson - Version 2.10.1:**
  - Library for serializing and deserializing Java objects to JSON and vice versa.

- **activemq-client - Version 6.0.0:**
  - Apache ActiveMQ client for communication with the message broker.

## Dependencies and Compilation
To compile and execute the project modules, make sure to have these dependencies installed. You can add these dependencies to the project's pom.xml file to manage them automatically with Maven or any other dependency management tool.

## Versioning and Delivery
- Each application should be packaged into a ZIP file (main jar and dependencies) and attached to a release in the repository.

## Contribution and Authors
- Laura Aguiar Pérez

## Contact
For questions or more information, contact Laura Aguiar Pérez via laura.aguiar101@alu.ulpgc.es
```

This revised text translates the provided content into formal English for a README file.
