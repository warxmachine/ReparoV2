package in.cybersin.reparo.model;

public class Customer {
    String AvatarName,Name,Email,Phone,Uid;
    public Customer() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public Customer(String uid) {
        Uid = uid;
    }

    public String getAvatarName() {
        return AvatarName;
    }

    public void setAvatarName(String avatarName) {
        AvatarName = avatarName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Customer(String avatarName, String name, String email, String phone) {
        AvatarName = avatarName;
        Name = name;
        Email = email;
        Phone = phone;
    }
}
