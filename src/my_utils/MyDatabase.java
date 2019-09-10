package my_utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_alert.MyAlert;
import take_admission.StudentModal;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MyDatabase {

    private static Connection connection;
    public static ObservableList<StudentModal> masterDataObservableList;

    public static void connectToDataBase() {

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:My_Database.db");

            new Thread(() -> {

                try {
                    Thread.sleep(1000);
                    refreshMasterDataObservableList();
                } catch (InterruptedException e) {
                    MyAlert.errorAlert(e);
                }

            }).start();

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

    }

    public static boolean saveAdmission(StudentModal sm) {

        try {

            PreparedStatement pr = connection.prepareStatement("insert into student (name, s_class, gender, f_name, phone, doa, batch, course, address, total_fee, dues, paid, pay_date, pic) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pr.setString(1, sm.getName());
            pr.setString(2, sm.getS_class());
            pr.setString(3, sm.getGender());
            pr.setString(4, sm.getF_name());
            pr.setString(5, sm.getPhone());
            pr.setString(6, sm.getDoa());
            pr.setString(7, sm.getBatch());
            pr.setString(8, sm.getCourse());
            pr.setString(9, sm.getAddress());
            pr.setInt(10, sm.getTotal_fee());
            pr.setInt(11, sm.getDues());
            pr.setInt(12, sm.getPaid());
            pr.setString(13, sm.getPay_date());
            pr.setString(14, sm.getPic());

            pr.execute();
            return true;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        return false;
    }

    public static void refreshMasterDataObservableList() {

        masterDataObservableList = FXCollections.observableArrayList();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from student");

            while (rs.next()) {
                masterDataObservableList.add(new StudentModal(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getInt(11),
                        rs.getInt(12),
                        rs.getInt(13),
                        rs.getString(14),
                        rs.getString(15)
                ));
            }

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
    }

    public static void delete(String tableName, int id) {

        try {
            Statement st = connection.createStatement();
            st.execute("delete from " + tableName + " where id = " + id);
        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
    }

    public static boolean updateStudentInfoData(StudentModal sm) {

        try {

            PreparedStatement pr = connection
                    .prepareStatement("update student set name = ?, s_class = ?, gender = ?, f_name = ?, phone = ?, doa = ?, batch = ?, course = ?, address = ?, pic =? where id = ?");
            pr.setString(1, sm.getName());
            pr.setString(2, sm.getS_class());
            pr.setString(3, sm.getGender());
            pr.setString(4, sm.getF_name());
            pr.setString(5, sm.getPhone());
            pr.setString(6, sm.getDoa());
            pr.setString(7, sm.getBatch());
            pr.setString(8, sm.getCourse());
            pr.setString(9, sm.getAddress());
            pr.setString(10, sm.getPic());
            pr.setInt(11, sm.getId());
            return pr.executeUpdate() == 1;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        return false;
    }

    public static boolean payFee(int payingAmount, int dues, String payingDate, int id) {

        try {

            PreparedStatement pr = connection.prepareStatement("update student set paid = ?, dues = ?, pay_date = ? where id = ?");
            pr.setInt(1, payingAmount);
            pr.setInt(2, dues);
            pr.setString(3, payingDate);
            pr.setInt(4, id);
            return pr.executeUpdate() == 1;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        return false;
    }

    public static boolean updateCredentials(String newPassword) {

        try {

            PreparedStatement pr = connection.prepareStatement("update credentials set password = ?");
            pr.setString(1, newPassword);
            pr.executeUpdate();
            return true;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
        return false;
    }

    public static ArrayList<String> getCredentials() {

        ArrayList<String> credentials = new ArrayList<>();

        try {
            PreparedStatement pr = connection.prepareStatement("select * from credentials");
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                credentials.add(rs.getString(1));
                credentials.add(rs.getString(2));
            }
        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
        return credentials;
    }

    public static HashMap<String, Integer> getChartDataMap() {

        HashMap<String, Integer> chartDataMap = new HashMap<>();

        int boys = 0, girls = 0, paid = 0, dues = 0, boysPaid = 0, girlsPaid = 0, boysDue = 0, girlsDue = 0;

        try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select gender, dues from student");

            while (rs.next()) {
                if (rs.getString(1).equals("Male")) {
                    boys++;

                    if (rs.getInt(2) == 0)
                        boysPaid++;
                    else
                        boysDue++;

                } else {
                    girls++;

                    if (rs.getInt(2) == 0)
                        girlsPaid++;
                    else
                        girlsDue++;

                }

                if(rs.getInt(2) == 0)
                    paid++;
                else
                    dues++;
            }

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        chartDataMap.put("boys", boys);
        chartDataMap.put("girls", girls);
        chartDataMap.put("paid", paid);
        chartDataMap.put("dues", dues);
        chartDataMap.put("boysPaid", boysPaid);
        chartDataMap.put("boysDues", boysDue);
        chartDataMap.put("girlsPaid", girlsPaid);
        chartDataMap.put("girlsDues", girlsDue);


        return chartDataMap;
    }

}