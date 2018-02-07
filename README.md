# Your Momma Loves Drama (YMLD)
This is a port of the YMLD, <a href="http://www.ibmpressbooks.com/title/0131855255">Apache Derby - Off to the Races</a> application using the following stack:
- Apache Derby
- Scala
- Slick
- Akka Http 
- Google Guice

The "Your Momma Loves Drama" database contains fictitious information about productions that are held in a small theater.
It contains the following tables:
 - PRODUCTIONS: Title and dates of all the productions. 
 - PERFORMANCE: Date and time information, as well as the number of seats available for each performance. 
 - SEATS: Seats available for a performance.
 - PRICEPLAN: Seat price plan.
 - SEATMAP: Seat view.
 - TRANSACTIONS: All purchase transactions.

Your Momma Loves Drama Database
  - Apache Derby - Off to the Races
  - Copyright (c) 2005 George Baklarz, Paul Zikopoulos, Dan Scott

Running Apache Derby
- Running the following command: startNetworkServer 