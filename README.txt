Online Auciton System By Tomos Wootton


-------------------------------------------------------------------------------------------------------
We build a simple online auction system in which users can bid on items for sale with the following functionality:

1. Users bid on an item where each new bid must be at a higher price than before
2. Get the current winning bid for an item
3. Get all bids for an item
4. Get all items on which a user has bid
5. Persistant storage
6. Menu in command line interface
-------------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------------
Run:
	java OnlineAuctionSystem

-------------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------------
Usage:
	A command line interface is implemented for the user to explore the functionality of the system.
Testing: 
	Enable by uncommenting testing() in DB.java contructor, then run:
	javac DB.java
	java DB
	(See DB.java testing() for explanations)
-------------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------------
Description:
	-The sytem is broken into two classes - DB and Main.
		DB handles all data storage, manipulation and retrieval.
		Main handles user interactions.
	-DB entries get passed around as subclasses of DBentry objects so that functionality can be easily added to their classes.
	-Sufficient error catching is implemented at DB level to ensure the system doesnt crash upon invalid operations.
	-3 tables (.txt) are used for storage. Each entry is a string tuple of its fields. Id's are used to easily retreive related data in different tables.
-------------------------------------------------------------------------------------------------------
