package rabi.com.cgpagarbage;

/**
 * Created by RABI on 10-Apr-16.
 */
public class ListItem {
    public String course;
    public String credit;
    public String grade;

    public ListItem(String course, String credit, String grade) {
        this.course = course;
        this.credit = credit;
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public String getCredit() {
        return credit;
    }

    public String getGrade() {
        return grade;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public float converter(String gre){

        return Float.parseFloat(gre);
    }
}
