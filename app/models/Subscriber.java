package models;


import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscribers")
public class Subscriber extends Model {
    public enum Status {
        NOT_CONFIRMED, CONFIRMED, GREETING_SENT
    }

    @Column(name = "email", unique = true, nullable = false)
    public String email;

    @Column(name = "subscription_date", nullable = false)
    public Date subscriptionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public Status status = Status.NOT_CONFIRMED;

    @Column(name = "remote_address")
    public String remoteAddress;

    public Subscriber() {

    }

    public Subscriber(String email, String remoteAddress) {
        this.email = email;
        this.remoteAddress = remoteAddress;
        subscriptionDate = new Date();
    }

    public static Subscriber create(String email, String remoteAddress) {
        Subscriber subscriber = new Subscriber(email, remoteAddress);
        subscriber.save();
        return subscriber;
    }

}
