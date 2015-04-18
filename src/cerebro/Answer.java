/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cerebro;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Beny
 */
@Entity
@Table(name = "answer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a"),
    @NamedQuery(name = "Answer.findByCharacterId", query = "SELECT a FROM Answer a WHERE a.answerPK.characterId = :characterId"),
    @NamedQuery(name = "Answer.findByQuestionId", query = "SELECT a FROM Answer a WHERE a.answerPK.questionId = :questionId"),
    @NamedQuery(name = "Answer.findByAnswerValue", query = "SELECT a FROM Answer a WHERE a.answerValue = :answerValue"),
    @NamedQuery(name = "Answer.findByAnswerScore", query = "SELECT a FROM Answer a WHERE a.answerScore = :answerScore")})
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnswerPK answerPK;
    @Column(name = "answerValue")
    private Boolean answerValue;
    @Column(name = "answerScore")
    private Integer answerScore;
    @JoinColumn(name = "characterId", referencedColumnName = "characterId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Hero hero;
    @JoinColumn(name = "questionId", referencedColumnName = "questionId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Question question;

    public Answer() {
    }

    public Answer(AnswerPK answerPK) {
        this.answerPK = answerPK;
    }

    public Answer(int characterId, int questionId) {
        this.answerPK = new AnswerPK(characterId, questionId);
    }

    public AnswerPK getAnswerPK() {
        return answerPK;
    }

    public void setAnswerPK(AnswerPK answerPK) {
        this.answerPK = answerPK;
    }

    public Boolean getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(Boolean answerValue) {
        this.answerValue = answerValue;
    }

    public Integer getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(Integer answerScore) {
        this.answerScore = answerScore;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (answerPK != null ? answerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Answer)) {
            return false;
        }
        Answer other = (Answer) object;
        if ((this.answerPK == null && other.answerPK != null) || (this.answerPK != null && !this.answerPK.equals(other.answerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cerebro.Answer[ answerPK=" + answerPK + " ]";
    }

}
