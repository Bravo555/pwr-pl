import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Phonebook implements Serializable {

    private Map<String, String> map = new ConcurrentHashMap<>();

    public PhonebookOpResult applyCommand(PhonebookCommand command) {
        try {
            if (command instanceof PhonebookCommand.Load load) {
                return loadFromFile(load.filename);
            } else if (command instanceof PhonebookCommand.Save save) {
                return saveToFile(save.filename);
            } else if (command instanceof PhonebookCommand.Get get) {
                return get(get.name);
            } else if (command instanceof PhonebookCommand.Put put) {
                return put(put.name, put.number);
            } else if (command instanceof PhonebookCommand.Replace replace) {
                return replace(replace.name, replace.number);
            } else if (command instanceof PhonebookCommand.Delete delete) {
                return remove(delete.name);
            } else if (command instanceof PhonebookCommand.List) {
                return list();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PhonebookOpResult.Error(e.toString());
        }

        // for some reason passed unsuitable command
        return new PhonebookOpResult.Error("command unrecognised");
    }

    PhonebookOpResult loadFromFile(String filename) throws IOException, ClassNotFoundException {
        var in = new ObjectInputStream(new FileInputStream(filename));
        var newMap = (Phonebook) in.readObject();
        this.map = newMap.map;

        return new PhonebookOpResult.Ok();
    }

    PhonebookOpResult saveToFile(String filename) throws IOException {
        var out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(this);

        return new PhonebookOpResult.Ok();
    }

    PhonebookOpResult get(String name) {
        String number = map.get(name);
        if(number == null) {
            return new PhonebookOpResult.Error("Name does not exist");
        }
        return new PhonebookOpResult.OkNumber(map.get(name));
    }

    PhonebookOpResult put(String name, String number) {
        map.put(name, number);
        return new PhonebookOpResult.Ok();
    }

    PhonebookOpResult replace(String name, String number) {
        map.replace(name, number);

        return new PhonebookOpResult.Ok();
    }

    // this was supposed to be called delete but if this is just a wrapper over map, let's keep method names the same
    PhonebookOpResult remove(String name) {
        map.remove(name);

        return new PhonebookOpResult.Ok();
    }

    PhonebookOpResult list() {
        return new PhonebookOpResult.OkList(new HashSet<>(map.keySet()));
    }

}
