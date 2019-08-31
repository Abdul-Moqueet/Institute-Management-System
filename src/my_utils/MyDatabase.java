package my_utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_alert.MyAlert;
import take_admission.StudentModal;

import java.sql.*;

public class MyDatabase {

    public static Connection connection;

    public static void connectToDataBase() {

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:My_Database.db");
        } catch (Exception e) {
            MyAlert.errorAlert(e.toString());
        }

    }

    public static boolean saveAdmission(StudentModal sm) {

        try {

            PreparedStatement pr = connection.prepareStatement("insert into student (name, s_class, gender, f_name, phone, doa, batch, course, address, total_fee, dues, paid, pay_date, pic) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pr.setString(1, sm.getName());
            pr.setString(2, sm.getsClass());
            pr.setString(3, sm.getGender());
            pr.setString(4, sm.getfName());
            pr.setString(5, sm.getPhone());
            pr.setString(6, sm.getDoa());
            pr.setString(7, sm.getBatch());
            pr.setString(8, sm.getCourse());
            pr.setString(9, sm.getAddress());
            pr.setInt(10, sm.getTotalFee());
            pr.setInt(11, sm.getDues());
            pr.setInt(12, sm.getPaid());
            pr.setString(13, sm.getPayDate());
            pr.setString(14, sm.getPic());

            pr.execute();
            return true;

        } catch (Exception e) {
            MyAlert.errorAlert(e.toString());
        }

        return false;
    }

//    public static ObservableList<StudentModal> loadStudentDateInObList() {
//
//        ObservableList<StudentModal> list = FXCollections.observableArrayList();
//
//        try {
//            Statement st = connection.createStatement();
//            ResultSet rs = st.executeQuery("select * from student_table");
//
//            while (rs.next()) {
//                list.add(new StudentModal(
//                        rs.getString(1),
//                        rs.getString(2),
//                        rs.getString(3),
//                        rs.getString(4),
//                        rs.getString(5),
//                        rs.getString(6),
//                        rs.getString(7),
//                        rs.getString(8),
//                        rs.getString(9),
//                        rs.getInt(10)
//                ));
//            }
//
//        } catch (Exception e) {
//            MyAlert.errorAlert(e.toString());
//        }
//        return list;
//    }

    public static void delete(String tableName, int id) {

        try {
            Statement st = connection.createStatement();
            st.execute("delete from " + tableName + " where id = " + id);
        } catch (Exception e) {
            MyAlert.errorAlert(e.toString());
        }
    }

//    public static boolean updateStudentInfoData(StudentModal sm) {
//
//        try {
//
//            PreparedStatement pr = connection
//                    .prepareStatement("update student_table set s_name = ?, s_class = ?, s_roll = ?, s_gender = ?, s_father_name = ?, s_dob = ?, s_doa = ?, s_address = ?, s_pic = ? where id = ?");
//            setPreparedStatement(pr, sm);
//            pr.setInt(10, sm.getId());
//            return pr.executeUpdate() == 1;
//
//        } catch (Exception e) {
//            MyAlert.errorAlert(e.toString());
//        }
//
//        return false;
//    }

}