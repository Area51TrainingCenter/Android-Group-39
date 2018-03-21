package pe.area51.parcelabletest;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    private final long id;
    private final String name;
    private final String lastName;
    private final float age;
    private final String address;

    public Person(long id, String name, String lastName, float age, String address) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
    }

    public Person(final Parcel source) {
        this.id = source.readLong();
        this.name = source.readString();
        this.lastName = source.readString();
        this.age = source.readFloat();
        this.address = source.readString();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public float getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(lastName);
        dest.writeFloat(age);
        dest.writeString(address);
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
