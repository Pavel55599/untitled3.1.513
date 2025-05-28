package org.example.pavelkrykov.spring.rest;


import org.example.pavelkrykov.spring.rest.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class Communication {

    private final String URL = "http://94.198.50.185:7081/api/users";
    private String sessionId = "";

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

//    public Communication(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public void performOperations() {

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).split(";")[0];
        System.out.println("JSESSIONID: " + sessionId);
        System.out.println("Полученные пользователи: " + response.getBody());
        headers.set("Cookie", sessionId);

        User user = new User();
        user.setId(3L);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte) 30);
        System.err.println(user);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        String code1 = restTemplate.postForEntity(URL, request, String.class).getBody();
        System.out.println("созданный пользователь " + request.getBody());
        System.out.println("Код после создания: " + code1);

        user.setName("Thomas");
        user.setLastName("Shelby");


        HttpEntity<User> updateUser = new HttpEntity<>(user, headers);
        String code2 = restTemplate.exchange(URL, HttpMethod.PUT, updateUser, String.class).getBody();
        System.out.println("обновленный пользователь " + updateUser.getBody());
        System.out.println("Вторая часть кода: " + code2);


// Добавляем ID к URL
        HttpEntity<User> deleteUser = new HttpEntity<>(user, headers);
        String code3 = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, deleteUser, String.class).getBody();
        System.out.println("удаленный пользователь " + deleteUser.getBody());
        System.out.println("ТРЕТЬЯ ЧАСТЬ КОДА: " + code3);

        System.out.println("Итоговый код: " + code1 + code2 + code3);
    }
}















/*
// 1. Создаём ПУСТЫЕ заголовки (пока без данных)
HttpHeaders headers = new HttpHeaders(); // → headers = {}

// 2. Отправляем GET-запрос с этими пустыми headers
ResponseEntity<String> response = restTemplate.exchange(
    URL,
    HttpMethod.GET,
    new HttpEntity<>(headers), // → Запрос с пустыми headers
    String.class
);

// 3. Сервер возвращает ответ с Set-Cookie
// (теперь headers ответа содержат JSESSIONID)
String rawCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
// → "JSESSIONID=abc123; Path=/; HttpOnly"

// 4. Извлекаем чистый JSESSIONID
sessionId = rawCookie.split(";")[0]; // → "JSESSIONID=abc123"

// 5. Теперь ДОБАВЛЯЕМ куку в headers!
headers.set("Cookie", sessionId); // → headers = { Cookie: "JSESSIONID=abc123" }


🌟 Ключевые моменты:

Изначально пусто
new HttpHeaders() создаёт пустой объект — это нормально для первого запроса.
Сервер даёт куку в ответе
После GET-запроса сервер присылает Set-Cookie в headers ответа — это не те же самые headers, что мы отправляли!
Мы копируем куку в наши headers
Строка headers.set("Cookie", sessionId) наполняет наш изначально пустой объект headers.
Дальше эти headers используются
Теперь все последующие запросы (POST/PUT/DELETE) будут отправляться с этим заголовком:
http
Cookie: JSESSIONID=abc123
❓ Почему так сделано?

Первый GET-запрос — это как "знакомство" с сервером (нам не нужно ничего отправлять, кроме пустых headers).
Сервер в ответ говорит: "Вот твой идентификатор сессии (JSESSIONID), используй его в следующих запросах".
Все дальнейшие запросы уже отправляются с этим идентификатором.
*/


//public class Communication {
//
//    // URL  для работы с пользователями
//    private final String URL = "http://94.198.50.185:7081/api/users";
//
//    // Идентификатор сессии, который будет получен от сервера
//    private String sessionId = "";
//
//    // Инъекция RestTemplate , который находится в классе MyConfig для выполнения HTTP-запросов
//    @Autowired
//    private RestTemplate restTemplate = new RestTemplate();
//
//    // Основной метод выполнения операций с API
//    public void performOperations() {
//
//        // 1. Шаг: Получение списка всех пользователей и сохранение сессии
//
//        // Создаем объект для хранения HTTP-заголовков
//        HttpHeaders headers = new HttpHeaders();
//
//        // Выполняем GET-запрос для получения списка пользователей
//        // new HttpEntity<>(headers) - создаем HTTP-сущность с заголовками
//        // String.class - ожидаем ответ в виде строки
//        ResponseEntity<String> response = restTemplate.exchange(
//                URL,
//                HttpMethod.GET,
//                new HttpEntity<>(headers),
//                String.class
//        );
//
//        // Извлекаем JSESSIONID из заголовков ответа:
//        // 1. Получаем заголовок SET_COOKIE
//        // 2. Разделяем строку по точке с запятой
//        // 3. Берем первую часть (до точки с запятой)
//        sessionId = response.getHeaders()
//                .getFirst(HttpHeaders.SET_COOKIE)
//                .split(";")[0];
//
//        // Выводим полученный идентификатор сессии
//        System.out.println("JSESSIONID: " + sessionId);
//        // Выводим тело ответа (список пользователей в JSON)
//        System.out.println("Полученные пользователи: " + response.getBody());
//
//        // 2. Шаг: Настройка заголовков для последующих запросов
//
//        // Устанавливаем Content-Type для отправки данных в формате JSON
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        // Добавляем Cookie с идентификатором сессии
//        headers.set("Cookie", sessionId);
//
//        // 3. Шаг: Создание нового пользователя (POST)
//
//        // Создаем объект пользователя
//        User user = new User();
//        // Устанавливаем ID пользователя
//        user.setId(3L);
//        // Устанавливаем имя пользователя
//        user.setName("James");
//        // Устанавливаем фамилию пользователя
//        user.setLastName("Brown");
//        // Устанавливаем возраст пользователя
//        user.setAge((byte) 30);
//
//        // Создаем HTTP-сущность с телом (пользователь) и заголовками
//        HttpEntity<User> request = new HttpEntity<>(user, headers);
//        // Выполняем POST-запрос для создания пользователя и получаем ответ
//        String code1 = restTemplate.postForEntity(URL, request, String.class).getBody();
//        // Выводим первую часть кода
//        System.out.println("Код после создания: " + code1);
//
//        // 4. Шаг: Обновление пользователя (PUT)
//
//        // Изменяем данные пользователя
//        user.setName("Thomas");
//        user.setLastName("Shelby");
//
//        // Создаем HTTP-сущность с обновленными данными
//        HttpEntity<User> updateUser = new HttpEntity<>(user, headers);
//        // Выполняем PUT-запрос для обновления пользователя
//        String code2 = restTemplate.exchange(
//                URL,
//                HttpMethod.PUT,
//                updateUser,
//                String.class
//        ).getBody();
//        // Выводим вторую часть кода
//        System.out.println("Вторая часть кода " + code2);
//
//        // 5. Шаг: Удаление пользователя (DELETE)
//
//        // Создаем HTTP-сущность для удаления (тело не обязательно для DELETE)
//        HttpEntity<User> deleteUser = new HttpEntity<>(user, headers);
//        // Выполняем DELETE-запрос для удаления пользователя с ID=3
//        String code3 = restTemplate.exchange(
//                URL + "/3",  // Добавляем ID к URL
//                HttpMethod.DELETE,
//                deleteUser,
//                String.class
//        ).getBody();
//        // Выводим третью часть кода
//        System.out.println("ТРЕТЬЯ ЧАСТЬ КОДА " + code3);
//
//        // 6. Шаг: Формирование итогового кода
//
//        // Объединяем все три части кода в одну строку
//        System.out.println("Итоговый код: " + code1 + code2 + code3);
//    }
//}

