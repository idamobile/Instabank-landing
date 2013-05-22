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
@Table(name = "faq_items")
public class FaqItem extends GenericModel {

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
    @MaxSize(1024)
    @Column(name = "question", nullable = false, length = 1024)
    public String question;

    @Lob
    @Required
    @MaxSize(2048)
    @Type(type="org.hibernate.type.TextType")
    @Column(name = "answer", nullable = false, length = 2048)
    public String answer;

    @MaxSize(1024)
    @Column(name = "comment", nullable = true, length = 1024)
    public String comment;

    public FaqItem() {
        id = UUID.randomUUID().toString();
        modificationDate = new Date();
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s] %s - %s", modificationDate, locale, question, answer);
    }

    public static List<FaqItem> list(String localeStr) {
        Locale locale = "en".equals(localeStr) ? Locale.EN : Locale.RU;
        return FaqItem.find("locale = ? order by modificationDate desc", locale).fetch();
    }

}
