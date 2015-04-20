package cerebro;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "hero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hero.findAll", query = "SELECT h FROM Hero h"),
    @NamedQuery(name = "Hero.findByCharacterId", query = "SELECT h FROM Hero h WHERE h.characterId = :characterId"),
    @NamedQuery(name = "Hero.findByCharacterName", query = "SELECT h FROM Hero h WHERE h.characterName = :characterName")})
public class Hero implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "characterId")
    private Integer characterId;
    @Column(name = "characterName")
    private String characterName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hero")
    private Collection<Answer> answerCollection;

    public Hero() {
    }

    public Hero(Integer characterId) {
        this.characterId = characterId;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    @XmlTransient
    public Collection<Answer> getAnswerCollection() {
        return answerCollection;
    }

    public void setAnswerCollection(Collection<Answer> answerCollection) {
        this.answerCollection = answerCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (characterId != null ? characterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hero)) {
            return false;
        }
        Hero other = (Hero) object;
        if ((this.characterId == null && other.characterId != null) || (this.characterId != null && !this.characterId.equals(other.characterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cerebro.Hero[ characterId=" + characterId + " ]";
    }

}
