# bitvavo-exchange

### How to run the application
To run the application run the following command in the exchange folder

```./gradlew run --args="orders.txt"```

Any text file can be added to this folder and the name can be passed in the args section instead of "orders.txt"


To run the test run 

```./gradlew test pitest```

This command runs the tests and also runs a mutation testing
([PITEST](https://pitest.org)). You can see the results in /build/reports/pitest/index.html
