/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.types;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@SuppressWarnings("serial")
public class Tweets implements Serializable {
    private String author=null;
    private String text=null;
    private long id;
      private String ownerName;
      private long baseid;

      public Tweets(){}

    public Tweets(String author,String text, long baseid, String owner){
        this.author=author;
        this.text=text;
        this.ownerName=owner;
        this.baseid=baseid;
    }

    @Column()
    public long getbaseid(){
    return baseid;}

    public void setbaseid(long baseid){
        this.baseid=baseid;
    }

  /**
   * @return Tweet
   */
    @Column(length=250)
  public String getText() {
    return text;
  }

  /**
   * @return Author
   */
    @Column(length=100)
  public String getAuthor() {
    return author;
  }

  /**
   * @param string Sets the Tweet
   */
  public void setText(String string) {
    text = string;
  }

  /**
   * @param string sets the Author
   */
  public void setAuthor(String string) {
    author = string;
  }

  /**
   * @return ID Returns ID
   */
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  public long getId() {
    return id;
  }

  /**
   * @param l Sets the ID
   */
  public void setId(long l) {
    id = l;
  }
  @Column(length=20)
  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }
}
