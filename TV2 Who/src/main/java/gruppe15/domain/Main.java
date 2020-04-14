package gruppe15.domain;

import java.sql.Date;

public class Main {

    public static void main(String[] args) {

        Producer producer = new Producer("", "", "");

        producer.createProduction("Badehotellet", new Date(System.currentTimeMillis()));
        producer.addCastMember("Bob Bobsen");
        producer.addCastMember("Inger Bobsen");

    }
}
