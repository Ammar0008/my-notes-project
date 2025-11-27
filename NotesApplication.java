package com.notes;

import java.util.Scanner;

public class NotesApplication {
    public static void main(String[] args) {
        NotesManager manager = new NotesManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Notes App ===");
            System.out.println("1. Add Note");
            System.out.println("2. View Notes");
            System.out.println("3. Edit Note");
            System.out.println("4. Delete Note");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter note text: ");
                    String text = scanner.nextLine();
                    manager.addNote(text);
                    System.out.println("Note added!");
                    break;

                case 2:
                    System.out.println("\nYour Notes:");
                    for (int i = 0; i < manager.getAllNotes().size(); i++) {
                        System.out.println((i + 1) + ". " + manager.getAllNotes().get(i).getText());
                    }
                    break;

                case 3:
                    System.out.print("Enter note number to edit: ");
                    int editIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    System.out.print("Enter new text: ");
                    String newText = scanner.nextLine();
                    manager.editNote(editIndex, newText);
                    break;

                case 4:
                    System.out.print("Enter note number to delete: ");
                    int deleteIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    manager.deleteNote(deleteIndex);
                    break;

                case 5:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
