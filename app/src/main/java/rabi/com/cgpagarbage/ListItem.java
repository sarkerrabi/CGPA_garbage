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

    public void upgradeOneGrade(){
        if (Float.parseFloat(this.grade)!= (float)4.0){
            if (Float.parseFloat(this.grade)== (float) 3.7){
                this.grade =4.0+"";
            }
            if (Float.parseFloat(this.grade)== (float)3.3){
                this.grade =3.7+"";
            }
            if (Float.parseFloat(this.grade) ==(float) 3.0){
                this.grade =3.3+"";
            }
            if (Float.parseFloat(this.grade)==(float) 2.7){
                this.grade =3.0+"";
            }
            if (Float.parseFloat(this.grade) ==(float) 2.3){
                this.grade =2.7+"";
            }
            if (Float.parseFloat(this.grade) ==(float) 2.0){
                this.grade =2.3+"";
            }
            if (Float.parseFloat(this.grade) == (float)1.7){
                this.grade =2.0+"";
            }
            if (Float.parseFloat(this.grade) ==(float) 1.3){
                this.grade =1.7+"";
            }
            if (Float.parseFloat(this.grade) ==(float) 1.0){
                this.grade =1.3+"";
            }
            if (Float.parseFloat(this.grade) ==(float) 0){
                this.grade =1.0+"";
            }
        }
    }

}
