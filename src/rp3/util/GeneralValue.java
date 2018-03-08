package rp3.util;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 28/12/2017.
 */

public class GeneralValue {
    @SerializedName("Id")
    private long id;
    @SerializedName("Code")
    private String code;
    @SerializedName("Content")
    private String content;
    @SerializedName("Reference01")
    private String reference01;
    @SerializedName("Reference02")
    private String reference02;
    @SerializedName("Reference03")
    private String reference03;
    @SerializedName("Reference04")
    private String reference04;
    @SerializedName("Reference05")
    private String reference05;
    @SerializedName("Reference06")
    private String reference06;
    @SerializedName("Reference07")
    private String reference07;
    @SerializedName("Reference08")
    private String reference08;
    @SerializedName("Reference09")
    private String reference09;
    @SerializedName("Reference10")
    private String reference10;
    @SerializedName("Locked")
    private String locked;

    //region Encapsulamiento

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReference01() {
        return reference01;
    }

    public void setReference01(String reference01) {
        this.reference01 = reference01;
    }

    public String getReference02() {
        return reference02;
    }

    public void setReference02(String reference02) {
        this.reference02 = reference02;
    }

    public String getReference03() {
        return reference03;
    }

    public void setReference03(String reference03) {
        this.reference03 = reference03;
    }

    public String getReference04() {
        return reference04;
    }

    public void setReference04(String reference04) {
        this.reference04 = reference04;
    }

    public String getReference05() {
        return reference05;
    }

    public void setReference05(String reference05) {
        this.reference05 = reference05;
    }

    public String getReference06() {
        return reference06;
    }

    public void setReference06(String reference06) {
        this.reference06 = reference06;
    }

    public String getReference07() {
        return reference07;
    }

    public void setReference07(String reference07) {
        this.reference07 = reference07;
    }

    public String getReference08() {
        return reference08;
    }

    public void setReference08(String reference08) {
        this.reference08 = reference08;
    }

    public String getReference09() {
        return reference09;
    }

    public void setReference09(String reference09) {
        this.reference09 = reference09;
    }

    public String getReference10() {
        return reference10;
    }

    public void setReference10(String reference10) {
        this.reference10 = reference10;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    //endregion

    @Override
    public String toString() {
        return "GeneralValue{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", content='" + content + '\'' +
                ", reference01='" + reference01 + '\'' +
                ", reference02='" + reference02 + '\'' +
                ", reference03='" + reference03 + '\'' +
                ", reference04='" + reference04 + '\'' +
                ", reference05='" + reference05 + '\'' +
                ", reference06='" + reference06 + '\'' +
                ", reference07='" + reference07 + '\'' +
                ", reference08='" + reference08 + '\'' +
                ", reference09='" + reference09 + '\'' +
                ", reference10='" + reference10 + '\'' +
                ", locked='" + locked + '\'' +
                '}';
    }
}
