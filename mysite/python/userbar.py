# userbar.py — скрипт для формування у тілі сторінки блоку для виведення користувачу даних про його авторизацію.
page['body'] += '<div class="userbar">' # Початок блоку, в якому пишеться про те, чи користувач залогінений.
if user == "guest": # Якщо користувач є гостем.
	page['body'] += 'Привіт, госте! Якщо ти зареєстрований, то можеш <a href="./index.py?action=login">увійти</a>.'
else: # Якщо користувач залогінений.
	page['body'] += 'Ви увійшли як ' + user + '. <a href="./index.py?action=profile" style="color:#C2D2D2;">Профіль</a>. <a href="./index.py?action=logout" style="color:669999;">Вийти.</a>'
page['body'] += '</div>' # Закінчення блоку "userbar".