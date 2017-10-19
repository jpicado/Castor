package castor.utils;

/**
 * Created by Sudhanshu on 27/08/17.
 */
public class Constants {

    public enum ModeType {
        INPUT("+"), OUTPUT("-"), CONSTANT("#");
        private String value;

        ModeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum TransformDelimeter {
        ARROW("->"), SLASH_CLOSE_PARA("\\)"), CLOSE_PARA(")"), OPEN_PARA("("), COMMA(",");
        private String value;

        TransformDelimeter(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Regex {
        CLOSE_PARENTHESIS(")"), OPEN_PARENTHESIS("("), PARENTHESIS(") < ("), PERIOD("."), SUBSET("<"), SEMICOLON(";"),
        COMMA(",");
        private String value;

        Regex(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum DDLRegex {
        PK_REGEX("constraint\\s+\\w+\\s+primary\\s+key\\s*[(].*?[)]"),
        FK_REGEX("constraint\\s+\\w+\\s+foreign\\s+key\\s*[(].*?[)]\\s+references\\s+\\w+.\\w+\\s*[(].*?[)]"),
        DDL_SPLITTER(", (?![^(]*\\))");
        private String value;

        DDLRegex(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum DDLString {
        CONSTRAINT("constraint"),
        CREATE("create"),
        PRIMARY("primary"),
        FOREGIN("foreign");
        private String value;

        DDLString(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    public enum Strings {
        INDS("inds"), UNCHANGED("Unchanged");
        private String value;

        Strings(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum IndexType {
        PRIMARY_KEY("primary key"),
        FOREGIN_KEY("foreign key");
        private String value;

        IndexType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
