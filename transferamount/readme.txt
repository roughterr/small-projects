1. The run the solution use a method named main in a class named App. The method in question is still named transferAmount of the class AccountService.

2. The system uses an OrientDB database. Database connection parameters are:
  database name - masrdb
  login - root
  password - 1111
  
3. One disadvantage of the solution is that sometimes 2 entries can be modified simultaneously. In this case a ConcurrentModificationException can be thrown. The problem can be solved by retries. The limit of retries is up to the developer.

4. The tranfer operation is atomic, since the operation is performed with a transaction. Although the transaction is optimistic, the data will be still correct, since OrientDB supports version of records (in SQL databases called rows). If at the moment of the commit operation, the current version of the record has changed, the whole transaction will be aborted, and to perform the operation it should be started again.