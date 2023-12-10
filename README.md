# TreeWonder

Android application for discovering trees.

Backend : https://github.com/Victor804/treewonderserver

The main activity is displayed by default.
The CreateTree activity is launched when the plus button (top centre) is clicked.

## Main activity

The top menu has 3 buttons:
  - the plus button (to launch the CreateTree activity)
  - the refresh button (used to refresh the API data, particularly useful if you lose your internet connection or TimeOut during the initial loading of the data)
  - the spanner button (settings) to open the settings fragment

The navigation bar allows you to move between the ListTree, Maps and Favorites fragments (from left to right).

The main fragment (in the centre) can be one of 5 fragments :

### TreeList fragment

Display the picture and the name of all the trees in list (with a recyclerView).
Each tree can be clicked to display more details calling the Tree fragment (see below).
The star button at the right of each line can be used to add/remove the tree from the favorites.
The search button at bottom right opens a search bar for filtering the trees displayed (all trees containing the phrase entered in at least one of their parameters, including numerical parameters, will be displayed).

### Maps fragment

Displays a map (google map) of the world, with markers at the position of each tree.
By default, the map will be centred on Paris, but you can enable localisation to centre the map on your current location.
The location button at the bottom right refocuses the map on your location (if authorised).

### Favorites fragment

This fragment displays a list of all your favourite trees.
It uses TreeList fragment, and therefore works in the same way.

### Settings fragment

This fragment displays various information about the application.
The trash button at the bottom right deletes all the trees stored locally on the application (it's useless, but it's there).
Clicking on the settings button again closes this fragment.

### Tree fragment

This fragment displays all the information about a tree.
The star button at the bottom left can be used to add/delete the Favourites tree.
The bin button at the bottom right can be used to delete the tree locally.
The map button between the two displays the map fragment centred on this tree.

## CreateTree activity

### First fragment

Allows to set name, common name, botanic name and general informations about the tree (height, circumference and development stage).
Only the name is mandatory.

### Second fragment

Allows to set more detailed information : plantation year, outstanding qualification (the reason IN ONE WORD why this tree is on this app), summary (the detailed reason why this tree is on this app), description of the tree, type, species and variety, and finaly the links of the sign and a picture of the tree.

All these information are opionals.

### Third fragment

Allows so set the localisation of the tree on the map (with a pointer), this will set automaticaly the latitude, longitude and address of the tree.
This action is mandatory (actualy it's not in the backend, but Victor forgot to allow to pass this stage).
