package models;

import org.hibernate.annotations.Type;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: Sergey Royz
 * @since: 22.05.2013
 */
@Entity
@Table(name = "reviews")
public class Review extends GenericModel {

    enum Locale {
        EN, RU
    }

    @Id
    @Column(name = "id", unique = true)
    public String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "locale", nullable = false)
    public Locale locale;

    @Column(name = "modification_date", nullable = false)
    public Date modificationDate;

    @Required
    @Column(name = "image_url", nullable = false)
    public String imageUrl;

    @Required
    @Column(name = "title", nullable = false)
    public String title;

    @Lob
    @Required
    @MaxSize(10000)
    @Type(type="org.hibernate.type.TextType")
    @Column(name = "content", nullable = false, length = 2048)
    public String content;

    @Required
    @Column(name = "source_url", nullable = false)
    public String sourceUrl;

    public Review() {
        id = UUID.randomUUID().toString();
        modificationDate = new Date();
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s] %s - %s", modificationDate, locale, title, content);
    }

    public static List<Review> list(String localeStr) {
        Locale locale = "en".equals(localeStr) ? Locale.EN : Locale.RU;
        return Review.find("locale = ? order by modificationDate desc", locale).fetch();
    }

}
