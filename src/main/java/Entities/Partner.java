package Entities;

import enums.PartnerCategory;
import enums.PartnerType;
import javafx.beans.property.*;

import java.util.Objects;

public class Partner {
    //pour FX
    private final IntegerProperty ID = new SimpleIntegerProperty();
    private final StringProperty Name = new SimpleStringProperty();
    private final ObjectProperty<PartnerCategory> Category = new SimpleObjectProperty<>();
    private final ObjectProperty<PartnerType> Type = new SimpleObjectProperty<>();
    private final StringProperty Email = new SimpleStringProperty();
    private final StringProperty Phone = new SimpleStringProperty();
    private final StringProperty Address = new SimpleStringProperty();
    private int id;
    private String name;
    private PartnerCategory category;
    private PartnerType type;
    private String email;
    private String phone;
    private String address;

    public Partner() {
    }

    public Partner(String name, int id, PartnerCategory category, PartnerType type,
                   String email, String phone, String address) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.type = type;
        this.email = email;
        this.phone = phone;
        this.address = address;

        //pour FX
        this.Name.set(name);
        this.ID.set(id);
        this.Category.set(category);
        this.Type.set(type);
        this.Email.set(email);
        this.Phone.set(phone);
        this.Address.set(address);
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

    public PartnerCategory getCategory() {
        return category;
    }

    public void setCategory(PartnerCategory category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PartnerType getType() {
        return type;
    }

    public void setType(PartnerType type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public int getID() {
        return ID.get();
    }

    public IntegerProperty IDProperty() {
        return ID;
    }

    public StringProperty nameProperty() {
        return Name;
    }

    public ObjectProperty<PartnerCategory> categoryProperty() {
        return Category;
    }

    public ObjectProperty<PartnerType> typeProperty() {
        return Type;
    }

    public StringProperty emailProperty() {
        return Email;
    }

    public StringProperty phoneProperty() {
        return Phone;
    }

    public StringProperty addressProperty() {
        return Address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partner partner)) return false;
        return id == partner.id && category == partner.category && type == partner.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, type);
    }
}
