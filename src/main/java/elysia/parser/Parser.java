package elysia.parser;

import java.time.LocalDate;

import elysia.command.Command;
import elysia.command.DeadlineCommand;
import elysia.command.DeleteCommand;
import elysia.command.ElysiaCommand;
import elysia.command.EventCommand;
import elysia.command.ExitCommand;
import elysia.command.FindCommand;
import elysia.command.MarkCommand;
import elysia.command.PrintListCommand;
import elysia.command.ToDoCommand;
import elysia.command.UnmarkCommand;
import elysia.exception.EmptyDescriptionException;
import elysia.exception.InvalidDateFormatException;
import elysia.exception.UnknownCommandException;

/**
 * Parses the user's commands into respective commands.
 */
public class Parser {
    private Command command;

    public Command parseCommand(String input)
            throws EmptyDescriptionException, InvalidDateFormatException, UnknownCommandException {
        String[] str = input.split(" ");
        String token = str[0].toLowerCase();

        switch (token) {
        case "elysia":
            parseElysiaCommand();
            break;
        case "list":
            parsePrintListCommand();
            break;
        case "todo":
            parseTodo(input);
            break;
        case "deadline":
            parseDeadLine(input);
            break;
        case "event":
            parseEvent(input);
            break;
        case "mark":
            parseMarkEvent(input);
            break;
        case "unmark":
            parseUnmarkEvent(input);
            break;
        case "delete":
            parseDeleteCommand(input);
            break;
        case "find":
            parseFindCommand(input);
            break;
        case "bye":
            parseExitCommand();
            break;
        default:
            throw new UnknownCommandException();
        }

        return command;
    }

    private void parseElysiaCommand() {
        command = new ElysiaCommand();
    }

    private void parseExitCommand() {
        command = new ExitCommand();
    }

    private void parseFindCommand(String input) {
        this.command = new FindCommand(input.substring(5).trim());
    }

    private void parsePrintListCommand() {
        this.command = new PrintListCommand();
    }

    private void parseTodo(String str) throws EmptyDescriptionException {
        String input = str.substring(4).trim();
        if (input.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        this.command = new ToDoCommand(input);
    }

    private void parseDeadLine(String str) throws EmptyDescriptionException, InvalidDateFormatException {
        String trimmed = str.substring(8).trim();
        String[] inputArray = trimmed.split("/by ");
        String description = inputArray[0].trim();

        if (trimmed.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }

        LocalDate date = DateParser.parseDate(inputArray[1]);

        if (date == null) {
            throw new InvalidDateFormatException();
        }

        this.command = new DeadlineCommand(description, date);
    }

    private void parseEvent(String str) throws EmptyDescriptionException {
        String trimmed = str.substring(5).trim();
        if (trimmed.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        String[] inputArray = trimmed.split("/from | /to ");
        String description = inputArray[0].trim();
        String startTime = inputArray[1];
        String endTime = inputArray[2];


        this.command = new EventCommand(description, startTime, endTime);

    }

    private void parseMarkEvent(String input) {
        int index = Integer.parseInt(String.valueOf(input.charAt(5))) - 1;
        command = new MarkCommand(index);
    }

    private void parseUnmarkEvent(String input) {
        int index = Integer.parseInt(String.valueOf(input.charAt(7))) - 1;
        command = new UnmarkCommand(index);
    }

    private void parseDeleteCommand(String input) {
        int index = Integer.parseInt(input.substring(7)) - 1;
        command = new DeleteCommand(index);
    }
}
