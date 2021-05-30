# Graphical User Interface Prototype  

Authors: Yasser Hobballah,Giorgio Rasetto, Simone Pistilli

Date:21 April 2021

Version: 1.0


# Screen Shots :
<!--LOG IN PNGs-->
## Screenshot 1: Log in 

![](GUIPNG/Login.png)

The EzShop application starts with screenshot 1. As it's possible to see, the user has 2 alternatives:
1. He can insert his Username, password and click on "LOG IN" button. When the user clicks on this button, if they are correct, the user is autenticated and screenshot 2 is shown.
2. If he doesn't remember his password he can click on "Forgot password?" link. And the IT Admin is notified.

<!--Under each PNG/s there is a small description -->

## Screenshot 2: Display Access Options

![](GUIPNG/Access_Type.png)

This screen is shown when a user clicks on "Login" button in screenshot 1.
In this second screenshot, a user has 4 alternatives.
The Employee depending on his role can access 4 part, also the Employee can have more than 1 role, the 4 possible roles that an employee can have
1. Cashier
2. Warehouse Admin
3. IT Admin
4. Accountant

<!-------------------------------------------->
<!--Cashier Part-->
## Screenshot 3: Cashier Sale Transaction

![](GUIPNG/Cashier.png)

When the Employee Chooses Cashier This screenshot appears.
1. Start Sale Transaction, Starts the process of scanning items.
2. Each scanned item is added to the transaction table.
3. Update Sale Transaction, allows adding or removing an item from the transaction table.
4. If the customer has a fidelity card, the cashier press "fidelity card" and is redirected to another transaction table which calculates points also (Screenshot 4: Fidelity Card).
5. when Customer wants to pay, "payment" Redirects to Payement Page . (Screenshot 5: Payment)

## Screenshot 4: Fidelity Card

![](GUIPNG/Cashier(Fidelity_Card).png)

The Same as Screenshot 3: Payment but with points calculation.

## Screenshot 5: Payment

![](GUIPNG/Payment.png)

The Customer can pay cash, or using Credit Card, If the fidelity card is used or a Coupon, points/discount are considered.

<!--Accountant PART-->
## Screenshot 6: Managing options

![](GUIPNG/Accountant.png)
The Accountant has 4 alternatives to Manage income and expenses. 
1. Display Daily Transactions, redirects user to screenshot 7 (to display Transactions related to a specific day)
2. Display Expenses ( To display expnese related to item expense and also other possible expenses) it redirects to screenshot 9.
3. Set New item prices, the accountant can set the price of the items according to a certain profit threshold. see scrrenshot 12.
4. Calculate Profit, the accountant can with this option calculate the profit (daily or monthly).
## Screenshot 7: Daily Transactions

![](GUIPNG/Daily_Transactions.png)
The Accountant can display the Daily Transactions, with curves for clarity and help in Analyzing the Sales in the Shop. Accountant can also possibly display the transactions from previous days using the prev button.
Also the Bar graph and line graph are used to display for the accountant in a proper way some statistics related to transactions in a particular day or over a given time period.

## Screenshot 8: Yesterday Transactions

![](GUIPNG/Daily_Transaction(Yesterday).png)
Display previous days transactions
## Screenshot 9: Expenses

![](GUIPNG/Expneses.png)
Display Expenses, Both Item expenses when bought from the supplier and Other expenses that are common to the EzShop.

## Screenshot 10: Daily profit

![](GUIPNG/Profit_Calculation_(Daily).png)

Accountant Calculates the Profit by pressing buttons "Calculate Total item Expense" and "Calculate Total income" and then the Total daily income is calculated automatically.
And there is a curve to show the growth of income at each day.

## Screenshot 11: Monthly Profit

![](GUIPNG/Profit_Calculation_Monthly.png)

Accounat Takes into Account the Other Expenses that arenot related to item expenses and subtract them from the total monthly income from sales and then Calculates The Real Monthly Profit of the Ezshop.
## Screenshot 12: Set item Price

![](GUIPNG/Set_New_item_Prices.png)
The Accountant can set price of new item arrived from thw supplier taking into account a certain predefined profit threshold for each new item the accountant wants to sell in the EzShop.

<!--IT ADMIN PART-->
## Screenshot 13: IT Admin - Main Page
![](GUIPNG/IT_Admin.png)

From this page the IT Admin could:
    1. Search an account typing the ID in the search Bar
    2. Start the creation of a new account for an employee
    3. Start the elimination of an existing account

## Screenshot 14: Remove Account

![](GUIPNG/Remove_Account.png)

To remove an account in this page is sufficient to press the icon at the end of each row, and the complete button confirms the the remove.

## Screenshot 15: Account not found

![](GUIPNG/Account_Not_found.png)

If an account searched is not in the database an alert is raised 

## Screenshot 16: Create Account

![](GUIPNG/Create_Account.png)

In this page the IT Admin compile the form of the employee that he wants to add with all the personl data required and sets the privileges of the account using the checkboxes.

## Screenshot 17: Creation Completed

![](GUIPNG/Creation_Completed.png)

After the compilation of the form, for this account is displayed the ID and the password. 


## Screenshot 18: update Account

![](GUIPNG/Update_Account.png)

In this page the personal data and the privileges could be changed by the IT Admin and updated using the button. It is also possible to reset the password using the "reset password" button that redirects to a new page. 

## Screenshot 19: Password update

![](GUIPNG/Password_Update.png)

In this page we could see the password after a reset, with the same ID but a new password for the account

<!--WareHouse Admin-->
## Screenshot 20: Warehouse items
In this page the warehouse admin can see all the items added in the store. Every item can be modified and/or deleted. Warehouse admin can also add new items from the "New Item" button. The page is built to be user friendly for the admin. He have a navbar from which he can control all the features he need: item, warning and order CRUDs. 
![](GUIPNG/Warehouse_Items.png)



## Screenshot 21: Warehouse Orders
In this page the warehouse admin can see all the orders added in the system. Every order can be modified and/or deleted. Warehouse admin can also add new orders from the "New Order" button. The page is built to be user friendly for the admin. He have a navbar from which he can control all the features he need: item, warning and order CRUDs. 
![](GUIPNG/Warehouse_Orders.png)


## Screenshot 22: Warehouse Warnings
In this page the warehouse admin can see all the warnings added in the system. Every warning can be modified and/or deleted. Warehouse admin can also add new warnings from the "New Warning" button. The page is built to be user friendly for the admin. He have a navbar from which he can control all the features he need: item, warning and order CRUDs. 
![](GUIPNG/Warehouse_Warnings.png)


## Screenshot 23: Add new item
In this page the warehouse admin can create a new item basically compiling the form and pushing the "confirm" button.
![](GUIPNG/Add_New_Item.png)


## Screenshot 24: update item
In this page the warehouse admin can update an existing item basically modifing the form and pushing the "confirm" button.
![](GUIPNG/Update_Item.png)

## Screenshot 25: Add new Warning
In this page the warehouse admin can create a new warning basically compiling the form, uploading the automatic order and pushing the "confirm" button.
![](GUIPNG/Add_New_Warning.png)


## Screenshot 26: Upload Warning
In this page the warehouse admin can update an existing item basically modifing the form and pushing the "confirm" button.
![](GUIPNG/Upload_Warning.png)


## Screenshot 27: Add new order
In this page the warehouse admin can create a new order basically uploading the order's file from his pc.
![](GUIPNG/Add_new_Order.png)


## Screenshot 28: Update order
In this page the warehouse admin can update an existing order basically uploading the new order's file from his pc.
![](GUIPNG/Update_Order.png)
