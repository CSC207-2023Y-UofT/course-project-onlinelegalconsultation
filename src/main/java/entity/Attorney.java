package entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import businessrule.requestmodel.RegistrationData;
import driver.database.AttorneyRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class Attorney extends UserImp {
    @OneToMany(targetEntity = Question.class, fetch = FetchType.EAGER)
    @JsonProperty(required = true)
    private List<Question> recommendations;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> professionals;

    public Attorney() {
        super();
        recommendations = new ArrayList<>();
        professionals = new HashSet<>();
    }

    public Attorney(Builder builder) {
        super(builder);
    }
    public static class Builder extends UserImp.Builder<Builder> {
        public Builder(RegistrationData data) {
            super(data);
        }

        public Attorney.Builder professionals(Set<String> professionals) {
            this.data.professionals = professionals;
            return this;
        }
        @Override
        protected Attorney.Builder self() {
            return this;
        }

        @Override
        public Attorney build() {
            return new Attorney(this);
        }
    }

    public List<Question> getRecommendations() {return recommendations;}

    public void setRecommendations(ArrayList<Question> recommendations) {
        this.recommendations = recommendations;
    }

    public void addRecommendation(Question question){
        if (! recommendations.contains(question)) {
            recommendations.add(question);
        }
    }

    public Set<String> getProfessionals() {return professionals;}

    public void setProfessionals(Set<String> professionals) {this.professionals = professionals;}

    @Override
    @Transient
    public String getUserType() {
        return "Attorney";
    }


    @Override
    public boolean isQuestionCloseable(Question question){return false;}

    @Override
    public boolean isQuestionSelectable(Question question) {
        boolean isClose = question.isClose();
        boolean isTaken = question.isTaken();
        int takenByAttorney = question.getTakenByAttorney();

        if (isClose) {
            return false;
        } else if (! isTaken) {
            return true;
        } else {
            return takenByAttorney == userId;
        }
    }

    @Override
    public boolean isQuestionReplyable(Question question) {
        if (!question.isClose()){
            if (question.isTaken()) {
                return question.getTakenByAttorney() == userId;
            } else {
                question.setTaken(true);
                question.setTakenByAttorney(userId);
                question.setTakenAt(LocalDate.now());
                addQuestion(question);
                return true;
            }
        }return false;
    }

    @Override
    public boolean isQuestionRateable(Question question) {
        return false;
    }


    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Attorney)) return false;
        Attorney otherAttorney = (Attorney) obj;
        return userId == otherAttorney.userId;
    }

    @Override
    public String toString() {
        return String.format("[Attorney]: %s", name);
    }
}
