package com.example.sss.goodlife.Models;

public class ParticipantsTypeIds {

    String participant_type_id,participant_type;

    public ParticipantsTypeIds(String participant_type_id, String participant_type) {
        this.participant_type_id = participant_type_id;
        this.participant_type = participant_type;
    }

    public String getParticipant_type_id() {
        return participant_type_id;
    }

    public void setParticipant_type_id(String participant_type_id) {
        this.participant_type_id = participant_type_id;
    }

    public String getParticipant_type() {
        return participant_type;
    }

    public void setParticipant_type(String participant_type) {
        this.participant_type = participant_type;
    }
}
