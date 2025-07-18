import java.util.*;
import java.io.*; // For optional file I/O
import java.time.LocalDateTime;

// Stock Class
class Stock {
    private String symbol;
    private String companyName;
    private double currentPrice;

    public Stock(String symbol, String companyName, double currentPrice) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
    }
  
    public String getSymbol() {
        return symbol;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }

    public void displayStock() {
        System.out.println(symbol + ": " + companyName + " - $" + currentPrice);
    }
}

// User Class
class User {
    private String username;
    private double accountBalance;
    private Portfolio portfolio;
    
     public User(String username, double balance){
        this.username = username;
        this.accountBalance = balance;
        this.portfolio = new Portfolio(this);
    }

    public String getUsername() {
        return username;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

     public Portfolio getPortfolio() {
        return portfolio;
    }
}

// Transaction Class
class Transaction {
    private Stock stock;
    private User user;
    private int quantity;
    private double price;
    private LocalDateTime timestamp;
  
    public Transaction(Stock stock, User user, int quantity, double price, LocalDateTime timestamp) {
        this.stock = stock;
        this.user = user;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

  public Stock getStock() {
      return stock;
  }
  public int getQuantity() {
      return quantity;
  }
    public double getPrice() {
        return price;
    }
    public User getUser() {
        return user;
    }

    public void displayTransaction() {
        System.out.println("Transaction - User: " + user.getUsername() + ", Stock: " + stock.getSymbol() +
                ", Quantity: " + quantity + ", Price: $" + price + ", Time: " + timestamp);
    }
}

// PortfolioItem Class
class PortfolioItem {
    private Stock stock;
    private int quantity;

    public PortfolioItem(Stock stock, int quantity) {
      this.stock = stock;
      this.quantity = quantity;
    }

     public Stock getStock() {
        return stock;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void displayPortfolioItem() {
        System.out.println("Stock: " + stock.getSymbol() + ", Quantity: " + quantity);
    }
}

// Portfolio Class
class Portfolio {
  private User user;
  private List<PortfolioItem> items;
  public Portfolio(User user) {
      this.user = user;
      this.items = new ArrayList<>();
    }
  public void addStock(Stock stock, int quantity) {
        // Check if the stock already exists in the portfolio.
        for (PortfolioItem item : items) {
          if (item.getStock().getSymbol().equals(stock.getSymbol())) {
              // Update the quantity.
              item.setQuantity(item.getQuantity() + quantity);
              return;
          }
        }
        // If the stock is not in the portfolio, create a new PortfolioItem.
        items.add(new PortfolioItem(stock, quantity));
    }
  public void removeStock(Stock stock, int quantity) {
        for (PortfolioItem item : items) {
          if (item.getStock().getSymbol().equals(stock.getSymbol())) {
            int newQuantity = item.getQuantity() - quantity;
            if (newQuantity > 0) {
              item.setQuantity(newQuantity); // Update the quantity if there are stocks remaining
            } else {
                items.remove(item); // Remove the item if all stocks are sold
            }
            return;
          }
        }
      System.out.println("Stock not found in portfolio: " + stock.getSymbol());
    }
     public double calculatePortfolioValue() {
        double totalValue = 0;
        for (PortfolioItem item : items) {
           totalValue += item.getQuantity() * item.getStock().getCurrentPrice();
        }
        return totalValue;
    }

  public void displayPortfolio() {
        System.out.println("Portfolio for " + user.getUsername() + ":");
        for (PortfolioItem item : items) {
           item.displayPortfolioItem();
        }
        System.out.println("Total portfolio value: $" + calculatePortfolioValue());
    }
}

// Market Data Class
class MarketData {
    private List<Stock> stocks;

    public MarketData() {
        this.stocks = new ArrayList<>();
        // Initialize with some sample stocks (you'd normally load this from a data source)
        stocks.add(new Stock("AAPL", "Apple Inc.", 150.0));
        stocks.add(new Stock("GOOG", "Alphabet Inc.", 2500.0));
        stocks.add(new Stock("TSLA", "Tesla, Inc.", 800.0));
    }

  public Stock findStock(String symbol) {
      for(Stock stock : stocks) {
          if(stock.getSymbol().equals(symbol)) {
              return stock;
          }
      }
      return null; // Stock not found
  }
  public void updatePrices() {
        Random random = new Random();
        for (Stock stock : stocks) {
          double priceChange = (random.nextDouble() * 10 - 5); // Simulating random price change
          stock.currentPrice += priceChange;
        }
    }
  public void displayMarket() {
        System.out.println("Current Market Data:");
        for (Stock stock : stocks) {
            stock.displayStock();
        }
    }
}

// Trading Engine Class
class TradingEngine {
    private MarketData marketData;
    private List<Transaction> transactions;
    
    public TradingEngine(MarketData marketData){
        this.marketData = marketData;
        this.transactions = new ArrayList<>();
    }

  public boolean buyStock(String stockSymbol, User user, int quantity) {
      Stock stock = marketData.findStock(stockSymbol);
      if(stock == null) {
          System.out.println("Invalid stock symbol: " + stockSymbol);
          return false;
      }
    if (user.getAccountBalance() < stock.getCurrentPrice() * quantity) {
         System.out.println("Insufficient funds for purchase.");
         return false;
      }
        // Update user portfolio
        user.getPortfolio().addStock(stock, quantity);

        // Record the transaction
        double transactionPrice = stock.getCurrentPrice() * quantity;
        Transaction transaction = new Transaction(stock, user, quantity, transactionPrice, LocalDateTime.now());
        transactions.add(transaction);

        // Update user balance
        user.setAccountBalance(user.getAccountBalance() - transactionPrice);
        System.out.println("Buy order executed: " + quantity + " shares of " + stockSymbol + " for $" + transactionPrice);

        return true;
    }

  public boolean sellStock(String stockSymbol, User user, int quantity) {
       Stock stock = marketData.findStock(stockSymbol);
       if(stock == null) {
         System.out.println("Invalid stock symbol: " + stockSymbol);
          return false;
      }
      // Check if user has sufficient stocks
      PortfolioItem portfolioItem = null;
       for (PortfolioItem item : user.getPortfolio().items) {
           if (item.getStock().getSymbol().equals(stockSymbol)) {
               portfolioItem = item;
               break;
           }
       }
        if (portfolioItem == null || portfolioItem.getQuantity() < quantity) {
          System.out.println("Insufficient shares in portfolio for selling.");
          return false;
        }

        