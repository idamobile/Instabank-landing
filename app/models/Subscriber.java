package models;


import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subscribers")
public class Subscriber extends Model {
    public enum Status {
        NOT_CONFIRMED, CONFIRMED, GREETING_SENT, UNSUBSCRIBED
    }

    @Column(name = "name", nullable = true)
    public String name;

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

    public Subscriber(String name, String email, String remoteAddress) {
        this.name = name;
        this.email = email;
        this.remoteAddress = remoteAddress;
        subscriptionDate = new Date();
    }

    public static Subscriber create(String name, String email, String remoteAddress) {
        Subscriber subscriber = new Subscriber(name, email, remoteAddress);
        subscriber.save();
        return subscriber;
    }

    public static List<Subscriber> findByStatus(Status status) {
        return Subscriber.find("byStatus", Subscriber.Status.NOT_CONFIRMED).fetch();
    }

    public void updateStatus(Status newStatus) {
        status = newStatus;
        save();
    }

}
