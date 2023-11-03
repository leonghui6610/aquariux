package com.interview.aquariux.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan(basePackages = {"com.interview.aquariux.trade.service", "com.interview.aquariux.trade.controller"})
@EntityScan(basePackageClasses = Jsr310Converters.class, basePackages = {"com.interview.aquariux.trade.entities"})
@EnableJpaRepositories(basePackages = {"com.interview.aquariux.trade.entities"})
@SpringBootApplication
public class TradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }

}


/*
http://localhost:8008/h2-console/
jdbc URL: jdbc:h2:file:./dataTrade
username: dba
password: aquariux

requirements as follows:

1)scheduler is at SymbolScheduler.java -  10 seconds interval scheduler to retrieve the pricing from the source
above and store the best pricing into the database.

Assumption best aggregated price is
a) best Bid price if new bid price higher than existing bid price
b) best Ask price if new ask price lower than existing ask price

2) To use api to retrieve the latest best aggregated price.

use GET:    http://localhost:8008/crypto/getPrices

3) To use api which allows users to trade based on the latest best aggregated price.

    use POST: http://localhost:8008/crypto/trade
    Trader Id 1 was already created at DataInitilizer class upon server startup

    POST body
    sample 1
        {
            "traderId":1,"symbol":"BTCUSDT","unit":0.1,"direction":"BUY"
        }
        sample 2
        {
            "traderId":1,"symbol":"BTCUSDT","unit":0.1,"direction":"SELL"
        }

4) To use api to retrieve the user’s crypto currencies wallet balance
   use GET:  http://localhost:8008/crypto/trade?traderId=1

5) To use api to retrieve the user trading history.
    use GET: http://localhost:8008/crypto/history?traderId=1

 */