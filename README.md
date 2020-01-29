# NasaApiAndroid
Android app that allows to search oficial images from the Nasa, using the Nasa Images API.
Has a search field where searches can be made based on keywords. The results are shown in a recycler view.

<image src="images/v1_mainActivity.jpeg" width=250> <image src="images/v1_mainActivity2.jpeg" width=250> <image src="images/v1_searchResult.jpeg" width=250>

Selecting a single item of the results shows a new activity with item details. The item description may contain official Nasa links.

<image src="images/v1_itemDetails.jpeg" width=250> <image src="images/v1_itemDescription.jpeg" width=250>

The images can be saved on local storage, shared and added to favourites. When added to favourites, the item is saved in a SQLite database. Items saved on favourites can be accessed with no internet connection. The favourites list allows to filter its items.

<image src="images/v1_saveShare.jpeg" width=250> <image src="images/v1_favList.jpeg" width=250> <image src="images/v1_favListFilter.jpeg" width=250>
  
The searches are saved in a history list, which is inflate through a SQLite database. History list allows to filter its items.

<image src="images/v1_histList.jpeg" width=250> <image src="images/v1_histListFilter.jpeg" width=250>


# Development Log
# 1/16/2020
Added search functionality, api connection and simple display of the results.

<image src="images/day1_mainactivity.jpeg" width=250> <image src="images/day1_searchfunc.jpeg" width=250> <image src="images/day1_recyclerview.jpeg" width=250>

# 1/17/2020
UI improved.

<image src="images/day2_mainactivity.jpeg" width=250> <image src="images/day2_recyclerviewsearch.jpeg" width=250>

Added details when tap on item.

<image src="images/day2_itemdetails.jpeg" width=250>

SQLite set to add a history.

<image src="images/day2_history.jpeg" width=250>
  
# 1/18/2020
History UI improved.

<image src="images/day3_history.jpeg" width=250>

Added delete by item and delete all in history.

<image src="images/day3_deleteItemHistory.jpeg" width=250> <image src="images/day3_deleteAllHistory.jpeg" width=250>
  
# 1/19/2020
Added search view and selectable items to history.

<image src="images/day4_history.jpeg" width=250> <image src="images/day4_searchHistory.jpeg" width=250>
  
# 1/21/2020
UI improved.

<image src="images/day5_mainActivity.jpeg" width=250>
  
Added favourites list. Items now can be added to favourites. 
 
<image src="images/day5_favourites.jpeg" width=250> <image src="images/day5_itemDetails.jpeg" width=250>

Images on item details now can be saved or shared when long pressed on them.

<image src="images/day5_shareSave.jpeg" width=250>











