package com.manulaiko.filenator.launcher;

import java.io.File;

import com.manulaiko.filenator.utils.Settings;
import com.manulaiko.tabitha.Console;

/**
 * Main class.
 *
 * Application's entry point.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Main
{
    /**
     * Settings object.
     */
    public static final Settings settings = new Settings();

    /**
     * Application's version.
     */
    public static final String version = "1.0.0";

    /**
     * Main method.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        Main._parseArguments(args);
        Console.debug = Main.settings.debug;

        Filenator f = new Filenator(
                Main.settings.files,
                Main.settings.directories,
                Main.settings.recursive
        );


        Console.println("Calculating file hashes, this my take a while depending on amount and size of files...");
        f.prepare();

        Console.println("FIREEEEE!!!!");
        f.fire();
    }

    /**
     * Parses command line arguments.
     *
     * @param args Command line arguments.
     */
    private static void _parseArguments(String[] args)
    {
        boolean nextArgIsFile = false;
        for(String arg : args) {
            if(nextArgIsFile) {
                nextArgIsFile = false;
                Main._parseFileArgument(arg);

                continue;
            }

            switch(arg.toLowerCase())
            {
                case "-d":
                case "--debug":
                    Main.settings.debug = true;
                    Console.debug("Running on debug mode.");

                    break;

                case "-r":
                case "--recursive":
                    Main.settings.recursive = true;
                    Console.debug("Running in recursive mode.");

                    break;

                case "-f":
                case "--file":
                    nextArgIsFile = true;

                    break;

                case "-h":
                case "--help":
                    Main.printHelp();
                    System.exit(0);

                    break;

                default:
                    Main._parseDirectoryArgument(arg);

                    break;
            }
        }
    }

    /**
     * Prints help page.
     */
    public static void printHelp()
    {
        String help = "Filenator "+ Main.version +" by Manulaiko\n" +
                      Console.LINE_EQ +"\n" +
                      "Deletes duplicate files on a directory.\n" +
                      "Usage:\n" +
                      "    filenator [args] [directories_to_check]\n" +
                      "\n" +
                      "Available arguments:\n" +
                      "\t-d         --debug      Runs the application in debug mode\n" +
                      "\t-h         --help       Prints this help\n" +
                      "\t-f [file] --file [file] Deletes also the duplicates of [file], if is a directory,\n" +
                      "\t                        all duplicated files of the directory will be deleted\n" +
                      "\t-r         --recursive  Runs recursively between sub directories\n" +
                      "\n" +
                      "Examples:" +
                      "\tfilenator -r ./Images\n" +
                      "\tfilenator -f ./SpydermanImages -f ./BaitImages ./4ChumThread\n" +
                      "\n" +
                      "GitHub repo: https://github.com/manulaiko/Filenator";

        Console.println(help);
    }

    /**
     * Parses the `-f` argument file.
     *
     * @param path Path to file.
     */
    private static void _parseFileArgument(String path)
    {
        File f = new File(path);

        if(!f.exists()) {
            Console.debug(f.getAbsoluteFile() +" does not exist!");

            return;
        }

        if(f.isDirectory()) {
            Console.debug("Reading all files from "+ f.getAbsolutePath() +"...");

            for(File sf : f.listFiles()) {
                Main._parseFileArgument(sf.getAbsolutePath());
            }

            return;
        }

        Main.settings.files.add(f);
    }

    /**
     * Parses the default argument.
     *
     * @param path Argument path.
     */
    private static void _parseDirectoryArgument(String path)
    {
        File f = new File(path);

        if(!f.exists()) {
            Console.println(f.getAbsoluteFile() +" does not exist!");

            return;
        }

        if(!f.isDirectory()) {
            Console.debug("The directory is a file, adding it to the files list.");
            Main.settings.files.add(f);

            return;
        }

        Main.settings.directories.add(f);
    }
}
