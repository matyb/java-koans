package intermediate;

import com.sandwich.koan.Koan;

import java.io.*;
import java.util.logging.Logger;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;


public class AboutSerialization {

    @Koan
    public void simpleSerialization() throws FileNotFoundException, IOException, ClassNotFoundException {
        String s = "Hello world";
        // serialize
        File file = new File("SerializeFile");
        file.deleteOnExit();
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
        os.writeObject(s);
        os.close();

        // deserialize
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream("SerializeFile"));
            String otherString = (String) is.readObject();
            assertEquals(otherString, "Hello world");
        } finally {
            closeStream(is);
        }
    }

    static class Starship implements Serializable {

        // Although it is not enforced, you should define this constant
        // to make sure you serialize/deserialize only compatible versions
        // of your objects
        private static final long serialVersionUID = 1L;
        int maxWarpSpeed;
    }

    @Koan
    public void customObjectSerialization() throws IOException, ClassNotFoundException {
        Starship s = new Starship();
        s.maxWarpSpeed = 9;
        File file = new File("SerializeFile");
        file.deleteOnExit();
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
        os.writeObject(s);
        os.close();

        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream("SerializeFile"));
            Starship onTheOtherSide = (Starship) is.readObject();
            assertEquals(onTheOtherSide.maxWarpSpeed, 9);
        } finally {
            closeStream(is);
        }
    }

    static class Engine {
        String type;

        public Engine(String t) {
            type = t;
        }
    }

    @SuppressWarnings("serial")
    static class Car implements Serializable {
        // Transient means: Ignore field for serialization
        transient Engine engine;

        // Notice these methods are private and will be called by the JVM
        // internally - as if they where defined by the Serializable interface
        // but they aren't defined as part of the interface
        private void writeObject(ObjectOutputStream os) throws IOException {
            os.defaultWriteObject();
            os.writeObject(engine.type);
        }

        private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
            is.defaultReadObject();
            engine = new Engine((String) is.readObject());
        }
    }

    @Koan
    public void customObjectSerializationWithTransientFields() throws FileNotFoundException, IOException, ClassNotFoundException {
        // Note that this kind of access of fields is not good OO practice.
        // But let's focus on serialization here :)
        Car car = new Car();
        car.engine = new Engine("diesel");
        File file = new File("SerializeFile");
        file.deleteOnExit();
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
        os.writeObject(car);
        os.close();

        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream("SerializeFile"));
            Car deserializedCar = (Car) is.readObject();
            assertEquals(deserializedCar.engine.type, "diesel");
        } finally {
            closeStream(is);
        }
    }

    @SuppressWarnings("serial")
    class Boat implements Serializable {
        Engine engine;
    }

    @Koan
    public void customSerializationWithUnserializableFields() throws FileNotFoundException, IOException {
        Boat boat = new Boat();
        boat.engine = new Engine("diesel");
        File file = new File("SerializeFile");
        file.deleteOnExit();
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
        String marker = "Start ";
        try {
            os.writeObject(boat);
        } catch (NotSerializableException e) {
            marker += "Exception";
        }
        os.close();
        assertEquals(marker, "Start Exception");
    }

    @SuppressWarnings("serial")
    static class Animal implements Serializable {
        String name;

        public Animal(String s) {
            name = s;
        }
    }

    @SuppressWarnings("serial")
    static class Dog extends Animal {
        public Dog(String s) {
            super(s);
        }
    }

    @Koan
    public void serializeWithInheritance() throws IOException, ClassNotFoundException {
        Dog d = new Dog("snoopy");
        File file = new File("SerializeFile");
        file.deleteOnExit();
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
        os.writeObject(d);
        os.close();

        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream("SerializeFile"));
            Dog otherDog = (Dog) is.readObject();
            assertEquals(otherDog.name, "snoopy");
        } finally {
            closeStream(is);
        }
    }

    static class Plane {
        String name;

        public Plane(String s) {
            name = s;
        }

        public Plane() {
        }

    }

    @SuppressWarnings("serial")
    static class MilitaryPlane extends Plane implements Serializable {
        public MilitaryPlane(String s) {
            super(s);
        }
    }

    @Koan
    public void serializeWithInheritanceWhenParentNotSerializable() throws FileNotFoundException, IOException, ClassNotFoundException {
        MilitaryPlane p = new MilitaryPlane("F22");

        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("SerializeFile"));
        os.writeObject(p);
        os.close();

        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream("SerializeFile"));
            MilitaryPlane otherPlane = (MilitaryPlane) is.readObject();
            // Does this surprise you?
            assertEquals(otherPlane.name, null);

            // Think about how serialization creates objects...
            // It does not use constructors! But if a parent object is not serializable
            // it actually uses constructors and if the fields are not in a serializable class...
            // unexpected things - like this - may happen
        } finally {
            closeStream(is);
        }
    }

    private void closeStream(ObjectInputStream ois) {
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException x) {
                Logger.getAnonymousLogger().severe("Unable to close reader.");
            }
        }
    }

}

