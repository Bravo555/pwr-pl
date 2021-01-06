import java.io.Serializable;
import java.util.HashSet;

public abstract class PhonebookOpResult implements Serializable {
    private PhonebookOpResult() {}

    public static class Error extends PhonebookOpResult {
        String cause;

        Error(String cause) {
            this.cause = cause;
        }

        @Override
        public String toString() {
            return "ERROR " + cause;
        }
    }
    public static class Ok extends PhonebookOpResult {
        @Override
        public String toString() {
            return "OK";
        }
    }
    public static class OkNumber extends PhonebookOpResult {
        String number;

        public OkNumber(String number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "OK " + number;
        }
    }
    public static class OkList extends PhonebookOpResult {
        HashSet<String> values;

        OkList(HashSet<String> values) {
            this.values = values;
        }

        @Override
        public String toString() {
            return "OK " + String.join(" ", values);
        }
    }
}
