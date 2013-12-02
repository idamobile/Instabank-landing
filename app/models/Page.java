package models;

import org.hibernate.annotations.Type;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author: Sergey Royz
 * @since: 02.12.2013
 */
@Entity
@Table(name="page")
public class Page extends Model {

    @Required
    @Column(name = "page_key", unique = true, nullable = false)
    public String key;

    @Lob
    @MaxSize(60000)
    @Type(type="org.hibernate.type.TextType")
    @Column(name = "content", nullable = false, length = 60000)
    public String content;

}
