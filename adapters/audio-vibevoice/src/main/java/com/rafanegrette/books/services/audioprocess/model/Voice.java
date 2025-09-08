package com.rafanegrette.books.services.audioprocess.model;

import com.fasterxml.jackson.annotation.JsonValue;


public enum Voice {
    EN_ALICE_WOMAN("en-Alice_woman"),
    EN_CARTER_MAN("en-Carter_man"),
    EN_FRANK_MAN("en-Frank_man"),
    EN_MARY_WOMAN("en-Mary_woman"),
    EN_MAYA_WOMAN("en-Maya_woman");

    private final String voiceModelName;

    Voice(String voiceModelName) {
        this.voiceModelName = voiceModelName;
    }

    @JsonValue
    public String getVoiceModelName() {
        return voiceModelName;
    }
}
