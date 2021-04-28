## StockViewer

This application is basically an aggregator of the content provided by stock market APIs

### News aggregation

News are aggregated with the help of [Finnhub stock API](https://finnhub.io). This is the only source of news information used by the app, developers of StockViewer do not write news. More precisely, we use [this method](https://finnhub.io/docs/api/company-news)

### Stock prices aggregation
Stock prices are received from two sources:

* [IEX Cloud API](https://iexcloud.io/docs/api/) - for displaying it on the main page
* [Finnhub stock API](https://finnhub.io/docs/api/) - for drawing charts

### Support or Contact

Having bugs? Contact us: [Yaroslav Gamayunov](mailto:yaroslav.gamayunov@gmail.com?subject=[StockViewer]%20Bug%20found)
