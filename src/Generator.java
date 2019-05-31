import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Random;


public class Generator {
    private int nStrings; // количество строк, которое хотим добавить в таблицу фигуристы
    private int nCoach ;
    private int nCountry ;
    private int nCities;
    private int nPrograms;
    private int nResults;
    private int nCompetition;
    private int nSeason;
    private int nCountryCity;
    private int nSkaterCoach;
    private int nSkaterCountry;
    private int nSkaterCompetition;

    private Connection dbcon;

    public Generator(int n) {
        this.nStrings = n;
        this.nCoach = 2*n;
        this.nCountry = 2*n;
        this.nCities = n/2;
        this.nPrograms = n*10;
        this.nResults = n*5;
        this.nCompetition = n/2;
        this.nSeason = 5*n;
        this.nCountryCity = n/2;
        this.nSkaterCoach = 4*n;
        this.nSkaterCountry = 4*n;
        this.nSkaterCompetition = 4*n;


    }

    void dbConnect() throws ClassNotFoundException {
        String dbHost = "jdbc:postgresql://localhost:5432/fsbase";
        String userName = "postgres";
        String password = "asdasdasd";
        Class.forName("org.postgresql.Driver");
        try {
            dbcon = DriverManager.getConnection(dbHost, userName, password);
            System.out.println("Connection was successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dbDisconnect() {
        try {
            if (dbcon != null) {
                dbcon.close();
                System.out.println("Disconnection was successful");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // для позиций
    private int generateInt(int from, int to) {
        return (from + (int) (Math.random() * to));
    }

    //для названий и имен
    private String generateString(int len) {
        String rus = "абвгдеёжзийклмнопрстуфхцчъыьэюя";
        String eng = "abcdefghijklmnopqrstuvwxyz";
        String symbols = rus + rus.toUpperCase() +
                eng + eng.toUpperCase();
        Random rnd = new Random();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(symbols.charAt(rnd.nextInt(symbols.length())));
        }
        return sb.toString();
    }

    //для пола
    private char generateSex() {
        int a = generateInt(1, 2);
        return (a == 1) ? 'F' : 'M';
    }

    //для очков
    private double generateScore(int from, int to) {
        double temp = from + (Math.random() * to);
        return new BigDecimal(temp).setScale(2, RoundingMode.UP).doubleValue();
    }

    //для года
    private String generateYear() {
        int d = generateInt(0, 20);
        int from = 2000 + d;
        int to = from + 1;
        String year = Integer.toString(from) + '-' + (to);
        return year;
    }

    //для дат
    private LocalDate generateDate(int from, int to) {
        Random random = new Random();
        int minDay = (int) LocalDate.of(from, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(to, 12, 31).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    //заполнение таблиц
    void insertTableCoach(int nCoach) throws SQLException {
        int nextId = getMaxId("coach");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO coach( id, name, surname ) VALUES(?, ?, ?)");
        for (int i = 0; i < nCoach; i++) {
            nextId++;
            String name = generateString(5);
            String surname = generateString(5);
            statement.setInt(1, nextId);
            statement.setString(2, name);
            statement.setString(3, surname);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nCoach + " row(s) were added to table coach");
    }

    void insertTableCities(int nStrings) throws SQLException {
        int nextId = getMaxId("cities");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO cities( id, city) VALUES(?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            String city = generateString(10);
            statement.setInt(1, nextId);
            statement.setString(2, city);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table cities");
    }

    void insertTableCompetition(int nStrings) throws SQLException {
        int nextId = getMaxId("competition");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO competition( id, name, place,date) VALUES(?, ?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            String name = generateString(15);
            int place = generateIdCities();
            LocalDate date = generateDate(2018, 2020);
            statement.setInt(1, nextId);
            statement.setString(2, name);
            statement.setInt(3, place);
            statement.setObject(4, date);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table competition");
    }

    void insertTableCountry(int nStrings) throws SQLException {
        int nextId = getMaxId("country");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO country( id, name) VALUES(?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            String country = generateString(10);
            statement.setInt(1, nextId);
            statement.setString(2, country);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table country");
    }

    void insertTableCountryCity(int nStrings) throws SQLException {
        int nextId = getMaxId("country_city");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO country_city( id, id_city, id_country ) VALUES(?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            int id_city = generateIdCities();
            int id_country = generateIdCountry();
            statement.setInt(1, nextId);
            statement.setInt(2, id_city);
            statement.setInt(3, id_country);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table country_city");
    }

    void insertTableFSkaters(int nStrings) throws SQLException {
        int nextId = getMaxId("fskaters");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO fskaters( id, name, surname, birthday, sex ) VALUES(?, ?, ?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            String name = generateString(10);
            String surname = generateString(10);
            LocalDate bday = generateDate(1980, 2005);
            char sex = generateSex();
            statement.setInt(1, nextId);
            statement.setString(2, name);
            statement.setString(3, surname);
            statement.setObject(4, bday);
            statement.setObject(5, sex, Types.OTHER);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table fskaters");
    }

    void insertTableProgram(int nStrings) throws SQLException {
        int nextId = getMaxId("program");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO program( id, position, element_score, component_score, deductions,total_score) VALUES(?, ?, ?, ?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            int position = generateInt(1, 50);
            double element_score = generateScore(0, 110);
            double component_score = generateScore(0, 100);
            int deductions = generateInt(0, -3);
            double total_score = generateScore(0, 210);
            statement.setInt(1, nextId);
            statement.setInt(2, position);
            statement.setDouble(3, element_score);
            statement.setDouble(4, component_score);
            statement.setDouble(5, deductions);
            statement.setDouble(6, total_score);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table program");
    }

    void insertTableResults(int nStrings) throws SQLException {
        int nextId = getMaxId("results");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO results( id, position, total,id_skater, short_program,free_program) VALUES(?, ?, ?, ?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            int position = generateInt(1, 50);
            double total = generateScore(0, 315);
            int id_skater = generateIdSkaterCompetition();
            int short_program = generateIdProgram();
            int free_program = generateIdProgram();
            statement.setInt(1, nextId);
            statement.setInt(2, position);
            statement.setDouble(3, total);
            statement.setInt(4, id_skater);
            statement.setInt(5, short_program);
            statement.setInt(6, free_program);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table results");
    }

    void insertTableSeason(int nStrings) throws SQLException {
        int nextId = getMaxId("season");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO season( id, highest_position, year,best_score_per_season, best_score_sp,best_score_fp,fskater) VALUES(?, ?, ?, ?, ?, ?,?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            int highest_position = generateInt(1, 50);
            String year = generateYear();
            double best_score_per_season = generateScore(0, 340);
            double best_score_sp = generateScore(0, 120);
            double best_score_fp = generateScore(0, 220);
            int id_skater = generateIdFSkaters();
            statement.setInt(1, nextId);
            statement.setInt(2, highest_position);
            statement.setString(3, year);
            statement.setDouble(4, best_score_per_season);
            statement.setDouble(5, best_score_sp);
            statement.setDouble(6, best_score_fp);
            statement.setInt(7, id_skater);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table season");
    }

    void insertTableSkaterCoach(int nStrings) throws SQLException {
        int nextId = getMaxId("skater_coach");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO skater_coach( id, id_skater, id_coach, since, until) VALUES(?, ?, ?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            int id_skater = generateIdFSkaters();
            int id_coach = generateIdCoach();
            LocalDate since = generateDate(2000, 2018);
            int d = generateInt(0, 20);
            LocalDate until = since.plusYears(d);
            statement.setInt(1, nextId);
            statement.setInt(2, id_skater);
            statement.setInt(3, id_coach);
            statement.setObject(4, since);
            statement.setObject(5, until);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table skater_coach");
    }

    void insertTableSkaterCompetition(int nStrings) throws SQLException {
        int nextId = getMaxId("skater_competition");
            PreparedStatement statement = dbcon.prepareStatement("INSERT INTO skater_competition( id, checkin_day, id_skater, id_competition) VALUES(?, ?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            int id_skater = generateIdFSkaters();
            int id_competition = generateIdCompetition();
            LocalDate day = generateDate(2000, 2019);
            statement.setInt(1, nextId);
            statement.setObject(2, day);
            statement.setInt(3, id_skater);
            statement.setInt(4, id_competition);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table skater_competition");
    }

    void insertTableSkaterCountry(int nStrings) throws SQLException {
        int nextId = getMaxId("skater_country");
        PreparedStatement statement = dbcon.prepareStatement("INSERT INTO skater_country( id, id_skater, id_country, since, until) VALUES(?, ?, ?, ?, ?)");
        for (int i = 0; i < nStrings; i++) {
            nextId++;
            int id_skater = generateIdFSkaters();
            int id_country = generateIdCountry();
            LocalDate since = generateDate(2000, 2018);
            int d = generateInt(0, 20);
            LocalDate until = since.plusYears(d);
            statement.setInt(1, nextId);
            statement.setInt(2, id_skater);
            statement.setInt(3, id_country);
            statement.setObject(4, since);
            statement.setObject(5, until);
            statement.executeUpdate();
        }
        statement.close();
        System.out.println(nStrings + " row(s) were added to table skater_country");
    }

    // получение id таблиц для заполнения внешних ключей
    private Integer[] programIds;

    private int generateIdProgram() throws SQLException {
        PreparedStatement stm = dbcon.prepareStatement("SELECT array(select id from program)");
        ResultSet r = stm.executeQuery();
        while (r.next()) {
            Array ids = r.getArray(1);
            programIds = (Integer[]) ids.getArray();
        }
        int s = programIds.length;
        int i = generateInt(0, s - 1);
        return programIds[i];
    }

    private Integer[] fskatersIds;

    private int generateIdFSkaters() throws SQLException {
        PreparedStatement stm = dbcon.prepareStatement("SELECT array(select id from fskaters)");
        ResultSet r = stm.executeQuery();
        while (r.next()) {
            Array ids = r.getArray(1);
            fskatersIds = (Integer[]) ids.getArray();
        }
        int s = fskatersIds.length;
        int i = generateInt(0, s - 1);
        return fskatersIds[i];
    }

    private Integer[] citiesIds;

    private int generateIdCities() throws SQLException {
        PreparedStatement stm = dbcon.prepareStatement("SELECT array(select id from cities)");
        ResultSet r = stm.executeQuery();
        while (r.next()) {
            Array ids = r.getArray(1);
            citiesIds = (Integer[]) ids.getArray();
        }
        int s = citiesIds.length;
        int i = generateInt(0, s - 1);
        return citiesIds[i];
    }

    private Integer[] countryIds;

    private int generateIdCountry() throws SQLException {
        PreparedStatement stm = dbcon.prepareStatement("SELECT array(select id from country)");
        ResultSet r = stm.executeQuery();
        while (r.next()) {
            Array ids = r.getArray(1);
            countryIds = (Integer[]) ids.getArray();
        }
        int s = countryIds.length;
        int i = generateInt(0, s - 1);
        return countryIds[i];
    }

    private Integer[] coachIds;

    private int generateIdCoach() throws SQLException {
        PreparedStatement stm = dbcon.prepareStatement("SELECT array(select id from coach)");
        ResultSet r = stm.executeQuery();
        while (r.next()) {
            Array ids = r.getArray(1);
            coachIds = (Integer[]) ids.getArray();
        }
        int s = coachIds.length;
        int i = generateInt(0, s - 1);
        return coachIds[i];
    }

    private Integer[] competitionIds;

    private int generateIdCompetition() throws SQLException {
        PreparedStatement stm = dbcon.prepareStatement("SELECT array(select id from competition)");
        ResultSet r = stm.executeQuery();
        while (r.next()) {
            Array ids = r.getArray(1);
            competitionIds = (Integer[]) ids.getArray();
        }
        int s = competitionIds.length;
        int i = generateInt(0, s - 1);
        return competitionIds[i];
    }

    private Integer[] skaterCompetitionIds;
    private int generateIdSkaterCompetition() throws SQLException {
        PreparedStatement stm = dbcon.prepareStatement("SELECT array(select id from skater_competition)");
        ResultSet r = stm.executeQuery();
        while (r.next()) {
            Array ids = r.getArray(1);
            skaterCompetitionIds = (Integer[]) ids.getArray();
        }
        int s = skaterCompetitionIds.length;
        int i = generateInt(0, s - 1);
        return skaterCompetitionIds[i];
    }

    //поиск максимального id
    private int getMaxId(String table) throws SQLException {
        String query = "SELECT MAX(id) FROM " + table;
        ResultSet sqlResultSet = dbcon.createStatement().executeQuery(query);
        if (sqlResultSet.next()) {
            return sqlResultSet.getInt(1);
        } else {
            return 0;
        }
    }


    public int getnStrings() {
        return nStrings;
    }
    public int getnCoach() {
        return nCoach;
    }


}

