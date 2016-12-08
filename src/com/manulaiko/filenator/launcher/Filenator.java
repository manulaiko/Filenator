package com.manulaiko.filenator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;

import com.manulaiko.tabitha.Console;

/**
 * Performs the actual file deleting.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Filenator
{
    /**
     * Files to compare.
     */
    private ArrayList<File> _files;

    /**
     * Directories to check.
     */
    private ArrayList<File> _directories;

    /**
     * Whether we're running on recursive mode or not.
     */
    private boolean _recursive;

    /**
     * Files compared so far.
     */
    private ArrayList<String> _hashes = new ArrayList<>();

    /**
     * Duplicated files.
     */
    private ArrayList<String> _duplicates = new ArrayList<>();

    /**
     * Parsed files.
     */
    private int _filesParsed;

    /**
     * Constructor.
     *
     * @param files       Files to compare.
     * @param directories Directories to check.
     * @param recursive   Whether we're running on recursive mode or not.
     */
    public Filenator(ArrayList<File> files, ArrayList<File> directories, boolean recursive)
    {
        this._files       = files;
        this._directories = directories;
        this._recursive   = recursive;
    }

    /**
     * Prepares file's hashes.
     */
    public void prepare()
    {
        this._files.forEach((f)->{
            String hash = this.hash(f);

            this._hashes.add(hash);
        });
    }

    /**
     * Performs the deleting
     */
    public void fire()
    {
        this._directories.forEach((f)->{
            Console.println("Parsing directory "+ f.getAbsolutePath() +"...");
            this._parseDir(f);
        });

        Console.println("Successfully parsed "+ this._filesParsed +"!");
        Console.println("Found "+ this._duplicates.size() +" duplicated files!");
        Console.println("Destroying with fire the duplicated files...");

        this._duplicates.forEach((p)->{
            File f = new File(p);

            try {
                if(!f.delete()) {
                    Console.println("Couldn't delete " + f.getAbsolutePath() + ", check directory permissions!");
                }
            } catch(Exception e) {
                Console.println("Couldn't delete " + f.getAbsolutePath() + ", check directory permissions!");
                Console.debug(e.getMessage());
            }
        });

        Console.println("Duplicated files killed with lasers and destroyed with fire!");
    }

    /**
     * Parses a directory.
     *
     * @param dir Directory's file object.
     */
    private void _parseDir(File dir)
    {
        for(File f : dir.listFiles()) {
            if(f.isDirectory()) {
                if(!this._recursive) {
                    continue;
                }

                Console.println("Parsing subdirectory "+ f.getAbsolutePath() +"...");
                this._parseDir(f);

                continue;
            }

            String hash = this.hash(f);
            this._filesParsed++;

            if(this._hashes.contains(hash)) {
                this._duplicates.add(f.getAbsolutePath());

                continue;
            }

            this._hashes.add(hash);
        }
    }

    /**
     * Returns the checksum of a file.
     *
     * @param file File object.
     *
     * @return File's MD5 checksum bytes.
     *
     * @throws Exception If anything goes wrong.
     */
    private byte[] _checksum(File file) throws Exception
    {
        MessageDigest   complete = MessageDigest.getInstance("MD5");
        FileInputStream fis      = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        int    numRead;

        do {
            numRead = fis.read(buffer);
            if(numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while(numRead != -1);

        fis.close();

        return complete.digest();
    }

    /**
     * Returns the MD5 hash of a file.
     *
     * @param file File object.
     *
     * @return File's MD5 checksum
     */
    public String hash(File file)
    {
        try {
            byte[] b = this._checksum(file);
            String result = "";

            for(int i = 0; i < b.length; i++) {
                result += Integer.toString((b[i] & 0xff) + 0x100, 16)
                                 .substring(1);
            }
            return result;
        } catch(Exception e) {
            Console.debug("Couldn't calculate checksum for "+ file.getAbsolutePath());
            Console.debug(e.getMessage());

            return "";
        }
    }
}
