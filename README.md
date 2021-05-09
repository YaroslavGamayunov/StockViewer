<h1>📈 StockViewer</h1>
<p align="left">
  <img width="170" src="docs/media/stockviewer_logo.png"></img>
</p>

<a href='https://play.google.com/store/apps/details?id=com.yaroslavgamayunov.stockviewer'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="170"/></a>
----------------------------------------------------------------

This application allows users to analyze stock prices and find latest market news

## Technologies and architecture
* Built on MVVM pattern 
* Uses [Dagger 2](https://github.com/google/dagger) for dependency injection
* Android Architecture Components: ViewModel, Room, Paging
* [Retrofit](https://github.com/square/retrofit) for API calls
* [Koil](https://github.com/coil-kt/coil) for image loading

## Features 
* Discover stock prices (represented as a list)
* Search for tickers
* Add tickers to favourites 
* View stock price chart for specified time segment (Day, Week, Month, Year)
* View company profile and news
* Caches list of stock prices into database

## Screenshots
<img width="300" src="docs/media/screenshot_1.gif"> <img width="300" src="docs/media/screenshot_2.gif"><img width="300" src="docs/media/screenshot_3.gif"><img width="300" src="docs/media/screenshot_4.gif">

## Plans
* Use websockets to provide users with instant updates of stock prices
* Migrate to Jetpack Compose