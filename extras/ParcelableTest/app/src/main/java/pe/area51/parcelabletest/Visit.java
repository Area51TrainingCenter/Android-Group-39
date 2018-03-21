package pe.area51.parcelabletest;

import android.os.Parcel;
import android.os.Parcelable;

public class Visit implements Parcelable {

    private final long id;
    //Nuestro objeto "Person" también implementa la interfaz "Parcelable".
    private final Person person;
    private final long creationTimestamp;

    public Visit(long id, Person person, long creationTimestamp) {
        this.id = id;
        this.person = person;
        this.creationTimestamp = creationTimestamp;
    }


    //MUY IMPORTANTE: El parcel debe leerse en el mismo orden en que se escribió.
    protected Visit(Parcel in) {
        id = in.readLong();
        person = in.readParcelable(Person.class.getClassLoader());
        creationTimestamp = in.readLong();
    }

    /**
     * El objeto "Creator" es necesario para recrear nuestro objeto
     * a partir de un "Parcel". Debe crearse como público y estático
     * con exactamente el nombre "CREATOR".
     */
    public static final Creator<Visit> CREATOR = new Creator<Visit>() {
        @Override
        public Visit createFromParcel(Parcel in) {
            return new Visit(in);
        }

        @Override
        public Visit[] newArray(int size) {
            return new Visit[size];
        }
    };

    public long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Este método debe devolver "0" o "1" (constante
     * "Parcelable.CONTENTS_FILE_DESCRIPTOR"). Se utiliza la constante
     * "CONTENTS_FILE_DESCRIPTOR" (o 1), cuando se implementa la interfaz
     * "Parcelable" con la clase "FileDescriptor". En todos los otros casos,
     * debe devolverse 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Este método almacenará los valores actuales de nuestro objeto en el "Parcel".
     * Hay que almacenar el valor y recrearlo al momento de leerlo, por lo que la idea
     * es guardar el valor de tal forma que la información se pueda reconstruir al leerlo.
     * No siempre se tendrán los métodos para almacenar el tipo de dato que deseamos.
     * Por ejemplo no hay un método para almacenar un booleano, por lo que podemos guardarlo
     * como entero (1 = verdadero, 0 = falso) y al leerlo lo convertimos a booleano siguiendo
     * la misma lógica.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(person, flags);
        dest.writeLong(creationTimestamp);
    }

    /**
     * Reemplazamos el método "toString()" de la clase "Object" (recordar que
     * toda clase en Java hereda de "Object") para mostrar fácilmente los
     * valores del objeto en un campo de texto. No es necesario reemplazar
     * este método para crear un objeto "Parcelable".
     */
    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", person=" + person +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}
