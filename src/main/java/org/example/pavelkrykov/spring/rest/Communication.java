package org.example.pavelkrykov.spring.rest;


import org.example.pavelkrykov.spring.rest.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.Entity;
import java.util.List;

// Класс для взаимодействия с REST API пользователей
public class Communication {

    // URL API для работы с пользователями
    private final String URL = "http://94.198.50.185:7081/api/users";

    // Идентификатор сессии, который будет получен от сервера
    private String sessionId = "";

    // Инъекция RestTemplate для выполнения HTTP-запросов
    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    // Основной метод выполнения операций с API
    public void performOperations() {

        // 1. Шаг: Получение списка всех пользователей и сохранение сессии

        // Создаем объект для хранения HTTP-заголовков
        HttpHeaders headers = new HttpHeaders();

        // Выполняем GET-запрос для получения списка пользователей
        // new HttpEntity<>(headers) - создаем HTTP-сущность с заголовками
        // String.class - ожидаем ответ в виде строки
        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        // Извлекаем JSESSIONID из заголовков ответа:
        // 1. Получаем заголовок SET_COOKIE
        // 2. Разделяем строку по точке с запятой
        // 3. Берем первую часть (до точки с запятой)
        sessionId = response.getHeaders()
                .getFirst(HttpHeaders.SET_COOKIE)
                .split(";")[0];

        // Выводим полученный идентификатор сессии
        System.out.println("JSESSIONID: " + sessionId);
        // Выводим тело ответа (список пользователей в JSON)
        System.out.println("Полученные пользователи: " + response.getBody());

        // 2. Шаг: Настройка заголовков для последующих запросов

        // Устанавливаем Content-Type для отправки данных в формате JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Добавляем Cookie с идентификатором сессии
        headers.set("Cookie", sessionId);

        // 3. Шаг: Создание нового пользователя (POST)

        // Создаем объект пользователя
        User user = new User();
        // Устанавливаем ID пользователя
        user.setId(3L);
        // Устанавливаем имя пользователя
        user.setName("James");
        // Устанавливаем фамилию пользователя
        user.setLastName("Brown");
        // Устанавливаем возраст пользователя
        user.setAge((byte) 30);

        // Создаем HTTP-сущность с телом (пользователь) и заголовками
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        // Выполняем POST-запрос для создания пользователя и получаем ответ
        String code1 = restTemplate.postForEntity(URL, request, String.class).getBody();
        // Выводим первую часть кода
        System.out.println("Код после создания: " + code1);

        // 4. Шаг: Обновление пользователя (PUT)

        // Изменяем данные пользователя
        user.setName("Thomas");
        user.setLastName("Shelby");

        // Создаем HTTP-сущность с обновленными данными
        HttpEntity<User> updateUser = new HttpEntity<>(user, headers);
        // Выполняем PUT-запрос для обновления пользователя
        String code2 = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                updateUser,
                String.class
        ).getBody();
        // Выводим вторую часть кода
        System.out.println("Вторая часть кода " + code2);

        // 5. Шаг: Удаление пользователя (DELETE)

        // Создаем HTTP-сущность для удаления (тело не обязательно для DELETE)
        HttpEntity<User> deleteUser = new HttpEntity<>(user, headers);
        // Выполняем DELETE-запрос для удаления пользователя с ID=3
        String code3 = restTemplate.exchange(
                URL + "/3",  // Добавляем ID к URL
                HttpMethod.DELETE,
                deleteUser,
                String.class
        ).getBody();
        // Выводим третью часть кода
        System.out.println("ТРЕТЬЯ ЧАСТЬ КОДА " + code3);

        // 6. Шаг: Формирование итогового кода

        // Объединяем все три части кода в одну строку
        System.out.println("Итоговый код: " + code1 + code2 + code3);
    }
}

