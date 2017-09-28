package ba.unsa.pmf.planerputovanja.database;

public class PutovanjeDbSchema {
    public static final class PutovanjeTable {
        public static final String NAME = "putovanja";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String RESERVED = "reserved";
            public static final String MJESTA = "mjesta";
            public static final String NAPOMENA = "napomena";
        }
    }
}
