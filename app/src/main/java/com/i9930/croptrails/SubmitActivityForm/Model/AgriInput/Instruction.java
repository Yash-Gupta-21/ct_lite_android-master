package com.i9930.croptrails.SubmitActivityForm.Model.AgriInput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instruction {

@SerializedName("farm_cal_instruction_id")
@Expose
private String farmCalInstructionId;
@SerializedName("farm_cal_activity_id")
@Expose
private String farmCalActivityId;
@SerializedName("instruction")
@Expose
private String instruction;
@SerializedName("sequence_no")
@Expose
private String sequenceNo;

public String getFarmCalInstructionId() {
return farmCalInstructionId;
}

public void setFarmCalInstructionId(String farmCalInstructionId) {
this.farmCalInstructionId = farmCalInstructionId;
}

public String getFarmCalActivityId() {
return farmCalActivityId;
}

public void setFarmCalActivityId(String farmCalActivityId) {
this.farmCalActivityId = farmCalActivityId;
}

public String getInstruction() {
return instruction;
}

public void setInstruction(String instruction) {
this.instruction = instruction;
}

public String getSequenceNo() {
return sequenceNo;
}

public void setSequenceNo(String sequenceNo) {
this.sequenceNo = sequenceNo;
}

}