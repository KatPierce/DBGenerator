import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a number of rows you want to add: ");
        int num = scanner.nextInt();
        scanner.close();
        Generator gen = new Generator(num);
        gen.dbConnect();
        try {
            gen.insertTableCoach(gen.getnCoach());
        } catch (SQLException e) {
            System.out.println("Error filling table coach.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableCities(gen.getnCities());
        } catch (SQLException e) {
            System.out.println("Error filling table cities.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableCompetition(gen.getnCompetition());
        } catch (SQLException e) {
            System.out.println("Error filling table competition.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableCountry(gen.getnCountry());
        } catch (SQLException e) {
            System.out.println("Error filling table country.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableCountryCity(gen.getnCountryCity());
        } catch (SQLException e) {
            System.out.println("Error filling table country_city.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableFSkaters(gen.getnStrings());
        } catch (SQLException e) {
            System.out.println("Error filling table fskaters.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableProgram(gen.getnPrograms());
        } catch (SQLException e) {
            System.out.println("Error filling table program.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableResults(gen.getnResults());
        } catch (SQLException e) {
            System.out.println("Error filling table results.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableSeason(gen.getnSeason());
        } catch (SQLException e) {
            System.out.println("Error filling table season.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableSkaterCoach(gen.getnSkaterCoach());
        } catch (SQLException e) {
            System.out.println("Error filling table skater_coach.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableSkaterCompetition(gen.getnSkaterCompetition());
        } catch (SQLException e) {
            System.out.println("Error filling table skater_competition.\n Error message: " + e.getMessage());
            System.exit(1);
        }
        try {
            gen.insertTableSkaterCountry(gen.getnSkaterCountry());
        } catch (SQLException e) {
            System.out.println("Error filling table skater_country.\n Error message: " + e.getMessage());
            System.exit(1);
        }

        gen.dbDisconnect();

        System.out.println("Rows were added successfully");
    }

}
