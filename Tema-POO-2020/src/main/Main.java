package main;

import checker.Checkstyle;

import checker.Checker;
import common.Constants;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import setinputdata.SetInputData;

import org.json.simple.JSONArray;

import actions.Command;
import actions.Query;
import actor.ActorsDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import user.UsersDatabase;
import entertainment.VideosDatabase;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
       
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();
        
        SetInputData setData = new SetInputData(input); // used for initialising databases
        ActorsDatabase actorsDatabase = setData.setActorsDatabase(); // used for actors
                                                                     // management
        UsersDatabase usersDatabase = setData.setUsersDatabase(); // used for users
                                                                  // management
        VideosDatabase videosDatabase = setData.setVideosDatabase(); // used for movies
                                                                     // and shows
                                                                     // management
        
        // List of actions that will be performed.
        List<ActionInputData> actions = input.getCommands();

        // Perform each action based on its type.
        for (ActionInputData action : actions) {
            if (action.getActionType().equals("command")) {
                Command command = new Command(action);
                String message = command.executeCommand(usersDatabase, videosDatabase);
                arrayResult.add(fileWriter.writeFile(action.getActionId(), "", message));
            } else if (action.getActionType().equals("query")) {
                Query query = new Query(action);
                String message = query.executeQuery(usersDatabase, actorsDatabase,
                        videosDatabase);
                arrayResult.add(fileWriter.writeFile(action.getActionId(), "", message));
            }
        }

        fileWriter.closeJSON(arrayResult);
    }
}
