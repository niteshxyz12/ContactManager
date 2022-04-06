package com.example.smartcontactmanager.entities;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="USER")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    @NotBlank(message= "name field is required !!")
    @Size(min=4,max=25,message="minimum 4 and maximum 25 characters are allowed!!")
    private String name;
    @Column(unique = true)

    @NotBlank(message= "email field is required !!")
    private String email;
    @NotBlank(message= "password field is required !!")
    private String password;
    private String role;
    private boolean enabled;
    private String imageurl;
    @Column(length=500)
    @NotBlank(message= "about field is required !!")
    private String about;

    @OneToMany(cascade=CascadeType.ALL,fetch =FetchType.LAZY,mappedBy = "user")// jab user save kare to uske contact apne aap save ho jayenge aur jab user ko
    // delete kare to uske contacts apne aap delete ho jayenge(ye cascade krne ka fayda h)
    // jab bhi user ko call kare usi waqt sare contacts fetch ho jaye to fetchtype.eager krte h lekin contacts ko
    // jab fetch krna ho wo tabhi fetch ho to fetchtype.lazy krte h
    private List<Contact> contacts = new ArrayList<>();


    public User() {
        super();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", imageurl='" + imageurl + '\'' +
                ", about='" + about + '\'' +
                ", contacts=" + contacts +
                '}';
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}










