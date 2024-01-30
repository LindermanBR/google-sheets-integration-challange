package google.sheets.integration.challange.entities;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Student {
    private Integer enrollment;
    private String name;
    private Integer classAbsence;
    private Double grade1;
    private Double grade2;
    private Double grade3;
    private Double avarageGrade;
    private Double finalGradeToApproval;
    private String situation;

    public Student() {
    }

    public Integer getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Integer enrollment) {
        this.enrollment = enrollment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClassAbsence() {
        return classAbsence;
    }

    public void setClassAbsence(Integer classAbsence) {
        this.classAbsence = classAbsence;
    }

    public Double getGrade1() {
        return grade1;
    }

    public void setGrade1(Double grade1) {
        this.grade1 = grade1 / 10;
    }

    public Double getGrade2() {
        return grade2;
    }

    public void setGrade2(Double grade2) {
        this.grade2 = grade2 / 10;
    }

    public Double getGrade3() {
        return grade3;
    }

    public void setGrade3(Double grade3) {
        this.grade3 = grade3 / 10;
    }

    public Double getAvarageGrade() {
        return avarageGrade;
    }

    public void setAvarageGrade() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        String roundedGrade = decimalFormat.format((grade1 + grade2 + grade3) / 3);
        roundedGrade = roundedGrade.replace(",", ".");
        this.avarageGrade = Double.parseDouble(roundedGrade);
    }

    public Double getFinalGradeToApproval() {
        return finalGradeToApproval;
    }

    public void setFinalGradeToApproval(Double finalApprovalGrade) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        String roundedGrade = decimalFormat.format(finalApprovalGrade);
        roundedGrade = roundedGrade.replace(",", ".");

        this.finalGradeToApproval = Double.parseDouble(roundedGrade);
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(int totalSemesterClasses) {
        int maxAbsence = (int) Math.ceil(totalSemesterClasses * 0.25);

        if (avarageGrade == null) {
            setAvarageGrade();
        }
        if (getClassAbsence() > maxAbsence) {
            this.situation = "Reprovado por Falta";
        } else if (avarageGrade < 5.0) {
            this.situation = "Reprovado por Nota";
        } else if (avarageGrade < 7.0) {
            this.situation = "Exame Final";
        } else {
            this.situation = "Aprovado";
        }
    }

    @Override
    public String toString() {
        return "Student [enrollment=" + enrollment + ", name=" + name + ", classAbsence=" + classAbsence + ", grade1="
                + grade1 + ", grade2=" + grade2 + ", grade3=" + grade3 + ", avarageGrade=" + avarageGrade
                + ", finalApprovalGrade=" + finalGradeToApproval + ", situation=" + situation + "]";
    }

}
