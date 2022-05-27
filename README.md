# Инструкция подключения БД и запуска SUT
1. Склонировать проект из репозитория командой ``` git clone ```
1. Открыть склонированный проект в Intellij IDEA
1. Для запуска контейнеров с MySql использовать команду ``` docker-compose up -d --force-recreate ```
1. Запустить SUT введя в терминале команду

``` java -jar ./artifacts/aqa-shop.jar ```

5. Запуск тестов (Allure)
-  для запуска ввести команду

``` ./gradlew clean test allureReport ```

6. Открыть в Google Chrome ссылку http://localhost:8080
7. Для получения отчета Allure в браузере, ввести команду ``` ./gradlew allureServe ```
8. После окончания тестов завершить работу приложения (Ctrl+C), остановить контейнеры командой ``` docker-compose down ```