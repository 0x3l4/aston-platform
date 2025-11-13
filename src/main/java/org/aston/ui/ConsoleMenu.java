package org.aston.ui;

import org.aston.model.User;
import org.aston.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleMenu.class);
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();

    public void start() {
        logger.info("Console menu started");
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addUser();
                case "2" -> listUsers();
                case "3" -> findUser();
                case "4" -> updateUser();
                case "5" -> deleteUser();
                case "0" -> {
                    System.out.println("Выход...");
                    logger.info("Menu terminated by user");
                    return;
                }
                default -> System.out.println("Некорректный ввод, попробуйте снова.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== Меню пользователей =====");
        System.out.println("1. Добавить пользователя");
        System.out.println("2. Показать всех пользователей");
        System.out.println("3. Найти пользователя по ID");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void addUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        userService.addUser(name, email, age);
        logger.info("User creation request: {} {} {}", name, email, age);
    }

    private void listUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(u -> System.out.printf("[%d] %s %d (%s)%n", u.getId(), u.getName(), u.getAge(), u.getEmail()));
        logger.info("Displayed {} users", users.size());
    }

    private void findUser() {
        System.out.print("Введите ID: ");
        long id = Long.parseLong(scanner.nextLine());

        User user = userService.getUserById(id);
        if (user != null)
            System.out.printf("ID: %d | Имя: %s | Возраст: %d | Email: %s%n", user.getId(), user.getName(), user.getAge(), user.getEmail());

        else
            System.out.println("Пользователь не найден.");

        logger.info("Find request by id {}", id);
    }

    private void updateUser() {
        System.out.print("Введите ID пользователя: ");
        long id = Long.parseLong(scanner.nextLine());

        System.out.print("Новое имя (пусто — без изменений): ");
        String name = scanner.nextLine();

        System.out.print("Новый email (пусто — без изменений): ");
        String email = scanner.nextLine();

        System.out.print("Новый возраст: (пусто - без изменений): ");
        int age = Integer.parseInt(scanner.nextLine());

        boolean updated = userService.updateUser(id, name, email, age);

        logger.info("Update request for id {}", id);
        System.out.println(updated ? "Обновлено!" : "Пользователь не найден.");
    }

    private void deleteUser() {
        System.out.print("Введите ID: ");
        long id = Long.parseLong(scanner.nextLine());

        boolean deleted = userService.deleteUser(id);

        logger.info("Delete request for id {}", id);
        System.out.println(deleted ? "Удалено." : "Пользователь не найден.");
    }
}
