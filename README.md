# EuroWeather Explorer
![68095bd8-cd06-4f5a-b66b-0d9c27408a07_upscaled (1)](https://github.com/Lauuaguiar/Weather2/assets/145450311/4b431967-b1c9-47ef-8883-74efe00111a5)

## General Information
- **Subject:** Data Science Application Development
- **Course:** Data Science Engineering Degree
- **School:** School of Computer Engineering
- **University:** Universidad de Las Palmas de Gran Canaria

## Name and Description:

I have named this application "EuroWeather Explorer." Its primary purpose is to enable tourists in European capitals to inquire about points of interest located within a kilometer of the city center. Additionally, it provides details about the weather in that area, both for the current day and the upcoming days.

From a technical perspective, the functionality of the application is broken down into four modules. Initially, we have the Prediction Provider module and the Activity Provider module, responsible for gathering the necessary information and uploading it to the designated broker. Subsequently, the Datalake Builder module subscribes to the broker's topics, extracts the information, and stores it in a folder named "datalake," located in the user-specified path. Finally, the EuroWeather Business-Unit module manages the user interface and datamart.

## Installation:

1. Follow the detailed instructions in the [ActiveMQ Getting Started](https://activemq.apache.org/getting-started) guide to download and install the broker.
2. Download the zip file of this project and open it in IntelliJ IDEA.

## System Requirements:

Ensure that the resources folder of the Business-Unit, PredictionProvider, and ActivityProvider modules contains a file named "locations.csv," which includes all European capitals along with their respective coordinates.

## Configuration:

To ensure the proper functioning of the application, it is necessary to add the required arguments for each module:
- In PredictionProvider, input your OpenWeatherMap APIKey.
- In ActivityProvider, input your OpenTripMap APIKey.
- In DatalakeBuilder and Business-Unit, specify the path where you want to save the "datalake" folder.

Refer to this [video tutorial]() for a detailed guide on adding these arguments.

## Usage:

1. Run the Main Datalake to wait for messages from the topics.
2. Subsequently, run the Main Activity and the Main Prediction in any order.
3. When both Main modules indicate they have finished, stop all three Main modules to create the "datalake" folder. Note that the Main Activity may take slightly longer to complete.
4. Finally, run the Main Business. At this point, you can choose to rerun the Main Activity and the Main Prediction to obtain information directly from the topics or let the module detect the absence of messages (which may take about 40 seconds) and retrieve information from the "datalake." Once this process is complete, the user interface will start functioning, and questions will appear in the console.

## Examples:

Provide practical examples of common use cases.

## Documentation:

This project utilizes the OpenWeatherMap APIs (for weather data) and OpenTripMap APIs (for points of interest).

## Class Diagram and Design

## Class diagram of the DatalakeBuilder module.

## Class diagram of the ActivityProvider module.

## Class diagram of the PredictionProvider module.

## Class diagram of the Business-Unit module.


## Project Dependencies

### jsoup - Version 1.16.2
- Description: Library for parsing HTML and working with web data.

### gson - Version 2.10.1
- Description: Library for serializing and deserializing Java objects to JSON and vice versa.

### activemq-client - Version 6.0.0
- Description: Apache ActiveMQ client for communication with the message broker.

### jakarta.jms-api - Version 3.1.0 (Compile Scope)
- Description: Jakarta Messaging API for Java Message Service.

### sqlite-jdbc - Version 3.41.2.2
- Description: SQLite JDBC driver for database connectivity.

## Dependencies and Compilation

To compile and execute the project modules, make sure to have these dependencies installed. You can add these dependencies to the project's pom.xml file to manage them automatically with Maven or any other dependency management tool.

## Versioning and Delivery
- Each application should be packaged into a ZIP file (main jar and dependencies) and attached to a release in the repository.

## Contribution and Authors
- Laura Aguiar Pérez

## Contact
For questions or more information, contact Laura Aguiar Pérez via laura.aguiar101@alu.ulpgc.es
