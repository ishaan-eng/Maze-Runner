package Interface;

import Maze.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Console                        //This class is a console wrapper for user interaction. Reads an input and prints the output to the console.
{                                           //Stores a maze internally.
    private Scanner scanner;
    private Model maze;
    private boolean isMazeAvailable = false;

    /**
     * An endless loop that prints available options and
     * processes user input. All available options are:
     * <p>
     * 1. Generate a new maze<br>
     * 2. Load a maze<br>
     * 3. Save the maze<br>
     * 4. Display the maze<br>
     * 5. Find the escape<br>
     * <p>
     * The last three are available only if
     * {@code isMazeAvailable == true}.
     */
    public void start() 
    {
        scanner = new Scanner(System.in);
        while (true) 
        {
            help();
            try 
            {
                var choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) 
                {
                    case 0:
                        exit();
                        return;
                    case 1:
                        generate();
                        break;
                    case 2:
                        load();
                        break;
                    case 3:
                        save();
                        break;
                    case 4:
                        display();
                        break;
                    case 5:
                        findEscape();
                        break;
                    default:
                        System.out.println("Incorrect option. Please try again");
                        break;
                }
            } 
          catch (InputMismatchException e) 
          {
                System.out.println("Incorrect option. Please try again");
          }
          catch (Exception e) 
          {
                System.out.println("Unknown error");
          }
        }
    }

    private void help() 
    {
        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        if (isMazeAvailable) 
        {
            System.out.println("3. Save the maze");
            System.out.println("4. Display the maze");
            System.out.println("5. Find the escape");
        }
        System.out.println("0. Exit");
    }

    private void exit() 
    {
        scanner.close();
        System.out.println("Thankyou.. Bye!");
    }

    private void generate()          //Asks a user to enter the dimensions of the new maze and then generates and prints the new one.
    {
        System.out.println("Enter the size of the new maze (in the [size] or [height width] format)");
        var line = scanner.nextLine();
        var split = line.split(" ");
        if (split.length == 1) 
        {
            var size = parseInt(split[0]);
            maze = new Model(size);
        } 
      else if (split.length == 2) 
      {
            var height = parseInt(split[0]);
            var width = parseInt(split[1]);
            maze = new Model(height, width);
      } 
      else 
      {
            System.out.println("Cannot generate a maze. Invalid size");
      }
      isMazeAvailable = true;
      display();
    }

    private void load()                          //Asks for a filename and then loads the serialized maze from the corresponding file which replaces the old one.
    {
        System.out.println("Enter the filename");
        var filename = scanner.nextLine();
        try 
        {
            var content = Files.readString(Paths.get(filename));
            maze = Model.load(content);
            isMazeAvailable = true;
            System.out.println("The maze is loaded");
        } 
        catch (IOException e)
        {
            System.out.println("The file " + filename + " does not exist");
        }
        catch (IllegalArgumentException e) 
        {
            System.out.println(e.getMessage());
        }
    }

    private void save()                         //Asks for a filename and then saves the serialized maze to the corresponding file.
    {
        System.out.println("Enter the filename");
        var filename = scanner.nextLine();
        try 
        {
            var export = maze.export();
            Files.write(Paths.get(filename), export.getBytes());
            System.out.println("The maze is saved");
        } 
       catch (IOException e) 
       {
            System.out.println("Cannot write to file " + filename);
       }
    }

    private void display()                    //Prints the current maze.
    {
        System.out.println(maze);
    }

    private void findEscape()                 //Prints the maze with its path from the entrance to the exit.
    {
        System.out.println(maze.findEscape());
    }


}
