/**
 * @author Anthony Peters
 *
 * Driver class used to create a Register object then run it, after it finishes running it will exit the program
 */

public class HW4_peters {

    public static void main(String[] args) {
        // Create register object instance
        Register register = new Register();

        // Run register object
        register.run();

        // Finish
        System.exit(0);
    }

}
