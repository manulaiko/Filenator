package com.manulaiko.filenator.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Settings class.
 *
 * Contains settings for the application.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Settings
{
    /**
     * Whether we're running on debug mode or not.
     */
    public boolean debug = false;

    /**
     * Whether we're running in recursive mode or not.
     */
    public boolean recursive = false;

    /**
     * Files from the `-f` argument.
     */
    public ArrayList<File> files = new ArrayList<>();

    /**
     * Directories to check
     */
    public ArrayList<File> directories = new ArrayList<>();
}
