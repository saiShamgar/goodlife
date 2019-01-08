package com.example.sss.goodlife.Models;

public class ProgramIds {

    String program_id,program_aim;

    public ProgramIds(String program_id, String program_aim) {
        this.program_id = program_id;
        this.program_aim = program_aim;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getProgram_aim() {
        return program_aim;
    }

    public void setProgram_aim(String program_aim) {
        this.program_aim = program_aim;
    }
}
