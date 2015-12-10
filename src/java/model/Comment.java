/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaiPhan
 */
@Entity
@Table(name = "COMMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c"),
    @NamedQuery(name = "Comment.findByCid", query = "SELECT c FROM Comment c WHERE c.cid = :cid"),
    @NamedQuery(name = "Comment.findByCment", query = "SELECT c FROM Comment c WHERE c.cment = :cment"),
    @NamedQuery(name = "Comment.findByCtime", query = "SELECT c FROM Comment c WHERE c.ctime = :ctime")})
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CID")
    private Integer cid;
    @Size(max = 255)
    @Column(name = "CMENT")
    private String cment;
    @Column(name = "CTIME")
    @Temporal(TemporalType.TIME)
    private Date ctime;
    @JoinColumn(name = "UID", referencedColumnName = "UID")
    @ManyToOne
    private User uid;
    @JoinColumn(name = "IID", referencedColumnName = "IID")
    @ManyToOne
    private Image iid;

    public Comment() {
    }

    public Comment(Integer cid) {
        this.cid = cid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCment() {
        return cment;
    }

    public void setCment(String cment) {
        this.cment = cment;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public User getUid() {
        return uid;
    }

    public void setUid(User uid) {
        this.uid = uid;
    }

    public Image getIid() {
        return iid;
    }

    public void setIid(Image iid) {
        this.iid = iid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Comment[ cid=" + cid + " ]";
    }
    
}
