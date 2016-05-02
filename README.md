# Drawmap
Use the Taxicab GPS data to count on each taxi’s influence on the traffic and use different color to visualize the degree and plot the road system on map

# Run the app

1. install [*NODEJS*](https://nodejs.org/en/download/) first, select the version that matches your environment, then go to the directory of the project.
2. install dependencies, you should finish this step without any issue
  
  ```sh
    npm install
  ```
  
3. start the node server, wait until see "Example app listening on port 3000!" in your console.

  ```sh
    node server
  ```
4. hit localhost:3000  

# Use the app
## Generate the map
When you go to the localhost:3000, the data begins loading and wait until map shows. It may take like 1 minitues.

## Export
Click the Export Button, the page will freeze since converting an svg to png is not an asychoronous operation.
Wait a few seconds and the old svg will be replaced by a png.

Then right click the page and select "save image as" and you will get the map!
![image](https://github.com/sbvictory/befit-workout/blob/master/readmeimages/login.jpg)

