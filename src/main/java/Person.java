import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;

public class Person {

    private static final String FILE_PATH = "person_data.txt";
    private String personID;
    private String birthDate;
    private boolean isSuspended;

    public Person(String personID, String birthDate) {
        this.personID = personID;
        this.birthDate = birthDate;
        this.isSuspended = false;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public String getPersonID() {
        return personID;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int calculateAge(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date birth = sdf.parse(birthDate);
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(birth);
            Calendar now = Calendar.getInstance();

            int age = now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            return -1;
        }
    }

    public String addDemeritPoints(String date, int points) {
        if (points < 1 || points > 9) return "Failed";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date offenseDate;
        try {
            offenseDate = sdf.parse(date);
        } catch (ParseException e) {
            return "Failed";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("demerits.txt", true))) {
            writer.write(personID + "|" + sdf.format(offenseDate) + "|" + points);
            writer.newLine();
        } catch (IOException e) {
            return "Failed";
        }

        int total = 0;
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.YEAR, -2);
        Date startDate = cal.getTime();

        try (BufferedReader reader = new BufferedReader(new FileReader("demerits.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 3) continue;
                if (!parts[0].equals(personID)) continue;

                Date d = sdf.parse(parts[1]);
                if ((d.equals(startDate) || d.after(startDate)) &&
                        (d.equals(currentDate) || d.before(currentDate))) {
                    total += Integer.parseInt(parts[2]);
                }
            }
        } catch (Exception e) {
            return "Failed";
        }

        int age = calculateAge(this.birthDate);
        if ((age < 21 && total > 6) || (age >= 21 && total > 12)) {
            isSuspended = true;
        }

        return "OK";
    }

    public boolean addPerson(String personID, String address, String birthdate) {
        if (!Pattern.matches("^[2-9]{2}[\\w\\W]{6}[A-Z]{2}$", personID)) return false;

        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = personID.charAt(i);
            if (!Character.isLetterOrDigit(c)) specialCount++;
        }
        if (specialCount < 2) return false;

        String[] parts = address.split("\\|");
        if (parts.length != 5 || !parts[3].equalsIgnoreCase("Victoria")) return false;

        if (!Pattern.matches("^\\d{2}-\\d{2}-\\d{4}$", birthdate)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(personID + "|" + address + "|" + birthdate + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean updatePersonalDetails(String oldPersonID, String newPersonID, String address, String birthdate) {
        if (!Pattern.matches("^[2-9]{2}[\\w\\W]{6}[A-Z]{2}$", newPersonID)) return false;

        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = newPersonID.charAt(i);
            if (!Character.isLetterOrDigit(c)) specialCount++;
        }
        if (specialCount < 2) return false;

        String[] parts = address.split("\\|");
        if (parts.length != 5 || !parts[3].equalsIgnoreCase("Victoria")) return false;

        if (!Pattern.matches("^\\d{2}-\\d{2}-\\d{4}$", birthdate)) return false;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int age;
        try {
            Date birth = sdf.parse(birthdate);
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(birth);
            Calendar now = Calendar.getInstance();
            age = now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) age--;
        } catch (ParseException e) {
            return false;
        }

        try {
            File inputFile = new File("person_data.txt");
            File tempFile = new File("temp_data.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean updated = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length < 8 || !data[0].equals(oldPersonID)) {
                    writer.write(line + System.lineSeparator());
                    continue;
                }

                if (age < 18 && !address.equals(String.join("|", data[1], data[2], data[3], data[4], data[5]))) {
                    writer.write(line + System.lineSeparator());
                    continue;
                }

                if (!birthdate.equals(data[6]) &&
                        (!newPersonID.equals(oldPersonID) || !address.equals(String.join("|", data[1], data[2], data[3], data[4], data[5])))) {
                    writer.write(line + System.lineSeparator());
                    continue;
                }

                if (oldPersonID.charAt(0) % 2 == 0 && !newPersonID.equals(oldPersonID)) {
                    writer.write(line + System.lineSeparator());
                    continue;
                }

                writer.write(newPersonID + "|" + address + "|" + birthdate + System.lineSeparator());
                updated = true;
            }

            writer.close();
            reader.close();

            if (!inputFile.delete()) return false;
            if (!tempFile.renameTo(inputFile)) return false;

            return updated;
        } catch (IOException e) {
            return false;
        }
    }
}


