package com.jgba.nasaapiandroid

//Models for database objects
class History{
    var historyID: Int = 0
    var historySearch: String = ""
    var historyDate: String = ""
}
class Favourites{
    var favouritesID: Int = 0
    var favouritesNasaID: String = ""
    var favouritesImageLink: String = ""
    var favouritesTitle: String = ""
    var favouritesCreator: String = ""
    var favouritesDate: String = ""
    var favouritesDescription = ""
}