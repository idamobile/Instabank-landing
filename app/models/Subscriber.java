package models;


import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subscribers")
public class Subscriber extends Model {

    public static final int NUMERIC_STATUS_NEW = 0;
    public static final int NUMERIC_STATUS_NEWLY_REGISTERED = 1;

    public enum Status {
        NOT_CONFIRMED, CONFIRMED, GREETING_SENT, UNSUBSCRIBED
    }

    public enum Platform {
        IPHONE, ANDROID
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

    @Column(name = "numeric_status", nullable = false)
    public int numericStatus;

    @Column(name = "is_subscribed", nullable = false)
    public boolean subscribed = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = true)
    public Platform platform;

    public Subscriber() {

    }

    public Subscriber(String name, String email, String remoteAddress) {
        this.name = name;
        this.email = email;
        this.remoteAddress = remoteAddress;
        subscriptionDate = new Date();
        platform = Platform.ANDROID;
    }

    public static boolean isSubscribed(String email) {
        Subscriber subscriber = findByEmail(email);
        return subscriber != null && subscriber.subscribed;
    }

    public static Subscriber subscribe(String name, String email, String remoteAddress) {
        Subscriber subscriber = findByEmail(email);
        if (subscriber == null) {
            subscriber = create(name, email, remoteAddress);
        } else {
            subscriber.subscribed = true;
            subscriber.save();
        }
        return subscriber;
    }

    public static Subscriber create(String name, String email, String remoteAddress) {
        Subscriber subscriber = new Subscriber(name, email, remoteAddress);
        subscriber.save();
        return subscriber;
    }

    public static List<Subscriber> findByStatus(Status status) {
        return Subscriber.find("byStatus", Subscriber.Status.NOT_CONFIRMED).fetch();
    }

    public static Subscriber findByEmail(String email) {
        return Subscriber.find("byEmail", email).first();
    }

    public static Subscriber findFirstByNumericStatus(int numericStatus) {
        return Subscriber.find("byNumericStatus", numericStatus).first();
    }

    public void unsubscribe() {
        subscribed = false;
        numericStatus = NUMERIC_STATUS_NEW;
        save();
    }

    public void updateNumericStatus(int newStatus) {
        numericStatus = newStatus;
        save();
    }

    public void updateStatus(Status newStatus) {
        status = newStatus;
        save();
    }

}
