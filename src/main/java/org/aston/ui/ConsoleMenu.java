package org.aston.ui;

import org.aston.model.User;
import org.aston.service.UserService;

import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();

    public void start() {
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
        System.out.println("Пользователь добавлен!");
    }

    private void listUsers() {
        userService.getAllUsers().forEach(
                u -> System.out.printf("[%d] %s (%s)%n", u.getId(), u.getName(), u.getEmail())
        );
    }

    private void findUser() {
        System.out.print("Введите ID: ");
        long id = Long.parseLong(scanner.nextLine());

        User user = userService.getUserById(id);
        if (user != null)
            System.out.printf("ID: %d | Имя: %s | Email: %s%n", user.getId(), user.getName(), user.getEmail());
        else
            System.out.println("Пользователь не найден.");
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
        System.out.println(updated ? "Обновлено!" : "Пользователь не найден.");
    }

    private void deleteUser() {
        System.out.print("Введите ID: ");
        long id = Long.parseLong(scanner.nextLine());

        boolean deleted = userService.deleteUser(id);
        System.out.println(deleted ? "Удалено." : "Пользователь не найден.");
    }
}
