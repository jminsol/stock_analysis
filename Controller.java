// The Stock program is following the MVC design template and this is our controller object.
// The main functionality for buying and selling the stocks are in this controller object.
// This is the ONLY file you may edit

import java.util.*;

public class Controller {

	private static Object LinkedList;

	public Controller() {
		HashMap<String, LinkedList<Stock>> stock_list = new HashMap<String, LinkedList<Stock>>();

		Scanner input = new Scanner(System.in);

		do {
			// User enter stock name
			System.out.print("Enter 1 for stock name, 2 for quit: ");
			int stockSelect = input.nextInt();

			if (stockSelect <1 || stockSelect >2 )	//	error check 1: wrong menu input
				System.out.println("Wrong menu input. Please enter right number of menu.");

			else {
				if (stockSelect == 2)    //quit
					break;

				//Make user choice of stock to be capitalized
					System.out.print("What is the name of the stock?: ");
					String stockName = input.next();
					stockName = stockName.substring(0, 1).toUpperCase() + stockName.substring(1).toLowerCase();


				do {

					System.out.print("Input 1 to buy, 2 to sell: ");
					int controlNum = input.nextInt();

					if ((controlNum != 1 && controlNum != 2))
						System.out.println("Please choose to buy or sell");    // error check 2. wrong menu input2: buy or sell

					if (stock_list.isEmpty() && controlNum == 2){	// error check 3. When they are trying to sell stocks at first
						System.out.println("You haven't bought anything. You should buy some shares first");
					}

					else {
						//Adding the stock only when there's no duplicate
						if (!stock_list.containsKey(stockName))
							stock_list.put(stockName, new LinkedList<Stock>());

						System.out.print("How many stocks: ");
						int quantity = input.nextInt();

						//When a user chose to buy shares
						if (controlNum == 1) {
							System.out.print("At what price: ");
							double price = input.nextDouble();
							Controller.buyStock(stock_list.get(stockName), stockName, quantity, price);
						}
						//When a user chose to sell shares
						else {
							if (stock_list.containsKey(stockName) && !stock_list.get(stockName).isEmpty()) {		//error check 1: if ever bought
								System.out.print("Press 1 for LIFO accounting, 2 for FIFO accounting: ");
								controlNum = input.nextInt();

								System.out.print("How much do you want to sell?: ");
								double sellingPrice = input.nextDouble();

								//Choice of accounting
								if (controlNum == 1) {
									Controller.sellLIFO(stock_list.get(stockName), quantity, sellingPrice);
								} else {
									Controller.sellFIFO(stock_list.get(stockName), quantity, sellingPrice);
								}
							}
							else{
								System.out.printf("You didn't buy any shares of %s stock %n", stockName);
								break;
							}
						}
						break;
					}
				} while (true);

			}
			} while (true) ;
			input.close();
	}


	public static void buyStock(LinkedList<Stock> list, String name, int quantity, double price) {
		Stock temp = new Stock(name, quantity, price);
		list.push(temp);
		System.out.printf("You bought %d shares of %s stock at $%.2f per share %n", quantity, name, price);
	}

	public static void sellLIFO(LinkedList<Stock> list, int numToSell, double askingPrice) {

		// You need to write the code to sell the stock using the LIFO method (Stack)
		// You also need to calculate the profit/loss on the sale
		int total = 0; // this variable will store the total after the sale
		double profit = 0; // the price paid minus the sale price, negative # means a loss
		double purchasedPrice = 0;	// the total price paid


		for (int i = 0; i < list.size(); i++)       // error check 4: if the user owns less than s/he wants to sell
			total += list.get(i).getQuantity();

		if (total < numToSell)
			System.out.printf("You only own %d share of %s stock which is less than the %d shares that you wish to sell %n", total, list.element().getName(), numToSell);


		else {
			int temp = numToSell;
			for (int i = 0; i < list.size(); i++) {

				if (list.get(i).getQuantity() <= temp) {        // case 1: when you have to sell the whole slot of shares
					purchasedPrice += list.get(i).getQuantity() * list.get(i).getPrice();
					temp -= list.get(i).getQuantity();
					list.pop();
					i--;
				} else {                                    // case 2: when you have to sell part of slot of shares
					purchasedPrice += temp * list.get(i).getPrice();
					list.get(i).setQuantity(list.get(i).getQuantity() - temp);
					temp = 0;
					break;
				}
			}

			profit = (numToSell * askingPrice) - purchasedPrice;

			System.out.printf("You sold %d shares of %s stock at %.2f per share %n", numToSell, list.element().getName(), askingPrice);
			System.out.printf("You made $%.2f on the sale %n", profit);
		}

	}

	public static void sellFIFO(LinkedList<Stock> list, int numToSell, double askingPrice) {
		// You need to write the code to sell the stock using the FIFO method (Queue)
		// You also need to calculate the profit/loss on the sale
		int total = 0; // this variable will store the total after the sale
		double profit = 0; // the price paid minus the sale price, negative # means a loss
		double purchasedPrice = 0;

		Iterator<Stock> iter = list.descendingIterator();
		for (Stock element : list)
			total += element.getQuantity();

		if (total < numToSell)        //err check 4-2: User owns more shares than shares to sell
			System.out.printf("You only own %d share of %s stock which is less than the %d shares that you wish to sell %n", total, list.element().getName(), numToSell);

		else {
			int temp = numToSell;

			while (iter.hasNext()) {
				Stock element = iter.next();
				if (element.getQuantity() <= temp) {
					purchasedPrice += element.getQuantity() * element.getPrice();
					temp -= element.getQuantity();
					element.setQuantity(0);		//Set quantity to 0 for later removals.
				} else {
					purchasedPrice += temp * element.getPrice();
					element.setQuantity(element.getQuantity() - temp);
					temp = 0;
				}
			}

				//remove all the elements with 0 quantity
			Iterator<Stock> iter2 = list.iterator();
			while (iter2.hasNext()){
				if (iter2.next().getQuantity() == 0){
					iter2.remove();
					}
				}
			profit = (numToSell * askingPrice) - purchasedPrice;

			System.out.printf("You sold %d shares of %s stock at %.2f per share %n", numToSell, list.element().getName(), askingPrice);
			System.out.printf("You made $%.2f on the sale %n", profit);
		}
	}
	}


