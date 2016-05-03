# Drawmap
Analyze the Taxicab GPS data to count on each taxi’s influence on the traffic and use different colors to visualize the degree and plot the road system on map

#Get the JSON file for plotting

1. Get the map GPS data for the intereted city(Shenzhen) from the [*OpenStreetMap*](http://www.openstreetmap.org/export#map=13/22.5750/114.1050) by defining the bounding box of the city and export into a xml file.
2. Get the traffic GPS data and prepare it into a csv file.
3. Run the [*DrawMap.ipynb*](https://github.com/SarahDai/drawmap/blob/SarahDai-patch-1/DrawMap.ipynb) to preprocess the data and the output is a JSON file for plotting the map.

# Run the app

1. Install [*NODEJS*](https://nodejs.org/en/download/) first, select the version that matches your environment, then go to the directory of the project.
2. Install dependencies, you should finish this step without any issue
  
  ```sh
    npm install
  ```
  
3. Start the node server, wait until see "Example app listening on port 3000!" in your console.

  ```sh
    node server
  ```
4. Hit localhost:3000  

# Use the app
## Generate the map
When you go to the localhost:3000, the data begins loading and wait until map shows. It may take like 1 minitues.

## Export
Click the Export Button. Since transform a svg to png also requires a long time, a page that tells you the data is processing will appear. Wait until this page disappear. The old svg will be replaced by a png.

Then right click the page and select "save image as" and you will get the map!
![image](https://github.com/SarahDai/drawmap/blob/master/dashmap/instructionimage/save.jpg)

## Sample
You can get a sample image here
[*Sample Image*](https://raw.githubusercontent.com/SarahDai/drawmap/master/dashmap/instructionimage/sample.png)

