package elysia.command;

import java.util.ArrayList;

import elysia.task.Task;
import elysia.ui.Ui;

/**
 * Represents a search command.
 */
public class FindCommand extends Command {
    private String searchContent;

    public FindCommand(String searchContent) {
        super();
        this.searchContent = searchContent;
    }

    /**
     * Finds the item in list if it matches or partially matches.
     *
     * @param tasks
     * @return
     */
    @Override
    public String execute(ArrayList<Task> tasks) {
        Ui ui = new Ui();

        ArrayList<Task> results = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String taskDescription = task.getDescription().toLowerCase();

            if (taskDescription.contains(searchContent)) {
                results.add(task);
            }
        }

        return ui.printList(results);
    }
}
