# mysql-connect.py — скрипт для підключення до MySQL.
import MySQLdb # Імпортування модуля для роботи з MySQL.
mysql = MySQLdb.connect(host="localhost", user="root", passwd="21000", db="mysite", charset='utf8') # Підключення до MySQL.
cursor = mysql.cursor() # Формування курсору, за допомогою якого можна виконувати SQL-запити.