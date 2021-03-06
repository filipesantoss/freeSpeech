package org.academiadecodigo.bootcamp8.freespeech.shared.utils;

import org.academiadecodigo.bootcamp8.freespeech.shared.message.SealedSendable;

import java.io.*;
import java.security.Key;

/**
 * @author by André Martins <Code Cadet>
 *         Project freeSpeach (26/06/17)
 *         <Academia de Código_>
 */
public class Stream {

    /**
     * Write an object to stream
     * Needs an object stream
     *
     * @param out     destination object stream
     * @param message object to send
     */
    public static void write(ObjectOutputStream out, Object message) {

        // Don't allow send of null messages
        if (message == null) {
            return;
        }

        try {

            out.writeObject(message);
            out.flush();

        } catch (IOException e) {
            System.err.println("Socket abruptly closed!");
        }

    }

    /**
     * Read an object from the stream
     * Needs an object stream
     *
     * @param in source object stream
     * @return the received object
     */
    public static Object read(ObjectInputStream in) {

        Object object = null;

        try {

            object = in.readObject();

        } catch (IOException e) {
            System.err.println("Socket abruptly closed!");
        } catch (ClassNotFoundException e) {
            System.err.println("Communication protocol violation!");
        } finally {
            if (!(object instanceof SealedSendable || object instanceof Key)) {
                object = null;
            }
        }

        return object;

    }

    /**
     * Cast object to sealed sendable
     *
     * @param in source object stream
     * @return the received object
     * @see Stream#read(ObjectInputStream)
     */
    public static SealedSendable readSendable(ObjectInputStream in) {
        return (SealedSendable) read(in);
    }

    /**
     * Close the stream
     *
     * @param stream to close
     */
    public static void close(Closeable stream) {

        try {

            if (stream != null) {
                stream.close();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}
