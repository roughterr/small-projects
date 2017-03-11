#!/usr/bin/python3
# -*- coding: utf-8 -*-

import os # Завантаження модуля os — для роботи з файлами і змінними оточення.
import time # Завантаження модуля time — для роботи з часом.

def include(infile): # Функція, яка дозволяє включати інші скрипти в цей скрипт.
	global globals # Зчитати глобальні змінні у функцію.
	includefile = open(infile,"r",encoding='utf-8') # Відкриття файлу.
	exec (includefile.read(),globals()) # Зчитування і виконання файлу, "globals()" означає, що скрипт працюватиме із глобальними змінними.
	includefile.close() # Закриття файлу.
	
include('mysql-connect.py') # Підключення до MySQL.

# Перевірка чи користувача не забанено по IP.
sql = "select `time` from `banip` where `ip` = '" + os.environ["REMOTE_ADDR"] + "';" # Перевірка чи користувача не забанено по IP.
cursor.execute(sql) # Виконання SQL-запиту.
data = cursor.fetchall() # Скачування результатів SQL запиту.	
try: # Спробувати
	timeintable = int(data[0][0]) # вибрати перший результат запиту.
except: # Якщо відмостей про бан даного IP немає.
	del data,sql # Видалення використаних змінних і продовження роботи скрипта.
else: # Якщо в таблиці є інформація про даний IP.
	timenow = int(time.time()) # Перетворення поточного часу в формат цілого числа.
	if timenow > timeintable: # Якщо час бану уже минув.
		del timenow,timeintable,data,sql # Видалення використаних змінних і продовження роботи скрипта.
	else: # Якщо час бану ще не минув, то
		quit() # вихід з програми.

import sys # Завантаження модуля sys, який буде потрібен для виводу тексту.
import re # Завантаження модуля re — для роботи з текстом.
import hashlib # Завантаження модуля hashlib — для генерування хешів.

def out (message): # Функція для виводу тексту в кодуванні UTF-8.
	message = str(message) # Якщо, наприклад, числовий тип, то перетворення його в рядок.
	sys.stdout.buffer.write(message.encode('UTF8')) # Вивід тексту користувачу в кодуванні UTF-8.

# Витягування із бази даних назви сайту.
cursor.execute("select znachen from config where parametr = 'nazvasaitu';") # Запит до MySQL.
data = cursor.fetchone() # Скачування результату SQL запиту.	
try: # Спробувати
	nazvasaitu = (data[0]) # вибрати результат запиту.
except: # Якщо у таблиці немає даних про назву сайту, то
	nazvasaitu = "mysite" # використовуватиметься дефолтна назва.
del data # Видалення використаної змінної.

page = {} # Оголошення хешу, в якому зберігатимуться частини сторінки.
page['body'] = "" # Оголошення змінної, в якій зберігатиметься тіло сторінки.

def pageout (): # Оголошення функції для виводу HTML сторінки.
	include('pageout.py') # Власне вся функція у окремому файлі.

def error (error): # Функція для виводу повідомлення про помилку.
	global page # Ця функція повинна опрацювати лише хеш 'page'.
	includefile = open('error.py',"r",encoding='utf-8') # Відкриття файлу скрипта.
	exec (includefile.read()) # Виконання вмісту скрипта.

def sqladopt(a): # Функція для підготовки змінної для включення у SQL-запит.
	a = "'" + re.sub("'","''",a) + "'" # Заміна одної одинарної лапки на дві.
	return a

# Розбиття запиту на параметри.
query = {} # Створення хешу, в якому зберігатимуться параметри рядку запиту.
if os.environ['REQUEST_METHOD'] == "GET": # Якщо змінні запиту передаються методом GET:
	if os.environ['QUERY_STRING'] == "": # Якщо користувач не передав ніяких параметрів,
		query['action'] = "list" # то вивести список повідомлень.
	else: # Якщо рядок запиту не порожній, то
		query1 = os.environ['QUERY_STRING'].split("&") # розрізати рядок запиту по амперсандах.
		for line in query1: # Зчитування рядку запиту від амперсанду до амперсанду.
			parameter = line.split("=",1) # Розрізання на параметр і значення (розмежовуються знаком дорівнює).
			nameofparameter = parameter[0] # Ім'я параметру.
			try: # Перевірка
				valueofparameter = parameter[1] # чи запит зроблений у формі "ключ=значення".
			except: # Якщо ні, то
				error('400') # сказати користувачу про помилку вводу.
			else: # Інакше.
				query[nameofparameter] = valueofparameter # Зчитування параметру і його значення в хеш.
				del valueofparameter # Видалення використаних і уже непотрібних змінних.
			del parameter,nameofparameter # аналогічно передньому рядку.
		del query1
