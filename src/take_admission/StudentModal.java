package take_admission;

public class StudentModal {

    private int id;

    private String name, s_class, gender, f_name, phone, doa, batch, course, address;

    private int total_fee, dues, paid;

    private String pay_date, pic;

    public StudentModal(int id, String name, String s_class, String gender, String f_name, String phone, String doa, String batch, String course, String address, int total_fee, int dues, int paid, String pay_date, String pic) {
        this.id = id;
        this.name = name;
        this.s_class = s_class;
        this.gender = gender;
        this.f_name = f_name;
        this.phone = phone;
        this.doa = doa;
        this.batch = batch;
        this.course = course;
        this.address = address;
        this.total_fee = total_fee;
        this.dues = dues;
        this.paid = paid;
        this.pay_date = pay_date;
        this.pic = pic;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getS_class() {
        return s_class;
    }

    public void setS_class(String s_class) {
        this.s_class = s_class;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getDues() {
        return dues;
    }

    public void setDues(int dues) {
        this.dues = dues;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
