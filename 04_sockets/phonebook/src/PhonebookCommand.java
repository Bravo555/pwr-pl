import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;

// Really no benefit to doing ADTs in Java since the list is non-exhaustive (and it's not like the compiler checks we're
// covering all the variants anyway) as we can add a variant at any point simply inheriting a new class... oh well
public abstract class PhonebookCommand implements Serializable {
    private PhonebookCommand() {}

    public static class Load extends PhonebookCommand {
        String filename;

        public Load(String filename) {
            this.filename = filename;
        }
    }
    public static class Save extends PhonebookCommand {
        String filename;

        public Save(String filename) {
            this.filename = filename;
        }
    }
    public static class Get extends PhonebookCommand {
        String name;

        public Get(String name) {
            this.name = name;
        }
    }
    public static class Put extends PhonebookCommand {
        String name;
        String number;

        Put(String name, String number) {
            this.name = name;
            this.number = number;
        }
    }
    public static class Replace extends PhonebookCommand {
        String name;
        String number;

        Replace(String name, String number) {
            this.name = name;
            this.number = number;
        }
    }
    public static class Delete extends PhonebookCommand {
        String name;

        Delete(String name) {
            this.name = name;
        }
    }
    public static class List extends PhonebookCommand {}
    public static class Close extends PhonebookCommand {}
    public static class Bye extends PhonebookCommand {}

    public static PhonebookCommand parse(String text) throws PhonebookParseException {
        var words = Arrays.stream(text.split(" ")).iterator();

        if(!words.hasNext()) {
            throw new PhonebookParseException("no command");
        }
        String command = words.next();

        return switch (command) {
            case "LOAD" -> {


                // NOTE: doesn't handle whitespaces in the filename
                if(!words.hasNext()) {
                    throw new PhonebookParseException("filename not provided");
                }
                String filename = words.next();
                if (filename.isBlank()) {   // filename not provided
                    yield null; // probably should throw some error
                }

                if(words.hasNext()) {
                    throw new PhonebookParseException("Wrong number of arguments, expected: 1, got more");
                }

                yield new Load(filename);
            }
            case "SAVE" -> {
                if(!words.hasNext()) {
                    throw new PhonebookParseException("filename not provided");
                }
                String filename = words.next();
                if (filename.isBlank()) {   // filename not provided
                    yield null; // probably should throw some error
                }
                if(words.hasNext()) {
                    throw new PhonebookParseException("Wrong number of arguments, expected: 1, got more");
                }
                yield new Save(filename);
            }
            case "GET" -> {
                if(!words.hasNext()) {
                    throw new PhonebookParseException("name not provided");
                }
                String name = words.next();
                if(name.isBlank()) {
                    yield null;
                }
                if(words.hasNext()) {
                    throw new PhonebookParseException("Wrong number of arguments, expected: 1, got more");
                }
                yield new Get(name);
            }
            case "PUT" -> {
                if(!words.hasNext()) {
                    throw new PhonebookParseException("name not provided");
                }
                String name = words.next();
                if(name.isBlank()) {
                    yield null;
                }
                if(!words.hasNext()) {
                    throw new PhonebookParseException("number not provided");
                }
                String number = words.next();
                if(number.isBlank()) {
                    yield null;
                }
                if(words.hasNext()) {
                    throw new PhonebookParseException("Wrong number of arguments, expected: 2, got more");
                }
                yield new Put(name, number);
            }
            case "REPLACE" -> {
                if(!words.hasNext()) {
                    throw new PhonebookParseException("name not provided");
                }
                String name = words.next();
                if(name.isBlank()) {
                    yield null;
                }
                if(!words.hasNext()) {
                    throw new PhonebookParseException("number not provided");
                }
                String number = words.next();
                if(number.isBlank()) {
                    yield null;
                }
                if(words.hasNext()) {
                    throw new PhonebookParseException("Wrong number of arguments, expected: 2, got more");
                }
                yield new Replace(name, number);
            }
            case "DELETE" -> {
                if(!words.hasNext()) {
                    throw new PhonebookParseException("number not provided");
                }
                String name = words.next();
                if(name.isBlank()) {
                    yield null;
                }
                if(words.hasNext()) {
                    throw new PhonebookParseException("Wrong number of arguments, expected: 2, got more");
                }
                yield new Delete(name);
            }
            case "LIST" -> new List();
            case "CLOSE" -> new Close();
            case "BYE" -> new Bye();
            default -> null;
        };
    }
}

class PhonebookParseException extends Exception {
    public PhonebookParseException(String message) {
        super(message);
    }
}