elif os.environ['REQUEST_METHOD'] == "POST": # Якщо змінні запиту передаються методом POST:
	import cgi # Завантаження модуля cgi, який необхідний для того, щоб зчитати дані, передані методом POST.
	form = cgi.FieldStorage() # Зчитування усіх даних методом POST.
	for row in form: # Почастинне зчитування даних форми.
		query[row] = form.getvalue(row) # Зчитування параметру і його значення в хеш.
	del form # Видалення змінної із параметрами, переданими мотодом POST, бо дані уже зчитані в хеш 'query'.
	del cgi,sys.modules['cgi'] # Вивантаження модуля 'cgi'.

# Розбиття коржика на параметри.
cookie = {} # Створення хешу, в якому зберігатимуться параметри з коржика.
if 'HTTP_COOKIE' in os.environ: # Перевірка чи коржик існує.
	cookie1 = os.environ['HTTP_COOKIE'].split("; ") # Розрізання коржику по знаках крапки з комою і
	for line in cookie1: # його зчитування по цих частинах.
		parameter = line.split("=",1) # Розрізання на параметр і значення (розмежовуються знаком дорівнює).
		nameofparameter = parameter[0] # Ім'я параметру.
		cookie[nameofparameter] = parameter[1] # Зчитування параметру і його значення в хеш.
		del parameter,nameofparameter # Видалення використаних і уже непотрібних змінних.
	del cookie1

# Ідентифікація користувача по коржику.
if 'login' in cookie: # Якщо дані є у коржику.
	user = cookie['login'].split("^",1) # Розрізати ці дані по знаку ^.
	try: # Перевірка, чи
		hashfromcookie = user[1] # хеш міститься в коржику.
	except: # Якщо ні, то
		user = "guest" # користувач стає гостем.
	else: # Якщо міститься.
		if not re.search(r'[^.a-z|0-9]',user[0]): # Перевірка чи бува логін в коржику не міститься SQL-ін'єкції.
			sql = "select hash,ugroup from users where login =" + sqladopt(user[0]) + ";" # Підготовка запиту на видачу правильного хешу.
			cursor.execute(sql) # Виконання SQL запиту.
			data = cursor.fetchone() # Скачування результатів SQL запиту.
			try: # Перевірка, чи 
				hashfromdb = data[0] # видав запит хеш.
			except: # Якщо ні, то користувач
				user = "guest" # стає гостем.
			else: # Якщо так.
				if hashfromdb == hashfromcookie: # Перевірка правильності хешу.
					user = user[0] # Якщо правильний, то користувач стає авторизованим.
					ugroup = data[1] # І зчитування у змінну його групової приналежності.
				else: # Якщо хеш неправильний, то
					user = "guest" # користувач стає гостем.
				del hashfromdb # Видалення змінної, в якій містився хеш з бази даних.
			del hashfromcookie	 # Видалення змінної, в якій містився хеш з коржика.
		else: # Якщо логін в коржику неправильний.
			user = "guest" # користувач стає гостем.
else: # Якщо даних про користувача у коржику немає, то
	user = "guest" # користувач стає гостем.

if user == "guest": # Якщо користувач — гість, то
	ugroup = "guest" # його група так само називається як і він.

if query['action'] == "login" or query['action'] == "logout": # Якщо користувач захотів увійти або вийти, то
	include('gate.py') # це обробляється виділеним скриптом.
elif query['action'] == "list":	# Якщо потрібно вивести користувачу список повідомлень.
	include('list.py')
elif query['action'] == "delete": # Якщо адміністратор захотів видалити повідомлення.
	include('delete.py')
elif query['action'] == "addpost": # Якщо адміністратор захотів опублікувати повідомлення.
	include('addpost.py')
elif query['action'] == "edit": # Якщо адміністратор захотів відредагувати повідомлення.
	include('edit.py')
elif query['action'] == "changecate": # Якщо адміністратор захотів змінити категорію повідомлення.
	include('changecate.py')
elif query['action'] == "catelist": # Якщо користувач захотів подивитись дописи лише з певної категорії.
	include('catelist.py')
elif query['action'] == "removecate": # Якщо адміністратор захотів видалити категорію.
	include('removecate.py')
elif query['action'] == "addcate": # Якщо адміністратор захотів створити нову категорію.
	include('addcate.py')
elif query['action'] == "managecate": # Якщо адміністратор захотів впорядкувати категорії.
	include('managecate.py')
elif query['action'] == "profile": # Якщо користувач захотів відредагувати свої особисті дані.
	include('profile.py')
elif query['action'] == "admin": # Якщо адміністратор захотів вналаштувати сайт.
	include('admin.py')
elif query['action'] == "rss": # Якщо потрібно видати стрічку новин.
	include('rss.py')
elif query['action'] == "showpost": # Якщо потрібно показати одне повідомлення.
	include('showpost.py')
else: # Якщо інша дія, то
	error('404') # помилка 404.