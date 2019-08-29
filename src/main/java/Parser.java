import java.io.IOException;
import java.util.ArrayList;

import duke.exception.DukeException;
import duke.task.Task;

/**
 * Represents the class responsible for parsing and evaluating user input.
 */
public class Parser {

    private Duke parent;
    private TaskList taskList;

    public Parser(Duke parent, TaskList taskList) {
        this.parent = parent;
        this.taskList = taskList;
    }

    /**
     * Evaluates the input entered by the user.
     *
     * @param input The user input
     * @throws DukeException If the input is invalid.
     */
    public void evaluateInput(String input) throws DukeException {
        if (input.equalsIgnoreCase("bye")) {
            parent.print("Bye. Have a nice day!");
        } else if (input.equalsIgnoreCase("list")) {
            parent.printTasks();
        } else if (input.toLowerCase().startsWith("done")) {
            evaluateDone(input);
        } else if (input.toLowerCase().startsWith("todo")
                || input.toLowerCase().startsWith("deadline")
                || input.toLowerCase().startsWith("event")) {
            taskList.addTask(input);
        } else if (input.toLowerCase().startsWith("delete")) {
            taskList.deleteTask(input);
        } else if (input.toLowerCase().startsWith("find")) {
            evaluateFind(input);
        } else {
            throw new DukeException("OOPS!!! I don't know what this is :(");
        }
    }

    /**
     * Evaluates the "Done" command.
     *
     * @param input The input with a "Done" command.
     * @throws DukeException If "Done" command is invalid.
     */
    public void evaluateDone(String input) throws DukeException {
        String number = input.substring(4, input.length()).strip();
        if (number.isEmpty()) {
            parent.print("Invalid input! Mention a valid task number.");
        } else {
            try {
                int taskNumber = Integer.parseInt(number);
                if (taskNumber > taskList.size()) {
                    parent.print("Task doesn't exist.");
                } else {
                    taskList.get(taskNumber - 1).markAsDone();
                    String output = "Nice! I've marked this task as done:"
                            + "\n  "
                            + taskList.get(taskNumber - 1).toString();
                    parent.print(output);
                }
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                throw new DukeException("OOPS!!! The Done command must be followed by a valid task ID.");
            }
        }
    }

    public void evaluateFind(String input) throws DukeException {
        String[] tokens = input.split("\\s");
        if (tokens.length <= 1) {
            throw new DukeException("Invalid find command");
        } else {
            ArrayList<Task> foundTasks = taskList.find(input.substring(4).strip());
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < foundTasks.size(); i++) {
                output.append((i + 1) + ". " + foundTasks.get(i).toString());
                if (i != foundTasks.size() - 1) {
                    output.append("\n");
                }
            }
            parent.print(output.toString());
        }
    }
}
