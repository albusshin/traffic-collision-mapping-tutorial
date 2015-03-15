## Video Playlist
[Deployment Guide & Tutorial Youtube Playlist](https://www.youtube.com/playlist?list=PLSYa2aROWeB_m-hEPK5qKPBH2nZsHdMEW)

## Blog
[Read my blog post of this tutorial](http://blog.albusshin.com/tutorial/2015/03/15/New-York-City-traffic-collision-mapping-tutorial.html)

## Deployment

### Deployment of the project
1. Open Eclipse for Java EE Developers IDE
2. Click on File Menu -> Import...
3. Select "Existing Projects into Workspace", and click on Next
4. Select "Select archive file", and choose the zip file `traffic-collisions-mapping-tutorial.zip`, and click on Finish
5. DONE!

#### JRE Version
I strongly encourage you use 7 or higher versions of Java. However, if you have got a JRE 6, please note the below.

1. JRE System Library: As this project is written in Java 7, if you're using Java 6, you should replace the JRE System Library with Java 6. To do this, in the Build Path configuration dialog, click on JRE System Library, and click on "Remove" button. Click on "Add Library..." button, select "JRE System Library", and select an available library to import into the build path.
2. Project Facets: Again, if you're using Java 6 for development, you should change the project facets. In the project explorer, right click on the project, and choose "Properties", in the list on the left, choose "Project Facets", and change the value of Java from 1.7 to 1.6.

### Deployment of the Database
1. Get the Virtual Machine up and running
2. Open up a terminal
3. Unzip the `traffic-collisions-mapping-tutorial.zip` into the folder `/home/dbadmin/workspace/traffic-collisions-mapping-tutorial`
4. Enter the directory above
5. Execute `$ cd data`
6. Execute `$ adminTools`
7. Select `6 Configuration Menu`
8. Select `1 Create Database`
9. Input database name as `nytc`
10. Click on OK
11. Enter the password as `password`, and confirm
12. Input into fields `Catalog pathname` and `Data pathname` both as `/home/dbadmin/db/nytc`
13. Confirm everything and create the database
14. Select OK, and Select `M Main Menu`
15. Select `2 Connect to Database`
16. Execute `\i schema.sql` to create the schema
17. Execute `copy nypd_motor_vehicle_collisions from '/home/dbadmin/workspace/traffic-collision-mapping-tutorial/data/data_nypd_motor_vehicle_collision.tbl' delimiter '|';
18. After the rows being loaded, execute `delete from nypd\_motor\_vehicle\_collisions where longitude is null;`
19. In the terminal, execute `/sbin/ifconfig` to see the IP address.
20. Open up `src/haven/mappingtutorial/config.json` to config the DB connections properties
21. DONE!

### CARTO DB CONFIGURATIONS
The Carto DB should be able to use without any configurations. However, if you don't want to use my API Key and my carto db tables, please follow the steps below.

1. Register an account at [cartodb](http://cartodb.com);
2. Login. In your dashboard, select "Create your first table";
3. Select "Empty Table", and confirm. A new web page should be opened.
4. Unzip the zip file and open `cartoDBAlterTable.sql`. Copy all of its content.
5. Select the `SQL` icon on the right side of the webpage. Replace all of its content with what we've copied, and click on "Apply query"
6. Rename the table to "nytc", by clicking the table name on the top left corner.
7. Click on the `Visualize` button on the top right corner, and name that visualization `New York Traffic Collisions`
8. Clck on the `Share` button on the top right corner, and copy the url under the `carto.js` section.
9. Open the `WebContent/js/script.js` file, and replace the value of the variable `cartodbMapUrl` in line 14 to the content that you just copied.
10. Open your dashboard, and on the top right corner, click your name and select "Your API Keys", copy the API Key.
11. Open the `config.json` file, from `src/haven/mappingtutorial/`, and replace the cartodb api key with what you just copied.
12. Run the Java EE project from Eclipse. Open up the index page in a modern browser, toggle the filter form, and limit the time interval from start date and end date in 3 days. Click on "Filter" button.
13. See the dots showing on the map.
14. Open your visulization in cartodb.com. Open "MAP VIEW".
15. Click on the "infowindow" icon on the right.
16. Click on the "Change HTML" small icon in the opened editor.
17. Open the file `visulizationClickTooltip.html`, under the root folder of our unzipped folder.
18. Copy all of the content from this html file to the editor opened in the webpage, and click on `Apply` button.
19. DONE!

