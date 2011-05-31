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

/**
 *
 * @author user
 */
@Entity
@SuppressWarnings("serial")
public class Login implements Serializable{
     private String accesstoken=null;
    private String accesskey=null;
    private long baseid;
    private long id;

    public Login(){}

    public Login(String accesskey,String accesstoken,long userid)
            {
    this.accesskey=accesskey;
    this.accesstoken=accesstoken;

    this.baseid=userid;
  }

        @Column()
    public long getbaseid(){
    return baseid;}

    public void setbaseid(long baseid){
        this.baseid=baseid;
    }

  /**
   * @return Author
   */
    @Column(length=100)
  public String getaccesstoken() {
    return accesstoken;
  }

  /**
   * @param string Sets the Tweet
   */
  public void setaccesstoken(String string) {
    accesstoken = string;
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
  @Column(length=100)
  public String getaccesskey() {
    return accesskey;
  }

  public void setaccesskey(String accesskey) {
    this.accesskey = accesskey;
  }
}
