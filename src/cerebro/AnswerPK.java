package cerebro;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Beny
 */
@Embeddable
public class AnswerPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "characterId")
    private int characterId;
    @Basic(optional = false)
    @Column(name = "questionId")
    private int questionId;

    public AnswerPK() {
    }

    public AnswerPK(int characterId, int questionId) {
        this.characterId = characterId;
        this.questionId = questionId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) characterId;
        hash += (int) questionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnswerPK)) {
            return false;
        }
        AnswerPK other = (AnswerPK) object;
        if (this.characterId != other.characterId) {
            return false;
        }
        if (this.questionId != other.questionId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cerebro.AnswerPK[ characterId=" + characterId + ", questionId=" + questionId + " ]";
    }

}
